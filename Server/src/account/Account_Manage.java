/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package account;

/**
 * @author Zhh
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import server.Server_Frame;

public class Account_Manage {
	private volatile static BplusTree tree;

	public static void setTree(Object tree) {
		if (tree == null)
			Account_Manage.tree = null;
		else if (tree instanceof BplusTree)
			Account_Manage.tree = (BplusTree) tree;
	}

	public static BplusTree getTree() {
		return tree;
	}

	/** 设置在线状态 */
	public static void setOnline(int id, boolean flag) {
		if (tree == null)
			return;
		Account acc = (Account) tree.get(id);
		if (acc != null)
			acc.setOnline(flag);
	}

	/** 设置重置次数 */
	public static void Reset() throws Exception {
		if (tree == null)
			return;
		File file = new File("res/ResetOverThree.txt");
		if (!file.exists()) {
			file.createNewFile();
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			int id = Integer.valueOf(line);
			Account acc = (Account) tree.get(id);
			if (acc != null)
				acc.setReset(0);
		}
		br.close();
		file.delete();
		file.createNewFile();
	}

	/** 获取某账户信息 */
	public static Object getAcc(int id) {
		if (tree == null)
			return null;
		return tree.get(id);
	}

	/** 登陆 */
	public static int Login(int id, String pas, String ip) {
		if (tree == null)
			return 0;
		Account acc = (Account) tree.get(id);
		if (acc != null && acc.isOnline())
			return -2;
		if (acc != null && pas.equals(acc.getPassword())) {
			/** 更新服务器列表 */
			Server_Frame.UpdateTable(String.valueOf(id), ip, acc.getNickname(), String.valueOf(acc.getHead()), true);
			acc.setOnline(true);
			acc.setLastLogin(new Date().toString());
			return id;
		} else
			return -1;
	}

	/** 注销 */
	public static void Logout(int id) {
		if (tree == null)
			return;
		Account acc = (Account) tree.get(id);
		if (acc != null) {
			acc.setOnline(false);
			acc.setLastLogout(new Date().toString());
		}
	}

	/** 注册 */
	public synchronized static int Register(String str) {
		if (tree == null)
			return 0;
		String[] inf = str.split("/");
		Account acc = new Account(tree.id, inf[0], inf[1], inf[2], inf[3]);
		return tree.insertOrUpdate(tree.id, acc);
	}

	/** 获取密保 */
	public static String Getserque(int id) {
		if (tree == null)
			return null;
		Account acc = (Account) tree.get(id);
		if (acc == null)
			return null;
		else
			return acc.getSerque();
	}

	/** 重置密码 */
	public static String resetpassword(int id, String serans, String password) throws Exception {
		if (tree == null)
			return null;
		Account acc = (Account) tree.get(id);
		if (acc == null)
			return null;
		if (acc.getReset() >= 3)
			return "BAN"; // 超过三次错误禁止重置
		if (acc.getSerans().equals(serans)) {
			acc.setPassword(password);
			return "RESET";
		} else {
			acc.setReset(acc.getReset() + 1);
			if (acc.getReset() >= 3) {
				File file = new File("res/ResetOverThree.txt");
				PrintWriter pw = new PrintWriter(new FileWriter(file, true));
				pw.println(id);
				pw.flush();
				pw.close();
			}
			return "WRONG";
		}
	}

	/** 设置用户的昵称和头像 */
	public static boolean setInfo(int id, String nickname, String head) {
		if (tree == null)
			return false;
		Account acc = (Account) tree.get(id);
		if (acc == null)
			return false;
		acc.setNickname(nickname);
		acc.setHead(head);
		return true;
	}

	/** 获取用户的昵称和头像 */
	public static String getInfo(int id) {
		if (tree == null)
			return null;
		Account acc = (Account) tree.get(id);
		if (acc == null)
			return null;
		return acc.getNickname() + "%" + acc.getHead();
	}
}
