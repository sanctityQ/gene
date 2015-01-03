package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


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

/**
 * PrimerProductOperation.
 */
@Entity
@Table(name = "`primer_product_operation`", uniqueConstraints = @UniqueConstraint(columnNames = {"`primer_product_id`", "`type`", "`back_times`"}))
public class PrimerProductOperation extends IdEntity implements java.io.Serializable {

    /**
     * 引物生产数据ID.
     */
    private PrimerProduct primerProduct;
    /**
     * 类型.
     */
    private PrimerOperationType type;
    /**
     * 类型描述.
     */
    private String typeDesc;
    /**
     * 工艺循环次数.
     */
    private Integer backTimes;
    /**
     * 用户代码.
     */
    private String userCode;
    /**
     * 用户名称.
     */
    private String userName;
    /**
     * 操作时间.
     */
    private Date createTime;
    /**
     * 失败原因.
     */
    private String failReason;

    public PrimerProductOperation() {
    }


    public PrimerProductOperation(PrimerProduct primerProduct, PrimerOperationType type, String typeDesc, String userCode, String userName, Date createTime, String failReason) {
        this.primerProduct = primerProduct;
        this.type = type;
        this.typeDesc = typeDesc;
        this.userCode = userCode;
        this.userName = userName;
        this.createTime = createTime;
        this.failReason = failReason;

    }

//    @Column(name = "`primer_product_id`")
//    public Integer getPrimerProductId() {
//        return this.primerProductId;
//    }
//
//    public void setPrimerProductId(Integer primerProductId) {
//        this.primerProductId = primerProductId;
//    }

    @ManyToOne
    @JoinColumn(name = "`primer_product_id`", nullable = false)
    public PrimerProduct getPrimerProduct() {
        return primerProduct;
    }

    public void setPrimerProduct(PrimerProduct primerProduct) {
        this.primerProduct = primerProduct;
    }

    @Column(name = "`type`", length = 31)
    @Enumerated(value = EnumType.STRING)
    public PrimerOperationType getType() {
        return this.type;
    }

    public void setType(PrimerOperationType type) {
        this.type = type;
    }

    @Column(name = "`type_desc`", length = 63)
    public String getTypeDesc() {
        return this.typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    @Column(name = "`back_times`")
    public Integer getBackTimes() {
        return this.backTimes;
    }

    public void setBackTimes(Integer backTimes) {
        this.backTimes = backTimes;
    }

    @Column(name = "`user_code`", length = 15)
    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Column(name = "`user_name`", length = 31)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`create_time`", length = 19)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "`fail_reason`", length = 511)
    public String getFailReason() {
        return this.failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}


