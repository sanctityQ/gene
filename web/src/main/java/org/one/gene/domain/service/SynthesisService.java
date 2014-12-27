package org.one.gene.domain.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.Properties;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Raw;

import org.apache.commons.lang.StringUtils;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.PrimerOperationType;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.one.mvc.util.MvcPathUtil;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
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
	public Page<PrimerProduct> makeTableQuery(String customer_code,
			String modiFlag, String tbn1, String tbn2, String purifytype,
			Pageable pageable) {

		Page<PrimerProduct> primerProductPage = primerProductRepository.selectPrimerProduct(customer_code, modiFlag, tbn1, tbn2, purifytype, pageable);
		
		// 查询primer_product_value
		for (PrimerProduct primerProduct : primerProductPage) {
			List<PrimerProductValue> primerProductValues = primerProductValueRepository.selectValueByPrimerProductId(primerProduct.getId());
			for (PrimerProductValue ppv : primerProductValues) {
				if (ppv.getType().equals("odTotal")) {
					primerProduct.setOdTotal(ppv.getValue());
				} else if (ppv.getType().equals("odTB")) {
					primerProduct.setOdTB(ppv.getValue());
				} else if (ppv.getType().equals("nmolTotal")) {
					primerProduct.setNmolTotal(ppv.getValue());
				} else if (ppv.getType().equals("nmolTB")) {
					primerProduct.setNmolTB(ppv.getValue());
				} else if (ppv.getType().equals("tbn")) {
					primerProduct.setTbn(ppv.getValue());
				}
			}
			// 翻译操作类型
			PrimerOperationType operationType = primerProduct.getOperationType();
			if (operationType.equals(PrimerOperationType.orderInit)) {
				primerProduct.setOperationTypeDesc("订单初始化");
			} else if (operationType.equals(PrimerOperationType.orderCheckFailure)) {
				primerProduct.setOperationTypeDesc("订单检查失败");
			} else if (operationType.equals(PrimerOperationType.orderCheckSuccess)) {
				primerProduct.setOperationTypeDesc("订单检查成功");
			} else if (operationType.equals("synthesisPrepare")) {
				primerProduct.setOperationTypeDesc("合成准备");
			} else if (operationType.equals("synthesisSuccess")) {
				primerProduct.setOperationTypeDesc("合成成功");
			} else if (operationType.equals("synthesisFailure")) {
				primerProduct.setOperationTypeDesc("合成失败");
			} else if (operationType.equals(PrimerOperationType.modification)) {
				primerProduct.setOperationTypeDesc("修饰");
			} else if (operationType.equals(PrimerOperationType.ammonia)) {
				primerProduct.setOperationTypeDesc("氨解");
			} else if (operationType.equals(PrimerOperationType.purify)) {
				primerProduct.setOperationTypeDesc("纯化");
			} else if (operationType.equals(PrimerOperationType.measure)) {
				primerProduct.setOperationTypeDesc("测值");
			} else if (operationType.equals(PrimerOperationType.pack)) {
				primerProduct.setOperationTypeDesc("分装");
			} else if (operationType.equals(PrimerOperationType.bake)) {
				primerProduct.setOperationTypeDesc("烘干");
			} else if (operationType.equals(PrimerOperationType.detect)) {
				primerProduct.setOperationTypeDesc("检测");
			} else if (operationType.equals(PrimerOperationType.delivery)) {
				primerProduct.setOperationTypeDesc("发货");
			} else if (operationType.equals(PrimerOperationType.back)) {
				primerProduct.setOperationTypeDesc("召回");
			}
		}

		return primerProductPage;
	}

    //到制表页面
	public Board makeTable(Board board) {
		
		//查询孔号信息
		PrimerProduct primerProduct = new PrimerProduct();
		List<BoardHole> boardHoles = boardHoleRepository.findByBoard(board);
		for (BoardHole boardHole : boardHoles) {
			//根据id去查询生产编号，后续对自动根据 public Board findByBoardNo(String boardNo); 查出：Board，BoardHole和primerProduct
			primerProduct = new PrimerProduct();
			primerProduct = primerProductRepository.findOne(boardHole.getId());
			if (primerProduct != null){
				boardHole.setPrimerProduct(primerProduct);
			}
		}
		
		if(boardHoles!=null){
			board.setBoardHoles(boardHoles);
		}
		
		return board;
	}
	
    //增加新空
	public Board addNewHole(Board board) {
		String boardType = board.getType();//板类型:0-横排 1-竖排
		
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
		if ("0".equals(boardType)) {
			
			for(int i=0 ;i<holeList.size();i++){
				String hole = (String)holeList.get(i);
				for (int j=1;j<13;j++){
					System.out.println("==========板孔初始化 横版="+hole+j);
					holeNoList.add(hole+j);
				}
			}

		} else {

			for (int j=1;j<13;j++){
				for(int i=0 ;i<holeList.size();i++){
					String hole = (String)holeList.get(i);
					System.out.println("==========板孔初始化 竖版="+hole+j);
					holeNoList.add(hole+j);
				}
			}
		}
		
		//数据库的孔号和标准模板两个list进行倒序，如果不存在倒序增加
		
		for (BoardHole boardHole : board.getBoardHoles()) {
			
		}
		
		return board;
	}
	
	
	//提交制表信息
    @Transactional(readOnly = false)
	public String submitBoard(Board board) {
		
		//组织操作记录表信息
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList();
		board.setCreateUser(123);//后续需要调整到从session取值
		board.setCreateTime(new Date());
		
		List<BoardHole> boardHoles =  board.getBoardHoles();
		BoardHole boardHole = null;
		for (int i = boardHoles.size() - 1; i >= 0; i--) {
			
			boardHole = (BoardHole)boardHoles.get(i);
			
			boardHole.setCreateUser(123L);//后续需要调整到从session取值
			boardHole.setCreateTime(new Date());
			boardHole.setBoard(board);
			
			//如果没填生产编号id，就移除
			if (boardHole.getPrimerProduct() == null || boardHole.getPrimerProduct().getId() == null){
				//先手动删除孔号信息
				if(boardHole.getId()!=null){
					boardHoleRepository.delete(boardHole);
				}
				//然后移除
				boardHoles.remove(i);
			}else{
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(boardHole.getPrimerProduct());
				primerProductOperation.setType(PrimerOperationType.synthesisPrepare);
				primerProductOperation.setTypeDesc("合成准备");
				primerProductOperation.setUserCode("123");//后续从session取得
				primerProductOperation.setUserName("张三");//后续从session取得
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setBackTimes(0);
				primerProductOperation.setFailReason("成功了");
				primerProductOperations.add(primerProductOperation);
				
				//更新primer_product的操作类型和板号
				
				PrimerProduct primerProduct = primerProductRepository.findOne(boardHole.getPrimerProduct().getId());
				if( primerProduct != null ){
					primerProduct.setBoardNo(board.getBoardNo());
					primerProduct.setOperationType(PrimerOperationType.synthesisPrepare);
					primerProductRepository.save(primerProduct);
				}
				
			}
		}
		
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
	public String submitSynthesis(Board board, String modiFlag, String failReason) {
    	
		BoardHole boardHole = null;
		List<BoardHole> boardHoles = board.getBoardHoles();
		for (int i = board.getBoardHoles().size() - 1; i >= 0; i--) {
			boardHole = (BoardHole) board.getBoardHoles().get(i);
			String selectFlag = boardHole.getPrimerProduct().getSelectFlag();
    		
			if ("1".equals(selectFlag)) { // Synthesis success
				
				if ("1".equals(modiFlag)){ //需要修饰，到修饰状态
					boardHole.getPrimerProduct().setOperationType(PrimerOperationType.modification);
				} else {//不需要修饰，到氨解状态
					boardHole.getPrimerProduct().setOperationType(PrimerOperationType.ammonia);
				}

			} else if ("2".equals(selectFlag)) { // Synthesis fail

				boardHole.getPrimerProduct().setOperationType(PrimerOperationType.synthesisFailure);//回到合成准备阶段
				boardHole.getPrimerProduct().setBoardNo("");//清空板号
				boardHole.getPrimerProduct().setBackTimes(boardHole.getPrimerProduct().getBackTimes()+1);//循环重回次数+1
                
				//删除孔信息
				boardHoleRepository.delete(boardHole);
				boardHoles.remove(i);
				
				
				//更新操作信息--------待和客户讨论后添加
				//primer_product_operation
				//failReason
				
				
			}
			
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
	public EntityReply<File> exportPimerProduct(String boardNo, Invocation inv) throws IOException {
		
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
		String fileName = boardNo + "_shangjibiao.txt";
		FileOutputStream   fos = new FileOutputStream(p.getProperty("sjbPath")+fileName);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter     bw  = new BufferedWriter(osw);
		bw.write(tableContext);
		bw.newLine();
		bw.close();
		
		File file = new File(p.getProperty("sjbPath"), fileName); 
		
		return Replys.with(file).as(Raw.class).downloadFileName(fileName);
    }
}
