package org.one.gene.web.synthesis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sinosoft.one.data.jade.parsers.util.StringUtil;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Post;

import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;


import org.apache.commons.lang.time.DateUtils;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Path
public class SynthesisController {

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
    
    
    /**
     * 进入首页面
     * 
     * */
    public String first(){
    	System.out.println("===========first.jsp================");
    	Order order = orderRepository.findOne(1l);
    	return "first";
    }
    
    
    /**
     * 进入安排合成查询页面
     * 
     * */
    @Get("preQuery")
    public String preQuery(){
    	System.out.println("============preQuery===============");
    	return "synthesisForm";
    }
    
    
    /**
     * 安排合成查询
     * 
     * */
    public String synthesisQuery(@Param("customer_code") String customer_code, 
					    		 @Param("tbn1") String tbn1,
					    		 @Param("tbn2") String tbn2,
					    		 @Param("modiFlag") String modiFlag,
					    		 @Param("purifytype") String purifytype,
					    		 Invocation inv){
    	String modiFlagStr = "0";
    	if(modiFlag!=null && modiFlag.equals("")){
    		modiFlagStr = "1";
    	}

    	
    	 List<PrimerProduct> primerProducts = primerProductRepository.selectPrimerProduct(customer_code, purifytype, tbn1, tbn2);
    	 //查询primer_product_value
         for (PrimerProduct primerProduct : primerProducts) {
        	 List<PrimerProductValue> primerProductValues = primerProductValueRepository.selectValueByPrimerProductId(primerProduct.getId());
        	 for (PrimerProductValue ppv : primerProductValues) {
        		 if(ppv.getType().equals("ODTOTAL")){
        			 primerProduct.setOdTotal(ppv.getValue());
        		 }else if(ppv.getType().equals("ODTB")){
        			 primerProduct.setOdTB(ppv.getValue());
        		 }else if(ppv.getType().equals("NMOLTOTAL")){
        			 primerProduct.setNmolTotal(ppv.getValue());
        		 }else if(ppv.getType().equals("NMOLTB")){
        			 primerProduct.setNmolTB(ppv.getValue());
        		 }else if(ppv.getType().equals("TBN")){
        			 primerProduct.setTbn(ppv.getValue());
        		 }
        	 }
             //翻译操作类型
        	 String operationType = primerProduct.getOperationType();
        	 if (operationType.equals("ListImport")){
        		  primerProduct.setOperationTypeDesc("订单");
        	  }else if(operationType.equals("MakeTable")){
        		  primerProduct.setOperationTypeDesc("制表");
        	  }else if(operationType.equals("Synthesis")){
        		  primerProduct.setOperationTypeDesc("合成");
        	  }else if(operationType.equals("Label")){
        		  primerProduct.setOperationTypeDesc("贴标签");
        	  }else if(operationType.equals("Modification")){
        		  primerProduct.setOperationTypeDesc("修饰");
        	  }else if(operationType.equals("Ammonia")){
        		  primerProduct.setOperationTypeDesc("氨解");
        	  }else if(operationType.equals("Purify")){
        		  primerProduct.setOperationTypeDesc("纯化");
        	  }else if(operationType.equals("Measure")){
        		  primerProduct.setOperationTypeDesc("测值");
        	  }else if(operationType.equals("Pack")){
        		  primerProduct.setOperationTypeDesc("分装");
        	  }else if(operationType.equals("Bake")){
        		  primerProduct.setOperationTypeDesc("烘干");
        	  }else if(operationType.equals("Detect")){
        		  primerProduct.setOperationTypeDesc("检测");
        	  }else if(operationType.equals("Delivery")){
        		  primerProduct.setOperationTypeDesc("发货");
        	  }else if(operationType.equals("Back")){
        		  primerProduct.setOperationTypeDesc("召回");
        	  }
         }
         
    	 inv.addModel("primerProducts", primerProducts);
    	
    	return "synthesisList";
    }
    
