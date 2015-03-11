package org.one.gene.domain.service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerType.PrimerOperationType;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.entity.PrintLabel;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.web.order.OrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.instruction.reply.EntityReply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Raw;

//Spring Bean的标识.
@Component
//默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class DeliveryService {

    private static Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PrimerProductRepository primerProductRepository;
    
    @Autowired
    private PrimerProductOperationRepository primerProductOperationRepository;

    @Autowired
    private SynthesisService synthesisService;
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private BoardRepository boardRepository;
    
	//保存发货信息
    @Transactional(readOnly = false)
	public String saveDelivery(List<PrimerProduct> primerProducts) {
    	
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (PrimerProduct pp:primerProducts) {
			primerProduct = primerProductRepository.findOne(pp.getId());
			if (primerProduct != null) {
				primerProduct.setOperationType(PrimerStatusType.finish);
				type = PrimerOperationType.deliverySuccess;
				typeDesc = PrimerOperationType.deliverySuccess.desc();
				
				
				//组装操作信息
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(primerProduct);
				primerProductOperation.setUserCode("123");//后续从session取得
				primerProductOperation.setUserName("张三");//后续从session取得
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setType(type);
				primerProductOperation.setTypeDesc(typeDesc);
				primerProductOperation.setBackTimes(primerProduct.getBackTimes());
				primerProductOperation.setFailReason("");
				
				primerProductOperations.add(primerProductOperation);
				
				
				//保存primer_product表数据
				primerProductRepository.save(primerProduct);
			}
			
    	}
		//更新操作信息
		primerProductOperationRepository.save(primerProductOperations);
    	
    	return "";
    }
   
		
	public Page<OrderInfo> convertPrimerProductList(
			Page<PrimerProduct> primerProductPage, Pageable pageable)
			throws IllegalStateException, IOException {

		OrderInfo orderInfo = new OrderInfo();
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		
		for(PrimerProduct primerProduct:primerProductPage.getContent()){
			
			synthesisService.addNewValue(primerProduct);
			
			orderInfo = new OrderInfo();
			if(!"".equals(primerProduct.getProductNo())){
				orderInfo.setProductNoMinToMax(primerProduct.getProductNo());
			}else{
				orderInfo.setProductNoMinToMax(primerProduct.getOutProductNo());
			}
			orderInfo.setOrderNo(primerProduct.getOrder().getOrderNo());
			orderInfo.setCustomerName(primerProduct.getOrder().getCustomerName());
			orderInfo.setCreateTime(primerProduct.getOrder().getCreateTime());
			orderInfo.setModifyTime(primerProduct.getOrder().getModifyTime());
			orderInfo.setStatus(primerProduct.getOperationType().desc());
			orderInfo.setTbnTotal(primerProduct.getTbn()+"");
			orderInfoList.add(orderInfo);
		}
		
		Page<OrderInfo> primerProductListPage = new PageImpl<OrderInfo>(orderInfoList,pageable,primerProductPage.getTotalElements());
		
        return primerProductListPage;
    }
	
	//保存召回信息
    @Transactional(readOnly = false)
	public String saveBack(List<OrderInfo> orderInfos, String flag, String text ) {
    	
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (OrderInfo orderInfo : orderInfos) {
			primerProduct = primerProductRepository.findByProductNoOrOutProductNo(orderInfo.getProductNoMinToMax(), orderInfo.getProductNoMinToMax());
			if (primerProduct != null) {
				Board board = boardRepository.findByBoardNo(primerProduct.getBoardNo());
				if (board != null) {
					for (BoardHole boardHole:board.getBoardHoles()) {
						
						primerProductOperations = new ArrayList<PrimerProductOperation>();
						
						if (boardHole.getPrimerProduct().getId() == primerProduct.getId()){
							
							boardHole.getPrimerProduct().setModifyTime(new Date());//最后修改时间
							boardHole.setModifyTime(new Date());//最后修改时间
							boardHole.setModifyUser(123L);//后续从session取得
							if (boardHole.getPrimerProduct().getBackTimes() != null) {
								boardHole.getPrimerProduct().setBackTimes(boardHole.getPrimerProduct().getBackTimes()+1);//循环重回次数+1
							}
							
							if ("1".equals(flag)) {//安排合成
								boardHole.getPrimerProduct().setOperationType(PrimerStatusType.synthesis);//回到待合成
								boardHole.getPrimerProduct().setBoardNo("");//清空板号
								boardHole.setStatus(1);//删除
							}else if ("2".equals(flag)) {//重新分装
								boardHole.getPrimerProduct().setOperationType(PrimerStatusType.pack);//回到分装
							}
							
							type = PrimerOperationType.backSuccess;
							typeDesc = PrimerOperationType.backSuccess.desc();
							
							
							//组装操作信息
							primerProductOperation = new PrimerProductOperation();
							primerProductOperation.setPrimerProduct(boardHole.getPrimerProduct());
							primerProductOperation.setUserCode("123");//后续从session取得
							primerProductOperation.setUserName("张三");//后续从session取得
							primerProductOperation.setCreateTime(new Date());
							primerProductOperation.setType(type);
							primerProductOperation.setTypeDesc(typeDesc);
							primerProductOperation.setBackTimes(boardHole.getPrimerProduct().getBackTimes());
							primerProductOperation.setFailReason(text);
							
							if ("1".equals(flag)) {
								boardHole.setPrimerProductOperation(primerProductOperation);
							}
							
							primerProductOperations.add(primerProductOperation);
							
							
							//保存primer_product表数据
							primerProductRepository.save(boardHole.getPrimerProduct());
						}
						
						//更新操作信息
						primerProductOperationRepository.save(primerProductOperations);
						
					}
					
					boardRepository.save(board);
					
				}
			}
			
    	}
		
    	
    	return "";
    }
    
    
    
	/**
     * 导出打印标签文件
     * @throws Exception 
     * */
	public EntityReply<File> deliveryLabel(List<PrimerProduct> primerProducts, Invocation inv) throws Exception {
		//打印单位、订单号、生产编号、碱基数（总和）、od/tube、od总量
		String customerCode = primerProducts.get(0).getOrder().getCustomerCode();
		String strFilePath = inv.getRequest().getSession().getServletContext().getRealPath("/")+"upExcel"+File.separator+"deliveryLabel"+File.separator;
		String strFileName = customerCode+"-"+System.currentTimeMillis()+".xls";
		
		OrderInfo orderInfo = null;
		List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
		Customer customer = customerRepository.findByCode(customerCode);
		
		
		for (PrimerProduct primerProduct : primerProducts) {
			
					
			orderInfo = new OrderInfo();
			//单位
			orderInfo.setUnit(customer.getUnit());
			//订单号
			orderInfo.setOrderNo(primerProduct.getOrder().getOrderNo());
			//生产编号
			if(!"".equals(primerProduct.getProductNo())){
				orderInfo.setProductNo(primerProduct.getProductNo());
			}else{
				orderInfo.setProductNo(primerProduct.getOutProductNo());
			}
			orderInfo.setTbnTotal(new BigDecimal(primerProduct.getGeneOrder().trim().length()).toString());
			//tbnTotal = tbnTotal.add(new BigDecimal(primerProduct.getGeneOrder().trim().length()));
			
			for(PrimerProductValue primerProductValue:primerProduct.getPrimerProductValues()){
				PrimerValueType type = primerProductValue.getType();
				if(type.equals(PrimerValueType.odTotal)){//OD总量
					orderInfo.setOdTotal(primerProductValue.getValue());
				}else if(type.equals(PrimerValueType.odTB)){//OD/TB
					orderInfo.setOdTB(primerProductValue.getValue());
				}
			}
			
			orderInfos.add(orderInfo);
		}
			
		//形成Excel


		//计算第一列多少行
		BigDecimal totalListCount = new BigDecimal(orderInfos.size());//本excel的条数
		int totalcolumns = totalListCount.divide(new BigDecimal(3), 0, BigDecimal.ROUND_UP).intValue();//总共的行数
		
		
		FileOutputStream fos = null;
		
		HSSFWorkbook workbook = new HSSFWorkbook(); //产生工作簿对象
		
		//每行逐行放数据
		int rowNum = 0;
		int rowNumForpage  = 0;
		int columNum = 1;
		int sheetNum = -1;
		HSSFSheet sheet = null;
		for (OrderInfo orderInfoLable : orderInfos) {
			
			if(rowNum==0){
				sheetNum +=1;
				sheet = workbook.createSheet(); //产生工作表对象
				//设置工作表的名称
				workbook.setSheetName(sheetNum,"第"+(sheetNum+1)+"页");
			}
			HSSFRow row = null;
			if(columNum ==1){
				row = sheet.createRow((short)rowNum);//从第一行开始记录
			}else{
				row = sheet.getRow(rowNumForpage);
			}
			for (int k=0;k<6;k++){
				HSSFCell cell = row.createCell((short) ((columNum-1)*6+k));//产生单元格
				//设置单元格内容为字符串型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				//往单元格中写入信息
				String value = "";
				switch(k){
				  case 0:
					value = orderInfoLable.getUnit();
				  	break;
				  case 1:
					value = orderInfoLable.getOrderNo();
					break;
				  case 2:
					value = orderInfoLable.getProductNo();
					break;
				  case 3:
					value = orderInfoLable.getTbnTotal();
					break;
				  case 4:
					value = orderInfoLable.getOdTotal().toString();
					break;
				  case 5:
					value = orderInfoLable.getOdTB().toString();
					break;
				  default:
					break;
				}
				
				if (!"null".equals(value)) {
					cell.setCellValue(value);
				}
		    }
			rowNum+=1;
			rowNumForpage+=1;
			if(totalcolumns == (rowNumForpage)){
				columNum+=1;
				rowNumForpage = 0;
			}
			
		}
		fos = new FileOutputStream(strFilePath + strFileName);
		// 把相应的Excel 工作簿存盘
		workbook.write(fos);
    
        File file = new File(strFilePath, strFileName); 
		
		return Replys.with(file).as(Raw.class).downloadFileName(strFileName);
		
    }
		
}
