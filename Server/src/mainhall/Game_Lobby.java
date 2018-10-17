/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package mainhall;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import server.Msg_Deliver;
import server.Server_Listener;
import server.Write_Log;

@SuppressWarnings("serial")
public class Game_Lobby extends JFrame implements MouseListener {
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

	private StyledDocument doc = null;
	private StyledDocument doct = null;
	private JTextPane textPane = null;
	private DateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
	private final static String Name = "管理员";
	private JButton Button1;
	private JButton sendButton;
	private JTextPane textArea;
	private emoji em = null;
	private boolean flag = false;
	private final static String head = "1.png";
	private static JLabel[] labelChairs = null;
	private static JLabel[] playnames = null;
	private boolean isShow = false;
	private String status = null;
	private JLabel Landlord_Size = new JLabel();
	private JLabel Gobang_Size = new JLabel();
	private JTextPane Notice = new JTextPane();

	public void updateSize(int landlord, int gobang) {
		Landlord_Size.setText("<html><body>斗地主：<br>" + landlord + "</body></html>");
		Gobang_Size.setText("<html><body>五子棋：<br>" + gobang + "</body></html>");
	}

	public String getStatus() {
		return status;
	}

	public void reverseShow() {
		this.isShow = !isShow;
		this.setVisible(isShow);
	}
	
	public void updateNotice()
	{
		Notice.setText(Server_Listener.getNotice());
	}

	public class JPanelFight extends JPanel {
		private int table_num = 99, tableLeftCornerX = 200, tableLeftCornerY = 100, tableLength = 150, chairLength = 40;

		public JPanelFight() {
			super();
			setPreferredSize(new Dimension(1200, 320 * table_num / 3));
			JLabel[] tables = new JLabel[table_num];
			labelChairs = new JLabel[table_num * 4];
			playnames = new JLabel[table_num * 4];
			int cnt = 0, num = 1;
			for (int i = 0; i < table_num * 4; i++) {
				labelChairs[i] = new JLabel();
				playnames[i] = new JLabel();
				labelChairs[i].setSize(40, 40);
				playnames[i].setSize(100, 40);
				labelChairs[i].setHorizontalTextPosition(SwingConstants.CENTER);
				labelChairs[i].setHorizontalAlignment(SwingConstants.CENTER);
				labelChairs[i].setName(String.valueOf(i));
				add(labelChairs[i]);
				add(playnames[i]);
			}
			for (int i = 0; i < table_num; i++) {
				tables[i] = new JLabel();
				tables[i].setSize(100, 82);
				tables[i].setIcon(new ImageIcon("res/image/table.png"));
				add(tables[i]);
			}
			for (int i = 0; i < table_num / 3; i++) {
				for (int j = 0; j < 3; j++) {
					tables[cnt / 4].setLocation(
							tableLeftCornerX + j * tableLength * 2 + (tableLength - chairLength) / 2 - 30,
							tableLeftCornerY + i * 300 - (10 + chairLength) + 80);
					labelChairs[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 - (10 + chairLength),
							tableLeftCornerY + i * 300 + (tableLength - chairLength) / 2);
					playnames[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 - (10 + chairLength),
							tableLeftCornerY + i * 300 + (tableLength - chairLength) / 2 - 40);
					labelChairs[cnt++].setIcon(new ImageIcon("res/image/chair.png"));
					labelChairs[cnt].setLocation(
							tableLeftCornerX + j * tableLength * 2 + (tableLength - chairLength) / 2,
							tableLeftCornerY + i * 300 - (10 + chairLength));
					playnames[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 + (tableLength - chairLength) / 2,
							tableLeftCornerY + i * 300 - (10 + chairLength) - 40);
					labelChairs[cnt++].setIcon(new ImageIcon("res/image/chair.png"));
					labelChairs[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 + (tableLength + 10),
							tableLeftCornerY + i * 300 + (tableLength - chairLength) / 2);
					playnames[cnt].setLocation(tableLeftCornerX + j * tableLength * 2 + (tableLength + 10),
							tableLeftCornerY + i * 300 + (tableLength - chairLength) / 2 - 40);
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

		JLabel labelHead = new JLabel();
		labelHead.setHorizontalAlignment(SwingConstants.CENTER);
		labelHead.setIcon(new ImageIcon("res/head/" + head));
		labelHead.setSize(40, 40);
		labelHead.setLocation(75, 10);

		JLabel labelUser = new JLabel();
		labelUser.setHorizontalAlignment(SwingConstants.CENTER);
		labelUser.setText(Name);
		labelUser.setSize(180, 40);
		labelUser.setLocation(0, 80);
		labelUser.setFont(new Font("DialogInput", Font.BOLD, 20));
		labelUser.setForeground(Color.BLACK);

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
				String temp = comboBoxchooseGame.getSelectedItem().toString();
				if ("斗地主".equals(temp)) {
					if (status == null)
						Panel2.setViewportView(Panel2_1);
					status = temp;
					repanel();
					refresh(0);
				} else if ("五子棋".equals(temp)) {
					if (status == null)
						Panel2.setViewportView(Panel2_1);
					status = temp;
					repanel();
					refresh(1);
				}
			}
		});

