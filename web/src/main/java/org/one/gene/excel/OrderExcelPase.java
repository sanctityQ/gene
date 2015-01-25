package org.one.gene.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.repository.CustomerRepository;
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
	
	/**
	 * 组织解析excel验证异常信息
	 * @param data
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public ArrayList<String> getExcelPaseErrors(String path,int ignoreRows, int sheetIndex) throws FileNotFoundException, IOException{
		
		String[][] date = excelResolver.getDataValide(path,1,2);
		
		ArrayList<String> Errors = new ArrayList<String>();

		for (int i = 0; i < date.length; i++) {
			StringBuffer message = new StringBuffer(100);
			if("".equals(date[i][2])){
				message.append("第"+(i+2)+"行第3列[序列]数据不能为空! \n");
			}
			if("".equals(date[i][9])){
				message.append("第"+(i+2)+"行第10列[纯化方式]数据不能为空! \n");
			}
			if("".equals(date[i][3])&&"".equals(date[i][5])){
				message.append("第"+(i+2)+"行第4列与第6列[OD与nmol]数据不能同时为空! \n");
			}
			if(!"".equals(date[i][3])&&!"".equals(date[i][5])){
				message.append("第"+(i+2)+"行第4列与第6列[OD与nmol]数据不能同时存在! \n");
			}
			if(!"".equals(date[i][3])&&"".equals(date[i][4])){
				message.append("第"+(i+2)+"行第4列[nmol总量]不为空值时，第5列[nmol/tube]数据不能为空! \n");
			}
			if(!"".equals(date[i][5])&&"".equals(date[i][6])){
				message.append("第"+(i+2)+"行第6列[OD总量]不为空值时，第7列[OD/tube]数据不能为空! \n");
			}
			if(!"".equals(message.toString())){
			  Errors.add(message.toString());
			}
		}
		return Errors;
	}
	
	/**
	 * 解析第二个订单中的列表信息
	 * @param path
	 * @param sheetIndex 第几个sheet页
	 * @param rows 忽略的行数或从第几行开始
	 */
	public Order ReadExcel(String path, int sheetIndex, String rows,Order order) {
		// 获取Excel文件的第1个sheet的内容
		ArrayList<ArrayList<String>> lists = excelResolver.ReadExcel(path, 1,"2-");
//		ArrayList<PrimerProduct>  primerProducts = new ArrayList<PrimerProduct>();
    	//输出单元格数据
		for(ArrayList<String> data : lists) {
			if(data==null){continue;}
			int index = 1;
			PrimerProduct primerProduct = new PrimerProduct();
			for(String v : data) {
				//给客户对象赋值
				switch(index){
				  case 1:
					  //如果Excel导入中有生产编号存储为外部生产编号，如果没有系统自动生成
					  if("".equals(v)){
						  primerProduct.setProductNo(atomicLongUtil.getProductSerialNo());
					  }else{
						  primerProduct.setOutProductNo(v);
					  }
				  	break;
				  case 2:
					  primerProduct.setPrimeName(v);
					break;
				  case 3:	
					  primerProduct.setGeneOrder(orderCaculate.getYWSeqValue(v));
					break;
				  case 4:	
					  if(!"".equals(v)){
					    primerProduct.setNmolTotal(new BigDecimal(v));
					    order.setOrderUpType(Order.OrderType.nmol);
					  }
					break;
				  case 5:	
					  if(!"".equals(v)){
					    primerProduct.setNmolTB(new BigDecimal(v));
					  }
					break;
				  case 6:	
					  if(!"".equals(v)){
					    primerProduct.setOdTotal(new BigDecimal(v));
					    order.setOrderUpType(Order.OrderType.od);
					  }
					break;
				  case 7:	
					  if(!"".equals(v)){
					    primerProduct.setOdTB(new BigDecimal(v));
					  }
					break;
				  case 8:	
					  primerProduct.setPurifyType(v);
					break;
				  case 9:	
					  primerProduct.setModiFiveType(v);
					break;
				  case 10:	
					  primerProduct.setModiThreeType(v);
					break;
				  case 11:	
					  primerProduct.setModiMidType(v);
					break;
				  case 12:	
					  primerProduct.setModiSpeType(v);
					break;
				  case 13:	
					  primerProduct.setModiPrice(new BigDecimal(v));
					break;
				  case 14:	
					  primerProduct.setBaseVal(new BigDecimal(v));
					  break;
				  case 15:	
					  primerProduct.setPurifyVal(new BigDecimal(v));
					  break;
				  case 16:	
					  primerProduct.setRemark(v);
					break;	
				  default:
					break;
				}
				
				index ++;
			}
			order.getPrimerProducts().add(primerProduct);
		}
		return order;
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
