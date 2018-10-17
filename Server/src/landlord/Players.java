/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package landlord;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Players implements Serializable {

	private static final long serialVersionUID = 1L;
	private int size = 0;

	public class info implements Serializable {
		private static final long serialVersionUID = 1L;
		public int id = -1;
		public String playername = null;
		public String playerhead = null;
		public int position = -1;
		public transient ObjectOutputStream output = null;
		public transient ObjectInputStream input = null;
	}

	public volatile info[] playerinfo = null;
	public Poker[] pokers = null;
	public Poker dzpoker = null;
	public int dz = -1;// 地主编号
	public int round = 0;// 轮数
	public Poker lastpoker = new Poker(0);// 上轮出的牌
	public int temps = 0;// 当前打出牌数量
	public Poker temppoker = null;// 当前打出的牌
	public boolean[] qdz = null;// 抢地主
	public int over = 0;

	public Players() {
		size = 0;
		qdz = new boolean[3];
		playerinfo = new info[3];
		pokers = new Poker[3];// 三套各17张牌
		for (int i = 0; i < 3; i++) {
			pokers[i] = new Poker(17);
			playerinfo[i] = new info();
			qdz[i] = false;
		}
		dzpoker = new Poker(3);// 地主3张牌
		temppoker = new Poker(0);
	}

	// 添加玩家
	public void addplayer(int id, int position, String name, String head, Object out, Object in) {
		boolean flag = true;// 查重
		for (int i = 0; i < size; i++) {
			if (playerinfo[i].id == id)
				flag = false;
		}
		if (flag) {
			playerinfo[size].id = id;
			playerinfo[size].position = position;
			playerinfo[size].playername = name;
			playerinfo[size].playerhead = head;
			playerinfo[size].output = (ObjectOutputStream) out;
			playerinfo[size].input = (ObjectInputStream) in;
			size++;
		}
	}

	// 获取账号
	public int getplayid(int num) {
		return playerinfo[num].id;
	}

	// 获取昵称
	public String getplayername(int num) {
		return playerinfo[num].playername;
	}

	// 获取头像
	public String getplayerhead(int num) {
		return playerinfo[num].playerhead;
	}

	// 获取连接
	public ObjectOutputStream getclientput(int num) {
		return playerinfo[num].output;
	}

	public ObjectInputStream getinput(int num) {
		return playerinfo[num].input;
	}

	// 获取人数
	public int getnum() {
		return size;
	}

	// 删除玩家
	public void delplayer(int id) {
		for (int i = 0; i < size; i++) {
			if (playerinfo[i].id == id) {
				playerinfo[i] = new info();
				for (int t = i + 1; t < size; t++) {
					playerinfo[t - 1] = playerinfo[t];
				}
				size--;
				break;
			}
		}
	}

	public void adddz() {
		pokers[dz].add(dzpoker);
	}

	public boolean contains(int id) {
		for (int i = 0; i < size; i++) {
			if (this.playerinfo[i].id == id)
				return true;
		}
		return false;
	}
}
