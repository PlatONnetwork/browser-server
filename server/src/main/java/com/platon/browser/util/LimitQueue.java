package com.platon.browser.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LimitQueue<E>{

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private int limit; // 队列长度

    private LinkedList<E> queue = new LinkedList<>();

    public LimitQueue(int limit){
        this.limit = limit;
    }

    /**
     * 入列：当队列大小已满时，把队头的元素poll掉
     */
    public void offer(E e){
        lock.writeLock().lock();
        try{
            if(queue.size() >= limit){
                queue.poll();
            }
            queue.offer(e);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public E get(int position) {
        lock.readLock().lock();
        try {
            return queue.get(position);
        }finally {
            lock.readLock().unlock();
        }
    }

    public E getLast() {
        lock.readLock().lock();
        try {
            return queue.getLast();
        }finally {
            lock.readLock().unlock();
        }
    }

    public E getFirst() {
        lock.readLock().lock();
        try {
            return queue.getFirst();
        }finally {
            lock.readLock().unlock();
        }
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        lock.readLock().lock();
        try {
            return queue.size();
        }finally {
            lock.readLock().unlock();
        }
    }

    public List<E> elements(){
        lock.readLock().lock();
        try {
            List<E> list = new ArrayList<>();
            for (int i=queue.size()-1;i>=0;i--){
                list.add(queue.get(i));
            }
            return list;
        }finally {
            lock.readLock().unlock();
        }
    }
}
