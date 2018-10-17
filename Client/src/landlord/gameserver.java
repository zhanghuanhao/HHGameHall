/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package landlord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


//一个房间游戏线程,负责发牌和游戏判定
public class gameserver extends Thread {
@SuppressWarnings("rawtypes")
private List pok=null;
@SuppressWarnings("rawtypes")
private Iterator ite=null;
private Players players=null;
private int start=0;
private boolean first=true;
private boolean out=false;

	public gameserver(Players playlist) {
		players=playlist;
		prepare();
	    start();
	}
	

	public void run() {
		while(true) {
		while(true) {//接收，转发
			try {
				Players p=(Players)players.getinput(start).readObject();
				if(p!=null) {
			      if(p.over==1) {
					 start=(start+1)%3;
					 p=(Players)players.getinput(start).readObject();
					 start=(start+1)%3;
					 p=(Players)players.getinput(start).readObject();
					 break;
					}
				players.pokers=p.pokers;players.temppoker=p.temppoker;players.lastpoker=p.lastpoker;
				start=(start+1)%3;
				this.players.round=start;
				send();
				}
				else {
					players.over=-1;
					start=(start+1)%3;
					this.players.round=start;
					out=true;
					send();
					break;
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();break;
			}
		}
		
		//强退
		if(out) {
			//添加房间解散
			
			break;
			}
		for(int i=0;i<3;i++) {//收集准备情况
			try {	
				players.qdz[i]=((Players)players.getinput(i).readObject()).qdz[i];
				players.round=i+1;
				send();//反馈准备情况
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}	
		if(!(players.qdz[0]&&players.qdz[1]&&players.qdz[2])) {
			//添加房间解散
			
			break;
			}
		
		
		Players p=new Players();
		p.playerinfo=players.playerinfo;
	    players=p;
		prepare();
		}
	}

	//准备工作
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void prepare( ) {
		
		pok=new ArrayList();
		
		for(int i=0;i<54;i++) {
			pok.add(i);
		}
		Collections.shuffle(pok);//随机洗牌
		ite=pok.iterator();
		int [][]po=new int[3][];
		for(int i=0;i<3;i++) { po[i]=new int[17];}
		for(int t=0;t<17;t++) {//取号
			po[0][t]= (int)ite.next();	
			po[1][t]= (int)ite.next();	
			po[2][t]= (int)ite.next();	
			}
		for(int i=0;i<3;i++) {
			Arrays.sort(po[i]);
			for(int t=0;t<17;t++) {//输入牌
	            players.pokers[i].addcard(t,po[i][t]);
			}
		}
		int []dzpo=new int[3];
		for(int i=0;i<3;i++)dzpo[i]=(int)ite.next();
		Arrays.sort(dzpo);
		for(int i=0;i<3;i++)players.dzpoker.addcard(i,dzpo[i]);
		for(int i=0;i<3;i++) {
			try {		
				if(first) {
					//players.getclientput(i).writeObject(new Tables(-1));//发送启动信号	
				}
				players.getclientput(i).writeObject(players);//发牌
				players.getclientput(i).flush();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		first=false;
		for(int i=0;i<3;i++) {//收集抢地主情况
			try {	
				players.qdz[i]=((Players)players.getinput(i).readObject()).qdz[i];
				players.round=i+1;
				send();//反馈抢地主情况
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}	
		
		if(players.qdz[0]==true&&players.qdz[1]==true&&players.qdz[2]==true||
				players.qdz[0]==false&&players.qdz[1]==false&&players.qdz[2]==false) {
			players.dz=(int)(Math.random()*100)%3;//随机一个	
		}
		else if(players.qdz[0]==true&&players.qdz[1]==false&&players.qdz[2]==false) {
			players.dz=0;
		}
		else if(players.qdz[0]==false&&players.qdz[1]==true&&players.qdz[2]==false) {
			players.dz=1;
		}
		else if(players.qdz[0]==false&&players.qdz[1]==false&&players.qdz[2]==true) {
			players.dz=2;
		}
		else if(players.qdz[0]==true&&players.qdz[1]==true&&players.qdz[2]==false) {
			players.dz=(int)(Math.random()*100)%2;
		}
		else if(players.qdz[0]==true&&players.qdz[1]==false&&players.qdz[2]==true) {
			players.dz=((int)(Math.random()*100)%2)*2;
		}
		else if(players.qdz[0]==false&&players.qdz[1]==true&&players.qdz[2]==true) {
			players.dz=(int)(Math.random()*100)%2+1;
		}
	    players.round=players.dz;
	    players.adddz();
		send();
		
		start=players.dz;
		
	}
	//发送
	public void send() {
		for(int i=0;i<3;i++) {
			try {
				players.getclientput(i).reset();//必须运行
				players.getclientput(i).writeObject(players);	
				players.getclientput(i).flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
