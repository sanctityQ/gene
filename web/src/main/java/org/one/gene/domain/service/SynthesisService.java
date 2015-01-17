package org.one.gene.domain.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Raw;

import org.apache.commons.lang.StringUtils;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
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
	public Page<PrimerProduct> makeBoardQuery(String customer_code,
			String modiFlag, String tbn1, String tbn2, String purifytype,
			Pageable pageable) {

		Page<PrimerProduct> primerProductPage = primerProductRepository.selectPrimerProduct(customer_code, modiFlag, tbn1, tbn2, purifytype, pageable);
		
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
	public String makeBoard(String boardNo, String flag, String productNoStr, Invocation inv) throws IOException{
		
		System.out.println("=====页面选择的生产编号=" + productNoStr);
		
		Map<String, String> productNoMap = new HashMap<String, String>();
		String[] productNoStrArray = productNoStr.split(",");
		for (int i = 0; i < productNoStrArray.length; i++) {
			
			productNoMap.put(productNoStrArray[i], productNoStrArray[i]);
		}
		
		//查询板号信息
		Map<String, String> boardHoleMap = new HashMap<String, String>();
		Board board = boardRepository.findByBoardNo(boardNo);
		if (board != null) {
			for (BoardHole boardHole : board.getBoardHoles()) {
				boardHoleMap.put(boardHole.getHoleNo(), boardHole.getPrimerProduct().getProductNo());
			}
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
		jsonStr += "\"total\":96,\r";
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
	public String submitSynthesis(Board board, String failReason) {
    	
		BoardHole boardHole = null;
		List<BoardHole> boardHoles = board.getBoardHoles();
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerType.PrimerOperationType type = null;
		String typeDesc = "";
		
		for (int i = board.getBoardHoles().size() - 1; i >= 0; i--) {
			boardHole = (BoardHole) board.getBoardHoles().get(i);
			primerProduct = boardHole.getPrimerProduct();
			String selectFlag = primerProduct.getSelectFlag();
			

			
			if ("1".equals(selectFlag)) { // Synthesis success
				
				if (!"".equals(primerProduct.getModiFiveType())
						|| !"".equals(primerProduct.getModiThreeType())
						|| !"".equals(primerProduct.getModiMidType())
						|| !"".equals(primerProduct.getModiSpeType())) { // 需要修饰，到待修饰状态
					boardHole.getPrimerProduct().setOperationType(PrimerStatusType.modification);
				} else {//不需要修饰，到待氨解状态
					boardHole.getPrimerProduct().setOperationType(PrimerStatusType.ammonia);
				}

				type = PrimerType.PrimerOperationType.synthesisSuccess;
				typeDesc = "合成成功";
				
			} else if ("2".equals(selectFlag)) { // Synthesis fail

				boardHole.getPrimerProduct().setOperationType(PrimerStatusType.synthesis);//回到待合成
				boardHole.getPrimerProduct().setBoardNo("");//清空板号
				boardHole.getPrimerProduct().setBackTimes(boardHole.getPrimerProduct().getBackTimes()+1);//循环重回次数+1
                
				//删除孔信息
				boardHoleRepository.delete(boardHole);
				boardHoles.remove(i);
				
				type = PrimerType.PrimerOperationType.synthesisFailure;
				typeDesc = "合成失败";
				
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
			
			//更新操作信息
			//primer_product_operation
//			primerProductOperationRepository.save(primerProductOperations);
			
			//保存primer_product表数据
			primerProductRepository.save(boardHole.getPrimerProduct());
			
    	}
    	
		boardRepository.save(board);
		
    	
    	return "";
    }
    
    /**
     * 导出上机表文件
     * @throws IOException 
     * */
	public EntityReply<File> exportMachineTable(String boardNo, Invocation inv) throws IOException {
		
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
    	
		//读取上机表文件输出地址
		InputStream inputStream  =   this .getClass().getClassLoader().getResourceAsStream("application.properties");    
		Properties p = new  Properties(); 
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}   
		
		//生成文件
		String fileName = boardNo + "_shangjibiao.SEQ";
		FileOutputStream   fos = new FileOutputStream(p.getProperty("sjbPath")+fileName);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter     bw  = new BufferedWriter(osw);
		bw.write(tableContext);
		bw.newLine();
		bw.close();
		
		File file = new File(p.getProperty("sjbPath"), fileName); 
		
		return Replys.with(file).as(Raw.class).downloadFileName(fileName);
    }
	
	//补充临时字段的值
	public void addNewValue(PrimerProduct primerProduct) {
    	//补充4个临时字段的值
    	for (PrimerProductValue pv : primerProduct.getPrimerProductValues()) {
    		if(pv.getType().equals(PrimerValueType.odTotal)){
    			primerProduct.setOdTotal(pv.getValue());
    		}else if(pv.getType().equals(PrimerValueType.odTB)){
    			primerProduct.setOdTB(pv.getValue());
    		}else if(pv.getType().equals(PrimerValueType.nmolTotal)){
    			primerProduct.setNmolTotal(pv.getValue());
    		}else if(pv.getType().equals(PrimerValueType.nmolTB)){
    			primerProduct.setNmolTB(pv.getValue());
    		}
    	}
		
	}
}
