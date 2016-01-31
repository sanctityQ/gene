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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;


@Entity
@Table(name = "`menu`", uniqueConstraints = @UniqueConstraint(columnNames = {"`first_id`", "`second_id`"}))
public class Menu extends IdEntity implements java.io.Serializable {

    private String firstId;//一级菜单id
    private String firstName;//一级菜单名称
    private String secondId;//一级菜单id
    private String secondName;//一级菜单名称
    private String url;//二级菜单url
    private Integer orderNo;;//菜单位置序号
    private String remark;//备注
    private String valid;//备注
    private String selectFlag ;//是否被选中 临时字段


    public Menu() {
    }

    @Column(name = "`first_id`", length = 31)
    public String getFirstId() {
        return this.firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    @Column(name = "`first_name`", length = 31)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "`second_id`", length = 31)
    public String getSecondId() {
        return this.secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }
    
    @Column(name = "`second_name`", length = 31)
    public String getSecondName() {
        return this.secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    
    @Column(name = "`url`", length = 100)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Column(name = "`order_no`")
    public Integer getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
    
    @Column(name = "`remark`", length = 100)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Column(name = "`valid`", length = 7)
    public String getValid() {
        return this.valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Transient
    public String getSelectFlag() {
      return selectFlag;
    }

    public void setSelectFlag(String selectFlag) {
      this.selectFlag = selectFlag;
    }
    
}


