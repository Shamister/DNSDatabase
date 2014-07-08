/**
 * Implements a B+ tree in which the keys are Strings (with maximum length 60
 * characters) and the values are integers
 */

public class BPlusTreeString60toInt {
	public final int MAX_LEAF_KEYS = 3;
	public final int MAX_NODE_KEYS = 3;
	Node root;

	/**
	 * Returns the integer associated with the given key, or null if the key is
	 * not in the B+ tree.
	 */
	public Integer find(String key) {
		// YOUR CODE HERE
		// if root is empty, return null
		if (root == null)
			return null;
		else
			return find(key, root);
	}

	public Integer find(String key, Node node) {
		// if node is leaf node
		// find the value in the node by iterating each value of the node
		// if no key in the node matched, return null
		if (node instanceof LeafNodeString) {
			LeafNodeString lNode = (LeafNodeString) node;
			for (int i = 0; i < lNode.size(); i++) {
				KeyValueString keyValue = lNode.getKVPairs().get(i);
				if (key.equals(keyValue.getKey())) {
					return keyValue.getValue();
				}
			}
			return null;
		}
		// if node is internal node
		// find the order of the node and call find method in that branch
		else if (node instanceof InternalNodeString) {
			InternalNodeString iNode = (InternalNodeString) node;
			for (int i = 0; i < iNode.size(); i++) {
				if (key.compareTo(iNode.getKeys().get(i)) < 0) {
					return find(key, iNode.getChildren().get(i));
				}
			}
			return find(key, iNode.getChildren().get(iNode.size()));
		}
		return null;
	}

	/**
	 * Stores the value associated with the key in the B+ tree. If the key is
	 * already present, replaces the associated value. If the key is not
	 * present, adds the key with the associated value
	 * 
	 * @param value
	 * @param key
	 * @return whether pair was successfully added.
	 */
	public boolean put(String key, int value) {
		// YOUR CODE HERE
		// if root is empty
		// create new leaf and add the key-value
		if (root == null) {
			root = new LeafNodeString(new KeyValueString(key, value));
		} else {
			KeyNodeStringPair kChild = put(key, value, root);
			if (kChild != null) {
				InternalNodeString node = new InternalNodeString();
				node.getChildren().add(root); // node.child[0] = root
				node.getKeys().add(kChild.getKey()); // node.keys[0] = newKey
				node.getChildren().add(kChild.getNode()); // node.child[1] =
															// rightChild
				root = node;
			}
		}

		return true;
	}

	public KeyNodeStringPair put(String key, int value, Node node) {
		// if node is a leaf
		// then insert key-value in the correct place and return null
		// otherwise split the leaf and return the node
		if (node instanceof LeafNodeString) {
			LeafNodeString lNode = (LeafNodeString) node;
			if (lNode.size() < MAX_LEAF_KEYS) {
				// insert key-value in the correct place
				int size = lNode.size();
				for (int i = 0; i < size; i++) {
					KeyValueString keyValue = lNode.getKVPairs().get(i);
					if (key.compareTo(keyValue.getKey()) < 0) {
						lNode.getKVPairs().add(i,
								new KeyValueString(key, value));
						return null;
					}
				}
				// if all keys are smaller, add node at the end of list
				lNode.getKVPairs().add(new KeyValueString(key, value));
				return null;
			} else {
				return splitLeaf(key, value, lNode);
			}
			// if node is internal node
			// put the key-value into the correct place
		} else if (node instanceof InternalNodeString) {
			InternalNodeString iNode = (InternalNodeString) node;
			int size = iNode.size();
			for (int i = 0; i < size; i++) {
				if (key.compareTo(iNode.getKeys().get(i)) < 0) {
					KeyNodeStringPair keyNode = put(key, value, iNode
							.getChildren().get(i));
					// if key-value is null then return null
					if (keyNode == null) {
						return null;
					} else {
						return dealWithPromote(keyNode.getKey(),
								keyNode.getNode(), iNode);
					}
				}
			}
			// if all the keys are smaller than current key
			// put the key-value to the end of the list

			KeyNodeStringPair keyNode = put(key, value, iNode.getChildren()
					.get(iNode.size()));

			// if key-value is null, then return null
			if (keyNode == null) {
				return null;
			} else {
				return dealWithPromote(keyNode.getKey(), keyNode.getNode(),
						iNode);
			}
		}
		return null;
	}

