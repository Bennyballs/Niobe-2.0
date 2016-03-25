package src;


final class Queue {

	public Queue() {
		head = new NodeSub();
		head.next = head;
		head.previous = head;
	}

	public void insertHead(NodeSub nodeSub) {
		if (nodeSub.previous != null)
			nodeSub.unlinkSub();
		nodeSub.previous = head.previous;
		nodeSub.next = head;
		nodeSub.previous.next = nodeSub;
		nodeSub.next.previous = nodeSub;
	}

	public NodeSub popTail() {
		NodeSub nodeSub = head.next;
		if (nodeSub == head) {
			return null;
		} else {
			nodeSub.unlinkSub();
			return nodeSub;
		}
	}

	public NodeSub reverseGetFirst() {
		NodeSub nodeSub = head.next;
		if (nodeSub == head) {
			current = null;
			return null;
		} else {
			current = nodeSub.next;
			return nodeSub;
		}
	}

	public NodeSub reverseGetNext() {
		NodeSub nodeSub = current;
		if (nodeSub == head) {
			current = null;
			return null;
		} else {
			current = nodeSub.next;
			return nodeSub;
		}
	}

	public int getNodeCount() {
		int i = 0;
		for (NodeSub nodeSub = head.next; nodeSub != head; nodeSub = nodeSub.next)
			i++;

		return i;
	}

	private final NodeSub head;
	private NodeSub current;
}
