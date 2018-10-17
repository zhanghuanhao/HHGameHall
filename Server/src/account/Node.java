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
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 是否为叶子节点 */
	protected boolean isLeaf;
	/** 是否为根节点 */
	protected boolean isRoot;
	/** 父节点 */
	protected Node parent;
	/** 叶节点的前节点 */
	protected Node previous;
	/** 叶节点的后节点 */
	protected Node next;

	/** 节点的关键字 */
	protected List<Entry<Comparable, Object>> entries;

	/** 子节点 */
	protected List<Node> children;

	/** 每个叶子节点对应的索引文件 */
	protected File file;


	
	
	public Node(boolean isLeaf) {
		this.isLeaf = isLeaf;
		entries = new ArrayList<Entry<Comparable, Object>>();

		if (!isLeaf) {
			children = new ArrayList<Node>();
		}
	}

	public Node(boolean isLeaf, boolean isRoot) {
		this(isLeaf);
		this.isRoot = isRoot;
	}

	public Object get(Comparable key) {

		// 如果是叶子节点
		if (isLeaf) {
			for (Entry<Comparable, Object> entry : entries) {
				if (entry.getKey().compareTo(key) == 0) {
					// 返回找到的对象
					return entry.getValue();
				}
			}
			// 未找到所要查询的对象
			return null;

			// 如果不是叶子节点
		} else {
			// 如果key小于等于节点最左边的key，沿第一个子节点继续搜索
			if (key.compareTo(entries.get(0).getKey()) <= 0) {
				return children.get(0).get(key);
				// 如果key大于节点最右边的key，沿最后一个子节点继续搜索
			} else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
				return children.get(children.size() - 1).get(key);
				// 否则沿比key大的前一个子节点继续搜索
			} else {
				for (int i = 0; i < entries.size(); i++) {
					if (entries.get(i).getKey().compareTo(key) <= 0 && entries.get(i + 1).getKey().compareTo(key) > 0) {
						return children.get(i).get(key);
					}
				}
			}
		}

		return null;
	}

	public void insertOrUpdate(Comparable key, Object obj, BplusTree tree) {

		// 如果是叶子节点
		if (isLeaf) {
			// 不需要分裂，直接插入或更新
			if (contains(key) || entries.size() < tree.order) {
				insertOrUpdate(key, obj);
				if (parent != null) {
					// 更新父节点
					parent.updateInsert(tree);
				}

				// 需要分裂
			} else {
				// 分裂成左右两个节点
				Node left = new Node(true);
				Node right = new Node(true);
				// 设置链接
				if (previous != null) {
					previous.next=(left);
					left.previous=(previous);
				}
				if (next != null) {
					next.previous=(right);
					right.next=(next);
				}
				if (previous == null) {
					tree.head = left;
				}

				left.next=(right);
				right.previous=(left);
				previous = null;
				next = null;

				// 左右两个节点关键字长度
				int leftSize = (tree.order + 1) / 2 + (tree.order + 1) % 2;
				int rightSize = (tree.order + 1) / 2;
				// 复制原节点关键字到分裂出来的新节点
				insertOrUpdate(key, obj);
				for (int i = 0; i < leftSize; i++) {
					left.entries.add(entries.get(i));
				}
				for (int i = 0; i < rightSize; i++) {
					right.entries.add(entries.get(leftSize + i));
				}

				// 如果不是根节点
				if (parent != null) {
					// 调整父子节点关系
					int index = parent.children.indexOf(this);
					parent.children.remove(this);
					left.parent=(parent);
					right.parent=(parent);
					parent.children.add(index, left);
					parent.children.add(index + 1, right);
					entries=(null);
					children=(null);

					// 父节点插入或更新关键字
					parent.updateInsert(tree);
					parent=(null);
					// 如果是根节点
				} else {
					isRoot = false;
					Node parent = new Node(false, true);
					tree.root = parent;
					left.parent=(parent);
					right.parent=(parent);
					parent.children.add(left);
					parent.children.add(right);
					entries=(null);
					children=(null);

					// 更新根节点
					parent.updateInsert(tree);
				}

			}

			// 如果不是叶子节点
		} else {
			// 如果key小于等于节点最左边的key，沿第一个子节点继续搜索
			if (key.compareTo(entries.get(0).getKey()) <= 0) {
				children.get(0).insertOrUpdate(key, obj, tree);
				// 如果key大于节点最右边的key，沿最后一个子节点继续搜索
			} else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
				children.get(children.size() - 1).insertOrUpdate(key, obj, tree);
				// 否则沿比key大的前一个子节点继续搜索
			} else {
				for (int i = 0; i < entries.size(); i++) {
					if (entries.get(i).getKey().compareTo(key) <= 0 && entries.get(i + 1).getKey().compareTo(key) > 0) {
						children.get(i).insertOrUpdate(key, obj, tree);
						break;
					}
				}
			}
		}
	}

	/** 插入节点后中间节点的更新 */
	protected void updateInsert(BplusTree tree) {

		validate(this, tree);

		// 如果子节点数超出阶数，则需要分裂该节点
		if (children.size() > tree.order) {
			// 分裂成左右两个节点
			Node left = new Node(false);
			Node right = new Node(false);
			// 左右两个节点关键字长度
			int leftSize = (tree.order + 1) / 2 + (tree.order + 1) % 2;
			int rightSize = (tree.order + 1) / 2;
			// 复制子节点到分裂出来的新节点，并更新关键字
			for (int i = 0; i < leftSize; i++) {
				left.children.add(children.get(i));
				left.entries.add(new SimpleEntry(children.get(i).entries.get(0).getKey(), null));
				children.get(i).parent=(left);
			}
			for (int i = 0; i < rightSize; i++) {
				right.children.add(children.get(leftSize + i));
				right.entries.add(new SimpleEntry(children.get(leftSize + i).entries.get(0).getKey(), null));
				children.get(leftSize + i).parent=(right);
			}

			// 如果不是根节点
			if (parent != null) {
				// 调整父子节点关系
				int index = parent.children.indexOf(this);
				parent.children.remove(this);
				left.parent=(parent);
				right.parent=(parent);
				parent.children.add(index, left);
				parent.children.add(index + 1, right);
				entries=(null);
				children=(null);

				// 父节点更新关键字
				parent.updateInsert(tree);
				parent=(null);
				// 如果是根节点
			} else {
				isRoot = false;
				Node parent = new Node(false, true);
				tree.root = parent;
				left.parent=(parent);
				right.parent=(parent);
				parent.children.add(left);
				parent.children.add(right);
				entries=(null);
				children=(null);

				// 更新根节点
				parent.updateInsert(tree);
			}
		}
	}

	/** 调整节点关键字 */
	protected static void validate(Node node, BplusTree tree) {

		// 如果关键字个数与子节点个数相同
		if (node.entries.size() == node.children.size()) {
			for (int i = 0; i < node.entries.size(); i++) {
				Comparable key = node.children.get(i).entries.get(0).getKey();
				if (node.entries.get(i).getKey().compareTo(key) != 0) {
					node.entries.remove(i);
					node.entries.add(i, new SimpleEntry(key, null));
					if (!node.isRoot) {
						validate(node.parent, tree);
					}
				}
			}
			// 如果子节点数不等于关键字个数但仍大于M / 2并且小于M，并且大于2
		} else if (node.isRoot && node.children.size() >= 2 || node.children.size() >= tree.order / 2
				&& node.children.size() <= tree.order && node.children.size() >= 2) {
			node.entries.clear();
			for (int i = 0; i < node.children.size(); i++) {
				Comparable key = node.children.get(i).entries.get(0).getKey();
				node.entries.add(new SimpleEntry(key, null));
				if (!node.isRoot) {
					validate(node.parent, tree);
				}
			}
		}
	}

	/** 删除节点后中间节点的更新 */
	protected void updateRemove(BplusTree tree) {

		validate(this, tree);

		// 如果子节点数小于M / 2或者小于2，则需要合并节点
		if (children.size() < tree.order / 2 || children.size() < 2) {
			if (isRoot) {
				// 如果是根节点并且子节点数大于等于2，OK
				if (children.size() >= 2) {
					return;
					// 否则与子节点合并
				} else {
					Node root = children.get(0);
					tree.root = root;
					root.parent=(null);
					root.isRoot=(true);
					entries=(null);
					children=(null);
				}
			} else {
				// 计算前后节点
				int currIdx = parent.children.indexOf(this);
				int prevIdx = currIdx - 1;
				int nextIdx = currIdx + 1;
				Node previous = null, next = null;
				if (prevIdx >= 0) {
					previous = parent.children.get(prevIdx);
				}
				if (nextIdx < parent.children.size()) {
					next = parent.children.get(nextIdx);
				}

				// 如果前节点子节点数大于M / 2并且大于2，则从其处借补
				if (previous != null && previous.children.size() > tree.order / 2
						&& previous.children.size() > 2) {
					// 前叶子节点末尾节点添加到首位
					int idx = previous.children.size() - 1;
					Node borrow = previous.children.get(idx);
					previous.children.remove(idx);
					borrow.parent=(this);
					children.add(0, borrow);
					validate(previous, tree);
					validate(this, tree);
					parent.updateRemove(tree);

					// 如果后节点子节点数大于M / 2并且大于2，则从其处借补
				} else if (next != null && next.children.size() > tree.order / 2
						&& next.children.size() > 2) {
					// 后叶子节点首位添加到末尾
					Node borrow = next.children.get(0);
					next.children.remove(0);
					borrow.parent=(this);
					children.add(borrow);
					validate(next, tree);
					validate(this, tree);
					parent.updateRemove(tree);

					// 否则需要合并节点
				} else {
					// 同前面节点合并
					if (previous != null && (previous.children.size() <= tree.order / 2
							|| previous.children.size() <= 2)) {

						for (int i = previous.children.size() - 1; i >= 0; i--) {
							Node child = previous.children.get(i);
							children.add(0, child);
							child.parent=(this);
						}
						previous.children=(null);
						previous.entries=(null);
						previous.parent=(null);
						parent.children.remove(previous);
						validate(this, tree);
						parent.updateRemove(tree);

						// 同后面节点合并
					} else if (next != null
							&& (next.children.size() <= tree.order / 2 || next.children.size() <= 2)) {

						for (int i = 0; i < next.children.size(); i++) {
							Node child = next.children.get(i);
							children.add(child);
							child.parent=(this);
						}
						next.children=(null);
						next.entries=(null);
						next.parent=(null);
						parent.children.remove(next);
						validate(this, tree);
						parent.updateRemove(tree);
					}
				}
			}
		}
	}

	public void remove(Comparable key, BplusTree tree) {
		// 如果是叶子节点
		if (isLeaf) {

			// 如果不包含该关键字，则直接返回
			if (!contains(key)) {
				return;
			}

			// 如果既是叶子节点又是跟节点，直接删除
			if (isRoot) {
				remove(key);
			} else {
				// 如果关键字数大于M / 2，直接删除
				if (entries.size() > tree.order / 2 && entries.size() > 2) {
					remove(key);
				} else {
					// 如果自身关键字数小于M / 2，并且前节点关键字数大于M / 2，则从其处借补
					if (previous != null && previous.entries.size() > tree.order / 2
							&& previous.entries.size() > 2 && previous.parent == parent) {
						int size = previous.entries.size();
						Entry<Comparable, Object> entry = previous.entries.get(size - 1);
						previous.entries.remove(entry);
						// 添加到首位
						entries.add(0, entry);
						remove(key);
						// 如果自身关键字数小于M / 2，并且后节点关键字数大于M / 2，则从其处借补
					} else if (next != null && next.entries.size() > tree.order / 2 && next.entries.size() > 2
							&& next.parent == parent) {
						Entry<Comparable, Object> entry = next.entries.get(0);
						next.entries.remove(entry);
						// 添加到末尾
						entries.add(entry);
						remove(key);
						// 否则需要合并叶子节点
					} else {
						// 同前面节点合并
						if (previous != null
								&& (previous.entries.size() <= tree.order / 2 || previous.entries.size() <= 2)
								&& previous.parent == parent) {
							for (int i = previous.entries.size() - 1; i >= 0; i--) {
								// 从末尾开始添加到首位
								entries.add(0, previous.entries.get(i));
							}
							remove(key);
							previous.parent=(null);
							previous.entries=(null);
							parent.children.remove(previous);
							// 更新链表
							if (previous.previous != null) {
								Node temp = previous;
								temp.previous.next=(this);
								previous = temp.previous;
								temp.previous=(null);
								temp.next=(null);
							} else {
								tree.head = this;
								previous.next=(null);
								previous = null;
							}
							// 同后面节点合并
						} else if (next != null
								&& (next.entries.size() <= tree.order / 2 || next.entries.size() <= 2)
								&& next.parent == parent) {
							for (int i = 0; i < next.entries.size(); i++) {
								// 从首位开始添加到末尾
								entries.add(next.entries.get(i));
							}
							remove(key);
							next.parent=(null);
							next.entries=(null);
							parent.children.remove(next);
							// 更新链表
							if (next.next != null) {
								Node temp = next;
								temp.next.previous=(this);
								next = temp.next;
								temp.previous=(null);
								temp.next=(null);
							} else {
								next.previous=(null);
								next = null;
							}
						}
					}
				}
				parent.updateRemove(tree);
			}
			// 如果不是叶子节点
		} else {
			// 如果key小于等于节点最左边的key，沿第一个子节点继续搜索
			if (key.compareTo(entries.get(0).getKey()) <= 0) {
				children.get(0).remove(key, tree);
				// 如果key大于节点最右边的key，沿最后一个子节点继续搜索
			} else if (key.compareTo(entries.get(entries.size() - 1).getKey()) >= 0) {
				children.get(children.size() - 1).remove(key, tree);
				// 否则沿比key大的前一个子节点继续搜索
			} else {
				for (int i = 0; i < entries.size(); i++) {
					if (entries.get(i).getKey().compareTo(key) <= 0 && entries.get(i + 1).getKey().compareTo(key) > 0) {
						children.get(i).remove(key, tree);
						break;
					}
				}
			}
		}
	}

	/** 判断当前节点是否包含该关键字 */
	protected boolean contains(Comparable key) {
		for (Entry<Comparable, Object> entry : entries) {
			if (entry.getKey().compareTo(key) == 0) {
				return true;
			}
		}
		return false;
	}

	/** 插入到当前节点的关键字中 */
	protected void insertOrUpdate(Comparable key, Object obj) {
		Entry<Comparable, Object> entry = new SimpleEntry<Comparable, Object>(key, obj);
		// 如果关键字列表长度为0，则直接插入
		if (entries.size() == 0) {
			entries.add(entry);
			return;
		}
		// 否则遍历列表
		for (int i = 0; i < entries.size(); i++) {
			// 如果该关键字键值已存在，则更新
			if (entries.get(i).getKey().compareTo(key) == 0) {
				entries.get(i).setValue(obj);
				return;
				// 否则插入
			} else if (entries.get(i).getKey().compareTo(key) > 0) {
				// 插入到链首
				if (i == 0) {
					entries.add(0, entry);
					return;
					// 插入到中间
				} else {
					entries.add(i, entry);
					return;
				}
			}
		}
		// 插入到末尾
		entries.add(entries.size(), entry);
	}

	/** 删除节点 */
	protected void remove(Comparable key) {
		int index = -1;
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getKey().compareTo(key) == 0) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			entries.remove(index);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("isRoot: ");
		sb.append(isRoot);
		sb.append(", ");
		sb.append("isLeaf: ");
		sb.append(isLeaf);
		sb.append(", ");
		sb.append("keys: ");
		for (Entry entry : entries) {
			sb.append(entry.getKey());
			sb.append(", ");
		}
		return sb.toString();
	}

}