	public KeyNodeStringPair dealWithPromote(String newKey, Node rightChild,
			InternalNodeString node) {

		// if new key is greater than the key in the last element of list
		// insert child into the node
		if (newKey.compareTo(node.getKeys().get(node.size() - 1)) > 0) {
			node.getKeys().add(newKey);
			node.getChildren().add(rightChild);
		} else {
			int size = node.size();
			for (int i = 0; i < size; i++) {
				if (newKey.compareTo(node.getKeys().get(i)) < 0) {
					node.getKeys().add(i, newKey);
					node.getChildren().add(i + 1, rightChild);
					break;
				}
			}
		}

		// determine the return value
		if (node.size() <= MAX_NODE_KEYS) {
			return null;
		}

		InternalNodeString sibling = new InternalNodeString();
		int mid = (node.size() + 1) / 2 - 1;
		// move node.keys from mid - node.size to sibling.node starting from 0
		int size = node.size();
		for (int i = mid + 1; i < size; i++) {
			sibling.getKeys().add(node.getKeys().get(mid + 1));
			node.getKeys().remove(mid + 1);
		}
		// move node.child from mid - node.size to sibling.child starting from
		// 0
		for (int i = mid + 1; i < size + 1; i++) {
			sibling.getChildren().add(node.getChildren().get(mid + 1));
			node.getChildren().remove(mid + 1);
		}
		String promoteKey = node.getKeys().get(mid);
		// remove key which is promoted from node
		node.getKeys().remove(mid);
		return new KeyNodeStringPair(promoteKey, sibling);
	}

	public KeyNodeStringPair splitLeaf(String key, int value,
			LeafNodeString node) {
		int size = node.size();

		boolean inserted = false;
		for (int i = 0; i < size; i++) {
			KeyValueString keyValue = node.getKVPairs().get(i);
			if (key.compareTo(keyValue.getKey()) < 0) {
				node.getKVPairs().add(i, new KeyValueString(key, value));
				inserted = true;
				break;
			}
		}

		if (!inserted) {
			node.getKVPairs().add(new KeyValueString(key, value));
		}
		LeafNodeString sibling = new LeafNodeString();
		int mid = (node.size() + 1) / 2 - 1;
		// move keys and values from range mid-node size out of node into
		// sibling
		size = node.size();
		for (int i = mid; i < size; i++) {
			sibling.getKVPairs().add(node.getKVPairs().get(mid));
			node.getKVPairs().remove(mid);
		}
		sibling.setNext(node.getNext());
		node.setNext(sibling);
		return new KeyNodeStringPair(sibling.getKVPairs().get(0).getKey(),
				sibling);
	}

	public boolean delete(String key) {
		if (root == null) {
			return false;
		} else {
			KeyNodeStringPair keyNode = delete(key, root);

			if (root instanceof LeafNodeString) {
				LeafNodeString lNode = (LeafNodeString) root;
				if (lNode.size() == 0) {
					root = null;
				}
			} else if (root instanceof InternalNodeString) {
				InternalNodeString iNode = (InternalNodeString) root;
				if (iNode.size() == 0) {
					root = iNode.getChild(0);
				}
			}
		}

		return true;
	}

