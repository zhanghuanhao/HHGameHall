/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package mainhall;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import account.Account;
import gobang.gamegobang;
import landlord.gamelandlord;

public class Connect extends Thread{
	private ObjectOutputStream  ojout=null;
	private ObjectInputStream ojin=null;
	private Account ac=null;
	private GameLobby gamelobby=null;
	private String refresh=null;;
 public Connect(ObjectOutputStream out,ObjectInputStream in,Account ac,GameLobby gameLobby) throws IOException {
	 this.ojout=out;this.ojin=in;
	 this.ac=ac;this.gamelobby=gameLobby;
	 start();
 }
 
 @SuppressWarnings("unchecked")
public void run() {
	
	 //刷新大厅情况
	
		try {
			String temp=null;
			String ntf=ojin.readUTF();	
			gamelobby.showntf(ntf);			
			temp=ojin.readUTF();		
		    Hashtable<Integer,String> ts=(Hashtable<Integer,String>)ojin.readObject();
			for(int i=0;i<396;i++) {
				temp=ts.get(i);
				if(temp!=null) {
					gamelobby.paint(temp);
				}
			}		
			
		while(true) {
			refresh=ojin.readUTF();
			if(refresh.equals("START%LANDLORD")) { //启动斗地主游戏后台
				ojout.reset();
			    ojout.writeUTF("START%"+gamelobby.Sit());   
			    ojout.flush();
				gamelobby.setVisible(false);
				new gamelandlord(ac,ojin,ojout,this);
				synchronized(refresh) {refresh.wait();}
				
				ts=(Hashtable<Integer,String>)ojin.readObject();
				gamelobby.setVisible(true);
				for(int i=0;i<396;i++) {
					temp=ts.get(i);
					if(temp!=null) {
						gamelobby.paint(temp);
					}
				}
			
			}
			else if(refresh.equals("START%GOBANG")) {//启动五子棋游戏后台
				ojout.reset();
			    ojout.writeUTF("START%"+gamelobby.Sit());   
			    ojout.flush();
			    gamelobby.setVisible(false);
				new gamegobang(ac,ojin,ojout,this);
				
	            synchronized(refresh) {refresh.wait();}
				
				ts=(Hashtable<Integer,String>)ojin.readObject();
				gamelobby.setVisible(true);
				for(int i=0;i<396;i++) {
					temp=ts.get(i);
					if(temp!=null) {
						gamelobby.paint(temp);
					}
				}
			}
			else if(refresh.equals("GOBANG")||refresh.equals("LANDLORD")) {
				ts=(Hashtable<Integer,String>)ojin.readObject();
				for(int i=0;i<396;i++) {
					temp=ts.get(i);
					if(temp!=null) {
						gamelobby.paint(temp);
					}
				}
			}
			else if(refresh.equals("CONFIRM")) {
				gamelobby.per.showconfirm();
			}
			
			else { //更新桌子
				gamelobby.paint(refresh);
			}
			}
		}
			catch (ClassNotFoundException | IOException | InterruptedException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "与服务器断开连接，请重新登录！", "提示", JOptionPane.OK_OPTION);
		}
	
 }	
 //唤醒
 public void wakeup(){
		synchronized(refresh)
		{
			refresh.notify();
			gamelobby.setVisible(true);
		}
	 
			 }
 	 

 //坐下
 public void sit(String num) throws IOException {
	ojout.reset();
	ojout.writeUTF("SITDOWN%"+num);
	ojout.flush();
 }
 //站立
 public void stand(String num) throws IOException {
	ojout.reset();
	ojout.writeUTF("STANDUP%"+num);
	ojout.flush();
 }

//准备
 public void ready(String num) throws IOException {
	 ojout.reset();
	 ojout.writeUTF("READY%"+num);
	 ojout.flush();
 }
 //游戏名字
 public void send(String game) throws IOException {
	 ojout.reset();
	 ojout.writeUTF(game);
	 ojout.flush();
 }
 //修改信息
 public void change(String head,String name) throws IOException {
	 ojout.reset();
	 ojout.writeUTF("CHANGE%"+name+"%"+head);
	 ojout.flush();
	 gamelobby.change(head, name);
 }
 
 
}
