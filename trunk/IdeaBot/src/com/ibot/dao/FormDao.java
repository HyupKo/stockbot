package com.ibot.dao;

import com.ibot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.dao
 *   ㄴFormDao.java
 * </pre>
 * @Author 	고협
 * @Date	2011. 10. 27.
 * @Version	1.0.0
 */

public interface FormDao {
	/**
	 * 사용자 주식 데이터 저장.
	 * 
	 * @param TwitterDmSendModel
	 * @see
	 */
	public void insertUserStockData(TwitterDmSendModel model);
}

