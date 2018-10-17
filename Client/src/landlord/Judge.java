/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package landlord;

public class Judge {
private Poker lastpoker=null;
private Poker temppoker=null;
	public boolean can(Poker last,Poker temp) {
		
		lastpoker=last;
		temppoker=temp;
		
		switch(lastpoker.remain) {
		case 0:{
			if(temppoker.remain<5) {
				return (single()||doublepok()||third()||three_one()||boom());
			}
			else return(line()||three_two()||four_two()||douline()||four_two_two()||fly());
		}
		case 1:return single()||boom();
		case 2:return doublepok()||boom();
		case 3:return third()||boom();
		case 4:return (boom()||three_one());
		case 5:return (line()||three_two()||boom());
		case 6:return (line()||four_two()||douline()||fly()||boom());
		case 7:return line()||boom();
		case 8:return (line()||four_two_two()||douline()||fly()||boom());
		case 10:return (line()||douline()||fly()||boom());
		default:return line()||douline()||boom();
		}
		
		}
		
//牌类状态type说明：1：王炸，2：普通炸弹， 3：3带1， 4：3带2，5：顺子，6：四带二，7：飞机不带，8：飞机带单，9：飞机带对，10：四带二对，11：连对
	public boolean single() {//单牌
	if(temppoker.remain==1) {
		if(lastpoker.remain==0)return true;
		if(temppoker.serecord(0)<lastpoker.serecord(0))return true;
		return false;
	}
	else return false;
	}
	public boolean doublepok() {//一对
		if(lastpoker.type==1)return false;
		if(temppoker.remain==2) {
			if(temppoker.serecord(0)==temppoker.serecord(1)) {
				if(lastpoker.remain==0||temppoker.serecord(0)<lastpoker.serecord(0))return true;
				return false;
			}
			return false;
		}
		else return false;
	}
	public boolean third() {//三个
		if(temppoker.remain==3) {
			if(temppoker.serecord(0)==temppoker.serecord(1)&&temppoker.serecord(1)==temppoker.serecord(2)) {
				if(lastpoker.remain==0||temppoker.serecord(0)<lastpoker.serecord(0))return true;
				return false;
			}
			return false;
		}
		else return false;
	}

