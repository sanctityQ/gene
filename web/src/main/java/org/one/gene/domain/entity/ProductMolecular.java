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
 * ProductMolecular.
 */
@Entity
@Table(name = "product_molecular")
public class ProductMolecular implements java.io.Serializable {

  /**
   * 唯一标识id.
   */
  private Integer id;
  /**
   * 产品大类代码.
   */
  private String productCategories;
  /**
   * 产品小类代码.
   */
  private String productCode;
  /**
   * 修饰分子量.
   */
  private BigDecimal modifiedMolecular;
  /**
   * 有效标志.
   */
  private String validate;

  public ProductMolecular() {
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

  @Column(name = "product_code", length = 31)
  public String getProductCode() {
    return this.productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  @Column(name = "modified_molecular", precision = 10)
  public BigDecimal getModifiedMolecular() {
    return this.modifiedMolecular;
  }

  public void setModifiedMolecular(BigDecimal modifiedMolecular) {
    this.modifiedMolecular = modifiedMolecular;
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


