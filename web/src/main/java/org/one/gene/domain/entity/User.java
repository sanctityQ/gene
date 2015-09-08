package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

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
  private Company company;
  /**
   * 手机号.
   */
  private String mobile;
  /**
   * 邮箱.
   */
  private String email;

  /**
   * 用户标志,0-系统管理员，1-管理层，2-使用者
   */
  private String userFlag;
  /**
   * 是否有效,0-不是，1-是.
   */
  private boolean validate;

  private Customer customer;

  private String salt;

  private String plainPassword;

  private Date createTime;

  private Date modifyTime;


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

  @OneToOne
  @JoinColumn(name = "`com_code`",referencedColumnName = "`com_code`")
  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
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

  @Column(name = "`user_flag`")
  public String getUserFlag() {
    return this.userFlag;
  }

  public void setUserFlag(String userFlag) {
    this.userFlag = userFlag;
  }
  
  @Column(name = "`validate`")
  public boolean isValidate() {
    return this.validate;
  }

  public void setValidate(boolean validate) {
    this.validate = validate;
  }

  @OneToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "`customer_id`",unique = true)
  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Column(name = "`salt`")
  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  @Transient
  public String getPlainPassword() {
    return plainPassword;
  }

  public void setPlainPassword(String plainPassword) {
    this.plainPassword = plainPassword;
  }

  @Column(name = "`create_time`", length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(name = "`modify_time`", length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }

  @PrePersist
  public void generateUserTime() {
    this.createTime = new Date();
  }

  @PreUpdate
  public void generateModifyTime() {
    this.modifyTime = new Date();
  }

}


