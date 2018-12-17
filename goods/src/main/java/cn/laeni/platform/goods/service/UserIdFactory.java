package cn.laeni.platform.goods.service;

public interface UserIdFactory {
	/**
	 * 随机获取一条账户id并将其删除(使用存储函数)
	 * @return
	 */
	public String getUserIdAndDel();
}
