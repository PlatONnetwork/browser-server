package com.platon.browser.queue;

import com.platon.browser.util.LimitQueue;

public class QueueTest {
    public static void main(String[] args) {
        LimitQueue<Integer> queue = new LimitQueue<>(3);
        queue.offer(1);
        queue.offer(2);
        queue.offer(4);
        queue.offer(5);
        System.out.println(queue.elements());
    }
}
