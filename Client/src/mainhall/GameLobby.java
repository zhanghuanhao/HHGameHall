/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package mainhall;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import account.Account;

@SuppressWarnings("serial")
public class GameLobby extends JFrame implements MouseListener {
	private JPanel Panel1 = new JPanel();
	private JPanel Panel1_1 = new JPanel();
	private JPanel Panel1_2 = new JPanel();
	private JScrollPane Panel2 = new JScrollPane();
	private JPanelFight Panel2_1 = new JPanelFight();
	private JPanel Panel3 = new JPanel();
	private JScrollPane Panel3_1;
	private JPanel Panel3_2 = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JScrollPane scrollPane_1 = new JScrollPane();

	/** 窗口比例 */
	private final double x1 = 0.1;
	private final double x2 = 0.625;
	private final double x3 = 0.275;
	private final double y3_1 = 0.375;
	private final double y3_2 = 0.625;
	private final double y1_1 = 0.3;
	private final double y1_2 = 0.7;
	// 窗口的宽高
	private int win_width = 1024;
	private int win_height = 768;

	private static StyledDocument doc = null;
	private static StyledDocument doct = null;
	private static JTextPane textPane = null;
	private Chat chat = null;
	private static DateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
	private String Name = null;
	private static JButton Button1;
	private static JButton sendButton;
	private static JTextPane textArea;
	private emoji em = null;
	private static boolean flag = false;
	private String head = null;
	private String id = null;
	private Connect connect=null;
	private static JLabel[] labelChairs=null;
	private static JLabel[] playnames=null;
	private boolean[] chairs=null;
	private int sit=-1;
	private int gamenum=0;
	public personinfo per=null;
	private JLabel labelHead=null;
	private JLabel labelUser=null;
	private static JTextPane ntftext=null;

