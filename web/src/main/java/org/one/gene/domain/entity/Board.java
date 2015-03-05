package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;

/**
 * Board
 */
@Entity
@Table(name="`board`", uniqueConstraints = @UniqueConstraint(columnNames="`board_no`"))
public class Board extends IdEntity implements java.io.Serializable {

    /**
    * 板号.
    */
    private String boardNo;
    /**
    * 板类型:0-横排 1-竖排.
    */
    private String boardType;
    /**
    * 类型.
    */
    private String type;
    /**
    * 创建时间.
    */
    private Date createTime;
    /**
    * 创建user.
    */
    private Integer createUser;

    private List<BoardHole> boardHoles = Lists.newArrayList();
    /**
     * 状态.
     */
    private PrimerStatusType operationType;
    
    
    private String operationTypeDesc;//操作类型描述
    
    
    
    public Board() {
    }

	
    public Board(Date createTime, Integer createUser) {
        this.createTime = createTime;
        this.createUser = createUser;
    }

    @Column(name="`board_no`", unique=true, length=127)
    public String getBoardNo() {
    return this.boardNo;
    }

    public void setBoardNo(String boardNo) {
    this.boardNo = boardNo;
    }
    
    @Column(name="`board_type`")
    public String getBoardType() {
    return this.boardType;
    }

    public void setBoardType(String boardType) {
    this.boardType = boardType;
    }
    
    @Column(name="`type`", length=31)
    public String getType() {
    return this.type;
    }

    public void setType(String type) {
    this.type = type;
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
    public Integer getCreateUser() {
    return this.createUser;
    }

    public void setCreateUser(Integer createUser) {
    this.createUser = createUser;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board")
    @OrderBy(value = "sorting ASC")
    public List<BoardHole> getBoardHoles() {
        return this.boardHoles;
    }

    public void setBoardHoles(List<BoardHole> boardHoles) {
        this.boardHoles = boardHoles;
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

    @Column(name = "`operation_type`", length = 31)
    @Enumerated(value = EnumType.STRING)
    public PrimerStatusType getOperationType() {
        return this.operationType;
    }

    public void setOperationType(PrimerStatusType operationType) {
        this.operationType = operationType;
    }
    
	@Transient
	public String getOperationTypeDesc() {
		return operationTypeDesc;
	}


	public void setOperationTypeDesc(String operationTypeDesc) {
		this.operationTypeDesc = operationTypeDesc;
	}
    
}


