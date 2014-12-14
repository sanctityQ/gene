package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * PrimerProductValue.
* 
 */
@Entity
@Table(name="primer_product_value"
    , uniqueConstraints = @UniqueConstraint(columnNames="primer_product_id") 
)
public class PrimerProductValue  implements java.io.Serializable {

    /**
    * 唯一标识id.
    */
    private Integer id;
    /**
    * 引物生产数据ID.
    */
    private Integer primerProductId;
    /**
    * 值类型.
    */
    private String type;
    /**
    * 类型描述.
    */
    private String typeDesc;
    /**
    * 数值.
    */
    private BigDecimal value;
    /**
    * 操作时间.
    */
    private Date createTime;

    public PrimerProductValue() {
    }

   
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true)
    public Integer getId() {
    return this.id;
    }

    public void setId(Integer id) {
    this.id = id;
    }
    
    @Column(name="primer_product_id", unique=true)
    public Integer getPrimerProductId() {
    return this.primerProductId;
    }

    public void setPrimerProductId(Integer primerProductId) {
    this.primerProductId = primerProductId;
    }
    
    @Column(name="type", length=31)
    public String getType() {
    return this.type;
    }

    public void setType(String type) {
    this.type = type;
    }
    
    @Column(name="type_desc", length=63)
    public String getTypeDesc() {
    return this.typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
    this.typeDesc = typeDesc;
    }
    
    @Column(name="value", precision=10)
    public BigDecimal getValue() {
    return this.value;
    }

    public void setValue(BigDecimal value) {
    this.value = value;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time", length=19)
    public Date getCreateTime() {
    return this.createTime;
    }

    public void setCreateTime(Date createTime) {
    this.createTime = createTime;
    }


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}


