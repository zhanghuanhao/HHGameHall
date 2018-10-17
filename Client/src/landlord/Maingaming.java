/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package landlord;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
public class Maingaming {

	private JFrame frame;
    public Players players=null;
    private JLabel name1=null;
    private JLabel name2=null;
    private JLabel name3=null;
    private int num=-1;
    private Poker poker=null;
    private Poker dzpoker=null;
    private JLabel [] pokerimg=null;
    private JLabel [] dzpokerimg=null;
    private JLabel [] lastpokerimg=null;
    private JLabel remain1=null;
    private JLabel remain2=null;
    private JLabel remain3=null;
    private int left=0;
    private int right=0;
    private JLabel send =null;
    private JLabel nosend =null;
    private JButton qdz=null;
    private JButton bq=null;
    private gamelandlord g=null;
    private JLabel dz1=null;
    private JLabel dz2=null;
    private JLabel dz3=null;
    private JLabel Player1=null;
    private JLabel Player2=null;
    private JLabel Player3=null;
    private boolean reset[]=new boolean[3];
    private boolean can=false;
    private JLabel[] times=null;
    private JButton ready=null;
    private JButton out=null;
 
 
	public Maingaming(Players play,int num,gamelandlord G) {
		this.players=play;this.num=num;this.poker=play.pokers[num];this.dzpoker=play.dzpoker;this.g=G;
		initialize();
		paint();
		repaint();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("超级斗地主");
		frame.setBounds(100, 100, 1422, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		
		
		for(int i=0;i<3;i++) {
			reset[i]=true;
		}
		
		
		pokerimg=new JLabel[20];
		lastpokerimg=new JLabel[20];
		for(int i=19;i>=0;i--) {
			int x=350+40*i;
			pokerimg[i]=new JLabel();lastpokerimg[i]=new JLabel();
			pokerimg[i].setBounds(x, 540, 94, 134);lastpokerimg[i].setBounds(x, 270, 94, 134);
			frame.getContentPane().add(pokerimg[i]);frame.getContentPane().add(lastpokerimg[i]);
			pokerimg[i].addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
					JLabel l=(JLabel)e.getComponent();
					String temp=l.getIcon().toString();
					temp=temp.substring(temp.lastIndexOf("/")+1,temp.indexOf("."));
					
					if(e.getComponent().getY()==540)
					{e.getComponent().setLocation(e.getComponent().getX(), 520);
					g.addtemp(Integer.parseInt(temp));}
					else {
						e.getComponent().setLocation(e.getComponent().getX(), 540);
						g.reducetemp(Integer.parseInt(temp));
						}
					g.check();//判定
				}
				@Override
				public void mousePressed(MouseEvent e) {			}
				@Override
				public void mouseReleased(MouseEvent e) {			}
				@Override
				public void mouseEntered(MouseEvent e) {			}
				@Override
				public void mouseExited(MouseEvent e) {				}
							
			});}
		
		dzpokerimg=new JLabel[3];
		for(int t=2;t>=0;t--) {
			int x=640+40*t;
			dzpokerimg[t]=new JLabel();
			dzpokerimg[t].setBounds(x, 20, 94, 134);
			frame.getContentPane().add(dzpokerimg[t]);
		}
		
		
		name1 = new JLabel("");
		name1.setFont(new Font("宋体", Font.BOLD, 25));
		name1.setForeground(Color.white);
		name1.setBounds(324, 181, 100, 40);
		frame.getContentPane().add(name1);
		
		name2 = new JLabel("");
		name2.setFont(new Font("宋体", Font.BOLD, 25));
		name2.setForeground(Color.white);
		name2.setBounds(1231, 181, 100, 40);
		frame.getContentPane().add(name2);
		
		name3 = new JLabel("");
		name3.setFont(new Font("宋体", Font.BOLD, 25));
		name3.setForeground(Color.white);
		name3.setBounds(56, 660, 100, 40);
		frame.getContentPane().add(name3);
		
		send = new JLabel(new ImageIcon("res/image/out1.png"));
		send.setFont(new Font("宋体", Font.BOLD, 15));
		send.setBounds(575, 411, 135, 30);
		
		send.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				send.setIcon(new ImageIcon("res/image/out3.png"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {	
				if((players.round==num)&&(can)) {
					players.lastpoker.pass=0;
					g.presend();//标记
					players=g.get();
					g.send();//发送	
					g.del();
					}
			}
			@Override
			public void mousePressed(MouseEvent e) {			}
			@Override
			public void mouseReleased(MouseEvent e) {			}
			@Override
			public void mouseExited(MouseEvent e) {
				send.setIcon(new ImageIcon("res/image/out1.png")); 	
			}
		});
		frame.getContentPane().add(send);
		send.setEnabled(false);
		
		nosend = new JLabel(new ImageIcon("res/image/pass.png"));
		nosend.setFont(new Font("宋体", Font.BOLD, 15));
		nosend.setBounds(756, 411, 135, 30);
		frame.getContentPane().add(nosend);
		nosend.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				nosend.setIcon(new ImageIcon("res/image/pass1.png"));
			}

			@Override
			public void mouseClicked(MouseEvent e) {	
				if(players.round==num) {
				players.lastpoker.pass++;
				g.send();
				g.del();//倒计时重置
				
				for(int i=0;i<3;i++)times[i].setVisible(false);
				if(players.round==left)times[0].setVisible(true);
				else if(players.round==right)times[1].setVisible(true);
				else if(players.round==num)times[2].setVisible(true);
				}		
				}
			@Override
			public void mousePressed(MouseEvent e) {		    }
			@Override
			public void mouseReleased(MouseEvent e) {		    }
			@Override
			public void mouseExited(MouseEvent e) {
				nosend.setIcon(new ImageIcon("res/image/pass.png")); 	
			}
		});
		nosend.setEnabled(false);
		
		remain1 = new JLabel("");
		remain1.setFont(new Font("宋体", Font.BOLD, 25));
		remain1.setForeground(Color.white);
		remain1.setBounds(310, 217, 200, 40);
		frame.getContentPane().add(remain1);
		
		remain2 = new JLabel("");
		remain2.setFont(new Font("宋体", Font.BOLD, 25));
		remain2.setForeground(Color.white);
		remain2.setBounds(1206, 231, 200, 40);
		frame.getContentPane().add(remain2);
		
		remain3 = new JLabel("");
		remain3.setFont(new Font("宋体", Font.BOLD, 25));
		remain3.setForeground(Color.white);
		remain3.setBounds(50, 695, 200, 40);
		frame.getContentPane().add(remain3);
		
		Player1 = new JLabel(new ImageIcon("res/image/nongmin.png"));
		Player1.setBounds(290, 51, 120, 120);
		frame.getContentPane().add(Player1);
		
		Player2 = new JLabel(new ImageIcon("res/image/nongmin.png"));
		Player2.setBounds(1210, 51, 120, 120);
		frame.getContentPane().add(Player2);
		
		Player3 = new JLabel(new ImageIcon("res/image/nongmin.png"));
		Player3.setBounds(36, 517, 120, 120);
		frame.getContentPane().add(Player3);
		
		bq = new JButton("不抢");
		bq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				players=g.get();
				if(players.round==num) {
				g.qdz(false);
				bq.setEnabled(false);qdz.setEnabled(false);
				bq.setVisible(false);qdz.setVisible(false);
				}
			}
		});
		bq.setFont(new Font("宋体", Font.BOLD, 15));
		bq.setBounds(779, 445, 90, 40);
		frame.getContentPane().add(bq);
		bq.setEnabled(false);
		
		qdz = new JButton("抢地主");
		qdz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				players=g.get();
				if(players.round==num) {
				g.qdz(true);
				qdz.setEnabled(false);bq.setEnabled(false);
				bq.setVisible(false);qdz.setVisible(false);
				}
			}
		});
		qdz.setFont(new Font("宋体", Font.BOLD, 15));
		qdz.setBounds(601, 445, 90, 40);
		frame.getContentPane().add(qdz);
		qdz.setEnabled(false);
		
		dz1 = new JLabel("");
		dz1.setFont(new Font("宋体", Font.BOLD, 20));
		dz1.setForeground(Color.red);
		dz1.setBounds(320, 267, 80, 40);
		frame.getContentPane().add(dz1);
		dz1.setVisible(false);
		
	    dz2 = new JLabel("");
		dz2.setFont(new Font("宋体", Font.BOLD, 20));
		dz2.setForeground(Color.red);
		dz2.setBounds(1251, 286, 80, 40);
		frame.getContentPane().add(dz2);
		dz2.setVisible(false);
		
		dz3 = new JLabel("");
		dz3.setFont(new Font("宋体", Font.BOLD, 20));
		dz3.setForeground(Color.red);
		dz3.setBounds(76, 467, 80, 40);
		frame.getContentPane().add(dz3);
		dz3.setVisible(false);	
		
		times=new JLabel[3];
		for(int i=0;i<3;i++) {
			times[i]=new JLabel();
			times[i].setFont(new Font("宋体", Font.BOLD, 30));
			times[i].setForeground(Color.red);
			frame.getContentPane().add(times[i]);
		}
		times[0].setBounds(340, 22, 80, 40);
		times[1].setBounds(1265, 22, 80, 40);
		times[2].setBounds(80, 470, 80, 40);
		
		ready = new JButton("准备");
		ready.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				players.qdz[num]=true;
				g.send();
				ready.setEnabled(false);ready.setVisible(false);
				out.setEnabled(false);out.setVisible(false);
			}
		});
		ready.setBounds(601, 375, 90, 40);
		frame.getContentPane().add(ready);
		ready.setVisible(false);
		
		out = new JButton("退出");
		out.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int res=JOptionPane.showConfirmDialog(null, "是否退出游戏？","是否退出",JOptionPane.YES_NO_OPTION);
				if(res==JOptionPane.YES_OPTION) {
					players.qdz[num]=false;
					g.send();
				}
				else {
					return;
				}
			}
		});
		out.setBounds(779, 375, 90, 40);
		frame.getContentPane().add(out);
		out.setVisible(false);
		
		
		JLabel Background = new JLabel(new ImageIcon("res/image/background.jpg"));
		Background.setBounds(0, 0, 1422, 800);
		frame.getContentPane().add(Background);
		
	}
	//初始绘图
	public void paint() {
		name3.setText(players.playerinfo[num].playername);
		
		if(num==0) {
			name1.setText(players.playerinfo[1].playername);name2.setText(players.playerinfo[2].playername);
			left=1;right=2;
		}
		else if(num==1) {
			name1.setText(players.playerinfo[2].playername);name2.setText(players.playerinfo[0].playername);
			left=2;right=0;
		}
		else if(num==2) {
			name1.setText(players.playerinfo[0].playername);name2.setText(players.playerinfo[1].playername);
			left=0;right=1;
		}
		
		
	}
	//重绘牌
	public void repaint() {
		players=g.get();poker=players.pokers[num];
		for(int i=0;i<20;i++) {
			pokerimg[i].setIcon(null);lastpokerimg[i].setIcon(null);
			pokerimg[i].setLocation(320+40*i, 540);
		}
		int t=0;
		for(int i=0;i<poker.num;i++) {
			if(!poker.ifsend(i)) {
				pokerimg[t++].setIcon(new ImageIcon("res/image/poker/"+poker.cardrecord(i)+".png"));
				}	
		}
		if(t!=poker.num)pokerimg[t].setLocation(320+40*(t+1),540);
		t=10-players.lastpoker.num/2;
		for(int i=0;i<players.lastpoker.num;i++) {
			lastpokerimg[t++].setIcon(new ImageIcon("res/image/poker/"+players.lastpoker.cardrecord(i)+".png"));
		}
		remain1.setText("剩余："+players.pokers[left].remain+"张");
		remain2.setText("剩余："+players.pokers[right].remain+"张");
		remain3.setText("剩余："+players.pokers[num].remain+"张");
		
		for(int i=0;i<3;i++)times[i].setVisible(false);
		if(players.round==left)times[0].setVisible(true);
		else if(players.round==right)times[1].setVisible(true);
		else if(players.round==num)times[2].setVisible(true);
		
		if(players.round==num&&players.dz!=-1) {
			nosend.setEnabled(true);
		}
		else {
			send.setEnabled(false);nosend.setEnabled(false);
		}
	}
	
	//重新游戏
	public void regame() {
		players=null;
		Player1.setIcon(new ImageIcon("res/image/nongmin.png"));
		Player2.setIcon(new ImageIcon("res/image/nongmin.png"));
		Player3.setIcon(new ImageIcon("res/image/nongmin.png"));
		qdz.setVisible(true);bq.setVisible(true);qdz.setEnabled(false);bq.setEnabled(false);
		send.setEnabled(false);nosend.setEnabled(false);
		for(int i=0;i<3;i++) {
			reset[i]=true;
			times[i].setVisible(false);
		}
		dz1.setText("");dz2.setText("");dz3.setText("");
		dz1.setVisible(false);dz2.setVisible(false);dz3.setVisible(false);
	}
	//显示抢地主情况
	public void showqdz(Players p) {
		this.players=p;
		if(players.round-1==left)dz1.setVisible(true);
		if(players.round-1==right)dz2.setVisible(true);
		if(players.round-1==num)dz3.setVisible(true);
		if(players.round==num) {
			qdz.setVisible(true);bq.setVisible(true);
			qdz.setEnabled(true);bq.setEnabled(true);
			}
		if(players.qdz[left]==true&&players.round-1==left) {
			dz1.setText("抢地主");
		}
		else {
			if(reset[left])
			{
				dz1.setText("不抢");
			    reset[left]=false;
			}
			}
		if(players.qdz[right]==true&&players.round-1==right) {
			dz2.setText("抢地主");
		}
		else {
			if(reset[right])
			{
				dz2.setText("不抢");
				reset[right]=false;
				}
			}
		if(players.qdz[this.num]==true&&players.round-1==this.num) {
			dz3.setText("抢地主");
		}
		else {
			if(reset[num]) {
			dz3.setText("不抢");
			reset[num]=false;
			}
			}
	}
	//绘地主
	public void paintdz(int num) {
		dz1.setText("");dz2.setText("");dz3.setText("");
		Player1.setVisible(true);Player2.setVisible(true);Player3.setVisible(true);
		if(num==left) {
			Player1.setIcon(new ImageIcon("res/image/dizhu.png"));
			}
		else if(num==right) {
			Player2.setIcon(new ImageIcon("res/image/dizhu.png"));
			}
		else if(num==this.num) {
			Player3.setIcon(new ImageIcon("res/image/dizhu.png"));
			}
		for(int i=0;i<3;i++) {
			dzpokerimg[i].setIcon(new ImageIcon("res/image/poker/"+dzpoker.cardrecord(i)+".png"));
		}
		repaint();
	}
	//能否发
	public void ifcansend(boolean b) {
		if(players.round==num&&players.dz!=-1) {
		send.setEnabled(b);
		can=b;
		}
	}
	//结束一局游戏
	public void over(int num) {
		if(this.num==players.dz&&this.num==num) {
			JOptionPane.showMessageDialog(null,"地主胜利！","胜利！",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(this.num==players.dz&&this.num!=num) {
			JOptionPane.showMessageDialog(null,"农民胜利！","失败！",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(this.num!=players.dz&&this.num==num) {
			JOptionPane.showMessageDialog(null,"农民胜利！","胜利！",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(this.num!=players.dz&&num==players.dz) {
			JOptionPane.showMessageDialog(null,"地主胜利！","失败！",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(this.num!=players.dz&&num!=players.dz&&this.num!=num) {
			JOptionPane.showMessageDialog(null,"农民胜利！","胜利！",JOptionPane.INFORMATION_MESSAGE);
		}
		send.setEnabled(false);nosend.setEnabled(false);
		ready.setVisible(true);out.setVisible(true);
		ready.setEnabled(true);out.setEnabled(true);
		for(int i=0;i<3;i++) {
			reset[i]=true;
		}
		dz1.setText("");dz2.setText("");dz3.setText("");
		times[0].setVisible(false);times[1].setVisible(false);times[2].setVisible(false);
	}

//刷新时间
public void flushtime(int t) {
	if(t>30) {
		for(int i=0;i<3;i++)times[i].setText("");
		return;
	}
	if(players.round==left)times[0].setText(t+"");
	else if(players.round==right)times[1].setText(t+"");
	else if(players.round==num)times[2].setText(t+"");
	
	if(t==0) {
		g.del();
		if(players.round==num) {
			players.lastpoker.pass++;
			g.send();
			for(int i=0;i<3;i++)times[i].setVisible(false);
			if(players.round==left)times[0].setVisible(true);
			else if(players.round==right)times[1].setVisible(true);
			else if(players.round==num)times[2].setVisible(true);
			}		
	}
}
//显示准备
public void showready(Players p) {
	this.players=p;System.out.println("players.round="+players.round);
	if(players.round==3) {
	dz1.setVisible(true);
	dz2.setVisible(true);
	dz3.setVisible(true);}
	
	if(players.qdz[left]==true&&players.round-1==left) {
		dz1.setText("准备");
	}
	else {
		if(reset[left])
		{
			dz1.setText("不准备");
		    reset[left]=false;
		}
		}
	if(players.qdz[right]==true&&players.round-1==right) {
		dz2.setText("准备");
	}
	else {
		if(reset[right])
		{
			dz2.setText("不准备");
			reset[right]=false;
			}
		}
	if(players.qdz[this.num]==true&&players.round-1==this.num) {
		dz3.setText("准备");
	}
	else {
		if(reset[num]) {
		dz3.setText("不准备");
		reset[num]=false;
		}
		}
}
//退出
public void out() {
	JOptionPane.showMessageDialog(null, "因玩家数量不足，游戏结束!", "提示", JOptionPane.YES_OPTION);
	frame.dispose();
}
}
