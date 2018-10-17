/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package account;
import java.io.Serializable;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 账号，系统分配 */
	public int id;
	
	/** 昵称 */
	public String nickname;
	
	/** 图片编号 */
	public String head = "1.png";

	/** 积分 */
	int score = 0;

	/** 欢乐豆，少于2000可领3000 */
	int doudou = 3000;

	/** 是否已登陆 */
	boolean isOnline = false;
	
	/** 最后上线的时间 */
	String lastLogin = null;
	
	/** 最后下线的时间 */
	String lastLogout = null;
	
	/** 重置次数，超过3次禁止1天 */
	int reset = 0;
	
	/** 密码, transient 自定义序列化 */
	transient String password;

	/** 密保问题 */
	transient String serque;

	/** 密保答案 */
	transient String serans;


	public void jiadoudou() {
		if (doudou < 2000)
			doudou += 3000;
	}

	@Override
	public boolean equals(Object obj) {
		Account acc = (Account) obj;
		if (acc.id == this.id)
			return true;
		else
			return false;
	}
	
	@Override 
	public int hashCode()
	{
		return id;
	}
}
