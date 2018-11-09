package com.platon.browser.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LimitQueue<E>{

    private int limit; // 队列长度

    private LinkedList<E> queue = new LinkedList<>();

    public LimitQueue(int limit){
        this.limit = limit;
    }

    /**
     * 入列：当队列大小已满时，把队头的元素poll掉
     */
    public void offer(E e){
        if(queue.size() >= limit){
            queue.poll();
        }
        queue.offer(e);
    }

    public E get(int position) {
        return queue.get(position);
    }

    public E getLast() {
        return queue.getLast();
    }

    public E getFirst() {
        return queue.getFirst();
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        return queue.size();
    }

    public List<E> elements(){
        List<E> list = new ArrayList<>();
        for (int i=queue.size()-1;i>=0;i--){
            list.add(queue.get(i));
        }
        return list;
    }

    public List<E> elementsAsc(){
        List<E> list = new ArrayList<>();
        for (int i=0;i<queue.size();i++){
            list.add(queue.get(i));
        }
        return list;
    }
}
