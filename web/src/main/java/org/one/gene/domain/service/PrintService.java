package org.one.gene.domain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerLabelConfig;
import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.entity.PrintLabel;
import org.one.gene.excel.ExcelCreateUtil;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerLabelConfigRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.web.order.OrderInfo;
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
		int totalcolumns = totalListCount.divide(new BigDecimal(primerLabelConfig.getColumnType().getValue()), 0, BigDecimal.ROUND_UP).intValue();//总共的行数
		
		
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
     * 导出文件
     * @throws IOException 
     * */
	public EntityReply<File> exportFile( String orderNo,String flag, Invocation inv) throws IOException {
		
		EntityReply<File> fileStr = null;
		if ("0".equals(flag)) {
			fileStr = this.exportReport(orderNo, inv);
		} else if ("1".equals(flag)) {
			fileStr = this.exportEnvelope(orderNo, inv);
		}
    		
      return fileStr;
	}
	
	
    /**
     * 导出打印报告单文件
     * @throws IOException 
     * */
	public EntityReply<File> exportReport( String orderNo, Invocation inv) throws IOException {
		
		
		//读取上机表文件输出地址
		InputStream inputStream  =   this.getClass().getClassLoader().getResourceAsStream("application.properties");  
		Properties p = new  Properties(); 
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Order order = orderRepository.findByOrderNo(orderNo);
		String strFilePath = p.getProperty("sjbPath");
		String report_templet_Path = p.getProperty("report_templet_Path");
		String customerCode = order.getCustomerCode();//客户代码
		String customerName = order.getCustomerName();//客户名称
		Customer customer = customerRepository.findByCode(customerCode);
		String strFileName = customerCode+"-"+System.currentTimeMillis()+".xls";
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
        String currentTime = df.format(new Date());
		
		BigDecimal orderODTotal = new BigDecimal(0);//订单OD总量
		BigDecimal orderTbTotal = new BigDecimal(0);//订单总管数
		BigDecimal ugOD = new BigDecimal(0);//ug/OD
		String purifyType = "";
		PrintLabel printLabel = new PrintLabel();
		List<PrintLabel> printLabels = new ArrayList<PrintLabel>();
		for (PrimerProduct primerProduct : order.getPrimerProducts()) {
			
			purifyType = primerProduct.getPurifyType();
			
			printLabel = new PrintLabel();

			// 生产编号
			if (!"".equals(primerProduct.getProductNo())) {
				printLabel.setProductNo(primerProduct.getProductNo());
			} else {
				printLabel.setProductNo(primerProduct.getOutProductNo());
			}

			printLabel.setPrimeName(primerProduct.getPrimeName());// 引物名称
			printLabel.setOrderNo(primerProduct.getOrder().getOrderNo());// 订单号
			printLabel.setGeneOrder(primerProduct.getGeneOrder());// 引物序列
			// 修饰
			String midi = "";
			if (!"".equals(primerProduct.getModiFiveType())) {
				midi += primerProduct.getModiFiveType() + ",";
			}
			if (!"".equals(primerProduct.getModiMidType())) {
				midi += primerProduct.getModiMidType() + ",";
			}
			if (!"".equals(primerProduct.getModiSpeType())) {
				midi += primerProduct.getModiSpeType() + ",";
			}
			if (!"".equals(primerProduct.getModiThreeType())) {
				midi += primerProduct.getModiThreeType() + ",";
			}
			if (!"".equals(midi)) {
				midi = "(" + midi.substring(0, midi.length() - 1) + ")";
				printLabel.setMidi(midi);
			}

			for (PrimerProductValue primerProductValue : primerProduct.getPrimerProductValues()) {
				PrimerValueType type = primerProductValue.getType();
				if (type.equals(PrimerValueType.odTotal)) {// OD总量
					orderODTotal = orderODTotal.add(primerProductValue.getValue());
					printLabel.setOdTotal(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.odTB)) {// OD/TB
					printLabel.setOdTB(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.nmolTotal)) {// NUML总量
					printLabel.setNmolTotal(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.nmolTB)) {// NUML/TB
					printLabel.setNmolTB(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.baseCount)) {// 碱基数
					printLabel.setTbn(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.MW)) {// MW
					printLabel.setMw(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.TM)) {// TM
					printLabel.setTm(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.mv)) {// MV
					printLabel.setMv(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.GC)) {// GC
					printLabel.setGc(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.μgOD)) {//μg/OD='nmol/tube' * 'MW' /1000
					ugOD = primerProductValue.getValue();
					printLabel.setUgOD(primerProductValue.getValue());
				} else if (type.equals(PrimerValueType.ODμmol)) {
					printLabel.setOdμmol(primerProductValue.getValue());
				} else if(type.equals(PrimerValueType.tb)){
					orderTbTotal = orderTbTotal.add(primerProductValue.getValue());
				}
				
				if (type.equals(PrimerValueType.nmolTB)) {// 加水量
					printLabel.setPmole(new BigDecimal(10)
							.multiply(primerProductValue.getValue()));
				}
			}
			printLabels.add(printLabel);
		  }
		    //形成Excel
			boolean jwzh = false;
			if (customerName.startsWith("金唯智")) {
				jwzh = true;
			}
			//读取模板,现在是两种模板。每个公司放一个模板
			String templetName = "";
			if (jwzh) {
				templetName = "genewiz.xls";
			}else{
				templetName = customerCode+".xls";
			}
			
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(report_templet_Path+templetName));
	        HSSFSheet sheet = workbook.getSheetAt(0);
			FileOutputStream fos = null;
			HSSFRow row = null;
			HSSFCell cell = null;
			int startRowNum = 0;
			if (jwzh) {
				startRowNum = 6;
				
				row = sheet.getRow(1);
				cell = row.getCell(1);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("金唯智订单号："+orderNo);
				
				row = sheet.getRow(2);
				cell = row.getCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("合成规格："+orderODTotal+" OD");
				
				cell = row.getCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("纯化方式："+purifyType);
				
				row = sheet.getRow(3);
				cell = row.getCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("发货规格：1OD（干粉）X"+orderTbTotal);
				
				row = sheet.getRow(3);
				cell = row.getCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("完成日期：");
				
			} else {
				startRowNum = 12;
				
				row = sheet.getRow(4);
				cell = row.getCell(6);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(ugOD+"μg");
				
				row = sheet.getRow(6);
				cell = row.getCell(2);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(purifyType);
				
				row = sheet.getRow(8);
				cell = row.getCell(2);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(customer.getName());
				cell = row.getCell(8);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(currentTime);
				
			}
			
			
			
        	for (PrintLabel printLabelExcel:printLabels) {
        		row = sheet.getRow(startRowNum);
         		
        		if(jwzh){
        			for (int k=1;k<13;k++){
        				cell = row.getCell(k);//产生单元格
        				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        				//往单元格中写入信息
        				String value = "";
        				if(k==1){
        					value = printLabelExcel.getProductNo();
        				}else if(k==2){
        					value = printLabelExcel.getPrimeName();
        				}else if(k==3){
        					value = printLabelExcel.getGeneOrder();
        				}else if(k==4){
        					value = printLabelExcel.getTbn()+"";
        				}else if(k==5){
        					value = printLabelExcel.getTm()+"";
        				}else if(k==6){
        					value = printLabelExcel.getGc()+"";
        				}else if(k==7){
        					value = printLabelExcel.getMv()+"";
        				}else if(k==8){
        					value = printLabelExcel.getUgOD()+"";
        				}else if(k==9){
        					value = printLabelExcel.getOdμmol()+"";
        				}else if(k==10){
        					value = printLabelExcel.getOdTB()+"";
        				}else if(k==11){
        					value = printLabelExcel.getNmolTB()+"";
        				}else if(k==12){
        					value = "";                         //==============μl/100μM
        				}
        				if (!"null".equals(value)) {
        					cell.setCellValue(value);
        				}
        			}
        				
        			}else{
        				for (int k=0;k<12;k++){
            				cell = row.getCell(k);//产生单元格
            				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            				//往单元格中写入信息
            				String value = "";
            				if(k==0){
            					value = printLabelExcel.getProductNo();
            				}else if(k==1){
            					value = printLabelExcel.getPrimeName();
            				}else if(k==2){
            					value = printLabelExcel.getGeneOrder();
            				}else if(k==3){
            					value = printLabelExcel.getTbn()+"";
            				}else if(k==4){
            					value = printLabelExcel.getOdTotal()+"";
            				}else if(k==5){
            					value = printLabelExcel.getOdTB()+"";
            				}else if(k==6){
            					value = printLabelExcel.getMw()+"";
            				}else if(k==7){
            					value = printLabelExcel.getTm()+"";
            				}else if(k==8){
            					value = printLabelExcel.getGc()+"";
            				}else if(k==9){
            					value = printLabelExcel.getNmolTB()+"";
            				}else if(k==10){
            					value = printLabelExcel.getPmole()+"";
            				}else if(k==11){
            					value = printLabelExcel.getMidi()+"";
            				}

            				if (!"null".equals(value)) {
            					cell.setCellValue(value);
            				}
            			}
        			}
        		
        			startRowNum+=1;
        	}
        	
        	//处理最后一行
        	if(jwzh){
        	//合并单元格
	        	CellRangeAddress range = new CellRangeAddress(startRowNum,startRowNum,1,12);
	        	sheet.addMergedRegion(range);
	        	
	        	//放入公司的相关信息
	        	row = sheet.getRow(startRowNum);
	        	cell = row.getCell(1);//产生单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("金唯智生物科技（北京）有限公司         电话："+customer.getPhoneNo()+"      传真：010-59458058         "+customer.getWebSite());
			}else{
	        	CellRangeAddress range = new CellRangeAddress(startRowNum,startRowNum,0,11);
	        	sheet.addMergedRegion(range);
	        	
	        	//放入公司的相关信息
	        	row = sheet.getRow(startRowNum);
	        	cell = row.getCell(0);//产生单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("     Release by _________      Phone : "+customer.getPhoneNo()+"     "+customer.getWebSite()+"    "+customer.getEmail());
			}
			
        	
        	fos = new FileOutputStream(strFilePath + strFileName);
    		// 把相应的Excel 工作簿存盘
    		workbook.write(fos);
		

		   //读取文件
		   File file = new File(strFilePath, strFileName);
		
		return Replys.with(file).as(Raw.class).downloadFileName(strFileName);
    }

    /**
     * 导出打印信封文件
     * @throws IOException 
     * */
	public EntityReply<File> exportEnvelope( String orderNo, Invocation inv) throws IOException {
		
		
		//读取上机表文件输出地址
		InputStream inputStream  =   this.getClass().getClassLoader().getResourceAsStream("application.properties");  
		Properties p = new  Properties(); 
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Order order = orderRepository.findByOrderNo(orderNo);
		String strFilePath = p.getProperty("sjbPath");
		String report_templet_Path = p.getProperty("report_templet_Path");
		String customerCode = order.getCustomerCode();//客户代码
		String customerName = order.getCustomerName();//客户名称
		Customer customer = customerRepository.findByCode(customerCode);
		String strFileName = customerCode+"-"+System.currentTimeMillis()+".xls";
		
		BigDecimal orderBaseCount = new BigDecimal(0);//订单碱基数总量

		for (PrimerProduct primerProduct : order.getPrimerProducts()) {
			for (PrimerProductValue primerProductValue : primerProduct.getPrimerProductValues()) {
				PrimerValueType type = primerProductValue.getType();
				if (type.equals(PrimerValueType.baseCount)) {// 碱基数
					orderBaseCount = orderBaseCount.add(primerProductValue.getValue());
				}
			}
		  }
	    
		//形成Excel
		//读取信封模板
		String templetName = "envelope.xls";
		
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(report_templet_Path+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		FileOutputStream fos = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		
		row = sheet.getRow(0);
		cell = row.getCell(0);//得到单元格
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(orderNo);
		cell = row.getCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(orderBaseCount+"");
        
		row = sheet.getRow(1);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(customer.getName());
		
		row = sheet.getRow(2);
		cell = row.getCell(3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(customer.getName());
		
    	fos = new FileOutputStream(strFilePath + strFileName);
		// 把相应的Excel 工作簿存盘
		workbook.write(fos);

	   //读取文件
	   File file = new File(strFilePath, strFileName);
		
	   return Replys.with(file).as(Raw.class).downloadFileName(strFileName);
    }
	
	
	/**
	 * 出库单打印查询信息对象组织
	 * @param orderPage
	 * @param pageable
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public Page<OrderInfo>  convertOutbound(Page<Order> orderPage,Pageable pageable) throws IllegalStateException, IOException {
		//生产编号（头尾）、碱基总数
		OrderInfo orderInfo = new OrderInfo();
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		
		for(Order order:orderPage.getContent()){
			orderInfo = getOutbound(order);
			orderInfoList.add(orderInfo);
		}
		
		Page<OrderInfo> orderListPage = new PageImpl<OrderInfo>(orderInfoList,pageable,orderPage.getSize());
		
        return orderListPage;
    }
	
	/**
	 * 出库单打印对象组织，生成打印excel
	 * @throws FileNotFoundException 
	 */
	public EntityReply<File> printOutbound(List<OrderInfo> orderInfoLists,Invocation inv) throws Exception{
		OrderInfo orderInfo = new OrderInfo();
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		for(OrderInfo orders:orderInfoLists){
			Order order = orderRepository.findByOrderNo(orders.getOrderNo());
			orderInfo = getOutbound(order);
			orderInfoList.add(orderInfo);
		}
		
		String filePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views\\downLoad\\template\\outboundTemplate.xlsx";
		String outFilePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views\\print\\outboundZip\\";
		//压缩包名称
		String zipFileName = "";
		int count = 0;
		String starName = "";
		String endName = "";
		System.out.println("文件下载路径filePath::::::"+filePath);
		System.out.println("文件下载路径outFilePath::::::"+outFilePath);
		List<String> fileNames = new ArrayList<String>();
		ExcelCreateUtil exportExcel = new ExcelCreateUtil();
		for(OrderInfo outbound:orderInfoList){
		
			exportExcel.setSrcPath(filePath);
			exportExcel.setDesPath(outFilePath+outbound.getOrderNo()+".xlsx");
			exportExcel.setSheetName("sheet1");
			exportExcel.getSheetBySheetName();
			Sheet sheet = exportExcel.getSheet();
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			//在相应的单元格进行赋值  
			Cell cell23 = sheet.getRow(1).getCell(2);  
	        cell23.setCellValue(sf.format(outbound.getCreateTime()));  
	        Cell cell26 = sheet.getRow(1).getCell(5);  
	        cell26.setCellValue(outbound.getOrderNo());  
	        Cell cell33 = sheet.getRow(2).getCell(2);  
	        cell33.setCellValue(outbound.getCustomerName());  
	        Cell cell36 = sheet.getRow(2).getCell(5);  
	        cell36.setCellValue("测试4"); 
	        Cell cell43 = sheet.getRow(3).getCell(2);  
	        cell43.setCellValue(sf.format(outbound.getMakingDate()));  
	        Cell cell46 = sheet.getRow(3).getCell(5);  
	        cell46.setCellValue(outbound.getOperatorCode());  
	        Cell cell63 = sheet.getRow(5).getCell(2);  
	        cell63.setCellValue(outbound.getHandlerCode());  
	        fileNames.add(outbound.getOrderNo()+".xlsx");
			exportExcel.outputExcel();
			
			 count++;
			 if(count==1) {
				 starName = outbound.getOrderNo();
		     }
			 if(count==orderInfoList.size()) {
				 endName = "~"+outbound.getOrderNo();
		     }
		}
		
		
		zipFileName = starName+endName+".zip";
		
		exportExcel.outputExcelZip(fileNames,outFilePath,zipFileName);
		
        File file = new File(outFilePath, zipFileName); 
		
		return Replys.with(file).as(Raw.class).downloadFileName(zipFileName);
	}
	
	public OrderInfo getOutbound(Order order){
		OrderInfo orderInfo = new OrderInfo();
		
		//组织订单列表对象
		orderInfo.setOrderNo(order.getOrderNo());
		orderInfo.setCustomerName(order.getCustomerName());
		orderInfo.setCustomerPhoneNm(customerRepository.findByCode(order.getCustomerCode()).getPhoneNo());
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
		//orderInfo.setCommodityCode("06030008");
		//货物名称 默认赋值
		//orderInfo.setCommodityName("DNA合成");
		
		return orderInfo;
	}
}
