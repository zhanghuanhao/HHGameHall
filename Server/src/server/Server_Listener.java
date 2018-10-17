/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package server;

/**
 * @author Zhh
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import account.Account_Manage;
import account.BplusTree;
import gobang.Gobang_Tables;
import mainhall.Game_Lobby;
import landlord.Landlord_Tables;

public class Server_Listener extends Thread {
	/**
	 * @param 服务器连接主端口
	 */
	private static final int MAINPORT = 12580;
	/**
	 * @param 在线用户列表，两个客户列表
	 */
	private volatile static Vector<Connections> gobang_clients;
	private volatile static Vector<Connections> landlord_clients;
	private volatile static int landlord_size = 0;
	private volatile static int gobang_size = 0;
	/**
	 * @param 斗地主桌子状态
	 */
	private static Landlord_Tables landlord_tables;
	/**
	 * @param 五子棋桌子状态
	 */
	private static Gobang_Tables gobang_tables;
	private ServerSocket s;
	private static Msg_Deliver msgdeliver;
	private static Game_Lobby gamelobby;

	/**
	 * @param 每隔6小时刷新数据库
	 *            每隔24小时刷新reset
	 */
	private Timer timer;

	/**
	 * @param volatile
	 *            保证数据是最新的
	 */
	volatile boolean isOpen = false;
	private volatile static Hashtable<String, Integer> blacklist;
	private volatile static ArrayList<String> whitelist;
	/** 判断是否开启黑名单 */
	private static final boolean BlackOn = true;
	/** 判断是否开启白名单 */
	private static final boolean WhiteOn = true;
	private static String NOTICE;

	public Server_Listener() {
		gobang_clients = new Vector<Connections>(20);
		landlord_clients = new Vector<Connections>(20);
		this.setName("Listener");
		this.start();
		InitTimer();
		readNotice();
		if (BlackOn)
			blacklist = BlacklistInit();
		if (WhiteOn)
			whitelist = WhitelistInit();
		new Thread(new Runnable() {
			public void run() {
				/** 打开大厅UDP端口 */
				msgdeliver = new Msg_Deliver();
				/** 初始化大厅 */
				gamelobby = new Game_Lobby();
				landlord_tables = new Landlord_Tables();
				gobang_tables = new Gobang_Tables();
			}
		}).start();
	}

	/** 初始化定时器 */
	private void InitTimer() {
		Write_Log.writeActivity("Server_Listener", "Initialing timer....");
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				/*************** 每3小时刷新数据库 **************/
				refreshdb(false);
			}
		}, 3 * 60 * 60 * 1000);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				/*************** 每24小时刷新reset **************/
				Write_Log.writeActivity("Server_Listener", "Refreshing reset times......");
				try {
					Account_Manage.Reset();
					Write_Log.writeActivity("Server_Listener", "Refreshing reset times completed.");
				} catch (Exception e) {
					Write_Log.writeActivity("Server_Listener", "Refreshing reset times failed.\r\n" + e.getMessage());
				}
			}
		}, 24 * 60 * 60 * 1000);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				/*************** 每10分钟更新线程 **************/
			}
		}, 24 * 60 * 60 * 1000);
		Write_Log.writeActivity("Server_Listener", "Initial timer completed");
	}

	/**
	 * 初始化白名单
	 * 
	 * @return
	 */
	private ArrayList<String> WhitelistInit() {
		Write_Log.writeActivity("Server_Listener", "Initialing WhiteList......");
		ArrayList<String> wl = new ArrayList<String>(4);
		try {
			File file = new File("Log/SomeList/white.list");
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String temp = null;
				while ((temp = br.readLine()) != null) {
					wl.add(temp);
				}
				br.close();
			}
		} catch (Exception e) {
		}
		Write_Log.writeActivity("Server_Listener", "Initial WhiteList completed.");
		return wl;
	}

	/**
	 * 初始化黑名单
	 * 
	 * @return
	 */
	private Hashtable<String, Integer> BlacklistInit() {
		Write_Log.writeActivity("Server_Listener", "Initialing BlackList......");
		Hashtable<String, Integer> bl = new Hashtable<String, Integer>(8);
		try {
			File file = new File("Log/SomeList/black.list");
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String temp = null;
				while ((temp = br.readLine()) != null) {
					bl.put(temp, 2);
				}
				br.close();
			}
		} catch (Exception e) {
		}
		Write_Log.writeActivity("Server_Listener", "Initial BlackList completed.");
		return bl;
	}

	/** 反转显示 */
	public void reverseShow() {
		if (gamelobby != null)
			gamelobby.reverseShow();
	}

	@Override
	public void run() {
		try {
			/** 初始化树 */
			Write_Log.writeActivity("Server_Listener", "Initialing tree....");
			long start = System.currentTimeMillis();
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("res/tree/BplusTree.txt")));
			Account_Manage.setTree(ois.readObject());
			ois.close();
			long end = System.currentTimeMillis();
			Write_Log.writeActivity("Server_Listener",
					"Initial BplusTree completed.\r\nCosting " + (end - start) / 1000 + "s.");
		} catch (FileNotFoundException fe) {
			Write_Log.writeActivity("Server_Listener", fe.toString());
			Account_Manage.setTree(new BplusTree(10000));
			Account_Manage.getTree().restore();
			Write_Log.writeActivity("Server_Listener", "Initial new BplusTree.");
		} catch (Exception e) {
			Write_Log.writeActivity("Server_Listener", "Initial BplusTree failed.\r\n" + e.toString());
		}
		try {
			Socket socket;
			s = new ServerSocket(MAINPORT, 50);
			isOpen = true;
			Write_Log.writeActivity("Server_Listener", "Listening the port...");
			while (true) {
				try {
					socket = s.accept();
					String ip = socket.getInetAddress().getHostAddress();
					if (WhiteOn) {
						if (whitelist.contains(ip)) {
							Write_Log.writeActivity("Server_Listener", "IP: " + ip + "\r\nWhite IP trying to login.");
							new Connections(socket, ip);
							continue;
						}
					}
					if (BlackOn) {
						Integer times = blacklist.get(ip);
						if (times != null && times.intValue() >= 2) {
							Write_Log.writeActivity("Server_Listener", "IP: " + ip + "\r\nBlack IP trying to login.");
							socket.close();
							continue;
						}
					}
					new Connections(socket, ip);
				} catch (SocketException se) {
					Write_Log.writeActivity("Server_Listener", "Port closed.");
					break;
				}
			}
		} catch (Exception e) {
			Write_Log.writeActivity("Server_Listener", e.toString());
		} finally {
			this.shutdownHelp();
		}
	}

	/**
	 * 刷新数据库
	 * 
	 * @param flag
	 *            判断是否需要将所有客户的Online设为false
	 */
	public void refreshdb(boolean flag) {
		long time = 0;
		Write_Log.writeActivity("Server_Listener", "Refreshing system database......");
		if (Account_Manage.getTree() != null) {
			if (flag) {
				if (!landlord_clients.isEmpty())
					for (Connections client : landlord_clients) {
						int id = Integer.valueOf(client.getName());
						Account_Manage.setOnline(id, false);
						Write_Log.writeActivity("Server_Listener", id + " now is offline.");
					}
				if (!gobang_clients.isEmpty())
					for (Connections client : gobang_clients) {
						int id = Integer.valueOf(client.getName());
						Account_Manage.setOnline(id, false);
						Write_Log.writeActivity("Server_Listener", id + " now is offline.");
					}
			}
			time = Account_Manage.getTree().restore();
		}
		Write_Log.writeActivity("Server_Listener", "Refresh completed.\r\nCosting " + time + "s.");
	}

	/** 关闭服务器 */
	public void shutdown() {
		try {
			if (isOpen && s != null)
				s.close(); // 关闭端口抛出异常，退出阻塞状态
		} catch (Exception e) {
		}
	}

	/** 关闭服务器辅助，防止线程阻塞无法写入Jtextarea */
	private void shutdownHelp() {
		if (msgdeliver != null)
			msgdeliver.Shutdown();
		gamelobby.dispose();
		// 使所有在线的客户端登陆状态为false
		Write_Log.writeActivity(getName(), "Logout all clients now.");
		if (Account_Manage.getTree() != null) {
			for (Connections client : landlord_clients) {
				Account_Manage.Logout(Integer.valueOf(client.getName()));
			}
			for (Connections client : gobang_clients) {
				Account_Manage.Logout(Integer.valueOf(client.getName()));
			}
		}
		Write_Log.writeActivity(getName(), "Logout all completed.");
		// 关闭定时器
		timer.cancel();
		timer.purge();
		Write_Log.writeActivity(getName(), "Timer closed.");
		// 保存状态
		this.refreshdb(true);
		if (landlord_clients != null)
			landlord_clients.clear();
		if (gobang_clients != null)
			gobang_clients.clear();
		Account_Manage.setTree(null);
		Write_Log.writeActivity("MAIN", "System has exited. Good Luck!");
		this.isOpen = false;
	}

	/**
	 * 更新斗地主大厅的在线列表
	 * 
	 * @param flag
	 *            true增加,false删除
	 * @param mode
	 *            1为能修改size，0为不修改，-1为一定修改
	 */
	static synchronized void updateLandlordClients(Connections client, boolean flag, int mode) {
		boolean temp = false;
		if (flag) {
			temp = landlord_clients.add(client);
			if (mode == -1 || temp && mode == 1) {
				landlord_size++;
				temp = true; // 修改成功
			} else
				temp = false;
		} else {
			temp = landlord_clients.remove(client);
			if (mode == -1 || temp && mode == 1) {
				landlord_size--;
				temp = true; // 修改成功
			} else
				temp = false;
		}
		if (temp)
			gamelobby.updateSize(landlord_size, gobang_size);
	}

	/**
	 * 更新五子棋大厅的在线列表
	 * 
	 * @param flag
	 *            true增加,false删除
	 * @param mode
	 *            1为能修改size，0为不修改，-1为一定修改
	 */
	static synchronized void updateGobangClients(Connections client, boolean flag, int mode) {
		boolean temp = false;
		if (flag) {
			temp = gobang_clients.add(client);
			if (mode == -1 || temp && mode == 1) {
				gobang_size++;
				temp = true; // 修改成功
			} else
				temp = false;
		} else {
			temp = gobang_clients.remove(client);
			if (mode == -1 || temp && mode == 1) {
				gobang_size--;
				temp = true; // 修改成功
			} else
				temp = false;
		}
		if (temp)
			gamelobby.updateSize(landlord_size, gobang_size);
	}

	/**
	 * 移除UDP的列表信息
	 * 
	 * @param ip
	 *            ip地址
	 * @param id
	 *            账户id
	 */
	static void removeUDP(int id) {
		if (msgdeliver != null)
			msgdeliver.Remove(id);
	}

	/**
	 * 发送账号信息给所有人
	 * 
	 * @param mode
	 *            1是斗地主 2是五子棋
	 */
	public static void sendAction2All(String action, int mode) {
		// socket错误需删除的名单
		Vector<Connections> deleted = new Vector<Connections>(4);
		if (mode == 1) {
			if (landlord_clients != null && !landlord_clients.isEmpty()) {
				for (Connections client : landlord_clients) {
					try {
						if (client.isOnline())
							client.send(action);
					} catch (IOException e) {
						Write_Log.writeActivity("Send message 2 client:", client.getName() + " error.");
						deleted.add(client);
					}
				}
			}
			if (!deleted.isEmpty()) {
				for (Connections client : deleted) {
					client.Close();
					landlord_clients.remove(client);
				}
			}
			if ("斗地主".equals(gamelobby.getStatus()))
				gamelobby.paint(action);
		} else if (mode == 2) {
			if (gobang_clients != null && !gobang_clients.isEmpty()) {
				for (Connections client : gobang_clients) {
					try {
						if (client.isOnline())
							client.send(action);
					} catch (IOException e) {
						Write_Log.writeActivity("Send message 2 client", client.getName() + " error.");
						deleted.add(client);
					}
				}
			}
			if (!deleted.isEmpty()) {
				for (Connections client : deleted) {
					client.Close();
					gobang_clients.remove(client);
				}
			}
			if ("五子棋".equals(gamelobby.getStatus()))
				gamelobby.paint(action);
		}
	}

	/**
	 * 踢出某些人
	 */
	public void Kickone(Vector<String> ids) {
		for (String id : ids) {
			for (Connections client : landlord_clients) {
				if (client.getName().equals(id)) {
					client.Close();
					break;
				}
			}
			for (Connections client : gobang_clients) {
				if (client.getName().equals(id)) {
					client.Close();
					break;
				}
			}
		}
	}

	/**
	 * 获取斗地主大厅
	 */
	public static Landlord_Tables getLandlordTableStatus() {
		return landlord_tables;
	}

	/**
	 * 获取五子棋大厅
	 */
	public static Gobang_Tables getGobangTableStatus() {
		return gobang_tables;
	}

	/**
	 * 添加黑名单，超过2次禁IP
	 */
	public static void AppendBlack(String ip) {
		if (WhiteOn && whitelist != null)
			if (whitelist.contains(ip))
				return;
		if (BlackOn && blacklist != null) {
			if (blacklist.containsKey(ip)) {
				int times = blacklist.get(ip).intValue();
				times++;
				blacklist.replace(ip, times);
				if (times >= 2)
					Write_Log.WriteBlack(ip);
			} else {
				blacklist.put(ip, 1);
			}
		}
	}

	public static void showChatContent(String info, String msg) {
		if (gamelobby != null)
			gamelobby.show(info, msg);
	}

	public static String getNotice() {
		return NOTICE;
	}

	/** 读取html文件作为公告栏 */
	void readNotice() {
		File file = new File("log/notice.html");
		boolean flag = false;
		if (file.exists()) {
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
				StringBuilder str = new StringBuilder();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					str.append(temp);
				}
				br.close();
				NOTICE = str.toString();
			} catch (Exception e) {
				flag = true;
			}
		} else
			flag = true;
		if (flag)
			NOTICE = "<html><body>暂无最新公告</body></html>";
		if (gamelobby != null)
			gamelobby.updateNotice();
		Write_Log.writeActivity(getName(), "Refreshing notification completed.");
	}
}
