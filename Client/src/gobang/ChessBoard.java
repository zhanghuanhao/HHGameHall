/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gobang;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;  
import java.awt.event.MouseEvent;
//五子棋-棋盘类
@SuppressWarnings("serial")
public class ChessBoard extends JPanel implements MouseListener
{  
    public static int MARGIN=30;//边距  
    public static int GRID_SPAN=35;//网格间距  
    public static int ROWS=15;//棋盘行数  
    public static int COLS=15;//棋盘列数  
    Point[] chessList=new Point[(ROWS+1)*(COLS+1)];//初始化每个数组元素为null  
    boolean isBack=true;//默认开始是黑棋先下  
    boolean gameOver=false;//游戏是否结束  
    int chessCount;//当前棋盘的棋子个数  
    int xIndex,yIndex;//当前刚下棋子的索引  
    private gamegobang game=null;
    private boolean pos=false;
    private StartChessJFrame chess=null;
    
    public ChessBoard(gamegobang g,StartChessJFrame s)
    {  
    	game=g;chess=s;
    	if(game.getpos()==0)pos=true;
    	else pos=false;
        setBackground(Color.LIGHT_GRAY);//设置背景颜色为灰色  
        addMouseListener(this);//添加事件监听器  
        addMouseMotionListener(new MouseMotionListener()
        {
        	//匿名内部类     
            @Override  
            public void mouseMoved(MouseEvent e) {  
                int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
                int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;//将鼠标单击的坐标位置转化为网格索引  
                if(x1<0||x1>ROWS||y1<0||y1>COLS||gameOver||findChess(x1,y1))
                {
                	//游戏已经结束，不能下；落在棋盘外，不能下；x，y位置已经有棋子存在，不能下  
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));//设置成默认形状  
                }
                else
                {  
                	if(pos==isBack)
                    setCursor(new Cursor(Cursor.HAND_CURSOR));//设置成手型  
                }  
            }
            @Override
            public void mouseDragged(MouseEvent e){}
        });
    }
    //绘制
    public void paintComponent(Graphics g)
    {  
        super.paintComponent(g);//画棋盘  
        for(int i=0;i<=ROWS;i++)
        {
        	//画横线  
            g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);  
        }  
        for(int i=0;i<=COLS;i++)
        {
        	//画竖线  
            g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN,MARGIN+ROWS*GRID_SPAN);  
        }  
        //画棋子 
        for(int i=0;i<chessCount;i++)
        {  
            int xPos=chessList[i].getX()*GRID_SPAN+MARGIN;//网格交叉的x坐标  
            int yPos=chessList[i].getY()*GRID_SPAN+MARGIN;//网格交叉的y坐标  
            g.setColor(chessList[i].getColor());//设置颜色  
            g.fillOval(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER);  
            if(i==chessCount-1)
            {  
                g.setColor(Color.red);//标记最后一个棋子为红色  
                g.drawRect(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER);  
            }  
        }  
    }
    @Override
    public void mousePressed(MouseEvent e)
    {
    	//鼠标按键在组件上按下时调用  
        if(gameOver)//游戏已经结束，不能下  
            return ;  
        if(pos!=isBack)return;
        xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
        yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;//将鼠标单击的坐标位置转化为网格索引  
        if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS)//棋子落在棋盘外，不能下  
            return ;  
        if(findChess(xIndex,yIndex))//x,y位置已经有棋子存在，不能下  
            return ;
        
        game.send(xIndex+"%"+yIndex);
        
       
    }  
    @Override  
    public void mouseClicked(MouseEvent e) {}  
    @Override  
    public void mouseReleased(MouseEvent e) {}
    @Override  
    public void mouseEntered(MouseEvent e) {        
    }
    @Override  
    public void mouseExited(MouseEvent e){}
    
    public void getpaint(String result) {
    	 if(result.equals("restart")) { 
    		 restartGame();
    		 return;
    		 }
    	 xIndex=Integer.parseInt(result.substring(0,result.indexOf("%")));
    	 yIndex=Integer.parseInt(result.substring(result.indexOf("%")+1));
    	 String colorName=isBack ? "黑棋" : "白棋";  
    	 Point ch=new Point(xIndex,yIndex,isBack ? Color.black : Color.white);  
         chessList[chessCount++]=ch;  
         repaint();//通知系统重新绘制  
         if(isWin())
         {  
             String msg=String.format("%s胜", colorName);
             chess.grade(colorName);
             game.send("over");
             JOptionPane.showMessageDialog(this, msg);  
             gameOver=true; 
             int res=JOptionPane.showConfirmDialog(null, "是否继续下一盘对局？","是否继续",JOptionPane.YES_NO_OPTION);
             if(res==JOptionPane.YES_OPTION) {
            	 game.send("ready");
             }
             else {
            	 game.send("out");
             }
         }  
         else if(chessCount==(COLS+1)*(ROWS+1))  
         {  
             String msg=String.format("双方打平");  
             JOptionPane.showMessageDialog(this,msg);  
             gameOver=true; 
             int res=JOptionPane.showConfirmDialog(null, "是否继续下一盘对局？","是否继续",JOptionPane.YES_NO_OPTION);
             if(res==JOptionPane.YES_OPTION) {
            	 game.send("ready");
             }
             else {
            	 game.send("out");
             }
             
         }  
         isBack=!isBack;  
    }
    
    private boolean findChess(int x,int y)
    {  
        for(Point c:chessList)
        {  
            if(c!=null&&c.getX()==x&&c.getY()==y)  
                return true;  
        }  
        return false;  
    }
    //判断那方赢
    private boolean isWin()
    {  
        int continueCount=1;//连续棋子的个数  
        for(int x=xIndex-1;x>=0;x--)
        {
        	//横向向左寻找
            Color c=isBack ? Color.black : Color.white;  
            if(getChess(x,yIndex,c)!=null)
                continueCount++;  
            else  
                break;  
        }  
        for(int x=xIndex+1;x<=ROWS;x++)
        {
        	//横向向右寻找  
            Color c=isBack ? Color.black : Color.white;  
            if(getChess(x,yIndex,c)!=null)
                continueCount++;  
            else  
                break;  
        }
        if(continueCount>=5)//判断记录数大于等于五，即表示此方获胜
        	return true;
        else  
            continueCount=1;
        for(int y=yIndex-1;y>=0;y--)
        {
        	//纵向向上寻找  
            Color c=isBack ? Color.black : Color.white;  
            if(getChess(xIndex,y,c)!=null)
            	continueCount++;
            else  
                break;  
        }  
        for(int y=yIndex+1;y<=ROWS;y++)
        {
        	//纵向向下寻找  
            Color c=isBack ? Color.black : Color.white;  
            if(getChess(xIndex,y,c)!=null)
            	continueCount++;  
            else  
                break;  
        }  
        if(continueCount>=5)//判断记录数大于等于五，即表示此方获胜  
            return true;  
        else  
            continueCount=1;
        for(int x=xIndex+1,y=yIndex-1;y>=0&&x<=COLS;x++,y--)
        {
        	//右下寻找  
            Color c=isBack ? Color.black : Color.white;  
            if(getChess(x,y,c)!=null)
                continueCount++;  
            else  
                break;  
        }  
        for(int x=xIndex-1,y=yIndex+1;y<=ROWS&&x>=0;x--,y++)
        {
        	//左上寻找  
            Color c=isBack ? Color.black : Color.white;  
            if(getChess(x,y,c)!=null)
                continueCount++;  
            else  
                break;  
        }  
        if(continueCount>=5)//判断记录数大于等于五，即表示此方获胜  
            return true;  
        else  
            continueCount=1;
        for(int x=xIndex-1,y=yIndex-1;y>=0&&x>=0;x--,y--)
        {
        	//左下寻找  
            Color c=isBack ? Color.black : Color.white;  
            if(getChess(x,y,c)!=null)
                continueCount++;
            else  
                break;  
        }
        for(int x=xIndex+1,y=yIndex+1;y<=ROWS&&x<=COLS;x++,y++)
        {
        	//右上寻找  
            Color c=isBack ? Color.black : Color.white;  
            if(getChess(x,y,c)!=null)
                continueCount++;  
            else  
                break;  
        }  
        if(continueCount>=5)//判断记录数大于等于五，即表示此方获胜  
            return true;  
        else  
            continueCount=1;  
        return false;         
    }  
    private Point getChess(int xIndex,int yIndex,Color color)
    {  
        for(Point c:chessList)
            if(c!=null&&c.getX()==xIndex&&c.getY()==yIndex&&c.getColor()==color)  
                return c;
        return null;  
    }  
    public void restartGame()
    {
    	//清除棋子  
        for(int i=0;i<chessList.length;i++)  
            chessList[i]=null;  
        //恢复游戏相关的变量值
        isBack=true;  
        gameOver=false;//游戏是否结束  
        chessCount=0;//当前棋盘的棋子个数  
        repaint();        
    }  
    public void goback()
    {  
        if(chessCount==0)  
            return ;  
        chessList[chessCount-1]=null;  
        chessCount--;  
        if(chessCount>0)
        {  
            xIndex=chessList[chessCount-1].getX();  
            yIndex=chessList[chessCount-1].getY();  
        }  
        isBack=!isBack;  
        repaint();  
    }  
    //Dimension:矩形  
    public Dimension getPreferredSize()
    {  
        return new Dimension(MARGIN*2+GRID_SPAN*COLS,MARGIN*2+GRID_SPAN*ROWS);  
    }
    
    
}