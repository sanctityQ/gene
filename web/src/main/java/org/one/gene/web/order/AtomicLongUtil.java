package org.one.gene.web.order;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author ThinkPad User
 *
 */
@Component
public class AtomicLongUtil {

	private AtomicLong orderNextNum = new AtomicLong(0);
	private AtomicLong productNextNum = new AtomicLong(0);
	private ConcurrentMap<String, AtomicLong> orderMap = new ConcurrentHashMap<String, AtomicLong>();
	private ConcurrentMap<String, AtomicLong> productMap = new ConcurrentHashMap<String, AtomicLong>();
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private PrimerProductRepository primerProductRepository;
	
	 /**
	  * 获取当前时间年月日
	  * @return
	  */
	 public String getFormatDate(){
		 
		 // 用于时间的格式化
		 SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
		 // 用于时间的处理
		 Calendar calendar = Calendar.getInstance();
		 // 格式化新的时间
		 return sFormat.format(calendar.getTime());
	 }
	 
	 /**
	  * 获取订单号
	  * @return
	  */
	 public String getOrderSerialNo(){
		 
		 //获取数据库当前最后一条的单号
		 Order order = orderRepository.getLastOrder();
		 String orderNo = "";
		 String currentMaxNo ="";
		 if(order!=null){
			 orderNo = order.getOrderNo();
			//截取最大流水号
			 currentMaxNo = orderNo.substring(orderNo.length()-4, orderNo.length());
		 }
		 
		 //如果查询到最大号，将当前最大号更新到orderNextNum
		 if(!"".equals(currentMaxNo)){
			 updateMax(orderNextNum,Integer.parseInt(currentMaxNo));
			 //获取当前最大单号+1，继续编号
			 orderNextNum.getAndIncrement();
			 orderMap.put("orderSerial", orderNextNum);
		 }else{
			 orderNextNum.getAndIncrement();
			 orderMap.put("orderSerial", orderNextNum);
		 }
		 long sn = orderMap.get("orderSerial").get();
		 if(sn+1 >= 10000){
			 updateMin(orderNextNum,0);
			 orderMap.put("orderSerial", orderNextNum);
		 }
		 return orderNoFormat(orderMap.get("orderSerial").get());
	 }
	 
	 /**
	  * 获取生产编号
	  * @return
	  */
	 public String getProductSerialNo(String prefix){
		 
		//获取数据库当前最后一条的单号
		 PrimerProduct product = primerProductRepository.getLastProduct();
		 String productNo = "";
		 String currentMaxNo = "";
		 if(product!=null){
			 productNo = product.getProductNo();
			 //截取最大流水号
		     currentMaxNo = productNo.substring(productNo.length()-4, productNo.length());
		 }
		 
		 //如果查询到最大号，将当前最大号更新到orderNextNum
		 if(!"".equals(currentMaxNo)){
			 updateMax(productNextNum,Integer.parseInt(currentMaxNo));
			 productNextNum.getAndIncrement();
			 productMap.put("productSerial", productNextNum);
		 }else{
			 productNextNum.getAndIncrement();
			 productMap.put("productSerial", productNextNum);
		 }
		 long sn = productMap.get("productSerial").get();
		 if(sn+1 >= 10000){
			 updateMin(productNextNum,0);
			 productMap.put("productSerial", productNextNum);
		 }
		 return productNoFormat(productMap.get("productSerial").get(),prefix);
	 }
	 
	 
	 public void updateMin(AtomicLong min, long value) {
	    while (true) {
	      long cur = min.get();
	      if (value >= cur) {
	        break;
	      }

	      if (min.compareAndSet(cur, value)) {
	        break;
	      }
	    }
	  }
	 public void updateMax(AtomicLong max, long value) {
	    while (true) {
	      long cur = max.get();
	      if (value <= cur) {
	        break;
	      }
	
	      if (max.compareAndSet(cur, value)) {
	        break;
	      }
	    }
	  }

	 /**
	  * 订单号生成
	  * @param sn
	  * @return
	  */
	 public String orderNoFormat(long sn){
		//取得序列号格式化字符串
		int count ="XXXX".length();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<count;i++){
			sb.append("0");
		}
		
		java.text.DecimalFormat df = new java.text.DecimalFormat(sb.toString());
		//当前的流水号
		String current_serial_number = df.format(sn);
		String dateString = this.getFormatDate();
		String current_number = dateString+current_serial_number;
		return current_number;
	 }
	 
	 /**
	  * 生产编号生成
	  * @param sn
	  * @return
	  */
	 public String productNoFormat(long sn,String prefix){
		//X+(年份最后1位) + MM-DD + 四位流水号
		//取得序列号格式化字符串
		int count ="XXXX".length();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<count;i++){
			sb.append("0");
		}
		
		java.text.DecimalFormat df = new java.text.DecimalFormat(sb.toString());
		//当前的流水号
		String current_serial_number = df.format(sn);
		if(prefix==null){prefix="";}
		String dateString = this.getFormatDate();
		String current_number = prefix+dateString.substring(3)+current_serial_number;
		return current_number;
	 }
	 
}
