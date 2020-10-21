package com.mytekskills.demo.datastructures;

/**
 * @author ChandanDigumarthy
 * CustomLimitedSize_LinkedList limits the size of linked list to the initialized size
 * Constructor parameter should be specified to mention the max size of linked list
 */
public class CustomLimitedSize_LinkedList {

	/**
	 * Initialize using max size of the linked list
	 */
	public CustomLimitedSize_LinkedList(int maxSize){
		this.maxSize = maxSize;
	}
	
	private Node head;
	private int size;
	private int maxSize;
	
	public void push(long value) {
		
		size ++;
		
		if(head == null) {
			head = new Node(value);
			head.setNext(null);
		} else {
			
			//
			Node nextNodes = head;
			head = new Node(value);
			head.setNext(nextNodes);
			
			//limiting linked list size to 100 by unlinking the nodes which gone beyond the size
			if(size > maxSize) {

				nextNodes = head;
				
				int nodeNumToRemove = 0;
				while(nodeNumToRemove < maxSize-1) {
					nodeNumToRemove++;
					nextNodes = nextNodes.getNext();
				}

				nextNodes.setNext(null);
				size--;

			}
			
		}
	}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Returns a proper JSON array of this class
	 * Reduces efforts to parse data or form JSON object separately
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node node = head;
		int i = 0;
		while(node.getNext()!=null){
			sb.append("{\"time\": "+ 10*i++ +", \"value\":"+node.getValue()+"},");
			node = node.getNext();
		}
		sb.append("{\"time\": "+ 10*i++ +", \"value\":"+node.getValue()+"}");
		sb.append("]");
		
		return sb.toString();
	}
		
}

class Node {
	
	private Node next;

	private long value;
	
	Node(long value){
		this.value = value;
	}
	
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public Node getNext() {
		return next;
	}
	public void setNext(Node next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "Node [next=" + next + ", value=" + value + "]";
	}
	
}