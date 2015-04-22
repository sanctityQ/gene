package org.one.gene.domain.entity;
// Generated Apr 8, 2015 12:02:08 AM by One Data Tools 1.0.0


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ModifiedPrice.
 */
@Entity
@Table(name = "modified_price")
public class ModifiedPrice implements java.io.Serializable {

  /**
   * 唯一标识id.
   */
  private Integer id;
  /**
   * 修饰类别.
   */
  private String productCategories;
  /**
   * 修饰组合类型.
   */
  private String modiType;
  /**
   * 修饰价格.
   */
  private BigDecimal modiPrice;
  /**
   * 有效标志.
   */
  private String validate;

  public ModifiedPrice() {
  }


  @Id
  @GeneratedValue(strategy = IDENTITY)

  @Column(name = "id", unique = true)
  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(name = "product_categories", length = 31)
  public String getProductCategories() {
    return this.productCategories;
  }

  public void setProductCategories(String productCategories) {
    this.productCategories = productCategories;
  }
  
  @Column(name = "modi_type", length = 31)
  public String getModiType() {
    return this.modiType;
  }

  public void setModiType(String modiType) {
    this.modiType = modiType;
  }

  @Column(name = "modi_price", precision = 10)
  public BigDecimal getModiPrice() {
    return this.modiPrice;
  }

  public void setModiPrice(BigDecimal modiPrice) {
    this.modiPrice = modiPrice;
  }

  @Column(name = "validate", length = 7)
  public String getValidate() {
    return this.validate;
  }

  public void setValidate(String validate) {
    this.validate = validate;
  }


  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}


