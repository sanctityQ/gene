package org.one.gene.domain.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
		DecimalFormat df = new DecimalFormat("#.00");
		
		for (Order tempOrder : orders) {
			orderNo = tempOrder.getOrderNo();
			
			String orderDate     = "";//订货日期
			String companyName = "";//公司名称
			String contactsName = "";//客户联系人
			String customerCode = "";
			String handlerName = "";
			
			Order order = orderRepository.findByOrderNo(orderNo);
			
			//计算客户数量
			if ("".equals(order.getContactsName())) {
				customerContacts += 1;
			}else{
				customerContactsMap.put(order.getContactsName(), "");
			}
			
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
			
			int geneCount = 0;//序列总条数
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
				geneCount += 1;
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
				String dnaSrt = orderNo+"DNA合成_"+df.format(baseVal);
				
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
					deliveryInfo.setExtendStr4(orderNo);//订单号
					deliveryInfo.setExtendStr5(handlerName);//业务员
					deliveryInfo.setDeliveryName("DNA合成");//货物名称
					deliveryInfo.setCountNum(1);//条数
					deliveryInfo.setOdTotal(odTotal);//合成量
					deliveryInfo.setMeasurement("bp");//计量单位
					deliveryInfo.setCount(tbn);//数量
					deliveryInfo.setPrice(baseVal);//单价
					deliveryInfo.setMoney(df.format(deliveryInfo.getCount() * deliveryInfo.getPrice()));//金额
					
					deliveryInfoMap.put(dnaSrt, deliveryInfo);
				}
				
				//修饰和纯化
				String modiStr    = "";
				String modiStrKey = "";
				double price = 0.0;
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
					
					modiStrKey = modiStr + "_" + df.format(modiPrice);
					price = Double.parseDouble(df.format(modiPrice));
				} else {
					if (purifyVal > 0) {
						modiStr      = purifyType;
						modiStrKey = purifyType + "_" + df.format(purifyVal);
						price = Double.parseDouble(df.format(purifyVal));
					}
				}
				if (!"".equals(modiStrKey)) {
					
					modiStrKey = orderNo+modiStrKey;
					
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
						deliveryInfo.setExtendStr4(orderNo);//订单号
						deliveryInfo.setExtendStr5(handlerName);//业务员
						deliveryInfo.setDeliveryName(modiStr);
						deliveryInfo.setCountNum(1);
						deliveryInfo.setOdTotal(odTotal);
						deliveryInfo.setMeasurement("条");
						deliveryInfo.setCount(deliveryInfo.getCountNum());
						deliveryInfo.setPrice(price);
						deliveryInfo.setMoney(df.format(deliveryInfo.getCount() * deliveryInfo.getPrice()));
						
						deliveryInfoMap.put(modiStrKey, deliveryInfo);
					}
				}
				
			}
			
		}
		
	    //累计客户数量
		customerContacts = customerContacts + customerContactsMap.size();
		int allOrderCount = orders.size();
		int tiaoshuCount = 0;//条数之和
		double hechengliangSum = 0;//合成量之和
		int shuliangCount = 0;//数量之和
		double danjiaSum = 0;//单价总和
		
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
		
		int startRow = 2;//从第2行开始
		double totalMoney = 0.0;//合计
		for (String key : deliveryInfoMap.keySet()) {
			DeliveryInfo dis = (DeliveryInfo)deliveryInfoMap.get(key);
			
			row = sheet.getRow(startRow);
			
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
			
			tiaoshuCount += dis.getCountNum();//条数之和
			
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
			
			danjiaSum += dis.getPrice();//单价总和
			
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
		
		double avgePrice = danjiaSum/tiaoshuCount;//单价底下平均数为：所有单价总和除以条数总数

		row = sheet.getRow(startRow);
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
	
		row = sheet.getRow(startRow+1);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("到");
		
		for (int a=1;a<13;a++){
			cell = row.createCell(a);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);	
		}
		
		row = sheet.getRow(startRow+2);
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
		cell.setCellValue(tiaoshuCount);//条数总数
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
		cell.setCellValue(df.format(avgePrice));//单价平均值
		cell = row.createCell(11);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue(totalMoney);//金额之和
		cell = row.createCell(12);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		
		row = sheet.getRow(startRow+3);
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
				cell.setCellValue(orderNo);//订单号
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
     * */
	public void exportYinWuJinDuBiao( StatisticsInfo statisticsInfo, Invocation inv) throws IOException {
		
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
    	Map<String, StatisticsInfo> siMap = new HashMap<String, StatisticsInfo>();//客户的生产引物信息
    	
		for (Order tempOrder : orders) {
			orderNo = tempOrder.getOrderNo();
			
			String c_Code = "";//公司代码
			String c_Name = "";//公司名称
			String c_Flag = "";//公司性质标志
			
			Order order = orderRepository.findByOrderNo(orderNo);
			
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
			int count2 = 0;//碱基总数
			int count3 = 0;//修饰和HPLC条数
			int count4 = 0;//修饰和HPLC碱基数
			int count5 = 0;//正常发货数
			int count6 = 0;//晚发货数
			int count7 = 0;//内部重合数
			int count8 = 0;//外部重合条数
			int count9 = 0;//修饰和HPLC重合数
			
			for (PrimerProduct pp : order.getPrimerProducts()) {
				
				for (PrimerProductValue primerProductValue : pp.getPrimerProductValues()) {
					PrimerValueType type = primerProductValue.getType();
					if (type.equals(PrimerValueType.baseCount)) {// 碱基数
						//tbn = primerProductValue.getValue().intValue();
					}else if(type.equals(PrimerValueType.odTotal)){//od总量
						//odTotal = primerProductValue.getValue().doubleValue();
					}else if(type.equals(PrimerValueType.nmolTotal)){//nmol总量
						//nmolTotal = primerProductValue.getValue().doubleValue();
					}
				}
				
			}
			
			if (siMap.get(c_Code) == null) {
				StatisticsInfo si = new StatisticsInfo();
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
				
				siMap.put(c_Code, si);
			} else {
				StatisticsInfo si = siMap.get(c_Code);
				si.setCount1(count1+si.getCount1());
				si.setCount2(count2+si.getCount2());
				si.setCount3(count3+si.getCount3());
				si.setCount4(count4+si.getCount4());
				si.setCount5(count5+si.getCount5());
				si.setCount6(count6+si.getCount6());
				si.setCount7(count7+si.getCount7());
				si.setCount8(count8+si.getCount8());
				si.setCount9(count9+si.getCount9());
				siMap.put(c_Code, si);
			}
			
		}
		
		row = sheet.getRow(0);
		cell = row.getCell(0);
		cell.setCellValue(cell.getStringCellValue()+"("+statisticsInfo.getCreateStartTime()+"~"+statisticsInfo.getCreateEndTime()+")");//放置起止时间
		
		for (String key : siMap.keySet()) {
			StatisticsInfo si = (StatisticsInfo)siMap.get(key);
			
			row = sheet.createRow(startRow);
			
			cell = row.createCell(0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(si.getCustomerName());//公司名称
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount1());//引物条数	
			cell = row.createCell(2);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount2());//碱基总数
			cell = row.createCell(3);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount3());//修饰和HPLC条数
			cell = row.createCell(4);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount4());//修饰和HPLC碱基数
			cell = row.createCell(5);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount5());//正常发货数
			cell = row.createCell(6);
		    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount6());//晚发货数
			cell = row.createCell(7);
		    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount7());//内部重合数
			cell = row.createCell(8);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount8());//外部重合条数
			cell = row.createCell(9);
		    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		    cell.setCellStyle(style_center);
			cell.setCellValue(si.getCount9());//修饰和HPLC重合数
			
			startRow = startRow +1;
		}
		
		row = sheet.createRow(startRow);
		
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("合计");//公司名称
		cell = row.createCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");//引物条数	
		cell = row.createCell(2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");//碱基总数
		cell = row.createCell(3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");//修饰和HPLC条数
		cell = row.createCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");//修饰和HPLC碱基数
		cell = row.createCell(5);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");//正常发货数
		cell = row.createCell(6);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellStyle(style_center);
		cell.setCellValue("总数");//晚发货数
		cell = row.createCell(7);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellStyle(style_center);
		cell.setCellValue("总数");//内部重合数
		cell = row.createCell(8);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(style_center);
		cell.setCellValue("总数");//外部重合条数
		cell = row.createCell(9);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellStyle(style_center);
		cell.setCellValue("总数");//修饰和HPLC重合数
		
		
        //输出文件到客户端
        HttpServletResponse response = inv.getResponse();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
        OutputStream out=response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
	
	
}
