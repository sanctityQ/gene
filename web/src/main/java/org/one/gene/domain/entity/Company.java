package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Company.
 */
@Entity
@Table(name = "`company`", uniqueConstraints = @UniqueConstraint(columnNames = "`com_code`"))
public class Company extends IdEntity implements java.io.Serializable {

    /**
     * 机构代码.
     */
    private String comCode;
    /**
     * 机构名称.
     */
    private String comName;
    /**
     * 上机机构代码.
     */
    private String upperComCode;
    /**
     * 机构类型.
     */
    private String comType;
    /**
     * 机构级别.
     */
    private String comLevel;
    /**
     * 电话号码.
     */
    private String phoneNumber;
    /**
     * 传真号码.
     */
    private String faxNumber;
    /**
     * 邮编.
     */
    private String postCode;
    /**
     * 地址.
     */
    private String address;
    /**
     * 描述.
     */
    private String desc;
    /**
     * 是否有效,0-无效，1-有效.
     */
    private boolean validate;

    public Company() {
    }


    public Company(String comCode, String comName, String upperComCode, String comType, String comLevel, boolean validate) {
        this.comCode = comCode;
        this.comName = comName;
        this.upperComCode = upperComCode;
        this.comType = comType;
        this.comLevel = comLevel;
        this.validate = validate;
    }

    /**
     * 获取机构代码
     */
    @Column(name = "`com_code`", unique = true, length = 15)
    public String getComCode() {
        return this.comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    @Column(name = "`com_name`", length = 127)
    public String getComName() {
        return this.comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    @Column(name = "`upper_com_code`", length = 15)
    public String getUpperComCode() {
        return this.upperComCode;
    }

    public void setUpperComCode(String upperComCode) {
        this.upperComCode = upperComCode;
    }

    @Column(name = "`com_type`", length = 15)
    public String getComType() {
        return this.comType;
    }

    public void setComType(String comType) {
        this.comType = comType;
    }

    @Column(name = "`com_level`")
    public String getComLevel() {
        return this.comLevel;
    }

    public void setComLevel(String comLevel) {
        this.comLevel = comLevel;
    }

    @Column(name = "`phone_number`", length = 35)
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "`fax_number`", length = 15)
    public String getFaxNumber() {
        return this.faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    @Column(name = "`post_code`", length = 6)
    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Column(name = "`address`")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "`desc`", length = 511)
    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Column(name = "`validate`")
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


