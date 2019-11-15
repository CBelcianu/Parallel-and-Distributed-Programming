import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class AppStart {
    public static void main(String[] args){
        int[][] A = new int[100][100];
        int[][] B = new int[100][100];
        int[][] C = new int[100][100];
        HashMap<String, HashMap<String, Long>> hm = new HashMap();

        for(int i=0; i<A[0].length; i++){
            for(int j=0; j<A[0].length; j++){
                A[i][j] = i + j;
                B[i][j] = i + j;
                C[i][j] = i + j;
            }
        }

        Scanner keyboard = new Scanner(System.in);
        printMenu();
        int cmd = keyboard.nextInt();
        while (cmd != 0){
            if (cmd == 1) {
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                AdditionThread additionThread = new AdditionThread(A, B, C, false);
                Date start = new Date(System.currentTimeMillis());
                executorService.execute(additionThread);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();

                Date end = new Date(System.currentTimeMillis());
                long diffInNanos = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nADDITION took " + diffInNanos + "us.");

                HashMap<String, Long> aux = new HashMap<>();
                aux.put("thread pool", diffInNanos);
                hm.put("1 thread ADDITION", aux);

                executorService = Executors.newFixedThreadPool(1);
                MultiplicationThread multiplicationThread = new MultiplicationThread(A, B, C, false);
                start = new Date(System.currentTimeMillis());
                executorService.execute(multiplicationThread);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();

                end = new Date(System.currentTimeMillis());
                long diffInNanosMul = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nMULTIPLICATION took " + diffInNanosMul + "us.");

                aux.put("thread pool", diffInNanosMul);
                hm.put("1 thread MULTIPLICATION", aux);

                printMenu();
                cmd = keyboard.nextInt();
            }
            else if(cmd == 2){
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                AdditionThread additionThread = new AdditionThread(A, B, C, true);
                Date start = new Date(System.currentTimeMillis());
                executorService.execute(additionThread);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();

                Date end = new Date(System.currentTimeMillis());
                long diffInNanos = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nADDITION took " + diffInNanos + "us.");

                HashMap<String, Long> aux = new HashMap<>();
                aux.put("future & task", diffInNanos);
                hm.put("1 thread ADDITION", aux);

                executorService = Executors.newFixedThreadPool(1);
                MultiplicationThread multiplicationThread = new MultiplicationThread(A, B, C, true);
                start = new Date(System.currentTimeMillis());
                executorService.execute(multiplicationThread);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();

                end = new Date(System.currentTimeMillis());
                long diffInNanosMul = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nMULTIPLICATION took " + diffInNanosMul + "us.");

                aux.put("future & task", diffInNanosMul);
                hm.put("1 thread MULTIPLICATION", aux);

                printMenu();
                cmd = keyboard.nextInt();
            }
            else if(cmd==3){
                ExecutorService executorService = Executors.newFixedThreadPool(100);
                List<AddByLinesThread> threads = new ArrayList<>();
                Date start = new Date(System.currentTimeMillis());
                for(int i=0; i<A[0].length; i++) {
                    threads.add(new AddByLinesThread(A, B, C, i, false));
                }
                threads.forEach(executorService::execute);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();
                Date end = new Date(System.currentTimeMillis());
                long diffInNanos = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nADDITION took " + diffInNanos + "us.");

                HashMap<String, Long> aux = new HashMap<>();
                aux.put("thread pool", diffInNanos);
                hm.put("N threads ADDITION", aux);

                executorService = Executors.newFixedThreadPool(100);
                List<MulByLinesThread> threadsMul = new ArrayList<>();
                start = new Date(System.currentTimeMillis());
                for(int i=0; i<A[0].length; i++) {
                    threadsMul.add(new MulByLinesThread(A, B, C, i, false));
                }
                threadsMul.forEach(executorService::execute);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();

                end = new Date(System.currentTimeMillis());
                long diffInNanosMul = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nMULTIPLICATION took " + diffInNanosMul + "us.");

                aux.put("thread pool", diffInNanosMul);
                hm.put("N threads MULTIPLICATION", aux);

                printMenu();
                cmd = keyboard.nextInt();
            }
            else if(cmd == 4){
                ExecutorService executorService = Executors.newFixedThreadPool(100);
                List<AddByLinesThread> threads = new ArrayList<>();
                Date start = new Date(System.currentTimeMillis());
                for(int i=0; i<A[0].length; i++) {
                    threads.add(new AddByLinesThread(A, B, C, i, true));
                }
                threads.forEach(executorService::execute);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();
                Date end = new Date(System.currentTimeMillis());
                long diffInNanos = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nADDITION took " + diffInNanos + "us.");

                HashMap<String, Long> aux = new HashMap<>();
                aux.put("future & task", diffInNanos);
                hm.put("N threads ADDITION", aux);

                executorService = Executors.newFixedThreadPool(100);
                List<MulByLinesThread> threadsMul = new ArrayList<>();
                start = new Date(System.currentTimeMillis());
                for(int i=0; i<A[0].length; i++) {
                    threadsMul.add(new MulByLinesThread(A, B, C, i, true));
                }
                threadsMul.forEach(executorService::execute);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();

                end = new Date(System.currentTimeMillis());
                long diffInNanosMul = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nMULTIPLICATION took " + diffInNanosMul + "us.");

                aux.put("future & task", diffInNanosMul);
                hm.put("N threads MULTIPLICATION", aux);

                printMenu();
                cmd = keyboard.nextInt();
            }
            else if(cmd == 5){
                ExecutorService executorService = Executors.newFixedThreadPool(10000);
                List<AddByElementsThread> threads = new ArrayList<>();
                Date start = new Date(System.currentTimeMillis());
                for(int i=0; i<A[0].length; i++){
                    for(int j=0; j<A[0].length; j++){
                        threads.add(new AddByElementsThread(A, B, C, i, j, false));
                    }
                }
                threads.forEach(executorService::execute);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();
                Date end = new Date(System.currentTimeMillis());
                long diffInNanos = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nADDITION took " + diffInNanos + "us.");

                HashMap<String, Long> aux = new HashMap<>();
                aux.put("thread pool", diffInNanos);
                hm.put("N*N threads ADDITION", aux);

                executorService = Executors.newFixedThreadPool(10000);
                List<MulByElementsThread> threadsMul = new ArrayList<>();
                start = new Date(System.currentTimeMillis());
                for(int i=0; i<A[0].length; i++){
                    for(int j=0; j<A[0].length; j++){
                        threadsMul.add(new MulByElementsThread(A, B, C, i, j, false));
                    }
                }
                threadsMul.forEach(executorService::execute);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();

                end = new Date(System.currentTimeMillis());
                long diffInNanosMul = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nMULTIPLICATION took " + diffInNanosMul + "us.");

                aux.put("thread pool", diffInNanosMul);
                hm.put("N*N threads MULTIPLICATION", aux);

                printMenu();
                cmd = keyboard.nextInt();
            }
            else if(cmd == 6){
                ExecutorService executorService = Executors.newFixedThreadPool(10000);
                List<AddByElementsThread> threads = new ArrayList<>();
                Date start = new Date(System.currentTimeMillis());
                for(int i=0; i<A[0].length; i++){
                    for(int j=0; j<A[0].length; j++){
                        threads.add(new AddByElementsThread(A, B, C, i, j, true));
                    }
                }
                threads.forEach(executorService::execute);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();
                Date end = new Date(System.currentTimeMillis());
                long diffInNanos = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nADDITION took " + diffInNanos + "us.");

                HashMap<String, Long> aux = new HashMap<>();
                aux.put("future & task", diffInNanos);
                hm.put("N*N threads ADDITION (future & task)", aux);

                executorService = Executors.newFixedThreadPool(10000);
                List<MulByElementsThread> threadsMul = new ArrayList<>();
                start = new Date(System.currentTimeMillis());
                for(int i=0; i<A[0].length; i++){
                    for(int j=0; j<A[0].length; j++){
                        threadsMul.add(new MulByElementsThread(A, B, C, i, j, true));
                    }
                }
                threadsMul.forEach(executorService::execute);
                try {
                    executorService.awaitTermination(40, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();

                end = new Date(System.currentTimeMillis());
                long diffInNanosMul = Math.abs(start.getTime() - end.getTime());
                System.out.println("\nMULTIPLICATION took " + diffInNanosMul + "us.");

                aux.put("future & task", diffInNanosMul);
                hm.put("N*N threads MULTIPLICATION (future & task)", aux);

                printMenu();
                cmd = keyboard.nextInt();
            }
            else{
                System.out.println("invalid command");
                printMenu();
                cmd = keyboard.nextInt();
            }
        }
        System.out.println("bye");
        System.out.println(hm);
    }

    public static void printMatrix(int[][] matrix){
        for(int i=0; i<matrix[0].length; i++){
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    public static void printMenu(){
        System.out.println("\n====================MENU====================");
        System.out.println("\t1. 1 thread with threadPool");
        System.out.println("\t2. 1 thread with Future and Task");
        System.out.println("\t3. N threads with threadPool");
        System.out.println("\t4. N threads with Futures and Tasks");
        System.out.println("\t5. N*N threads with threadPool");
        System.out.println("\t6. N*N threads with Futures and Tasks");
        System.out.println("\t0. exit");
        System.out.println("============================================");
    }
}
