package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * BoardHole.
* 
 */
@Entity
@Table(name = "`board_hole`", uniqueConstraints = @UniqueConstraint(columnNames = {"`board_no`", "`hole_no`"}))
public class BoardHole extends IdEntity implements java.io.Serializable {


    private Board board;

    /**
    * 孔号.
    */
    private String holeNo;

    /**
    * 生产数据ID.
    */
    private PrimerProduct primerProduct;

    /**
    * 创建时间.
    */
    private Date createTime;

    /**
    * 创建user.
    */
    private Long createUser;
    private String failFlag;//临时字段 成功失败标志:  0 成功; 1 失败; 2 重新合成; 3 重新分装
    private String remark;//临时字段
    
    public BoardHole() {
    }

	
    public BoardHole(Date createTime, Long createUser) {
        this.createTime = createTime;
        this.createUser = createUser;
    }

    
    @Column(name="`hole_no`", length=2)
    public String getHoleNo() {
    return this.holeNo;
    }

    public void setHoleNo(String holeNo) {
    this.holeNo = holeNo;
    }


    @NotNull
    @OneToOne
    @JoinColumn(name = "`product_id`", nullable = false)
    public PrimerProduct getPrimerProduct() {
        return primerProduct;
    }

    public void setPrimerProduct(PrimerProduct primerProduct) {
        this.primerProduct = primerProduct;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="`create_time`", length=19)
    public Date getCreateTime() {
    return this.createTime;
    }

    public void setCreateTime(Date createTime) {
    this.createTime = createTime;
    }
    
    @Column(name="`create_user`")
    public Long getCreateUser() {
    return this.createUser;
    }

    public void setCreateUser(Long createUser) {
    this.createUser = createUser;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`board_no`", referencedColumnName = "`board_no`", nullable = false)
    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board){
        this.board = board;
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Transient
	public String getFailFlag() {
		return failFlag;
	}


	public void setFailFlag(String failFlag) {
		this.failFlag = failFlag;
	}

	@Transient
    public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}


