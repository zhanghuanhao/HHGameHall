/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package landlord;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class Players implements Serializable{

   private static final long serialVersionUID = 1L;
   private int size=0;
   public class info implements Serializable{
	   private static final long serialVersionUID=1L;
	   public int id=-1;
	   public String playername=null;
       public String playerhead=null;
       public transient ObjectOutputStream output=null;
       public transient ObjectInputStream input=null;
   }
   public info[] playerinfo=null;
   public Poker [] pokers=null;
   public Poker dzpoker=null;
   public int dz=-1;//地主编号
   public int round=0;//轮数
   public Poker lastpoker=new Poker(0);//上轮出的牌
   public int temps=0;//当前打出牌数量
   public Poker temppoker=null;//当前打出的牌
   public boolean []qdz=null;//抢地主
   public int over=0;
public Players (){
		size=0;
		qdz=new boolean[3];
		playerinfo=new info[3];
		pokers=new Poker[3];//三套各17张牌
		for(int i=0;i<3;i++) {
			playerinfo[i]=new info();
			pokers[i]=new Poker(17);
			qdz[i]=false;
		}
		dzpoker=new Poker(3);//地主3张牌
		temppoker=new Poker(0);
	}
//添加玩家
public void addplayer(int id,String name,String head, ObjectOutputStream out,ObjectInputStream in) {
	playerinfo[size].id=id;
	playerinfo[size].playername=name;System.out.println("Players:"+name);
	playerinfo[size].playerhead=head;
	playerinfo[size].output=out;
	playerinfo[size].input=in;
	size++;
}
//获取账号
public int getplayid(int num) {
	return playerinfo[num].id;
}
//获取昵称
public String getplayername(int num) {
	return playerinfo[num].playername;
}
//获取头像
public String getplayerhead(int num) {
	return playerinfo[num].playerhead;
}
//获取连接
public ObjectOutputStream getclientput(int num) {
	return playerinfo[num].output;
}
public ObjectInputStream getinput(int num) {
	return playerinfo[num].input;
}
//获取人数
public int getnum() {
	return size;
}
//删除玩家
public void reduce(ObjectOutputStream soc) {
	for(int i=0;i<size;i++) {
		if(playerinfo[i].output.equals(soc)) {
			playerinfo[i]=null;
			for(int t=i+1;t<size;t++) {
				playerinfo[t-1]=playerinfo[t];
			}
			size--;
			break;
		}
	}
}
public void adddz() {
	pokers[dz].add(dzpoker);
}
//出的牌
public void addtemp(int play) {
	Poker newtemp=new Poker(temps+1);
	int []pok=new int [temps+1];
	for(int i=0;i<temps;i++) {
		pok[i]=temppoker.cardrecord(i);
	}
	pok[temps]=play;
	Arrays.sort(pok);
	temps++;
	for(int i=0;i<temps;i++) {
		newtemp.addcard(i, pok[i]);
	}
	temppoker=newtemp;
}
//取消出牌
public void reduce(int play) {
	Poker newtemp=new Poker(temps-1);
	int []pok=new int [temps-1];
	for(int i=0,t=0;i<temps;i++) {
		if(play!=temppoker.cardrecord(i))
		pok[t++]=temppoker.cardrecord(i);
	}
	Arrays.sort(pok);
	temps--;
	for(int i=0;i<temps;i++) {
		newtemp.addcard(i, pok[i]);
	}
	temppoker=newtemp;
}//

}