		Panel1_1.add(labelUser, BorderLayout.SOUTH);
		Panel1_1.add(labelHead, BorderLayout.NORTH);
		Panel1_2.add(labelChooseGame);
		Panel1_2.add(comboBoxchooseGame);
		Panel1_2.add(buttonChooseGame);
		Panel1.add(Panel1_1);
		Panel1.add(Panel1_2);

		Landlord_Size.setText("<html><body>斗地主：<br>" + 0 + "</body></html>");
		Landlord_Size.setHorizontalTextPosition(SwingConstants.CENTER);
		Landlord_Size.setHorizontalAlignment(SwingConstants.CENTER);
		Landlord_Size.setForeground(Color.BLACK);
		Landlord_Size.setFont(new Font("DialogInput", Font.BOLD, 20));
		Landlord_Size.setBounds(10, 264, 126, 80);
		Panel1_2.add(Landlord_Size);

		Gobang_Size.setText("<html><body>五子棋：<br>" + 0 + "</body></html>");
		Gobang_Size.setHorizontalTextPosition(SwingConstants.CENTER);
		Gobang_Size.setHorizontalAlignment(SwingConstants.CENTER);
		Gobang_Size.setForeground(Color.BLACK);
		Gobang_Size.setFont(new Font("DialogInput", Font.BOLD, 20));
		Gobang_Size.setBounds(10, 314, 126, 80);
		Panel1_2.add(Gobang_Size);
	}

	private void layoutPanel3() {
		Panel3_2 = new JPanel();
		Panel3_2.setLayout(null);
		Panel3_2.setBounds(0, (int) (win_height * y3_1), (int) (win_width * x3), (int) (win_height * y3_2));
		Panel3_2.setBackground(Color.WHITE);
		Notice.setBackground(new Color(255, 228, 181));
		Notice.setContentType("text/html");
		Notice.setText(Server_Listener.getNotice());
		Notice.setEditable(false);
		Panel3_1 = new JScrollPane(Notice);
		Panel3_1.setBounds(0, 0, (int) (win_width * x3), (int) (win_height * y3_1));
		Panel3.add(Panel3_1);
		Panel3.add(Panel3_2);
	}

