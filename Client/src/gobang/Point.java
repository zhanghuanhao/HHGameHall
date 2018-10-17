/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gobang;
import java.awt.*;
public class Point
{  
    private int x;//棋子在棋盘中的x索引值  
    private int y;//棋子在棋盘中的y索引值  
    private Color color;//颜色  
    public static int DIAMETER=30;//直径  
    public Point(int x,int y,Color color)
    {  
        this.x=x;  
        this.y=y;  
        this.color=color;  
    }  
    //得到棋子在棋盘中的x索引值  
    public int getX()
    {  
        return x;  
    }  
    //得到棋子在棋盘中的y索引值  
    public int getY()
    {  
        return y;  
    }  
    //得到棋子颜色  
    public Color getColor()
    {  
        return color;  
    }  
}