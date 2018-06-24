package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.*;


/**
 * ProductSequence.
 */
@Entity
@Table(name = "`product_sequence`")
public class ProductSequence implements java.io.Serializable {


    private Long id;//唯一标识id.
    

    private Date modifyTime;//操作时间
    

	public ProductSequence() {
    }


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "`id`", unique = true)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name="`modify_time`")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    

}


