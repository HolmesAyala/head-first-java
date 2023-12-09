package chapter_17.networking_and_threads.wait_with_count_down_latch;

import java.util.concurrent.CountDownLatch;

public class TestTask implements Runnable {
    private final CountDownLatch countDownLatch;
    public TestTask(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        System.out.println("TestTask.run()");
    }
}
