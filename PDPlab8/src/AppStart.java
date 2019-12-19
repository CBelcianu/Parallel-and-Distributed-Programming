import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppStart {
    static boolean cv = false;

    public static void main(final String[] args) {
        int[][] graph = new int[][]{
                {0, 1, 0, 1, 0},
                {1, 0, 0, 0, 1},
                {0, 0, 0, 1, 1},
                {1, 0, 1, 0, 0},
                {0, 1, 1, 0, 0}
        };

        Permutation obj = new Permutation(graph);
        int[] a = {0, 1, 2, 3, 4};
        obj.heapPermutation(a, a.length, a.length);
        List<Runnable> threads = obj.getThreads();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        threads.forEach(executorService::execute);
        try {
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
