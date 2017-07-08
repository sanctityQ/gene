package org.one.gene.domain.entity;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0


import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * PrimerProduct.
 */
@Entity
@Table(name = "`primer_product`", uniqueConstraints = @UniqueConstraint(name = "`uk_product_no`", columnNames = "`product_no`"))
public class PrimerProduct implements java.io.Serializable {


    private Long id;//唯一标识id.
    private String productNo;//生产编号
    private Order order;//订单号
    private String outProductNo;//外部生产编号
    private String fromProductNo;//来源编号ID
    private String primeName;//引物名称
    private String geneOrder;//引物序列
    private String purifyType;//纯化方式
    private String modiFiveType;//5修饰类型
    private String modiThreeType;//3修饰
    private String modiMidType;//中间修饰
    private String modiSpeType;//特殊单体
    private BigDecimal modiPrice;//修饰价格
    private BigDecimal baseVal;// 碱基单价
    private BigDecimal purifyVal;//纯化价格
    private BigDecimal totalVal;//总价格:修饰单价+碱基单价*碱基数+纯化价格
    private String remark;//描述
    private PrimerStatusType operationType;//状态
    private String boardNo;//板号
    private String comCode;//归属机构代码
    private Integer backTimes;//循环重回次数
    private String reviewFileName;// 检测
    private String geneOrderMidi;//带修饰引物序列
    
    private List<PrimerProductValue> primerProductValues = Lists.newArrayList();
    private List<PrimerProductOperation> primerProductOperations = Lists.newArrayList();

    private Date modifyTime;//操作时间
    private Integer measureVolume;//测值体积
    
    private String operationTypeDesc;//操作类型描述
	private BigDecimal odTotal;//OD总量
	private BigDecimal odTB;//OD/TB
    private BigDecimal nmolTotal;//NUML总量
    private BigDecimal nmolTB;//NUML/TB
    private BigDecimal tbn;//碱基数
    private BigDecimal tb;//分装管数
    private BigDecimal mw;//分子量
	private String midi;//修饰
	private String selectFlag;//是否被选择，用于页面选择时使用，不存库
	private Map<String, BigDecimal> productMolecularMap = Maps.newHashMap();//修饰分子量
    private String liquid;//TE/水溶
	private Integer density;//浓度(P)
	private Integer volume;//  补水体积
	

	public PrimerProduct() {
    }


