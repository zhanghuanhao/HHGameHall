/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package mainhall;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Font;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class personinfo extends JFrame{
private JLabel head=null;
private JLabel name=null;
private JLabel id=null;
private String prehead=null;
private String prename=null;
private heads choosehead=null;
private static boolean flag = false;
private JButton seek=null;
private String temphead=null;
private String tempname=null;
private Connect connect=null;
	
	public personinfo(Connect c) {
		choosehead=new heads(this);
		connect=c;
		setTitle("个人信息");
		initialize();
	}

	private void dis() {
		this.dispose();
	}
	private void initialize() {
		setBounds(100, 100, 450, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		head = new JLabel("head");
		head.setBounds(190, 10, 40, 40);
		getContentPane().add(head);
		
		name = new JLabel("name");
		name.setBounds(141, 87, 164, 40);
		name.setFont(new Font("宋体", Font.BOLD, 20));
		getContentPane().add(name);
		
		id = new JLabel("id:");
		id.setBounds(131, 174, 184, 15);
		id.setFont(new Font("宋体", Font.BOLD, 20));
		getContentPane().add(id);
		
		JButton confirm = new JButton("确定");
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dis();
			}
		});
		confirm.setBounds(114, 264, 80, 30);
		getContentPane().add(confirm);
		
		JButton save = new JButton("保存");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((!prehead.equals(temphead))||(!prename.equals(tempname))) {
					try {
						connect.change(temphead, tempname);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		save.setBounds(235, 264, 80, 30);
		getContentPane().add(save);
		
		seek = new JButton("浏览");
		seek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				if (!flag) {
					choosehead.setVisible(true);
					flag = true;
				} else {
					choosehead.setVisible(false);
					flag = false;
				}
			}
		});
		seek.setBounds(305, 19, 80, 30);
		getContentPane().add(seek);
		
		JButton changename = new JButton("修改");
		changename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input=JOptionPane.showInputDialog(null, "请输入新昵称：", prename);
				if(!input.equals("")) {
					setname(input);
				}
			}
		});
		changename.setBounds(305, 94, 80, 30);
		getContentPane().add(changename);
		
		
		setLocationRelativeTo(null);
	}
	
	public void show(String head,String name,String id) {
		this.head.setIcon(new ImageIcon("res/head/"+head));
		this.name.setText("昵称:"+name);
		this.id.setText("id:"+id);
		prehead=head;
		prename=name;
	}
	public JButton getPicBtn() {
		return seek;
	}
	public void sethead(String num) {
		if(num=="")return;
		temphead=num+".png";
		head.setIcon(new ImageIcon("res/head/"+temphead));
	}
	public void setname(String name) {
		tempname=name;
		this.name.setText("昵称:"+tempname);
	}
	public void showconfirm() {
		JOptionPane.showMessageDialog(null, "信息修改成功！");
	}
}
