package org.one.gene.domain.entity;
// Generated Dec 31, 2014 3:02:50 PM by One Data Tools 1.0.0


import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * PrimerLabelConfigSub.
 */
@Entity
@Table(name = "primer_label_config_sub", uniqueConstraints = @UniqueConstraint(columnNames = {"primer_label_config_id", "type"}))
public class PrimerLabelConfigSub extends IdEntity implements java.io.Serializable {

    enum Type{

    }

    /**
     * 引物标签打印配置
     */
    private PrimerLabelConfig primerLabelConfig;
    /**
     * 值类型.
     */
    private String type;
    /**
     * 类型描述.
     */
    private String typeDesc;
    /**
     * 排序.
     */
    private Integer sorting;

    public PrimerLabelConfigSub() {
    }


    public PrimerLabelConfigSub(PrimerLabelConfig primerLabelConfig, String type, String typeDesc) {
        this.primerLabelConfig = primerLabelConfig;
        this.type = type;
        this.typeDesc = typeDesc;
    }


    @ManyToOne
    @JoinColumn(name = "`primer_label_config_id`", nullable = false)
    public PrimerLabelConfig getPrimerLabelConfig() {
        return primerLabelConfig;
    }

    public void setPrimerLabelConfig(PrimerLabelConfig primerLabelConfig) {
        this.primerLabelConfig = primerLabelConfig;
    }

    @Column(name = "type", length = 31)
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "type_desc", length = 63)
    public String getTypeDesc() {
        return this.typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    @Column(name = "sorting")
    public Integer getSorting() {
        return this.sorting;
    }

    public void setSorting(Integer sorting) {
        this.sorting = sorting;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}


