/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package mainhall;

import java.awt.*;   
import javax.swing.*;

import java.awt.event.*;   

public  class  emoji  extends  JWindow   {   
    private static final long serialVersionUID = 1L;  
    GridLayout   gridLayout1   =   new   GridLayout(60,10);   
    JLabel[]   ico=new   JLabel[900]; /*放表情*/  
    int  i;  
    JFrame  owner;  
    public  emoji(JFrame  test)   {   
        super(test);   
        this.owner=test;   
        try   {   
            init();   
            this.setAlwaysOnTop(true);   
        }   
        catch   (Exception   exception)   {   
            exception.printStackTrace();   
        }   
    }   
    private   void   init()   throws   Exception   {   
        this.setPreferredSize(new Dimension(30*18,30*10));  
        JPanel p = new JPanel();  
        p.setOpaque(true);
        p.setVisible(true); 
        this.setContentPane(p);  
        p.setLayout(gridLayout1);  
        p.setBackground(SystemColor.text);
        
        JScrollPane sp = new JScrollPane(p); 
        sp.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        sp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setOpaque(true); 
        sp.setVisible(true);
        this.setContentPane(sp);  
        
        String fileName = "";  
        for(i=0;i <ico.length;i++){   
            fileName= "res/emoji/"+(i+1)+".png";/*修改图片路径*/    
            ico[i] =new JLabel(new ImageIcon((fileName)));
            ico[i].setBorder(BorderFactory.createLineBorder(new Color(225,225,225), 1));  
            ico[i].setToolTipText((i+1)+"");  
            ico[i].addMouseListener(new   MouseAdapter(){   
                public   void   mouseClicked(MouseEvent  e){   
                    if(e.getButton()==1){  
                        JLabel cubl = (JLabel)(e.getSource());   
                        String num=cubl.getToolTipText();
                        GameLobby.write(num);
                        cubl.setBorder(BorderFactory.createLineBorder(new Color(225,225,225), 1));  
                        getObj().dispose();  
                    }  
                    
                }  
                @Override  
                public void mouseEntered(MouseEvent e) {  
                    ((JLabel)e.getSource()).setBorder(BorderFactory.createLineBorder(Color.BLUE));  
                }  
                @Override  
                public void mouseExited(MouseEvent e) {  
                    ((JLabel)e.getSource()).setBorder(BorderFactory.createLineBorder(new Color(225,225,225), 1));  
                }   
                  
            });   
            p.add(ico[i]);   
        }   
     
    }   
    @Override  
    public void setVisible(boolean show) {  
        if (show) {  
            determineAndSetLocation();  
        }  
        super.setVisible(show);  
    }     
    private void determineAndSetLocation() {  
        Point loc =  GameLobby.getPicBtn().getLocationOnScreen();//控件相对于屏幕的位置 
        setBounds(loc.x-getPreferredSize().width/3, loc.y-getPreferredSize().height,  
                getPreferredSize().width-100, getPreferredSize().height);  
    }  
    
    private JWindow getObj(){   
        return this;   
    }   
  
}  