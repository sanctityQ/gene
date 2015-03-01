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
@Table(name = "`customer_price`", uniqueConstraints = @UniqueConstraint(columnNames = "`customer_code`"))
public class CustomerPrice extends IdEntity implements java.io.Serializable{


    /**
     * 客户代码.
     */
    //private String customerCode;

    private Customer customer;
    /**
     * 修饰价格.
     */
    private BigDecimal modifyPrice;
    /**
     * 碱基单价.
     */
    private BigDecimal baseVal;
    /**
     * 纯化价格.
     */
    private BigDecimal purifyVal;

    public CustomerPrice() {
    }


//    public CustomerPrice(String customerCode) {
//        this.customerCode = customerCode;
//    }

//    @Column(name = "customer_code", unique = true, length = 31)
//    public String getCustomerCode() {
//        return this.customerCode;
//    }
//
//    public void setCustomerCode(String customerCode) {
//        this.customerCode = customerCode;
//    }

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

    @OneToOne
    @JoinColumn(name = "`customer_code`",referencedColumnName="`code`", unique = true)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}


