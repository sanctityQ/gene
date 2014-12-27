package org.one.gene.web.synthesis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;


import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Post;

import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.instruction.reply.EntityReply;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;


import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.service.SynthesisService;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.web.order.PrimerProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    
    @Autowired
    private PrimerProductOperationRepository primerProductOperationRepository;
    
    @Autowired
    private SynthesisService synthesisService;
    
    
    /**
     * 打开录入失败原因页面
     * 
     * */
    public String failReason(){

    	return "failReason";
    }

    /**
     * 进入首页面
     * 
     * */
    public String first(){

    	return "first";
    }
    
    /**
     * 进入制表查询页面
     * 
     * */
    @Get("preMakeTableQuery")
    public String preMakeTableQuery(){
    	
    	return "makeTableQuery";
    }
    
    
    /**
     * 制表查询
     * */
    public String makeTableQuery(@Param("customer_code") String customer_code, 
					    		 @Param("tbn1") String tbn1,
					    		 @Param("tbn2") String tbn2,
					    		 @Param("modiFlag") String modiFlag,
					    		 @Param("purifytype") String purifytype,
					    		 @Param("pageNo") Integer pageNo,
			                     @Param("pageSize") Integer pageSize,
					    		 Invocation inv){
    	
    	
    	modiFlag = modiFlag==null?"0":modiFlag;
		
        if(pageNo == null){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 2;
        }
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        
        Page<PrimerProduct> primerProductPage = synthesisService.makeTableQuery(customer_code, modiFlag, tbn1, tbn2, purifytype, pageable);
    	inv.addModel("page", primerProductPage);
    	inv.addModel("preRequestPath", inv.getRequestPath().getUri());
    	inv.addModel("pageSize", pageSize);
    	
    	inv.addModel("customer_code", customer_code);
    	inv.addModel("tbn1", tbn1);
    	inv.addModel("tbn2", tbn2);
    	inv.addModel("modiFlag", modiFlag);
    	inv.addModel("purifytype", purifytype);
    	
    	return "makeTableList";
    }
    
    /**
     * 到制表页面
     * */
    @Post("makeTableEdit")
    public String makeTableEdit(@Param("primerProductList") PrimerProductList primerProductList,
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
			
			synthesisService.makeTable(board);
			
			inv.addModel("board", board);
			reStr = "makeTableEditOld";
			
		}else{
			System.out.println("============新增板号="+boardNo);
			
			reStr = "makeTableEdit";
		}
		
        inv.addModel("primerProductPages", primerProducts);
        
        return reStr;
    }

    
    /**
     * 提交板信息
     * */
    @Post("submitBoard")
    public String submitBoard(Board board, Invocation inv) {
    	
    	synthesisService.submitBoard(board);
    	
    	inv.addModel("message", "制表成功！");
    	
    	return "returnMessage";
    }
    
    
    
    
    
    @Post("queryBoard")
    public Reply queryBoard(@Param("boardNo") String boardNo,Invocation inv) {

    	System.out.println("============boardNo="+boardNo);
    	

    	return Replys.with(boardNo).as(Json.class);

    }
    
    /**
     * 进入安排合成查询页面
     * 
     * */
    @Get("preSynthesisQuery")
    public String preSynthesisQuery(){
    	
    	return "synthesisQuery";
    }

    /**
     * 安排合成查询
     * */
    public String synthesisQuery(@Param("boardNo") String boardNo, Invocation inv){
    	
    	List<Board> boards = synthesisService.synthesisQuery( boardNo );
    	 inv.addModel("boards", boards);
    	return "synthesisList";
    }
    
    /**
     * 到制表页面
     * */
    @Get("synthesisEdit/{id}")
    public String synthesisEdit(@Param("id") Long id, Invocation inv) {
        //查询板号信息
		Board board = boardRepository.findOne(id);
		inv.addModel("board", board);
        return "synthesisEdit";
    }
    
    /**
     * 提交合成信息
     * */
    @Post("submitSynthesis")
	public String submitSynthesis(Board board,
			@Param("modiFlag") String modiFlag,
			@Param("failReason") String failReason, Invocation inv) {
    	
    	
    	synthesisService.submitSynthesis(board, modiFlag, failReason);
    	
    	inv.addModel("message", "完成合成提交！");
    	
    	return "returnMessage";
    }
    
    /**
     * 进入导出上机表页面
     * */
    public String preExportPrimerProduct(){

    	return "exportPrimerProduct";
    }
    
    /**
     * 导出上机表文件
     * */
    public EntityReply<File> exportPrimerProduct(@Param("boardNo") String boardNo, Invocation inv){
    	EntityReply<File> fileStr = null;
    	try {
    		fileStr = synthesisService.exportPimerProduct(boardNo, inv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return fileStr;
    }
    
    /**
     * 查看生产数据信息
     * */
    @Get("viewPrimerProduct/{primerProductId}")
    public String viewPrimerProduct(@Param("primerProductId") Long primerProductId, Invocation inv){
    	
    	PrimerProduct primerProduct = primerProductRepository.findOne(primerProductId);
    	
    	inv.addModel("primerProduct", primerProduct);
    	return "viewPrimerProduct";
    }
    
}
