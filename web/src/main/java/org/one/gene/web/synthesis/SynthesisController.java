package org.one.gene.web.synthesis;

import java.util.Date;
import java.util.List;


import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Post;

import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;


import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.web.order.PrimerProductList;
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

    	return "first";
    }
    
    
    /**
     * 进入安排合成查询页面
     * 
     * */
    @Get("preQuery")
    public String preQuery(){
    	
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
    	
    	String modiFlagStr = "";
    	//如果选择无修饰过滤，查询条件增加：
    	//( pp.`modi_five_type` is null and pp.`modi_three_type` is null and pp.`modi_mid_type` is null and pp.`modi_spe_type` is null)
    	//如果选择有修饰过滤，查询条件增加：
    	//( pp.`modi_five_type` is not null and pp.`modi_three_type` is not null and pp.`modi_mid_type` is not null and pp.`modi_spe_type` is not null)
		if (modiFlag != null && modiFlag.equals("")) {
    		modiFlagStr = "1";
    	}
    	
    	 List<PrimerProduct> primerProducts = primerProductRepository.selectPrimerProduct(customer_code, modiFlagStr, tbn1, tbn2, purifytype);
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
    public String makeTable(@Param("primerProductList") PrimerProductList primerProductList,
    		                @Param("boardNo") String boardNo,
    		                Invocation inv) {
    	
        List<PrimerProduct> primerProducts = primerProductList.getPrimerProducts();
		for (int i = primerProducts.size() - 1; i >= 0; i--) {
			//如果页面没有选择，则移除
			if (((PrimerProduct)primerProducts.get(i)).getSelectFlag() == null) {
				primerProducts.remove(i);
			}
		}
        
        //查询板号信息
		Board board = boardRepository.findByBoardNo(boardNo);
        
		String reStr = "";
		if (board != null) {
			
			System.out.println("============修改板号="+boardNo);
			
			//查询孔号信息
			PrimerProduct primerProductData = new PrimerProduct();
			List<BoardHole> boardHoles = boardHoleRepository.findByBoard(board);
			for (BoardHole boardHole : boardHoles) {
				//根据id去查询生产编号，后续对自动根据 public Board findByBoardNo(String boardNo); 查出：Board，BoardHole和primerProduct
				primerProductData = new PrimerProduct();
				primerProductData = primerProductRepository.findOne(boardHole.getId());
				if (primerProductData!=null){
					boardHole.setPrimerProduct(primerProductData);
				}
			}
			if(boardHoles!=null){
				board.setBoardHoles(boardHoles);
			}
			
			inv.addModel("board", board);
			reStr = "makeTableOld";
			
		}else{
			System.out.println("============新增板号="+boardNo);
			
			reStr = "makeTable";
		}
		
        inv.addModel("primerProductPages", primerProducts);
        
        return reStr;
    }

    
    
    @Post("submitBoard")
    public void submitBoard(Board board, Invocation inv) {
    	
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
			}
		}
		//保存
		boardRepository.save(board);
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
