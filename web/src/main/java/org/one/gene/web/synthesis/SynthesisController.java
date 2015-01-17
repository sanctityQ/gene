package org.one.gene.web.synthesis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;


import com.alibaba.fastjson.JSON;
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
import org.one.gene.domain.entity.Order;
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
     * 进入制板查询页面
     * 
     * */
    @Get("preMakeBoardQuery")
    public String preMakeBoardQuery(){
    	
    	return "productionData";
    }
    
    
    /**
     * 制板查询
     * */
    @Post("makeBoardQuery")
    public Reply makeBoardQuery(@Param("customercode") String customercode, 
					    		 @Param("tbn1") String tbn1,
					    		 @Param("tbn2") String tbn2,
					    		 @Param("modiFlag") String modiFlag,
					    		 @Param("purifytype") String purifytype,
					    		 @Param("pageNo") Integer pageNo,
			                     @Param("pageSize") Integer pageSize,
					    		 Invocation inv){
    	
        if(pageNo == null){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 10;
        }
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        
        Page<PrimerProduct> primerProductPage = synthesisService.makeBoardQuery(customercode, modiFlag, tbn1, tbn2, purifytype, pageable);
    	
    	return Replys.with(primerProductPage).as(Json.class);
    }
    
    /**
     * 到制板页面
     * flag 1 竖排；0 横排
     * boardNo 板号
     * productNoStr 已选择的生产编号
     * @throws IOException 
     * */
    @Post("makeBoardEdit")
	public Reply makeBoardEdit(@Param("flag") String flag,
			                   @Param("boardNo") String boardNo,
			                   @Param("productNoStr") String productNoStr, Invocation inv) throws IOException {
    	
    	String jsonStr = synthesisService.makeBoard(boardNo, flag, productNoStr, inv);
        
    	return Replys.with(jsonStr).as(Json.class);
    }

    
    /**
     * 提交板信息
     * */
    @Post("submitBoard")
	public Reply submitBoard(@Param("holeStr") String holeStr,
			                 @Param("boardNo") String boardNo,
			                 @Param("boardType") String boardType, Invocation inv) {
    	
    	synthesisService.submitBoard(holeStr, boardNo, boardType, inv);
    	
    	return Replys.with("{\"success\":true,\"mesg\":\"success\"}").as(Json.class);
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
			@Param("failReason") String failReason, Invocation inv) {
    	
    	
    	synthesisService.submitSynthesis(board, failReason);
    	
    	inv.addModel("message", "完成合成提交！");
    	
    	return "returnMessage";
    }
    
    /**
     * 进入导出上机表页面
     * */
    public String machineTable(){
    	return "machineTable";
    }
    
    /**
     * 导出上机表文件
     * */
    @Post("exportMachineTable")
    public EntityReply<File> exportMachineTable(@Param("boardNo") String boardNo, Invocation inv){
    	EntityReply<File> fileStr = null;
    	try {
    		fileStr = synthesisService.exportMachineTable(boardNo, inv);
		} catch (IOException e) {
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
