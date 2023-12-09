package chapter_17.networking_and_threads.wait_with_count_down_latch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

public class WaitWithCountDownLatch {
    public void start() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        executorService.execute(new TestTask(countDownLatch));

        System.out.println("WaitWithCountDownLatch.start()");
        countDownLatch.countDown();

        executorService.shutdown();
    }
}
