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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerType.PrimerOperationType;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.entity.PrintLabel;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.web.delivery.DeliveryInfo;
import org.one.gene.web.order.OrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.instruction.reply.EntityReply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Raw;

//Spring Bean的标识.
@Component
//默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class DeliveryService {

    private static Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PrimerProductRepository primerProductRepository;
    
    @Autowired
    private PrimerProductOperationRepository primerProductOperationRepository;

    @Autowired
    private SynthesisService synthesisService;
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private BoardRepository boardRepository;
    
	//保存发货信息
    @Transactional(readOnly = false)
	public String saveDelivery(List<PrimerProduct> primerProducts) {
    	
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (PrimerProduct pp:primerProducts) {
			primerProduct = primerProductRepository.findOne(pp.getId());
			if (primerProduct != null) {
				primerProduct.setOperationType(PrimerStatusType.finish);
				type = PrimerOperationType.deliverySuccess;
				typeDesc = PrimerOperationType.deliverySuccess.desc();
				
				
				//组装操作信息
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(primerProduct);
				primerProductOperation.setUserCode("123");//后续从session取得
				primerProductOperation.setUserName("张三");//后续从session取得
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setType(type);
				primerProductOperation.setTypeDesc(typeDesc);
				primerProductOperation.setBackTimes(primerProduct.getBackTimes());
				primerProductOperation.setFailReason("");
				
				primerProductOperations.add(primerProductOperation);
				
				
				//保存primer_product表数据
				primerProductRepository.save(primerProduct);
			}
			
    	}
		//更新操作信息
		primerProductOperationRepository.save(primerProductOperations);
    	
    	return "";
    }
   
		
	public Page<OrderInfo> convertPrimerProductList(
			Page<PrimerProduct> primerProductPage, Pageable pageable)
			throws IllegalStateException, IOException {

		OrderInfo orderInfo = new OrderInfo();
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		
		for(PrimerProduct primerProduct:primerProductPage.getContent()){
			
			synthesisService.addNewValue(primerProduct);
			
			orderInfo = new OrderInfo();
			if(!"".equals(primerProduct.getProductNo())){
				orderInfo.setProductNoMinToMax(primerProduct.getProductNo());
			}else{
				orderInfo.setProductNoMinToMax(primerProduct.getOutProductNo());
			}
			orderInfo.setOrderNo(primerProduct.getOrder().getOrderNo());
			orderInfo.setCustomerName(primerProduct.getOrder().getCustomerName());
			orderInfo.setCreateTime(primerProduct.getOrder().getCreateTime());
			orderInfo.setModifyTime(primerProduct.getOrder().getModifyTime());
			orderInfo.setStatus(primerProduct.getOperationType().desc());
			orderInfo.setTbnTotal(primerProduct.getTbn());
			orderInfoList.add(orderInfo);
		}
		
		Page<OrderInfo> primerProductListPage = new PageImpl<OrderInfo>(orderInfoList,pageable,primerProductPage.getTotalElements());
		
        return primerProductListPage;
    }
	
	//保存召回信息
    @Transactional(readOnly = false)
	public String saveBack(List<OrderInfo> orderInfos, String flag, String text ) {
    	
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (OrderInfo orderInfo : orderInfos) {
			primerProduct = primerProductRepository.findByProductNoOrOutProductNo(orderInfo.getProductNoMinToMax(), orderInfo.getProductNoMinToMax());
			if (primerProduct != null) {
				Board board = boardRepository.findByBoardNo(primerProduct.getBoardNo());
				if (board != null) {
					for (BoardHole boardHole:board.getBoardHoles()) {
						
						primerProductOperations = new ArrayList<PrimerProductOperation>();
						
						if (boardHole.getPrimerProduct().getId() == primerProduct.getId()){
							
							boardHole.getPrimerProduct().setModifyTime(new Date());//最后修改时间
							boardHole.setModifyTime(new Date());//最后修改时间
							boardHole.setModifyUser(123L);//后续从session取得
							if (boardHole.getPrimerProduct().getBackTimes() != null) {
								boardHole.getPrimerProduct().setBackTimes(boardHole.getPrimerProduct().getBackTimes()+1);//循环重回次数+1
							}
							
							if ("1".equals(flag)) {//安排合成
								boardHole.getPrimerProduct().setOperationType(PrimerStatusType.synthesis);//回到待合成
								boardHole.getPrimerProduct().setBoardNo("");//清空板号
								boardHole.setStatus(1);//删除
							}else if ("2".equals(flag)) {//重新分装
								boardHole.getPrimerProduct().setOperationType(PrimerStatusType.pack);//回到分装
							}
							
							type = PrimerOperationType.backSuccess;
							typeDesc = PrimerOperationType.backSuccess.desc();
							
							
							//组装操作信息
							primerProductOperation = new PrimerProductOperation();
							primerProductOperation.setPrimerProduct(boardHole.getPrimerProduct());
							primerProductOperation.setUserCode("123");//后续从session取得
							primerProductOperation.setUserName("张三");//后续从session取得
							primerProductOperation.setCreateTime(new Date());
							primerProductOperation.setType(type);
							primerProductOperation.setTypeDesc(typeDesc);
							primerProductOperation.setBackTimes(boardHole.getPrimerProduct().getBackTimes());
							primerProductOperation.setFailReason(text);
							
							if ("1".equals(flag)) {
								boardHole.setPrimerProductOperation(primerProductOperation);
							}
							
							primerProductOperations.add(primerProductOperation);
							
							
							//保存primer_product表数据
							primerProductRepository.save(boardHole.getPrimerProduct());
						}
						
						//更新操作信息
						primerProductOperationRepository.save(primerProductOperations);
						
					}
					
					boardRepository.save(board);
					
				}
			}
			
    	}
		
    	
    	return "";
    }
    
    
    
	/**
     * 导出打印标签文件
     * @throws Exception 
     * */
	public EntityReply<File> deliveryLabel(List<PrimerProduct> primerProducts, Invocation inv) throws Exception {
		//打印单位、订单号、生产编号、碱基数（总和）、od/tube、od总量
		String customerCode = primerProducts.get(0).getOrder().getCustomerCode();
		String strFilePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"upExcel"+File.separator+"deliveryLabel"+File.separator;
		String strFileName = customerCode+"-"+System.currentTimeMillis()+".xls";
		
		OrderInfo orderInfo = null;
		List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
		Customer customer = customerRepository.findByCode(customerCode);
		
		
		for (PrimerProduct primerProduct : primerProducts) {
			
					
			orderInfo = new OrderInfo();
			//单位
			orderInfo.setUnit(customer.getUnit());
			//订单号
			orderInfo.setOrderNo(primerProduct.getOrder().getOrderNo());
			//生产编号
			if(!"".equals(primerProduct.getProductNo())){
				orderInfo.setProductNo(primerProduct.getProductNo());
			}else{
				orderInfo.setProductNo(primerProduct.getOutProductNo());
			}
			orderInfo.setTbnTotal(new BigDecimal(primerProduct.getGeneOrder().trim().length()));
			//tbnTotal = tbnTotal.add(new BigDecimal(primerProduct.getGeneOrder().trim().length()));
			
			for(PrimerProductValue primerProductValue:primerProduct.getPrimerProductValues()){
				PrimerValueType type = primerProductValue.getType();
				if(type.equals(PrimerValueType.odTotal)){//OD总量
					orderInfo.setOdTotal(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.odTB)){//OD/TB
					orderInfo.setOdTB(primerProductValue.getValue());
				}
			}
			
			orderInfos.add(orderInfo);
		}
			
		//形成Excel


		//计算第一列多少行
		BigDecimal totalListCount = new BigDecimal(orderInfos.size());//本excel的条数
		int totalcolumns = totalListCount.divide(new BigDecimal(3), 0, BigDecimal.ROUND_UP).intValue();//总共的行数
		
		
		FileOutputStream fos = null;
		
		HSSFWorkbook workbook = new HSSFWorkbook(); //产生工作簿对象
		
		//每行逐行放数据
		int rowNum = 0;
		int rowNumForpage  = 0;
		int columNum = 1;
		int sheetNum = -1;
		HSSFSheet sheet = null;
		for (OrderInfo orderInfoLable : orderInfos) {
			
			if(rowNum==0){
				sheetNum +=1;
				sheet = workbook.createSheet(); //产生工作表对象
				//设置工作表的名称
				workbook.setSheetName(sheetNum,"第"+(sheetNum+1)+"页");
			}
			HSSFRow row = null;
			if(columNum ==1){
				row = sheet.createRow((short)rowNum);//从第一行开始记录
			}else{
				row = sheet.getRow(rowNumForpage);
			}
			for (int k=0;k<6;k++){
				HSSFCell cell = row.createCell((short) ((columNum-1)*6+k));//产生单元格
				//设置单元格内容为字符串型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				//往单元格中写入信息
				String value = "";
				switch(k){
				  case 0:
					value = orderInfoLable.getUnit();
				  	break;
				  case 1:
					value = orderInfoLable.getOrderNo();
					break;
				  case 2:
					value = orderInfoLable.getProductNo();
					break;
				  case 3:
					value = orderInfoLable.getTbnTotal().toString();
					break;
				  case 4:
					value = orderInfoLable.getOdTotal().toString();
					break;
				  case 5:
					value = orderInfoLable.getOdTB().toString();
					break;
				  default:
					break;
				}
				
				if (!"null".equals(value)) {
					cell.setCellValue(value);
				}
		    }
			rowNum+=1;
			rowNumForpage+=1;
			if(totalcolumns == (rowNumForpage)){
				columNum+=1;
				rowNumForpage = 0;
			}
			
		}
		fos = new FileOutputStream(strFilePath + strFileName);
		// 把相应的Excel 工作簿存盘
		workbook.write(fos);
    
        File file = new File(strFilePath, strFileName); 
		
		return Replys.with(file).as(Raw.class).downloadFileName(strFileName);
		
    }
		
	
    /**
     * 导出发货清单文件
     * @throws IOException 
     * */
	public void deliveryList( List<OrderInfo> orderInfos, Invocation inv) throws IOException {
		
		String companyName = "";//公司名称
		String webSite     = "";//网址
		String customerName     = "";//客户姓名
		String orderDate     = "";//订货日期
		String deliveryDate     = "";//发货日期
		String unit     = "";//客户单位
		String deliveryAddress     = "";//送货地址
		String area     = "";//地区
		String address     = "";//公司地址
		String kaidan     = "";//开单员
		String saler     = "";//销售员
		String phoneNo     = "";//联系方式
		String email     = "";//邮箱
		String orderNo = "";//订单号
		String productNoMinToMax = "";//序列范围
		
		for (OrderInfo orderInfo : orderInfos) {
			orderNo = orderInfo.getOrderNo();
			
		}
		
		Order order = orderRepository.findByOrderNo(orderNo);
		String customerCode = "";
		if( order != null ){
			customerCode = order.getCustomerCode();//客户代码
			orderDate = order.getCreateTime().toString();
			productNoMinToMax = order.getProductNoMinToMax();
		}
		
		if(orderDate.length()>10){
			orderDate = orderDate.substring(0, 10);
		}
		
		Customer customer = customerRepository.findByCode(customerCode);
		
		if (customer != null) {
			companyName = customer.getUnit();
			webSite     = customer.getWebSite();
			customerName= customer.getName();
			unit = customer.getUnit();
			deliveryAddress = customer.getAddress();
			address         = customer.getAddress();
			phoneNo         = customer.getPhoneNo();
			email           = customer.getEmail();
		}
		
		
		int orderBaseCount = 0;//订单碱基数总量
		double odTotalCount = 0.0;//OD总量
		int geneCount = 0;//序列总条数

		int countNum_OPC  = 0;//订单碱基条数(OPC)
		int baseCount_OPC  = 0;//订单碱基数总量(OPC)
		double odTotalCount_OPC = 0.0;//OD总量(OPC)
		double basePrice_OPC  = 0.0;//订单碱基价格(OPC)
		int countNum_PAGE  = 0;//订单碱基条数(PAGE)
		int baseCount_PAGE  = 0;//订单碱基数总量(PAGE)
		double odTotalCount_PAGE = 0.0;//OD总量(PAGE)
		double basePrice_PAGE  = 0.0;//订单碱基价格(PAGE)
		int countNum_HPLC  = 0;//订单碱基条数(HPLC)
		int baseCount_HPLC  = 0;//订单碱基数总量(HPLC)
		double odTotalCount_HPLC = 0.0;//OD总量(HPLC)
		double basePrice_HPLC  = 0.0;//订单碱基价格(HPLC)
		String purifyType = "";//纯化类型
		for (PrimerProduct primerProduct : order.getPrimerProducts()) {
			geneCount += 1;
			
			purifyType = primerProduct.getPurifyType();
			if ("OPC".equals(purifyType)) {
				countNum_OPC += 1;
			} else if ("PAGE".equals(purifyType)) {
				countNum_PAGE += 1;
			} else {
				countNum_HPLC += 1;
			}
			for (PrimerProductValue primerProductValue : primerProduct.getPrimerProductValues()) {
				PrimerValueType type = primerProductValue.getType();
				if (type.equals(PrimerValueType.baseCount)) {// 碱基数
					orderBaseCount += primerProductValue.getValue().intValue();
					if ("OPC".equals(purifyType)) {
						baseCount_OPC += primerProductValue.getValue().intValue();
					} else if ("PAGE".equals(purifyType)) {
						baseCount_PAGE += primerProductValue.getValue().intValue();
					} else {
						baseCount_HPLC += primerProductValue.getValue().intValue();
					}
				}else if(type.equals(PrimerValueType.odTotal)){//od总量
					odTotalCount += primerProductValue.getValue().doubleValue();
					if ("OPC".equals(purifyType)) {
						odTotalCount_OPC += primerProductValue.getValue().doubleValue();
					} else if ("PAGE".equals(purifyType)) {
						odTotalCount_PAGE += primerProductValue.getValue().doubleValue();
					} else {
						odTotalCount_HPLC += primerProductValue.getValue().doubleValue();
					}
				}
			}
		  }
	    
		if (baseCount_OPC < 60) {
			if (odTotalCount_OPC <= 5) {
				basePrice_OPC = 0.45;
			} else if (odTotalCount_OPC > 5 && odTotalCount_OPC <= 10) {
				basePrice_OPC = 0.8;
			} else if (odTotalCount_OPC > 10 && odTotalCount_OPC <= 20) {
				basePrice_OPC = 1.6;
			}
		}else if (baseCount_OPC >= 60) {
			if (odTotalCount_OPC <= 5) {
				basePrice_OPC = 0.8;
			} else if (odTotalCount_OPC > 5 && odTotalCount_OPC <= 10) {
				basePrice_OPC = 1.6;
			} else if (odTotalCount_OPC > 10 && odTotalCount_OPC <= 20) {
				basePrice_OPC = 3.2;
			}
		}
		if (baseCount_PAGE < 60) {
			if (odTotalCount_PAGE <= 5) {
				basePrice_PAGE = 0.6;
			} else if (odTotalCount_PAGE > 5 && odTotalCount_PAGE <= 10) {
				basePrice_PAGE = 1.2;
			} else if (odTotalCount_PAGE > 10 && odTotalCount_PAGE <= 20) {
				basePrice_PAGE = 2.4;
			}
		}else if (baseCount_PAGE >= 60) {
			if (odTotalCount_PAGE <= 5) {
				basePrice_PAGE = 1.2;
			} else if (odTotalCount_PAGE > 5 && odTotalCount_PAGE <= 10) {
				basePrice_PAGE = 2.4;
			} else if (odTotalCount_PAGE > 10 && odTotalCount_PAGE <= 20) {
				basePrice_PAGE = 4.8;
			}
		}
		if (baseCount_HPLC < 60) {
			if (odTotalCount_HPLC <= 5) {
				basePrice_HPLC = 0.45;
			} else if (odTotalCount_HPLC > 5 && odTotalCount_HPLC <= 10) {
				basePrice_HPLC = 0.8;
			} else if (odTotalCount_HPLC > 10 && odTotalCount_HPLC <= 20) {
				basePrice_HPLC = 1.6;
			}
		}else if (baseCount_HPLC >= 60) {
			if (odTotalCount_HPLC <= 5) {
				basePrice_HPLC = 0.8;
			} else if (odTotalCount_HPLC > 5 && odTotalCount_HPLC <= 10) {
				basePrice_HPLC = 1.6;
			} else if (odTotalCount_HPLC > 10 && odTotalCount_HPLC <= 20) {
				basePrice_HPLC = 3.2;
			}
		}
		//
		DecimalFormat df = new DecimalFormat("#.00");
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		List<DeliveryInfo> deliveryInfos = new ArrayList<DeliveryInfo>();
		
		if (basePrice_OPC > 0) {
			deliveryInfo = new DeliveryInfo();
			deliveryInfo.setDeliveryName("DNA合成(OPC)");
			deliveryInfo.setCountNum(countNum_OPC);
			deliveryInfo.setOdTotal(odTotalCount_OPC);
			deliveryInfo.setMeasurement("bp");
			deliveryInfo.setCount(baseCount_OPC);
			deliveryInfo.setPrice(basePrice_OPC);
			deliveryInfo.setMoney(df.format(baseCount_OPC * basePrice_OPC));
			deliveryInfos.add(deliveryInfo);
		}
		if (basePrice_PAGE > 0) {
			deliveryInfo = new DeliveryInfo();
			deliveryInfo.setDeliveryName("DNA合成(PAGE)");
			deliveryInfo.setCountNum(countNum_PAGE);
			deliveryInfo.setOdTotal(odTotalCount_PAGE);
			deliveryInfo.setMeasurement("bp");
			deliveryInfo.setCount(baseCount_PAGE);
			deliveryInfo.setPrice(basePrice_PAGE);
			deliveryInfo.setMoney(df.format(baseCount_PAGE * basePrice_PAGE));
			deliveryInfos.add(deliveryInfo);
		}
		if (basePrice_HPLC > 0) {
			deliveryInfo = new DeliveryInfo();
			deliveryInfo.setDeliveryName("DNA合成(HPLC)");
			deliveryInfo.setCountNum(countNum_HPLC);
			deliveryInfo.setOdTotal(odTotalCount_HPLC);
			deliveryInfo.setMeasurement("bp");
			deliveryInfo.setCount(baseCount_HPLC);
			deliveryInfo.setPrice(basePrice_HPLC);
			deliveryInfo.setMoney(df.format(baseCount_HPLC * basePrice_HPLC));
			deliveryInfos.add(deliveryInfo);
		}
		
		//形成Excel
		String templetName = "deliveryListTemplate.xls";
		String strFileName = orderNo+"-"+System.currentTimeMillis()+".xls";
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
		row = sheet.getRow(0);
		cell = row.getCell(0);//得到单元格
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(companyName);//
        
		row = sheet.getRow(1);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(webSite+"   合成送货单");//网址
		
		row = sheet.getRow(2);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("客户姓名："+customerName);
		cell = row.getCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("订货日期："+orderDate+"   发货日期："+deliveryDate);
		
		row = sheet.getRow(3);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("客户单位："+unit);
		cell = row.getCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("订单号："+orderNo+"   序列范围："+productNoMinToMax);
		
		row = sheet.getRow(4);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("送货地址："+deliveryAddress);
		cell = row.getCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("地区："+area+"   总条数："+geneCount+"条");
		
		int startRow = 6;//从第6行开始
		int rowNo = 1;//行号
		double totalMoney = 0.0;//合计
		for (DeliveryInfo dis : deliveryInfos) {
			row = sheet.getRow(startRow);
			cell = row.getCell(0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(rowNo);
			cell = row.getCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(dis.getDeliveryName());
		cell = row.getCell(2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(dis.getCountNum());
		cell = row.getCell(3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(dis.getOdTotal()+"OD");
			cell = row.getCell(4);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(dis.getMeasurement());
		cell = row.getCell(5);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(dis.getCount());
			cell = row.getCell(6);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(dis.getPrice());
			cell = row.getCell(7);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(dis.getMoney());
			startRow = startRow +1;
			rowNo = rowNo +1;
			if(!"".equals(dis.getMoney())){
				totalMoney += Double.parseDouble(dis.getMoney());
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
	
}
