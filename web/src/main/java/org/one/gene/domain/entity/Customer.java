package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * Customer.
 */
@Entity
@Table(name = "`customer`", uniqueConstraints = @UniqueConstraint(columnNames = "`code`"))
public class Customer extends IdEntity implements java.io.Serializable {

    /**
     * 客户代码.
     */
    private String code;
    /**
     * 客户姓名.
     */
    private String name;
    /**
     * 客户标识，0-梓熙，1-代理公司，2-直接客户
     */
    private String customerFlag;
    /**
     * 机构代码.
     */
    private Company company;
    /**
     * 联系人姓名.
     */
    private String leaderName;

    /**
     * 发票抬头.
     */
    private String invoiceTitle;
    /**
     * 结账方式.
     */
    private String payWays;
    /**
     * 客户地址.
     */
    private String address;
    /**
     * 联系电话.
     */
    private String phoneNo;

    /**
     * 传真.
     */
    private String fax;
    /**
     * 邮箱.
     */
    private String email;
    /**
     * 网址
     */
    private String webSite;
    
    
    /**
     * 办事处
     */
    private String office;
    
    /**
     * 创建时间.
     */
    private Date createTime;
    /**
     * 修改时间.
     */
    private Date modifyTime;
    /**
     * 生产编号开头.
     */
    private String prefix;
    /**
     * 业务员.
     */
    private String handlerCode;
    /**
     * 业务员姓名.
     */
    private String handlerName;

//    private CustomerPrice customerPrice;
    
    private List<PrimerProduct> primerProducts = Lists.newArrayList();
    
    private String haveUserFlag;//是否含有用户
    
    
	public Customer() {
    }


    public Customer(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Column(name = "`code`", unique = true, length = 15)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "`name`", length = 127)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "`customer_flag`", length = 3)
    public String getCustomerFlag() {
        return this.customerFlag;
    }
    
    public void setCustomerFlag(String customerFlag) {
        this.customerFlag = customerFlag;
    }
    
    @OneToOne
    @JoinColumn(name = "`com_code`",referencedColumnName = "`com_code`")
    public Company getCompany() {
      return company;
    }

    public void setCompany(Company company) {
      this.company = company;
    }
    
    @Column(name = "`leader_name`", length = 127)
    public String getLeaderName() {
        return this.leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    @Column(name = "`invoice_title`", length = 120)
    public String getInvoiceTitle() {
        return this.invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    @Column(name = "`pay_ways`", length = 15)
    public String getPayWays() {
        return this.payWays;
    }

    public void setPayWays(String payWays) {
        this.payWays = payWays;
    }

    @Column(name = "`address`")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "`phone_no`", length = 15)
    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Column(name = "`email`", length = 63)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Column(name = "`web_site`", length = 120)
    public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	@Column(name = "`office`", length = 255)
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "`create_time`", length = 19)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "`modify_time`", length = 19)
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "`prefix`", length = 2)
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@Column(name = "`handler_code`", length = 30)
	public String getHandlerCode() {
		return handlerCode;
	}
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	@Column(name = "`handler_name`", length = 255)
	public String getHandlerName() {
		return handlerName;
	}


	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}


	@Transient
    public List<PrimerProduct> getPrimerProducts() {
        return primerProducts;
    }


    public void setPrimerProducts(List<PrimerProduct> primerProducts) {
        this.primerProducts = primerProducts;
    }

    @Column(name="fax", length=15)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Transient
	public String getHaveUserFlag() {
		return haveUserFlag;
	}


	public void setHaveUserFlag(String haveUserFlag) {
		this.haveUserFlag = haveUserFlag;
	}
}


