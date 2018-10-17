/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package server;

/**
 * @author Zhh
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import account.Account_Manage;

public class Connections extends Thread {
	private static final String LANDLORD = "LANDLORD";
	private static final String GOBANG = "GOBANG";
	private static final String CHANGE = "CHANGE";
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String ip;
	private boolean Online;
	private int id;
	private String head;
	private String nickname;
	private int position;
	/** 判断动作是否成功 */
	private Boolean flag = false;
	/** 在哪个游戏大厅 */
	private String status;
	/** 加载RSA */
	private static Decode dec = new Decode();

	public Connections(Socket socket, String ip) {
		this.socket = socket;
		Online = false;
		id = -1;
		position = -1;
		head = null;
		this.ip = ip;
		status = null;
		this.setName(ip);
		this.start();

	}

	/**
	 * 关闭线程
	 */
	public void Close() {
		Write_Log.writeActivity("id: " + id, ip + "\r\nIs closing...");
		try {
			/** 连接关闭 */
			Server_Frame.UpdateTable(this.getName(), null, null, null, false);
			if (Online) {
				Server_Listener.removeUDP(id);
				Account_Manage.Logout(id);
				Online = false;
			}
			int mode = 0;
			flag = false;
			String msg = position + "%STANDUP%" + nickname + "%" + head;
			if (LANDLORD.equals(status)) {
				Server_Listener.updateLandlordClients(this, false, -1);
				if (position != -1) {
					mode = 1;
					flag = Server_Listener.getLandlordTableStatus().leaveTable(position, id);
				}
			} else if (GOBANG.equals(status)) {
				Server_Listener.updateGobangClients(this, false, -1);
				if (position != -1) {
					mode = 2;
					flag = Server_Listener.getGobangTableStatus().leaveTable(position, id);
				}
			}
			if (flag)
				Server_Listener.sendAction2All(msg, mode);
			socket.close();
		} catch (Exception e) {
		}
		Write_Log.writeActivity("id: " + id, ip + "\r\nClose complete.");
	}

	/**
	 * 发送桌子，消息
	 */
	public void send(Object object) throws IOException {
		out.reset();
		if (object instanceof String) {
			out.writeUTF((String) object);
		} else {
			out.writeObject(object);
		}
		out.flush();
	}

	/**
	 * 返回是否在线
	 */
	boolean isOnline() {
		return Online;
	}

	@Override
	public void run() {
		try {
			// 设置超时20分钟
			socket.setSoTimeout(20 * 60 * 1000);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			/** 发送公钥 */
			out.reset();
			out.write(dec.getpubkey(), 0, dec.getpubkey().length);
			out.flush();
			byte[] msgbyte = new byte[256];
			in.read(msgbyte);
			/** 接收第一条登陆信息 */
			String firstmsg = dec.decode(msgbyte);
			if (firstmsg == null)
				throw new Exception("First message is Null!");
			/** 完成第一步操作 */
			String[] str = firstmsg.split("%");
			if (str[0].equals("LOGIN")) {
				try {
					/** 尝试登陆 */
					String[] inf = str[1].split("/");
					id = Integer.parseInt(inf[0]);
					Write_Log.writeActivity("id: " + id, ip + "\r\nTrying Login.");
					int check = Account_Manage.Login(id, inf[1], ip);
					if (check == -1) {
						/** 登录失败 */
						send("DISCONFIRM");
						Write_Log.writeActivity("id: " + id, ip + "\r\nLogin failed.");
					} else if (check == -2) {
						/** 已经登陆 */
						send("BIND");
						Write_Log.writeActivity("id: " + id, ip + "\r\nLogin failed.");
					} else if (check == id) {
						/** 登陆成功 */
						Write_Log.writeActivity("id: " + id, ip + "\r\nLogin succeeded.");
						send("CONFIRM");
						/*** 获取个人信息 **/
						String[] infomation = Account_Manage.getInfo(id).split("%");
						nickname = infomation[0];
						head = infomation[1];
						/*** 在线参数设为true */
						Online = true;
						/*** 线程名为id */
						this.setName(String.valueOf(id));
						/*** 将账号参数发送出去 */
						send(Account_Manage.getAcc(id));
						Msg_Deliver.send(nickname + " 欢迎来到本大厅~~~");
					}
				} catch (Exception e) {
					send("DISCONFIRM");
					Write_Log.writeActivity("id: " + id, ip + "\r\nLogin failed.");
				}
			} else if (str[0].equals("FORGET1")) {
				try {
					/** 忘记密码 */
					id = Integer.parseInt(str[1]);
					Write_Log.writeActivity("id: " + id, ip + "\r\nTrying get ser-question.");
					String msg = Account_Manage.Getserque(id);
					if (msg == null) {
						/** 账号不存在 */
						send("NOEXIST");
						Write_Log.writeActivity("id: " + id, ip + "\r\nReturn NOEXIST.");
					} else {
						/** 找到密保 */
						send("FIND");
						send(msg);
						Write_Log.writeActivity("id: " + id, ip + "\r\nReturn FIND.");
					}
				} catch (Exception e) {
					send("NOEXIST");
					Write_Log.writeActivity("id: " + id, ip + "\r\nReturn NOEXIST.");
				}
			} else if (str[0].equals("FORGET2")) {
				try {
					/** 尝试重置 */
					String[] inf = str[1].split("/");
					int id = Integer.parseInt(inf[0]);
					Write_Log.writeActivity("id: " + id, ip + "\r\nTrying RESET.");
					String msg = Account_Manage.resetpassword(id, inf[1], inf[2]);
					send(msg);
					Write_Log.writeActivity("id: " + id, ip + " Return " + msg + ".");
				} catch (Exception e) {
					send("WRONG");
					Write_Log.writeActivity("id: " + id, ip + " Return WRONG.");
				}
			} else if (str[0].equals("RESITER")) {
				/** 尝试注册 */
				id = Account_Manage.Register(str[1]);
				Write_Log.writeActivity("id: " + id, ip + "\r\nTrying Resiter.");
				send("SUCCESS");
				out.reset();
				out.writeInt(id);
				out.flush();
				/** 注册成功 */
				Write_Log.writeActivity("id: " + id, ip + "\r\nResiter succeeded.");
			}
			/** 第一次结束 */

			/** 如果登陆，开始循环 */
			if (Online) {
				// 发送公告
				send(Server_Listener.getNotice());
				while (true) {
					/** 收取用户动作信息 */
					String msg = in.readUTF();
					if (LANDLORD.equals(msg)) {
						// 发送斗地主大桌子更新
						send(msg);
						send(Server_Listener.getLandlordTableStatus().getStatus());
						// 第一次登陆
						if (status == null)
							Server_Listener.updateLandlordClients(this, true, -1);
						else if (!LANDLORD.equals(status)) {
							Server_Listener.updateLandlordClients(this, true, 1);
							Server_Listener.updateGobangClients(this, false, 1);
						}
						status = msg;
						Write_Log.writeActivity("id: " + id, ip + "\r\nSelect Landlord.");
					} else if (GOBANG.equals(msg)) {
						// 发送五子棋大桌子更新
						send(msg);
						send(Server_Listener.getGobangTableStatus().getStatus());
						if (status == null)
							Server_Listener.updateGobangClients(this, true, -1);
						else if (!GOBANG.equals(status)) {
							Server_Listener.updateLandlordClients(this, false, 1);
							Server_Listener.updateGobangClients(this, true, 1);
						}
						status = msg;
						Write_Log.writeActivity("id: " + id, ip + "\r\nSelect Gobang.");
					} else {
						String[] s = msg.split("%");
						if (CHANGE.equals(s[0])) {
							// 修改信息
							Write_Log.writeActivity("id: " + id, ip + "\r\nTrying to change info.");
							boolean flag = Account_Manage.setInfo(id, s[1], s[2]);
							if (flag) {
								send("CONFIRM");
								nickname = s[1];
								head = s[2];
								Write_Log.writeActivity("id: " + id, ip + "\r\nChange info succeeded.");
							} else {
								send("DISCONFIRM");
								Write_Log.writeActivity("id: " + id, ip + "\r\nChange info failed.");
							}
						} else if (LANDLORD.equals(status)) {
							// 斗地主大厅
							position = Integer.valueOf(s[1]);
							msg = position + "%" + s[0] + "%" + nickname + "%" + head;
							if (s[0].equals("SITDOWN")) {
								flag = Server_Listener.getLandlordTableStatus().joinTable(position, id, msg);
								if (flag) {
									Write_Log.writeActivity("id: " + id,
											ip + "\r\n(Landlord) Sit down. Table " + ((int) position / 4) + ".");
									Server_Listener.sendAction2All(msg, 1);
								}
							} else if (s[0].equals("READY")) {
								flag = Server_Listener.getLandlordTableStatus().ready(position, id, nickname, head, out,
										in, msg);
								if (flag) {
									Write_Log.writeActivity("id: " + id,
											ip + "\r\n(Landlord) Ready. Table " + ((int) position / 4) + ".");
									Server_Listener.sendAction2All(msg, 1);
								}
							} else if (s[0].equals("STANDUP")) {
								flag = Server_Listener.getLandlordTableStatus().leaveTable(position, id);
								if (flag) {
									Write_Log.writeActivity("id: " + id,
											ip + "\r\n(Landlord) Stand up. Table " + ((int) position / 4) + ".");
									Server_Listener.sendAction2All(msg, 1);
								}
							} else if (s[0].equals("START")) {
								flag = Server_Listener.getLandlordTableStatus().start(position, this);
								if (flag) {
									// 移出client列表，防止小桌子更新
									Server_Listener.updateLandlordClients(this, false, 0);
									// 阻塞线程用，synchronized(Object)，防止报错
									synchronized (flag) {
										flag.wait();
									}
									System.out.println(getName() + " 阻塞结束");
								}
							}
						} else if (GOBANG.equals(status)) {
							// 五子棋大厅
							position = Integer.valueOf(s[1]);
							msg = position + "%" + s[0] + "%" + nickname + "%" + head;
							if (s[0].equals("SITDOWN")) {
								flag = Server_Listener.getGobangTableStatus().joinTable(position, id, msg);
								if (flag) {
									Write_Log.writeActivity("id: " + id,
											ip + "\r\n(Gobang) Sit down. Table " + ((int) position / 4) + ".");
									Server_Listener.sendAction2All(msg, 2);
								}
							} else if (s[0].equals("READY")) {
								flag = Server_Listener.getGobangTableStatus().ready(position, id, nickname, head, out,
										in, msg);
								if (flag) {
									Write_Log.writeActivity("id: " + id,
											ip + "\r\n(Gobang) Ready. Table " + ((int) position / 4) + ".");
									Server_Listener.sendAction2All(msg, 2);
								}
							} else if (s[0].equals("STANDUP")) {
								flag = Server_Listener.getGobangTableStatus().leaveTable(position, id);
								if (flag) {
									Write_Log.writeActivity("id: " + id,
											ip + "\r\n(Gobang) Stand up. Table " + ((int) position / 4) + ".");
									Server_Listener.sendAction2All(msg, 2);
								}
							} else if (s[0].equals("START")) {
								flag = Server_Listener.getGobangTableStatus().start(position, this);
								if (flag) {
									// 移出client列表，防止小桌子更新
									Server_Listener.updateGobangClients(this, false, 0);
									// 阻塞线程用，synchronized(Object)，防止报错
									synchronized (flag) {
										flag.wait();
									}
								}
							}
						} else
							this.Close();
					}
				}
			}
		} catch (SocketException | SocketTimeoutException | EOFException se) {
			Write_Log.writeActivity("id: " + id, "Connection closed.");
		} catch (Exception e) {
			Write_Log.writeActivity("id: " + id, e.getMessage());
			Server_Listener.AppendBlack(ip);
		} finally {
			this.Close();
		}
	}

	/**
	 * @param 唤醒线程
	 * @param id
	 *            强退id
	 */
	public void wakeup(int id) {
		try {
			if (id != this.id)
				if (LANDLORD.equals(status)) {
					Server_Listener.updateLandlordClients(this, true, 0);
					send(Server_Listener.getLandlordTableStatus().getStatus());
				} else if (GOBANG.equals(status)) {
					Server_Listener.updateGobangClients(this, true, 0);
					send(Server_Listener.getGobangTableStatus().getStatus());
				}
		} catch (IOException e) {
			Write_Log.writeActivity(this.getName(), "Notify error.\r\n" + e.getMessage());
		}
		synchronized (flag) {
			flag.notify();
		}
	}
}