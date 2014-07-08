import java.util.ArrayList;
import java.util.List;

public class InternalNodeString implements Node {
	private List<String> keys;
	private List<Node> children;

	public InternalNodeString() {
		keys = new ArrayList<String>();
		children = new ArrayList<Node>();
	}

	public int size() {
		return keys.size();
	}

	public int childrenSize() {
		return children.size();
	}

	public List<String> getKeys() {
		return keys;
	}

	public List<Node> getChildren() {
		return children;
	}

	public String getKey(int index) {
		return keys.get(index);
	}

	public Node getChild(int index) {
		return children.get(index);
	}

	public void addKey(String key) {
		keys.add(key);
	}

	public void addKey(int index, String key) {
		keys.add(index, key);
	}

	public void addChild(Node node) {
		children.add(node);
	}

	public void addChild(int index, Node child) {
		children.add(index, child);
	}

	public void removeKey(int index) {
		keys.remove(index);
	}

	public void removeChild(int index) {
		children.remove(index);
	}

	public void setKey(int index, String key) {
		keys.set(index, key);
	}

	public void setChild(int index, Node node) {
		children.set(index, node);
	}

	public String toString() {
		return "Keys: " + keys + "\nChildren: " + children + "\n";
	}

}
