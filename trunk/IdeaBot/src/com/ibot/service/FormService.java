package com.ibot.service;

import com.ibot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.service
 *   ㄴFormService.java
 * </pre>
 * @Author 	고협
 * @Date	2011. 10. 27.
 * @Version	:
 */

public interface FormService {
	/**
	 * 사용자 주식 데이터 저장.
	 * 
	 * @param TwitterDmSendModel
	 * @see
	 */
	public void createUserStockData(TwitterDmSendModel model);
}

