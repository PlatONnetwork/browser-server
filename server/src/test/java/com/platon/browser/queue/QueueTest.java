package com.platon.browser.queue;


import com.platon.browser.dto.cache.LimitQueue;

import java.util.Collections;
import java.util.List;

public class QueueTest {
    public static void main(String[] args) {
        LimitQueue<Integer> queue = new LimitQueue<>(3);
        queue.offer(1);
        queue.offer(2);
        queue.offer(8);
        queue.offer(5);

        List<Integer> list = queue.list();
        Collections.sort(list,(c1,c2)->{
            if(c1>c2) return -1;
            if(c1<c2) return 1;
            return 0;
        });
        System.out.println(list);
    }
}
