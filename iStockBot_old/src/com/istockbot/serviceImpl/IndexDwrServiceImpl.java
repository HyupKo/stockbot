package com.istockbot.serviceImpl;

import java.util.List;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;

import com.common.util.ParserUtil;
import com.common.util.SessionUtil;
import com.istockbot.dao.IndexDao;
import com.istockbot.model.TwitterDmSendModel;
import com.istockbot.service.IndexDwrService;

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
	@Autowired IndexDao indexDao;
	
	@Override
	public List<TwitterDmSendModel> getDaumFinancialValue(TwitterDmSendModel model) {
		String searchStr = model.getSearchStr();
		if(searchStr.matches("\\d{5}0")) {
			model.setkrCode(searchStr);
		} else {
			model.setStockName(searchStr);
		}
		try {
			model.setTwitterId((String)SessionUtil.getAttribute("_userId"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indexDao.selectSearchInfo(model);
	}
	
	@Override
	public String getTwitterTimeLine(int pageSet){
		Paging paging = new Paging(pageSet,10);
		Twitter twitter = null;
		List<Status> list = null;
		String mentionStr = "<hr>";
		try {
			twitter = (Twitter) SessionUtil.getAttribute("_twitter");
			list = (List<Status>) twitter.getHomeTimeline(paging);
			for(Status status : list){
				mentionStr += "<font size=4><b>"+status.getUser().getScreenName()+"</b></font> " + status.getUser().getName()+"<br>";
				mentionStr += status.getText()+"<br>";
				mentionStr += status.getCreatedAt().toString()+"<br><hr>";
			}
		} catch (Exception e) {
			System.out.println("[IndexDwrServiceImpl  -  index]  :  TwitterException");
			e.printStackTrace();
		}
		return mentionStr;
	}
	
	@Override
	public String setTwitterMention(String mentionCont){
		Status status = null;
		Twitter twitter = null;
		try {
			twitter = (Twitter) SessionUtil.getAttribute("_twitter");
			status = twitter.updateStatus(mentionCont);
		} catch (Exception e) {
			System.out.println("[IndexDwrServiceImpl  -  setTwitterMention]  :  TwitterException");
			e.printStackTrace();
		}
		if(status != null) return "S";
		else return "F";
		
	}
	
	@Override
	public String getParseIndexInfo(){
		return ParserUtil.getIndexInfoStr();
	}
	
	@Override
	public String getParseStockInfo(String krCode){
		TwitterDmSendModel model = new TwitterDmSendModel();
		try {
			model.setTwitterId((String)SessionUtil.getAttribute("_userId"));
			model.setkrCode(krCode);
			List<TwitterDmSendModel> model2 = indexDao.selectUserInfo(model);
			if(!model2.isEmpty()){
				model.setStPrice(model2.get(0).getStPrice());
				model.setStQuantity(model2.get(0).getStQuantity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ParserUtil.getStockInfoStr(krCode, model);
	}
	
	@Override
	public List<TwitterDmSendModel> getInfoList(TwitterDmSendModel model){
		try {
			model.setTwitterId((String)SessionUtil.getAttribute("_userId"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indexDao.selectInfoList(model);
	}
	
	@Override
	public String removeCustTwitterInfo(TwitterDmSendModel model) {
		try {
			model.setTwitterId((String)SessionUtil.getAttribute("_userId"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		indexDao.deleteCustTwitterInfo(model);
		return "S";
	}
	
	@Override
	public String createStockInfo(TwitterDmSendModel model){
		try {
			model.setTwitterId((String)SessionUtil.getAttribute("_userId"));
			model.setActivateTime("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<TwitterDmSendModel> checkInfo = indexDao.checkStockInfo(model);
		if(!checkInfo.isEmpty()) return "F";
		else {
			indexDao.insertUserStockData(model);
			return "S";
		}
	}
	
	@Override
	public List<TwitterDmSendModel> getUserInfoList(TwitterDmSendModel model){
		try {
			model.setTwitterId((String)SessionUtil.getAttribute("_userId"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indexDao.selectUserInfo(model);
	}
	
	@Override
	public String UpdateUserInfoList(TwitterDmSendModel model){
		try {
			model.setTwitterId((String)SessionUtil.getAttribute("_userId"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		indexDao.updateUserInfo(model);
		return "S";
	}
}

