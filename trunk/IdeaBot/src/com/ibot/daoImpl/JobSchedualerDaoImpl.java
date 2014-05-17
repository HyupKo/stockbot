package com.ibot.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import com.ibot.dao.JobSchedualerDao;
import com.ibot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.daoImpl
 *   ㄴJobSchedualerDaoImpl.java
 * </pre>
 * @Author 	불광가스
 * @Date	2011. 10. 27.
 * @Version	:
 */
@SuppressWarnings("unchecked")
@Repository
public class JobSchedualerDaoImpl implements JobSchedualerDao {

	@Autowired private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public List<TwitterDmSendModel> selectSendDmSchedual() {
		return sqlMapClientTemplate.queryForList("job.selectSendDmSchedual");
	}

}

