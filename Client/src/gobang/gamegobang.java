/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gobang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import account.Account;
import mainhall.Connect;

public class gamegobang extends Thread{
	private ObjectInputStream ojin=null;
	private ObjectOutputStream ojout=null;
	private Account ac=null;
	private Connect connect=null;
	private playinfo info=null;
	private int pos=-1;
	private StartChessJFrame chess=null;
	
	public gamegobang(Account ac,ObjectInputStream in,ObjectOutputStream out,Connect c) {
		this.ac=ac;this.ojin=in;this.ojout=out;this.connect=c;
		prepare();
		start();		
	}

	public void run() {
		String temp=null;
		while(true) {
			try {
				temp=ojin.readUTF();
				if(!temp.equals("over")) {
				chess.getpaint(temp);
				}
				else {
					chess.over();
					connect.wakeup();
					break;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void prepare() {	
			try {
				info=(playinfo)ojin.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		if(ac.id==info.getid(0))pos=0;
		else pos=1;
		chess=new StartChessJFrame(this);
        chess.setVisible(true);
	}
	public int getpos() {
		return pos;
	}
	public playinfo getinfo() {
		return info;
	}
	public void send(String msg) {
		try {
			
			ojout.reset();
			ojout.writeUTF(msg);
			ojout.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
