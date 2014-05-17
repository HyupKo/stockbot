package com.ibot.jobSchedualer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.common.util.TwitterUtil;
import com.ibot.dao.JobSchedualerDao;
import com.ibot.model.TwitterDmSendModel;

/**
 * <pre>
 * com.ibot.jobSchedualer
 *   ㄴJobSchedualer.java
 * </pre>
 * @Author 	고협
 * @Date	2011. 10. 27.
 * @Version	1.0.0
 */
@Component
public class JobSchedualer {
	
	@Autowired private JobSchedualerDao jobSchedualerDao;

	@Scheduled(cron="0 0/30 9-14 * * MON-FRI")
	public void sendDmToUser() {
		List<TwitterDmSendModel> list = jobSchedualerDao.selectSendDmSchedual();
		for (TwitterDmSendModel twitterDmSendModel : list) {
			TwitterUtil.sendDirectMessage(twitterDmSendModel);
		}
	}

	@Scheduled(cron="0 1 15 * * MON-FRI")
	public void sendDmToUserLast() {
		List<TwitterDmSendModel> list = jobSchedualerDao.selectSendDmSchedual();
		for (TwitterDmSendModel twitterDmSendModel : list) {
			TwitterUtil.sendDirectMessage(twitterDmSendModel);
		}
	}
}

