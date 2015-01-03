package org.one.gene.domain.entity;
// Generated Dec 31, 2014 3:02:50 PM by One Data Tools 1.0.0


import java.util.Date;
import java.util.List;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * PrimerLabelConfig.
 */
@Entity
@Table(name = "primer_label_config", uniqueConstraints = @UniqueConstraint(columnNames = "customer_code"))
public class PrimerLabelConfig extends IdEntity implements java.io.Serializable {

    public static enum ColumnType {

        one(1), two(2), three(3);

        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static ColumnType convertValue(Integer value) {
            for (ColumnType columnType : ColumnType.values()) {
                if (columnType.getValue() == value) {
                    return columnType;
                }
            }
            throw new IllegalArgumentException("no this type value exception");
        }
    }

    /**
     * 客户代码.
     */
    private String customerCode;
    /**
     * 客户名称.
     */
    private String customerName;
    /**
     * 标签排列列数.
     */
    @Column(name = "columns")
    private Integer columns;
    /**
     * 用户代码.
     */
    private String userCode;
    /**
     * 用户名称.
     */
    private String userName;
    /**
     * 创建时间.
     */
    private Date createTime;
    /**
     * 最后修改时间.
     */
    private Date modifyTime;

    private List<PrimerLabelConfigSub> primerLabelConfigSubs = Lists.newArrayList();

    public PrimerLabelConfig() {
    }


    public PrimerLabelConfig(String customerCode, String customerName, String userCode, String userName, Date createTime, Date modifyTime) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.userCode = userCode;
        this.userName = userName;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }

    @Column(name = "customer_code", unique = true, length = 31)
    public String getCustomerCode() {
        return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    @Column(name = "customer_name", length = 127)
    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public ColumnType getColumns() {
        return ColumnType.convertValue(this.columns);
    }

    public void setColumns(ColumnType columns) {
        this.columns = columns.getValue();
    }

    @Column(name = "user_code", length = 15)
    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Column(name = "user_name", length = 31)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", length = 19)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_time", length = 19)
    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "primerLabelConfig")
    public List<PrimerLabelConfigSub> getPrimerLabelConfigSubs() {
        return primerLabelConfigSubs;
    }

    public void setPrimerLabelConfigSubs(List<PrimerLabelConfigSub> primerLabelConfigSubs) {
        this.primerLabelConfigSubs = primerLabelConfigSubs;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}


