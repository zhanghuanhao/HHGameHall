/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package mainhall;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

public class Chat extends Thread{
	private int PORT=43838;
	private DatagramSocket dsocket = null;
	private static String hostname="111.230.38.149";//"127.0.0.1";//"125.216.246.62";//"192.168.43.153";////"172.20.10.11";//
	private InetAddress host=InetAddress.getByName(hostname);
	private String id=null;
	private Timer time=null;
	public Chat(String Id)throws IOException {
		id=Id;
		start();
		time=new Timer(false);
		time.schedule(new TimerTask() {
			public void run()
			{
				try {
					send("[id:"+id+"$]"+"^w^");
				} catch (IOException e) {
	      		 e.printStackTrace();
				}
			}
		}, 10000,10000);
	}
		
public void run() {
	
	try {
		dsocket=new DatagramSocket();
	} catch (IOException e) {
		e.printStackTrace();
	}
	dsocket.connect(host, PORT);
	try {
		this.send("[id:"+id+"$]"+"LOGIN");
	} catch (IOException e) {
		e.printStackTrace();
	}
	              
        while(true){
        	try{  
            byte[] data = new byte[8192];  
            DatagramPacket DP = new DatagramPacket(data,data.length);                 
            dsocket.receive(DP);               
            String str = new String(DP.getData(),"UTF-8"); 
            GameLobby.show(str);
            }
            catch(Exception ex){  
            	ex.printStackTrace();
            	break;
    }  	         
    }
        close();
}
//发送聊天
public void send(String msg) throws IOException {
	 byte[] data = msg.getBytes("UTF-8");  
     DatagramPacket dp = new DatagramPacket(data,data.length);  
	 dsocket.send(dp);
}
//关闭聊天连接
public void close() {
	dsocket.close();
}

}
