/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package server;

/**
 * @author Zhh
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

public class Msg_Deliver extends Thread {

	/**
	 * 内部类用来储存客户信息
	 */
	private class Client_Info {
		InetAddress ip;
		int port;
		int id;

		public Client_Info(InetAddress ip, int port, int id) {
			this.ip = ip;
			this.port = port;
			this.id = id;
		}

		/**
		 * 重写equals用来移除列表中的在线人数
		 */
		@Override
		public boolean equals(Object c) {
			Client_Info x = (Client_Info) c;
			return id == x.id;
		}
	}

	/**
	 * @param 最大接收字节长度
	 */
	private static final int MAX_LENGTH = 8192;
	/**
	 * @param 大厅消息连接端口号
	 */
	private static final int PORT_NUM = 43838;
	/**
	 * @param 用以存放接收数据的字节数组
	 */
	private byte[] receMsgs = new byte[MAX_LENGTH];
	/**
	 * @param 数据报套接字
	 */
	private DatagramSocket datagramSocket;
	/**
	 * @param 用以接收数据报
	 */
	private DatagramPacket datagramPacket;
	/**
	 * @param 登陆String
	 */
	private static final String LOGIN = "LOGIN";
	/**
	 * @param 心跳包
	 */
	private static final String HEARTBEAT = "^w^";
	/**
	 * @param 在线列表
	 */
	private Vector<Client_Info> clients;

	public void Shutdown() {
		/** 关闭socket */
		if (datagramSocket != null) {
			datagramSocket.close();
			if (clients != null)
				clients.clear();
			Write_Log.writeActivity(this.getName(), "Mainhall UDP closed.");
		}
	}

	public void Append(InetAddress Address, int PORT, int id) {
		Client_Info client = new Client_Info(Address, PORT, id);
		if (!clients.contains(client)) {
			clients.add(client);
			Write_Log.writeActivity(getName(), "After append id " + id + ". Online: " + clients.size());
		}
	}

	public void Remove(int id) {
		Client_Info client = new Client_Info(null, 0, id);
		if (clients.remove(client))
			Write_Log.writeActivity(getName(), "After remove id " + client.id + ". Online: " + clients.size());
	}

	public Msg_Deliver() {
		clients = new Vector<Client_Info>(50);
		this.setName("Msg_deliver");
		this.start();
	}

	public void sendMsg(byte[] buf, int length) {
		DatagramPacket sendPacket = null;
		for (Client_Info client : clients) {
			try {
				sendPacket = new DatagramPacket(buf, length, client.ip, client.port);
				datagramSocket.send(sendPacket);
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		try {
			datagramSocket = new DatagramSocket(PORT_NUM);
			Write_Log.writeActivity(this.getName(), "Mainhall UDP opened.");
			String ip = null;
			String receStr = null;
			while (true) {
				try {
					receStr = null;
					receMsgs = new byte[MAX_LENGTH];
					datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
					datagramSocket.receive(datagramPacket);
					/****** 解析数据报,必须有长度限制 ****/
					byte[] buf = datagramPacket.getData();
					int length = datagramPacket.getLength();
					receStr = new String(buf, 0, length, "utf-8");
					ip = datagramPacket.getAddress().getHostAddress();
					Write_Log.Write_Chat(receStr, ip);	
					String idstr = receStr.substring(4, receStr.indexOf("$]"));
					int id = Integer.valueOf(idstr);
					receStr = receStr.substring(receStr.indexOf("$]") + 2);
					if (HEARTBEAT.equals(receStr)) {
						continue;
					} else if (LOGIN.equals(receStr)) {
						this.Append(datagramPacket.getAddress(), datagramPacket.getPort(), id);
						continue;
					} else {
						/** 转发消息 */
						buf = receStr.getBytes("utf-8");
						length = buf.length;
						sendMsg(buf, length);
						Server_Listener.showChatContent("IP: " + ip + "\t Id: " + idstr + "\r\n", receStr);
					}
				} catch (StringIndexOutOfBoundsException | NumberFormatException | NullPointerException sse) {
					Write_Log.writeActivity(this.getName(),
							"Wrong message received from \r\nIP: " + ip + "\r\n" + sse.getMessage());
					continue;
				} catch (SocketException se) {
					break;
				}
			}
		} catch (Exception e) {
			Write_Log.writeActivity(this.getName(), e.toString());
		} finally {
			this.Shutdown();
		}
	}

	public static void send(String msg) {
		try {
			@SuppressWarnings("resource")
			DatagramSocket datagramSocket = new DatagramSocket();
			msg = "[id:888888$][head:1.png$]管理员：" + msg;
			byte[] buf = msg.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, InetAddress.getByName("127.0.0.1"), 43838);
			datagramSocket.send(sendPacket);
		} catch (Exception ee) {
			Write_Log.writeActivity("Send: ", ee.toString());
		}
	}
}