import java.util.ArrayList;
import java.util.List;

public class LeafNodeInt implements Node {

	private List<KeyValueInt> kvPairs;
	private Node next;

	public LeafNodeInt() {
		kvPairs = new ArrayList<KeyValueInt>();
	}

	public LeafNodeInt(KeyValueInt keyValue) {
		kvPairs = new ArrayList<KeyValueInt>();
		kvPairs.add(keyValue);
	}

	public int size() {
		return kvPairs.size();
	}

	public List<KeyValueInt> getKVPairs() {
		return kvPairs;
	}

	public int getKey(int index) {
		return kvPairs.get(index).getKey();
	}

	public String getValue(int index) {
		return kvPairs.get(index).getValue();
	}

	public KeyValueInt getKeyValue(int index) {
		return kvPairs.get(index);
	}

	public void add(KeyValueInt keyValue) {
		kvPairs.add(keyValue);
	}

	public void add(int index, KeyValueInt keyValue) {
		kvPairs.add(index, keyValue);
	}

	public void remove(int index) {
		kvPairs.remove(index);
	}

	public void remove(KeyValueInt keyValue) {
		kvPairs.remove(keyValue);
	}

	public void setKey(int index, int key) {
		kvPairs.get(index).setKey(key);
	}

	public void setValue(int index, String value) {
		kvPairs.get(index).setValue(value);
	}

	/**
	 * @return the next
	 */
	public Node getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public void setNext(Node next) {
		this.next = next;
	}

	public String toString() {
		return "kvPairs: " + kvPairs;
	}
}
