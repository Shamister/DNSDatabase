public class IteratorInt {
	LeafNodeInt currentNode;
	KeyValueInt currentPos;
	int index = 0;

	public IteratorInt(Node node) {
		while (!(node instanceof LeafNodeInt)) {
			InternalNodeInt iNode = (InternalNodeInt) node;
			if (!iNode.getChildren().isEmpty()) {
				node = iNode.getChildren().get(0);
			}
		}

		currentNode = (LeafNodeInt) node;
		if (!currentNode.getKVPairs().isEmpty()) {
			currentPos = currentNode.getKVPairs().get(index);
			index++;

			if (index >= currentNode.size()) {
				currentNode = (LeafNodeInt) currentNode.getNext();
				index = 0;

				if (currentNode == null) {
					currentPos = null;
				}
			}
		}
	}

	public boolean hasNext() {
		return (currentPos != null);
	}

	public KeyValueInt next() {
		KeyValueInt currentKeyValue = currentPos;

		if (currentNode != null && !currentNode.getKVPairs().isEmpty()) {
			currentPos = currentNode.getKVPairs().get(index);
			index++;

			if (index >= currentNode.size()) {
				currentNode = (LeafNodeInt) currentNode.getNext();
				index = 0;
			}
		} else {
			currentPos = null;
		}

		if (currentKeyValue != null) {
			return currentKeyValue;
		}

		throw new IndexOutOfBoundsException();
	}

}
