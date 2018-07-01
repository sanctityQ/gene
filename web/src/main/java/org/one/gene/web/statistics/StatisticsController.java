package org.one.gene.web.statistics;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Company;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.ProductMolecular;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.DeliveryService;
import org.one.gene.domain.service.PrintService;
import org.one.gene.domain.service.StatisticsService;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.repository.ProductMolecularRepository;
import org.one.gene.web.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.EntityReply;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;

@Path
public class StatisticsController {

    @Autowired
	private StatisticsService statisticsService;
    
	@Autowired
	private ProductMolecularRepository productMolecularRepository;
	
    /**
     * 进入   出库统计   查询页面
     * */
    @Get("chuKuTongJi")
    public String chuKuTongJi(){
    	return "chuKuTongJi";
    }
    
    /**
     * 导出   出库统计   文件
     * */
    @Post("exportChuKuTongJi")
	public void exportChuKuTongJi(@Param("statisticsInfojson") String statisticsInfojson, Invocation inv)
			throws IOException {
    	
    	statisticsInfojson = new String(statisticsInfojson.getBytes("iso-8859-1"), "utf-8");;  
    	List<StatisticsInfo> statisticsInfos = JSON.parseArray(statisticsInfojson,StatisticsInfo.class);
    	
    	StatisticsInfo statisticsInfo = statisticsInfos.get(0);
		
		statisticsService.exportChuKuTongJi(statisticsInfo, inv);
    }
    
    
    /**
     * 进入  对账单   查询页面
     * */
    @Get("duiZhangDan")
    public String duiZhangDan(){
    	return "duiZhangDan";
    }
    
    /**
     * 导出  对账单 文件
     * */
    @Post("exportDuiZhangDan")
	public void exportDuiZhangDan(@Param("statisticsInfojson") String statisticsInfojson, Invocation inv)
			throws IOException {
    	
    	statisticsInfojson = new String(statisticsInfojson.getBytes("iso-8859-1"), "utf-8");;  
    	List<StatisticsInfo> statisticsInfos = JSON.parseArray(statisticsInfojson,StatisticsInfo.class);
    	
    	StatisticsInfo statisticsInfo = statisticsInfos.get(0);
		
		statisticsService.exportDuiZhangDan(statisticsInfo, inv);
    }
    
    /**
     * 进入引物进度表查询页面
     * */
    @Get("yinWuJinDuBiao")
    public String yinWuJinDuBiao(){
    	return "yinWuJinDuBiao";
    }
    
    /**
     * 导出引物进度表文件
     * */
    @Post("exportYinWuJinDuBiao")
	public void exportYinWuJinDuBiao(@Param("statisticsInfojson") String statisticsInfojson, Invocation inv)
			throws IOException {
    	
    	statisticsInfojson = new String(statisticsInfojson.getBytes("iso-8859-1"), "utf-8");;  
    	List<StatisticsInfo> statisticsInfos = JSON.parseArray(statisticsInfojson,StatisticsInfo.class);
    	
    	StatisticsInfo statisticsInfo = statisticsInfos.get(0);
		
		try {
			statisticsService.exportYinWuJinDuBiao(statisticsInfo, inv);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    /**
     * 进入修饰进度表查询页面
     * */
    @Get("xiuShiJinDuBiao")
    public String xiuShiJinDuBiao(Invocation inv){
    	List<ProductMolecular> modiFives = productMolecularRepository.findByProductCategories("modiFiveType");
    	List<ProductMolecular> modiThrees = productMolecularRepository.findByProductCategories("modiThreeType");
    	List<ProductMolecular> modiMids = productMolecularRepository.findByProductCategories("modiMidType");
    	List<ProductMolecular> modiSpes = productMolecularRepository.findByProductCategories("modiSpeType");
		inv.addModel("modiFives", modiFives);
		inv.addModel("modiThrees", modiThrees);
		inv.addModel("modiMids", modiMids);
		inv.addModel("modiSpes", modiSpes);
    	return "xiuShiJinDuBiao";
    }
    
    /**
     * 导出修饰进度表文件
     * */
    @Post("exportXiuShiJinDuBiao")
	public void exportXiuShiJinDuBiao(@Param("statisticsInfojson") String statisticsInfojson, Invocation inv)
			throws IOException {
    	
    	statisticsInfojson = new String(statisticsInfojson.getBytes("iso-8859-1"), "utf-8");;  
    	List<StatisticsInfo> statisticsInfos = JSON.parseArray(statisticsInfojson,StatisticsInfo.class);
    	
    	StatisticsInfo statisticsInfo = statisticsInfos.get(0);
		
		try {
			statisticsService.exportXiuShiJinDuBiao(statisticsInfo, inv);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    /**
     * 进入HPLC纯化进度表查询页面
     * */
    @Get("hplcChunHuaBiao")
    public String hplcChunHuaBiao(){
    	return "hplcChunHuaBiao";
    }
    
    /**
     * 导出HPLC纯化进度表文件
     * @throws Exception 
     * @throws IOException 
     * */
    @Post("exHplcChunHuaBiao")
    public void exHplcChunHuaBiao(@Param("boardNo") String  boardNo, Invocation inv) throws Exception {
    	
    	statisticsService.exHplcChunHuaBiao(boardNo, inv);
    }
    
    /**
     * 进入工作量统计查询页面
     * */
    @Get("gzlTongJi")
    public String gzlTongJi(){
    	return "gzlTongJi";
    }
    
    /**
     * 导出工作量统计文件
     * */
    @Post("exportGZLTongJi")
	public void exportGZLTongJi(@Param("statisticsInfojson") String statisticsInfojson, Invocation inv)
			throws IOException {
    	
    	statisticsInfojson = new String(statisticsInfojson.getBytes("iso-8859-1"), "utf-8");;  
    	List<StatisticsInfo> statisticsInfos = JSON.parseArray(statisticsInfojson,StatisticsInfo.class);
    	
    	StatisticsInfo statisticsInfo = statisticsInfos.get(0);
		
		statisticsService.exportGZLTongJi(statisticsInfo, inv);
    }
    
}
