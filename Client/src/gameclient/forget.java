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

public class forget {

	private static JFrame Forget;
	private JTextField acount;
	private static JTextField question;
	private JTextField answer;
	private JPasswordField pass;
	private JPasswordField pass1;
	private static JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					forget window = new forget();
					forget.Forget.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public forget() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Forget = new JFrame();
		Forget.setTitle("忘记密码");
		Forget.setResizable(false);
		Forget.setBounds(100, 100, 450, 300);
		Forget.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Forget.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Forget.setLocationRelativeTo(null);
        Forget.getContentPane().setLayout(null);
        
        acount = new JTextField();
        acount.setBounds(99, 31, 252, 21);
        Forget.getContentPane().add(acount);
        acount.setColumns(10);
        
        question = new JTextField();
        question.setBounds(99, 62, 252, 21);
        Forget.getContentPane().add(question);
        question.setColumns(10);
        
        answer = new JTextField();
        answer.setBounds(99, 93, 252, 21);
        Forget.getContentPane().add(answer);
        answer.setColumns(10);
        
        pass = new JPasswordField();
        pass.setBounds(99, 124, 252, 21);
        Forget.getContentPane().add(pass);
        
        pass1 = new JPasswordField();
        pass1.setBounds(99, 155, 252, 21);
        Forget.getContentPane().add(pass1);
        
        JButton btnNewButton = new JButton("确定");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(acount.getText().equals(""))label.setText("请输入账号！");
				else if(question.getText().equals(""))label.setText("请获取密保问题！");
				else if(answer.getText().equals(""))label.setText("请输入密保答案！");
				else if(pass.getPassword().length==0)label.setText("请输入密码！");
				else if(!new String(pass1.getPassword()).equals(new String(pass.getPassword())))label.setText("密码不一致！");
				else {
					label.setText("正在处理。。。");
        		try {
					new c_connect(acount.getText(),new String( pass.getPassword()),
							answer.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        	}}
        });
        btnNewButton.setBounds(176, 186, 93, 23);
        Forget.getContentPane().add(btnNewButton);
        
        JLabel lblNewLabel = new JLabel("账号");
        lblNewLabel.setBounds(21, 34, 54, 15);
        Forget.getContentPane().add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("密保问题");
        lblNewLabel_1.setBounds(21, 65, 54, 15);
        Forget.getContentPane().add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("密保答案");
        lblNewLabel_2.setBounds(21, 96, 54, 15);
        Forget.getContentPane().add(lblNewLabel_2);
        
        JLabel lblNewLabel_3 = new JLabel("新密码");
        lblNewLabel_3.setBounds(21, 127, 54, 15);
        Forget.getContentPane().add(lblNewLabel_3);
        
        JLabel lblNewLabel_4 = new JLabel("确认密码");
        lblNewLabel_4.setBounds(21, 158, 54, 15);
        Forget.getContentPane().add(lblNewLabel_4);
        
        label = new JLabel("");
        label.setBounds(190, 246, 150, 15);
        Forget.getContentPane().add(label);
        
        JButton btnNewButton_1 = new JButton("获取问题");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(acount.getText().equals(""))label.setText("请输入账号！");
        		else{try {
					new c_connect(acount.getText());
				} catch (IOException e1) {e1.printStackTrace();}
        	}}
        });
        btnNewButton_1.setBounds(352, 61, 90, 23);
        Forget.getContentPane().add(btnNewButton_1);
        Forget.setVisible(true);
	}
	
	//提示
	public static void show(int sign) {
		if(sign==1) {
			JOptionPane.showMessageDialog(null,"密码重置成功！","提示",JOptionPane.OK_OPTION);
			Forget.dispose();
			}
		if(sign==0) {
			JOptionPane.showMessageDialog(null,"连接超时", "重置失败！",JOptionPane.ERROR_MESSAGE);
			label.setText("");
		}
	    if(sign==-1) {
	    	JOptionPane.showMessageDialog(null,"请检查密保问题与答案！","密码重置失败",JOptionPane.INFORMATION_MESSAGE);
	    	label.setText("");
	    	}
		if(sign==-2) {
			JOptionPane.showMessageDialog(null,"账号不存在！","提示",JOptionPane.INFORMATION_MESSAGE);
			label.setText("");
		}
		if(sign==-3) {
			label.setText("密保问题获取失败！");
		}
		if(sign==-4) {
			JOptionPane.showMessageDialog(null,"密保答案错误超过每日3次！","提示",JOptionPane.INFORMATION_MESSAGE);
			label.setText("");
		}
	}
	public static void show(int sign,String quest) {
		if(sign==2)question.setText(quest);
	}
}
