package org.one.gene.domain.entity;

import java.math.BigDecimal;

public class PrintLabel {

    private String productNo;//生产编号
	private String primeName;//引物名称
	private String geneOrder;//引物序列
	private String orderNo;//订单号
    private String outOrderNo;//外部订单号
	private BigDecimal tube;//管数
	private BigDecimal odTotal;//OD总量
	private BigDecimal odTB;//OD/TB
    private BigDecimal nmolTotal;//NUML总量
    private BigDecimal nmolTB;//NUML/TB
    private BigDecimal tbn;//碱基数
    private BigDecimal mw;//MW
    private BigDecimal tm;//TM
    private BigDecimal gc;//GC
    private BigDecimal mv;//MV
	private BigDecimal ugTB;//ug/tube
	private BigDecimal ugOD;//ug/OD=μg/OD='nmol/tube' * 'MW' /1000
	private BigDecimal odμmol;
	private BigDecimal pmole;//加水量
    private String remark;//备注/日期  ？？
    private String midi;//修饰
    private String siteNo;//位置号
    
    public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getPrimeName() {
		return primeName;
	}
	public void setPrimeName(String primeName) {
		this.primeName = primeName;
	}
    public String getGeneOrder() {
		return geneOrder;
	}
	public void setGeneOrder(String geneOrder) {
		this.geneOrder = geneOrder;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
    public String getOutOrderNo() {
		return outOrderNo;
	}
	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}
	public BigDecimal getTube() {
		return tube;
	}
	public void setTube(BigDecimal tube) {
		this.tube = tube;
	}
	public BigDecimal getOdTotal() {
		return odTotal;
	}
	public void setOdTotal(BigDecimal odTotal) {
		this.odTotal = odTotal;
	}
	public BigDecimal getOdTB() {
		return odTB;
	}
	public void setOdTB(BigDecimal odTB) {
		this.odTB = odTB;
	}
	public BigDecimal getNmolTotal() {
		return nmolTotal;
	}
	public void setNmolTotal(BigDecimal nmolTotal) {
		this.nmolTotal = nmolTotal;
	}
	public BigDecimal getNmolTB() {
		return nmolTB;
	}
	public void setNmolTB(BigDecimal nmolTB) {
		this.nmolTB = nmolTB;
	}
	public BigDecimal getTbn() {
		return tbn;
	}
	public void setTbn(BigDecimal tbn) {
		this.tbn = tbn;
	}
	public BigDecimal getMw() {
		return mw;
	}
	public void setMw(BigDecimal mw) {
		this.mw = mw;
	}
	public BigDecimal getTm() {
		return tm;
	}
	public void setTm(BigDecimal tm) {
		this.tm = tm;
	}
	public BigDecimal getGc() {
		return gc;
	}
	public void setGc(BigDecimal gc) {
		this.gc = gc;
	}
    public BigDecimal getMv() {
		return mv;
	}
	public void setMv(BigDecimal mv) {
		this.mv = mv;
	}
	public BigDecimal getPmole() {
		return pmole;
	}
	public void setPmole(BigDecimal pmole) {
		this.pmole = pmole;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMidi() {
		return midi;
	}
	public void setMidi(String midi) {
		this.midi = midi;
	}
	public BigDecimal getUgTB() {
		return ugTB;
	}
	public void setUgTB(BigDecimal ugTB) {
		this.ugTB = ugTB;
	}
	public BigDecimal getUgOD() {
		return ugOD;
	}
	public void setUgOD(BigDecimal ugOD) {
		this.ugOD = ugOD;
	}
	public BigDecimal getOdμmol() {
		return odμmol;
	}
	public void setOdμmol(BigDecimal odμmol) {
		this.odμmol = odμmol;
	}
	public String getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}
	
	
}


