package com.istockbot.dao;

import java.util.List;

import com.istockbot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.dao
 *   ㄴIndexDao.java
 * </pre>
 * @Author 	이학준
 * @Date	2011. 10. 27.
 * @Version	1.0.0
 */

public interface IndexDao {
	
	/**
	 * 종목 검색 결과 조회
	 * 
	 * @param 	model
	 * @return	List<TwitterDmSendModel>
	 * @see
	 */
	public List<TwitterDmSendModel> selectSearchInfo(TwitterDmSendModel model);
	
	/**
	 * 저장된 종목 조회
	 * 
	 * @param 	model
	 * @return	List<TwitterDmSendModel>
	 * @see
	 */
	public List<TwitterDmSendModel> selectInfoList(TwitterDmSendModel model);
	
	/**
	 * 저장된 종목 삭제
	 * 
	 * @param 	model
	 * @return	void
	 * @see
	 */
	public void deleteCustTwitterInfo(TwitterDmSendModel model);
	
	/**
	 * 저장된 종목 등록여부 체크
	 * 
	 * @param 	model
	 * @return	List<TwitterDmSendModel>
	 * @see
	 */
	public List<TwitterDmSendModel> checkStockInfo(TwitterDmSendModel model);
	
	/**
	 * 사용자 주식 데이터 저장.
	 * 
	 * @param TwitterDmSendModel
	 * @return	void
	 * @see
	 */
	public void insertUserStockData(TwitterDmSendModel model);
	
	/**
	 * 사용자 정보 조회.
	 * 
	 * @param TwitterDmSendModel
	 * @return	List<TwitterDmSendModel>
	 * @see
	 */
	public List<TwitterDmSendModel> selectUserInfo(TwitterDmSendModel model);
	
	/**
	 * 등록종목 정보 수정
	 * 
	 * @param TwitterDmSendModel
	 * @return	void
	 * @see
	 */
	public void updateUserInfo(TwitterDmSendModel model);
}

