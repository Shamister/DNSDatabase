import java.util.ArrayList;
import java.util.List;

public class LeafNodeString implements Node {

	private List<KeyValueString> kvPairs;
	private Node next;

	public LeafNodeString() {
		kvPairs = new ArrayList<KeyValueString>();
	}

	public LeafNodeString(KeyValueString keyValue) {
		kvPairs = new ArrayList<KeyValueString>();
		kvPairs.add(keyValue);
	}

	public int size() {
		return kvPairs.size();
	}

	public List<KeyValueString> getKVPairs() {
		return kvPairs;
	}

	public String getKey(int index) {
		return kvPairs.get(index).getKey();
	}

	public int getValue(int index) {
		return kvPairs.get(index).getValue();
	}

	public KeyValueString getKeyValue(int index) {
		return kvPairs.get(index);
	}

	public void add(KeyValueString keyValue) {
		kvPairs.add(keyValue);
	}

	public void add(int index, KeyValueString keyValue) {
		kvPairs.add(index, keyValue);
	}

	public void remove(int index) {
		kvPairs.remove(index);
	}

	public void remove(KeyValueString keyValue) {
		kvPairs.remove(keyValue);
	}

	public void setKey(int index, String key) {
		kvPairs.get(index).setKey(key);
	}

	public void setValue(int index, int value) {
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
