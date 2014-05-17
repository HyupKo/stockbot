package com.ibot.dao;

import java.util.List;

import com.ibot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.dao
 *   ㄴJobSchedualerDao.java
 * </pre>
 * @Author 	고협
 * @Date	2011. 10. 27.
 * @Version	1.0.0
 */

public interface JobSchedualerDao {
	public List<TwitterDmSendModel> selectSendDmSchedual();
}

