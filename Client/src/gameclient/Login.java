/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gameclient;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;

import account.Account;
import mainhall.GameLobby;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Login {

	private static JFrame login;
	private JTextField acount;
	private JPasswordField passwordField;
	private static  JLabel label;
    private static c_connect ccon=null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					Login window = new Login();
					window.login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		login = new JFrame();
		login.setResizable(false);
		login.setTitle("登录");
		login.setBounds(100, 100, 450, 300);
		login.setLocationRelativeTo(null);
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.getContentPane().setLayout(null);
		
		acount = new JTextField();
		acount.setFont(new Font("宋体", Font.PLAIN, 20));
		acount.setBounds(128, 61, 194, 38);
		login.getContentPane().add(acount);
		acount.setColumns(10);
		
		JLabel label1 = new JLabel("账号");
		label1.setFont(new Font("宋体", Font.PLAIN, 20));
		label1.setBounds(42, 72, 61, 27);
		login.getContentPane().add(label1);
		
		label = new JLabel("");
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		label.setBounds(129, 211, 201, 40);
		login.getContentPane().add(label);
		
		JButton Button = new JButton("登录");
		Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {       
				if(acount.getText().equals(""))label.setText("请输入账号！");
				else if(passwordField.getPassword().length==0)label.setText("请输入密码！");
				else {
					label.setText("正在连接。。。");
				try {
					ccon=new c_connect(acount.getText(),new String( passwordField.getPassword()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				}
			}
		});
		
		JLabel label2 = new JLabel("密码");
		label2.setFont(new Font("宋体", Font.PLAIN, 20));
		label2.setBounds(42, 126, 54, 27);
		login.getContentPane().add(label2);
		Button.setBounds(130, 166, 93, 38);
		login.getContentPane().add(Button);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(128, 114, 194, 38);
		login.getContentPane().add(passwordField);
		
		JButton Button1 = new JButton("注册");
		Button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new register();
			   
			}
		});
		Button1.setBounds(229, 166, 93, 38);
		login.getContentPane().add(Button1);
		
		JButton Button2 = new JButton("忘记密码");
		Button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new forget();
				
			}
		});
		Button2.setBounds(332, 130, 93, 23);
		login.getContentPane().add(Button2);

	}
	//提示
	public static void show(int sign)  {
		if(sign==-1) {
			JOptionPane.showMessageDialog(null,"连接超时", "登录失败！",JOptionPane.ERROR_MESSAGE);
		    label.setText("连接超时！");
		}
		if(sign==-2) {
			JOptionPane.showMessageDialog(null,"账号或密码错误！","登录失败！",JOptionPane.ERROR_MESSAGE);
			label.setText("");
		}
		if(sign==-3) {
			JOptionPane.showMessageDialog(null,"账号已登录！","登录失败！",JOptionPane.ERROR_MESSAGE);
			label.setText("");
		}
	}
	public static void show(int sign,Account acc) {
		if(sign==1) {
			label.setText("登录成功！");
			login.setVisible(false);
			try {
			  new GameLobby(acc,ccon.out(),ccon.in());
			} catch (IOException e) {
				e.printStackTrace();
			}
			}	
	}
}
