/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gobang;

import java.io.IOException;

public class gameserver extends Thread{
private playinfo info=null;
	
	public gameserver(playinfo p) {
		info=p;
		prepare();
		start();
	}
	
	public void run() {
		String temp=null;int start=0;
		try {
		while(true) {
			
				while(true) {
				temp=info.getin(start).readUTF();
				start=(start+1)%2;
				if(temp.equals("over")) {
					temp=info.getin(start).readUTF();
					break;
				}
			
				send(temp);
				}
				
				int count=0;
				for(int i=0;i<2;i++) {
					temp=info.getin(i).readUTF();
					if(temp.equals("ready"))count++;
				}
				if(count==2) {
					start=0;
					send("restart");
				}
				else {
					send("over");
					//出口
									  
					break;
					}
			}
			
			
			
		}catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	public void prepare() {	
		try {
			for(int i=0;i<2;i++) {
				info.getout(i).reset();
			    info.getout(i).writeObject(info);
			    info.getout(i).flush();
			    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void send(String msg) {
		try {
			for(int i=0;i<2;i++) {
				info.getout(i).reset();
			    info.getout(i).writeUTF(msg);
			    info.getout(i).flush();
			    }
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