	public boolean boom() {//炸弹
		if(temppoker.remain==0)return false;
		if(temppoker.serecord(0)==0&&temppoker.serecord(1)==1) {
			temppoker.type=1;
			return true;
			}
		if(lastpoker.type!=1&&temppoker.remain==4) {
			if(temppoker.serecord(0)==temppoker.serecord(1)&&temppoker.serecord(1)==temppoker.serecord(2)
					&&temppoker.serecord(2)==temppoker.serecord(3)) {
				if(lastpoker.remain==0||lastpoker.remain==1||lastpoker.remain==2)return true;
				if(lastpoker.type>=3&&lastpoker.type<=11)return true;
				if(temppoker.serecord(0)<lastpoker.serecord(0)&&lastpoker.serecord(0)==lastpoker.serecord(3))return true;
				return false;
			}
			return false;
		}
		else return false;
	}
	public boolean three_one() {//三带一
		if((lastpoker.type==3||lastpoker.remain==0)&&temppoker.remain==4) {
			if(temppoker.serecord(0)==temppoker.serecord(2)&&temppoker.serecord(2)!=temppoker.serecord(3)
					||temppoker.serecord(1)==temppoker.serecord(3)&&temppoker.serecord(2)!=temppoker.serecord(0)) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=3;
					return true;
				}
				return false;
			}
			return false;
		}
		else return false; 
	}
	public boolean three_two() {//三带二
		if((lastpoker.type==4||lastpoker.remain==0)&&temppoker.remain==5) {
			if(temppoker.serecord(0)==temppoker.serecord(1)&&temppoker.serecord(3)==temppoker.serecord(4)
					&&temppoker.serecord(1)!=temppoker.serecord(3)&&(temppoker.serecord(2)==temppoker.serecord(1)||
					temppoker.serecord(2)==temppoker.serecord(3))) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=4;
					return true;
				}
				return false;
			}
			return false;
		}
		else return false; 
	}
	
	public boolean line() {//顺子
		if(temppoker.remain<5)return false;
		for(int i=0,t=temppoker.serecord(0);i<temppoker.remain;i++,t++) {
			if(temppoker.serecord(i)!=t||temppoker.serecord(i)==0
		||temppoker.serecord(i)==1||temppoker.serecord(i)==2)return false;
		}
		if(lastpoker.remain==0) {
			temppoker.type=5;
			return true;
		}
		if(lastpoker.type==5&&temppoker.remain==lastpoker.remain) {
			if(temppoker.serecord(0)<lastpoker.serecord(0)) {
		      temppoker.type=5;
		      return true;
		      }
		}
		return false;
	}
	public boolean douline() {//连对
		if(temppoker.remain<6||temppoker.remain%2!=0)return false;
		for(int i=0,t=temppoker.serecord(0);i<temppoker.remain;i=i+2,t++) {
			if(temppoker.serecord(i)!=t||temppoker.serecord(i)==0
		||temppoker.serecord(i)==1||temppoker.serecord(i)==2)return false;
		}
		if(lastpoker.remain==0) {
			temppoker.type=11;
			return true;
		}
		if(lastpoker.type==11&&temppoker.remain==lastpoker.remain) {
			if(temppoker.serecord(0)<lastpoker.serecord(0)) {
		      temppoker.type=11;
		      return true;
		      }
		}
		return false;
	}
	
	public boolean four_two() {//四带二
		if((lastpoker.type==6||lastpoker.remain==0)&&temppoker.remain==6) {
			if(temppoker.serecord(0)==temppoker.serecord(3)||temppoker.serecord(1)==temppoker.serecord(4)
					||temppoker.serecord(2)==temppoker.serecord(5)) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=6;
					return true;
				}
				return false;
			}
			return false;
		}
		else return false; 
	}
	
	public boolean four_two_two() {//四带二对
		if((lastpoker.type==10||lastpoker.remain==0)&&temppoker.remain==8) {
			if(temppoker.serecord(0)==temppoker.serecord(3)) {
				if(temppoker.serecord(4)==temppoker.serecord(5)&&temppoker.serecord(6)==temppoker.serecord(7)) {
					if(lastpoker.remain==0||temppoker.serecord(0)<lastpoker.serecord(0)) {
						temppoker.type=10;
						return true;
						}
				}
			}
			if(temppoker.serecord(2)==temppoker.serecord(5)) {
                if(temppoker.serecord(0)==temppoker.serecord(1)&&temppoker.serecord(6)==temppoker.serecord(7)) {
                	if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
                		temppoker.type=10;
                		return true;
                		}
				}
			}
			if(temppoker.serecord(4)==temppoker.serecord(7)) {
                if(temppoker.serecord(0)==temppoker.serecord(1)&&temppoker.serecord(2)==temppoker.serecord(3)) {
                	if(lastpoker.remain==0||temppoker.serecord(4)<lastpoker.serecord(4)) {
                		temppoker.type=10;
                		return true;
                	}
				}
			}
			return false;
					}
		else return false; 
	}
	
	public boolean fly() {//飞机
/*不带*/	if((lastpoker.type==7||lastpoker.remain==0)&&temppoker.remain==6) {
			if(temppoker.serecord(0)==temppoker.serecord(2)&&temppoker.serecord(3)==temppoker.serecord(5)) {
				if(lastpoker.remain==0||temppoker.serecord(0)<lastpoker.serecord(0)) {
					temppoker.type=7;
					return true;
				}
			}
		}
/*带单*/	if((lastpoker.type==8||lastpoker.remain==0)&&temppoker.remain==8) {
			if(temppoker.serecord(0)==temppoker.serecord(2)) { //8.1
				if(temppoker.serecord(3)==temppoker.serecord(5)) {
					if(temppoker.serecord(6)!=temppoker.serecord(7)&&temppoker.serecord(6)!=temppoker.serecord(5)) {
						if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
							temppoker.type=8;
							return true;
						}
					}
				}
				if(temppoker.serecord(4)==temppoker.serecord(6)) {
					if(temppoker.serecord(3)!=temppoker.serecord(2)&&temppoker.serecord(7)!=temppoker.serecord(6)) {
						if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
							temppoker.type=8;
							return true;
						}
					}
				}
				if(temppoker.serecord(5)==temppoker.serecord(7)) {
					if(temppoker.serecord(3)!=temppoker.serecord(2)&&temppoker.serecord(4)!=temppoker.serecord(5)
							&&temppoker.serecord(3)!=temppoker.serecord(4)) {
						if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
							temppoker.type=8;
							return true;
						}
					}
				}
				return false;
			}//8.1
			
			if(temppoker.serecord(1)==temppoker.serecord(3)) { //8.2
				if(temppoker.serecord(4)==temppoker.serecord(6)) {
					if(temppoker.serecord(0)!=temppoker.serecord(1)&&temppoker.serecord(7)!=temppoker.serecord(6)) {
						if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
							temppoker.type=8;
							return true;
						}
					}
				}
				if(temppoker.serecord(5)==temppoker.serecord(7)) {
					if(temppoker.serecord(0)!=temppoker.serecord(4)&&temppoker.serecord(4)!=temppoker.serecord(5)
							&&temppoker.serecord(3)!=temppoker.serecord(4)) {
						if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
							temppoker.type=8;
							return true;
						}
					}
				}
				return false;
			}//8.2
			
			if(temppoker.serecord(2)==temppoker.serecord(4)) { //8.3
				if(temppoker.serecord(5)==temppoker.serecord(7)) {
					if(temppoker.serecord(0)!=temppoker.serecord(1)&&temppoker.serecord(1)!=temppoker.serecord(2)) {
						if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
							temppoker.type=8;
							return true;
						}
					}
				}
				return false;
			}//8.3
			return false;
		}
