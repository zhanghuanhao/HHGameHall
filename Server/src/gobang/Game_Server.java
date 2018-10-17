/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gobang;

import java.io.IOException;
import server.Connections;
import server.Server_Listener;
import server.Write_Log;

public class Game_Server extends Thread {
	playinfo info = null;
	volatile int num = 0;
	private boolean over = false;
	private Gobang_Tables.Table table;
	private int QTid = -1;

	public Game_Server(Gobang_Tables.Table table) {
		info = new playinfo();
		this.table = table;
	}

	public void run() {
		try {
			Write_Log.writeActivity(getName(), "Countdown in 3 seconds......");
			sleep(3000);
		for (int i = 0; i < 2; i++) {
			try {
				info.getout(i).reset();
				info.getout(i).writeUTF("START%GOBANG");
				info.getout(i).flush();
			} catch (IOException e) {
			}
		}
		for (;;) {
			if (num == 2)
				break;
			sleep(50);
		}
		} catch (InterruptedException e1) {
		}
		Write_Log.writeActivity(getName(), "Start the game!");
		String temp = null;
		int start = 0;
		prepare();
		try {
			while (true) {
				while (true) {
					temp = info.getin(start).readUTF();
					start = (start + 1) % 2;
					if (temp.equals("over")) {
						temp = info.getin(start).readUTF();
						break;
					}
					send(temp);
				}
				if (!over) {
					int count = 0;
					for (int i = 0; i < 2; i++) {
						temp = info.getin(i).readUTF();
						if (temp.equals("ready"))
							count++;
					}
					if (count == 2) {
						start = 0;
						send("restart");
					} else {
						send("over");
						break;
					}
				} else
					break;
			}
		} catch (Exception e) {
			QTid = info.getid(start);
			send("over");
			over = true;
		} finally {
			Close();
		}
	}

	public void prepare() {
		for (int i = 0; i < 2; i++) {
			try {
				info.getout(i).reset();
				info.getout(i).writeObject(info);
				info.getout(i).flush();
			} catch (IOException e) {
			}
		}
	}

	public void send(String msg) {
		for (int i = 0; i < 2; i++) {
			try {
				info.getout(i).reset();
				info.getout(i).writeUTF(msg);
				info.getout(i).flush();
			} catch (IOException e) {
			}
		}
	}

	public void Close() {
		String msg = null;
		for (int i = 0; i < 2; i++) {
			if (info.getid(i) != QTid) {
				msg = info.getPosition(i) + "%SITDOWN%" + info.getname(i) + "%" + info.gethead(i);
				Server_Listener.getGobangTableStatus().getStatus().replace(info.getPosition(i), msg);
				Server_Listener.sendAction2All(msg, 2);
			}
		}
		for (Object object : table.connections) {
			Connections con = (Connections) object;
			con.wakeup(QTid);
		}
		over = true;
		if (QTid == -1)
			Write_Log.writeActivity(getName(), "Game Over! ");
		else
			Write_Log.writeActivity(getName(), "id: " + QTid + " Stop the game! ");
	}

	public boolean Over() {
		return over;
	}
}
