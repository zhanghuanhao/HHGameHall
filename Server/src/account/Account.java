/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package account;

/**
 * @author Zhh
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 账号，系统分配 */
	private int id;

	/** 昵称 */
	private String nickname;

	/** 图片编号 */
	private String head = "1.png";

	/** 是否已登陆 */
	private boolean isOnline = false;

	/** 最后上线的时间 */
	private String lastLogin = null;

	/** 最后下线的时间 */
	private String lastLogout = null;

	/** 重置次数，超过3次禁止1天 */
	private int reset = 0;

	/** 密码, transient 自定义序列化 */
	private transient String password;

	/** 密保问题 */
	private transient String serque;

	/** 密保答案 */
	private transient String serans;

	public Account(int id, String pas, String nickname, String serque, String serans) {
		this.id = id;
		this.setPassword(pas);
		this.setNickname(nickname);
		this.setSerque(serque);
		this.setSerans(serans);
	}

	/** 对密码和密保进行加密 */
	private void writeObject(ObjectOutputStream oos) throws Exception {
		oos.defaultWriteObject();
		String pas = AESEncrypt.Encrypt(this.getPassword());
		oos.writeUTF(pas);
		String que = AESEncrypt.Encrypt(this.getSerque());
		oos.writeUTF(que);
		String ans = AESEncrypt.Encrypt(this.getSerans());
		oos.writeUTF(ans);
	}

	private void readObject(ObjectInputStream ois) throws Exception {
		ois.defaultReadObject();
		String pas = ois.readUTF();
		this.setPassword(AESEncrypt.Decrpyt(pas));
		String que = ois.readUTF();
		this.setSerque(AESEncrypt.Decrpyt(que));
		String ans = ois.readUTF();
		this.setSerans(AESEncrypt.Decrpyt(ans));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!obj.getClass().equals(Account.class))
			return false;
		Account acc = (Account) obj;
		if (acc.getId() == this.getId())
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public int getReset() {
		return reset;
	}

	public void setReset(int reset) {
		this.reset = reset;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastLogout() {
		return lastLogout;
	}

	public void setLastLogout(String lastLogout) {
		this.lastLogout = lastLogout;
	}

	public String getSerans() {
		return serans;
	}

	public void setSerans(String serans) {
		this.serans = serans;
	}

	public String getSerque() {
		return serque;
	}

	public void setSerque(String serque) {
		this.serque = serque;
	}

	public int getId() {
		return id;
	}
}
