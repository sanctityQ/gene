package org.one.gene.domain.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PackTableHoleConfig;
import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerType;
import org.one.gene.domain.entity.Order.OrderType;
import org.one.gene.domain.entity.PrimerType.PrimerOperationType;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.entity.PrintLabel;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.excel.OrderCaculate;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.repository.UserRepository;
import org.one.gene.util.mail.MailSenderInfo;
import org.one.gene.utils.MapKeyComparator;
import org.one.gene.web.delivery.DeliveryInfo;
import org.one.gene.web.order.OrderInfo;
import org.one.gene.web.statistics.StatisticsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.instruction.reply.EntityReply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Raw;

//Spring Bean的标识.
@Component
//默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class StatisticsService {

    private static Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PrimerProductRepository primerProductRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrimerProductOperationRepository primerProductOperationRepository;
    @Autowired
    private PrimerProductValueRepository primerProductValueRepository;
    @Autowired
    private BoardRepository boardRepository;
    
    /**
     * 导出出库统计
     * @throws IOException 
     * */
	public void exportChuKuTongJi( StatisticsInfo statisticsInfo, Invocation inv) throws IOException {
		
		Sort s = new Sort(Direction.DESC, "createTime");
        Map<String,Object> searchParams = Maps.newHashMap();
        if (!"".equals(statisticsInfo.getCreateStartTime())) {
        	searchParams.put(SearchFilter.Operator.GT+"__createTime",new Date(statisticsInfo.getCreateStartTime()+" 00:00:00"));
        }
        if (!"".equals(statisticsInfo.getCreateEndTime())) {
        	searchParams.put(SearchFilter.Operator.LT+"__createTime",new Date(statisticsInfo.getCreateEndTime()+" 23:59:59"));
        }
		if (!"".equals(statisticsInfo.getOutOrderNo())) {
			searchParams.put(SearchFilter.Operator.EQ+"_outOrderNo",statisticsInfo.getOutOrderNo());
		}
		if (!"".equals(statisticsInfo.getUserCode())) {
			searchParams.put(SearchFilter.Operator.EQ+"_handlerCode",statisticsInfo.getUserCode());
		}
		if (!"".equals(statisticsInfo.getCustomerCode())) {
			searchParams.put(SearchFilter.Operator.EQ+"_customerCode",statisticsInfo.getCustomerCode());
		}
		if (!"".equals(statisticsInfo.getContactsName())) {
			searchParams.put(SearchFilter.Operator.EQ+"_contactsName",statisticsInfo.getContactsName());
		}
		searchParams.put(SearchFilter.Operator.EQ+"_status","1");//订单审核通过
        
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        List<Order> orders = orderRepository.findAll(spec, s);
		
		
		String orderNo = "";//订单号
		Map<String, DeliveryInfo> deliveryInfoMap = new HashMap<String, DeliveryInfo>();//整理好的订单信息
		Map<String, String> customerContactsMap = new HashMap<String, String>();//客户数量，重复的算1，空的累加
		int customerContacts = 0;//计算姓名为空的客户
		DecimalFormat df = new DecimalFormat("0.00");
		
		for (Order tempOrder : orders) {
			orderNo = tempOrder.getOrderNo();
			
			String orderDate     = "";//订货日期
			String companyName = "";//公司名称
			String contactsName = "";//客户联系人
			String customerCode = "";
			String handlerName = "";
			
			Order order = orderRepository.findByOrderNo(orderNo);
			String outOrderNo = order.getOutOrderNo();
			
			//放入客户公司代码和客户姓名，如果姓名为空，则只计算客户代码
			customerContactsMap.put(order.getCustomerCode()+order.getContactsName(), "");
			
			customerCode = order.getCustomerCode();//客户代码
			orderDate = order.getCreateTime().toString();
			contactsName = order.getContactsName();
			handlerName  = order.getHandlerName();
			
			if(orderDate.length()>10){
				orderDate = orderDate.substring(0, 10);
			}
			
			Customer customer = customerRepository.findByCode(customerCode);
			
			if (customer != null) {
				companyName     = customer.getName();
			}
			
			double odTotal = 0.0;//每条OD总量
			int    tbn = 0;//每条碱基的数量
			
			double baseVal  = 0.0;//订单碱基价格
			double modiPrice  = 0.0;//修饰价格
			double purifyVal  = 0.0;//纯化价格
			String purifyType = "";//纯化类型
			String modiFiveType = "";//5修饰
			String modiThreeType = "";//3修饰
			String modiMidType = "";//中间修饰
			String modiSpeType = "";//特殊单体
			
			DeliveryInfo deliveryInfo = new DeliveryInfo();
			
			
			for (PrimerProduct primerProduct : order.getPrimerProducts()) {
				odTotal = 0.0;
				tbn = 0;
				
				for (PrimerProductValue primerProductValue : primerProduct.getPrimerProductValues()) {
					PrimerValueType type = primerProductValue.getType();
					if (type.equals(PrimerValueType.baseCount)) {// 碱基数
						tbn = primerProductValue.getValue().intValue();
					}else if(type.equals(PrimerValueType.odTotal)){//od总量
						odTotal = primerProductValue.getValue().doubleValue();
					}
				}
				
				baseVal      = primerProduct.getBaseVal().doubleValue();//订单碱基价格
				modiPrice    = primerProduct.getModiPrice().doubleValue();//修饰价格
				purifyVal    = primerProduct.getPurifyVal().doubleValue();//纯化价格
				modiFiveType = primerProduct.getModiFiveType();
				modiThreeType= primerProduct.getModiThreeType();
				modiMidType  = primerProduct.getModiMidType();
				modiSpeType  = primerProduct.getModiSpeType();
				purifyType   = primerProduct.getPurifyType();
				
				//DNA合成
				String dnaSrt = orderDate+orderNo+"DNA合成_"+df.format(baseVal);
				
				if (deliveryInfoMap.get(dnaSrt) != null) {
					deliveryInfo = (DeliveryInfo)deliveryInfoMap.get(dnaSrt);
					deliveryInfo.setCountNum(deliveryInfo.getCountNum()+1);
					deliveryInfo.setCount(deliveryInfo.getCount() + tbn);
					deliveryInfo.setMoney(df.format(deliveryInfo.getCount() * deliveryInfo.getPrice()));
					
					deliveryInfoMap.put(dnaSrt, deliveryInfo);
				}else{
					deliveryInfo = new DeliveryInfo();
					deliveryInfo.setExtendStr1(orderDate);//订货日期
					deliveryInfo.setExtendStr2(companyName);//公司名称
					deliveryInfo.setExtendStr3(contactsName);//客户姓名
					deliveryInfo.setExtendStr4(outOrderNo);//订单号
					deliveryInfo.setExtendStr5(handlerName);//业务员
					deliveryInfo.setDeliveryName("DNA合成");//货物名称
					deliveryInfo.setCountNum(1);//条数
					deliveryInfo.setOdTotal(odTotal);//合成量
					deliveryInfo.setMeasurement("bp");//计量单位
					deliveryInfo.setCount(tbn);//数量
					deliveryInfo.setPrice(baseVal);//单价
					deliveryInfo.setMoney(df.format(deliveryInfo.getCount() * deliveryInfo.getPrice()));//金额
					deliveryInfo.setExtendStr9("0");//引物类型：0 普通引入 1 修饰引物 2 纯化引物
					
					deliveryInfoMap.put(dnaSrt, deliveryInfo);
				}
				
				//修饰和纯化
				String modiStr    = "";
				String modiStrKey = "";
				double price = 0.0;
				boolean haveModi = false;//是否含有修饰
				boolean haveHPLC = false;//是否含有HPLC
				if (!"".equals(modiFiveType) || !"".equals(modiThreeType) || !"".equals(modiMidType) || !"".equals(modiSpeType)) {
					haveModi = true;
					
					modiStr = "(";
					if (!"".equals(modiFiveType)) {
						modiStr += modiFiveType + ",";
					}
					if (!"".equals(modiThreeType)) {
						modiStr += modiThreeType + ",";
					}
					if (!"".equals(modiMidType)) {
						modiStr += modiMidType + ",";
					}
					if (!"".equals(modiSpeType)) {
						modiStr += modiSpeType + ",";
					}
					modiStr = modiStr.substring(0, modiStr.length()-1);
					modiStr += ")";
					
					modiStrKey = modiStr + "_" + df.format(modiPrice);
					price = Double.parseDouble(df.format(modiPrice));
				} else {
					if (purifyVal > 0) {
						if ("HPLC".equals(purifyType)) {
							haveHPLC = true;
						}
						modiStr      = purifyType;
						modiStrKey = purifyType + "_" + df.format(purifyVal);
						price = Double.parseDouble(df.format(purifyVal));
					}
				}
				if (!"".equals(modiStrKey)) {
					
					modiStrKey = orderDate+orderNo+modiStrKey;
					
					if (deliveryInfoMap.get(modiStrKey) != null) {
						deliveryInfo = (DeliveryInfo)deliveryInfoMap.get(modiStrKey);
						deliveryInfo.setCountNum(deliveryInfo.getCountNum()+1);
						deliveryInfo.setCount(deliveryInfo.getCountNum());
						deliveryInfo.setMoney(df.format(deliveryInfo.getCount() * deliveryInfo.getPrice()));
						
						deliveryInfoMap.put(modiStrKey, deliveryInfo);
					}else{
						deliveryInfo = new DeliveryInfo();
						deliveryInfo.setExtendStr1(orderDate);//订货日期
						deliveryInfo.setExtendStr2(companyName);//公司名称
						deliveryInfo.setExtendStr3(contactsName);//客户姓名
						deliveryInfo.setExtendStr4(outOrderNo);//订单号
						deliveryInfo.setExtendStr5(handlerName);//业务员
						deliveryInfo.setDeliveryName(modiStr);
						deliveryInfo.setCountNum(1);
						deliveryInfo.setOdTotal(odTotal);
						deliveryInfo.setMeasurement("条");
						deliveryInfo.setCount(deliveryInfo.getCountNum());
						deliveryInfo.setPrice(price);
						deliveryInfo.setMoney(df.format(deliveryInfo.getCount() * deliveryInfo.getPrice()));
						if (haveModi) {
							deliveryInfo.setExtendStr9("1");//引物类型：0 普通引入 1 修饰引物 2 纯化引物
						} 
						if (haveHPLC) {
							deliveryInfo.setExtendStr9("2");//引物类型：0 普通引入 1 修饰引物 2 纯化引物
						}
						deliveryInfoMap.put(modiStrKey, deliveryInfo);
					}
				}
				
			}
			
		}
		
	    //累计客户数量
		customerContacts = customerContactsMap.size();
		int allOrderCount = orders.size();
		int tiaoshuCount_pt = 0;//普通引物 条数之和
		int tiaoshuCount_xs = 0;//修饰引物 条数之和
		int tiaoshuCount_ch = 0;//纯化引物 条数之和
		double hechengliangSum = 0;//合成量之和
		int shuliangCount = 0;//数量之和
		double danjiaSum_pt = 0;//普通引物 单价总和
		double danjiaSum_xs = 0;//修饰引物 单价总和
		double danjiaSum_ch = 0;//纯化引物 单价总和
		
		//形成Excel
		String templetName = "cktjTemplate.xls";
		String strFileName = System.currentTimeMillis()+".xls";
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"statistics"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
    	HSSFCellStyle style_center = workbook.createCellStyle();
    	style_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	style_center.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框  
    	style_center.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框  
    	style_center.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框  
    	style_center.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框  
		
    	Map<String, DeliveryInfo> resultMap = sortMapByKeyDI(deliveryInfoMap);    //按Key进行排序 
    	
		int startRow = 2;//从第2行开始
		double totalMoney = 0.0;//合计
		if (resultMap!=null){
			
			for (String key : resultMap.keySet()) {
				DeliveryInfo dis = (DeliveryInfo)resultMap.get(key);
				
				row = sheet.createRow(startRow);
				
				cell = row.createCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getExtendStr1());//订货日期
				
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getExtendStr2());//公司名称
				
				cell = row.createCell(2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getExtendStr3());//客户姓名
				
				cell = row.createCell(3);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getExtendStr4());//订单号
				
				cell = row.createCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getExtendStr5());//业务员
				
				cell = row.createCell(5);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getDeliveryName());//存货名称
				
				cell = row.createCell(6);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getCountNum());//条数
				
				cell = row.createCell(7);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getOdTotal()+"OD");//合成量
				
				hechengliangSum += dis.getOdTotal();//合成量之和
				
				cell = row.createCell(8);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getMeasurement());//计量单位
				
				cell = row.createCell(9);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getCount());//数量
				
				shuliangCount += dis.getCount();//数量之和
				
				cell = row.createCell(10);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getPrice());//单价
				
				if ("0".equals(dis.getExtendStr9())) {// 普通引物
					tiaoshuCount_pt += dis.getCountNum();// 条数之和
					danjiaSum_pt += dis.getPrice();// 单价总和
				} else if ("1".equals(dis.getExtendStr9())) {// 修饰引物
					tiaoshuCount_xs += dis.getCountNum();// 条数之和
					danjiaSum_xs += dis.getPrice();// 单价总和
				} else if ("2".equals(dis.getExtendStr9())) {// 纯化引物
					tiaoshuCount_ch += dis.getCountNum();// 条数之和
					danjiaSum_ch += dis.getPrice();// 单价总和
				}
				
				cell = row.createCell(11);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(dis.getMoney());//金额
				
				cell = row.createCell(12);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				
				startRow = startRow +1;
				
				if(!"".equals(dis.getMoney())){
					totalMoney += Double.parseDouble(dis.getMoney());
				}
			}
		}
		
		double avgePrice_pt = 0;//单价底下平均数为：所有单价总和除以条数总数（不包括修饰的和HPLC）
		if (tiaoshuCount_pt != 0) {
			avgePrice_pt = danjiaSum_pt / tiaoshuCount_pt;
		}
		double avgePrice_xs = 0;//单价底下平均数为：所有单价总和除以条数总数（修饰的)
		if (tiaoshuCount_xs != 0) {
			 avgePrice_xs = danjiaSum_xs/tiaoshuCount_xs;
		}
		double avgePrice_ch = 0;//单价底下平均数为：所有单价总和除以条数总数（HPLC）
		if (tiaoshuCount_ch != 0) {
			avgePrice_ch = danjiaSum_ch/tiaoshuCount_ch;
		}
		
		row = sheet.createRow(startRow);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("起始日期");
		
		cell = row.createCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(statisticsInfo.getCreateStartTime());

		for (int a=2;a<13;a++){
			cell = row.createCell(a);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);	
		}
	
		row = sheet.createRow(startRow+1);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("到");
		
		for (int a=1;a<13;a++){
			cell = row.createCell(a);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);	
		}
		
		row = sheet.createRow(startRow+2);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("终止日期");
		cell = row.createCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(statisticsInfo.getCreateEndTime());
		cell = row.createCell(2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(customerContacts);//客戶姓名总数
		cell = row.createCell(3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(allOrderCount);//订单总数
		cell = row.createCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell = row.createCell(5);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell = row.createCell(6);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(tiaoshuCount_pt);//条数总数
		cell = row.createCell(7);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(hechengliangSum);//合成量总数
		cell = row.createCell(8);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell = row.createCell(9);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(shuliangCount);//数量总数
		cell = row.createCell(10);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(df.format(avgePrice_pt));//单价平均值
		cell = row.createCell(11);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(totalMoney);//金额之和
		cell = row.createCell(12);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		
		row = sheet.createRow(startRow+3);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总计");
		cell = row.createCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell = row.createCell(2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");
		cell = row.createCell(3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");
		cell = row.createCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell = row.createCell(5);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell = row.createCell(6);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");
		cell = row.createCell(7);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");
		cell = row.createCell(8);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell = row.createCell(9);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");
		cell = row.createCell(10);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("平均值");
		cell = row.createCell(11);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");
		cell = row.createCell(12);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		
		row = sheet.createRow(startRow+4);
		cell = row.createCell(9);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("HPLC均价");
		cell = row.createCell(10);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(df.format(avgePrice_ch));//单价平均值：HPLC均价
		
		row = sheet.createRow(startRow+5);
		cell = row.createCell(9);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("修饰均价");
		cell = row.createCell(10);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(df.format(avgePrice_xs));//单价平均值：修饰均价
		
        //输出文件到客户端
        HttpServletResponse response = inv.getResponse();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
        OutputStream out=response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }

    /**
     * 导出对账单
     * @throws IOException 
     * */
	public void exportDuiZhangDan( StatisticsInfo statisticsInfo, Invocation inv) throws IOException {
		
		String createStartTime = statisticsInfo.getCreateStartTime()+" 00:00:00";
		String createEndTime   = statisticsInfo.getCreateEndTime()+" 23:59:59";
        String productNoPrefix = statisticsInfo.getProductNoPrefix();
		if (!StringUtils.isBlank(productNoPrefix)) {
			productNoPrefix = productNoPrefix.toUpperCase() + "%";
        }
		
		List<Order> orders = orderRepository.queryqDuiZhangDan(createStartTime,
				createEndTime, statisticsInfo.getOutOrderNo(),
				statisticsInfo.getUserCode(), statisticsInfo.getCustomerCode(),
				statisticsInfo.getContactsName(),
				statisticsInfo.getProductNo(),
				productNoPrefix
				);
		
		
		//形成Excel
		String templetName = "dzdTemplate.xls";
		String strFileName = System.currentTimeMillis()+".xls";
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"statistics"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
    	HSSFCellStyle style_center = workbook.createCellStyle();
    	style_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	style_center.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框  
    	style_center.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框  
    	style_center.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框  
    	style_center.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框  
    	
    	String orderNo = "";//订单号
    	int startRow = 2;//从第2行开始
    	
		for (Order tempOrder : orders) {
			orderNo = tempOrder.getOrderNo();
			
			String orderDate     = "";//订货日期
			String companyName = "";//公司名称
			String contactsName = "";//客户联系人
			String customerCode = "";
			String handlerName = "";
			
			Order order = orderRepository.findByOrderNo(orderNo);
			
			customerCode = order.getCustomerCode();//客户代码
			orderDate = order.getCreateTime().toString();
			contactsName = order.getContactsName();
			handlerName  = order.getHandlerName();
			String outOrderNo = order.getOutOrderNo();
			
			if(orderDate.length()>10){
				orderDate = orderDate.substring(0, 10);
			}
			
			Customer customer = customerRepository.findByCode(customerCode);
			
			if (customer != null) {
				companyName     = customer.getName();
			}
			
			double odTotal = 0.0;//每条OD总量
			double nmolTotal = 0.0;//每条nmole总量
			int    tbn = 0;//每条碱基的数量
			
			double baseVal    = 0.0;//订单碱基价格
			double modiPrice  = 0.0;//修饰价格
			double purifyVal  = 0.0;//纯化价格
			double totalVal   = 0.0;//总价格
			String purifyType = "";//纯化类型
			String modiFiveType = "";//5修饰
			String modiThreeType = "";//3修饰
			String modiMidType = "";//中间修饰
			String modiSpeType = "";//特殊单体
			
			for (PrimerProduct pp : order.getPrimerProducts()) {
				odTotal = 0.0;
				nmolTotal = 0.0;
				tbn = 0;
				
				//生产编号不为空，只展现此生产编号的数据
				if (!"".equals(statisticsInfo.getProductNo()) && !pp.getProductNo().equals(statisticsInfo.getProductNo())) {
					continue;
				}
				
				for (PrimerProductValue primerProductValue : pp.getPrimerProductValues()) {
					PrimerValueType type = primerProductValue.getType();
					if (type.equals(PrimerValueType.baseCount)) {// 碱基数
						tbn = primerProductValue.getValue().intValue();
					}else if(type.equals(PrimerValueType.odTotal)){//od总量
						odTotal = primerProductValue.getValue().doubleValue();
					}else if(type.equals(PrimerValueType.nmolTotal)){//nmol总量
						nmolTotal = primerProductValue.getValue().doubleValue();
					}
				}
				
				baseVal      = pp.getBaseVal().doubleValue();//订单碱基价格
				modiPrice    = pp.getModiPrice().doubleValue();//修饰价格
				purifyVal    = pp.getPurifyVal().doubleValue();//纯化价格
				totalVal     = pp.getTotalVal().doubleValue();//总价格
				modiFiveType = pp.getModiFiveType();
				modiThreeType= pp.getModiThreeType();
				modiMidType  = pp.getModiMidType();
				modiSpeType  = pp.getModiSpeType();
				purifyType   = pp.getPurifyType();
				
				//修饰和纯化
				String modiStr    = "";
				if (!"".equals(modiFiveType) || !"".equals(modiThreeType) || !"".equals(modiMidType) || !"".equals(modiSpeType)) {
					modiStr = "(";
					if (!"".equals(modiFiveType)) {
						modiStr += modiFiveType + ",";
					}
					if (!"".equals(modiThreeType)) {
						modiStr += modiThreeType + ",";
					}
					if (!"".equals(modiMidType)) {
						modiStr += modiMidType + ",";
					}
					if (!"".equals(modiSpeType)) {
						modiStr += modiSpeType + ",";
					}
					modiStr = modiStr.substring(0, modiStr.length()-1);
					modiStr += ")";

				}


				row = sheet.createRow(startRow);
				
				cell = row.createCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(orderDate);//订货日期
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(companyName);//公司名称
				cell = row.createCell(2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(contactsName);//客户姓名
				cell = row.createCell(3);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(outOrderNo);//订单号（外部）
				cell = row.createCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(handlerName);//业务员
				cell = row.createCell(5);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(pp.getProductNo());//生产编号
				cell = row.createCell(6);
			    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    cell.setCellStyle(style_center);
				cell.setCellValue(pp.getPrimeName());//引物名称
				cell = row.createCell(7);
			    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    cell.setCellStyle(style_center);
				cell.setCellValue(pp.getGeneOrderMidi());//序列
				cell = row.createCell(8);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(tbn);//碱基
				cell = row.createCell(9);
			    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			    cell.setCellStyle(style_center);
				cell.setCellValue(odTotal);//OD总量
				cell = row.createCell(10);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(nmolTotal);//nmol总量
				cell = row.createCell(11);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(purifyType);//纯化方式
				cell = row.createCell(12);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(modiStr);//修饰
				cell = row.createCell(13);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(baseVal);//碱基单价
				cell = row.createCell(14);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(purifyVal);//纯化单价
				cell = row.createCell(15);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(modiPrice);//修饰单价
				cell = row.createCell(16);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(totalVal);//总价
				cell = row.createCell(17);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				
				startRow = startRow +1;
				
			}
		}
		
        //输出文件到客户端
        HttpServletResponse response = inv.getResponse();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
        OutputStream out=response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
	

    /**
     * 导出引物进度表
     * @throws IOException 
     * @throws ParseException 
     * */
	public void exportYinWuJinDuBiao( StatisticsInfo statisticsInfo, Invocation inv) throws IOException, ParseException {
		
		SimpleDateFormat df1 = new SimpleDateFormat("MMdd");//设置日期格式 月日
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式 年月日
		SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式 年月日时分秒
		Date today = new Date();//当前时间 
		
		String customerFlag    = statisticsInfo.getCustomerFlag();
		String customerCode    = statisticsInfo.getCustomerCode();
		String createStartTime = statisticsInfo.getCreateStartTime()+" 00:00:00";
		String createEndTime   = statisticsInfo.getCreateEndTime()+" 23:59:59";

		List<Order> orders = orderRepository.queryYinWuJInDu(createStartTime, createEndTime, customerFlag, customerCode);
		
		String zixiCode = "";//梓熙生物 公司代码
		String zixiName = "";//梓熙生物 公司名称
		
		List<Customer> customers = customerRepository.seachHaveZiXi();
		if (customers != null && customers.size() > 0) {
			Customer customerTemp = (Customer)customers.get(0);
			zixiCode = customerTemp.getCode();
			zixiName = customerTemp.getName();
		}
		
		//形成Excel
		String templetName = "ywjdTemplate.xls";
		String strFileName = System.currentTimeMillis()+".xls";
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"statistics"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
    	HSSFCellStyle style_center = workbook.createCellStyle();
    	style_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	style_center.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框  
    	style_center.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框  
    	style_center.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框  
    	style_center.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框  
    	
    	String orderNo = "";//订单号
    	int startRow = 2;//从第2行开始
    	Map<String, StatisticsInfo> siMap = new TreeMap<String, StatisticsInfo>();//客户的生产引物信息
    	
		for (Order tempOrder : orders) {
			orderNo = tempOrder.getOrderNo();
			
			String c_Code = "";//公司代码
			String c_Name = "";//公司名称
			String c_Flag = "";//公司性质标志
			
			Order order = orderRepository.findByOrderNo(orderNo);
			Date createTime = order.getCreateTime();//订单导入时间
			
			Calendar   calendar   =   new   GregorianCalendar();
			calendar.setTime(createTime);
			calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 

			Date createTime_nextDay = calendar.getTime();//订单导入时间的第二天
			
	        String orderTime_md = df1.format(createTime);
	        orderTime_md = orderTime_md.substring(0, 2)+"月"+orderTime_md.substring(2)+"日";
	        
	        String orderTime_ymd = df2.format(createTime);
	        String orderTime_ymd_nextDay = df2.format(createTime_nextDay);
			
	        Date  time_0 = df3.parse(orderTime_ymd + " 00:00:00");
	        Date  time_20 = df3.parse(orderTime_ymd + " 20:00:00");
	        
	        Date  nextDay_0830 = df3.parse(orderTime_ymd_nextDay + " 08:30:00");
	        Date  nextDay_1530 = df3.parse(orderTime_ymd_nextDay + " 15:30:00");
	        Date  nextDay_2359 = df3.parse(orderTime_ymd_nextDay + " 23:59:59");
	        
			c_Code = order.getCustomerCode();//客户代码
			
			Customer customer = customerRepository.findByCode(c_Code);
			
			if (customer != null) {
				c_Name = customer.getName();
				c_Flag = customer.getCustomerFlag();
				if ("2".equals(c_Flag)) {
					c_Code = zixiCode;
					c_Name = zixiName;
				}
			}
			
			int count1 = 0;//引物条数
			int count2 = order.getTbnTotal().intValue();//碱基总数
			int count3 = 0;//修饰和HPLC条数
			int count4 = 0;//修饰和HPLC碱基数
			int count5 = 0;//普通引物  正常发货数
			int count6 = 0;//普通引物  晚发货数
			int count7 = 0;//普通引物 内部重合数
			int count8 = 0;//普通引物 外部重合条数
			int count9 = 0;//修饰和HPLC内部重合数
			int count10= 0;//修饰和HPLC外部重合数
			
			for (PrimerProduct pp : order.getPrimerProducts()) {
				int tbn = 0;//每条生产数据的碱基数
				for (PrimerProductValue primerProductValue : pp.getPrimerProductValues()) {
					PrimerValueType type = primerProductValue.getType();
					if (type.equals(PrimerValueType.baseCount)) {// 碱基数
						tbn = primerProductValue.getValue().intValue();
					}
				}
				
				List<PrimerProductOperation> ppos= primerProductOperationRepository.getInfoByPrimerProductID(pp.getId());
				//内部重合
				boolean neibu = false;
				//外部重合
				boolean waibu = false;
				
				//是否已发货
				boolean fahuo = false;
				Date fahuoTime = null;
				
				for (PrimerProductOperation ppo : ppos) {
					if (ppo.getBackTimes() > 0) {
						neibu = true;
					}
					if (ppo.getType() == PrimerType.PrimerOperationType.backSuccess) {//发货召回成功
						waibu = true;
					}
					if (ppo.getType() == PrimerType.PrimerOperationType.deliverySuccess) {//发货成功
						fahuo = true;
						fahuoTime = ppo.getCreateTime();
					}
					
				}
				
				//计算比较每条生产数据的时间进度
				boolean primerType = false;//普通引物 还是修饰或HPLC的标志
				
				count1 = count1 + 1;//引物条数
				String modiFiveType = pp.getModiFiveType();
				String modiThreeType= pp.getModiThreeType();
				String modiMidType  = pp.getModiMidType();
				String modiSpeType  = pp.getModiSpeType();
				String purifyType   = pp.getPurifyType();
				
				if (purifyType.equals("HPLC") || !"".equals(modiFiveType)
						|| !"".equals(modiThreeType) || !"".equals(modiMidType)
						|| !"".equals(modiSpeType)) {
					primerType = true;
				}
				
				if (primerType) {
					count3 = count3 + 1;
					count4 = count4 + tbn;
					if (neibu) {
						count9 = count9 +1;
					}
					if (waibu) {
						count10 = count10 +1;
					}
					
				} else {//普通引物
					
					if (neibu) {
						count7 = count7 +1;
					}
					if (waibu) {
						count8 = count8 +1;
					}
					Date  compareTime = null;
					if (tbn <= 40) {// 40个碱基数以内
						compareTime = nextDay_0830;
					} else {
						compareTime = nextDay_1530;
					}
					
					if (createTime.getTime() >= time_0.getTime()
							&& createTime.getTime() <= time_20.getTime()) {// 0~20点导入的
						if (fahuo) {//已经发货
							if(compareTime.getTime()>=fahuoTime.getTime()){
								count5 = count5 + 1;
							}else{
								count6 = count6 + 1;
							}
							
						}else{//没有发货
							if(today.getTime()>compareTime.getTime()){
							count6 = count6 + 1;
							}
						}
					} else {// 20~24点导入的
						if (fahuo) {//已经发货
							if(nextDay_2359.getTime()>=fahuoTime.getTime()){
								count5 = count5 + 1;
							}else{
								count6 = count6 + 1;
							}
							
						}else{//没有发货
							if(today.getTime()>nextDay_2359.getTime()){
								count6 = count6 + 1;
							}
						}
					}
					
				}
				
			}
			
			if (siMap.get(orderTime_md+"_"+c_Code) == null) {
				StatisticsInfo si = new StatisticsInfo();
				si.setOrderTime(orderTime_md);
				si.setCustomerName(c_Name);
				si.setCount1(count1);
				si.setCount2(count2);
				si.setCount3(count3);
				si.setCount4(count4);
				si.setCount5(count5);
				si.setCount6(count6);
				si.setCount7(count7);
				si.setCount8(count8);
				si.setCount9(count9);
				si.setCount10(count10);
				
				siMap.put(orderTime_md+"_"+c_Code, si);
			} else {
				StatisticsInfo si = siMap.get(orderTime_md+"_"+c_Code);
				si.setCount1(count1+si.getCount1());
				si.setCount2(count2+si.getCount2());
				si.setCount3(count3+si.getCount3());
				si.setCount4(count4+si.getCount4());
				si.setCount5(count5+si.getCount5());
				si.setCount6(count6+si.getCount6());
				si.setCount7(count7+si.getCount7());
				si.setCount8(count8+si.getCount8());
				si.setCount9(count9+si.getCount9());
				si.setCount10(count10+si.getCount10());
				siMap.put(orderTime_md+"_"+c_Code, si);
			}
			
		}
		
		int Count1All = 0;
		int Count2All = 0;
		int Count3All = 0;
		int Count4All = 0;
		int Count5All = 0;
		int Count6All = 0;
		int Count7All = 0;
		int Count8All = 0;
		int Count9All = 0;
		int Count10All = 0;
		
		Map<String, StatisticsInfo> resultMap = sortMapByKey(siMap);    //按Key进行排序 
		
		if (resultMap != null) {
			for (String key : resultMap.keySet()) {
				StatisticsInfo si = (StatisticsInfo)siMap.get(key);
				
				row = sheet.createRow(startRow);
				
				cell = row.createCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getOrderTime());//订单日期
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCustomerName());//公司名称
				cell = row.createCell(2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount1());//引物条数
				Count1All += si.getCount1();
				cell = row.createCell(3);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount2());//碱基总数
				Count2All += si.getCount2();
				cell = row.createCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount3());//修饰和HPLC条数
				Count3All += si.getCount3();
				cell = row.createCell(5);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount4());//修饰和HPLC碱基数
				Count4All += si.getCount4();
				cell = row.createCell(6);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount5());//正常发货数
				Count5All += si.getCount5();
				cell = row.createCell(7);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount6());//晚发货数
				Count6All += si.getCount6();
				cell = row.createCell(8);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount7());//内部重合数
				Count7All += si.getCount7();
				cell = row.createCell(9);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount8());//外部重合条数
				Count8All += si.getCount8();
				cell = row.createCell(10);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount9());//修饰和HPLC内部重合数
				Count9All += si.getCount9();
				cell = row.createCell(11);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCount10());//修饰和HPLC外部重合数
				Count10All += si.getCount10();
				
				startRow = startRow +1;
			}
		}
		
		row = sheet.createRow(startRow);
		
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("合计");//公司名称
		cell = row.createCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell = row.createCell(2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(Count1All);//引物条数	
		cell = row.createCell(3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(Count2All);//碱基总数
		cell = row.createCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(Count3All);//修饰和HPLC条数
		cell = row.createCell(5);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(Count4All);//修饰和HPLC碱基数
		cell = row.createCell(6);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(Count5All);//正常发货数
		cell = row.createCell(7);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellStyle(style_center);
		cell.setCellValue(Count6All);//晚发货数
		cell = row.createCell(8);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellStyle(style_center);
		cell.setCellValue(Count7All);//内部重合数
		cell = row.createCell(9);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(Count8All);//外部重合条数
		cell = row.createCell(10);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellStyle(style_center);
		cell.setCellValue(Count9All);//修饰和HPLC内部重合数
		cell = row.createCell(11);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellStyle(style_center);
		cell.setCellValue(Count10All);//修饰和HPLC外部重合数
		
        //输出文件到客户端
        HttpServletResponse response = inv.getResponse();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
        OutputStream out=response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
	
    /** 
     * 使用 Map按key进行排序 
     * @param map StatisticsInfo
     * @return 
     */  
    public static Map<String, StatisticsInfo> sortMapByKey(Map<String, StatisticsInfo> map) {
        if (map == null || map.isEmpty()) {  
            return null;  
        }  
        Map<String, StatisticsInfo> sortMap = new TreeMap<String, StatisticsInfo>(new MapKeyComparator());
        sortMap.putAll(map);  
        return sortMap;  
    }
    
    /** 
     * 使用 Map按key进行排序 
     * @param map  DeliveryInfo
     * @return 
     */  
    public static Map<String, DeliveryInfo> sortMapByKeyDI(Map<String, DeliveryInfo> map) {
        if (map == null || map.isEmpty()) {  
            return null;  
        }  
        Map<String, DeliveryInfo> sortMap = new TreeMap<String, DeliveryInfo>(new MapKeyComparator());
        sortMap.putAll(map);  
        return sortMap;  
    }
    
    /**
     * 导出修饰进度表
     * @throws IOException 
     * @throws ParseException 
     * */
	public void exportXiuShiJinDuBiao( StatisticsInfo statisticsInfo, Invocation inv) throws IOException, ParseException {
		
		SimpleDateFormat df1 = new SimpleDateFormat("MMdd");//设置日期格式 月日
		
		String createStartTime = statisticsInfo.getCreateStartTime()+" 00:00:00";
		String createEndTime   = statisticsInfo.getCreateEndTime()+" 23:59:59";
        String midType = "";
        String speType = "";
		if (!StringUtils.isBlank(statisticsInfo.getModiMidType())) {
			midType = "%" + statisticsInfo.getModiMidType() + "%";
        }
		if (!StringUtils.isBlank(statisticsInfo.getModiSpeType())) {
			speType = "%" + statisticsInfo.getModiSpeType() + "%";
        }
		
		List<PrimerProduct> primerProducts = primerProductRepository
				.queryXiuShiJInDu(createStartTime, createEndTime,
						statisticsInfo.getOutOrderNo(),
						statisticsInfo.getProductNo(),
						statisticsInfo.getContactsName(),
						statisticsInfo.getCustomerFlag(),
						statisticsInfo.getCustomerCode(),
						statisticsInfo.getModiFiveType(),
						statisticsInfo.getModiThreeType(), midType, speType);
		
		
		//形成Excel
		String templetName = "xsjdTemplate.xls";
		String strFileName = System.currentTimeMillis()+".xls";
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"statistics"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
    	HSSFCellStyle style_center = workbook.createCellStyle();
    	style_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	style_center.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框  
    	style_center.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框  
    	style_center.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框  
    	style_center.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框  
    	
    	String orderNo = "";//订单号
    	int startRow = 2;//从第2行开始
    	Map<String, StatisticsInfo> siMap = new TreeMap<String, StatisticsInfo>();//客户的生产引物信息
    	
    	for (PrimerProduct ppTemp : primerProducts) {
    		
    		PrimerProduct pp = primerProductRepository.findOne(ppTemp.getId());
    		
			orderNo = pp.getOrder().getOrderNo();
			Order order = orderRepository.findByOrderNo(orderNo);
			Date createTime = order.getCreateTime();//订单导入时间

	        String orderTime_md = df1.format(createTime);
	        orderTime_md = orderTime_md.substring(0, 2)+"月"+orderTime_md.substring(2)+"日";
	        
			String c_Code       = order.getCustomerCode();//客户代码
			String c_Name       = order.getCustomerName();//客户公司名称
			String contactsName = order.getContactsName();//客户联系人姓名
			String outOrderNo   = order.getOutOrderNo();//外部订单号
			
			int tbn = 0;//每条生产数据的碱基数
			double odTB = 0;
			double nmolTB = 0;
			int tb = 0;
			for (PrimerProductValue primerProductValue : pp.getPrimerProductValues()) {
				PrimerValueType type = primerProductValue.getType();
				if (type.equals(PrimerValueType.baseCount)) {// 碱基数
					tbn = primerProductValue.getValue().intValue();
				}else if(type.equals(PrimerValueType.odTB)){
					odTB = primerProductValue.getValue().doubleValue();
				}else if(type.equals(PrimerValueType.nmolTB)){
					nmolTB = primerProductValue.getValue().doubleValue();
				}else if(type.equals(PrimerValueType.tb)){
					tb = primerProductValue.getValue().intValue();
				}
			}
			
			List<PrimerProductOperation> ppos= primerProductOperationRepository.getInfoByPrimerProductID(pp.getId());
			
			//是否已发货
			boolean fahuo = false;
			Date fahuoTime = null;
			
			for (PrimerProductOperation ppo : ppos) {
				if (ppo.getType() == PrimerType.PrimerOperationType.deliverySuccess) {//发货成功
					fahuo = true;
					fahuoTime = ppo.getCreateTime();
				}
			}
			
			String modiType = "";
			String modiFiveType = pp.getModiFiveType();
			String modiThreeType= pp.getModiThreeType();
			String modiMidType  = pp.getModiMidType();
			String modiSpeType  = pp.getModiSpeType();
			
			if (!"".equals(modiFiveType) || !"".equals(modiThreeType) || !"".equals(modiMidType) || !"".equals(modiSpeType)) {
				modiType = "(";
				if (!"".equals(modiFiveType)) {
					modiType += "5'"+modiFiveType + ",";
				}
				if (!"".equals(modiMidType)) {
					modiType += modiMidType + ",";
				}
				if (!"".equals(modiSpeType)) {
					modiType += modiSpeType + ",";
				}
				if (!"".equals(modiThreeType)) {
					modiType += "3'"+modiThreeType + ",";
				}
				modiType = modiType.substring(0, modiType.length()-1);
				modiType += ")";

			} else {
				modiType = "HPLC";
			}
			
			StatisticsInfo si = new StatisticsInfo();
			si.setOrderTime(orderTime_md);//订货日期
			si.setCustomerName(c_Name);//公司名称
			si.setContactsName(contactsName);//客户姓名
			si.setOutOrderNo(outOrderNo);//外部订单号
			si.setProductNo(pp.getProductNo());//生产编号
			si.setModiType(modiType);//修饰类型
			si.setTbn(tbn);//碱基数
			if (pp.isOrderUpType(Order.OrderType.od)) {
				si.setTb(odTB+"OD*"+tb);//合成规格
			}else{
				si.setTb(nmolTB+"nmol*"+tb);//合成规格
			}
			si.setPrimerType(pp.getOperationType().desc());//状态
	        String deliveryDate = "未发货";
	        if(fahuo){
	        	deliveryDate = df1.format(fahuoTime);
	        	deliveryDate = deliveryDate.substring(0, 2)+"月"+deliveryDate.substring(2)+"日";
	        }
			si.setDeliveryDate(deliveryDate);//发货日期
			
			siMap.put(orderTime_md+"_"+c_Code+"_"+pp.getProductNo(), si);
		}
    	
		Map<String, StatisticsInfo> resultMap = sortMapByKey(siMap);    //按Key进行排序 
		
		if (resultMap != null) {
			for (String key : resultMap.keySet()) {
				StatisticsInfo si = (StatisticsInfo)siMap.get(key);
				
				row = sheet.createRow(startRow);
				
				cell = row.createCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getOrderTime());//订单日期
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getCustomerName());//公司名称
				cell = row.createCell(2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getContactsName());//客户姓名
				cell = row.createCell(3);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getOutOrderNo());//外部订单号
				cell = row.createCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getProductNo());//生产编号
				cell = row.createCell(5);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getModiType());//修饰类型
				cell = row.createCell(6);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getTbn());//碱基数
				cell = row.createCell(7);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getTb());//合成规格
				cell = row.createCell(8);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getPrimerType());//状态
				cell = row.createCell(9);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(style_center);
				cell.setCellValue(si.getDeliveryDate());//发货日期
				
				startRow = startRow +1;
			}
		}
		
        //输出文件到客户端
        HttpServletResponse response = inv.getResponse();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
        OutputStream out=response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
	
	
    /**
     * 导出HPLC纯化表
     * @throws IOException 
     * @throws ParseException 
     * */
	public void exHplcChunHuaBiao( String boardNo, Invocation inv) throws IOException, ParseException {
		
       //组织数据

		OrderInfo orderInfo = null;
		List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
		Board board = boardRepository.findByBoardNo(boardNo);
		if (board != null) {
			for (BoardHole boardHole : board.getBoardHoles()) {
				PrimerProduct primerProduct = boardHole.getPrimerProduct();
				
				this.addNewValue(primerProduct);
				
				if (boardHole.getStatus() == 0) {//0正常  1 删除 
					orderInfo = new OrderInfo();
					//修饰
					String midi = "";
					if (!"".equals(primerProduct.getModiFiveType())) {
						midi += "5'"+primerProduct.getModiFiveType()+",";
					}
					if (!"".equals(primerProduct.getModiMidType())) {
						midi += primerProduct.getModiMidType()+",";
					}
					if (!"".equals(primerProduct.getModiSpeType())) {
						midi += primerProduct.getModiSpeType()+",";
					}
					if (!"".equals(primerProduct.getModiThreeType())) {
						midi += "3'"+primerProduct.getModiThreeType()+",";
					}
					if(!"".equals(midi)){
						midi = "("+midi.substring(0, midi.length()-1)+")";
						orderInfo.setMidi(midi);
					}
					if("".equals(midi) && "HPLC".equals(primerProduct.getPurifyType())){
						orderInfo.setMidi("HPLC");
					}
					if (primerProduct.getOutProductNo() != null
							&& !"".equals(primerProduct.getOutProductNo())
							&& !"null".equals(primerProduct.getOutProductNo())) {
						orderInfo.setProductNo(primerProduct.getOutProductNo());
					} else {
						orderInfo.setProductNo(primerProduct.getProductNo());
					}
					orderInfo.setBoardNo(primerProduct.getBoardNo());
					orderInfo.setMakingNo(primerProduct.getOdTB()+"OD*"+primerProduct.getTb());
					orderInfo.setTbn(primerProduct.getTbn());
					orderInfo.setMw(primerProduct.getMw()+"");
					orderInfo.setComTbn(CharMatcher.anyOf("MRWSYKVHDBN").retainFrom(primerProduct.getGeneOrder().toUpperCase()));
					orderInfo.setDeliveryRemark(primerProduct.getRemark());
					orderInfos.add(orderInfo);
				}
			}
		}
		
		//先按板号查
		if (orderInfos != null && orderInfos.size()>0) {
		} else {
			//再按生产编号查
			PrimerProduct primerProduct = primerProductRepository.findByProductNoOrOutProductNo(boardNo, boardNo);
			if (primerProduct != null) {
				
				this.addNewValue(primerProduct);
				
				orderInfo = new OrderInfo();
				//修饰
				String midi = "";
				if (!"".equals(primerProduct.getModiFiveType())) {
					midi += primerProduct.getModiFiveType()+",";
				}
				if (!"".equals(primerProduct.getModiMidType())) {
					midi += primerProduct.getModiMidType()+",";
				}
				if (!"".equals(primerProduct.getModiSpeType())) {
					midi += primerProduct.getModiSpeType()+",";
				}
				if (!"".equals(primerProduct.getModiThreeType())) {
					midi += primerProduct.getModiThreeType()+",";
				}
				if(!"".equals(midi)){
					midi = "("+midi.substring(0, midi.length()-1)+")";
					orderInfo.setMidi(midi);
				}
				if("".equals(midi) && "HPLC".equals(primerProduct.getPurifyType())){
					orderInfo.setMidi("HPLC");
				}
				if (primerProduct.getOutProductNo() != null
						&& !"".equals(primerProduct.getOutProductNo())
						&& !"null".equals(primerProduct.getOutProductNo())) {
					orderInfo.setProductNo(primerProduct.getOutProductNo());
				} else {
					orderInfo.setProductNo(primerProduct.getProductNo());
				}
				orderInfo.setBoardNo(primerProduct.getBoardNo());
				orderInfo.setMakingNo(primerProduct.getOdTB()+"OD*"+primerProduct.getTb());
				orderInfo.setTbn(primerProduct.getTbn());
				orderInfo.setMw(primerProduct.getMw()+"");
				orderInfo.setComTbn(CharMatcher.anyOf("MRWSYKVHDBN").retainFrom(primerProduct.getGeneOrder().toUpperCase()));
				orderInfo.setDeliveryRemark(primerProduct.getRemark());
				orderInfos.add(orderInfo);
			}
		}
		
		
		
		//形成Excel
		String templetName = "HPLCchunhuabiao.xls";
		String strFileName = boardNo+".xls";
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
    	HSSFCellStyle style_center = workbook.createCellStyle();
    	style_center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	style_center.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框  
    	style_center.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框  
    	style_center.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框  
    	style_center.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框  
    	
    	row = sheet.getRow(1);
    	cell = row.getCell(2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(boardNo);
		
    	int startRow = 3;//从第3行开始
    	int index=1;
    	for (OrderInfo oi : orderInfos) {
			row = sheet.createRow(startRow);
			
			cell = row.createCell(0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(index);//序号
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(oi.getMidi());//修饰类型
			cell = row.createCell(2);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(oi.getProductNo());//生产编号
			cell = row.createCell(3);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(oi.getMakingNo());//OD
			cell = row.createCell(4);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(oi.getTbn()+"");//碱基数
			cell = row.createCell(5);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(oi.getMw());//MW
			cell = row.createCell(6);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(oi.getComTbn());//复合碱基
			cell = row.createCell(7);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell = row.createCell(8);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell = row.createCell(9);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell = row.createCell(10);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell = row.createCell(11);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell = row.createCell(12);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(oi.getDeliveryRemark());//
			
			startRow = startRow +1;
    		index++;
		}
    	
		
        //输出文件到客户端
        HttpServletResponse response = inv.getResponse();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
        OutputStream out=response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
	
	
	//补充临时字段的值
	public void addNewValue(PrimerProduct primerProduct) {
		for (PrimerProductValue pv : primerProduct.getPrimerProductValues()) {
			if (pv.getType().equals(PrimerValueType.odTotal)) {
				primerProduct.setOdTotal(pv.getValue());
			} else if (pv.getType().equals(PrimerValueType.odTB)) {
				primerProduct.setOdTB(pv.getValue());
			} else if (pv.getType().equals(PrimerValueType.nmolTotal)) {
				primerProduct.setNmolTotal(pv.getValue());
			} else if (pv.getType().equals(PrimerValueType.nmolTB)) {
				primerProduct.setNmolTB(pv.getValue());
			} else if (pv.getType().equals(PrimerValueType.baseCount)) {
				primerProduct.setTbn(pv.getValue());
			} else if (pv.getType().equals(PrimerValueType.tb)) {
				primerProduct.setTb(pv.getValue());
			} else if (pv.getType().equals(PrimerValueType.MW)) {
				primerProduct.setMw(pv.getValue());
			}
		}

}
	
}
