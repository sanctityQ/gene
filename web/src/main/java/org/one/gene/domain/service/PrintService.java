package org.one.gene.domain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerLabelConfig;
import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.entity.PrintLabel;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerLabelConfigRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.web.order.OrderInfoList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.instruction.reply.EntityReply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Raw;

//Spring Bean的标识.
@Component
//默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class PrintService {

    private static Logger logger = LoggerFactory.getLogger(PrintService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PrimerProductRepository primerProductRepository;
    
    @Autowired
    private PrimerProductValueRepository primerProductValueRepository;
    
    @Autowired
    private BoardRepository boardRepository;
    
    @Autowired
    private BoardHoleRepository boardHoleRepository;
    
    @Autowired
    private PrimerProductOperationRepository primerProductOperationRepository;
	
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PrimerLabelConfigRepository primerLabelConfigRepository;
    /**
     * 得到导出打印标签的生产数据
     * */
	public List<PrimerProduct> getPrimerProducts(String boardNo, String  productNo, Invocation inv) {
		
		List<PrimerProduct> primerProducts = new ArrayList<PrimerProduct>();
		if (!"".equals(productNo)) {
			PrimerProduct primerProduct = primerProductRepository.findByProductNoOrOutProductNo(productNo, productNo);
			if (primerProduct != null) {
				primerProducts.add(primerProduct);
			}
		}else{
			primerProducts = primerProductRepository.findByBoardNo(boardNo);
		}
		
		
		return primerProducts;
	}
	
    /**
     * 从生产数据得到订单List
     * */
	public List<Order> getOrderListFromPrimerProductList(List<PrimerProduct> primerProducts) {
		
		List<Order> orders = new ArrayList<Order>();
		Map<String, Order> map = new HashMap<String, Order>();
		Order order = new Order();
		for (PrimerProduct primerProduct : primerProducts) {
			order = new Order();
			order = primerProduct.getOrder();
			if (map.get(order.getCustomerCode()) == null) {
				map.put(order.getCustomerCode(), order);
			}
		}

		for (Map.Entry<String, Order> orderMap : map.entrySet()) {
			orders.add((Order) orderMap.getValue());
		}
		
		return orders;
	}
	
    /**
     * 导出打印标签文件
     * @throws Exception 
     * */
	public EntityReply<File> exportLabel( List<PrimerProduct> primerProducts, String customerCode, Invocation inv) throws Exception {
		
		
		//读取上机表文件输出地址
		InputStream inputStream  =   this.getClass().getClassLoader().getResourceAsStream("application.properties");    
		Properties p = new  Properties(); 
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String strFilePath = p.getProperty("sjbPath");
		String strFileName = customerCode+System.currentTimeMillis()+".xls";

		PrintLabel printLabel = new PrintLabel();
		List<PrintLabel> printLabels = new ArrayList<PrintLabel>();
		PrimerProduct primerProduct = new PrimerProduct();
		
		for (PrimerProduct primerProductTemp : primerProducts) {
			
			//重新查询数据
			primerProduct = primerProductRepository.findOne(primerProductTemp.getId());
					
			printLabel = new PrintLabel();
			
			//生产编号
			if(!"".equals(primerProduct.getProductNo())){
				printLabel.setProductNo(primerProduct.getProductNo());
			}else{
				printLabel.setProductNo(primerProduct.getOutProductNo());
			}
			
			printLabel.setPrimeName(primerProduct.getPrimeName());//引物名称
			printLabel.setOrderNo(primerProduct.getOrder().getOrderNo());//订单号
			printLabel.setRemark(primerProduct.getRemark());//备注/日期
			
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
				printLabel.setMidi(midi);
			}
			
			for(PrimerProductValue primerProductValue:primerProduct.getPrimerProductValues()){
				PrimerValueType type = primerProductValue.getType();
				if(type.equals(PrimerValueType.MW)){//MW
					printLabel.setMw(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.tb)){//管数
					printLabel.setTube(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.odTotal)){//OD总量
					printLabel.setOdTotal(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.odTB)){//OD/TB
					printLabel.setOdTB(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.nmolTotal)){//NUML总量
					printLabel.setNmolTotal(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.nmolTB)){//NUML/TB
					printLabel.setNmolTB(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.baseCount)){//碱基数
					printLabel.setTbn(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.TM)){//TM
					printLabel.setTm(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.GC)){//GC
					printLabel.setGc(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.GC)){//ug/tube======================PrimerValueType.java 没有这个值？？？
					printLabel.setGc(primerProductValue.getValue());
				}
				
				if(type.equals(PrimerValueType.nmolTB)){//加水量
					printLabel.setPmole(new BigDecimal(10).multiply(primerProductValue.getValue()));
				}
			}
			
			//根据管数循环
			int baseCount = 1;
			if(printLabel.getTube() != null){
				baseCount = printLabel.getTube().intValue();
			}
			
			for(int i=0;i<baseCount;i++){
				printLabels.add(printLabel);
			}
		}
			
		//形成Excel
		//读取配置信息
		PrimerLabelConfig primerLabelConfig = primerLabelConfigRepository.findByCustomerCode(customerCode);
		if (primerLabelConfig == null) {
			throw new Exception("客户代码"+customerCode+"的引物标签打印信息为空，请配置！");
		}

		//计算第一列多少行
		BigDecimal totalListCount = new BigDecimal(printLabels.size());//本excel的条数
		int totalcolumns = totalListCount.divide(new BigDecimal(primerLabelConfig.getColumns().getValue()), 0, BigDecimal.ROUND_UP).intValue();//总共的行数
		
		
		FileOutputStream fos = null;
		
		HSSFWorkbook workbook = new HSSFWorkbook(); //产生工作簿对象
		
		//每行逐行放数据
		int rowNum = 0;
		int rowNumForpage  = 0;
		int columNum = 1;
		int sheetNum = -1;
		HSSFSheet sheet = null;
		for (PrintLabel printLabelExcel : printLabels) {
			
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
			List<PrimerLabelConfigSub> primerLabelConfigSubs = primerLabelConfig.getPrimerLabelConfigSubs();
			for (int k=0;k<primerLabelConfigSubs.size();k++){
				String type = ((PrimerLabelConfigSub) primerLabelConfigSubs.get(k)).getType();
				HSSFCell cell = row.createCell((short) ((columNum-1)*primerLabelConfigSubs.size()+k));//产生单元格
				//设置单元格内容为字符串型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				//往单元格中写入信息
				String value = "";
				if("productNo".equals(type)){
					value = printLabelExcel.getProductNo();
				}else if("primeName".equals(type)){
					value = printLabelExcel.getPrimeName();
				}else if("orderNo".equals(type)){
					value = printLabelExcel.getOrderNo();
				}else if("tube".equals(type)){
					value = printLabelExcel.getTube()+"";
				}else if("odTotal".equals(type)){
					value = printLabelExcel.getOdTotal()+"";
				}else if("odTB".equals(type)){
					value = printLabelExcel.getOdTB()+"";
				}else if("nmolTotal".equals(type)){
					value = printLabelExcel.getNmolTotal()+"";
				}else if("nmolTB".equals(type)){
					value = printLabelExcel.getNmolTB()+"";
				}else if("tbn".equals(type)){
					value = printLabelExcel.getTbn()+"";
				}else if("mw".equals(type)){
					value = printLabelExcel.getMw()+"";
				}else if("tm".equals(type)){
					value = printLabelExcel.getTm()+"";
				}else if("gc".equals(type)){
					value = printLabelExcel.getGc()+"";
				}else if("ugTB".equals(type)){
					value = printLabelExcel.getUgTB()+"";
				}else if("pmole".equals(type)){
					value = printLabelExcel.getPmole()+"";
				}else if("remark".equals(type)){
					value = printLabelExcel.getRemark();
				}else if("midi".equals(type)){
					value = printLabelExcel.getMidi();
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

		
		//读文件
		File file = new File(strFilePath, strFileName); 
		
		return Replys.with(file).as(Raw.class).downloadFileName(strFileName);
    }
	
    /**
     * 导出打印报告单文件
     * @throws IOException 
     * */
	public EntityReply<File> exportReport( List<PrimerProduct> primerProducts, Invocation inv) throws IOException {
		
		
		//读取上机表文件输出地址
		InputStream inputStream  =   this.getClass().getClassLoader().getResourceAsStream("application.properties");  
		Properties p = new  Properties(); 
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String strFilePath = p.getProperty("sjbPath");
		String report_templet_Path = p.getProperty("report_templet_Path");
		String strFileName = "";
		List<String> fileNames = new ArrayList<String>();
		
		//组装Map
		
		String customerCode = "";//客户代码
		Map<String, Customer> map = new HashMap<String, Customer>();
		Customer customer = new Customer();
		for(PrimerProduct primerProduct:primerProducts){
			customerCode = primerProduct.getOrder().getCustomerCode();
			customer = new Customer();
			//如果没有就新放入
			if( map.get(customerCode) == null){
				customer = customerRepository.findByCode(customerCode);
				if (customer != null) {
					customer.getPrimerProducts().add(primerProduct);
					map.put(customerCode, customer);
				}
			}else{
				customer = (Customer) map.get(customerCode);
				customer.getPrimerProducts().add(primerProduct);
				map.put(customerCode, customer);
			}
			
		}
		
		Map<String, List<PrintLabel>> printReportMap = new HashMap<String, List<PrintLabel>>();
		List<PrintLabel> printLabels = new ArrayList<PrintLabel>();
	    
		for (Map.Entry<String,Customer> customerMap : map.entrySet()) {
			
			PrintLabel printLabel = new PrintLabel();

			for(PrimerProduct primerProduct:customerMap.getValue().getPrimerProducts()){
				printLabel = new PrintLabel();
				
				//生产编号
				if(!"".equals(primerProduct.getProductNo())){
					printLabel.setProductNo(primerProduct.getProductNo());
				}else{
					printLabel.setProductNo(primerProduct.getOutProductNo());
				}
				
				printLabel.setPrimeName(primerProduct.getPrimeName());//引物名称
				printLabel.setOrderNo(primerProduct.getOrder().getOrderNo());//订单号
				printLabel.setGeneOrder(primerProduct.getGeneOrder());//引物序列
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
					printLabel.setMidi(midi);
				}
				
				for(PrimerProductValue primerProductValue:primerProduct.getPrimerProductValues()){
					PrimerValueType type = primerProductValue.getType();
					if(type.equals(PrimerValueType.odTotal)){//OD总量
						printLabel.setOdTotal(primerProductValue.getValue());
					}else if(type.equals(PrimerValueType.odTB)){//OD/TB
						printLabel.setOdTB(primerProductValue.getValue());
					}else if(type.equals(PrimerValueType.nmolTotal)){//NUML总量
						printLabel.setNmolTotal(primerProductValue.getValue());
					}else if(type.equals(PrimerValueType.nmolTB)){//NUML/TB
						printLabel.setNmolTB(primerProductValue.getValue());
					}else if(type.equals(PrimerValueType.baseCount)){//碱基数
						printLabel.setTbn(primerProductValue.getValue());
					}else if(type.equals(PrimerValueType.MW)){//MW
						printLabel.setMw(primerProductValue.getValue());
					}else if(type.equals(PrimerValueType.TM)){//TM
						printLabel.setTm(primerProductValue.getValue());
					}else if(type.equals(PrimerValueType.mv)){//MV
						printLabel.setMv(primerProductValue.getValue());
					}else if(type.equals(PrimerValueType.GC)){//GC
						printLabel.setGc(primerProductValue.getValue());
					}
					
					if(type.equals(PrimerValueType.nmolTB)){//加水量
						printLabel.setPmole(new BigDecimal(10).multiply(primerProductValue.getValue()));
					}
				}
				
				printLabels.add(printLabel);
			}
			
			printReportMap.put(customerMap.getKey(), printLabels);
			printLabels = new ArrayList<PrintLabel>();
			
		}
			
		//形成Excel
		for (Map.Entry<String, List<PrintLabel>> printReportapExcels : printReportMap.entrySet()) {
			
			//读取3种模板
			String costumerCodeExcel = printReportapExcels.getKey();
			strFileName = costumerCodeExcel+System.currentTimeMillis()+".xls";
			Customer customerFile = map.get(costumerCodeExcel);
			
			if(customerFile.getCode().equals("1102")){//金唯智

			}
			
			//----------------------------------
	        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("D:\\templet\\report\\金唯智报告单.xls"));

	        HSSFSheet sheet = workbook.getSheetAt(0);  
			
			FileOutputStream fos = null;
			HSSFRow row = null;
			
			int rowNum = 7;
        	for (PrintLabel printLabel:(List<PrintLabel>)printReportapExcels.getValue()) {
        		
        		row = sheet.getRow(rowNum);
         		
         		for (int k=1;k<13;k++){
         			HSSFCell cell = row.createCell((short) k);//产生单元格
         			//设置单元格内容为字符串型
         			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
         			//为了能在单元格中写入中文，设置字符编码为UTF_16
         			//往单元格中写入信息
         			String value = "";
         			if(k==1){
         				value = printLabel.getProductNo();
         			}else if(k==2){
         				value = printLabel.getPrimeName();
         			}else if(k==3){
         				value = printLabel.getGeneOrder();
         			}else if(k==4){
         				value = printLabel.getTbn()+"";
         			}else if(k==5){
         				value = printLabel.getTm()+"";
         			}else if(k==6){
         				value = printLabel.getGc()+"";
         			}else if(k==7){
         				value = printLabel.getMv()+"";
         			}else if(k==8){
         				value = printLabel.getUgTB()+"";//===========需要确认μg/OD
         			}else if(k==9){
         				value = printLabel.getTbn()+"";//============OD/μmole
         			}else if(k==10){
         				value = printLabel.getOdTB()+"";
         			}else if(k==11){
         				value = printLabel.getNmolTB()+"";
         			}else if(k==12){
         				value = printLabel.getTbn()+"";//==============μl/100μM
         			}
					if (!"null".equals(value)) {
         				cell.setCellValue(value);
         			}
         		}
         		rowNum+=1;
  
        	}
        	fos = new FileOutputStream(strFilePath + strFileName);
    		// 把相应的Excel 工作簿存盘
    		workbook.write(fos);
			
    		fileNames.add(strFileName);
		}
		
		//打包
    	String zipFileName = System.currentTimeMillis() + ".zip";
		ZipOutputStream zipWrit = new ZipOutputStream(new FileOutputStream(strFilePath + zipFileName));
		for (String fileName : fileNames) {
			File file = new File(strFilePath +fileName);
			FileInputStream fileIn = new FileInputStream(file);
			zipWrit.putNextEntry(new ZipEntry(file.getName()));
			byte[] bytes = new byte[1204];
			while (fileIn.read(bytes) > 0) {
				zipWrit.write(bytes);
			}
			zipWrit.closeEntry();
			fileIn.close();
			file.delete();
		}
		zipWrit.flush();
		zipWrit.close();

		
		File file = new File(strFilePath, zipFileName); 
		
		return Replys.with(file).as(Raw.class).downloadFileName(zipFileName);
    }
	
	/**
	 * 出库单打印对象组织
	 * @param orderPage
	 * @param pageable
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public Page<OrderInfoList>  convertOutbound(Page<Order> orderPage,Pageable pageable) throws IllegalStateException, IOException {
		//生产编号（头尾）、碱基总数
		OrderInfoList orderInfo = new OrderInfoList();
		List<OrderInfoList> orderInfoList = new ArrayList<OrderInfoList>();
		
		for(Order order:orderPage.getContent()){
			orderInfo = getOutbound(order);
			orderInfoList.add(orderInfo);
		}
		
		Page<OrderInfoList> orderListPage = new PageImpl<OrderInfoList>(orderInfoList,pageable,orderPage.getSize());
		
        return orderListPage;
    }
	
	public OrderInfoList getOutbound(Order order){
		OrderInfoList orderInfo = new OrderInfoList();
		
		//组织订单列表对象
		orderInfo.setOrderNo(order.getOrderNo());
		orderInfo.setCustomerName(order.getCustomerName());
		//调整值获取
		orderInfo.setCustomerPhoneNm("13566959955");
		//业务员 调整值获取
		orderInfo.setHandlerCode("98834342");
		//收样日期
		orderInfo.setCreateTime(order.getCreateTime());
		//单据编号 调整值获取 自动生成10位流水号
		orderInfo.setMakingNo("0000000010");
		//制单人（当前系统操作人员） 调整值获取 根据登陆信息获取
		orderInfo.setOperatorCode("34343434");
		//联系人（客户管理中的联系人） 调整值获取
		orderInfo.setLinkName("张三");
		//制单日期
		orderInfo.setMakingDate(new Date());
		//商品编码 默认赋值
		orderInfo.setCommodityCode("06030008");
		//货物名称 默认赋值
		orderInfo.setCommodityName("DNA合成");
		
		return orderInfo;
	}
}
