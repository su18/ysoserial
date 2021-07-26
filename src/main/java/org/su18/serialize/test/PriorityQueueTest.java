package org.su18.serialize.test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * 优先级队列测试
 *
 * @author su18
 */
public class PriorityQueueTest {


	public static void main(String[] args) {

//		PriorityQueue<String> queue = new PriorityQueue<>();
		PriorityQueue<String> queue = new PriorityQueue<>(5, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				Integer i1 = Integer.parseInt(o1.substring(1));
				Integer i2 = Integer.parseInt(o2.substring(1));
				if (i1 > i2) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		queue.add("e198");
		queue.add("d099");
		queue.add("c023");
		queue.add("a123");
		queue.add("b223");


		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());

	}

}