    public PrimerProduct(String productNo, Order order, String outProductNo, String fromProductNo, String primeName, String geneOrder, String purifyType,
    		PrimerStatusType operationType, String comCode) {
        this.productNo = productNo;
        this.order = order;
        this.outProductNo = outProductNo;
        this.fromProductNo = fromProductNo;
        this.primeName = primeName;
        this.geneOrder = geneOrder;
        this.purifyType = purifyType;
        this.operationType = operationType;
        this.comCode = comCode;
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

    @Column(name = "`product_no`", unique = true, length = 12)
    public String getProductNo() {
        return this.productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    @ManyToOne
    @JoinColumn(name = "`order_no`", referencedColumnName="`order_no`", nullable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Column(name = "`out_product_no`", length = 63)
    public String getOutProductNo() {
        return this.outProductNo;
    }

    public void setOutProductNo(String outProductNo) {
        this.outProductNo = outProductNo;
    }

    @Column(name = "`from_product_no`", length = 12)
    public String getFromProductNo() {
        return this.fromProductNo;
    }

    public void setFromProductNo(String fromProductNo) {
        this.fromProductNo = fromProductNo;
    }

    @Column(name = "`prime_name`")
    public String getPrimeName() {
        return this.primeName;
    }

    public void setPrimeName(String primeName) {
        this.primeName = primeName;
    }

    @Column(name = "`gene_order`")
    public String getGeneOrder() {
        return this.geneOrder;
    }

    public void setGeneOrder(String geneOrder) {
        this.geneOrder = geneOrder;
    }

    @Column(name = "`purify_type`", length = 7)
    public String getPurifyType() {
        return this.purifyType;
    }

    public void setPurifyType(String purifyType) {
        this.purifyType = purifyType;
    }

    @Column(name = "`modi_five_type`", length = 63)
    public String getModiFiveType() {
        return this.modiFiveType;
    }

    public void setModiFiveType(String modiFiveType) {
        this.modiFiveType = modiFiveType;
    }

    @Column(name = "`modi_three_type`", length = 63)
    public String getModiThreeType() {
        return this.modiThreeType;
    }

    public void setModiThreeType(String modiThreeType) {
        this.modiThreeType = modiThreeType;
    }

    @Column(name = "`base_val`", precision = 10)
    public BigDecimal getBaseVal() {
        return this.baseVal;
    }

    public void setBaseVal(BigDecimal baseVal) {
        this.baseVal = baseVal;
    }

    @Column(name = "`modi_mid_type`", length = 63)
    public String getModiMidType() {
        return this.modiMidType;
    }

    public void setModiMidType(String modiMidType) {
        this.modiMidType = modiMidType;
    }

    @Column(name = "`modi_spe_type`", length = 63)
    public String getModiSpeType() {
        return this.modiSpeType;
    }

    public void setModiSpeType(String modiSpeType) {
        this.modiSpeType = modiSpeType;
    }

    @Column(name = "`modi_price`", precision = 10)
    public BigDecimal getModiPrice() {
        return this.modiPrice;
    }

    @Column(name="`purify_val`", precision=10)
    public BigDecimal getPurifyVal() {
        return this.purifyVal;
    }

    public void setPurifyVal(BigDecimal purifyVal) {
        this.purifyVal = purifyVal;
    }

    @Column(name="`total_val`", precision=10)
    public BigDecimal getTotalVal() {
        return this.totalVal;
    }

    public void setTotalVal(BigDecimal totalVal) {
        this.totalVal = totalVal;
    }

    public void setModiPrice(BigDecimal modiPrice) {
        this.modiPrice = modiPrice;
    }

    @Column(name = "`remark`")
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "`operation_type`", length = 31)
    @Enumerated(value = EnumType.STRING)
    public PrimerStatusType getOperationType() {
        return this.operationType;
    }

    public void setOperationType(PrimerStatusType operationType) {
        this.operationType = operationType;
    }

    @Column(name = "`board_no`", length = 127)
    public String getBoardNo() {
        return this.boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    @Column(name = "`com_code`", length = 20)
    public String getComCode() {
        return this.comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    @Column(name = "`back_times`")
    public Integer getBackTimes() {
        return this.backTimes;
    }

    public void setBackTimes(Integer backTimes) {
        this.backTimes = backTimes;
    }

    @Column(name = "`review_file_name`", length = 127)
    public String getReviewFileName() {
        return this.reviewFileName;
    }

    public void setReviewFileName(String reviewFileName) {
        this.reviewFileName = reviewFileName;
    }
    
    @Column(name = "`gene_order_midi`", length = 255)
    public String getGeneOrderMidi() {
		return geneOrderMidi;
	}

	public void setGeneOrderMidi(String geneOrderMidi) {
		this.geneOrderMidi = geneOrderMidi;
	}


	@OneToMany(cascade = CascadeType.ALL, mappedBy = "primerProduct", fetch = FetchType.LAZY)
    public List<PrimerProductValue> getPrimerProductValues() {
        return primerProductValues;
    }

    public void setPrimerProductValues(List<PrimerProductValue> primerProductValues) {
        this.primerProductValues = primerProductValues;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "primerProduct", fetch = FetchType.LAZY)
    public List<PrimerProductOperation> getPrimerProductOperations() {
        return primerProductOperations;
    }

    public void setPrimerProductOperations(List<PrimerProductOperation> primerProductOperations) {
        this.primerProductOperations = primerProductOperations;
    }

	@Transient
	public String getOperationTypeDesc() {
		return operationTypeDesc;
	}


	public void setOperationTypeDesc(String operationTypeDesc) {
		this.operationTypeDesc = operationTypeDesc;
	}

	@Transient
	public BigDecimal getOdTotal() {
        if(this.odTotal == null){
            this.odTotal = this.getPrimerRealValue(PrimerValueType.odTotal);
        }
		return odTotal;
	}


	public void setOdTotal(BigDecimal odTotal) {
		this.odTotal = odTotal;
	}

	@Transient
	public BigDecimal getOdTB() {
        if(this.odTB == null){
            this.odTB = this.getPrimerRealValue(PrimerValueType.odTB);
        }
		return odTB;
	}


	public void setOdTB(BigDecimal odTB) {
		this.odTB = odTB;
	}

	@Transient
	public BigDecimal getNmolTotal() {
        if(this.nmolTotal == null){
            this.nmolTotal = this.getPrimerRealValue(PrimerValueType.nmolTotal);
        }
		return nmolTotal;
	}


	public void setNmolTotal(BigDecimal nmolTotal) {
		this.nmolTotal = nmolTotal;
	}

	@Transient
	public BigDecimal getNmolTB() {
        if (this.nmolTB == null) {
            this.nmolTB = this.getPrimerRealValue(PrimerValueType.nmolTB);
        }
        return nmolTB;
	}


	public void setNmolTB(BigDecimal nmolTB) {
		this.nmolTB = nmolTB;
	}

	@Transient
	public BigDecimal getTbn() {
		return tbn;
	}


	public void setTbn(BigDecimal tbn) {
		this.tbn = tbn;
	}
	
	@Transient
    public BigDecimal getTb() {
		return tb;
	}


	public void setTb(BigDecimal tb) {
		this.tb = tb;
	}
	
	@Transient
    public String getSelectFlag() {
		return selectFlag;
	}


	public void setSelectFlag(String selectFlag) {
		this.selectFlag = selectFlag;
	}
	
	@Transient
    public String getMidi() {
		return midi;
	}


	public void setMidi(String midi) {
		this.midi = midi;
	}

	@Transient
	public BigDecimal getMw() {
		return mw;
	}


	public void setMw(BigDecimal mw) {
		this.mw = mw;
	}
	
	
    @Column(name="`modify_time`")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    
    @Column(name="`measure_volume`")
    public Integer getMeasureVolume() {
    return this.measureVolume;
    }

    public void setMeasureVolume(Integer measureVolume) {
    this.measureVolume = measureVolume;
    }
    
    @Column(name="`liquid`")
	public String getLiquid() {
		return liquid;
	}


	public void setLiquid(String liquid) {
		this.liquid = liquid;
	}

	@Column(name="`density`")
	public Integer getDensity() {
		return density;
	}


	public void setDensity(Integer density) {
		this.density = density;
	}
	
	@Column(name="`volume`")
	public Integer getVolume() {
		return volume;
	}


	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
    @PostLoad
    public void init(){
        for (PrimerProductValue primerProductValue : this.getPrimerProductValues()) {
            this.primerProductValueMap.put(primerProductValue.getType(),primerProductValue);
        }
    }

    @Transient
    public BigDecimal getPrimerRealValue(PrimerValueType type){
    	if(primerProductValueMap.isEmpty() || this.primerProductValueMap.get(type) == null){
    		return new BigDecimal("0.0");
    	}else{
            return this.primerProductValueMap.get(type).getValue();
    	}
    }

    @Transient
    public Boolean isOrderUpType(Order.OrderType orderType){
        return this.getOrder().isOrderType(orderType);
    }

	@Transient
	public Map<String, BigDecimal> getProductMolecularMap() {
		return productMolecularMap;
	}


	public void setProductMolecularMap(Map<String, BigDecimal> productMolecularMap) {
		this.productMolecularMap = productMolecularMap;
	}
	
	
    @PrePersist
    @PreUpdate
    public void generatePrimerProductValue(){


        if (this.getPrimerProductValues().isEmpty()) {
            //初始化数据
            for (PrimerValueType type : PrimerValueType.values()) {
                primerProductValueMap.put(type,type.create(this));
            }
            //重新设置
            this.setPrimerProductValues(Lists.newArrayList(primerProductValueMap.values()));

        } else {
            //0 订单初始化  ，3 审核后订单修改：才允许调整具体的primerProductValue表里的值
            if(this.getOrder().getStatus()==0 || this.getOrder().getStatus()==3){
                //初始化数据
                for (PrimerValueType type : PrimerValueType.values()) {
                    primerProductValueMap.put(type,type.create(this));
                }
                //补ID
                for (PrimerProductValue pv : this.getPrimerProductValues()) {
                    PrimerProductValue npv = primerProductValueMap.get(pv.getType());
                    if (npv != null) {
                        pv.setValue(npv.getValue());
                    }
                }
            }

        }


    }

    private Map<PrimerValueType,PrimerProductValue> primerProductValueMap = Maps.newEnumMap(PrimerValueType.class);


	public void setDensity(BigDecimal bigDecimal) {
		// TODO Auto-generated method stub
		
	}

}


