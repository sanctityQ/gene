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
 * BoardHole.
* 
 */
@Entity
@Table(name="board_hole", uniqueConstraints = @UniqueConstraint(columnNames="board_no"))
public class BoardHole  implements java.io.Serializable {

    /**
    * 唯一标识id.
    */
    private Integer id;
    /**
    * 板号.
    */
    private String boardNo;
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
    private Integer createUser;

    public BoardHole() {
    }

	
    public BoardHole(Date createTime, Integer createUser) {
        this.createTime = createTime;
        this.createUser = createUser;
    }
   
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true)
    public Integer getId() {
    return this.id;
    }

    public void setId(Integer id) {
    this.id = id;
    }
    
    @Column(name="board_no", unique=true, length=127)
    public String getBoardNo() {
    return this.boardNo;
    }

    public void setBoardNo(String boardNo) {
    this.boardNo = boardNo;
    }
    
    @Column(name="hole_no", length=2)
    public String getHoleNo() {
    return this.holeNo;
    }

    public void setHoleNo(String holeNo) {
    this.holeNo = holeNo;
    }
    
    @Column(name="product_id")
    public Long getProductId() {
    return this.productId;
    }

    public void setProductId(Long productId) {
    this.productId = productId;
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


