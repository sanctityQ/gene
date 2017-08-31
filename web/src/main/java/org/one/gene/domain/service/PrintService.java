package org.one.gene.domain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
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
	public List<PrimerProduct> getPrimerProducts(String boardNo, String noType, Invocation inv) {
		List<PrimerProduct> primerProducts = new ArrayList<PrimerProduct>();
		List<PrimerProduct> primerProductBoardNOs =  new ArrayList<PrimerProduct>();
		if (!"".equals(boardNo)) {
			if ("1".equals(noType)) {
				Board board = boardRepository.findByBoardNo(boardNo);
				for(BoardHole bh:board.getBoardHoles()){
					primerProducts.add(bh.getPrimerProduct());
				}
			}else if("2".equals(noType)){
				PrimerProduct primerProduct = primerProductRepository.findByProductNoOrOutProductNo(boardNo, boardNo);
				Order order = orderRepository.findByOrderNo(primerProduct.getOrder().getOrderNo());
				if (order != null&order.getPrimerProducts().size()>0) {
					for(PrimerProduct pp:order.getPrimerProducts()){
						primerProducts.add(pp);
					}
				}
				
			}
		}
			
		return primerProducts;
			
	}
    /**
     * 从生产数据得到订单List
     * @throws Exception 
     * */
	public List<Order> getPrintLabelList(List<PrimerProduct> primerProducts, Invocation inv) throws Exception {
		
		List<Order> orders = new ArrayList<Order>();
		Map<String, Order> ccMap = new HashMap<String, Order>();
		Order order = new Order();
		String zixiCode = "";
		String zixiName = "";
		List<Customer> customers = customerRepository.seachHaveZiXi();
		if (customers != null && customers.size() > 0) {
			Customer customerTemp = (Customer)customers.get(0);
			zixiCode = customerTemp.getCode();
			zixiName = customerTemp.getName();
		}
		
		for (PrimerProduct primerProduct : primerProducts) {
			String customerCode = primerProduct.getOrder().getCustomerCode();
			String customerName = primerProduct.getOrder().getCustomerName();
					
			Customer customer = customerRepository.findByCode(customerCode);
			if (customer != null && "2".equals(customer.getCustomerFlag())) {//直接客户都汇总到梓熙下面
				customerCode = zixiCode;
				customerName = zixiName;
			}
			
			if (ccMap.get(customerCode) == null) {
				order = new Order();
				order.setCustomerCode(customerCode);
				order.setCustomerName(customerName);
			}else{
				order = ccMap.get(customerCode);
			}
			
			order.getPrimerProducts().add(primerProduct);
			ccMap.put(order.getCustomerCode(), order);
			
		}

		for (Map.Entry<String, Order> orderMap : ccMap.entrySet()) {
			orders.add(exportLabel( orderMap.getValue(), inv));
		}
		
		return orders;
	}
	
    /**
     * 导出打印标签文件
     * @throws Exception 
     * */
	public Order exportLabel(Order order, Invocation inv) throws Exception {
		 
		String filePath = File.separator+"gene"+File.separator+"upExcel"+File.separator+"printLabel"+File.separator;
		String strFilePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"upExcel"+File.separator+"printLabel"+File.separator;
		String strFileName = order.getCustomerCode()+System.currentTimeMillis()+".xls";
		
		PrintLabel printLabel = new PrintLabel();
		List<PrintLabel> printLabels = new ArrayList<PrintLabel>();
		PrimerProduct primerProduct = new PrimerProduct();
		
		for (PrimerProduct primerProductTemp : order.getPrimerProducts()) {
			
			//重新查询数据
			primerProduct = primerProductRepository.findOne(primerProductTemp.getId());
					
			printLabel = new PrintLabel();
			
			boolean isM = false;//是否是混合型的
			if (primerProduct.getLiquid() != null
					&& !"".equals(primerProduct.getLiquid())
					&& !"null".equals(primerProduct.getLiquid())) {
				isM = true;
			}
			//生产编号
			if(!"".equals(primerProduct.getProductNo())){
				printLabel.setProductNo(primerProduct.getProductNo());
			}else{
				printLabel.setProductNo(primerProduct.getOutProductNo());
			}
			
			printLabel.setPrimeName(primerProduct.getPrimeName());//引物名称
			printLabel.setOrderNo(primerProduct.getOrder().getOutOrderNo());//订单号,有外部的显示外部的，没有显示内部的
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
	        String createTime = df.format(primerProduct.getOrder().getCreateTime());
			printLabel.setRemark(createTime);// 日期：20151010
			printLabel.setMeasureVolume(primerProduct.getMeasureVolume()+"");//分装体积
			printLabel.setDensity(primerProduct.getDensity()+"");//浓度
			
			//查询孔号
			List<BoardHole> boardHoles = boardHoleRepository.findByPrimerProduct(primerProduct);
			if (boardHoles != null && boardHoles.size() > 0) {
				for(BoardHole boardHole :boardHoles){
					if(boardHole.getBoard().getBoardNo().equals(primerProduct.getBoardNo())){//是同一个板
						printLabel.setSiteNo(boardHole.getHoleNo());//位置号
						break;
					}
				}
			}
			
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
				}else if(type.equals(PrimerValueType.μgOD)){//ug/OD
					printLabel.setUgOD(primerProductValue.getValue());
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
				//如果是混合型的：生产编号：含有  M的  为混合型。需要打印两次标签：干粉+液体
				if(i==0){
					printLabels.add(printLabel);
				}else{
					if(isM){
						PrintLabel printLabelNew = new PrintLabel();
						printLabelNew.setProductNo(printLabel.getProductNo());
						printLabelNew.setPrimeName(printLabel.getPrimeName());//引物名称
						printLabelNew.setOrderNo(printLabel.getOrderNo());//订单号,有外部的显示外部的，没有显示内部的
						printLabelNew.setRemark(printLabel.getRemark());// 日期：20151010
						printLabelNew.setMeasureVolume(printLabel.getMeasureVolume());//分装体积
						printLabelNew.setSiteNo(printLabel.getSiteNo());//位置号
						printLabelNew.setMidi(printLabel.getMidi());
						printLabelNew.setMw(printLabel.getMw());
						printLabelNew.setTube(printLabel.getTube());
						printLabelNew.setOdTotal(printLabel.getOdTotal());
						printLabelNew.setOdTB(printLabel.getOdTB());
						printLabelNew.setNmolTotal(printLabel.getNmolTotal());
						printLabelNew.setNmolTB(printLabel.getNmolTB());
						printLabelNew.setTbn(printLabel.getTbn());
						printLabelNew.setTm(printLabel.getTm());
						printLabelNew.setGc(printLabel.getGc());
						printLabelNew.setUgOD(printLabel.getUgOD());
						printLabelNew.setPmole(printLabel.getPmole());
						printLabelNew.setDensity("");
						
						printLabels.add(printLabelNew);
					}else{
						printLabels.add(printLabel);
					}
				}
			}
			
		}
			
		//形成Excel
		//读取配置信息
		PrimerLabelConfig primerLabelConfig = null;
		Customer customer = customerRepository.findByCode(order.getCustomerCode());
		//直接客户，使用梓熙生物的配置
		if (customer != null && ("0".equals(customer.getCustomerFlag()) || "2".equals(customer.getCustomerFlag()))) {

			List<Customer> customers = customerRepository.seachHaveZiXi();
			if (customers != null && customers.size() > 0) {
				Customer customerTemp = (Customer)customers.get(0);
				primerLabelConfig = primerLabelConfigRepository.findByCustomerCode(customerTemp.getCode());
			}
			if (primerLabelConfig == null) {
				throw new Exception("梓熙生物的引物标签打印信息为空，请配置！");
			}
			
		} else {
			primerLabelConfig = primerLabelConfigRepository.findByCustomerCode(order.getCustomerCode());
			if (primerLabelConfig == null) {
				throw new Exception(order.getCustomerName()+ "的引物标签打印信息为空，请配置！");
			}
		}
		

		//计算第一列多少行
		int columnType = primerLabelConfig.getColumnType().getValue();
		BigDecimal totalListCount = new BigDecimal(printLabels.size());//本excel的条数
		int totalcolumns = totalListCount.divide(new BigDecimal(columnType), 0, BigDecimal.ROUND_UP).intValue();//总共的行数
		
		
		FileOutputStream fos = null;
		HSSFWorkbook workbook = new HSSFWorkbook(); //产生工作簿对象
		
		//每行逐行放数据
		int rowNum = 1;
		int rowNumForpage  = 1;
		int columNum = 1;
		int sheetNum = -1;
		HSSFSheet sheet = null;
		List<PrimerLabelConfigSub> primerLabelConfigSubs = primerLabelConfig.getPrimerLabelConfigSubs();
		
		for (PrintLabel printLabelExcel : printLabels) {
			
			if(rowNum==1){
				sheetNum +=1;
				sheet = workbook.createSheet(); //产生工作表对象
				//设置工作表的名称
				workbook.setSheetName(sheetNum,"第"+(sheetNum+1)+"页");
			}
			HSSFRow row = null;
			if(columNum ==1){
				row = sheet.createRow((short)0);//第一行放描述
				for (int k=0;k<primerLabelConfigSubs.size();k++){
					String typeDesc = ((PrimerLabelConfigSub) primerLabelConfigSubs.get(k)).getTypeDesc();
					
					for(int n=0;n<columnType;n++){
						HSSFCell cell = row.createCell((short) ((n)*primerLabelConfigSubs.size()+k));//产生单元格
						//设置单元格内容为字符串型
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						//往单元格中写入信息
						cell.setCellValue(typeDesc);
					}
				}
				
				row = sheet.createRow((short)rowNum);//从第二行开始记录
			}else{
				row = sheet.getRow(rowNumForpage);
			}
			
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
				}else if("tb".equals(type)){
					value = printLabelExcel.getTube()+"";
				}else if("odTotal".equals(type)){
					value = printLabelExcel.getOdTotal()+"";
				}else if("odTB".equals(type)){
					value = printLabelExcel.getOdTB()+"";
				}else if("nmolTotal".equals(type)){
					value = printLabelExcel.getNmolTotal()+"";
				}else if("nmolTB".equals(type)){
					//如果是液体，则显示分装体积
					if(!"".equals(printLabelExcel.getDensity()) && !"null".equals(printLabelExcel.getDensity())){
						value = printLabelExcel.getDensity();
					}else{
						value = printLabelExcel.getNmolTB()+"";	
					}
				}else if("tbn".equals(type)){
					value = printLabelExcel.getTbn()+"";
				}else if("MW".equals(type)){
					value = printLabelExcel.getMw()+"";
				}else if("TM".equals(type)){
					value = printLabelExcel.getTm()+"";
				}else if("GC".equals(type)){
					value = printLabelExcel.getGc()+"";
				}else if("ugTB".equals(type)){
					value = printLabelExcel.getUgTB()+"";
				}else if("100pmoleμl".equals(type)){
					//如果是液体，则显示分装体积
					if(!"".equals(printLabelExcel.getDensity()) && !"null".equals(printLabelExcel.getDensity())){
						value = printLabelExcel.getMeasureVolume();	
					}else{
						value = printLabelExcel.getPmole()+"";	
					}
				}else if("remark".equals(type)){
					value = printLabelExcel.getRemark();
				}else if("midi".equals(type)){
					value = printLabelExcel.getMidi();
				}else if("μgOD".equals(type)){
					value = printLabelExcel.getUgOD()+"";
				}else if("siteNo".equals(type)){
					value = printLabelExcel.getSiteNo();
				}
				
				if (!"null".equals(value)) {
					cell.setCellValue(value);
				}
			}
			
			rowNum+=1;
			rowNumForpage+=1;
			if(totalcolumns == (rowNumForpage-1)){
				columNum+=1;
				rowNumForpage = 1;
			}
			
		}
		fos = new FileOutputStream(strFilePath + strFileName);
		// 把相应的Excel 工作簿存盘
		workbook.write(fos);
    
		order.setFileName(filePath + strFileName);
		
		return order;
    }
	
    /**
     * 导出打印报告单文件
     * @throws IOException 
     * */
	public void exportReports( String orderNoStr, Invocation inv) throws IOException {

		//分解订单号
		String[] orderNos = orderNoStr.split(",");
		int orderNum = orderNos.length;//订单个数
		
		String fileName = "批量支付文件" + ".zip";  
		HttpServletResponse response = inv.getResponse();
		OutputStream out = response.getOutputStream();
		ZipOutputStream zipOutputStream = null;
		
		if( orderNum==1 ){//选择了一个订单
			fileName = orderNos[0] + ".xls";  
			response.setContentType("application/x-msdownload");
			
		}else{
			fileName = "批量生成报告单" + ".zip";
			response.setContentType("application/octet-stream ");
			zipOutputStream = new ZipOutputStream(out);
		}
        
        response.setHeader("Connection", "close"); // 表示不能用浏览器直接打开  
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));  
        response.setCharacterEncoding("UTF-8");
		
		for (int o = 0; o < orderNos.length; o++) {
			String orderNo = orderNos[o];
			
			Order order = orderRepository.findByOrderNo(orderNo);
			String customerCode = order.getCustomerCode();//客户代码
			String customerName = order.getCustomerName();//客户名称
			String outOrderNo   = order.getOutOrderNo();//外部订单号
			String customerPhoneNo = "";
			String customerWebSite = "";
			String customerEmail = "";
			String customerFlag  ="";
			Customer customer = customerRepository.findByCode(customerCode);
			if(customer!=null){
				customerPhoneNo = customer.getPhoneNo();
				customerWebSite = customer.getWebSite();
				customerEmail   = customer.getEmail();
				customerFlag    = customer.getCustomerFlag();
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			String currentTime = df.format(new Date());
			String importTime = df.format(order.getCreateTime());
			
			BigDecimal odTotal = new BigDecimal(0);//一个生产编号的OD总量
			BigDecimal tb = new BigDecimal(0);//一个生产编号的管数
			BigDecimal odTube = new BigDecimal(0);//一个生产编号的od/tube
			BigDecimal ugOD = new BigDecimal(0);//ug/OD
			String purifyType = "";
			String measureVolume = "";//测值体积
			String density = "";//浓度(P)
			
			PrintLabel printLabel = new PrintLabel();
			
			List<PrintLabel> printLabels = new ArrayList<PrintLabel>();
			for (PrimerProduct primerProduct : order.getPrimerProducts()) {
				printLabel = new PrintLabel();
				
				purifyType    = primerProduct.getPurifyType();
				measureVolume = primerProduct.getMeasureVolume()+"";
				density       = primerProduct.getDensity()+"";
				printLabel.setDensity(density);
				if(!"".equals(density) && !"null".equals(density)){
					printLabel.setMeasureVolume(measureVolume);
				}
				
				// 生产编号
				if (!"".equals(primerProduct.getProductNo())) {
					printLabel.setProductNo(primerProduct.getProductNo());
				} else {
					printLabel.setProductNo(primerProduct.getOutProductNo());
				}
				
				printLabel.setPrimeName(primerProduct.getPrimeName());// 引物名称
				printLabel.setOrderNo(primerProduct.getOrder().getOrderNo());// 订单号
				printLabel.setOutOrderNo(outOrderNo);//外部订单号
				
				//处理 引物序列(原始) ，如果长度30个字符回车一次
				String geneOrderMidi = primerProduct.getGeneOrderMidi();
				String newGeneOrderMidi = "";
				int perNum = 30;
				int num = new BigDecimal(geneOrderMidi.length()).divide(new BigDecimal(perNum), 0, BigDecimal.ROUND_UP).intValue();
				if (num == 1) {
					newGeneOrderMidi = geneOrderMidi;
				} else {
					for (int a = 0; a < num; a++) {
						if (geneOrderMidi.length() > perNum) {
							newGeneOrderMidi += geneOrderMidi.substring(0, perNum) + "\r\n";
							geneOrderMidi = geneOrderMidi.substring(perNum);
						} else {
							newGeneOrderMidi += geneOrderMidi;
							geneOrderMidi = "";
						}
					}
				}
				printLabel.setGeneOrder(newGeneOrderMidi);//
				
				// 修饰
				String midi = "";
				if (!"".equals(primerProduct.getModiFiveType())) {
					midi += "5'"+primerProduct.getModiFiveType() + ",";
				}
				if (!"".equals(primerProduct.getModiMidType())) {
					midi += primerProduct.getModiMidType() + ",";
				}
				if (!"".equals(primerProduct.getModiSpeType())) {
					midi += primerProduct.getModiSpeType() + ",";
				}
				if (!"".equals(primerProduct.getModiThreeType())) {
					midi += "3'"+primerProduct.getModiThreeType() + ",";
				}
				if (!"".equals(midi)) {
					midi = "(" + midi.substring(0, midi.length() - 1) + ")";
					printLabel.setMidi(midi);
				}
				
				for (PrimerProductValue primerProductValue : primerProduct.getPrimerProductValues()) {
					PrimerValueType type = primerProductValue.getType();
					if (type.equals(PrimerValueType.odTotal)) {// OD总量
						odTotal = primerProductValue.getValue();
						printLabel.setOdTotal(primerProductValue.getValue());
					} else if (type.equals(PrimerValueType.odTB)) {// OD/TB
						odTube = primerProductValue.getValue();
						printLabel.setOdTB(primerProductValue.getValue());
					} else if (type.equals(PrimerValueType.nmolTotal)) {// NUML总量
						printLabel.setNmolTotal(primerProductValue.getValue());
					} else if (type.equals(PrimerValueType.nmolTB)) {// NUML/TB
						printLabel.setNmolTB(primerProductValue.getValue());
						printLabel.setPmole(new BigDecimal(10).multiply(primerProductValue.getValue()));// 加水量
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
						tb = primerProductValue.getValue();
					}
				}
				printLabels.add(printLabel);
			}
			
			String templetName = "";//读取模板,现在是两种模板。每个公司放一个模板
			boolean jinweizhi = false;//金唯智
			boolean meiji = false;//美吉
			boolean huada = false;//华大
			boolean qitadaili = false;//其他代理
			boolean zixi = false;//梓熙
			
			if (customerName.indexOf("金唯智") != -1) {
				jinweizhi = true;
				templetName = "jinweizhi.xls";
			} else if (customerName.indexOf("美吉") != -1) {
				meiji = true;
				templetName = "meiji.xls";
			} else if (customerName.indexOf("北京华大") != -1) {
				huada = true;
				templetName = "huada.xls";
			} else if ("1".equals(customerFlag)) {
				qitadaili = true;
				templetName = "qitadaili.xls";
			} else {
				zixi = true;
				templetName = "zixi.xls";
			}
			
			//形成Excel
			String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"report"+File.separator;
			
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row = null;
			HSSFCell cell = null;
			int totalCount = printLabels.size();//总条数
			int perPage   = 20;//每页条数
			int totalPage = new BigDecimal(totalCount).divide(new BigDecimal(perPage),0, BigDecimal.ROUND_UP).intValue();
			
			int startRowNum = 0;
			
			if (jinweizhi) {
				startRowNum = 7;
				perPage   = 17;//每页条数
				totalPage = new BigDecimal(totalCount).divide(new BigDecimal(perPage),0, BigDecimal.ROUND_UP).intValue();
				
				row = sheet.getRow(1);
				cell = row.getCell(1);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("金唯智订单号："+outOrderNo);
				
				row = sheet.getRow(2);
				cell = row.getCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("合成规格："+odTotal+" OD");
				
				cell = row.getCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("纯化方式："+purifyType);
				
				row = sheet.getRow(3);
				cell = row.getCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("发货规格："+odTube+"*"+tb);
				
				row = sheet.getRow(3);
				cell = row.getCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("完成日期："+currentTime);
				
			}else if(meiji){
				startRowNum = 6;
				perPage   = 21;//每页条数
				totalPage = new BigDecimal(totalCount).divide(new BigDecimal(perPage),0, BigDecimal.ROUND_UP).intValue();
			}else if(huada){
				startRowNum = 10;
				perPage   = 18;//每页条数
				totalPage = new BigDecimal(totalCount).divide(new BigDecimal(perPage),0, BigDecimal.ROUND_UP).intValue();
				
				row = sheet.getRow(3);
				cell = row.getCell(6);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(ugOD+"μg");
				
				row = sheet.getRow(5);
				cell = row.getCell(2);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(purifyType);
				
			}else if(qitadaili){
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
				cell.setCellValue(customerName);
				cell = row.getCell(8);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(currentTime);
				
			} else {
				startRowNum = 6;
				
				row = sheet.getRow(2);
				cell = row.getCell(0);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("Order number："+orderNo);
				cell = row.getCell(3);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("Purification："+purifyType);
				
				row = sheet.getRow(3);
				cell = row.getCell(0);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("Customer："+customerName);
				cell = row.getCell(3);//得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("Date：          "+importTime);
			}
			
			//先copy第一个sheet创建所有的sheet
			for (int i=0;i<totalPage;i++){
				if(i!=0){
					int totalSheet = workbook.getNumberOfSheets();
					sheet = workbook.cloneSheet(0);
					workbook.setSheetName(totalSheet, "第"+(i+1)+"页");
				}
			}
			
			
			int startRowNumOld = startRowNum;
			int numCur  = 0;
			int pageNum = 1;
			boolean addLast = true;
			for (PrintLabel printLabelExcel:printLabels) {
				
				if(pageNum==1){
					sheet = workbook.getSheetAt(pageNum-1);
				}
				row = sheet.getRow(startRowNum);
				
				if(jinweizhi){
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
							value = printLabelExcel.getMw()+"";
						}else if(k==8){
							value = printLabelExcel.getUgOD()+"";
						}else if(k==9){
							value = printLabelExcel.getOdμmol()+"";
						}else if(k==10){
							value = printLabelExcel.getOdTB()+"";
						}else if(k==11){
							value = printLabelExcel.getNmolTB()+"";
						}else if(k==12){
							value = printLabelExcel.getPmole()+"";
						}
						if (!"null".equals(value)) {
							cell.setCellValue(value);
						}
					}
					
				}else if(meiji){
					for (int k=0;k<13;k++){
						cell = row.getCell(k);//产生单元格
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						//往单元格中写入信息
						String value = "";
						if(k==0){
							value = printLabelExcel.getOutOrderNo();
						}else if(k==1){
							value = printLabelExcel.getProductNo();
						}else if(k==2){
							value = printLabelExcel.getPrimeName();
						}else if(k==3){
							value = printLabelExcel.getGeneOrder();
						}else if(k==4){
							value = printLabelExcel.getTbn()+"";
						}else if(k==5){
							value = printLabelExcel.getOdTotal()+"";
						}else if(k==6){
							value = printLabelExcel.getOdTB()+"";
						}else if(k==7){
							value = printLabelExcel.getMw()+"";
						}else if(k==8){
							value = printLabelExcel.getTm()+"";
						}else if(k==9){
							value = printLabelExcel.getGc()+"";
						}else if(k==10){
							value = printLabelExcel.getNmolTB()+"";
						}else if(k==11){
							value = printLabelExcel.getPmole()+"";
						}else if(k==12){
							value = printLabelExcel.getMidi()+"";
						}
						
						if (!"null".equals(value)) {
							cell.setCellValue(value);
						}
					}
				}else if(zixi){
					for (int k=0;k<14;k++){
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
							value = printLabelExcel.getNmolTotal()+"";
						}else if(k==5){
							value = printLabelExcel.getNmolTB()+"";
						}else if(k==6){
							value = printLabelExcel.getMw()+"";
						}else if(k==7){
							value = printLabelExcel.getTm()+"";
						}else if(k==8){
							value = printLabelExcel.getGc()+"";
						}else if(k==9){
							value = printLabelExcel.getOdTB()+"";
						}else if(k==10){
							value = printLabelExcel.getDensity()+"";
						}else if(k==11){
							value = printLabelExcel.getMeasureVolume()+"";
						}else if(k==12){
							value = printLabelExcel.getPmole()+"";
						}else if(k==13){
							value = printLabelExcel.getMidi()+"";
						}
						
						if (!"null".equals(value)) {
							cell.setCellValue(value);
						}
					}
				}
				else{
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
				
				
				if(addLast){
					//处理最后一行
					if(jinweizhi){
						row = sheet.getRow(24);
						cell = row.getCell(1);//产生单元格
						cell.setCellValue(cell.getStringCellValue()+ pageNum+"/"+totalPage);
					}
					addLast = false;
				}
				
				numCur += 1;
				startRowNum+=1;
				if (numCur == perPage && pageNum!=totalPage){
					numCur = 0;
					startRowNum = startRowNumOld;
					pageNum +=1;
					addLast = true;
					sheet = workbook.getSheetAt(pageNum-1);
				}
			}
			
			//输出文件到客户端
			if( orderNum==1 ){//选择了一个订单
				workbook.write(out);
			}else{
				ZipEntry entry = new ZipEntry(orderNo + ".xls");
				zipOutputStream.putNextEntry(entry);  
				workbook.write(zipOutputStream);
			}
		}

        // 关闭输出流 
		if( orderNum==1 ){//选择了一个订单
	           out.flush();
	           out.close();
		}else{
			zipOutputStream.flush();  
			zipOutputStream.close();
		}
        
    }

    /**
     * 导出打印信封文件
     * @throws IOException 
     * */
	public void exportEnvelope( String orderNo, Invocation inv) throws IOException {
		
		Order order = orderRepository.findByOrderNo(orderNo);
		String handlerName = order.getHandlerName();//业务员
		String customerName = order.getCustomerName();//客户公司名称
		String contactsName = order.getContactsName();//客户联系人
		String strFileName = System.currentTimeMillis()+".xls";
		
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
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"report"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
		row = sheet.getRow(0);
		cell = row.getCell(0);//得到单元格
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(orderNo);
		cell = row.getCell(4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(orderBaseCount+"bp");
		cell = row.getCell(5);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(handlerName);
        
		row = sheet.getRow(1);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(customerName);
		
		row = sheet.getRow(2);
		cell = row.getCell(3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(order.getProductNoMinToMax());
		
		row = sheet.getRow(3);
		cell = row.getCell(3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(contactsName);
		
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
		
		String templateName = "outboundTemplate.xlsx";
		String filePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+templateName;
		String outFilePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"print"+File.separator+"outboundZip"+File.separator;
		//压缩包名称
		String zipFileName = "";
		int count = 0;
		String starName = "";
		String endName = "";
		System.out.println("文件下载路径filePath::::::"+filePath);
		System.out.println("文件下载路径outFilePath::::::"+outFilePath);
		List<String> fileNames = new ArrayList<String>();
		ExcelCreateUtil exportExcel = new ExcelCreateUtil();

		for(OrderInfo orders:orderInfoLists){
			Order order = orderRepository.findByOrderNo(orders.getOrderNo());
			Customer customer = customerRepository.findByCode(order.getCustomerCode());
			
			exportExcel.setSrcPath(filePath);
			exportExcel.setDesPath(outFilePath+order.getOrderNo()+".xlsx");
			exportExcel.setSheetName("sheet1");
			exportExcel.getSheetBySheetName();
			Sheet sheet = exportExcel.getSheet();
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			//在相应的单元格进行赋值  
			Cell cell11 = sheet.getRow(0).getCell(0); 
			cell11.setCellValue(customer.getName()+"引物合成出库单");
			Cell cell23 = sheet.getRow(1).getCell(2);  
	        cell23.setCellValue(sf.format(order.getCreateTime()));  
	        Cell cell26 = sheet.getRow(1).getCell(5);  
	        cell26.setCellValue(order.getOrderNo());  
	        Cell cell33 = sheet.getRow(2).getCell(2);  
	        cell33.setCellValue(customer.getName());  
	        Cell cell36 = sheet.getRow(2).getCell(5);  
	        cell36.setCellValue(customer.getName()); 
	        Cell cell43 = sheet.getRow(3).getCell(2);  
	        cell43.setCellValue(sf.format(new Date()));  
	        Cell cell46 = sheet.getRow(3).getCell(5);  
	        //制单人（当前系统操作人员） 调整值获取 根据登陆信息获取
	        cell46.setCellValue("34343434");  
	        Cell cell63 = sheet.getRow(5).getCell(2);  
	        cell63.setCellValue(customer.getHandlerCode());  
	        //表格顶部创建结束
	        
	        //创建表格列表开始
	        int cellIndex = 0;
	        for(PrimerProduct primerProduct:order.getPrimerProducts()){
	        	
//	        	exportExcel.getCellStyle().setBorderBottom(exportExcel.getCellStyle().BORDER_THIN);
//	        	exportExcel.getCellStyle().setBorderLeft(exportExcel.getCellStyle().BORDER_THIN);
//	        	exportExcel.getCellStyle().setBorderRight(exportExcel.getCellStyle().BORDER_THIN);
//	        	exportExcel.getCellStyle().setBorderTop(exportExcel.getCellStyle().BORDER_THIN);
	        	for(int i=0;i<11;i++){
	        		Cell cell = sheet.getRow(cellIndex+7).getCell(i); 
		        	cell.setCellType(Cell.CELL_TYPE_STRING);
		        	switch(i){
		        	  case 0:
						  cell.setCellValue(primerProduct.getProductNo());
					    break;
					  case 1:
						  cell.setCellValue(primerProduct.getPrimeName());
					  	break;
					  case 2:
						  cell.setCellValue(primerProduct.getGeneOrder());
						break;
					  case 3:
						  cell.setCellValue(primerProduct.getGeneOrder().trim().length());
						break;
					  case 4:
						  cell.setCellValue(primerProduct.getOdTotal().toString());
						break;
					  case 5:
						  cell.setCellValue(primerProduct.getOdTotal().divide(primerProduct.getOdTB(),2,BigDecimal.ROUND_HALF_UP).toString());
						break;
					  case 6:
						  cell.setCellValue(primerProduct.getPurifyType());
						break;
					  case 7:
						  cell.setCellValue(primerProduct.getModiFiveType());
						break;
					  case 8:
						  cell.setCellValue(primerProduct.getModiThreeType());
						break;
					  case 9:
						  cell.setCellValue(primerProduct.getModiMidType()+","+primerProduct.getModiSpeType());
						break;
					  case 10:
						  cell.setCellValue(primerProduct.getRemark());
						break;
					  default:
						break;
					}
	        	}
	        	
	        	cellIndex++;
	        }
	        //创建表格列表结束
	        fileNames.add(order.getOrderNo()+".xlsx");
			exportExcel.outputExcel();
			
			 count++;
			 if(count==1) {
				 starName = order.getOrderNo();
		     }
			 if(count==orderInfoLists.size()) {
				 endName = "~"+order.getOrderNo();
		     }
		}
		
		
		zipFileName = starName+endName+".zip";
		
		exportExcel.outputExcelZip(fileNames,outFilePath,zipFileName);
		
        File file = new File(outFilePath, zipFileName); 
		
		return Replys.with(file).as(Raw.class).downloadFileName(zipFileName);
	}
	
	public OrderInfo getOutbound(Order order){
		OrderInfo orderInfo = new OrderInfo();
		Customer customer = customerRepository.findByCode(order.getCustomerCode());
		//组织订单列表对象
		orderInfo.setOrderNo(order.getOrderNo());
		orderInfo.setCustomerName(order.getCustomerName());
		orderInfo.setCustomerPhoneNm(customer.getPhoneNo());
		//业务员 调整值获取
		orderInfo.setHandlerCode("98834342");
		//收样日期
		orderInfo.setCreateTime(order.getCreateTime());
		//单据编号 调整值获取 自动生成10位流水号
		orderInfo.setMakingNo("0000000010");
		//制单人（当前系统操作人员） 调整值获取 根据登陆信息获取
		orderInfo.setOperatorCode("34343434");
		//联系人（客户管理中的负责人） 调整值获取
		orderInfo.setLinkName(customer.getLeaderName());
		orderInfo.setUnit(customer.getName());
		//制单日期
		orderInfo.setMakingDate(new Date());
		//商品编码 默认赋值
		//orderInfo.setCommodityCode("06030008");
		//货物名称 默认赋值
		//orderInfo.setCommodityName("DNA合成");
		
		return orderInfo;
	}
	
	
    /**
     * 得到发货清单的生产数据
     * */
	public List<PrimerProduct> getDeliveryPrimerProducts(List<OrderInfo> orderInfos, Invocation inv) {
		
		List<PrimerProduct> primerProducts = new ArrayList<PrimerProduct>();
		PrimerProduct primerProduct = null;
		for (OrderInfo orderInfo : orderInfos) {
			String productNo = orderInfo.getProductNoMinToMax();
			primerProduct = primerProductRepository.findByProductNoOrOutProductNo(productNo, productNo);
			if (primerProduct != null) {
				primerProducts.add(primerProduct);
			}

		}
		return primerProducts;
	}
	
	public static void main(String[] args){
		
		String str = "10";
		
		 Pattern pattern = Pattern.compile("[0-9]*");
		 
		 boolean bb = pattern.matcher(str).matches();
		 
		 if(bb){
			 System.out.println("shi");
		 }else{
			 System.out.println("fou");
		 }
		
	}
	
	
}
