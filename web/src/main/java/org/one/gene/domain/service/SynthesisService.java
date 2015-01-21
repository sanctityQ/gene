package org.one.gene.domain.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Raw;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.PackTableHoleConfig;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerType;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.PrimerType.PrimerOperationType;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.instruction.reply.EntityReply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;

//Spring Bean的标识.
@Component
//默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class SynthesisService {

    private static Logger logger = LoggerFactory.getLogger(SynthesisService.class);

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


//    @Transactional(readOnly = false)
    //安排合成查询
	public Page<PrimerProduct> makeBoardQuery(String customercode,
			String modiFlag, String tbn1, String tbn2, String purifytype,
			Pageable pageable) {

		Page<PrimerProduct> primerProductPage = primerProductRepository.selectPrimerProduct(customercode, modiFlag, tbn1, tbn2, purifytype, pageable);
		
		// 查询primer_product_value
		for (PrimerProduct primerProduct : primerProductPage.getContent()) {
			List<PrimerProductValue> primerProductValues = primerProductValueRepository.selectValueByPrimerProductId(primerProduct.getId());
			for (PrimerProductValue ppv : primerProductValues) {
				if (ppv.getType().equals(PrimerValueType.odTotal)) {
					primerProduct.setOdTotal(ppv.getValue());
				} else if (ppv.getType().equals(PrimerValueType.odTB)) {
					primerProduct.setOdTB(ppv.getValue());
				} else if (ppv.getType().equals(PrimerValueType.nmolTotal)) {
					primerProduct.setNmolTotal(ppv.getValue());
				} else if (ppv.getType().equals(PrimerValueType.nmolTB)) {
					primerProduct.setNmolTB(ppv.getValue());
				} else if (ppv.getType().equals(PrimerValueType.baseCount)) {
					primerProduct.setTbn(ppv.getValue());
				}
			}
			// 翻译操作类型
			PrimerStatusType operationType = primerProduct.getOperationType();
            primerProduct.setOperationTypeDesc(operationType.desc());
            
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
				primerProduct.setMidi(midi);
			}
			
		}

		return primerProductPage;
	}

    //到制板页面
	public String makeBoard(String boardNo, String flag, String oldFlag,
			String productNoStr, Invocation inv) throws IOException {
		
		System.out.println("=====页面选择的生产编号=" + productNoStr);
		int totalCount = 0;
		Map<String, String> productNoMap = new HashMap<String, String>();
		String[] productNoStrArray = productNoStr.split(",");
		for (int i = 0; i < productNoStrArray.length; i++) {
			productNoMap.put(productNoStrArray[i], productNoStrArray[i]);
			totalCount += 1;
		}
		
		//查询板号信息
		Map<String, String> boardHoleMap = new HashMap<String, String>();
		Board board = boardRepository.findByBoardNo(boardNo);
		if (board != null) {
			for (BoardHole boardHole : board.getBoardHoles()) {
				boardHoleMap.put(boardHole.getHoleNo(), boardHole.getPrimerProduct().getProductNo());
				totalCount += 1;
			}
			if("1".equals(oldFlag)){//模糊查询的板号才使用已有的板类型
				flag = board.getBoardType();
			}
		}
		
		if(totalCount>96){
			totalCount = 96;
		}
		ArrayList holeNoList = new ArrayList();
		ArrayList holeList = new ArrayList();
		
		holeList.add("A");
		holeList.add("B");
		holeList.add("C");
		holeList.add("D");
		holeList.add("E");
		holeList.add("F");
		holeList.add("G");
		holeList.add("H");
		
		String holeNo = "";
		String productNo = "";
		String jsonStr = "{\r";
		jsonStr += "\"boardType\":"+flag+",\r";
		jsonStr += "\"total\":"+totalCount+",\r";
		jsonStr += "\"rows\":[\r";
		
		//竖板
		if ("1".equals(flag)) {
			
			for (int j=1;j<13;j++){
				
				jsonStr += "{\"row"+j+"\":[\r";
				
				for(int i=0 ;i<holeList.size();i++){
					String hole = (String)holeList.get(i);
					holeNo = hole+j;
					productNo = "";
					if(boardHoleMap.get(holeNo) != null){
						productNo = (String)boardHoleMap.get(holeNo);
					}else{
						for (String v : productNoMap.values()) {
							productNo = v;
							break;
						}
						if(!"".equals(productNo)){
							productNoMap.remove(productNo);
						}
					}
					
					jsonStr += "{\"tag\":\""+holeNo+"\",\"No\":\""+productNo+"\"}";
					
					if (i!=holeList.size()-1){
						jsonStr += ",\r";
					}
				}
				
				if(j==12){
					jsonStr += "]}\r";
				}else{
					jsonStr += "]},\r";
				}
			}
			
		} else {
			for(int i=0 ;i<holeList.size();i++){
				
				jsonStr += "{\"row"+(i+1)+"\":[\r";
				
				String hole = (String)holeList.get(i);
				for (int j=1;j<13;j++){
					holeNo = hole+j;
					productNo = "";
					if(boardHoleMap.get(holeNo) != null){
						productNo = (String)boardHoleMap.get(holeNo);
					}else{
						for (String v : productNoMap.values()) {
							productNo = v;
							break;
						}
						if(!"".equals(productNo)){
							productNoMap.remove(productNo);
						}
					}
					jsonStr += "{\"tag\":\""+holeNo+"\",\"No\":\""+productNo+"\"}";
					
					if (j!=12){
						jsonStr += ",\r";
					}
				}
				if(i == holeList.size()-1){
					jsonStr += "]}\r";
				}else{
					jsonStr += "]},\r";
				}
			}
		}

		jsonStr += "]\r}";

		
		
		System.out.println("==========拼接的jsonstr=\r"+jsonStr);
		
		return jsonStr;
	}
	
	//提交制表信息
    @Transactional(readOnly = false)
	public String submitBoard(String holeStr, String boardNo, String boardType, Invocation inv) {
    	
    	Board board = boardRepository.findByBoardNo(boardNo);
    	List<PrimerProductOperation> primerProductOperationList = null;
    	if( board == null ){
    		board = new Board();
    	}
    	board.setBoardNo(boardNo);
    	board.setBoardType(boardType);
		board.setCreateUser(123);//后续需要调整到从session取值
		board.setCreateTime(new Date());
    	
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		
		//拆分孔号，生产编号字符串
		List<BoardHole> boardHoles = new ArrayList<BoardHole>();
		BoardHole boardHole = new BoardHole();
		PrimerProduct primerProduct = new PrimerProduct();
		String[] holeStrArray = holeStr.split(";");
		for (int i = 0; i < holeStrArray.length; i++) {
			String holeNoStr = holeStrArray[i];
			String[] holeNoArray = holeNoStr.split(":");
			System.out.println("===============生产编号="+holeNoArray[1]);
			
			primerProduct = primerProductRepository.findByProductNo(holeNoArray[1]);
			
			addNewValue(primerProduct);
	    	
			//更新primer_product的操作类型和板号
			if( primerProduct != null ){
				primerProduct.setBoardNo(board.getBoardNo());
				primerProduct.setOperationType(PrimerStatusType.synthesis);//待合成
				primerProductRepository.save(primerProduct);
			}
			
			boardHole = new BoardHole();
			for(BoardHole boardHoleTemp:board.getBoardHoles()){
				if(boardHoleTemp.getHoleNo().equals(holeNoArray[0])){
					boardHole = boardHoleTemp;
				}
			}
			boardHole.setBoard(board);
			boardHole.setHoleNo(holeNoArray[0]);
			boardHole.setPrimerProduct(primerProduct);
			boardHole.setCreateUser(123L);//后续需要调整到从session取值
			boardHole.setCreateTime(new Date());
			
			boardHoles.add(boardHole);
			
			
			//组织操作记录表信息
			
			primerProductOperation = new PrimerProductOperation();
			primerProductOperation.setPrimerProduct(boardHole.getPrimerProduct());
			primerProductOperation.setType(PrimerOperationType.makeBoard);
			primerProductOperation.setTypeDesc(PrimerOperationType.makeBoard.desc());
			primerProductOperation.setBackTimes(0);
			
			primerProductOperationList = primerProduct.getPrimerProductOperations();
			for (PrimerProductOperation ppo : primerProductOperationList) {
				if (ppo.getPrimerProduct().getId() == primerProduct.getId()
						&& ppo.getType().equals(primerProductOperation.getType())) {
					primerProductOperation = ppo;
				}
			}
			primerProductOperation.setUserCode("123");//后续从session取得
			primerProductOperation.setUserName("张三");//后续从session取得
			primerProductOperation.setCreateTime(new Date());
			primerProductOperation.setFailReason("");
			primerProductOperations.add(primerProductOperation);
		}
		
		board.setBoardHoles(boardHoles);
		
		//保存操作信息
		primerProductOperationRepository.save(primerProductOperations);
		
		//保存板孔信息
		boardRepository.save(board);
		
		return "";
	}
    

	//模糊查询板信息 
	public List<Board> synthesisQuery(String boardNo) {
		if (!StringUtils.isBlank(boardNo)) {
			boardNo = "%" + boardNo + "%";
        }
    	
        return boardRepository.selectOrderByNo(boardNo);
    }
	
	//提交合成信息
    @Transactional(readOnly = false)
	public String submitSynthesis(String boardNo, List<BoardHole> boardHoleList, String failReason) {
    	
    	Map<String, String> bhMap = new HashMap<String, String>();
    	for(BoardHole bh:boardHoleList){
    		System.out.println("=========页面的选择合成结果="+bh.getHoleNo()+"="+ bh.getFailFlag());
    		bhMap.put(bh.getHoleNo(), bh.getFailFlag());
    	}
    	
    	Board board = boardRepository.findByBoardNo(boardNo);
		BoardHole boardHole = null;
		List<BoardHole> boardHoles = board.getBoardHoles();
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (int i = board.getBoardHoles().size() - 1; i >= 0; i--) {
			boardHole = (BoardHole) board.getBoardHoles().get(i);
			primerProduct = boardHole.getPrimerProduct();
			if(bhMap.get(boardHole.getHoleNo())!=null){
				
				String failFlag = (String)bhMap.get(boardHole.getHoleNo());
				type = null;
				
				if ("0".equals(failFlag)) { // Synthesis success
					
					if (!"".equals(primerProduct.getModiFiveType())
							|| !"".equals(primerProduct.getModiThreeType())
							|| !"".equals(primerProduct.getModiMidType())
							|| !"".equals(primerProduct.getModiSpeType())) { // 需要修饰，到待修饰状态
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.modification);
					} else {//不需要修饰，到待氨解状态
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.ammonia);
					}
					
					type = PrimerOperationType.synthesisSuccess;
					typeDesc = PrimerOperationType.synthesisSuccess.desc();
					
				} else if ("1".equals(failFlag)) { // Synthesis fail
					
					boardHole.getPrimerProduct().setOperationType(PrimerStatusType.synthesis);//回到待合成
					boardHole.getPrimerProduct().setBoardNo("");//清空板号
					if (boardHole.getPrimerProduct().getBackTimes() != null) {
						boardHole.getPrimerProduct().setBackTimes(boardHole.getPrimerProduct().getBackTimes()+1);//循环重回次数+1
					}else{
						boardHole.getPrimerProduct().setBackTimes(1);
					}
					
					//删除孔信息
					boardHoleRepository.delete(boardHole);
					boardHoles.remove(i);
					
					type = PrimerOperationType.synthesisFailure;
					typeDesc = PrimerOperationType.synthesisFailure.desc();
					
				}
				
				//组装操作信息
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(boardHole.getPrimerProduct());
				primerProductOperation.setUserCode("123");//后续从session取得
				primerProductOperation.setUserName("张三");//后续从session取得
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setType(type);
				primerProductOperation.setTypeDesc(typeDesc);
				primerProductOperation.setBackTimes(boardHole.getPrimerProduct().getBackTimes());
				primerProductOperation.setFailReason("");//需要记录失败原因！！！
				
				primerProductOperations.add(primerProductOperation);
				
				
				//保存primer_product表数据
				primerProductRepository.save(boardHole.getPrimerProduct());
			}
			//更新操作信息
			primerProductOperationRepository.save(primerProductOperations);
			
    	}
    	
		boardRepository.save(board);
    	
    	return "";
    }
    
    /**
     * 导出上机表文件
     * @throws IOException 
     * */
	public void exportMachineTable(String boardNo, Invocation inv) throws IOException {
		
		String tableContext = "";//
		List<PrimerProduct> PrimerProducts = primerProductRepository.findByBoardNo(boardNo);
		int i = 0;
		for(PrimerProduct primerProduct:PrimerProducts){
			if ( i !=0 ){//行尾加回车
				tableContext += "\r\n";
			}
			tableContext += primerProduct.getProductNo()+", ";
			tableContext += primerProduct.getGeneOrder();
			i++;
		}
    	
		//输出文件到客户端
		String fileName = boardNo + "_MachineTable.SEQ";
        HttpServletResponse response = inv.getResponse();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/text");
        response.getWriter().write(tableContext); 
        response.flushBuffer();
		
    }

    /**
     * 导出分装表文件
     * @throws IOException 
     * */
	public void exportPackTable(String boardNo, Invocation inv) throws IOException {
		
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"";
		String excelFilePath = templatePath+"packTable.xls";
		String configFilePath = templatePath+"packTableHoleConfig.txt";
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String currentTime = df.format(new Date());
        
		//读取分装表孔配置信息
		String encoding="GBK";
        File fileTxt = new File(configFilePath);
        InputStreamReader read = new InputStreamReader(new FileInputStream(fileTxt),encoding);//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        
        Map<String, PackTableHoleConfig> pthcMap = new HashMap<String, PackTableHoleConfig>();
        
        PackTableHoleConfig pthc = null;
        String lineTxt = null;
        while((lineTxt = bufferedReader.readLine()) != null){
        	String[] configArray = lineTxt.split(",");
        	pthc = new PackTableHoleConfig();
        	pthc.setHoleNo(configArray[0]);
        	pthc.setRow(Integer.parseInt(configArray[1]));
        	pthc.setColumn(Integer.parseInt(configArray[2]));
        	pthcMap.put(configArray[0], pthc);
        	
        }
        read.close();

		Board board = boardRepository.findByBoardNo(boardNo);
        if (board != null){
        	for(BoardHole boardHole:board.getBoardHoles()){
        		if (pthcMap.get(boardHole.getHoleNo()) != null) {
        			pthc = pthcMap.get(boardHole.getHoleNo());
        			pthc.setPrimerProduct(boardHole.getPrimerProduct());
        			pthcMap.put(boardHole.getHoleNo(), pthc);
        		}
        	}
        }
		
		// 读取分装表模板 形成Excel
		String strFileName = "packTable"+System.currentTimeMillis()+".xls";
		
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFilePath));
		HSSFSheet sheet = workbook.getSheetAt(0);
		FileOutputStream fos = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		BigDecimal totalBN= new BigDecimal(0);//总碱基数
		PrimerProduct pp = null;
		PrimerProduct ppLast = null;
		for (Map.Entry<String, PackTableHoleConfig> map : pthcMap.entrySet()) {
			pthc = map.getValue();
			
			pp = pthc.getPrimerProduct();
			if( pp != null ){
				
				addNewValue(pp);
				
				totalBN = totalBN.add(pp.getTbn());
				
				row = sheet.getRow(pthc.getRow());
				cell = row.getCell(pthc.getColumn()+1);// 生产编号
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if(!"".equals(pp.getProductNo())){
					cell.setCellValue(pp.getProductNo());
				}else{
					cell.setCellValue(pp.getOutProductNo());
				}
				cell = row.getCell(pthc.getColumn()+2);// 分装
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(pp.getOdTB()+"OD*"+pp.getTb());
				cell = row.getCell(pthc.getColumn()+3);// 体积
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(pp.getOdTotal()+"");
				
				ppLast = pp;
			}
		}

		
		//板号
		row = sheet.getRow(0);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(boardNo);
		row = sheet.getRow(27);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(boardNo);
		
		String remark = "备注：  合成柱:    "+currentTime+"       总碱基="+totalBN+"    洗脱体积=       μl";
		row = sheet.getRow(15);
		cell = row.getCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(remark);
		row = sheet.getRow(44);
		cell = row.getCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(remark);
		
		
		//制表
		row = sheet.getRow(17);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("操作员：");
		cell = row.getCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("时间："+currentTime);
        if (ppLast != null){
        	
        	for (PrimerProductOperation ppo : ppLast.getPrimerProductOperations()) {
				if (ppo.getType().equals(PrimerOperationType.synthesisSuccess)) {
					//合成
					cell = row.getCell(2);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(3);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.ammoniaSuccess)) {
					//氨解
					cell = row.getCell(4);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(5);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.purifySuccess)) {
					//纯化
					cell = row.getCell(6);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(7);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.measureSuccess)) {
					//测值
					cell = row.getCell(8);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(9);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.packSuccess)) {
					//分装
					cell = row.getCell(10);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(11);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.deliverySuccess)) {
					//发货
					cell = row.getCell(12);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(13);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}
        		
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

	
	//补充临时字段的值
	public void addNewValue(PrimerProduct primerProduct) {
    	for (PrimerProductValue pv : primerProduct.getPrimerProductValues()) {
    		if(pv.getType().equals(PrimerValueType.odTotal)){
    			primerProduct.setOdTotal(pv.getValue());
    		}else if(pv.getType().equals(PrimerValueType.odTB)){
    			primerProduct.setOdTB(pv.getValue());
    		}else if(pv.getType().equals(PrimerValueType.nmolTotal)){
    			primerProduct.setNmolTotal(pv.getValue());
    		}else if(pv.getType().equals(PrimerValueType.nmolTB)){
    			primerProduct.setNmolTB(pv.getValue());
    		}else if(pv.getType().equals(PrimerValueType.baseCount)){
    			primerProduct.setTbn(pv.getValue());
    		}else if(pv.getType().equals(PrimerValueType.tb)){
    			primerProduct.setTb(pv.getValue());
    		}
    	}
		
	}
	
	
	
	    //修饰查询
	public Page<PrimerProduct> decorateQuery(String boardNo,
			String modiFiveType, String modiThreeType, String modiMidType,
			String modiSpeType, Pageable pageable) {

			Page<PrimerProduct> primerProductPage = primerProductRepository.selectDecorateProducts(boardNo, modiFiveType, modiThreeType, modiMidType, modiSpeType, pageable);
			
			// 查询primer_product_value
			for (PrimerProduct primerProduct : primerProductPage.getContent()) {
				List<PrimerProductValue> primerProductValues = primerProductValueRepository.selectValueByPrimerProductId(primerProduct.getId());
				for (PrimerProductValue ppv : primerProductValues) {
					if (ppv.getType().equals(PrimerValueType.odTotal)) {
						primerProduct.setOdTotal(ppv.getValue());
					} else if (ppv.getType().equals(PrimerValueType.odTB)) {
						primerProduct.setOdTB(ppv.getValue());
					} else if (ppv.getType().equals(PrimerValueType.nmolTotal)) {
						primerProduct.setNmolTotal(ppv.getValue());
					} else if (ppv.getType().equals(PrimerValueType.nmolTB)) {
						primerProduct.setNmolTB(ppv.getValue());
					} else if (ppv.getType().equals(PrimerValueType.baseCount)) {
						primerProduct.setTbn(ppv.getValue());
					}
				}
				// 翻译操作类型
				PrimerStatusType operationType = primerProduct.getOperationType();
	            primerProduct.setOperationTypeDesc(operationType.desc());
	            
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
					primerProduct.setMidi(midi);
				}
				
			}

			return primerProductPage;
		}
		
		
	//提交修饰信息
    @Transactional(readOnly = false)
	public String submitDecorate(List<PrimerProduct> primerProducts,
			String successFlag, String failReason) {
    	
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (PrimerProduct pp:primerProducts) {
			
			PrimerProduct primerProduct = primerProductRepository.findOne(pp.getId());
			type = null;
			
			if ("1".equals(successFlag)) { // decorate success
				
				primerProduct.setOperationType(PrimerStatusType.purify);
				type = PrimerOperationType.modiSuccess;
				typeDesc = PrimerOperationType.modiSuccess.desc();
				
			} else if ("0".equals(successFlag)) { // decorate fail
				
				primerProduct.setOperationType(PrimerStatusType.synthesis);//回到待合成
				primerProduct.setBoardNo("");//清空板号
				if (primerProduct.getBackTimes() != null) {
					primerProduct.setBackTimes(primerProduct.getBackTimes()+1);//循环重回次数+1
				}else{
					primerProduct.setBackTimes(1);
				}
				
				//删除孔信息
				BoardHole boardHole = boardHoleRepository.findByPrimerProduct(primerProduct);
				if (boardHole != null) {
					boardHoleRepository.delete(boardHole);
				}
				
				type = PrimerOperationType.modiFailure;
				typeDesc = PrimerOperationType.modiFailure.desc();
			}
			
			//组装操作信息
			primerProductOperation = new PrimerProductOperation();
			primerProductOperation.setPrimerProduct(primerProduct);
			primerProductOperation.setUserCode("123");//后续从session取得
			primerProductOperation.setUserName("张三");//后续从session取得
			primerProductOperation.setCreateTime(new Date());
			primerProductOperation.setType(type);
			primerProductOperation.setTypeDesc(typeDesc);
			primerProductOperation.setBackTimes(primerProduct.getBackTimes());
			primerProductOperation.setFailReason(failReason);
			
			System.out.println("================"+primerProductOperation.getPrimerProduct().getId()+"=="+primerProductOperation.getType()+"==="+primerProductOperation.getBackTimes());
			primerProductOperationRepository.save(primerProductOperation);
//			primerProduct.getPrimerProductOperations().add(primerProductOperation);
			//保存primer_product表数据
			primerProductRepository.save(primerProduct);
    	}
    	
    	return "";
    }
		
		
    //组织板页面信息
	public String boardEdit(String boardNo, PrimerStatusType operationType, Invocation inv) throws IOException {
		
		int totalCount = 0;
		String flag = "";
		String typeFlag = "1";//生产数据是否复合查询类型 ：1 复合， 0 不复合
		//查询板号信息
		Map<String, PrimerProduct> boardHoleMap = new HashMap<String, PrimerProduct>();
		Board board = boardRepository.findByBoardNo(boardNo);
		if (board != null) {
			flag = board.getBoardType();
			for (BoardHole boardHole : board.getBoardHoles()) {
				boardHoleMap.put(boardHole.getHoleNo(), boardHole.getPrimerProduct());
				totalCount += 1;
				if (!boardHole.getPrimerProduct().getOperationType().equals(operationType)) {
					typeFlag = "0";
				}
			}
		}

		ArrayList holeNoList = new ArrayList();
		ArrayList holeList = new ArrayList();
		PrimerProduct primerProduct = null;
		holeList.add("A");
		holeList.add("B");
		holeList.add("C");
		holeList.add("D");
		holeList.add("E");
		holeList.add("F");
		holeList.add("G");
		holeList.add("H");
		
		String holeNo = "";
		String productNo = "";
		String purifyType = "";
		String jsonStr = "{\r";
		jsonStr += "\"typeFlag\":"+typeFlag+",\r";
		jsonStr += "\"typeDesc\":\""+operationType.desc()+"\",\r";
		jsonStr += "\"total\":"+totalCount+",\r";
		jsonStr += "\"rows\":[\r";
		
		//竖板
		if ("1".equals(flag)) {
			
			for (int j=1;j<13;j++){
				
				jsonStr += "{\"row"+j+"\":[\r";
				
				for(int i=0 ;i<holeList.size();i++){
					String hole = (String)holeList.get(i);
					holeNo = hole+j;
					productNo = "";
					purifyType = "";
					if(boardHoleMap.get(holeNo) != null){
						primerProduct = (PrimerProduct)boardHoleMap.get(holeNo);
						productNo = primerProduct.getProductNo();
						if (!"OPC".equals(primerProduct.getPurifyType())
								&& operationType.equals(PrimerStatusType.purify)) {
							purifyType = primerProduct.getPurifyType();
						}
					}
					
					jsonStr += "{\"tag\":\""+holeNo+"\",\"No\":\""+productNo+"\",\"identifying\":\""+purifyType+"\"}";
					
					if (i!=holeList.size()-1){
						jsonStr += ",\r";
					}
				}
				
				if(j==12){
					jsonStr += "]}\r";
				}else{
					jsonStr += "]},\r";
				}
			}
			
		} else {
			for(int i=0 ;i<holeList.size();i++){
				
				jsonStr += "{\"row"+(i+1)+"\":[\r";
				
				String hole = (String)holeList.get(i);
				for (int j=1;j<13;j++){
					holeNo = hole+j;
					productNo = "";
					purifyType = "";
					if(boardHoleMap.get(holeNo) != null){
						primerProduct = (PrimerProduct)boardHoleMap.get(holeNo);
						productNo = primerProduct.getProductNo();
						if (!"OPC".equals(primerProduct.getPurifyType())
								&& operationType.equals(PrimerStatusType.purify)) {
							purifyType = primerProduct.getPurifyType();
						}
					}
					jsonStr += "{\"tag\":\""+holeNo+"\",\"No\":\""+productNo+"\",\"identifying\":\""+purifyType+"\"}";
					
					if (j!=12){
						jsonStr += ",\r";
					}
				}
				if(i == holeList.size()-1){
					jsonStr += "]}\r";
				}else{
					jsonStr += "]},\r";
				}
			}
		}

		jsonStr += "]\r}";
		
		System.out.println("==========拼接的jsonstr=\r"+jsonStr);
		
		return jsonStr;
	}	
		
	//提交板編輯信息
    @Transactional(readOnly = false)
	public String submitBoardEdit(String boardNo,
			List<BoardHole> boardHoleList, PrimerStatusType operationType,
			String failReason) {
    	
    	Map<String, String> bhMap = new HashMap<String, String>();
    	for(BoardHole bh:boardHoleList){
    		bhMap.put(bh.getHoleNo(), bh.getFailFlag());
    	}
    	
    	Board board = boardRepository.findByBoardNo(boardNo);
		BoardHole boardHole = null;
		List<BoardHole> boardHoles = board.getBoardHoles();
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (int i = board.getBoardHoles().size() - 1; i >= 0; i--) {
			boardHole = (BoardHole) board.getBoardHoles().get(i);
			if(bhMap.get(boardHole.getHoleNo())!=null){
				
				String failFlag = (String)bhMap.get(boardHole.getHoleNo());
				type = null;
				
				if ("0".equals(failFlag)) { // success
					
					if (operationType.equals(PrimerStatusType.ammonia)) {
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.purify);
						type = PrimerOperationType.ammoniaSuccess;
						typeDesc = PrimerOperationType.ammoniaSuccess.desc();
					}else if (operationType.equals(PrimerStatusType.purify)) {
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.measure);
						type = PrimerOperationType.purifySuccess;
						typeDesc = PrimerOperationType.purifySuccess.desc();
					}else if (operationType.equals(PrimerStatusType.pack)) {
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.bake);
						type = PrimerOperationType.packSuccess;
						typeDesc = PrimerOperationType.packSuccess.desc();
					}
                    	
					
				} else if ("1".equals(failFlag)) { // fail
					
					boardHole.getPrimerProduct().setOperationType(PrimerStatusType.synthesis);//回到待合成
					boardHole.getPrimerProduct().setBoardNo("");//清空板号
					if (boardHole.getPrimerProduct().getBackTimes() != null) {
						boardHole.getPrimerProduct().setBackTimes(boardHole.getPrimerProduct().getBackTimes()+1);//循环重回次数+1
					}else{
						boardHole.getPrimerProduct().setBackTimes(1);
					}
					
					//删除孔信息
					boardHoleRepository.delete(boardHole);
					boardHoles.remove(i);
					
					if (operationType.equals(PrimerStatusType.ammonia)) {
						type = PrimerOperationType.ammoniaFailure;
						typeDesc = PrimerOperationType.ammoniaFailure.desc();
					}else if (operationType.equals(PrimerStatusType.purify)) {
						type = PrimerOperationType.purifyFailure;
						typeDesc = PrimerOperationType.purifyFailure.desc();
					}else if (operationType.equals(PrimerStatusType.pack)) {
						type = PrimerOperationType.packFailure;
						typeDesc = PrimerOperationType.packFailure.desc();
					}
					
				}
				
				//组装操作信息
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(boardHole.getPrimerProduct());
				primerProductOperation.setUserCode("123");//后续从session取得
				primerProductOperation.setUserName("张三");//后续从session取得
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setType(type);
				primerProductOperation.setTypeDesc(typeDesc);
				primerProductOperation.setBackTimes(boardHole.getPrimerProduct().getBackTimes());
				primerProductOperation.setFailReason("");//需要记录失败原因！！！
				
				primerProductOperations.add(primerProductOperation);
				
				
				//保存primer_product表数据
				primerProductRepository.save(boardHole.getPrimerProduct());
			}
			//更新操作信息
			primerProductOperationRepository.save(primerProductOperations);
			
    	}
    	
		boardRepository.save(board);
    	
    	return "";
    }	
		
		
		
		
		
		
		
		
}
