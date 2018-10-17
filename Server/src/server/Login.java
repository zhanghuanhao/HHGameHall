/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package server;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import account.AESEncrypt;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class Login {
	private static String ACC;
	private static String PASS;
	private JFrame login;
	private JTextField acc;
	private JPasswordField password;
	private JLabel label;

	public Login() {
		initialAcc();
		initialize();
		login.setVisible(true);
	}

	/** 初始化账号，成功则程序继续，失败（找不到文件）则退出程序 */
	private void initialAcc() {
		try {
			File file = new File("Log/administrator");
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String acc = br.readLine();
				br.close();
				acc = AESEncrypt.Decrpyt(acc);
				String[] temp = acc.split("&");
				if (temp.length != 2)
					throw new Exception();
				ACC = temp[0];
				PASS = temp[1];
			} else
				throw new Exception();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "服务器配置错误！请检查文件。", "错误！", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private void initialize() {
		login = new JFrame();
		login.setResizable(false);
		login.setTitle("登录");
		login.setBounds(100, 100, 450, 300);
		login.setLocationRelativeTo(null);
		login.setDefaultCloseOperation(3);
		login.getContentPane().setLayout(null);

		this.acc = new JTextField();
		this.acc.setFont(new Font("宋体", 0, 20));
		this.acc.setBounds(128, 61, 194, 38);
		login.getContentPane().add(this.acc);
		this.acc.setColumns(10);

		JLabel label1 = new JLabel("账号");
		label1.setFont(new Font("宋体", 0, 20));
		label1.setBounds(42, 72, 61, 27);
		login.getContentPane().add(label1);

		label = new JLabel("");
		label.setFont(new Font("宋体", 0, 18));
		label.setBounds(129, 211, 201, 40);
		login.getContentPane().add(label);

		JButton Button = new JButton("登录");
		Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pass = new String(password.getPassword());
				if (ACC.equals(acc.getText()) && PASS.equals(pass))
					show(true);
				else
					show(false);
			}
		});

		JLabel label2 = new JLabel("密码");
		label2.setFont(new Font("宋体", 0, 20));
		label2.setBounds(42, 126, 54, 27);
		login.getContentPane().add(label2);
		Button.setBounds(130, 166, 93, 38);
		login.getContentPane().add(Button);

		this.password = new JPasswordField();
		password.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pass = new String(password.getPassword());
				if (ACC.equals(acc.getText()) && PASS.equals(pass))
					show(true);
				else
					show(false);
			}
		});
		this.password.setBounds(128, 114, 194, 38);
		login.getContentPane().add(this.password);

		JButton button = new JButton("修改密码");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String acc = JOptionPane.showInputDialog(login, "请输入账号", "修改密码", JOptionPane.OK_CANCEL_OPTION);
				if (acc == null || acc != null && !ACC.equals(acc))
					JOptionPane.showMessageDialog(login, "账号错误", "修改密码", JOptionPane.ERROR_MESSAGE);
				else {
					String pass = JOptionPane.showInputDialog(login, "请输入原密码", "修改密码", JOptionPane.OK_CANCEL_OPTION);
					if (pass == null || pass != null && !PASS.equals(pass))
						JOptionPane.showMessageDialog(login, "密码错误", "修改密码", JOptionPane.ERROR_MESSAGE);
					else {
						pass = JOptionPane.showInputDialog(login, "请输入新密码", "修改密码", JOptionPane.OK_CANCEL_OPTION);
						if (pass == null)
							JOptionPane.showMessageDialog(login, "修改失败", "修改密码", JOptionPane.ERROR_MESSAGE);
						else {
							try {
								PASS = pass;
								File file = new File("Log/administrator");
								pass = AESEncrypt.Encrypt(acc + "&" + pass);
								FileOutputStream fos = new FileOutputStream(file);
								PrintWriter pw = new PrintWriter(fos);
								pw.println(pass);
								pw.close();
								JOptionPane.showMessageDialog(login, "修改成功！", "修改密码", JOptionPane.INFORMATION_MESSAGE);
							} catch (Exception ssse) {
							}
						}
					}
				}
			}
		});
		button.setBounds(229, 166, 93, 38);
		login.getContentPane().add(button);
	}

	public void show(boolean flag) {
		if (flag) {
			login.dispose();
			new Server_Frame();
		} else {
			label.setText("账号或密码错误！");
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Login();
			}
		});
	}
}