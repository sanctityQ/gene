package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * Order.
 */
@Entity
@Table(name = "`order`", uniqueConstraints = @UniqueConstraint(name = "`uk_order_no`", columnNames = "`order_no`"))
public class Order extends IdEntity implements java.io.Serializable {

    public static enum OrderType {od, nmol}

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
    private Integer status;
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
    /**
     * 订单导入类型 nmol、od
     */
    private OrderType orderUpType;
    /**
     * 订单总计 所有生产数据总价格合计
     */
    private BigDecimal totalValue;

    List<PrimerProduct> primerProducts = Lists.newArrayList();
    
    /**
     * 订单中生产数据头尾生产编码
     */
    private String productNoMinToMax;
    /**
     * 订单中所有生产数据碱基数汇总
     */
	private String tbnTotal;

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

    @Column(name = "`order_no`", unique = true, length = 12)
    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "`out_order_no`", length = 63)
    public String getOutOrderNo() {
        return this.outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    @Column(name = "`customer_code`", length = 31)
    public String getCustomerCode() {
        return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    @Column(name = "`customer_name`", length = 127)
    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "`com_code`", length = 15)
    public String getComCode() {
        return this.comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    @Column(name = "`status`")
    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "`type`", length = 2)
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "`file_name`", length = 127)
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`create_time`", length = 19)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`modify_time`", length = 19)
    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "`validate`")
    public boolean isValidate() {
        return this.validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @Column(name = "`order_up_type`")
    @Enumerated(value = EnumType.STRING)
    public OrderType getOrderUpType() {
		return orderUpType;
	}

	public void setOrderUpType(OrderType orderUpType) {
		this.orderUpType = orderUpType;
	}
	@Transient
	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	/**
     * 获取引物生产数据
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    public List<PrimerProduct> getPrimerProducts() {
        return primerProducts;
    }

    public void setPrimerProducts(List<PrimerProduct> primerProducts) {
        this.primerProducts = primerProducts;
    }

    @Column(name = "`product_no_min_to_max`", length = 50)
	public String getProductNoMinToMax() {
		return productNoMinToMax;
	}

	public void setProductNoMinToMax(String productNoMinToMax) {
		this.productNoMinToMax = productNoMinToMax;
	}
	@Column(name = "`tbn_total`", length = 10)
	public String getTbnTotal() {
		return tbnTotal;
	}

	public void setTbnTotal(String tbnTotal) {
		this.tbnTotal = tbnTotal;
	}

    @Transient
    public boolean isOrderType(OrderType orderType){
        return this.getOrderUpType().equals(orderType);
    }


    
}


