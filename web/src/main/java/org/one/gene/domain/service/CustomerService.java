package org.one.gene.domain.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.CustomerContacts;
import org.one.gene.domain.entity.CustomerPrice;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.repository.CustomerContactsRepository;
import org.one.gene.repository.CustomerPriceRepository;
import org.one.gene.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.one.mvc.web.Invocation;

@Component
public class CustomerService {
	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
    private CustomerContactsRepository customerContactsRepository;
	
	@Autowired
    private CustomerPriceRepository customerPriceRepository;
	
	
	@Transactional(readOnly = false)
	public void save(Customer customer){
		//Customer customerOld = customerRepository.findByCode(customer.getCode());
		//生产编号开头大写
    	String prefix = customer.getPrefix().toUpperCase();//生产编号开头
    	//直接客户使用梓熙的配置
		if ("2".equals(customer.getCustomerFlag())) {
			List<Customer> customers = customerRepository.seachHaveZiXi();
			if (customers != null && customers.size() > 0) {
				Customer customerTemp = (Customer)customers.get(0);
				prefix = customerTemp.getPrefix().toUpperCase();
			}
		}
		customer.setPrefix(prefix);
		customer.setModifyTime(new Date());
		
		//remove CustomerContactss 中null的对象
		for (int i = customer.getCustomerContactss().size() - 1; i >= 0; i--) {
			CustomerContacts cc = customer.getCustomerContactss().get(i);
			if(cc == null || cc.getName()==null){
				customer.getCustomerContactss().remove(i);
			}else{
				cc.setCustomer(customer);
			}
		}
        
		//删除数据库中需要删除的数据
		if (customer.getId() != null){
			List<CustomerContacts> ccsOld= customerContactsRepository.findByCustomer(customer);
			if (ccsOld != null) {
				for(CustomerContacts ccOld:ccsOld){
					boolean match = false;//数据库中的联系人id没有匹配上页面的值，需要删除
					for(CustomerContacts ccNew:customer.getCustomerContactss()){
						if (ccOld.getId() == ccNew.getId()) {
							match = true;
						}
					}
					
					if (!match) {
						customerContactsRepository.delete(ccOld);
					}
				}
			}
			
			customerPriceRepository.deleteByCustomerId(customer.getId());
			
		}
		
		//remove CustomerPrices 中null的对象
		for (int i = customer.getCustomerPrices().size() - 1; i >= 0; i--) {
			CustomerPrice cp = customer.getCustomerPrices().get(i);
			if(cp == null || cp.getPurifyType()==null){
				customer.getCustomerPrices().remove(i);
			}else{
				cp.setCustomer(customer);
			}
		}
		
		if (customer.getId() != null) {
			customerPriceRepository.deleteByCustomerId(customer.getId());
		}

		customerRepository.save(customer);
	}
	
    /**
     * 导出客户清单
     * @throws IOException 
     * */
	public void exportCustomer(  List<Customer> customers  , Invocation inv) throws IOException {
		
		String strFileName = System.currentTimeMillis()+".xls";
		
		//形成Excel
		String templetName = "customerList.xls";
		String templatePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(templatePath+templetName));
        HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		
		if (customers != null && customers.size() > 0) {
			for (int i = 0; i < customers.size(); i++) {
				Customer cust = (Customer) customers.get(i);

				row = sheet.createRow(i+1);

				cell = row.createCell(0);// 得到单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getCode());//客户编号

				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getName());//客户公司
				
				cell = row.createCell(2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if("0".equals(cust.getCustomerFlag())){
					cell.setCellValue("梓熙");//客户性质
				} else if("1".equals(cust.getCustomerFlag())){
					cell.setCellValue("代理公司");//客户性质
				} else {
					cell.setCellValue("直接客户");//客户性质
				}
				
				cell = row.createCell(3);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getLeaderName());//联系人姓名
				
				cell = row.createCell(4);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getHandlerName());//业务员姓名
				
				cell = row.createCell(5);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getPayWays());//结算方式
				
				cell = row.createCell(6);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getInvoiceTitle());//发票抬头
				
				cell = row.createCell(7);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getPrefix());//生产编号开头
				
				cell = row.createCell(8);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getPhoneNo());//联系电话
				
				cell = row.createCell(9);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getWebSite());//网址
				
				cell = row.createCell(10);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getOffice());//办事处
				
				cell = row.createCell(11);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getFax());//传真
				
				cell = row.createCell(12);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getAddress());//客户地址
				
				cell = row.createCell(13);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cust.getEmail());//Email

			}
		}
		
        //输出文件到客户端
        HttpServletResponse response = inv.getResponse();
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + strFileName + "\"");
        OutputStream out=response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
	
	
    //查询客户信息
	public Page<Customer> queryCustomers(String customerName,
			String customerFlag, String handlerName, String contactName,
			String comCode, Pageable pageable) {

		Page<Customer> customerPage = customerRepository.queryCustomers(
				customerName, customerFlag, handlerName, contactName, comCode,
				pageable);

		return customerPage;
	}
	
}
