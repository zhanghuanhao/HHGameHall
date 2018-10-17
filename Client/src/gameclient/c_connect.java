/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gameclient;
import account.Account;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class c_connect extends Thread{
	private static int PORT=12580;//端口号
	private static String hostname="111.230.38.149";//"127.0.0.1";//"125.216.246.62";//"172.20.10.11";//"192.168.43.153";//////目的地
	private int state=0;
	private Socket socket;
	private static ObjectInputStream ojin;
	private static ObjectOutputStream ojout;
	private InetAddress host=InetAddress.getByName(hostname);//指定包发送的目的地
	private String msg="";
	private boolean exist=false;
	private Encode enc;
	private Account acc=null;//账号对象
	//登录
	public c_connect(String acount,String pass) throws IOException {
		state=1;
		msg="LOGIN%"+acount+"/"+pass;
		start();
		} 
	//注册
	public c_connect(String name,String pass,String question,String answer)throws IOException {
		state=2;
	  	msg="RESITER%"+pass+"/"+name+"/"+question+"/"+answer;
		start();	
	}
	//获取密保
	public c_connect(String acount)throws IOException {
		state=3;
	    msg="FORGET1%"+acount;
		start();
	}
	//重置密码
	public c_connect(String acount,String pass,String answer)throws IOException {
		state=3;
		msg="FORGET2%"+acount+"/"+answer+"/"+pass;
		start();
	}
  public void run() {
	    try {
			socket=new Socket(host,PORT);		
			ojout=new ObjectOutputStream(socket.getOutputStream());
			ojin=new ObjectInputStream(socket.getInputStream());
			//获取公钥
			byte[]key=new byte[512];
			ojin.read(key);
			enc=new Encode(key,msg);
			//发送加密后信息
			ojout.write(enc.encode(),0,enc.encode().length);ojout.flush();
			exist=true;
		} catch (IOException e2) {
			if(state==1) {Login.show(-1);}
			else if(state==2) {register.show(0);}
			else if(state==3) {forget.show(0);} 
			e2.printStackTrace();  
		}

	    if(exist) {
			String result="";
			try {
				result=ojin.readUTF();
			} 
			catch (IOException e1) { e1.printStackTrace();}
			
			if(result.equals("CONFIRM")) {
				try {
					acc=(Account) ojin.readObject();
					Login.show(1,acc);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				}
			else {
				if(result.equals("DISCONFIRM")){Login.show(-2);}//账号密码不匹配
			else if(result.equals("BIND")) {Login.show(-3);}
			else if(result.equals("SUCCESS")) {
				try {
					register.show(1,Integer.toString(ojin.readInt()));
				} 
				catch (IOException e) {e.printStackTrace();}
				}//注册成功
			else if(result.equals("FIND")) {
				try {
				forget.show(2,ojin.readUTF());
			} catch (IOException e) {e.printStackTrace();forget.show(-3);}
				}
			else if(result.equals("RESET")) {forget.show(1);}//密码重置成功
			else if(result.equals("WRONG")) {forget.show(-1);}//密保答案错误
			else if(result.equals("NOEXIST")) {forget.show(-2);}//账号不存在
			else if(result.equals("BAN")) {forget.show(-4);}//密保答案错误超限
			Close();
			}
			}
  }
  //关闭socket
  private void Close() {
	  try { socket.close();} 
	  catch (IOException e) { e.printStackTrace();}
	  exist=false;
    }
  public Socket con() {
		return socket;	
  }
  public ObjectOutputStream out() {
		 return ojout;
	 }
	 public ObjectInputStream in() {
		 return ojin;
	 }
}
	
	
