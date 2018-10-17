/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gobang;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class playinfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private int[] id = null;
	private String[] name = null;
	private String[] head = null;
	private transient ObjectOutputStream[] out = null;
	private transient ObjectInputStream[] in = null;
	private transient int size = 0;
	private transient int[] position = null;

	public playinfo() {
		id = new int[2];
		position = new int[2];
		name = new String[2];
		head = new String[2];
		out = new ObjectOutputStream[2];
		in = new ObjectInputStream[2];
	}

	public boolean contains(int id) {
		for (int i = 0; i < size; i++) {
			if (this.id[i] == id)
				return true;
		}
		return false;
	}

	public boolean add(int id, int position, String name, String head, Object out, Object in) {
		if (size == 2)
			return false;
		else {
			this.id[size] = id;
			this.position[size] = position;
			this.name[size] = name;
			this.head[size] = head;
			this.out[size] = (ObjectOutputStream) out;
			this.in[size] = (ObjectInputStream) in;
			size++;
			return true;
		}
	}

	public boolean remove(int id) {
		if (size == 0)
			return false;
		else {
			if (size == 1 && this.id[0] == id) {
				this.id[0] = 0;
				this.position[0] = -1;
				this.name[0] = null;
				this.head[0] = null;
				this.out[0] = null;
				this.in[0] = null;
			} else if (size == 2) {
				if (this.id[1] == id) {
					this.id[1] = 0;
					this.position[1] = -1;
					this.name[1] = null;
					this.head[1] = null;
					this.out[1] = null;
					this.in[1] = null;
				} else if (this.id[0] == id) {
					this.id[0] = 0;
					this.position[0] = -1;
					this.name[0] = null;
					this.head[0] = null;
					this.out[0] = null;
					this.in[0] = null;
					this.id[0] = this.id[1];
					this.position[0] = this.position[1];
					this.name[0] = this.name[1];
					this.head[0] = this.head[1];
					this.out[0] = this.out[1];
					this.in[0] = this.in[1];
					this.id[1] = 0;
					this.position[1] = -1;
					this.name[1] = null;
					this.head[1] = null;
					this.out[1] = null;
					this.in[1] = null;
				} else
					return false;
			} else
				return false;
			size--;
			return true;
		}

	}

	public int getid(int i) {
		return this.id[i];
	}

	public String getname(int i) {
		return this.name[i];
	}

	public String gethead(int i) {
		return this.head[i];
	}

	public ObjectInputStream getin(int i) {
		return this.in[i];
	}

	public ObjectOutputStream getout(int i) {
		return this.out[i];
	}

	public int getnum() {
		return size;
	}

	public int getPosition(int i) {
		return position[i];
	}
}
