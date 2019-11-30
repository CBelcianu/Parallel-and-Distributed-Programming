import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.log;
import static java.lang.Math.pow;

class Adder {
    private int last;
    private int[] b;
    private int length;

    Adder(int last, int[] b){
        this.last=last;
        this.b=b;
        this.length=b.length-1;
    }

    void Algorithm(){
        long startTime;
        long endTime;
        long elapsedTime;

        startTime = System.currentTimeMillis();
        for (int d = 0; d < log(this.length)/log(2); d++)
        {
            int i = 0;
            List<Runnable> threads = new ArrayList<>();
            while (i < this.length)
            {
                int power = (int)pow(2, d + 1);
                Reduce thr = new Reduce(b, i, power);
                threads.add(thr);
                i += power;
            }
            ExecutorService executorService = Executors.newFixedThreadPool(threads.size());
            threads.forEach(executorService::execute);
            try {
                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
        }
        endTime = System.currentTimeMillis();
        elapsedTime = endTime-startTime;
        System.out.println("BENCHMARK: Reduce took " + elapsedTime + " us.");

        b[this.length-1] = 0; //set-up down-sweep phase

        startTime = System.currentTimeMillis();
        for (int d = (int)(log(this.length)/log(2)) - 1; d >= 0; d--)
        {
            int i = 0;
            List<Runnable> threads = new ArrayList<>();
            while (i < this.length)
            {
                int power = (int)(pow(2, d + 1));
                DownSweep thr = new DownSweep(b, i, power);
                threads.add(thr);
                i += power;
            }
            ExecutorService executorService = Executors.newFixedThreadPool(threads.size());
            threads.forEach(executorService::execute);
            try {
                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
        }
        endTime = System.currentTimeMillis();
        elapsedTime = endTime-startTime;
        System.out.println("BENCHMARK: DownSweep took " + elapsedTime + " us.");

        b[this.length] = last + b[this.length-1]; //compute last element
    }
}
