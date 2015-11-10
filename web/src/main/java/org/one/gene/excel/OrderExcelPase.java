package org.one.gene.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.CustomerPrice;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.Order.OrderType;
import org.one.gene.domain.service.PriceTool;
import org.one.gene.repository.CustomerPriceRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.utils.MapKeyComparator;
import org.one.gene.web.delivery.DeliveryInfo;
import org.one.gene.web.order.AtomicLongUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderExcelPase {

	@Autowired
    private ExcelResolver excelResolver;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private OrderCaculate orderCaculate;
    @Autowired
    private AtomicLongUtil atomicLongUtil;
    @Autowired
    private PriceTool priceTool;
	@Autowired
	private CustomerPriceRepository customerPriceRepository;
	
	/**
	 * 组织解析excel验证异常信息
	 * @param data
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public ArrayList<String> getExcelPaseErrors(String path,int ignoreRows, int sheetIndex) throws FileNotFoundException, IOException{
		
		String[][] date = excelResolver.getDataValide(path,ignoreRows,sheetIndex);
		
		ArrayList<String> Errors = new ArrayList<String>();

		for (int i = 0; i < date.length; i++) {
			StringBuffer message = new StringBuffer(100);
			if(!"".equals(date[i][3])){
				String result = orderCaculate.valiGeneOrder(date[i][3]);
				if(!"".equals(result)){
				  message.append("第"+(i+2)+"行第4列[序列]数据"+result+" \n");
				}
			}
			if("".equals(date[i][3])){
				message.append("第"+(i+2)+"行第4列[序列]数据不能为空! \n");
			}
			if (date[i].length < 8 || "".equals(date[i][8])) {
				message.append("第"+(i+2)+"行第9列[纯化方式]数据不能为空! \n");
			}
			if("".equals(date[i][4])&&"".equals(date[i][6])){
				message.append("第"+(i+2)+"行第5列与第7列[OD与nmol]数据不能同时为空! \n");
			}
			if(!"".equals(date[i][4])&&!"".equals(date[i][6])){
				message.append("第"+(i+2)+"行第5列与第7列[OD与nmol]数据不能同时存在! \n");
			}
			if(!"".equals(date[i][4])&&"".equals(date[i][5])){
				message.append("第"+(i+2)+"行第5列[nmol总量]不为空值时，第6列[nmol/tube]数据不能为空! \n");
			}
			if(!"".equals(date[i][6])&&"".equals(date[i][7])){
				message.append("第"+(i+2)+"行第7列[OD总量]不为空值时，第8列[OD/tube]数据不能为空! \n");
			}
			if(!"".equals(message.toString())){
			  Errors.add(message.toString());
			}
		}
		return Errors;
	}
	
	/**
	 * 获取外部订单号
	 * @param path
	 * @param sheetIndex
	 * @param rows
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public String getOutOrderNo(String path, int sheetIndex, int ignoreRows) throws FileNotFoundException, IOException {
		String outOrderNo = "";
		String[][] date = excelResolver.getDataValide(path,ignoreRows,sheetIndex);
		outOrderNo = date[0][0];
		
		return outOrderNo;
	}
	/**
	 * 解析订单中的列表信息
	 * @param path
	 * @param sheetIndex 第几个sheet页
	 * @param rows 忽略的行数或从第几行开始
	 */
	public ArrayList<Order> ReadExcel(String path, int sheetIndex, String rows,
			String prefix, List<CustomerPrice> customerPrices,
			Map<String, String> modiMidMap, Map<String, String> modiSpeMap) {
		
		// 获取Excel文件的第1个sheet的内容
		ArrayList<ArrayList<String>> lists = excelResolver.ReadExcel(path, sheetIndex,rows);
		ArrayList<Order>  orders = new ArrayList<Order>();
		Order order = new Order();
		boolean haveFirst = false;//通过第一个外部订单判断整个外部订单是不是全空
		OrderType orderUpType = Order.OrderType.nmol;;//订单类型
		String outOrderNoTemp = "";
		Map<String, Order> orderMap = new HashMap<String, Order>();
    	//输出单元格数据
		for(ArrayList<String> data : lists) {
			if(data==null){continue;}
			int index = 1;
			PrimerProduct primerProduct = new PrimerProduct();
			String outOrderNo = "";
			for(String v : data) {
				//给客户对象赋值
				switch(index){
				  case 1:
					if (!"".equals(v)) {
						outOrderNo = v;
						if (index == 1) {
							haveFirst = true;
						}
					}
					break;
				  case 2:
					  //如果Excel导入中有生产编号存储为外部生产编号，如果没有系统自动生成
					  if("".equals(v)){
						  primerProduct.setProductNo(atomicLongUtil.getProductSerialNo(prefix));
					  }else{
						  primerProduct.setProductNo(v);
					  }
				  	break;
				  case 3:
					  primerProduct.setPrimeName(v);
					break;
				  case 4:	
					  String geneOrder = orderCaculate.getGeneOrder(v);
					  primerProduct.setGeneOrderMidi(orderCaculate.getGeneOrderToUpper(v));
					  primerProduct.setGeneOrder(orderCaculate.getYWSeqValue(geneOrder));
					break;
				  case 5:	
					  if(!"".equals(v)){
					    primerProduct.setNmolTotal(new BigDecimal(v));
					  }
					break;
				  case 6:	
					  if(!"".equals(v)){
					    primerProduct.setNmolTB(new BigDecimal(v));
					  }
					break;
				  case 7:	
					  if(!"".equals(v)){
					    primerProduct.setOdTotal(new BigDecimal(v));
					    orderUpType = Order.OrderType.od;
					  }
					break;
				  case 8:	
					  if(!"".equals(v)){
					    primerProduct.setOdTB(new BigDecimal(v));
					  }
					break;
				  case 9:	
					  primerProduct.setPurifyType(v);
					break;
				  case 10:	
					  primerProduct.setModiFiveType(v);
					break;
				  case 11:	
					  primerProduct.setModiThreeType(v);
					break;
				  case 12:	
					  String modiMid = orderCaculate.getCountStr(orderCaculate.getModiType(primerProduct.getGeneOrderMidi(),modiMidMap));
					  primerProduct.setModiMidType(modiMid);
					break;
				  case 13:	
					  String modiSpe = orderCaculate.getCountStr(orderCaculate.getModiType(primerProduct.getGeneOrderMidi(),modiSpeMap));
					  primerProduct.setModiSpeType(modiSpe);
					break;
				  case 14:	
					  primerProduct.setRemark(v);
					  //最后根据条件获取价格
					  priceTool.getPrice(primerProduct, customerPrices);
					  priceTool.getModiTypePrice(primerProduct);
					break;	
				  default:
					break;
				}
				index ++;
			}
			
			if (!haveFirst) {
				order.setOrderUpType(orderUpType);
				order.getPrimerProducts().add(primerProduct);
			} else {
				if (!"".equals(outOrderNo)) {
					outOrderNoTemp = outOrderNo;
					order = new Order();
				} else {
					order = orderMap.get(outOrderNoTemp);
				}
				order.setOutOrderNo(outOrderNoTemp);
				order.setOrderUpType(orderUpType);
				order.getPrimerProducts().add(primerProduct);
				
				orderMap.put(outOrderNoTemp, order);
			}
		}
		if (!haveFirst) {
			orders.add(order);
		} else {
			Map<String, Order> resultMap = sortMapByKeyOutOrderNo(orderMap);    //按Key进行排序 
			
			for (Map.Entry<String, Order> map : resultMap.entrySet()) {
				order = (Order)map.getValue();
				orders.add(order);
			}
		}
		return orders;
	}
	
    /** 
     * 使用 Map按key进行排序 
     * @param map  DeliveryInfo
     * @return 
     */  
    public static Map<String, Order> sortMapByKeyOutOrderNo(Map<String, Order> map) {
        if (map == null || map.isEmpty()) {  
            return null;  
        }  
        Map<String, Order> sortMap = new TreeMap<String, Order>(new MapKeyComparator());
        sortMap.putAll(map);  
        return sortMap;  
    }
    
	/**
	 * 解析订单excel第一个sheet页中的客户信息
	 * @param path
	 * @param sheetIndex
	 * @param rows 
	 * @param columns 读取制定excel的列
	 */
	public void ReadExcel(String customerCode,String path, int sheetIndex, String rows, String[] columns) {
		//获取Excel文件的第1个sheet的内容
		ArrayList<ArrayList<String>> customerList = excelResolver.ReadExcel(path, 0, "5-",new String[] {"b"});
		Customer customer = new Customer();
		//输出单元格数据
		for(ArrayList<String> data : customerList) {
			int index=1;
			for(String v : data) {
				System.out.print("\t\t" + v);
				//给客户对象赋值
				switch(index){
				  case 1:
				  	customer.setName(v);
				  	break;
				  case 2:
					customer.setLeaderName(v);
					break;
				  case 3:	
					customer.setName(v);
					break;
				  case 4:	
					customer.setInvoiceTitle(v);
					break;
				  case 5:	
					customer.setPayWays(v);
					break;
				  case 6:	
					customer.setAddress(v);
					break;
				  case 7:	
					customer.setPhoneNo(v);
					break;
				  case 8:	
					customer.setEmail(v);
					break;
				  default:
					break;
				}
				index++;
			}
		}
		
		customer.setCode(customerCode);
		customerRepository.save(customer);
	}
	
	/**
	 * 检测一个字符串是否含有中文字符
	 * @param str
	 * @return
	 */
	public boolean isIncludedChinese(String str) {
		 Pattern pattern=Pattern.compile("[\\u4e00-\\u9fa5]+");  		  
		 Matcher matcher=pattern.matcher(str);  
		 return matcher.find();
	}
	
}
