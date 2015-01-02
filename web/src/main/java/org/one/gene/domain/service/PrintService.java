package org.one.gene.domain.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.one.gene.domain.entity.Order;
import org.one.gene.web.order.OrderInfoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PrintService {

	/**
	 * 出库单打印对象组织
	 * @param orderPage
	 * @param pageable
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public Page<OrderInfoList>  convertOutbound(Page<Order> orderPage,Pageable pageable) throws IllegalStateException, IOException {
		//生产编号（头尾）、碱基总数
		OrderInfoList orderInfo = new OrderInfoList();
		List<OrderInfoList> orderInfoList = new ArrayList<OrderInfoList>();
		
		for(Order order:orderPage.getContent()){
			orderInfo = getOutbound(order);
			orderInfoList.add(orderInfo);
		}
		
		Page<OrderInfoList> orderListPage = new PageImpl<OrderInfoList>(orderInfoList,pageable,orderPage.getSize());
		
        return orderListPage;
    }
	
	public OrderInfoList getOutbound(Order order){
		OrderInfoList orderInfo = new OrderInfoList();
		
		//组织订单列表对象
		orderInfo.setOrderNo(order.getOrderNo());
		orderInfo.setCustomerName(order.getCustomerName());
		//调整值获取
		orderInfo.setCustomerPhoneNm("13566959955");
		//业务员 调整值获取
		orderInfo.setHandlerCode("98834342");
		//收样日期
		orderInfo.setCreateTime(order.getCreateTime());
		//单据编号 调整值获取 自动生成10位流水号
		orderInfo.setMakingNo("0000000010");
		//制单人（当前系统操作人员） 调整值获取 根据登陆信息获取
		orderInfo.setOperatorCode("34343434");
		//联系人（客户管理中的联系人） 调整值获取
		orderInfo.setLinkName("张三");
		//制单日期
		orderInfo.setMakingDate(new Date());
		//商品编码 默认赋值
		orderInfo.setCommodityCode("06030008");
		//货物名称 默认赋值
		orderInfo.setCommodityName("DNA合成");
		
		return orderInfo;
	}
}
