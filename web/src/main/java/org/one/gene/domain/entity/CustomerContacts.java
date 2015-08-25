package org.one.gene.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;


@Entity
@Table(name = "`customer_contacts`", uniqueConstraints = @UniqueConstraint(columnNames = {"`customer_id`", "`name`"}))
public class CustomerContacts extends IdEntity implements java.io.Serializable {

    private Customer customer;//客户公司
    private String name;//联系人姓名
    private String phoneNo;//联系电话
    private String email;//邮箱
    private String fax;//传真


    public CustomerContacts() {
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "`customer_id`",referencedColumnName="`id`", nullable = false)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Column(name = "`name`", length = 30)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "`phone_no`", length = 15)
    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Column(name = "`email`", length = 60)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "`fax`", length = 60)
    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}


