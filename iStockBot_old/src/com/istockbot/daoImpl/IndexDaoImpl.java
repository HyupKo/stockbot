package com.istockbot.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import com.istockbot.dao.IndexDao;
import com.istockbot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.daoImpl
 *   ㄴIndexDaoImpl.java
 * </pre>
 * @Author 	이학준
 * @Date	2011. 10. 27.
 * @Version	:
 */
@SuppressWarnings("unchecked")
@Repository
public class IndexDaoImpl implements IndexDao {

	@Autowired private SqlMapClientTemplate sqlMapClientTemplate;

	@Override
	public List<TwitterDmSendModel> selectSearchInfo(TwitterDmSendModel model){
		return sqlMapClientTemplate.queryForList("index.selectSearchInfo", model);
	}
	
	@Override
	public List<TwitterDmSendModel> selectInfoList(TwitterDmSendModel model){
		return sqlMapClientTemplate.queryForList("index.selectInfoList", model);
	}
	
	@Override
	public void deleteCustTwitterInfo(TwitterDmSendModel model) {
		sqlMapClientTemplate.delete("index.deleteCustTwitterInfo", model);
	}
	
	@Override
	public List<TwitterDmSendModel> checkStockInfo(TwitterDmSendModel model){
		return sqlMapClientTemplate.queryForList("index.selectCheckStockInfo", model);
	}
	
	@Override
	public void insertUserStockData(TwitterDmSendModel model){
		sqlMapClientTemplate.insert("index.insertStockInfo", model);
	}
	
	@Override
	public List<TwitterDmSendModel> selectUserInfo(TwitterDmSendModel model){
		return sqlMapClientTemplate.queryForList("index.selectUserInfo", model);
	}
	
	@Override
	public void updateUserInfo(TwitterDmSendModel model){
		sqlMapClientTemplate.update("index.updateUserInfo", model);
	}
}

