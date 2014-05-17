package com.ibot.serviceImpl;

import java.io.IOException;

import org.directwebremoting.annotations.RemoteProxy;

import com.common.util.ParserUtil;
import com.ibot.service.IndexDwrService;

/**
 * <pre>
 * com.ibot.serviceImpl
 *   ㄴIndexDwrServiceImpl.java
 * </pre>
 * @Author 	불광가스
 * @Date	2011. 10. 23.
 * @Version	1.0.0
 */
@RemoteProxy(name="indexDwrService")
public class IndexDwrServiceImpl implements IndexDwrService {

	@Override
	public String getDaumFinancialValue(String stockCd) throws IOException {
		return ParserUtil.getStockInfo(stockCd).toString();
	}

}

