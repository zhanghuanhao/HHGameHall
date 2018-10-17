/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package server;

/**
 * @author Zhh
 */
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Write_Log {

	/** 文件名以时间命名，如2018-03-04.log */
	private static SimpleDateFormat txtf = new SimpleDateFormat("yyyy-MM-dd");

	/** 每一步操作的时间 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss::SSS MM-dd-yyyy");

	/** 创建监听器 */
	private static JFrame frame = new JFrame("Task Manager");
	private static JTextArea text = new JTextArea();
	private static JScrollPane pane = new JScrollPane(text);
	private static boolean isShow = false;
	static {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(600, 800);
		frame.getContentPane().add(pane);
		text.setEditable(false);
		text.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
		frame.setVisible(true);
		text.setLineWrap(true); // 激活自动换行功能
		text.setWrapStyleWord(true); // 激活断行不断字功能
		isShow = true;
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isShow = false;
				e.getWindow().setVisible(isShow);
			}
		});
	}

	static void revShow() {
		isShow = !isShow;
		frame.setVisible(isShow);
		if (isShow)
			text.setCaretPosition(text.getText().length());
	}

	/**
	 * 写入监听器内
	 * 
	 * @param str
	 *            需写入的内容
	 */
	private synchronized static void writeText(String str) {
		Date date = new Date();
		text.append("\r\n" + sdf.format(date) + "\r\n");
		text.append(str + "\r\n");
		text.setCaretPosition(text.getText().length());
		frame.setVisible(isShow); // 使其在任务栏闪烁
	}

	/**
	 * 辅助存储
	 * 
	 * @param check
	 *            判断类型，0是Activity，1是ChatContent, 2是黑名单
	 * @param one
	 *            第一个参数
	 * @param two
	 *            第二个参数
	 */
	private static void writeHelp(int check, String one, String two) {
		String path = null;
		if (check == 0)
			path = "Activity/";
		else if (check == 1)
			path = "ChatContent/";
		else if (check == 2)
			path = "SomeList/";
		else
			return;
		try {
			// 创建日志目录
			File file = new File("Log/" + path);
			if (!file.exists())
				file.mkdirs();

			// 文件不存在时，创建文件
			if (check == 2)
				file = new File("Log/" + path + "black.list");
			else
				file = new File("Log/" + path + txtf.format(new Date()) + ".log");
			if (!file.exists())
				file.createNewFile();
			// 追加方式写文件
			FileWriter fw = new FileWriter(file, true);
			if (check == 2) {
				// one是ip
				fw.write(one + "\r\n");
			} else {
				fw.write(sdf.format(new Date()) + "\r\n");
				fw.flush();
				if (check == 0)
					fw.write("host: " + one + "\r\nactivity: " + two + "\r\n\r\n");
				else if (check == 1)
					fw.write("Message: \tIP: " + two + "\r\n" + one + "\r\n\r\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 日志读写
	 * 
	 * @param host
	 *            由host操作的
	 * @param msg
	 *            进行的操作
	 */
	public static void writeActivity(String host, String msg) {
		writeText(host + ": " + msg);
		writeHelp(0, host, msg);
	}

	/**
	 * 写聊天内容的日志
	 * 
	 * @param content
	 *            聊天记录
	 * @param ip
	 *            发送方的ip地址
	 */
	public static void Write_Chat(String content, String ip) {
		writeHelp(1, content, ip);
	}

	public static void WriteBlack(String ip) {
		StringBuilder sb = new StringBuilder();
		sb.append("IP: ");
		sb.append(ip);
		sb.append("\r\n");
		sb.append("Now it's in the blacklist.");
		writeText(sb.toString());
		writeHelp(2, ip, null);
	}
}
