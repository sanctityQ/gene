package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Order.
* 
 */
@Entity
@Table(name="`order`", uniqueConstraints = @UniqueConstraint(name="`uk_order_no`",columnNames="`order_no`")
)
public class Order  implements java.io.Serializable {

    /**
    * 唯一标识id.
    */
    private Integer id;
    /**
    * 订单号.
    */
    private String orderNo;
    /**
    * 外部订单号.
    */
    private String outOrderNo;
    /**
    * 客户代码.
    */
    private String customerCode;
    /**
    * 客户姓名.
    */
    private String customerName;
    /**
    * 归属机构代码.
    */
    private String comCode;
    /**
    * 订单状态.
    */
    private Byte status;
    /**
    * 订单类型: 00-合成.
    */
    private String type;
    /**
    * 上传文件名称.
    */
    private String fileName;
    /**
    * 创建时间.
    */
    private Date createTime;
    /**
    * 修改时间.
    */
    private Date modifyTime;
    /**
    * 是否有效,0-不是，1-是.
    */
    private boolean validate;

    public Order() {
    }

	
    public Order(String orderNo, String outOrderNo, String customerCode, String type, Date createTime, Date modifyTime, boolean validate) {
        this.orderNo = orderNo;
        this.outOrderNo = outOrderNo;
        this.customerCode = customerCode;
        this.type = type;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.validate = validate;
    }
   
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="`id`", unique=true)
    public Integer getId() {
    return this.id;
    }

    public void setId(Integer id) {
    this.id = id;
    }
    
    @Column(name="`order_no`", unique=true, length=12)
    public String getOrderNo() {
    return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
    }
    
    @Column(name="`out_order_no`", length=63)
    public String getOutOrderNo() {
    return this.outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
    this.outOrderNo = outOrderNo;
    }
    
    @Column(name="`customer_code`", length=31)
    public String getCustomerCode() {
    return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
    }
    
    @Column(name="`customer_name`", length=127)
    public String getCustomerName() {
    return this.customerName;
    }

    public void setCustomerName(String customerName) {
    this.customerName = customerName;
    }
    
    @Column(name="`com_code`", length=15)
    public String getComCode() {
    return this.comCode;
    }

    public void setComCode(String comCode) {
    this.comCode = comCode;
    }
    
    @Column(name="`status`")
    public Byte getStatus() {
    return this.status;
    }

    public void setStatus(Byte status) {
    this.status = status;
    }
    
    @Column(name="`type`", length=2)
    public String getType() {
    return this.type;
    }

    public void setType(String type) {
    this.type = type;
    }
    
    @Column(name="`file_name`", length=127)
    public String getFileName() {
    return this.fileName;
    }

    public void setFileName(String fileName) {
    this.fileName = fileName;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="`create_time`", length=19)
    public Date getCreateTime() {
    return this.createTime;
    }

    public void setCreateTime(Date createTime) {
    this.createTime = createTime;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="`modify_time`", length=19)
    public Date getModifyTime() {
    return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
    }
    
    @Column(name="`validate`")
    public boolean isValidate() {
    return this.validate;
    }

    public void setValidate(boolean validate) {
    this.validate = validate;
    }


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}