    @Post("makeTable")
    public String makeTable(@Param("productNoValue") String[] productNoValues,
    		                @Param("boardNo") String boardNo,
    		                Invocation inv) {
    	
    	System.out.println("============板号="+boardNo);
        
        List<PrimerProduct> primerProducts = new ArrayList();//存前台选择的生产数据信息
        PrimerProduct primerProduct = new PrimerProduct();
		for (String product_no : productNoValues) {
			
			if (!"".equals(product_no) && !"0".equals(product_no)) {
				
				primerProduct = new PrimerProduct();
				primerProduct.setProductNo(product_no);
				primerProducts.add(primerProduct);
			}
		}
        
        //查询板号信息
		Board board = boardRepository.findByBoardNo(boardNo);
        
		if (board != null){
			
			//查询孔号信息
			PrimerProduct primerProductData = new PrimerProduct();
			
			for (BoardHole boardHole : board.getBoardHoles()) {
				//根据id去查询生产编号，后续对自动根据 public Board findByBoardNo(String boardNo); 查出：Board，BoardHole和primerProduct
				primerProductData = new PrimerProduct();
				primerProductData = primerProductRepository.findOne(boardHole.getId());
				if (primerProductData!=null){
					inv.addModel(boardHole.getHoleNo(), primerProductData.getProductNo());
				}
			}
			
			inv.addModel("board", board);
		}
        inv.addModel("primerProducts", primerProducts);
        
        return "makeTable";
    }

    
    
    @Post("submitBoard")
    public void submitBoard(@Param("boardNo") String boardNo, 
    		                @Param("boardType") Boolean boardType, 
    		                @Param("mapa") Map<String, String> mapa,
    		                Invocation inv) {
    	
    	
    	System.out.println("============制作合成板 提交 ，板号="+boardNo);
    	System.out.println("============制作合成板 提交 ，排版="+boardType);
    	System.out.println("============页面生产编号个数="+mapa.size());
    	
    	//先查是否有
    	Board board = boardRepository.findByBoardNo(boardNo);
		if (board == null) {
			board = new Board();
			board.setBoardNo(boardNo);
			board.setBoardType(boardType);
			board.setType("0");
			board.setCreateUser(123);
			board.setCreateTime(new Date());
		}else{
			board.setBoardType(boardType);
			board.setType("0");
			board.setCreateUser(123);
			board.setCreateTime(new Date());
		}
    			
        //查询孔号信息
        List<BoardHole> boardHoles = new ArrayList();
        BoardHole boardHole = new BoardHole();
        
		for (String key : mapa.keySet()) {
			 System.out.println("key= " + key + " and value= " + mapa.get(key));
			 
			 if (!StringUtil.isEmpty(mapa.get(key))) {
				 
				 PrimerProduct primerProduct = primerProductRepository.findByProductNo(mapa.get(key)+"");
				 
				 boardHole = new BoardHole();
				 
				 boardHole.setBoard(board);
				 boardHole.setHoleNo(key);
				 boardHole.setPrimerProduct(primerProduct);
				 boardHole.setCreateUser(123L);
				 boardHole.setCreateTime(new Date());
				 
				 boardHoles.add(boardHole);
				 
//				 boardHoleRepository.delete(boardHole);
			 } 
		}
		
		board.setBoardHoles(boardHoles);
		
//		boardRepository.delete(board);
		
		boardRepository.save(board);
//		boardHoleRepository.save(boardHoles);
    	
//    	return Replys.with(boardNo).as(Json.class);void

    }
    
    
    
    
    
    @Post("queryBoard")
    public Reply queryBoard(@Param("boardNo") String boardNo,Invocation inv) {

    	System.out.println("============boardNo="+boardNo);
    	

    	return Replys.with(boardNo).as(Json.class);

    }
    
    @Post("upload")
    public void upload(@Param("customerCode") String customerCode, @Param("file") MultipartFile[] files, Invocation inv) {

        for (MultipartFile multipartFile : files) {

        }
    }

}
