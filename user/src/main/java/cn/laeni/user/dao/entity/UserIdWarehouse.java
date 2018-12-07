package cn.laeni.user.dao.entity;

public class UserIdWarehouse {

	private String userId;
	private Long addTime;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getAddTime() {
		return addTime;
	}
	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}
	@Override
	public String toString() {
		return "UserIdWarehouse [userId=" + userId + ", addTime=" + addTime + "]";
	}
	
}
