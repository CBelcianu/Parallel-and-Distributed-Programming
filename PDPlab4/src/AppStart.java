import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AppStart {
    static final Lock lock = new ReentrantLock();

    static final Condition rowDone = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        int[][] A = new int[50][50];
        int[][] B = new int[50][50];
        int[][] C = new int[50][50];
        Random rand = new Random();

        for(int i=0; i<A[0].length; i++){
            for(int j=0; j<A[0].length; j++){
                A[i][j] = rand.nextInt(19) + 1;
                B[i][j] = rand.nextInt(19) + 1;
                C[i][j] = rand.nextInt(19) + 1;
            }
        }

        int[][] D = new int[50][50];
        int[][] E = new int[50][50];

        long startTime;
        long stopTime;
        long elapsedTime;


        List<Thread> threads = new ArrayList<>();
        startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(9);
        for (int rows = 0; rows < A.length; rows++) {
            MultiplicationAB thr = new MultiplicationAB(A, B, D, rows);
            threads.add(thr);
        }

        for (int rows = 0; rows < A.length; rows++) {
            MultiplicationResultC thr = new MultiplicationResultC(D, C, E, rows);
            threads.add(thr);
        }

        threads.forEach(executorService::execute);

        if (!executorService.awaitTermination(60, TimeUnit.MILLISECONDS)) {
            executorService.shutdown();
        }

        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;

        System.out.println("BENCHMARK TOOK: " + elapsedTime + "us");
    }


    static boolean isFilledRow(int[][] mat, int row) {
        for (int i = 0; i < mat.length; i++) {
            if (mat[row][i] == 0) return false;
        }
        return true;
    }


}