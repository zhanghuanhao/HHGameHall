/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package landlord;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Poker implements Serializable{
private class card implements Serializable{
	public int record=0;//编号
	public boolean sended=false;//是否已发
	public int serecord=0;
}
public card[] cards=null;
public int num=0;//牌数
public int remain=0;//剩下的牌
public int pass=0;//被pass次数
public int type=0;

	public Poker(int number) {
		this.num=number;
		cards=new card[this.num];
		for(int i=0;i<this.num;i++) {
			cards[i]=new card();
		}
	}
	//添加牌
	public boolean addcard(int num,int record) {
		cards[num].record=record;
		if(record==0||record==1) {
			cards[num].serecord=record;
		}
		else {
			cards[num].serecord=((record-2)/4)+2;
		}
		remain++;
		if(remain<num)return true;//判断是否满牌
		else return false;
	}
	public int cardrecord(int n) {
		return cards[n].record;
	}
	public int serecord(int n) {
		return cards[n].serecord;
	}
	public void send(int n) {
		cards[n].sended=true;
		remain--;
		System.out.println("remain:"+remain);
	}
	public boolean ifsend(int n) {
		return cards[n].sended;
	}
	public void add(Poker dz) {
		remain+=3;num+=3;
		card[] newcards=new card[20];int []temp=new int[20];
		for(int i=0;i<17;i++) {temp[i]=cards[i].record;}
		temp[17]=dz.cards[0].record;temp[18]=dz.cards[1].record;temp[19]=dz.cards[2].record;
		Arrays.sort(temp);
		for(int i=0;i<20;i++) {
			newcards[i]=new card();
			newcards[i].record=temp[i];
			}
		cards=newcards;
	}
}
