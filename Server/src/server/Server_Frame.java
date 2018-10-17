/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package server;

import java.awt.Component;
/**
 * @author Zhh
 */
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;

import account.Acc_Info;
import account.Account_Manage;
import javax.swing.JButton;

public class Server_Frame {

	private JFrame frame;
	private Server_Listener sl;
	private static JLabel label;
	private static JTable table;
	private static DefaultTableModel tablemodel;
	private static int count;
	/****** 通过关闭线程来关闭服务器，防止图形线程阻塞无法写入新行 *****/
	private Thread closeThread = null;

	public Server_Frame() {
		initialize();
		frame.setVisible(true);
	}

	private static void clearTableAndLabel() {
		label.setText("在线人数：0人");
		/*** 清空表table */
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		while (model.getRowCount() > 0) {
			model.removeRow(model.getRowCount() - 1);
		}
		count = 0;
	}

	private void initialize() {
		frame = new JFrame("服务器");
		frame.setBounds(100, 100, 730, 563);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 714, 29);
		frame.getContentPane().add(menuBar);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (sl == null)
					System.exit(0);
				if (closeThread != null)
					return;
				if (sl.isOpen)
					sl.shutdown();
				closeThread = new Thread(new Runnable() {
					@Override
					public void run() {
						while (true) {
							if (sl.isOpen)
								continue;
							System.exit(0);
						}
					}
				});
				closeThread.start();
				closeThread.setName("Closing System...");
				clearTableAndLabel();
			}
		});
		JMenu menu = new JMenu("菜单(G)");
		menu.setMnemonic('G');
		menu.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		menuBar.add(menu);

		JMenuItem open = new JMenuItem("开启服务器");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sl != null && sl.isOpen)
					JOptionPane.showMessageDialog(frame, "服务器已打开！", "错误", JOptionPane.ERROR_MESSAGE);
				else
					sl = new Server_Listener();
			}
		});
		open.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
		menu.add(open);

		JMenuItem close = new JMenuItem("关闭服务器");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sl != null && sl.isOpen)
					sl.shutdown();
				else
					JOptionPane.showMessageDialog(frame, "服务器未打开！", "错误", JOptionPane.ERROR_MESSAGE);
				clearTableAndLabel();
			}
		});
		close.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
		menu.add(close);

		JMenuItem exit = new JMenuItem("退出");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sl == null)
					System.exit(0);
				if (closeThread != null) {
					JOptionPane.showMessageDialog(frame, "服务器正在退出，请稍后。", "警告", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (sl.isOpen)
					sl.shutdown();
				closeThread = new Thread(new Runnable() {
					@Override
					public void run() {
						while (true) {
							if (sl.isOpen)
								continue;
							System.exit(0);
						}
					}
				});
				closeThread.start();
				closeThread.setName("Closing System...");
				clearTableAndLabel();
			}
		});
		exit.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
		menu.add(exit);

		JMenuItem forceExit = new JMenuItem("强制退出");
		forceExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(frame, "真的要强退吗？（有可能导致数据丢失）", "警告", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE) == 0) {
					Write_Log.writeActivity("MAIN", "System is forced to logout!");
					System.exit(0);
				}

			}
		});
		menu.add(forceExit);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(41, 141, 492, 197);
		frame.getContentPane().add(scrollPane);

		table = new JTable(tablemodel) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// 表格不允许被编辑 }
		};
		tablemodel = new DefaultTableModel();
		table.setEnabled(true);
		table.setAutoResizeMode(5);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);// 表头不可拖动
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
			},
			new String[] {
				"ID", "IP", "Nickname", "Imgid"
			}
		));
		tablemodel = (DefaultTableModel) table.getModel();
		tablemodel.setRowCount(0);// 清除原有行
		scrollPane.setViewportView(table);

		label = new JLabel("在线人数：0人");
		label.setFont(new Font("黑体", Font.PLAIN, 17));
		label.setBounds(41, 100, 160, 24);
		frame.getContentPane().add(label);

		JButton refreshdb = new JButton("刷新数据库");
		refreshdb.setFont(new Font("微软雅黑 Light", Font.PLAIN, 23));
		refreshdb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**** 用线程防止主系统线程阻塞 */
				new Thread(new Runnable() {
					@Override
					public void run() {
						sl.refreshdb(false);
					}
				}).start();
			}
		});
		refreshdb.setBounds(41, 360, 180, 62);
		frame.getContentPane().add(refreshdb);

		JButton showHall = new JButton("显示/关闭游戏大厅");
		showHall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sl != null) {
					sl.reverseShow();
				}
			}
		});
		showHall.setFont(new Font("微软雅黑 Light", Font.PLAIN, 23));
		showHall.setBounds(231, 360, 302, 62);
		frame.getContentPane().add(showHall);

		JButton button = new JButton("断开连接");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rows[] = table.getSelectedRows();
				if (rows.length == 0) {
					JOptionPane.showMessageDialog(null, "没有选中的连接！", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (JOptionPane.showConfirmDialog(null, "是否断开选中连接", "警告",
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					Vector<String> arr = new Vector<String>();
					for (int i = 0; i < rows.length; i++)
						arr.add((String) table.getValueAt(rows[i], 0));
					sl.Kickone(arr);
				}
			}
		});
		button.setFont(new Font("微软雅黑 Light", Font.PLAIN, 16));
		button.setBounds(159, 100, 122, 26);
		frame.getContentPane().add(button);

		JButton showTask = new JButton("显示/关闭监视器");
		showTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Write_Log.revShow();
			}
		});
		showTask.setFont(new Font("微软雅黑 Light", Font.PLAIN, 23));
		showTask.setBounds(231, 433, 302, 62);
		frame.getContentPane().add(showTask);

		JButton searchAcc = new JButton("查询账号");
		searchAcc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sl != null && sl.isOpen) {
					String idstr = JOptionPane.showInputDialog(null, "请输入账号：", "账号查询", JOptionPane.PLAIN_MESSAGE);
					if (idstr != null) {
						int id = Integer.valueOf(idstr);
						Object acc = Account_Manage.getAcc(id);
						if (acc == null)
							JOptionPane.showMessageDialog(null, "无此账号信息！", "账号查询", JOptionPane.ERROR_MESSAGE);
						else
							new Acc_Info(acc);
					}
				}
			}
		});
		searchAcc.setFont(new Font("微软雅黑 Light", Font.PLAIN, 23));
		searchAcc.setBounds(41, 432, 180, 62);
		frame.getContentPane().add(searchAcc);
		
		JButton button_1 = new JButton("刷新公告");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sl!=null&&sl.isOpen)
					sl.readNotice();
			}
		});
		button_1.setFont(new Font("微软雅黑 Light", Font.PLAIN, 23));
		button_1.setBounds(543, 360, 180, 62);
		frame.getContentPane().add(button_1);
		setUI(frame);
	}

	/**
	 * 更新列表
	 * 
	 * @param flag
	 *            true为增加，false为减少
	 */
	public static void UpdateTable(String id, String IP,String Nickname, String Imgid, boolean flag) {
		if (flag) {
			tablemodel.addRow(new Object[] { id,IP ,Nickname, Imgid });
			label.setText("在线人数：" + (++count) + "人");
		} else {
			int rowcount = table.getRowCount();
			if (rowcount > 0) {
				// 删除表中对应行
				int row;
				for (row = 0; row < rowcount; row++)
					if (((String) table.getValueAt(row, 0)).equals(id)) {
						tablemodel.removeRow(row);
						label.setText("在线人数：" + (--count) + "人");
						break;
					}
			}
		}
	}

	private void setUI(Component c) {
		// 设置当前系统外观
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (Exception e1) {
		}
		SwingUtilities.updateComponentTreeUI(c);
	}

}