	public Game_Lobby() {
		em = new emoji(this);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setTitle("游戏大厅");
		getContentPane().setLayout(null);
		setBounds(0, 0, win_width, win_height);
		setResizable(true);
		setBackground(new Color(135, 206, 250));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		Panel1.setLayout(null);
		Panel1.setBounds(0, 0, (int) (win_width * x1), win_height);
		Panel3.setLayout(null);
		Panel3.setBounds((int) (win_width * (1 - x3)), 0, (int) (win_width * x3), win_height);
		Panel3.setBackground(new Color(135, 206, 250));
		layoutPanel1();
		layoutPanel3();
		Panel2.setBounds((int) (win_width * x1), 0, (int) (win_width * x2), win_height);
		Panel2.setBackground(new Color(135, 206, 250));
		Panel2.getVerticalScrollBar().setUnitIncrement(20); // 调整滚动条的速度
		Panel2_1.setLayout(null);
		Panel2_1.setBounds(0, 0, (int) (win_width * x2), win_height);
		Panel2_1.setBackground(new Color(255, 255, 224));
		getContentPane().add(Panel1);
		getContentPane().add(Panel2);
		getContentPane().add(Panel3);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				e.getComponent().repaint();
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isShow = false;
				e.getWindow().setVisible(isShow);
			}
		});
		initialize();
		isShow = true;
		this.setVisible(isShow);
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
		Panel3_1.updateUI();
		Panel3_2.updateUI();
		scrollPane.updateUI();
		scrollPane_1.updateUI();
	}

	private void initialize() {
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
					flag = true;
					em.setVisible(flag, Button1.getLocationOnScreen());
				} else {
					flag = false;
					em.setVisible(flag, Button1.getLocationOnScreen());
				}
			}
		});

		sendButton = new JButton("发送");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = buildsend();
				msg = msg.trim();
				if (!msg.equals("")) {
					Msg_Deliver.send(msg);
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
	private void insertIcon(String path, JTextPane text, int pos) throws BadLocationException {
		text.setCaretPosition(pos);
		ImageIcon icon = new ImageIcon((path));
		text.insertIcon(icon);
	}

	private void inserthead(String path, JTextPane text, int pos) throws BadLocationException {
		text.setCaretPosition(pos);
		ImageIcon icon = new ImageIcon((path));
		icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		text.insertIcon(icon);
	}

	public void show(String info, String msg) {
		try {
			Date date = new Date();
			textPane.setCaretPosition(doc.getLength());
			doc.insertString(doc.getLength(), format.format(date) + "\r\n", null);
			doc.insertString(doc.getLength(), info, null);
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
		} catch (Exception e) {
			Write_Log.writeActivity("Chat_show", e.getMessage());
		}
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
				}
		}
		return sb.toString();
	}

	public void write(String msg) throws BadLocationException {
		flag = false;
		insertIcon("res/emoji/" + msg + ".png", textArea, textArea.getCaretPosition());
	}

	public void paint(String str) {
		String num = str.substring(0, str.indexOf("%"));
		int i = Integer.parseInt(num);
		str = str.substring(str.indexOf("%") + 1);
		String state = str.substring(0, str.indexOf("%"));
		str = str.substring(str.indexOf("%") + 1);
		String name = str.substring(0, str.indexOf("%"));
		String Head = str.substring(str.indexOf("%") + 1);
		if (state.equals("SITDOWN")) {
			labelChairs[i].setIcon(new ImageIcon("res/head/" + Head));
			playnames[i].setText(name);
		}
		if (state.equals("STANDUP")) {
			labelChairs[i].setIcon(new ImageIcon("res/image/chair.png"));
			playnames[i].setText("");
		}
		if (state.equals("READY")) {
			labelChairs[i].setIcon(new ImageIcon("res/head/" + Head));
			playnames[i].setText(name + ":Ready");
		}
	}

	private void repanel() {
		if ("五子棋".equals(status)) {
			for (int i = 0; i < 396; i++) {
				if ((i - 1) % 4 == 0) {
					labelChairs[i].setVisible(false);
					playnames[i].setVisible(false);
				} else {
					if ((i + 1) % 4 != 0)
						labelChairs[i].setIcon(new ImageIcon("res/image/chair.png"));
					playnames[i].setText("");
				}
			}

		} else if ("斗地主".equals(status)) {
			for (int i = 0; i < 396; i++) {
				if ((i - 1) % 4 == 0) {
					labelChairs[i].setVisible(true);
					playnames[i].setVisible(true);
				} else {
					if ((i + 1) % 4 != 0)
						labelChairs[i].setIcon(new ImageIcon("res/image/chair.png"));
					playnames[i].setText("");
				}
			}
		}
	}

	/** 0是斗地主，1是五子棋 */
	@SuppressWarnings("unchecked")
	private void refresh(int xxx) {
		Hashtable<Integer, String> temp = null;
		if (xxx == 0)
			temp = (Hashtable<Integer, String>) Server_Listener.getLandlordTableStatus().getStatus().clone();
		else if (xxx == 1)
			temp = (Hashtable<Integer, String>) Server_Listener.getGobangTableStatus().getStatus().clone();
		Enumeration<String> list = temp.elements();
		while (list.hasMoreElements())
			paint(list.nextElement());
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
