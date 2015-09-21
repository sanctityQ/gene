package org.one.gene.web.statistics;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
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
    
    /**
     * 进入出库统计查询页面
     * */
    @Get("chuKuTongJi")
    public String chuKuTongJi(){
    	return "chuKuTongJi";
    }
    
    /**
     * 导出出库统计文件
     * */
    @Post("exportChuKuTongJi")
	public void exportChuKuTongJi(@Param("statisticsInfojson") String statisticsInfojson, Invocation inv)
			throws IOException {
    	
    	statisticsInfojson = new String(statisticsInfojson.getBytes("iso-8859-1"), "utf-8");;  
    	List<StatisticsInfo> statisticsInfos = JSON.parseArray(statisticsInfojson,StatisticsInfo.class);
    	
    	StatisticsInfo statisticsInfo = statisticsInfos.get(0);
		
		statisticsService.exportChuKuTongJi(statisticsInfo, inv);
    }
    
    
}
