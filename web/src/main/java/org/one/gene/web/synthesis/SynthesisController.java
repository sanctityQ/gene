package org.one.gene.web.synthesis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.alibaba.fastjson.JSONObject;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Post;

import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.service.SynthesisService;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
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
     * 进入合成结果查询页面
     * 
     * */
    @Get("synthesisResults")
    public String synthesisResults(){
    	
    	return "synthesisResults";
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
    	
        if(pageNo == null || pageNo ==0){
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
	public Reply submitSynthesis(
			@Param("boardHoles") List<BoardHole> boardHoles,
			@Param("boardNo") String boardNo,
			@Param("failReason") String failReason, Invocation inv) {
    	
    	synthesisService.submitSynthesis(boardNo, boardHoles);
    	
    	return Replys.with("{\"success\":true,\"mesg\":\"success\"}").as(Json.class);
    }
    
    /**
     * 进入导出上机表页面
     * */
    public String machineTable(){
    	return "machineTable";
    }
    
    /**
     * 导出上机表文件
     * @throws IOException 
     * */
    @Post("exportMachineTable")
    public void exportMachineTable(@Param("boardNo") String boardNo, Invocation inv) throws IOException{
    	synthesisService.exportMachineTable(boardNo, inv);
    }

    /**
     * 进入导出分装表页面
     * */
    public String packTable(){
    	return "packTable";
    }
    
    /**
     * 导出分装表文件
     * @throws IOException 
     * */
    @Post("exportPackTable")
    public void exportPackTable(@Param("boardNo") String boardNo, Invocation inv) throws IOException{
    	synthesisService.exportPackTable(boardNo, inv);
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
    
    /**
     * 模糊查询板信息
     * */
    @Post("vagueSeachBoard")
	public Reply vagueSeachBoard(@Param("boardNo") String boardNo,
			@Param("operationType") PrimerStatusType operationType,
			Invocation inv) {
		if (!StringUtils.isBlank(boardNo)) {
			boardNo = "%" + boardNo + "%";
        }
		List<Board> boards = boardRepository.vagueSeachBoard(boardNo,operationType.toString());
    	return Replys.with(boards).as(Json.class);
    }
    
    /**
     * 进入修饰查询页面
     * 
     * */
    @Get("preDecorateQuery")
    public String preDecorateQuery(){
    	
    	return "decorate";
    }
    
    /**
     * 查询结果列表
     * */
    @Post("resultsSelectQuery")
	public Reply resultsSelectQuery(@Param("boardNo") String boardNo,
			@Param("productNo") String productNo,
			@Param("operationType") PrimerStatusType operationType,
			@Param("modiFiveType") String modiFiveType,
			@Param("modiThreeType") String modiThreeType,
			@Param("modiMidType") String modiMidType,
			@Param("modiSpeType") String modiSpeType,
			@Param("purifyType") String purifyType,
			@Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, Invocation inv) {
    	
        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 10;
        }
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        
        Page<PrimerProduct> primerProductPage = synthesisService.resultsSelectQuery(boardNo,productNo,operationType, modiFiveType, modiThreeType, modiMidType, modiSpeType,purifyType, pageable);
    	
    	return Replys.with(primerProductPage).as(Json.class);
    }
    
    /**
     * 提交修饰，检测等在结果页面选择成功失败的数据
     * */
    @Post("resultsSelectSubmit")
	public Reply resultsSelectSubmit(
			@Param("primerProducts") List<PrimerProduct> primerProducts,
			@Param("successFlag") String successFlag,
			@Param("operationType") PrimerStatusType operationType,
			@Param("failReason") String failReason, Invocation inv) {
    	
            synthesisService.resultsSelectSubmit(primerProducts, successFlag, operationType, failReason);
    	
    	return Replys.with("{\"success\":true,\"mesg\":\"success\"}").as(Json.class);
    }
    
    
    /**
     * 进入氨解结果查询页面
     * 
     * */
    @Get("ammoniaResults")
    public String ammoniaResults(){
    	
    	return "ammoniaResults";
    }
    
    /**
     * 到板编辑页面
     * boardNo 板号
     * operationType 类型
     * @throws IOException 
     * */
    @Post("boardEdit")
	public Reply boardEdit(
			@Param("operationType") PrimerStatusType operationType,
			@Param("boardNo") String boardNo, Invocation inv)
			throws IOException {
    	
    	String jsonStr = synthesisService.boardEdit(boardNo, operationType, null, inv);
        
    	return Replys.with(jsonStr).as(Json.class);
    }
    
    /**
     * 提交编辑板信息
     * */
    @Post("submitBoardEdit")
	public Reply submitBoardEdit(@Param("operationType") PrimerStatusType operationType,
			                     @Param("boardHoles") List<BoardHole> boardHoles,
			                     @Param("boardNo") String boardNo,
			                     Invocation inv) {
    	
    	synthesisService.submitBoardEdit(boardNo, boardHoles, operationType);
    	
    	return Replys.with("{\"success\":true,\"mesg\":\"success\"}").as(Json.class);
    }
    
    
    /**
     * 进入纯化结果查询页面
     * 
     * */
    @Get("purifyResults")
    public String purifyResults(){
    	
    	return "purifyResults";
    }
    /**
     * 进入分装结果查询页面
     * 
     * */
    @Get("packResults")
    public String packResults(){
    	
    	return "packResults";
    }  
    /**
     * 进入烘干结果查询页面
     * 
     * */
    @Get("bakeResults")
    public String bakeResults(){
    	
    	return "bakeResults";
    }  
    /**
     * 进入测值结果查询页面
     * 
     * */
    @Get("measureResults")
    public String measureResults(Invocation inv){
    	
    	String templateFilePath= File.separator+"gene"+File.separator+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+"measure.xls";
    	System.out.println("进入测值结果查询页面，下载模板文件的路径templateFilePath="+templateFilePath);
    	inv.addModel("templateFilePath",templateFilePath);
    	return "measureResults";
    }    
    
    //上传测值文件
    @Post("uploadMeasure")
	public String uploadMeasure(@Param("boardNo") String boardNo,
			@Param("operationType") PrimerStatusType operationType,
			@Param("file") MultipartFile file, Invocation inv) throws Exception {

    	String jsonStr = synthesisService.boardEdit(boardNo, operationType, file, inv);
     
		System.out.println(JSONObject.toJSONString(jsonStr));
		
		inv.addModel("boardNo", boardNo);
		inv.addModel("measuredata", JSONObject.toJSONString(jsonStr));
		
    	return "measureResultsBoard";
    }
   
    /**
     * 进入检测上传文件页面
     * 
     * */
    @Get("detectResults")
    public String detectResults(Invocation inv){
    	return "detectResults";
    }
    
    //上传检测文件
    @Post("uploadDetect")
	public String uploadDetect(@Param("file") MultipartFile file, Invocation inv) throws Exception {

    	List<PrimerProduct> primerProducts = synthesisService.uploadDetect(file, inv);
     
    	inv.getResponse().setContentType("text/html");
		inv.addModel("total", primerProducts.size());
		System.out.println("上传检测文件  返回json="+JSONObject.toJSONString(primerProducts));
		inv.addModel("reSultdata", JSONObject.toJSONString(primerProducts));
    	return "detectResultsBoard";
    }
    
    /**
     * 进入检测结果页面
     * 
     * */
    @Get("detectResultsBoard")
    public String detectResultsBoard(Invocation inv){
    	return "detectResultsBoard";
    }
    
    
    /**
     * 模糊查询板信息或生产信息
     * */
    @Post("vagueSeachBoardNoOrProductNo")
	public Reply vagueSeachBoardNoOrProductNo(@Param("boardNo") String boardNo,
			@Param("operationType") PrimerStatusType operationType,
			Invocation inv) {
		if (!StringUtils.isBlank(boardNo)) {
			boardNo = "%" + boardNo + "%";
        }
		List<Board> boards = boardRepository.vagueSeachBoard(boardNo,operationType.toString());
		for(Board board:boards){
			board.setType("1");//板
		}
		
		if (!StringUtils.isBlank(boardNo)) {
			List<PrimerProduct> primerProducts = primerProductRepository.vagueSeachPrimerProduct(boardNo, boardNo);
			for(PrimerProduct pp:primerProducts){
				Board board = new Board();
				
				board.setType("2");//生产编号
				if (!"".equals(pp.getProductNo())) {
					board.setBoardNo(pp.getProductNo());
				}else{
					board.setBoardNo(pp.getOutProductNo());
				}
				
				boards.add(board);
				
			}
		}
		
    	return Replys.with(boards).as(Json.class);
    }
    
    
    
}
