/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package account;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Acc_Info extends JFrame {

	private static final long serialVersionUID = 1L;

	public Acc_Info(Object acc) {
		getContentPane().setBackground(Color.WHITE);
		setTitle("账号信息");
		setResizable(false);
		initialize(acc);
	}

	private void initialize(Object object) {
		setBounds(40, 40, 400, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		Account acc = (Account) object;

		JLabel Acc = new JLabel();
		Acc.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		Acc.setBounds(20, 0, 200, 40);
		getContentPane().add(Acc);
		Acc.setText("账号：" + acc.getId());

		JLabel Nickname = new JLabel();
		Nickname.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		Nickname.setBounds(20, 40, 200, 40);
		getContentPane().add(Nickname);
		Nickname.setText("昵称：" + acc.getNickname());

		JLabel Password = new JLabel();
		Password.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		Password.setBounds(20, 80, 200, 40);
		getContentPane().add(Password);
		Password.setText("密码：" + acc.getPassword());

		JLabel IsOnline = new JLabel();
		IsOnline.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		IsOnline.setBounds(20, 120, 200, 40);
		getContentPane().add(IsOnline);
		IsOnline.setText("是否登陆：" + acc.isOnline());

		JLabel Reset = new JLabel();
		Reset.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		Reset.setBounds(20, 160, 200, 40);
		getContentPane().add(Reset);
		Reset.setText("重置次数：" + acc.getReset());

		JLabel Serque = new JLabel();
		Serque.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		Serque.setBounds(20, 200, 350, 40);
		getContentPane().add(Serque);
		Serque.setText("密保问题：" + acc.getSerque());

		JLabel Serans = new JLabel();
		Serans.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		Serans.setBounds(20, 240, 350, 40);
		getContentPane().add(Serans);
		Serans.setText("密保答案：" + acc.getSerans());

		JLabel LastLogin = new JLabel();
		LastLogin.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		LastLogin.setBounds(20, 280, 350, 80);
		getContentPane().add(LastLogin);
		LastLogin.setText("<html><body>最后上线：<br>" + acc.getLastLogin() + "</body></html>");

		JLabel LastLogout = new JLabel();
		LastLogout.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		LastLogout.setBounds(20, 360, 350, 80);
		getContentPane().add(LastLogout);
		LastLogout.setText("<html><body>最后下线：<br>" + acc.getLastLogout() + "</body></html>");

		JLabel Headnum = new JLabel();
		Headnum.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		Headnum.setBounds(250, 40, 200, 80);
		getContentPane().add(Headnum);
		Headnum.setText("<html><body>头像编号：<br>" + acc.getHead() + "</body></html>");

		JLabel Head = new JLabel();
		Head.setBounds(286, 0, 40, 40);
		getContentPane().add(Head);
		Head.setIcon(new ImageIcon("res/head/" + acc.getHead()));
		JButton ReverseOnline = new JButton("改变状态");
		ReverseOnline.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		ReverseOnline.setBounds(230, 120, 120, 40);
		getContentPane().add(ReverseOnline);
			ReverseOnline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acc.setOnline(!acc.isOnline());
				IsOnline.setText("是否登陆：" + acc.isOnline());
			}
		});
		setVisible(true);
	}
}
