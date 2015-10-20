package org.one.gene.domain.entity;
// Generated Mar 1, 2015 7:23:09 PM by One Data Tools 1.0.0


import java.math.BigDecimal;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CustomerPrice.
 */
@Entity
@Table(name = "`customer_price`")
public class CustomerPrice extends IdEntity implements java.io.Serializable{

    private Customer customer;//客户
    private BigDecimal minTbn;// 最小碱基数
	private BigDecimal maxTbn;// 最大碱基数
    private BigDecimal minOd;//最小OD总数 
    private BigDecimal maxOd;//最大OD总数 
    private String purifyType;//纯化方式
    private BigDecimal baseVal;//碱基单价
    private BigDecimal purifyVal;//纯化价格
    private BigDecimal modifyPrice;//修饰价格
    

    public CustomerPrice() {
    }


    @Column(name = "`modi_price`", precision = 10)
    public BigDecimal getModifyPrice() {
        return this.modifyPrice;
    }

    public void setModifyPrice(BigDecimal modifyPrice) {
        this.modifyPrice = modifyPrice;
    }

    @Column(name = "`base_val`", precision = 10)
    public BigDecimal getBaseVal() {
        return this.baseVal;
    }

    public void setBaseVal(BigDecimal baseVal) {
        this.baseVal = baseVal;
    }

    @Column(name = "`purify_val`", precision = 10)
    public BigDecimal getPurifyVal() {
        return this.purifyVal;
    }

    public void setPurifyVal(BigDecimal purifyVal) {
        this.purifyVal = purifyVal;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "`customer_id`",referencedColumnName="`id`", nullable = false)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @Column(name = "`min_tbn`", precision = 10)
    public BigDecimal getMinTbn() {
		return minTbn;
	}


	public void setMinTbn(BigDecimal minTbn) {
		this.minTbn = minTbn;
	}

    @Column(name = "`max_tbn`", precision = 10)
	public BigDecimal getMaxTbn() {
		return maxTbn;
	}


	public void setMaxTbn(BigDecimal maxTbn) {
		this.maxTbn = maxTbn;
	}

    @Column(name = "`min_od`", precision = 10)
	public BigDecimal getMinOd() {
		return minOd;
	}


	public void setMinOd(BigDecimal minOd) {
		this.minOd = minOd;
	}

    @Column(name = "`max_od`", precision = 10)
	public BigDecimal getMaxOd() {
		return maxOd;
	}


	public void setMaxOd(BigDecimal maxOd) {
		this.maxOd = maxOd;
	}

    @Column(name = "`purify_type`", length = 7)
    public String getPurifyType() {
        return this.purifyType;
    }

    public void setPurifyType(String purifyType) {
        this.purifyType = purifyType;
    }
}


