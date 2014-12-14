package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


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
 * Board
 */
@Entity
@Table(name="board", uniqueConstraints = @UniqueConstraint(columnNames="board_no"))
public class Board extends IdEntity implements java.io.Serializable {

    /**
    * 板号.
    */
    private String boardNo;
    /**
    * 板类型:0-横排 1-竖排.
    */
    private Boolean boardType;
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

    public Board() {
    }

	
    public Board(Date createTime, Integer createUser) {
        this.createTime = createTime;
        this.createUser = createUser;
    }

    @Column(name="board_no", unique=true, length=127)
    public String getBoardNo() {
    return this.boardNo;
    }

    public void setBoardNo(String boardNo) {
    this.boardNo = boardNo;
    }
    
    @Column(name="board_type")
    public Boolean getBoardType() {
    return this.boardType;
    }

    public void setBoardType(Boolean boardType) {
    this.boardType = boardType;
    }
    
    @Column(name="type", length=31)
    public String getType() {
    return this.type;
    }

    public void setType(String type) {
    this.type = type;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time", length=19)
    public Date getCreateTime() {
    return this.createTime;
    }

    public void setCreateTime(Date createTime) {
    this.createTime = createTime;
    }
    
    @Column(name="create_user")
    public Integer getCreateUser() {
    return this.createUser;
    }

    public void setCreateUser(Integer createUser) {
    this.createUser = createUser;
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}