	private KeyNodeStringPair delete(String key, Node node) {
		if (node instanceof LeafNodeString) {
			LeafNodeString lNode = (LeafNodeString) node;
			for (int i = 0; i < lNode.size(); i++) {
				if (key.equals(lNode.getKey(i))) {
					lNode.remove(i);

					// not half-full tree
					if (lNode.size() < MAX_LEAF_KEYS / 2) {
						if (lNode.size() > 0) {
							return new KeyNodeStringPair(lNode.getKey(0), node);
						}
						return new KeyNodeStringPair(key, node);
					}
					return new KeyNodeStringPair(lNode.getKey(0), null);
				}
			}
		} else if (node instanceof InternalNodeString) {
			InternalNodeString iNode = (InternalNodeString) node;

			int indexKey = 0;
			int indexChild = 0;
			KeyNodeStringPair keyNode = null;

			for (int i = 0; i < iNode.size(); i++) {
				if (key.equals(iNode.getKey(i))) {
					keyNode = delete(key, iNode.getChild(i + 1));
					if (keyNode != null) {
						iNode.setKey(i, keyNode.getKey());
					}
					indexKey = i;
					indexChild = i + 1;
					break;
				} else if (key.compareTo(iNode.getKey(i)) < 0) {
					keyNode = delete(key, iNode.getChild(i));
					indexKey = i;
					indexChild = i + 1;
					break;
				}
			}

			keyNode = delete(key, iNode.getChild(iNode.size()));

			if (keyNode != null && keyNode.getNode() != null) {
				if (keyNode.getNode() instanceof LeafNodeString) {
					// find sibling
					LeafNodeString iChild = (LeafNodeString) keyNode.getNode();
					LeafNodeString sibling = null;
					int siblingType = 0;

					if (indexChild == 0) {
						sibling = (LeafNodeString) iNode
								.getChild(indexChild + 1);
						siblingType = 0;
					} else if (indexChild == iNode.childrenSize() - 1) {
						sibling = (LeafNodeString) iNode
								.getChild(indexChild - 1);
						siblingType = 1;
					} else {
						sibling = (LeafNodeString) iNode
								.getChild(indexChild - 1);
						siblingType = 1;

						LeafNodeString sibling2 = (LeafNodeString) iNode
								.getChild(indexChild + 1);

						// if the left sibling cannot give more node
						// ask to the right sibling
						if (sibling.size() < MAX_LEAF_KEYS / 2 + 1
								&& sibling2.size() > MAX_LEAF_KEYS / 2) {
							sibling = sibling2;
							siblingType = 0;
						}
					}

					boolean stolen = stealFromSibling(iChild, sibling, iNode,
							indexKey, siblingType);

					if (!stolen) {
						LeafNodeString mergedNode = merge(iChild, sibling,
								iNode, indexKey, siblingType);
					}

					if (iNode.size() < MAX_LEAF_KEYS / 2) {
						return new KeyNodeStringPair(keyNode.getKey(), node);
					} else {
						return new KeyNodeStringPair(keyNode.getKey(), null);
					}
				} else if (keyNode.getNode() instanceof InternalNodeString) {
					// find sibling
					InternalNodeString iChild = (InternalNodeString) keyNode
							.getNode();
					InternalNodeString sibling = null;
					int siblingType = 0;

					if (indexChild == 0) {
						sibling = (InternalNodeString) iNode
								.getChild(indexChild + 1);
						siblingType = 0;
					} else if (indexChild == iNode.childrenSize() - 1) {
						sibling = (InternalNodeString) iNode
								.getChild(indexChild - 1);
						siblingType = 1;
					} else {
						sibling = (InternalNodeString) iNode
								.getChild(indexChild - 1);
						siblingType = 1;

						InternalNodeString sibling2 = (InternalNodeString) iNode
								.getChild(indexChild + 1);

						// if the left sibling cannot give more node
						// ask to the right sibling
						if (sibling.childrenSize() < (MAX_NODE_KEYS + 1) / 2 + 1
								&& sibling2.childrenSize() > (MAX_NODE_KEYS + 1) / 2) {
							sibling = sibling2;
							siblingType = 0;
						}
					}

					boolean stolen = stealFromSibling(iChild, sibling, iNode,
							indexKey, siblingType);

					if (!stolen) {
						// merge children
						InternalNodeString mergedNode = merge(iChild, sibling,
								iNode, indexKey, siblingType);
					}

					if (iNode.size() < MAX_NODE_KEYS / 2) {
						return new KeyNodeStringPair(keyNode.getKey(), node);
					}
					return new KeyNodeStringPair(keyNode.getKey(), null);
				}
			}
		}
		return null;
	}

