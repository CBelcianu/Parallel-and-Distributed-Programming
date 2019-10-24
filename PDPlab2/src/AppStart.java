import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppStart {
    public static void main(String[] args){
        int[][] A = new int[3][3];
        int[][] B = new int[3][3];
        int[][] C = new int[3][3];

        for(int i=0; i<A[0].length; i++){
            for(int j=0; j<A[0].length; j++){
                A[i][j] = i + j;
                B[i][j] = i + j;
                C[i][j] = i + j;
            }
        }
        /*
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        AdditionThread additionThread = new AdditionThread(A, B, C);
        executorService.execute(additionThread);
        try {
            executorService.awaitTermination(100, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        */

        /*
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        List<AddByElementsThread> threads = new ArrayList<>();
        for(int i=0; i<A[0].length; i++){
            for(int j=0; j<A[0].length; j++){
                threads.add(new AddByElementsThread(A, B, C, i, j));
            }
        }
        threads.forEach(executorService::execute);
        try {
            executorService.awaitTermination(100, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        */

        /*
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<AddByLinesThread> threads = new ArrayList<>();
        for(int i=0; i<A[0].length; i++) {
            threads.add(new AddByLinesThread(A, B, C, i));
        }
        threads.forEach(executorService::execute);
        try {
            executorService.awaitTermination(100, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        */

        /*
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        MultiplicationThread multiplicationThread = new MultiplicationThread(A, B, C);
        executorService.execute(multiplicationThread);
        try {
            executorService.awaitTermination(100, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        */

        /*
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        List<MulByElementsThread> threads = new ArrayList<>();
        for(int i=0; i<A[0].length; i++){
            for(int j=0; j<A[0].length; j++){
                threads.add(new MulByElementsThread(A, B, C, i, j));
            }
        }
        threads.forEach(executorService::execute);
        try {
            executorService.awaitTermination(100, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        */

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<MulByLinesThread> threads = new ArrayList<>();
        for(int i=0; i<A[0].length; i++) {
            threads.add(new MulByLinesThread(A, B, C, i));
        }
        threads.forEach(executorService::execute);
        try {
            executorService.awaitTermination(100, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        printMatrix(C);
    }

    public static void printMatrix(int[][] matrix){
        for(int i=0; i<matrix[0].length; i++){
            System.out.println(Arrays.toString(matrix[i]));
        }
    }
}
