package org.one.gene.domain.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.Order.OrderType;
import org.one.gene.domain.entity.PackTableHoleConfig;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.PrimerType.PrimerOperationType;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.web.statistics.StatisticsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.one.mvc.web.Invocation;

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
	public Page<PrimerProduct> makeBoardQuery(String customerCode,
			String modiFlag, String tbn1, String tbn2, String purifytype,
			String comCode, String productNoPrefix, String odTotal1,
			String odTotal2, String liquidFlag, Pageable pageable) {

		String[] purifytypes = purifytype.split(",");
		String ptFlag = "";
		
		if (purifytype == null || "".equals(purifytype) || purifytype.length()==1) {
			ptFlag = "0";
		} else {
			if (purifytype.startsWith(",")) {
				purifytype = purifytype.substring(1, purifytype.length());
			}
			purifytypes = purifytype.split(",");
			ptFlag = "1";
		}
		
		Page<PrimerProduct> primerProductPage = primerProductRepository
				.selectPrimerProduct(customerCode, modiFlag, tbn1, tbn2,
						purifytypes, ptFlag, comCode, productNoPrefix, odTotal1, odTotal2, liquidFlag, pageable);
		
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
	public String makeBoard(String boardNo, String flag, String productNoStr,
			PrimerStatusType operationType, String orderFlag, Invocation inv) throws IOException {
		
		System.out.println("=====页面选择的生产编号=" + productNoStr);
		int totalCount = 0;
		String[] productNoStrArray = productNoStr.split(",");
		List<String> selectProductNoList = new ArrayList<String>();
		for (int i = 0; i < productNoStrArray.length; i++) {
			selectProductNoList.add(productNoStrArray[i]);
			totalCount += 1;
		}
		
		//查询板号信息
		OrderType orderUpType = null;
		Map<String, String> boardHoleMap = new HashMap<String, String>();//数据库中的板孔信息
		Board board = boardRepository.findByBoardNo(boardNo);
		if (board != null) {
			for (BoardHole boardHole : board.getBoardHoles()) {
				if (boardHole.getStatus() == 0 && operationType == boardHole.getPrimerProduct().getOperationType()) {//0正常  1 删除 && 类型相同
					boardHoleMap.put(boardHole.getHoleNo(), boardHole.getPrimerProduct().getProductNo());
					totalCount += 1;
					orderUpType = boardHole.getPrimerProduct().getOrder().getOrderUpType();
				}
			}
			//默认是使用原板号的排序，手动页面选择排序时不使用原有方式
			if ("0".equals(orderFlag)) {
				flag = board.getBoardType();
			}
		}
		
		if(totalCount>96){
			totalCount = 96;
		}
		
		//通过竖排或者横排来组织排列的数据，然后放入固定的排版中
		List<BoardHole> holeNoList = getHoleOrderList(flag);
		String tempHoleNo = "";
		String tempProductNo = "";
		String lastProductNo = "";
		BoardHole boardHole = null;
		Map<String, String> lastHoleMap = new HashMap<String, String>();
		
		for(int i=0 ;i<holeNoList.size();i++){
			boardHole = (BoardHole)holeNoList.get(i);
			tempHoleNo = boardHole.getHoleNo();
			tempProductNo = "";
			
			if (boardHoleMap.get(tempHoleNo) != null) {
				tempProductNo = (String)boardHoleMap.get(tempHoleNo);
				lastProductNo = tempProductNo;
			} else {
				for (String v : selectProductNoList) {
					tempProductNo = v;
					lastProductNo = tempProductNo;
					break;
				}
				if (!"".equals(tempProductNo)) {
					selectProductNoList.remove(tempProductNo);
				}
			}
			
			lastHoleMap.put(tempHoleNo, tempProductNo);
		}
		if (orderUpType == null) {
			PrimerProduct primerProduct = primerProductRepository.findByProductNoOrOutProductNo(lastProductNo, lastProductNo);
			if (primerProduct != null) {
				 orderUpType = primerProduct.getOrder().getOrderUpType();
			}

		}
		
		String totalLabel = "";
		if (orderUpType == OrderType.nmol) {
			totalLabel = "NMOL / TUBE * "+totalCount;
		}else{
			totalLabel = "OD / TUBE * "+totalCount;
		}
		
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
		jsonStr += "\"total\":\""+totalLabel+"\",\r";
		jsonStr += "\"rows\":[\r";
		
		for(int i=0 ;i<holeList.size();i++){
			
			jsonStr += "{\"row"+(i+1)+"\":[\r";
			
			String hole = (String)holeList.get(i);
			for (int j=1;j<13;j++){
				holeNo = hole+j;
				productNo = "";
				
				if(lastHoleMap.get(holeNo) != null){
					productNo = (String)lastHoleMap.get(holeNo);
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


		jsonStr += "]\r}";

		
		
		System.out.println("==========拼接的jsonstr=\r"+jsonStr);
		
		return jsonStr;
	}
	
	public List<BoardHole> getHoleOrderList(String boardType) {
		
		List<BoardHole> holeNoList = new ArrayList<BoardHole>();
		BoardHole boardHole = null;
		ArrayList holeList = new ArrayList();
		String holeNo = "";
		
		holeList.add("A");
		holeList.add("B");
		holeList.add("C");
		holeList.add("D");
		holeList.add("E");
		holeList.add("F");
		holeList.add("G");
		holeList.add("H");
		
		int index = 0;
		//竖板
		if ("1".equals(boardType)) {
			
			for (int j=1;j<13;j++){
				for(int i=0 ;i<holeList.size();i++){
					String hole = (String)holeList.get(i);
					holeNo = hole+j;
					
					index++;
					boardHole = new BoardHole();
					boardHole.setHoleNo(holeNo);
					boardHole.setSorting(index);
					
					holeNoList.add(boardHole);
				}
			}
			
		} else {
			for(int i=0 ;i<holeList.size();i++){
				String hole = (String)holeList.get(i);
				for (int j=1;j<13;j++){
					holeNo = hole+j;
					
					index++;
					boardHole = new BoardHole();
					boardHole.setHoleNo(holeNo);
					boardHole.setSorting(index);
					
					holeNoList.add(boardHole);
				}
			}
		}
		
       return holeNoList;
	}
	
	
	
	//提交制表信息
    @Transactional(readOnly = false)
	public String submitBoard(String holeStr, String boardNo, String boardType,  ShiroUser user, Invocation inv) throws Exception {
    	
    	Board board = boardRepository.findByBoardNo(boardNo);
    	List<PrimerProductOperation> primerProductOperationList = null;
    	if( board == null ){
    		board = new Board();
    	}else{
			if (board.getOperationType() != PrimerStatusType.synthesis) {
				throw new Exception("您提交的板号在系统中已存在，但不是待合成状态。不允许提交！");
			}
			boolean haveFlag = false;
			for (BoardHole boardHoleTemp : board.getBoardHoles()) {
				if (holeStr.indexOf(boardHoleTemp.getPrimerProduct().getProductNo()) > 0) {
				} else {
					haveFlag = true;
				}
			}
			if (haveFlag) {
				throw new Exception("您提交的板号在系统中已存在，但已有板号的生产编号不在提交信息中。不允许提交！");
			}
    	}
    	board.setBoardNo(boardNo);
    	board.setBoardType(boardType);
		board.setCreateUser(Integer.parseInt(user.getUser().getId()+""));
		board.setCreateTime(new Date());
		board.setOperationType(PrimerStatusType.synthesis);//待合成
    	
		//取得孔的排序
		List<BoardHole> holeNoList = getHoleOrderList(boardType);
		String sortHoleNo = "";
		int    sorting = 0;
		BoardHole boardHoleSort = null;
		Map<String, Integer> lastHoleMap = new HashMap<String, Integer>();
		for(int i=0 ;i<holeNoList.size();i++){
			boardHoleSort = (BoardHole)holeNoList.get(i);
			sortHoleNo = boardHoleSort.getHoleNo();
			sorting    = boardHoleSort.getSorting();
			lastHoleMap.put(sortHoleNo, sorting);
		}
		
		
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
			
			primerProduct = primerProductRepository.findByProductNo(holeNoArray[1]);
			if (primerProduct != null && primerProduct.getBoardNo() != null
					&& !"".equals(primerProduct.getBoardNo())
					&& !boardNo.equals(primerProduct.getBoardNo())) {
				throw new Exception("您提交的生产编号" + holeNoArray[1] + "已在板号"+ primerProduct.getBoardNo() + "中。不允许提交！");
			}
		}
		
		for (int i = 0; i < holeStrArray.length; i++) {
			String holeNoStr = holeStrArray[i];
			String[] holeNoArray = holeNoStr.split(":");
			
			primerProduct = primerProductRepository.findByProductNo(holeNoArray[1]);
			primerProduct.setModifyTime(new Date());
			
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
			boardHole.setCreateUser(user.getUser().getId());
			boardHole.setCreateTime(new Date());
			boardHole.setStatus(0);//0 正常 1 删除
			boardHole.setModifyTime(new Date());
			
			if (lastHoleMap.get(holeNoArray[0]) != null) {
				boardHole.setSorting(lastHoleMap.get(holeNoArray[0]));
			}else{
				boardHole.setSorting(0);
			}
			
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
			primerProductOperation.setUserCode(user.getUser().getCode());
			primerProductOperation.setUserName(user.getUser().getName());
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
	public String submitSynthesis(String boardNo, List<BoardHole> boardHoleList) {
    	
    	ShiroUser shrioUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	User user = shrioUser.getUser();
    	
    	Map<String, BoardHole> bhMap = new HashMap<String, BoardHole>();
    	for(BoardHole bh:boardHoleList){
    		System.out.println("=========页面的选择合成结果="+bh.getHoleNo()+"="+ bh.getFailFlag()+"="+ bh.getRemark());
    		bhMap.put(bh.getHoleNo(), bh);
    	}
    	
    	Board board = boardRepository.findByBoardNo(boardNo);
		BoardHole boardHoleTemp = null;//页面取得的孔信息
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (BoardHole boardHole:board.getBoardHoles()) {
			
			primerProduct = boardHole.getPrimerProduct();
			
			if (bhMap.get(boardHole.getHoleNo()) != null) {
				boardHoleTemp = (BoardHole)bhMap.get(boardHole.getHoleNo());
				String failFlag = boardHoleTemp.getFailFlag();//是否失败标志
				String failReason = boardHoleTemp.getRemark();//失败原因
				
				type = null;
				boardHole.getPrimerProduct().setModifyTime(new Date());//最后修改时间
				boardHole.setModifyTime(new Date());//最后修改时间
				boardHole.setModifyUser(user.getId());//修改人id
				
				if ("0".equals(failFlag)) { // Synthesis success
					failReason = "";
					
					if (!"".equals(primerProduct.getModiFiveType())
							|| !"".equals(primerProduct.getModiThreeType())
							|| !"".equals(primerProduct.getModiMidType())
							|| !"".equals(primerProduct.getModiSpeType())) { // 需要修饰，到待修饰状态
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.modification);
					} else {//不需要修饰，到待氨解状态
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.ammonia);
					}
					
					board.setOperationType(boardHole.getPrimerProduct().getOperationType());
					
					type = PrimerOperationType.synthesisSuccess;
					typeDesc = PrimerOperationType.synthesisSuccess.desc();
					
				} else if ("1".equals(failFlag)) { // Synthesis fail
					
					if (boardHole.getPrimerProduct().getBackTimes() != null) {
						boardHole.getPrimerProduct().setBackTimes(boardHole.getPrimerProduct().getBackTimes()+1);//循环重回次数+1
					}else{
						boardHole.getPrimerProduct().setBackTimes(1);
					}
					
					boardHole.setStatus(1);//删除
					boardHole.getPrimerProduct().setBoardNo("");//清空板号
					boardHole.getPrimerProduct().setOperationType(PrimerStatusType.makeBoard);//回到待制板
					
					type = PrimerOperationType.synthesisFailure;
					typeDesc = PrimerOperationType.synthesisFailure.desc();
					
				}
				
				//组装操作信息
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(boardHole.getPrimerProduct());
				primerProductOperation.setUserCode(user.getCode());
				primerProductOperation.setUserName(user.getName());
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setType(type);
				primerProductOperation.setTypeDesc(typeDesc);
				primerProductOperation.setBackTimes(boardHole.getPrimerProduct().getBackTimes());
				primerProductOperation.setFailReason(failReason);
				
				if ("1".equals(failFlag)) {
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
    	
    	return "";
    }
    
    /**
     * 导出上机表文件
     * @throws IOException 
     * */
	public void exportMachineTable(String boardNo, Invocation inv) throws IOException {
		
		String tableContext = "";//
		Board board = boardRepository.findByBoardNo(boardNo);
		PrimerProduct primerProduct = null;
		int i = 0;
		for(BoardHole bh:board.getBoardHoles()){
			primerProduct = bh.getPrimerProduct();
			if ( i !=0 ){//行尾加回车
				tableContext += "\r\n";
			}
			//生产编号
			if (!"".equals(primerProduct.getProductNo())) {
				tableContext += primerProduct.getProductNo()+",";
			}else{
				tableContext += primerProduct.getOutProductNo()+", ";
			}
			
			//序列，如果是删除的，展现错误类型信息
			if(bh.getStatus() == 0){
				tableContext += primerProduct.getGeneOrderMidi().replaceAll("\\(\\*\\)", "\\*");
				//3端修饰的引物序列，并在序列前自动加一个N
				if (!"".equals(primerProduct.getModiThreeType())) {
					tableContext += "N";
				}
			}else if(bh.getStatus() == 1){
				tableContext += bh.getPrimerProductOperation().getTypeDesc();
			}
			i++;
		}
    	
		//输出文件到客户端
		String fileName = boardNo + ".SEQ";
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
		
    	ShiroUser shrioUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	User user = shrioUser.getUser();
    	
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"";
		String excelFilePath = templatePath+"packTable.xls";
		String configFilePath = templatePath+"packTableHoleConfig.txt";
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        String currentTime = df.format(new Date());
        Board board = boardRepository.findByBoardNo(boardNo);
        String boardType = "";
        if (board != null){
        	boardType = board.getBoardType();
        }
        
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
        	pthc.setBoardType(configArray[3]);
			if (boardType.equals(pthc.getBoardType())) {//板类型匹配才使用
        		pthcMap.put(configArray[0], pthc);
        	}
        	
        }
        read.close();

		
        if (board != null){
        	for(BoardHole boardHole:board.getBoardHoles()){
        		if (pthcMap.get(boardHole.getHoleNo()) != null) {
        			pthc = pthcMap.get(boardHole.getHoleNo());
        			pthc.setPrimerProduct(boardHole.getPrimerProduct());
        			pthc.setStatus(boardHole.getStatus());
					if (pthc.getStatus() == 1) {//删除
						pthc.setReason(boardHole.getPrimerProductOperation().getTypeDesc());
        			}
        			pthcMap.put(boardHole.getHoleNo(), pthc);
        		}
        	}
        }
		
		// 读取分装表模板 形成Excel
		String strFileName = System.currentTimeMillis()+".xls";
		
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFilePath));
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;

		BigDecimal totalBN= new BigDecimal(0);//总碱基数
		PrimerProduct pp = null;
		PrimerProduct ppLast = null;
		String holeNo = "";
		for (Map.Entry<String, PackTableHoleConfig> map : pthcMap.entrySet()) {
			pthc = map.getValue();
			holeNo = map.getKey();
			
			row = sheet.getRow(pthc.getRow());
			cell = row.getCell(pthc.getColumn());// 孔号
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(holeNo);
			
			pp = pthc.getPrimerProduct();
			if( pp != null ){
				
				addNewValue(pp);
				
				totalBN = totalBN.add(pp.getTbn());
                
				// 生产编号
				cell = row.getCell(pthc.getColumn()+1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if (!"".equals(pp.getProductNo())) {
					cell.setCellValue(pp.getProductNo());
				}else{
					cell.setCellValue(pp.getOutProductNo());
				}
				if (pthc.getStatus() == 0) {//正常数据
					
					// 分装
					cell = row.getCell(pthc.getColumn()+3);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//					if (pp.getOrder().getOrderUpType() == OrderType.nmol) {
//						cell.setCellValue(pp.getNmolTB()+"N*"+pp.getTb().toBigInteger());
//					}else{
//					}
					cell.setCellValue(pp.getOdTB()+"D*"+pp.getTb().toBigInteger());
					
					// 体积:导入测试数据后显示体积,如果在测试之前：显示级别顺序：1：修饰，2：HPLC，3：PAGE)
					if (pp.getMeasureVolume() != null) {
						
						//重合引物把体积数改为“重合”二字
						int ppoCount = primerProductOperationRepository.getCountWithPPID(pp.getId());
						
						cell = row.getCell(pthc.getColumn() + 4);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						
						if(ppoCount == 0){
							cell.setCellValue(pp.getMeasureVolume() + "");
						}else{
							cell.setCellValue("重合");
						}
					} else if (!"".equals(pp.getModiFiveType())|| !"".equals(pp.getModiThreeType())
							|| !"".equals(pp.getModiMidType()) || !"".equals(pp.getModiSpeType())) {
						String modiStr = "";
						if (!"".equals(pp.getModiFiveType())) {
							modiStr += pp.getModiFiveType()+",";
						}
						if (!"".equals(pp.getModiThreeType())) {
							modiStr += pp.getModiThreeType()+",";
						}
						if (!"".equals(pp.getModiMidType())) {
							modiStr += pp.getModiMidType()+",";
						}
						if (!"".equals(pp.getModiSpeType())) {
							modiStr += pp.getModiSpeType()+",";
						}
						if (modiStr.length() > 0) {
							while(modiStr.indexOf(",")!=-1 && modiStr.endsWith(",")){
								modiStr = modiStr.substring(0, modiStr.length() - 1);
							}
							modiStr = "(" + modiStr + ")";
						}
						
						cell = row.getCell(pthc.getColumn() + 4);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(modiStr);
					} else if ("HPLC".equals(pp.getPurifyType())) {
						cell = row.getCell(pthc.getColumn() + 4);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("HPLC");
					} else if ("PAGE".equals(pp.getPurifyType())) {
						cell = row.getCell(pthc.getColumn() + 4);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("PAGE");
					} else if ("page".equals(pp.getPurifyType())) {
						cell = row.getCell(pthc.getColumn() + 4);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("page");
					}
					
					ppLast = pp;
				}else{
					cell = row.getCell(pthc.getColumn() + 3);//显示失败原因
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(pthc.getReason());
				}
			}
		}

		
		//板号
		row = sheet.getRow(0);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(boardNo);
		row = sheet.getRow(28);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(boardNo);
		
		String remark = "备注：  合成柱:                   总碱基="+totalBN+"    洗脱体积=       μl";
		row = sheet.getRow(15);
		cell = row.getCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(remark);
		row = sheet.getRow(45);
		cell = row.getCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(remark);
		
		
		//制表
		row = sheet.getRow(17);
		cell = row.getCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("操作员："+user.getName());
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
					cell = row.getCell(7);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(8);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.purifySuccess)) {
					//纯化
					cell = row.getCell(11);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(12);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.measureSuccess)) {
					//测值
					cell = row.getCell(13);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(14);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.packSuccess)) {
					//分装
					cell = row.getCell(15);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(16);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.bakeSuccess)) {
					//干燥
					cell = row.getCell(17);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(18);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("时间："+df.format(ppo.getCreateTime()));
				}else if (ppo.getType().equals(PrimerOperationType.deliverySuccess)) {
					//发货
					cell = row.getCell(21);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("操作员："+ppo.getUserName());
					cell = row.getCell(22);
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
    		}else if(pv.getType().equals(PrimerValueType.MW)){
    			primerProduct.setMw(pv.getValue());
    		}
    	}
		
	}
	
	
	
	    //根据operationType等条件查询生产数据列表
	public Page<PrimerProduct> resultsSelectQuery(String boardNo,
			String productNo, PrimerStatusType operationType,
			String modiFiveType, String modiThreeType, String modiMidType,
			String modiSpeType, String purifyType, String comCode,
			Pageable pageable) {

		    Page<PrimerProduct> primerProductPage = null;
		    if (operationType == PrimerStatusType.modification) {//操作类型为修饰时只查有修饰的数据
		    	primerProductPage = primerProductRepository.resultsSelectQueryDecorate(boardNo, operationType.toString(), comCode, pageable);
		    }else{
		    	primerProductPage = primerProductRepository.resultsSelectQuery(boardNo,productNo,operationType.toString(), modiFiveType, modiThreeType, modiMidType, modiSpeType,purifyType,comCode, pageable);
		    }
			
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
					}else if(ppv.getType().equals(PrimerValueType.MW)){
		    			primerProduct.setMw(ppv.getValue());
		    		}
				}
				// 翻译操作类型
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
				
			   if (primerProduct.getReviewFileName() == null || "null".equals(primerProduct.getReviewFileName())) {
				   primerProduct.setReviewFileName("");
			   }
				   
				//文件超连接
			   if (!"".equals(primerProduct.getReviewFileName())) {
				   String templateFilePath= "D:\\geneUploadFile\\detect\\"+primerProduct.getReviewFileName();
				   System.out.println("检测结果查询页面，下载附件文件的路径templateFilePath="+templateFilePath);
				   primerProduct.setReviewFileName("<a href='"+templateFilePath+"' target='_blank'>"+primerProduct.getReviewFileName()+"</a>");
				}
			   
			   if(primerProduct.getBackTimes() == null){
				   primerProduct.setBackTimes(0);
			   }

			}

			return primerProductPage;
		}
		
		
	//提交修饰,检测等选择的信息
    @Transactional(readOnly = false)
	public String resultsSelectSubmit(List<PrimerProduct> primerProducts,
			String successFlag,PrimerStatusType operationType, String failReason) {
    	
    	ShiroUser shrioUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	User user = shrioUser.getUser();
    	
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (PrimerProduct pp:primerProducts) {
			
			PrimerProduct primerProduct = primerProductRepository.findOne(pp.getId());
			BoardHole boardHole = null;
			List<BoardHole> boardHoles = boardHoleRepository.findByPrimerProduct(primerProduct);
			for(BoardHole bh:boardHoles){
				if (bh.getBoard().getBoardNo().equals(primerProduct.getBoardNo())) {//如果是本板下的数据功
					boardHole = bh;
				}
			}
			primerProduct.setModifyTime(new Date());//最后修改时间
			
			if (boardHole != null) {
				boardHole.setModifyTime(new Date());//最后修改时间
				boardHole.setModifyUser(user.getId());
			}
			
			if(primerProduct.getBackTimes() == null){
				primerProduct.setBackTimes(0);
			}
			type = null;
			
			if ("1".equals(successFlag)) { // success
				
				if (operationType.equals(PrimerStatusType.modification)) {
					primerProduct.setOperationType(PrimerStatusType.purify);
					type = PrimerOperationType.modiSuccess;
					typeDesc = PrimerOperationType.modiSuccess.desc();
				}else if (operationType.equals(PrimerStatusType.detect)) {
					primerProduct.setOperationType(PrimerStatusType.delivery);
					type = PrimerOperationType.detectSuccess;
					typeDesc = PrimerOperationType.detectSuccess.desc();
				}
				
			} else if ("0".equals(successFlag)) { // fail
				
				primerProduct.setOperationType(PrimerStatusType.makeBoard);//回到待制板
				primerProduct.setBoardNo("");//清空板号
				if (primerProduct.getBackTimes() != null) {
					primerProduct.setBackTimes(primerProduct.getBackTimes()+1);//循环重回次数+1
				}else{
					primerProduct.setBackTimes(1);
				}
				
				if (boardHole != null) {
					boardHole.setStatus(1);//删除
				}
				
				if (operationType.equals(PrimerStatusType.modification)) {
					type = PrimerOperationType.modiFailure;
					typeDesc = PrimerOperationType.modiFailure.desc();
				}else if (operationType.equals(PrimerStatusType.detect)) {
					type = PrimerOperationType.detectFailure;
					typeDesc = PrimerOperationType.detectFailure.desc();
				}

			}
			
			//组装操作信息
			primerProductOperation = new PrimerProductOperation();
			primerProductOperation.setPrimerProduct(primerProduct);
			primerProductOperation.setUserCode(user.getCode());
			primerProductOperation.setUserName(user.getName());
			primerProductOperation.setCreateTime(new Date());
			primerProductOperation.setType(type);
			primerProductOperation.setTypeDesc(typeDesc);
			primerProductOperation.setBackTimes(primerProduct.getBackTimes());
			primerProductOperation.setFailReason(failReason);
			
			if ("0".equals(successFlag)) {
				boardHole.setPrimerProductOperation(primerProductOperation);
			}
			
			//保存轨迹信息
			primerProductOperationRepository.save(primerProductOperation);

			//保存primer_product表数据
			primerProductRepository.save(primerProduct);
			
			//更新孔信息
			boardHoleRepository.save(boardHole);
			
			
			//如果是本板号中最后一个修饰（检测）的数据，则更新板状态
			String boardNo = boardHole.getBoard().getBoardNo();
			Board board = boardRepository.findByBoardNo(boardNo);
			
			boolean boardFlag = false;
			boolean allPPFlag = true;
			
			if(board.getOperationType() == operationType){
				boardFlag = true;
			}
			
			for(BoardHole bh:board.getBoardHoles()){
				PrimerProduct ppTemp = bh.getPrimerProduct();
				if(ppTemp.getOperationType() == operationType){
					allPPFlag = false;
					break;
				}
			}
			if(boardFlag && allPPFlag){
				if (operationType.equals(PrimerStatusType.modification)) {
					board.setOperationType(PrimerStatusType.purify);
					
				} else if (operationType.equals(PrimerStatusType.detect)) {
					board.setOperationType(PrimerStatusType.delivery);
					
				}
				boardRepository.save(board);
			}
		}
    	
    	return "";
    }
		
		
    //组织板页面信息
    @Transactional(readOnly = false)
	public String boardEdit(String boardNo, PrimerStatusType operationType, MultipartFile file, Invocation inv) throws IOException {
		
		
		Map<String, BigDecimal> measureMap = new HashMap<String, BigDecimal>();
		if(operationType.equals(PrimerStatusType.measure) && file!=null){
			measureMap = this.measureData(file);
		}
		
		int totalCount = 0;
		String typeFlag = "1";//生产数据是否复合查询类型 ：1 复合， 0 不复合
		OrderType orderUpType = null;
		//查询板号信息
		Map<String, PrimerProduct> boardHoleMap = new HashMap<String, PrimerProduct>();
		Board board = boardRepository.findByBoardNo(boardNo);
		if (board != null) {
			for (BoardHole boardHole : board.getBoardHoles()) {
				PrimerProduct primerProduct = boardHole.getPrimerProduct();
				
				this.addNewValue(primerProduct);
				
				if (boardHole.getStatus() == 0  && operationType == primerProduct.getOperationType()) {//0正常  1 删除   && 类型相同
					boardHoleMap.put(boardHole.getHoleNo(), primerProduct);
					totalCount += 1;
					orderUpType = boardHole.getPrimerProduct().getOrder().getOrderUpType();
				}
				
//				if (!boardHole.getPrimerProduct().getOperationType().equals(operationType) && boardHole.getStatus() == 0) {
//					typeFlag = "0";
//				}
			}
		}

		String totalLabel = "";
		if (orderUpType == OrderType.nmol) {
			totalLabel = "NOML / TUBE * "+totalCount;
		}else{
			totalLabel = "OD / TUBE * "+totalCount;
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
		String measureVolume = "";//测值体积
		String warning = "";//预警标志
		
		String jsonStr = "{\r";
		jsonStr += "\"typeFlag\":"+typeFlag+",\r";
		jsonStr += "\"typeDesc\":\""+operationType.desc()+"\",\r";
		jsonStr += "\"total\":\""+totalLabel+"\",\r";
		jsonStr += "\"rows\":[\r";
		
		for(int i=0 ;i<holeList.size();i++){
			
			jsonStr += "{\"row"+(i+1)+"\":[\r";
			
			String hole = (String)holeList.get(i);
			for (int j=1;j<13;j++){
				
				holeNo = hole+j;
				
				productNo = "";
				purifyType = "";
				measureVolume = "";
				warning = "";
				
				if(boardHoleMap.get(holeNo) != null){
					primerProduct = (PrimerProduct)boardHoleMap.get(holeNo);
					measureVolume = primerProduct.getProductNo();
					
					//纯化，不包括OPC 才显示
					if (!"OPC".equals(primerProduct.getPurifyType())
							&& operationType.equals(PrimerStatusType.purify)) {
						purifyType = primerProduct.getPurifyType();
					}
					
					//测值 & 孔有数值
					if (operationType.equals(PrimerStatusType.measure) && measureMap.get(holeNo) != null) {
						
						BigDecimal measure = (BigDecimal)measureMap.get(holeNo);
						
						//干粉
						if (primerProduct.getLiquid() == null || "".equals(primerProduct.getLiquid())) {
							//改为：OD/tube*22.5/测量值
							measureVolume = new BigDecimal(22.5).multiply(primerProduct.getOdTB()).divide(new BigDecimal(measure.doubleValue()),0, BigDecimal.ROUND_UP)+"";
							
						}
						//液体
						else{

							double    nmoleOD      = 0.0;//nmoleOD
							for (PrimerProductValue primerProductValue : primerProduct.getPrimerProductValues()) {
								PrimerValueType type = primerProductValue.getType();
								if (type.equals(PrimerValueType.nmoleOD)) {// nmoleOD
									nmoleOD = primerProductValue.getValue().doubleValue();
								}
							}
							
							measureVolume = primerProduct.getMeasureVolume()+"";//液体引物，直接取导入订单的计算值：nmol/tube*1000/浓度
							
							//计算出补水体积，直接存到数据库中。保留整数，四舍五入
							//液体的 补水体积公式是：=nmole/OD数据*95*（测量值/15）*1000/浓度数据-100
							//浓度数据是10 时，按照15计算
							if(primerProduct.getDensity()==10){
//								primerProduct.setDensity(15);//20180626先注释掉
							}
							int volume =  new BigDecimal(nmoleOD*95*(measure.doubleValue()/15)*1000/primerProduct.getDensity()-100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();  ;
							
							primerProduct.setVolume(volume);
							//保存primer_product表数据
							primerProductRepository.save(primerProduct);
						}
						
						//判断测值数值是否大于(OD数值*40*2)
						BigDecimal odTotal = primerProduct.getOdTotal();
						odTotal = odTotal.multiply(new BigDecimal(80));
						BigDecimal measureVolumeNum = new BigDecimal(measureVolume);
						
						if(measureVolumeNum.compareTo(odTotal)==1){//大于
							warning = "maxODTotal";//颜色提醒
						}
						
					}
				}
				
				jsonStr += "{\"tag\":\""+holeNo+"\",\"No\":\""+measureVolume+"\",\"identifying\":\""+purifyType+"\",\"warning\":\""+warning+"\"}";
				
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

		jsonStr += "]\r}";
		
		System.out.println("==========拼接的jsonstr=\r"+jsonStr);
		
		return jsonStr;
	}	
		
	//提交板編輯信息
    @Transactional(readOnly = false)
	public String submitBoardEdit(String boardNo,
			List<BoardHole> boardHoleList, PrimerStatusType operationType) throws IOException {
    	
    	ShiroUser shrioUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	User user = shrioUser.getUser();
    	
    	Map<String, BoardHole> bhMap = new HashMap<String, BoardHole>();
    	for(BoardHole bh:boardHoleList){
    		bhMap.put(bh.getHoleNo(), bh);
    	}
    	
    	Board board = boardRepository.findByBoardNo(boardNo);
		BoardHole boardHoleTemp = null;
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		Map<PrimerStatusType, Integer> typeMap = this.getTypeOrder();
		int boardOrder = typeMap.get(board.getOperationType());
		
		for (BoardHole boardHole:board.getBoardHoles()) {
			
			if(bhMap.get(boardHole.getHoleNo())!=null){
				
				boardHoleTemp = (BoardHole)bhMap.get(boardHole.getHoleNo());
				String failFlag = boardHoleTemp.getFailFlag();
				String failReason = boardHoleTemp.getRemark();
				type = null;
				
				boardHole.getPrimerProduct().setModifyTime(new Date());//最后修改时间
				boardHole.setModifyTime(new Date());//最后修改时间
				boardHole.setModifyUser(user.getId());
				
				if ("0".equals(failFlag)) { // success
					failReason = "";
					
					if (operationType.equals(PrimerStatusType.ammonia)) {
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.purify);
						type = PrimerOperationType.ammoniaSuccess;
						typeDesc = PrimerOperationType.ammoniaSuccess.desc();
					}else if (operationType.equals(PrimerStatusType.purify)) {
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.measure);
						type = PrimerOperationType.purifySuccess;
						typeDesc = PrimerOperationType.purifySuccess.desc();
					}else if (operationType.equals(PrimerStatusType.pack)) {
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.detect);//20180625 分装成功后到检测环节
						type = PrimerOperationType.packSuccess;
						typeDesc = PrimerOperationType.packSuccess.desc();
					}
//					else if (operationType.equals(PrimerStatusType.bake)) {
//						//重新分装的烘干后不走检测环节,直接到发货
//						if (boardOrder <= typeMap.get(PrimerStatusType.bake)) {
//							boardHole.getPrimerProduct().setOperationType(PrimerStatusType.detect);
//						}else{
//							boardHole.getPrimerProduct().setOperationType(PrimerStatusType.delivery);
//						}
//						
//						type = PrimerOperationType.bakeSuccess;
//						typeDesc = PrimerOperationType.bakeSuccess.desc();
//					}
					else if (operationType.equals(PrimerStatusType.measure)) {
						boardHole.getPrimerProduct().setMeasureVolume(boardHoleTemp.getPrimerProduct().getMeasureVolume());//测值体积
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.pack);
						type = PrimerOperationType.measureSuccess;
						typeDesc = PrimerOperationType.measureSuccess.desc();
					}
					//板的状态不往回走
					int primerProductOrder = typeMap.get(boardHole.getPrimerProduct().getOperationType());
					if (boardOrder < primerProductOrder) {
						board.setOperationType(boardHole.getPrimerProduct().getOperationType());// 板的状态赋值
					}
					
				} //1 失败，  2 重新合成 ，3 重新分装
				else if ("1".equals(failFlag) || "2".equals(failFlag) || "3".equals(failFlag)) { // fail
					
					if ("3".equals(failFlag)) {
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.pack);//回到分装
					}else{
						boardHole.getPrimerProduct().setOperationType(PrimerStatusType.makeBoard);//回到待制板
						boardHole.getPrimerProduct().setBoardNo("");//清空板号
						boardHole.setStatus(1);//删除
					}
					if (boardHole.getPrimerProduct().getBackTimes() != null) {
						boardHole.getPrimerProduct().setBackTimes(boardHole.getPrimerProduct().getBackTimes()+1);//循环重回次数+1
					}else{
						boardHole.getPrimerProduct().setBackTimes(1);
					}
					
					
					if (operationType.equals(PrimerStatusType.ammonia)) {
						type = PrimerOperationType.ammoniaFailure;
						typeDesc = PrimerOperationType.ammoniaFailure.desc();
					}else if (operationType.equals(PrimerStatusType.purify)) {
						type = PrimerOperationType.purifyFailure;
						typeDesc = PrimerOperationType.purifyFailure.desc();
					}else if (operationType.equals(PrimerStatusType.pack)) {
						type = PrimerOperationType.packFailure;
						typeDesc = PrimerOperationType.packFailure.desc();
					}else if (operationType.equals(PrimerStatusType.bake)) {
						type = PrimerOperationType.bakeFailure;
						typeDesc = PrimerOperationType.bakeFailure.desc();
					}else if (operationType.equals(PrimerStatusType.measure)) {
						type = PrimerOperationType.measureFailure;
						typeDesc = PrimerOperationType.measureFailure.desc();
					}
					
				}
				
				//组装操作信息
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(boardHole.getPrimerProduct());
				primerProductOperation.setUserCode(user.getCode());
				primerProductOperation.setUserName(user.getName());
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setType(type);
				primerProductOperation.setTypeDesc(typeDesc);
				primerProductOperation.setBackTimes(boardHole.getPrimerProduct().getBackTimes());
				primerProductOperation.setFailReason(failReason);
				
				if ("1".equals(failFlag) || "2".equals(failFlag)) {
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
    	
    	return "";
    }	
		
    //组织测值信息
   	public Map<String, BigDecimal> measureData(MultipartFile file) throws IOException {
   		
   		
   		Map<String, BigDecimal> measureMap = new HashMap<String, BigDecimal>();
   		
		// 读取上传文件
		HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
		ArrayList holeList = new ArrayList();
		holeList.add("A");
		holeList.add("B");
		holeList.add("C");
		holeList.add("D");
		holeList.add("E");
		holeList.add("F");
		holeList.add("G");
		holeList.add("H");
		
		BigDecimal measure = new BigDecimal(0);
		String holeNo = "";
		for (int i = 0; i < holeList.size(); i++) {
			String hole = (String)holeList.get(i);
			row = sheet.getRow(i+1);
			
			for (int j=1;j<13;j++){
				holeNo = hole+j;
				cell = row.getCell(j);
				if(cell!=null){
					if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						measure =  new BigDecimal(cell.getNumericCellValue());
						measureMap.put(holeNo, measure);
					}
				}
			}
		}
		
		System.out.println("====上传excel中测值="+measureMap.toString());
		
		
   		return measureMap;
   	}
   	
    //上传检测文件
   	@Transactional(readOnly = false)
	public List<PrimerProduct> uploadDetect(MultipartFile file, Invocation inv) throws Exception {
		
   		List<PrimerProduct> primerProducts = new ArrayList<PrimerProduct>();
		String realpathdir = "";
    	String path="";
    	String originalFilename = "";
    	if (!file.isEmpty()) {
    		originalFilename = file.getOriginalFilename();
        	realpathdir = "D:\\geneUploadFile\\detect\\";
        	path = realpathdir+originalFilename;
        	
    	    // 创建文件目录
    	    File savedir = new File(realpathdir);
    	    // 如果目录不存在就创建
    	    if (!savedir.exists()) {
    	      savedir.mkdirs();
    	    }
    	    
        	Map<String, String> productNoMap = new HashMap<String, String>();
        	
        	//压缩文件
        	if(originalFilename.toLowerCase().endsWith("rar") || originalFilename.toLowerCase().endsWith("zip")){
        		
        		this.zipDecompress(file, realpathdir);
        		
        	}else{
        		String[] productNoStrArray = originalFilename.split("-");
        		if(productNoStrArray.length>1){
        			productNoMap.put(productNoStrArray[0], originalFilename);
        		}
        		
        		System.out.println("========================上传文件="+productNoMap.toString());
        		System.out.println("========================上传文件 path="+path);
        		file.transferTo(new File(path));
        	}

        	for (Map.Entry<String, String> map : productNoMap.entrySet()) {
        		PrimerProduct primerProduct = primerProductRepository.findByProductNoOrOutProductNo(map.getKey(), map.getKey());
				if (primerProduct != null) {
					this.addNewValue(primerProduct);
					primerProduct.setReviewFileName(map.getValue());
					primerProductRepository.save(primerProduct);
					primerProducts.add(primerProduct);
        		}
        	}
    	}
		
		
		return primerProducts;
	}
   	

	
	public static void zipDecompress(MultipartFile zipfile, String destDir)
			throws IOException
	{
		BufferedInputStream bin = new BufferedInputStream(zipfile.getInputStream());
		ZipInputStream zin = null;
		BufferedOutputStream dest = null;
		try
		{
			File fdir = new File(destDir);
			fdir.mkdirs();
			zin = new ZipInputStream(bin);
			ZipEntry entry;
			
			while ((entry = zin.getNextEntry()) != null)
			{
				System.out.println("=========================="+entry.getName());
				
				String name = entry.getName();
			if (entry.isDirectory())
				{
					String dir = destDir + File.separator + name;
					fdir = new File(dir);
					fdir.mkdirs();
					continue;
				} else {
					byte data[] = new byte[2048];
	                FileOutputStream fos = new FileOutputStream((new StringBuffer()).append(destDir).append(entry.getName()).toString());
	                dest = new BufferedOutputStream(fos, 2048);
	                int count;
	                while((count = zin.read(data, 0, 2048)) != -1) {
	                	dest.write(data, 0, count);
	                }
	                dest.flush();
	                dest.close();
				}
				zin.closeEntry();
			}
		} finally {
			try	{
				if (zin != null)
					zin.close();
				zin = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	 
       /** 解压	 
        *  @param root 输出目标	 
        *  @param zipfile zip文件	 
        * */
	protected void unzip(File root, MultipartFile zipfile) throws Exception { 
		
		ZipInputStream zin = new ZipInputStream(zipfile.getInputStream());
		ZipEntry entry;
		while ((entry = zin.getNextEntry()) != null) {//
			
			System.out.println("=========================="+entry.getName());
			
			File tmpFile = new File(root, entry.getName());
			if (entry.isDirectory()) {
				tmpFile.mkdirs();
			} else {
				byte[] buff = new byte[4096];
				int len = 0;
				tmpFile.getParentFile().mkdirs();
				FileOutputStream fout = new FileOutputStream(tmpFile);
				while ((len = zin.read(buff)) != -1) {
					fout.write(buff, 0, len);
				}
				zin.closeEntry();
				fout.close();
			}
		}

	}
		
    //组织测值信息
   	public Map<PrimerStatusType, Integer> getTypeOrder() throws IOException {
   		
   		Map<PrimerStatusType, Integer> typeMap = new HashMap<PrimerStatusType, Integer>();
   		typeMap.put(PrimerStatusType.makeBoard, 1);
   		typeMap.put(PrimerStatusType.synthesis, 2);
   		typeMap.put(PrimerStatusType.modification, 3);
   		typeMap.put(PrimerStatusType.ammonia, 3);
   		typeMap.put(PrimerStatusType.purify, 4);
   		typeMap.put(PrimerStatusType.measure, 5);
   		typeMap.put(PrimerStatusType.pack, 6);
   		typeMap.put(PrimerStatusType.bake, 7);
   		typeMap.put(PrimerStatusType.detect, 8);
   		typeMap.put(PrimerStatusType.delivery, 9);
		
   		return typeMap;
   	}	
	
    /**
     * 导出合成柱排版文件
     * @throws IOException 
     * */
	public void exportHeChengZhuPaiBan(String boardNo, Invocation inv) throws IOException {
		
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"";
		String excelFilePath = templatePath+"heChengZhuPaiBan.xls";
		
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFilePath));
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
		int rowIndex = 1;
        Board board = boardRepository.findByBoardNo(boardNo);
        if (board != null){
        	for(BoardHole boardHole:board.getBoardHoles()){
        		PrimerProduct pp = boardHole.getPrimerProduct();
        		String holeNo = boardHole.getHoleNo();//孔号
        		String productNo = pp.getOutProductNo();//生产编号
        		if(productNo==null || "".equals(productNo)){
        			productNo = pp.getProductNo();
        		}
        		String purifyType = pp.getPurifyType();//纯化方式
        		
        		String modiStr    = "";//修饰
        		String modiFiveType = pp.getModiFiveType();
        		String modiThreeType= pp.getModiThreeType();
        		String modiMidType  = pp.getModiMidType();
        		String modiSpeType  = pp.getModiSpeType();
        		
        		//判断是否含有5个G
        		boolean res = pp.getGeneOrderMidi().contains("GGGGG");
        		
        		String geneOrder = pp.getGeneOrderMidi().replaceAll("\\(\\*\\)", "\\*");//序列
				//3端修饰的引物序列，并在序列前自动加一个N
				if (!"".equals(modiThreeType)) {
					geneOrder += "N";
				}
        		
        		if (!"".equals(modiFiveType) || !"".equals(modiThreeType) || !"".equals(modiMidType) || !"".equals(modiSpeType)) {
    				modiStr = "(";
    				if (!"".equals(modiFiveType)) {
    					modiStr += "5'"+modiFiveType + ",";
    				}
    				if (!"".equals(modiMidType)) {
    					modiStr += modiMidType + ",";
    				}
    				if (!"".equals(modiSpeType)) {
    					modiStr += modiSpeType + ",";
    				}
    				if (!"".equals(modiThreeType)) {
    					modiStr += "3'"+modiThreeType + ",";
    				}
    				modiStr = modiStr.substring(0, modiStr.length()-1);
    				modiStr += ")";
    			}
        		
        		double odTB  = 0.0;//OD/Tube 
				int tb    = 0;//管数
				int tbn   = 0;//碱基数
				double mw = 0.0;
				for (PrimerProductValue ppv : pp.getPrimerProductValues()) {
					PrimerValueType type = ppv.getType();
					if (type.equals(PrimerValueType.odTB)) {// OD/Tube
						odTB = ppv.getValue().doubleValue();
					}else if(type.equals(PrimerValueType.tb)){//管数
						tb = ppv.getValue().intValue();
					}else if(type.equals(PrimerValueType.baseCount)){
						tbn = ppv.getValue().intValue();
		      		}else if(type.equals(PrimerValueType.MW)){
		      			mw = ppv.getValue().doubleValue();
		      		}
				}
        		
				row = sheet.createRow(rowIndex);
				cell = row.createCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(holeNo);//
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(productNo);//
				cell = row.createCell(2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(geneOrder);//
				cell = row.createCell(3);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(tbn);//
				cell = row.createCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(odTB);//
				cell = row.createCell(5);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(tb);//
				cell = row.createCell(6);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(purifyType);//
				cell = row.createCell(7);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(modiStr);//
				cell = row.createCell(8);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(mw);//
				cell = row.createCell(9);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if(res){
					cell.setCellValue("是");
				}else{
					cell.setCellValue("否");
				}
				rowIndex ++;
        	}
        }
		
		// 读取分装表模板 形成Excel
		String strFileName = boardNo+".xls";
		
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
     * 导出补水体积附件
     * @throws IOException 
     * */
	public void downVolume( StatisticsInfo statisticsInfo, Invocation inv) throws IOException {
		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String boardNo = statisticsInfo.getBoardNo();
        
		Board board = boardRepository.findByBoardNo(boardNo);
        List<BoardHole> bhs = boardHoleRepository.findByBoard(board);
        
        Map<String, Integer> volumeMap = new HashMap<String, Integer>();
        for (BoardHole bh : bhs) {
			if(bh.getPrimerProduct().getVolume()!=null){
				volumeMap.put(bh.getHoleNo(), bh.getPrimerProduct().getVolume());
			}
        }
        

		//形成Excel
		String templetName = "bstjTemplate.xls";
		String strFileName = System.currentTimeMillis()+".xls";
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
			
		int startRow = 2;//从第2行开始
		
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
		String volume = "";
		for (int i = 0; i < holeList.size(); i++) {
			String hole = (String)holeList.get(i);
			row = sheet.getRow(i+1);
			
			for (int j=1;j<13;j++){
				holeNo = hole+j;
				volume = "";
				if (volumeMap.get(holeNo) != null) {
					volume = volumeMap.get(holeNo).toString();
				}
				cell = row.getCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(volume);//补水体积
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
