/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package mainhall;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;

public class emoji extends JWindow {
	private static final long serialVersionUID = 1L;
	private GridLayout gridLayout1 = new GridLayout(60, 10);
	private JLabel[] ico = new JLabel[900]; /* 放表情 */
	private int i;
	private JFrame owner;

	public emoji(JFrame test) {
		super(test);
		this.owner = test;
		try {
			init();
			this.setAlwaysOnTop(true);
		} catch (Exception exception) {
		}
	}

	private void init() throws Exception {
		this.setPreferredSize(new Dimension(30 * 18, 30 * 10));
		JPanel p = new JPanel();
		p.setOpaque(true);
		p.setVisible(true);
		this.setContentPane(p);
		p.setLayout(gridLayout1);
		p.setBackground(SystemColor.text);
		JScrollPane sp = new JScrollPane(p);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setOpaque(true);
		sp.setVisible(true);
		this.setContentPane(sp);
		String fileName = "";
		for (i = 0; i < ico.length; i++) {
			fileName = "res/emoji/" + (i + 1) + ".png";/* 修改图片路径 */
			ico[i] = new JLabel(new ImageIcon((fileName)));
			ico[i].setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
			ico[i].setToolTipText((i + 1) + "");
			ico[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 1) {
						try {
							JLabel cubl = (JLabel) (e.getSource());
							String num = cubl.getToolTipText();
							((Game_Lobby) owner).write(num);
							cubl.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
						} catch (Exception ee) {
						}
						getObj().dispose();
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.BLUE));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
				}
			});
			p.add(ico[i]);
		}
	}

	public void setVisible(boolean show, Point loc) {
		if (show) {
			setBounds(loc.x - getPreferredSize().width / 3, loc.y - getPreferredSize().height,
					getPreferredSize().width - 100, getPreferredSize().height);
		}
		this.setVisible(show);
	}

	private JWindow getObj() {
		return this;
	}
}