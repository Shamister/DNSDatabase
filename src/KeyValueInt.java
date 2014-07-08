public class KeyValueInt {
	private Integer key;
	private String value;

	public KeyValueInt(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public Integer getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(Integer key) {
		this.key = key;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return key + " " + value;
	}
}
