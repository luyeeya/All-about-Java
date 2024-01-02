package dev.luyee;

import java.util.concurrent.*;

/**
 * Java 中使用线程的 4 种方式
 */
public class UseThread {
    public static void main(String[] args) {
        // 1.继承 Thread 类
        Thread1 thread1 = new Thread1();
        thread1.start();

        // 2.实现 Runnable 接口
        Thread thread2 = new Thread(new Thread2());
        thread2.start();

        // 3.实现 Callable 接口
        FutureTask<String> futureTask = new FutureTask<>(new Thread3());
        Thread thread3 = new Thread(futureTask);
        thread3.start();
        try {
            String result = futureTask.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 4.使用线程池
        /*
        四种阻塞队列：
            ArrayBlockingQueue：基于数组、有界，按 FIFO（先进先出）原则对元素进行排序；
            LinkedBlockingQueue：基于链表，按FIFO （先进先出） 排序元素，吞吐量通常要高于 ArrayBlockingQueue；
            SynchronousQueue：每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于 LinkedBlockingQueue；
            PriorityBlockingQueue：具有优先级的、无限阻塞队列。
        线程拒绝策略：
            CallerRunsPolicy：如果发现线程池还在运行，就直接运行这个线程;
            DiscardOldestPolicy：在线程池的等待队列中，将头取出一个抛弃，然后将当前线程放进去;
            DiscardPolicy：默默丢弃,不抛出异常;
            AbortPolicy：java默认，抛出一个异常（RejectedExecutionException）。
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2, // int corePoolSize: 核心线程数，默认线程个数为 0，只有接到任务才新建线程
                4, // int maximumPoolSize: 最大线程数
                10, // long keepAliveTime: 线程池空闲时，线程存活的时间，当线程池中的线程数大于 corePoolSize 时才会起作用
                TimeUnit.SECONDS, // TimeUnit unit: 时间单位
                new LinkedBlockingQueue<>(10), // BlockingQueue<Runnable> workQueue: 阻塞队列，当达到线程数达到 corePoolSize 时，将任务放入队列等待线程处理
                Executors.defaultThreadFactory(), // ThreadFactory threadFactory: 线程创建工厂
                new ThreadPoolExecutor.AbortPolicy() // RejectedExecutionHandler handler: 线程拒绝策略，当队列满了并且线程个数达到 maximumPoolSize 后采取的策略
        );
        threadPoolExecutor.submit(new Thread1());
        threadPoolExecutor.submit(new Thread2());
        Future<String> future = threadPoolExecutor.submit(new Thread3());
        try {
            String result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    static class Thread1 extends Thread {
        @Override
        public void run() {
            System.out.println("继承 Thread 类");
        }
    }

    static class Thread2 implements Runnable {
        @Override
        public void run() {
            System.out.println("实现 Runnable 接口");
        }
    }

    static class Thread3 implements Callable<String> {
        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(1);
            return "实现 Callable 接口";
        }
    }
}
