package src;

public class NodeSub extends Node {

	public final void unlinkSub() {
		if (previous == null) {
		} else {
			previous.next = next;
			next.previous = previous;
			next = null;
			previous = null;
		}
	}

	public NodeSub() {
	}

	public NodeSub next;
	NodeSub previous;
	public static int anInt1305;
}
