package org.edgesim.tool.platform.redundancy.test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: Solution
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2020-01-17 09:15
 * @Version: 1.0
 */
public class Solution {

    public AtomicInteger atomicInteger = new AtomicInteger(0);

    private Integer integer = 0;

    public static void main(String[] args) {

        Solution solution = new Solution();

        ArrayList<Thread> threads = new ArrayList<>(1000);

        for (int i = 0; i < 800; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        solution.count();
                        solution.safeCount();
                    }
                }
            });
            threads.add(t);
        }
        for (Thread t1:
             threads) {
            t1.start();
        }
        for (Thread t1:
             threads) {
            try{
                t1.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println(solution.integer);
        System.out.println(solution.atomicInteger.get());
    }

    public void count(){
        integer++;
    }

    /**
     * 使用CAS实现安全计数器
     */

    public void safeCount(){
        int i = atomicInteger.get();
        while(true){
            boolean flag = atomicInteger.compareAndSet(i, ++i);
            if (flag){
                break;
            }
        }
    }
}
