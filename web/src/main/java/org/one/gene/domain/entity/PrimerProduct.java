package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * PrimerProduct.
* 
 */
@Entity
@Table(name="primer_product"
    , uniqueConstraints = @UniqueConstraint(columnNames="order_no") 
)
public class PrimerProduct  implements java.io.Serializable {

    /**
    * 唯一标识id.
    */
    private Long id;
    /**
    * 生产编号.
    */
    private String productNo;
    /**
    * 订单号.
    */
    private String orderNo;
    /**
    * 外部生产编号.
    */
    private String outProductNo;
    /**
    * 来源编号ID.
    */
    private String fromProductNo;
    /**
    * 引物名称.
    */
    private String primeName;
    /**
    * 引物序列.
    */
    private String geneOrder;
    /**
    * 纯化方式.
    */
    private String purifyType;
    /**
    * 5修饰类型.
    */
    private String modiFiveType;
    /**
    * 3修饰.
    */
    private String modiThreeType;
    /**
    * 5修饰.
    */
    private String modiFiveVal;
    /**
    * 中间修饰.
    */
    private String modiMidType;
    /**
    * 特殊单体.
    */
    private String modiSpeType;
    /**
    * 修饰价格.
    */
    private BigDecimal modiPrice;
    /**
    * 描述.
    */
    private String remark;
    /**
    * 状态.
    */
    private String operationType;
    /**
    * 板号.
    */
    private String boardNo;
    /**
    * 归属机构代码.
    */
    private String comCode;
    /**
    * 循环重回次数.
    */
    private Byte backTimes;
    /**
    * 检测.
    */
    private String reviewFileName;

    public PrimerProduct() {
    }

	
    public PrimerProduct(String productNo, String orderNo, String outProductNo, String fromProductNo, String primeName, String geneOrder, String purifyType, String operationType, String comCode) {
        this.productNo = productNo;
        this.orderNo = orderNo;
        this.outProductNo = outProductNo;
        this.fromProductNo = fromProductNo;
        this.primeName = primeName;
        this.geneOrder = geneOrder;
        this.purifyType = purifyType;
        this.operationType = operationType;
        this.comCode = comCode;
    }
   
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true)
    public Long getId() {
    return this.id;
    }

    public void setId(Long id) {
    this.id = id;
    }
    
    @Column(name="product_no", length=12)
    public String getProductNo() {
    return this.productNo;
    }

    public void setProductNo(String productNo) {
    this.productNo = productNo;
    }
    
    @Column(name="order_no", unique=true, length=12)
    public String getOrderNo() {
    return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
    }
    
    @Column(name="out_product_no", length=63)
    public String getOutProductNo() {
    return this.outProductNo;
    }

    public void setOutProductNo(String outProductNo) {
    this.outProductNo = outProductNo;
    }
    
    @Column(name="from_product_no", length=12)
    public String getFromProductNo() {
    return this.fromProductNo;
    }

    public void setFromProductNo(String fromProductNo) {
    this.fromProductNo = fromProductNo;
    }
    
    @Column(name="prime_name")
    public String getPrimeName() {
    return this.primeName;
    }

    public void setPrimeName(String primeName) {
    this.primeName = primeName;
    }
    
    @Column(name="gene_order")
    public String getGeneOrder() {
    return this.geneOrder;
    }

    public void setGeneOrder(String geneOrder) {
    this.geneOrder = geneOrder;
    }
    
    @Column(name="purify_type", length=7)
    public String getPurifyType() {
    return this.purifyType;
    }

    public void setPurifyType(String purifyType) {
    this.purifyType = purifyType;
    }
    
    @Column(name="modi_five_type", length=63)
    public String getModiFiveType() {
    return this.modiFiveType;
    }

    public void setModiFiveType(String modiFiveType) {
    this.modiFiveType = modiFiveType;
    }
    
    @Column(name="modi_three_type", length=63)
    public String getModiThreeType() {
    return this.modiThreeType;
    }

    public void setModiThreeType(String modiThreeType) {
    this.modiThreeType = modiThreeType;
    }
    
    @Column(name="modi_five_val", length=63)
    public String getModiFiveVal() {
    return this.modiFiveVal;
    }

    public void setModiFiveVal(String modiFiveVal) {
    this.modiFiveVal = modiFiveVal;
    }
    
    @Column(name="modi_mid_type", length=63)
    public String getModiMidType() {
    return this.modiMidType;
    }

    public void setModiMidType(String modiMidType) {
    this.modiMidType = modiMidType;
    }
    
    @Column(name="modi_spe_type", length=63)
    public String getModiSpeType() {
    return this.modiSpeType;
    }

    public void setModiSpeType(String modiSpeType) {
    this.modiSpeType = modiSpeType;
    }
    
    @Column(name="modi_price", precision=10)
    public BigDecimal getModiPrice() {
    return this.modiPrice;
    }

    public void setModiPrice(BigDecimal modiPrice) {
    this.modiPrice = modiPrice;
    }
    
    @Column(name="remark")
    public String getRemark() {
    return this.remark;
    }

    public void setRemark(String remark) {
    this.remark = remark;
    }
    
    @Column(name="operation_type", length=31)
    public String getOperationType() {
    return this.operationType;
    }

    public void setOperationType(String operationType) {
    this.operationType = operationType;
    }
    
    @Column(name="board_no", length=127)
    public String getBoardNo() {
    return this.boardNo;
    }

    public void setBoardNo(String boardNo) {
    this.boardNo = boardNo;
    }
    
    @Column(name="com_code", length=20)
    public String getComCode() {
    return this.comCode;
    }

    public void setComCode(String comCode) {
    this.comCode = comCode;
    }
    
    @Column(name="back_times")
    public Byte getBackTimes() {
    return this.backTimes;
    }

    public void setBackTimes(Byte backTimes) {
    this.backTimes = backTimes;
    }
    
    @Column(name="review_file_name", length=127)
    public String getReviewFileName() {
    return this.reviewFileName;
    }

    public void setReviewFileName(String reviewFileName) {
    this.reviewFileName = reviewFileName;
    }


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}


