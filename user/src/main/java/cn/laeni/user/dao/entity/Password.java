package cn.laeni.user.dao.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.laeni.user.other.entity.HisPass;

public class Password implements Serializable{
	private static final long serialVersionUID = 6804554603075174002L;
	
	private String userId;			// 用户ID
	private String password;		// 用户密码
	private String historyPassword;	// 历史密码(以数组形式存放,如果超过20个)
	private int lastReviseTime;		// 上一次修改时间(时间戳)
	private Boolean forbid;			// 是否禁用
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	/**
	 * 将旧密码存入历史密码中,并将旧密码用新密码替换
	 * @param password
	 */
	public void setPassword(String password) {
		// 如果当前密码不为空,且输入的密码与原密码不相同时,则表示正在修改密码
		if(this.password != null && !this.password.equals(password)) {
			// 此时应该将原密码加入到历史密码中去
			List<HisPass> list = this.historyPassword == null ?
					new ArrayList<HisPass>() :
					JSON.parseArray(this.historyPassword,HisPass.class);
			
			// 由于该列表是ArrayList,所以元素顺序为添加顺序(后添加的排在后面)
			if(list.size()>=20) {
				// 由于每次存入都会判断,所以个数不会大于20个,如果达到20个就删除一个后再存储新元素
				list.remove(0);
			}
			
			// 将新元素加入其中
			HisPass hisPass = new HisPass();
			hisPass.setChangeTime(System.currentTimeMillis()/1000);
			hisPass.setValue(this.password);
			list.add(hisPass);
			
			this.historyPassword = JSON.toJSONString(list);
		}
		this.password = password;
	}
	public String getHistoryPassword() {
		return historyPassword;
	}
	public void setHistoryPassword(String historyPassword) {
		this.historyPassword = historyPassword;
	}
	public int getLastReviseTime() {
		return lastReviseTime;
	}
	public void setLastReviseTime(int lastReviseTime) {
		this.lastReviseTime = lastReviseTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Boolean getForbid() {
		return forbid;
	}
	public void setForbid(Boolean forbid) {
		this.forbid = forbid;
	}
	@Override
	public String toString() {
		return "Password [userId=" + userId + ", password=" + password + ", historyPassword=" + historyPassword
				+ ", lastReviseTime=" + lastReviseTime + ", forbid=" + forbid + "]";
	}

}