/*带对*/	if((lastpoker.type==9||lastpoker.remain==0)&&temppoker.remain==10){
	
	if(temppoker.serecord(0)==temppoker.serecord(2)) { //9.1
		if(temppoker.serecord(3)==temppoker.serecord(5)) {
			if(temppoker.serecord(6)!=temppoker.serecord(8)&&temppoker.serecord(6)!=temppoker.serecord(5)
					&&temppoker.serecord(6)==temppoker.serecord(7)&&temppoker.serecord(8)==temppoker.serecord(9)) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=9;
					return true;
				}
			}
		}
		if(temppoker.serecord(5)==temppoker.serecord(7)) {
			if(temppoker.serecord(3)!=temppoker.serecord(2)&&temppoker.serecord(8)!=temppoker.serecord(7)
					&&temppoker.serecord(3)==temppoker.serecord(4)&&temppoker.serecord(8)==temppoker.serecord(9)) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=9;
					return true;
				}
			}
		}
		if(temppoker.serecord(7)==temppoker.serecord(9)) {
			if(temppoker.serecord(4)!=temppoker.serecord(5)&&
					temppoker.serecord(3)==temppoker.serecord(4)&&temppoker.serecord(5)==temppoker.serecord(6)) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=9;
					return true;
				}
			}
		}
		return false;
	}//9.1
	
	if(temppoker.serecord(2)==temppoker.serecord(4)) { //9.2
		if(temppoker.serecord(5)==temppoker.serecord(7)) {
			if(temppoker.serecord(0)==temppoker.serecord(1)&&temppoker.serecord(8)==temppoker.serecord(9)) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=9;
					return true;
				}
			}
		}
		if(temppoker.serecord(7)==temppoker.serecord(9)) {
			if(temppoker.serecord(0)==temppoker.serecord(1)&&temppoker.serecord(5)==temppoker.serecord(6)) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=9;
					return true;
				}
			}
		}
		return false;
	}//9.2
	
	if(temppoker.serecord(4)==temppoker.serecord(6)) { //9.3
		if(temppoker.serecord(7)==temppoker.serecord(9)) {
			if(temppoker.serecord(1)!=temppoker.serecord(2)&&
					temppoker.serecord(0)==temppoker.serecord(1)&&temppoker.serecord(2)==temppoker.serecord(3)) {
				if(lastpoker.remain==0||temppoker.serecord(2)<lastpoker.serecord(2)) {
					temppoker.type=9;
					return true;
				}
			}
		}
		return false;
	}//9.3
		
		}
		return false;
	}
	public int reset() {//更新待发送牌组
		return temppoker.type;
	}

}
