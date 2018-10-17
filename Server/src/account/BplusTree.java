/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package account;
/**
 * @author Zhh
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;



@SuppressWarnings("rawtypes")
public class BplusTree implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 根节点 */
	Node root;
	/** 阶数，M值 */
	int order;
	/** 叶子节点的链表头 */
	Node head;
	/** 账号ID */
	volatile int id;

	public Object get(Comparable key) {
		return root.get(key);
	}

	public void remove(Comparable key) {
		root.remove(key, this);

	}

	public int insertOrUpdate(Comparable key, Object obj) {
		root.insertOrUpdate(key, obj, this);
		return this.id++;
	}

	public BplusTree(int order) {
		if (order < 3) {
			System.out.print("order must be greater than 2");
			System.exit(0);
		}
		this.order = order;
		root = new Node(true, true);
		head = root;
		id = 10000000;
	}

	/**
	 * 
	 * @return 完成此动作的时间 s
	 */
	public long restore() {
		long start = System.currentTimeMillis();
		try {
			Node next = this.head;
			int count = 0;
			File file = new File("res/tree/node");
			if (!file.exists())
				file.mkdirs();
			while (true) {
				if (next == null)
					break;
				++count;
				file = new File("res/tree/node/" + String.valueOf(count) + ".txt");
				next.file = file;
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
				objectOutputStream.writeObject(next);
				objectOutputStream.close();
				next = next.next;
			}
			File treeFile = new File("res/tree/BplusTree.txt");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(treeFile));
			objectOutputStream.writeObject(this);
			objectOutputStream.close();
		} catch (Exception e) {
		}
		long end = System.currentTimeMillis();
		return (end-start)/1000;
	}

	// 测试
	public static void main(String[] args) throws Exception {
/*
		File f = new File("res/tree/BplusTree.txt");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		BplusTree tree = (BplusTree) ois.readObject();
		ois.close();
		Account acc = (Account) (tree.get(10000000));
		System.out.print(acc.password);
		*/
		
		long start = System.currentTimeMillis();
		System.out.println("Start reading...");
		BplusTree tree = new BplusTree(1000);
		int counter = 0;
		for (long i = 0; i <= 10000; i++) {
			counter = (counter + 1) % 255;
			if(counter==0) counter++;
			Account acc = new Account(tree.id, String.valueOf(111), "嘿"+i+"号", "我尼玛", "我尼玛");
			acc.setLastLogin(new Date().toString());
			acc.setLastLogout(new Date().toString());
			acc.setHead(counter + ".png");
			tree.insertOrUpdate(tree.id, acc);
			
		}
		Account acc = new Account(888888, String.valueOf(123456), "Super Root", "你猜", "你猜");
		acc.setLastLogin(new Date().toString());
		acc.setLastLogout(new Date().toString());
		acc.setHead("23.png");
		tree.insertOrUpdate(888888, acc);
		long end = System.currentTimeMillis();
		System.out.println("Using time: " + (end - start) / 1000 + "s");
		System.out.println("Start restoring...");
		System.out.println("Using time: " + tree.restore() + "s");
		
	}
}