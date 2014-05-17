package com.ibot.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibot.dao.FormDao;
import com.ibot.model.TwitterDmSendModel;
import com.ibot.service.FormService;

/**
 * <pre>
 * com.ibot.serviceImpl
 *   ㄴFormServiceImpl.java
 * </pre>
 * @Author 	불광가스
 * @Date	2011. 10. 27.
 * @Version	:
 */
@Service
public class FormServiceImpl implements FormService {
	
	@Autowired private FormDao formDao;

	@Override
	@Transactional
	public void createUserStockData(TwitterDmSendModel model) {
		formDao.insertUserStockData(model);
	}

}

