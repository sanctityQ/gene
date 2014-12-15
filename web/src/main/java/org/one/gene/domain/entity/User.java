package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * User
 */
@Entity
@Table(name = "`user`", uniqueConstraints = @UniqueConstraint(columnNames = "`code`"))
public class User extends IdEntity implements java.io.Serializable {

    /**
     * 用户代码.
     */
    private String code;
    /**
     * 用户名称.
     */
    private String name;
    /**
     * 用户密码.
     */
    private String password;
    /**
     * 机构代码.
     */
    private String comCode;
    /**
     * 手机号.
     */
    private String mobile;
    /**
     * 邮箱.
     */
    private String email;
    /**
     * 是否本公司用户标识,0-不是，1-是.
     */
    private boolean staffFlag;
    /**
     * 是否有效,0-不是，1-是.
     */
    private boolean validate;

    public User() {
    }

    @Column(name = "`code`", unique = true, length = 15)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "`name`", length = 31)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "`password`", length = 64)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "`com_code`", length = 10)
    public String getComCode() {
        return this.comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    @Column(name = "`mobile`", length = 11)
    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "`email`", length = 63)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "`staff_flag`")
    public boolean isStaffFlag() {
        return this.staffFlag;
    }

    public void setStaffFlag(boolean staffFlag) {
        this.staffFlag = staffFlag;
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