	private boolean stealFromSibling(LeafNodeString node,
			LeafNodeString sibling, InternalNodeString parent, int index,
			int type) {
		// if siblingType is 0 then sibling is younger (right sibling)
		// if siblingType is 1 then sibling is older (left sibling)

		if (sibling.size() < MAX_LEAF_KEYS / 2 + 1) {
			return false;
		}

		// borrow 1 keyValue from sibling and put it into node
		// if it is borrowing from older sibling, take the keyValue[sibling.size
		// - 1] and insert it into node.[0]
		// if it is borrowing from younger sibling, take the keyValue[0] and
		// insert it into node[node.size-1]
		// replace the index-th key of the parent with the key from keyValue.key
		// borrowed from older sibling
		// replace the index-th of the parent with the key from keyValue.key
		// borrowed from younger sibling

		if (type == 0) {
			KeyValueString keyValue = sibling.getKeyValue(0);
			sibling.remove(0);

			node.add(node.size(), keyValue);

			if (parent.size() > 1) {
				parent.setKey(index, keyValue.getKey());
				parent.setKey(index + 1, sibling.getKey(0));
			} else {
				parent.setKey(index, sibling.getKey(0));
			}

			return true;
		} else if (type == 1) {
			KeyValueString keyValue = sibling.getKeyValue(sibling.size() - 1);
			sibling.remove(sibling.size() - 1);

			node.add(0, keyValue);
			parent.setKey(index, keyValue.getKey());

			return true;
		}

		return true;
	}

	public boolean stealFromSibling(InternalNodeString node,
			InternalNodeString sibling, InternalNodeString parent, int index,
			int type) {
		// if siblingType is 0 then sibling is younger (right sibling)
		// if siblingType is 1 then sibling is older (left sibling)

		if (sibling.size() < MAX_NODE_KEYS / 2 + 1) {
			return false;
		}

		if (type == 0) {
			// take out key and child of index 0 from sibling
			// replace key of parent to key of sibling
			// insert key of parent to the last element of node
			// insert child of sibling to the last element of node

			String key = sibling.getKey(0);
			sibling.removeKey(0);

			Node child = sibling.getChild(0);
			sibling.removeChild(0);

			node.addKey(node.size(), parent.getKey(index));
			parent.setKey(index, key);
			node.addChild(node.size(), child);

			return true;
		} else if (type == 1) {
			// take out key and child of index node.size-1 from sibling
			// replace key of parent to key of sibling
			// insert key of parent to the 1st element of node
			// insert child of sibling to the 1st element of node

			String key = sibling.getKey(node.size() - 1);
			sibling.removeKey(node.size() - 1);

			Node child = sibling.getChild(node.size());
			sibling.removeChild(node.size());

			node.addKey(0, parent.getKey(index));
			parent.setKey(index, key);
			node.addChild(0, child);

			return true;
		}

		return false;
	}

	private LeafNodeString merge(LeafNodeString node, LeafNodeString sibling,
			InternalNodeString parent, int index, int type) {
		int size = node.size() - 1;
		if (type == 0) { // younger sibling
			for (int i = node.size() - 1; i >= 0; i--) {
				sibling.add(0, node.getKeyValue(i));
				node.remove(i);
			}

			parent.removeKey(index);
			parent.removeChild(index);
			return sibling;
		} else if (type == 1) {
			for (int i = 0; i <= size; i++) {
				sibling.add(sibling.size(), node.getKeyValue(0));
				node.remove(0);
			}

			parent.removeKey(index);
			parent.removeChild(index + 1);
			return sibling;
		}
		return null;
	}

	private InternalNodeString merge(InternalNodeString node,
			InternalNodeString sibling, InternalNodeString parent, int index,
			int type) {
		if (type == 0) { // younger sibling
			int size = node.size() - 1;
			// move key
			for (int i = size; i >= 0; i--) {
				sibling.addKey(i, node.getKey(i));
				node.removeKey(i);
			}
			// move child
			for (int i = size + 1; i >= 0; i--) {
				sibling.addChild(i, node.getChild(i));
				node.removeChild(i);
			}

			String key = parent.getKey(index);

			parent.removeKey(index);
			sibling.addKey(0, key);
			parent.removeChild(index);
		} else if (type == 1) {
			int size = node.size() - 1;
			// move key
			for (int i = 0; i <= size; i++) {
				sibling.addKey(sibling.size(), node.getKey(0));
				node.removeChild(0);
			}
			// move child
			for (int i = 0; i <= size + 1; i++) {
				sibling.addChild(sibling.size() + 1, node.getChild(0));
				node.removeChild(0);
			}

			String key = parent.getKey(index);

			parent.removeKey(index);
			sibling.addKey(parent.size(), key);
			parent.removeChild(index + 1);
		}
		return null;
	}

	public IteratorString iterator() {
		return new IteratorString(root);
	}

}
