public class KeyNodeIntPair {

	private Integer key;
	private Node node;

	public KeyNodeIntPair(Integer key, Node node) {
		this.key = key;
		this.node = node;
	}

	/**
	 * @return the key
	 */
	public Integer getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(Integer key) {
		this.key = key;
	}

	/**
	 * @return the child
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param child
	 *            the child to set
	 */
	public void setNode(Node child) {
		this.node = child;
	}

	public String toString() {
		return key + " " + node;
	}

}
