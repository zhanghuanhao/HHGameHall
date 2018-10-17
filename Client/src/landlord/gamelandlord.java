/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package landlord;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import account.Account;
import mainhall.Connect;
//游戏线程
public class gamelandlord extends Thread{
private int playnum=-1;	
private ObjectInputStream ojin=null;
private ObjectOutputStream ojout=null;
private Players players;
private Account ac=null;
private Maingaming main=null;
private boolean first=true;
private Judge judge=new Judge();
private Timer time=null;
private int curSec;
private Connect connect=null;


public gamelandlord(Account ac,ObjectInputStream in,ObjectOutputStream out,Connect c)  {
		this.ac=ac;this.ojin=in;this.ojout=out;this.connect=c;
		time = new Timer(); 
		prepare();
	    start(); 
}
	

	public void run() {
		while(true) {
		boolean flag=true;
	    while(flag) {//接收
		try {
			players=(Players)ojin.readObject();
			if(players.lastpoker.pass==2)players.lastpoker=new Poker(0);
			main.repaint();
			curSec=30;
			for(int i=0;i<3;i++) {
				if(players.pokers[i].remain==0) {
					main.over(i);
					players.over=1;
					send();
					curSec=999999;
					flag=false;
				    break;
				    }
				if(players.over==-1) {
					main.out();
					time.cancel();time=null;
					flag=false;connect.wakeup();
				    break;
				    }
			}
					
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	    }
	  
	  //强退
	  if(players.over==-1)break;
	   //下一局准备情况
	  
	   try {//接收玩家准备情况
		   
			for(int i=0;i<3;i++) {
			players=(Players)ojin.readObject();
			main.showready(players);
			}
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	  
	   if(!(players.qdz[0]&&players.qdz[1]&&players.qdz[2])) {
		   main.out();
		   connect.wakeup();
		   break;
		   }
	      
	  prepare();	
	  } 
	}

	//准备工作
	 public void prepare() {
		
		 try {  
				players=(Players)ojin.readObject();//收到第一次
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		if(first) {
		for(int i=0;i<3;i++) {//第一次启动要账号信息
			if(ac.id==players.getplayid(i)) {
				playnum=i;break;
			}
		}
		main=new Maingaming(players,playnum,this);
		main.showqdz(players);
		}
		else {
			main.regame();
			main.showqdz(players);
			main.repaint();
			}
		
	
		first=false;
		try {//接收抢地主玩家情况
			for(int i=0;i<3;i++) {
			players=(Players)ojin.readObject();
			main.showqdz(players);
			}
			players=(Players)ojin.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		main.paintdz(players.dz);
		
		 
		 curSec=30;
		 time.schedule(new TimerTask(){  
		        public void run(){  
		            main.flushtime(curSec--);
		        }  
		    },0,1000);  
		 

	}
	 public Players get() {
		return players;
	 }
	 //抢地主
	 public void qdz(boolean f) {
		 players.qdz[playnum]=f;System.out.println("抢地主");
		 send();
	 }
	 //暂出牌
	 public void addtemp(int cardnum) {
		 players.addtemp(cardnum);	 
	 }
	 //取消出牌
	 public void reducetemp(int cardnum) {
		 players.reduce(cardnum);
	 }
	 //判断函数
	 public void check() {
		 if(judge.can(players.lastpoker,players.temppoker)) {
			players.temppoker.type=judge.reset();	
			main.ifcansend(true);
			 }
		 else main.ifcansend(false);
	 }
	  
	 //成功出牌
	 public void presend() {
		players=main.players;
		players.lastpoker=players.temppoker;
		 for(int i=0;i<players.temppoker.remain;i++) {
			 for(int t=0;t<players.pokers[playnum].num;t++) {
				 if(players.temppoker.cardrecord(i)==players.pokers[playnum].cardrecord(t))
					 players.pokers[playnum].send(t);
			 }
		 }
		 players.temppoker=null;
	 }
	 //发送
     public void send() {
    	 players=main.players;
    	 try {
    		ojout.reset();//必须运行
    		ojout.writeObject(players);
			ojout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
     }
     //计时器重置
     public void del() {
    	 curSec=30;
    	 main.flushtime(30);
     }
     
     
}
