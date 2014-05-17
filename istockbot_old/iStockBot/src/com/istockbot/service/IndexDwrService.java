package com.istockbot.service;

import java.io.IOException;
import java.util.List;

import com.istockbot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.service
 *   ㄴIndexService.java
 * </pre>
 * @Author 	고협
 * @Date	2011. 10. 23.
 * @Version	1.0.0
 */

public interface IndexDwrService {
	/**
	 * 증권 코드로 결과 받아오는 함수.
	 * 
	 * @param stockCd
	 * @return String 검색 결과
	 * @throws IOException
	 * @see
	 */
	public List<TwitterDmSendModel> getDaumFinancialValue(TwitterDmSendModel model);
	
	/**
	 * 타임라인 페이징 처리하여 받아오는 함수
	 * 
	 * @param pageSet
	 * @return String 검색 결과
	 * @see
	 */
	public String getTwitterTimeLine(int pageSet);
	
	/**
	 * 트윗 버튼으로 트위터에 내용 전송 함수
	 * 
	 * @param mentionCont
	 * @return String 저장결과 (S)uccess/(F)ail
	 * @see
	 */
	public String setTwitterMention(String mentionCont);
	
	/**
	 * 주식 인덱스 받아오는 함수 (KOSPI,KOSDAQ,KRW/USD...)
	 * 
	 * @param 
	 * @return String 인덱스 조회 결과
	 * @see
	 */
	public String getParseIndexInfo();
	
	/**
	 * 주식 정보 받아오는 함수
	 * 
	 * @param krCode
	 * @return String 종목 정보 
	 * @see
	 */
	public String getParseStockInfo(String krCode);
	
	/**
	 * 저장된 종목코드 불러오는 함수
	 * 
	 * @param model 트위터ID
	 * @return List<TwitterDmSendModel>
	 * @see
	 */
	public List<TwitterDmSendModel> getInfoList(TwitterDmSendModel model);
	
	/**
	 * 저장된 종목 코드 삭제 함수
	 * 
	 * @param model 트위터ID, 종목코드
	 * @return String 삭제결과 S
	 * @see
	 */
	public String removeCustTwitterInfo(TwitterDmSendModel model);
	
	/**
	 * 종목 코드 등록 함수
	 * 
	 * @param model 트위터ID, 종목코드
	 * @return String 등록결과 S(성공)/F(중복)
	 * @see
	 */
	public String createStockInfo(TwitterDmSendModel model);
	
	/**
	 * 등록종목 정보조회
	 * 
	 * @param TwitterDmSendModel
	 * @return List<TwitterDmSendModel>
	 * @see
	 */
	public List<TwitterDmSendModel> getUserInfoList(TwitterDmSendModel model);
	
	/**
	 * 등록종목 정보수정
	 * 
	 * @param TwitterDmSendModel
	 * @return String 수정결과 S(성공)/F(실패)
	 * @see
	 */
	public String UpdateUserInfoList(TwitterDmSendModel model);
}

