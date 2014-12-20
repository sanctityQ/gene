package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import java.util.Date;
import javax.persistence.*;
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
    * 板号.
    */
    //private String boardNo;
    /**
    * 孔号.
    */
    private String holeNo;
    /**
    * 生产数据ID.
    */
    private Long productId;
    /**
    * 创建时间.
    */
    private Date createTime;
    /**
    * 创建user.
    */
    private Long createUser;

    public BoardHole() {
    }

	
    public BoardHole(Date createTime, Long createUser) {
        this.createTime = createTime;
        this.createUser = createUser;
    }

//    @Column(name="`board_no`", length=127)
//    public String getBoardNo() {
//    return this.boardNo;
//    }
//
//    public void setBoardNo(String boardNo) {
//    this.boardNo = boardNo;
//    }
    
    @Column(name="`hole_no`", length=2)
    public String getHoleNo() {
    return this.holeNo;
    }

    public void setHoleNo(String holeNo) {
    this.holeNo = holeNo;
    }
    
    @Column(name="`product_id`")
    public Long getProductId() {
    return this.productId;
    }

    public void setProductId(Long productId) {
    this.productId = productId;
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
    @JoinColumn(name = "`board_no`", nullable = false)
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

}


