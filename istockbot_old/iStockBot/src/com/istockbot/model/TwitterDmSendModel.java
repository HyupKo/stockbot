package com.istockbot.model;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * <pre>
 * com.ibot.model
 *   ㄴTwitterDmSendModel.java
 * </pre>
 * @Author 	고협
 * @Date	2011. 10. 27.
 * @Version	1.0.0
 */
@DataTransferObject
public class TwitterDmSendModel {
	private String twitterId;
	private String krCode;
	private String activateTime;
	private String regDt;
	private String regr;
	private String stockName;
	private String stockKind;
	private String searchStr;
	private String mentionCont;
	private String indexName;
	private String indexPrice;
	private String indexCompPrice;
	private String indexCompPercent;
	private int stQuantity;
	private int stPrice;
	
	public String getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	public String getkrCode() {
		return krCode;
	}
	public void setkrCode(String krCode) {
		this.krCode = krCode;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getRegr() {
		return regr;
	}
	public void setRegr(String regr) {
		this.regr = regr;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockName() {
		return stockName;
	}
	public String getStockKind() {
		return stockKind;
	}
	public void setStockKind(String stockKind) {
		this.stockKind = stockKind;
	}
	public String getSearchStr() {
		return searchStr;
	}
	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}
	public String getMentionCont() {
		return mentionCont;
	}
	public void setMentionCont(String mentionCont) {
		this.mentionCont = mentionCont;
	}
	public String getActivateTime() {
		return activateTime;
	}
	public void setActivateTime(String activateTime) {
		this.activateTime = activateTime;
	}
	public void setStQuantity(int stQuantity) {
		this.stQuantity = stQuantity;
	}
	public int getStQuantity() {
		return stQuantity;
	}
	public void setStPrice(int stPrice) {
		this.stPrice = stPrice;
	}
	public int getStPrice() {
		return stPrice;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexPrice(String indexPrice) {
		this.indexPrice = indexPrice;
	}
	public String getIndexPrice() {
		return indexPrice;
	}
	public void setIndexCompPrice(String indexCompPrice) {
		this.indexCompPrice = indexCompPrice;
	}
	public String getIndexCompPrice() {
		return indexCompPrice;
	}
	public void setIndexCompPercent(String indexCompPercent) {
		this.indexCompPercent = indexCompPercent;
	}
	public String getIndexCompPercent() {
		return indexCompPercent;
	}
}