	public class JPanelFight extends JPanel {//斗地主大厅
		private int table_num = 99, tableLeftCornerX = 200, tableLeftCornerY = 100, tableLength = 150, chairLength = 40;
        		
		
		public JPanelFight() {
			super();
			setPreferredSize(new Dimension(1200, 320 * table_num / 3));
			JLabel[]tables=new JLabel[table_num];
			labelChairs = new JLabel[table_num * 4];
			playnames=new JLabel[table_num*4];
			int cnt = 0, num = 1;		
		
			for(int i=0;i<table_num;i++) {
				tables[i]=new JLabel();
				tables[i].setSize(100,82);
				tables[i].setIcon(new ImageIcon("res/image/table.png"));
				add(tables[i]);
			}
			
			
			for (int i = 0; i < 396; i++) {//凳子与坐下来的玩家名字
				labelChairs[i] = new JLabel();playnames[i]=new JLabel();
				labelChairs[i].setSize(40, 40);playnames[i].setSize(100,40);
				labelChairs[i].setHorizontalTextPosition(SwingConstants.CENTER);
				labelChairs[i].setHorizontalAlignment(SwingConstants.CENTER);
				labelChairs[i].setName(String.valueOf(i));
				if((i+1)%4!=0)labelChairs[i].addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						int i=Integer.parseInt(e.getComponent().getName());
						try {
						if(chairs[i]&&sit==-1) {
						    sit=i;
						    chairs[i]=false;					
							connect.sit(i+"");
						}
						else {
							if(sit==i) {
								chairs[i]=true;
								sit=-1;								
								connect.stand(i+"");								
							}
							else if(sit!=i&&sit!=-1&&chairs[i]) {
								chairs[sit]=true;chairs[i]=false;
								connect.stand(sit+"");
								connect.sit(i+"");
								sit=i;
							}
						}
					}catch (IOException e1) {
							e1.printStackTrace();
						}
						}
					@Override
					public void mousePressed(MouseEvent e) {		
					}
					@Override
					public void mouseReleased(MouseEvent e) {		
					}
					@Override
					public void mouseEntered(MouseEvent e) {
					}
					@Override
					public void mouseExited(MouseEvent e) {
					}
				});
				add(labelChairs[i]);add(playnames[i]);
			}
			
			
			for (int i = 0; i < table_num / 3; i++) {
				for (int j = 0; j < 3; j++) {
					tables[cnt/4].setLocation(tableLeftCornerX + j * tableLength * 2 + (tableLength - chairLength) / 2-30,
							tableLeftCornerY + i * 300 - (10 + chairLength)+80);
					
					labelChairs[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 - (10 + chairLength),
							tableLeftCornerY + i * 300 + (tableLength - chairLength) / 2);
					playnames[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 - (10 + chairLength),
							tableLeftCornerY + i * 300 + (tableLength - chairLength) / 2-40);
					labelChairs[cnt++].setIcon(new ImageIcon("res/image/chair.png"));
					labelChairs[cnt].setLocation(
							tableLeftCornerX + j * tableLength * 2 + (tableLength - chairLength) / 2,
							tableLeftCornerY + i * 300 - (10 + chairLength));
					playnames[cnt].setLocation(
							tableLeftCornerX + j * tableLength * 2 + (tableLength - chairLength) / 2,
							tableLeftCornerY + i * 300 - (10 + chairLength)-40);
					labelChairs[cnt++].setIcon(new ImageIcon("res/image/chair.png"));
					labelChairs[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 + (tableLength + 10),
							tableLeftCornerY + i * 300 + (tableLength - chairLength) / 2);
					playnames[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 + (tableLength + 10),
							tableLeftCornerY + i * 300 + (tableLength - chairLength) / 2-40);
					labelChairs[cnt++].setIcon(new ImageIcon("res/image/chair.png"));
					labelChairs[cnt].setLocation(
							tableLeftCornerX + j * tableLength * 2 + (tableLength - chairLength) / 2,
							tableLeftCornerY + i * 300 + (tableLength + 10));
					labelChairs[cnt++].setText(String.valueOf(num++) + "号桌");
				}
			}
		
			
		}
	

	}



	

	private void layoutPanel1() {
		Panel1_1 = new JPanel(new BorderLayout());
		Panel1_1.setLayout(null);
		Panel1_1.setBounds(0, 0, (int) (win_width * x1), (int) (win_height * y1_1));
		Panel1_1.setBackground(new Color(144, 238, 144));

		Panel1_2 = new JPanel();
		Panel1_2.setLayout(null);
		Panel1_2.setBounds(0, (int) (win_height * y1_1), (int) (win_width * x1), (int) (win_height * y1_2));
		Panel1_2.setBackground(new Color(210, 180, 140));

		labelHead = new JLabel();
		labelHead.setHorizontalAlignment(SwingConstants.CENTER);
		labelHead.setIcon(new ImageIcon("res/head/" + head));
		labelHead.setSize(40, 40);
		labelHead.setLocation(75, 10);

		labelUser = new JLabel();
		labelUser.setHorizontalAlignment(SwingConstants.CENTER);
		labelUser.setText(Name);
		labelUser.setSize(180, 40);
		labelUser.setLocation(0, 80);
		labelUser.setFont(new Font("DialogInput", Font.BOLD, 20));
		labelUser.setForeground(Color.BLACK);
		
		JButton perinfo=new JButton("个人信息");
		perinfo.setBounds(40, 120, 100, 40);
		perinfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				per=new personinfo(connect);
			    per.setVisible(true);
			    per.show(head, Name, id);
			}
		});
			
		
		JLabel labelChooseGame = new JLabel();
		labelChooseGame.setText("请选择游戏：");
		labelChooseGame.setHorizontalTextPosition(SwingConstants.CENTER);
		labelChooseGame.setHorizontalAlignment(SwingConstants.CENTER);
		labelChooseGame.setSize(126, 40);
		labelChooseGame.setLocation(30, 50);
		labelChooseGame.setFont(new Font("DialogInput", Font.BOLD, 20));
		labelChooseGame.setForeground(Color.BLACK);

		JComboBox<String> comboBoxchooseGame = new JComboBox<String>();
		comboBoxchooseGame.setBounds(40, 120, 100, 40);
		comboBoxchooseGame.setEnabled(true);
		comboBoxchooseGame.addItem("斗地主");
		comboBoxchooseGame.addItem("五子棋");
		
		JButton buttonChooseGame = new JButton("确认");
		buttonChooseGame.setBounds(40, 200, 100, 40);
		buttonChooseGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboBoxchooseGame.getSelectedItem().toString().equals("斗地主")) {
					
					try {	
						if(sit!=-1) {
							connect.stand(sit+"");
							chairs[sit]=true;
							sit=-1;
							}
						
						if(gamenum==0)Panel2.setViewportView(Panel2_1);
						gamenum=1;
						repanel();
						connect.send("LANDLORD");
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else if (comboBoxchooseGame.getSelectedItem().toString().equals("五子棋")) {             
					try {				
						if(sit!=-1) {
							connect.stand(sit+"");
							chairs[sit]=true;
							sit=-1;
							}  
					
						if(gamenum==0)Panel2.setViewportView(Panel2_1);
						gamenum=2;
					    repanel();				
						connect.send("GOBANG");
										
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
			}
		});
		
		JButton Ready=new JButton("准备");
		Ready.setBounds(40,250, 100, 40);
		Ready.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sit!=-1) {
					try {
						connect.ready(sit+"");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		Panel1_1.add(labelUser, BorderLayout.CENTER);
		Panel1_1.add(labelHead, BorderLayout.NORTH);
		Panel1_1.add(perinfo,BorderLayout.SOUTH);
		Panel1_2.add(labelChooseGame);
		Panel1_2.add(comboBoxchooseGame);
		Panel1_2.add(buttonChooseGame);
		Panel1_2.add(Ready);
		
		Panel1.add(Panel1_1);
		Panel1.add(Panel1_2);
	}

	private void layoutPanel3() {
		Panel3_2 = new JPanel();
		Panel3_2.setLayout(null);
		Panel3_2.setBounds(0, (int) (win_height * y3_1), (int) (win_width * x3), (int) (win_height * y3_2));
		Panel3_2.setBackground(Color.WHITE);
		
		
		ntftext.setContentType("text/html");
		ntftext.setEditable(false);
		Panel3_1 = new JScrollPane(ntftext);
		Panel3_1.setBounds(0, 0, 270, 288);
		ntftext.setBackground(new Color(245, 222, 179));

		Panel3.add(Panel3_1);
		Panel3.add(Panel3_2);

	}

	public GameLobby(Account acc, ObjectOutputStream out,ObjectInputStream in) throws IOException {
		
		Name = acc.nickname;// 账号名
		head = acc.head;
		id = Integer.toString(acc.id);
		em = new emoji(this);// 表情框
		chat = new Chat(id);// 大厅聊天后台
		
		chairs=new boolean[396];
		for (int i = 0; i < 396; i++) {
			chairs[i]=true;
		}
		ntftext=new JTextPane();
		
		setTitle("游戏大厅");
		getContentPane().setLayout(null);
		setBounds(0, 0, win_width, win_height);
		setResizable(true);
		setBackground(new Color(135, 206, 250));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Panel1.setLayout(null);
		Panel1.setBounds(0, 0, (int) (win_width * x1), win_height);
		Panel3.setLayout(null);
		Panel3.setBounds((int) (win_width * (1 - x3)), 0, 270, 768);
		Panel3.setBackground(new Color(135, 206, 250));
		layoutPanel1();
		layoutPanel3();
		Panel2.setBounds((int) (win_width * x1), 0, (int) (win_width * x2), win_height);
		Panel2.setBackground(new Color(135, 206, 250));
		Panel2.getVerticalScrollBar().setUnitIncrement(20); // 调整滚动条的速度
		Panel2_1.setLayout(null);
		Panel2_1.setBounds(0, 0, (int) (win_width * x2), win_height);

        Panel2_1.setLayout(null);

		getContentPane().add(Panel1);
		getContentPane().add(Panel2);
		getContentPane().add(Panel3);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				
				System.exit(0);
			}
			
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				e.getComponent().repaint();
			}
		});
		initialize();
		
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		connect = new Connect(out,in, acc, this);
		
	}

	@Override
	public void repaint() {
		super.repaint();
		win_height = getSize().height;
		win_width = getSize().width;
		Panel1.setBounds(0, 0, (int) (win_width * x1), win_height);
		Panel1_1.setBounds(0, 0, (int) (win_width * x1), (int) (win_height * y1_1));
		Panel1_2.setBounds(0, (int) (win_height * y1_1), (int) (win_width * x1), (int) (win_height * y1_2));
		Panel2.setBounds((int) (win_width * x1), 0, (int) (win_width * x2), win_height);
		Panel3.setBounds((int) (win_width * (1 - x3)), 0, (int) (win_width * x3), win_height);
		Panel3_1.setBounds(0, 0, (int) (win_width * x3), (int) (win_height * y3_1));
		Panel3_2.setBounds(0, (int) (win_height * y3_1), (int) (win_width * x3), (int) (win_height * y3_2));
		scrollPane.setBounds(0, 0, (int) (win_width * x3), (int) (win_height * 0.45));
		scrollPane_1.setBounds(0, (int) (win_height * 0.45), (int) (win_width * x3), (int) (win_height * 0.1));
		Button1.setBounds((int) (win_width * (x3 - 0.1) - 100), (int) (win_height * 0.55), (int) (win_height * 0.04),
				(int) (win_height * 0.04));
		sendButton.setBounds((int) (win_width * (x3 - 0.06) - 20), (int) (win_height * 0.55), (int) (win_width * 0.06),
				(int) (win_height * 0.04));
		Panel1.updateUI();
		Panel1_1.updateUI();
		Panel1_2.updateUI();
		Panel2.updateUI();
		Panel2_1.updateUI();
		Panel3_1.updateUI();
		Panel3_2.updateUI();
		scrollPane.updateUI();
	
	}

	private void initialize() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					chat.send("[id:" + id + "$]" + "LOGOUT");
					chat.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});

		scrollPane.setBounds(0, 0, (int) (win_width * x3), (int) (win_height * 0.45));
		textPane = new JTextPane();
		textPane.setFont(new Font("宋体", Font.PLAIN, 15));
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		doc = textPane.getStyledDocument();

		scrollPane_1.setBounds(0, (int) (win_height * 0.45), (int) (win_width * x3), (int) (win_height * 0.1));

		textArea = new JTextPane();
		textArea.setFont(new Font("宋体", Font.PLAIN, 20));
		scrollPane_1.setViewportView(textArea);
		doct = textArea.getStyledDocument();

		Button1 = new JButton();
		Button1.setIcon(new ImageIcon("res/emoji/1.png"));
		Button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!flag) {
					em.setVisible(true);
					flag = true;
				} else {
					em.setVisible(false);
					flag = false;
				}
			}
		});

		sendButton = new JButton("发送");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = buildsend();
				msg=msg.trim();
				if (!msg.equals("")) {
					try {
						chat.send("[id:" + id + "$][head:" + head + "$]" + Name + "：" + msg);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				textArea.setText("");
			}
		});
		Button1.setBounds((int) (win_width * (x3 - 0.1) - 100), (int) (win_height * 0.55), (int) (win_height * 0.04),
				(int) (win_height * 0.04));
		sendButton.setBounds((int) (win_width * (x3 - 0.06) - 20), (int) (win_height * 0.55), (int) (win_width * 0.06),
				(int) (win_height * 0.04));
		Panel3_2.add(scrollPane);
		Panel3_2.add(scrollPane_1);
		Panel3_2.add(Button1);
		Panel3_2.add(sendButton);
		this.addMouseListener(this);
		textArea.addMouseListener(this);
		sendButton.addMouseListener(this);
		textPane.addMouseListener(this);

	}

	// 显示图片
	private static void insertIcon(String path, JTextPane text) throws BadLocationException {
		text.setCaretPosition(text.getCaretPosition());
		ImageIcon icon = new ImageIcon((path));
		text.insertIcon(icon);
	}

	private static void insertIcon(String path, JTextPane text, int pos) throws BadLocationException {
		text.setCaretPosition(pos);
		ImageIcon icon = new ImageIcon((path));
		text.insertIcon(icon);
	}

	private static void inserthead(String path, JTextPane text, int pos) throws BadLocationException {
		text.setCaretPosition(pos);
		ImageIcon icon = new ImageIcon((path));
		icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		text.insertIcon(icon);
	}

	public static void show(String msg) throws BadLocationException {
		Date date = new Date();
		textPane.setCaretPosition(doc.getLength());
		doc.insertString(doc.getLength(), format.format(date), null);
		doc.insertString(doc.getLength(), "\n", null);
		String temp = null;
		int pos = 0;
		String num = null;
		String tx = msg.substring(msg.indexOf(":") + 1, msg.indexOf("$"));
		inserthead("res/head/" + tx, textPane, doc.getLength());
		msg = msg.substring(msg.indexOf("$") + 2);
		while (msg.contains("[emoji:")) {
			temp = msg.substring(pos, msg.indexOf("["));
			pos = msg.indexOf("$]");
			num = msg.substring(msg.indexOf("[emoji:") + 7, pos);
			msg = msg.substring(pos + 2);
			doc.insertString(doc.getLength(), temp, null);
			insertIcon("res/emoji/" + num + ".png", textPane, doc.getLength());
			pos = 0;
		}
		doc.insertString(doc.getLength(), msg, null);
		doc.insertString(doc.getLength(), "\n", null);
		textPane.setCaretPosition(doc.getLength());
	}

	private String buildsend() {
		StringBuilder sb = new StringBuilder("");
		// 提取输入框信息
		for (int i = 0; i < textArea.getText().length(); i++) {
			if (doct.getCharacterElement(i).getName().equals("icon")) {
				Icon icon = StyleConstants.getIcon(textArea.getStyledDocument().getCharacterElement(i).getAttributes());
				String str = icon.toString();
				str = str.substring(str.indexOf("/") + 7, str.indexOf("."));
				sb.append("[emoji:" + str + "$]"); // 还原表情代号
			} else
				try {
					sb.append(textArea.getStyledDocument().getText(i, 1));// 还原文字
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
		}
		return sb.toString();
	}

	public static void write(String msg) {
		try {
			insertIcon("res/emoji/" + msg + ".png", textArea);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		flag = false;
	}

	public static JButton getPicBtn() {
		return Button1;
	}
	public int Sit() {
		return sit;
	}
	public void paint(String str) {
		 String num=str.substring(0, str.indexOf("%"));
		 int i=Integer.parseInt(num);
		 str=str.substring(str.indexOf("%")+1);
		 String state=str.substring(0,str.indexOf("%"));
		 str=str.substring(str.indexOf("%")+1);
		 String name=str.substring(0, str.indexOf("%"));
		 String Head=str.substring(str.indexOf("%")+1);
		 if(state.equals("SITDOWN")) {
			 labelChairs[i].setIcon(new ImageIcon("res/head/"+Head));
			 playnames[i].setText(name);
			 chairs[i]=false;
		 }
		 if(state.equals("STANDUP")) {
			 labelChairs[i].setIcon(new ImageIcon("res/image/chair.png"));
			 playnames[i].setText("");
			 chairs[i]=true;
		 }
		 if(state.equals("READY")) {
			 labelChairs[i].setIcon(new ImageIcon("res/head/"+Head));
			 playnames[i].setText(name+":Ready");
			 chairs[i]=false;
		 }
	 }
	
	public void repanel() {
		for (int i = 0; i < 396; i++) {
			chairs[i]=true;
		}
		if(gamenum==2) {
			for(int i=0;i<396;i++){
				if((i-1)%4==0)
				{
					labelChairs[i].setVisible(false);
					playnames[i].setVisible(false);
					chairs[i]=false;
					}
				else {
					if((i+1)%4!=0)labelChairs[i].setIcon(new ImageIcon("res/image/chair.png"));
					playnames[i].setText("");
				}
						}
			
		}
		if(gamenum==1) {
			for(int i=0;i<396;i++){
				if((i-1)%4==0)
				{
					labelChairs[i].setVisible(true);
					playnames[i].setVisible(true);
					}
				else {
					if((i+1)%4!=0)labelChairs[i].setIcon(new ImageIcon("res/image/chair.png"));
					playnames[i].setText("");
				}
						}
		}
	}
	
	public void change(String head,String name) {
		labelHead.setIcon(new ImageIcon("res/head/"+head));
		labelUser.setText(name);
	}
	
	public void showntf(String ntf) {
		ntftext.setText(ntf);
	}


	@Override
	public void mousePressed(MouseEvent e) {
		em.setVisible(false);
	}
	@Override
	public void mouseClicked(MouseEvent e) {

	}
	@Override
	public void mouseReleased(MouseEvent e) {

	}
	@Override
	public void mouseEntered(MouseEvent e) {

	}
	@Override
	public void mouseExited(MouseEvent e) {

	}

}
