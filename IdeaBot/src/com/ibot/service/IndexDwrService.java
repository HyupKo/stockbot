package com.ibot.service;

import java.io.IOException;

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
	public String getDaumFinancialValue(String stockCd) throws IOException;
}

