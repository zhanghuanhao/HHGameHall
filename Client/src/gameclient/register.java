/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gameclient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class register {

	private static JFrame Register;
	private JTextField name;
	private JPasswordField pass;
	private JPasswordField pass1;
	private JTextField question;
	private JTextField answer;
	private static JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					register window = new register();
					register.Register.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public register() {
		initialize();
	}
	
	private void initialize() {
		Register = new JFrame();
		Register.setResizable(false);
		Register.setTitle("注册");
		Register.setBounds(100, 100, 450, 300);
		Register.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Register.setLocationRelativeTo(null);
		Register.getContentPane().setLayout(null);
		Register.setVisible(true);
		
		name = new JTextField();
		name.setBounds(108, 41, 217, 21);
		Register.getContentPane().add(name);
		name.setColumns(10);
		
		pass = new JPasswordField();
		pass.setBounds(108, 72, 217, 21);
		Register.getContentPane().add(pass);
		
		pass1 = new JPasswordField();
		pass1.setBounds(108, 103, 217, 21);
		Register.getContentPane().add(pass1);
		
		question = new JTextField();
		question.setBounds(108, 134, 217, 21);
		Register.getContentPane().add(question);
		question.setColumns(10);
		
		answer = new JTextField();
		answer.setBounds(108, 165, 217, 21);
		Register.getContentPane().add(answer);
		answer.setColumns(10);
		
		JButton btnNewButton = new JButton("提交");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    if(name.getText().equals(""))label.setText("请输入昵称！");
				else if(pass.getPassword().length==0)label.setText("请输入密码！");
				else if(!new String(pass1.getPassword()).equals(new String(pass.getPassword())))label.setText("密码不一致！");
				else if(question.getText().equals(""))label.setText("请输入密保问题！");
				else if(answer.getText().equals(""))label.setText("请输入密保答案！");
				else {
					label.setText("正在处理。。。");
				try {
					new c_connect(name.getText(),new String( pass.getPassword()),
							question.getText(),answer.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}}
		});
		btnNewButton.setBounds(170, 196, 93, 23);
		Register.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("昵称");
		lblNewLabel_1.setBounds(26, 44, 54, 15);
		Register.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("密码");
		lblNewLabel_2.setBounds(26, 75, 54, 15);
		Register.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("确认密码");
		lblNewLabel_3.setBounds(26, 106, 54, 15);
		Register.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("密保问题");
		lblNewLabel_4.setBounds(26, 137, 54, 15);
		Register.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("密保答案");
		lblNewLabel_5.setBounds(26, 168, 54, 15);
		Register.getContentPane().add(lblNewLabel_5);
		
	    label = new JLabel("");
		label.setBounds(136, 246, 172, 15);
		Register.getContentPane().add(label);
	}
	//提示
	public static void show(int sign) {
		if(sign==0) {
			JOptionPane.showMessageDialog(null,"连接超时", "注册失败！",JOptionPane.ERROR_MESSAGE);label.setText("");
		}
		if(sign==-1) {
			JOptionPane.showMessageDialog(null,"账号已存在！","提示",JOptionPane.INFORMATION_MESSAGE);label.setText("");
			
		}
	}
	public static void show(int sign,String acount) {
		if(sign==1) {
			JOptionPane.showMessageDialog(null,"账号为："+acount);
			Register.dispose();
		}
		
	}
}
