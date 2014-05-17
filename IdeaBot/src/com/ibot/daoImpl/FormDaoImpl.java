package com.ibot.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import com.ibot.dao.FormDao;
import com.ibot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.daoImpl
 *   ㄴFormDaoImpl.java
 * </pre>
 * @Author 	불광가스
 * @Date	2011. 10. 27.
 * @Version	:
 */
@Repository
public class FormDaoImpl implements FormDao {

	@Autowired private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public void insertUserStockData(TwitterDmSendModel model) {
		sqlMapClientTemplate.insert("form.insertUserStockData", model);
	}

}

