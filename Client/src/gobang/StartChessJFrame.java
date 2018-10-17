/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gobang;
import javax.swing.*;

import java.awt.*;  
//五子棋的主框架，程序启动类
public class StartChessJFrame extends JFrame
{  
    
	private static final long serialVersionUID = -54622384713078023L;
	private ChessBoard chessBoard;//对战面板  
    private Panel toolbartop;//顶部条
    private JLabel headleft=null;
    private JLabel nameleft=null;
    private JLabel content=null;
    private JLabel headright=null;
    private JLabel nameright=null;
    private int white=0;
    private int black=0; 
    private gamegobang game=null;
    private playinfo info=null;
    public StartChessJFrame(gamegobang g)
    {
    	game=g;
        setTitle("五子棋");//设置标题 
        setLocationRelativeTo(null);
        chessBoard=new ChessBoard(game,this);       
        info=game.getinfo();
        toolbartop=new Panel();
        headleft=new JLabel(new ImageIcon("res/head/"+info.gethead(0)));
        nameleft=new JLabel(info.getname(0));
        nameleft.setFont(new Font("宋体",Font.BOLD,25));
        content=new JLabel("黑方0:0白方");
        content.setFont(new Font("宋体",Font.BOLD,25));
        nameright=new JLabel(info.getname(1));
        nameright.setFont(new Font("宋体",Font.BOLD,25));
        headright=new JLabel(new ImageIcon("res/head/"+info.gethead(1)));
        toolbartop.add(headleft);
        toolbartop.add(nameleft);
        toolbartop.add(content);
        toolbartop.add(nameright);
        toolbartop.add(headright);
        getContentPane().add(toolbartop,BorderLayout.NORTH);
        
        
        getContentPane().add(chessBoard);//将面板对象添加到窗体上  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置界面关闭事件 
       
        pack();//自适应大小  
     
    }  
    public void getpaint(String msg) {
    	chessBoard.getpaint(msg);
    }
    public void grade(String color) {
    	if(color.equals("黑棋"))black++;
    	else white++;
    	content.setText("黑方"+black+":"+white+"白方");
    }
    //结束游戏
    public void over() {
    	JOptionPane.showMessageDialog(null, "因玩家数量不足，游戏结束！", "游戏结束", JOptionPane.OK_OPTION);
    	this.dispose();
    }
}