class IteratorString {
	LeafNodeString currentNode;
	KeyValueString currentPos;
	int index = 0;

	public IteratorString(Node node) {
		while (!(node instanceof LeafNodeString)) {
			InternalNodeString iNode = (InternalNodeString) node;
			if (!iNode.getChildren().isEmpty()) {
				node = iNode.getChildren().get(0);
			}
		}

		currentNode = (LeafNodeString) node;
		if (!currentNode.getKVPairs().isEmpty()) {
			currentPos = currentNode.getKVPairs().get(index);
			index++;

			if (index >= currentNode.size()) {
				currentNode = (LeafNodeString) currentNode.getNext();
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

	public KeyValueString next() {
		KeyValueString currentKeyValue = currentPos;

		if (currentNode != null && !currentNode.getKVPairs().isEmpty()) {
			currentPos = currentNode.getKVPairs().get(index);
			index++;

			if (index >= currentNode.size()) {
				currentNode = (LeafNodeString) currentNode.getNext();
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