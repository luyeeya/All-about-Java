package dev.luyee;

import java.util.concurrent.TimeUnit;

/**
 * 展示 Java 线程的 6 种状态
 */
public class ThreadStatus {
    public static void main(String[] args) throws InterruptedException {
        // 1.NEW
        NewThread newThread = new NewThread();
        System.out.println(newThread.getState());

        // 2.RUNNABLE
        RunnableThread runnableThread = new RunnableThread();
        runnableThread.start();

        // 3.WAITING
        WaitingThread waitingThread = new WaitingThread();
        waitingThread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(waitingThread.getState());

        // 4.TIMED_WAITING
        TimedWaitingThread timedWaitingThread = new TimedWaitingThread();
        timedWaitingThread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(timedWaitingThread.getState());

        // 5.BLOCKED
        BlockedThread blockedThread1 = new BlockedThread();
        BlockedThread blockedThread2 = new BlockedThread();
        blockedThread1.start();
        TimeUnit.SECONDS.sleep(1);
        blockedThread2.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(blockedThread2.getState());

        // 6.TERMINATED
        TerminatedThread terminatedThread = new TerminatedThread();
        terminatedThread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(terminatedThread.getState());
    }

    static class NewThread extends Thread {
    }

    static class RunnableThread extends Thread {
        @Override
        public void run() {
            System.out.println(this.getState());
        }
    }

    static class WaitingThread extends Thread {
        @Override
        public void run() {
            synchronized (WaitingThread.class) {
                try {
                    WaitingThread.class.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class TimedWaitingThread extends Thread {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class BlockedThread extends Thread {
        @Override
        public void run() {
            synchronized (BlockedThread.class) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class TerminatedThread extends Thread {
    }
}
