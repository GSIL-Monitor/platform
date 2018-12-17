package cn.laeni.platform.goods.other.entity;

import java.io.Serializable;

/**
 * 仅仅用于统计记录数
 * 需要用As重命名为"count"
 * @author laeni.cn
 *
 */
public class Count implements Serializable{
	private static final long serialVersionUID = 1245725062079678800L;

	private int count;
	/**
	 * 返回记录的数量
	 * @return
	 */
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "Count [count=" + count + "]";
	}
}
