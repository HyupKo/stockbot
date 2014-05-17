package com.ibot.model;

import org.springframework.stereotype.Component;

/**
 * <pre>
 * com.ibot.model
 *   ㄴTwitterDmSendModel.java
 * </pre>
 * @Author 	고협
 * @Date	2011. 10. 27.
 * @Version	1.0.0
 */
@Component
public class TwitterDmSendModel {
	private String twitterId;
	private String krCode;
	private String activateTime;
	private String regDt;
	private String regr;
	
	public String getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	public String getKrCode() {
		return krCode;
	}
	public void setKrCode(String krCode) {
		this.krCode = krCode;
	}
	public String getActivateTime() {
		return activateTime;
	}
	public void setActivateTime(String activateTime) {
		this.activateTime = activateTime;
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
}

