package com.langlang.test;

public class ThreadInterruptExample {


    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("线程开始运行");
            try {
                Thread.sleep(1000);
                System.out.println("子线程状态sleep: " + Thread.currentThread().isInterrupted());
            } catch (InterruptedException e) {
                System.out.println("子线程状态: " + Thread.currentThread().isInterrupted());
                System.out.println("线程被中断");
                return;
            }
            System.out.println("线程运行结束");
        });

        thread.start();

        Thread.sleep(500);
        thread.interrupt();

        System.out.println("主线程判断线程是否中断：" + thread.isInterrupted());

        Thread.sleep(1000);

        System.out.println("主线程再次判断线程是否中断：" + thread.isInterrupted());
    }
}
