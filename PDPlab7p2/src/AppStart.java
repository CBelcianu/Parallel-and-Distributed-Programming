import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppStart {
    private static Node[] nodes;

    public static void main(final String[] args) {
        int noThreads;
        int maxNumLen;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input number of threads:");
        noThreads = scanner.nextInt();
        System.out.println("Input the max number of digits a number can have:");
        maxNumLen = scanner.nextInt();

        final int n = noThreads;
        final Random r = new Random();
        nodes = new Node[n];
        nodes[n - 1] = new Node(r.nextInt(maxNumLen) + 1, null, false, true);
        for (int i = n - 2; i >= 0; --i)
            if (i == 0)
                nodes[i] = new Node(r.nextInt(maxNumLen) + 1, nodes[i + 1], true, false);
            else
                nodes[i] = new Node(r.nextInt(maxNumLen) + 1, nodes[i + 1]);


        for (final Node nd : nodes) {
            nd.print();
        }

        final long startTime = System.currentTimeMillis();

        Thread[] threads = new Thread[noThreads];
        for (int i = 0; i < noThreads; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    Adder.add(nodes[index]);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        ExecutorService executorService = Executors.newFixedThreadPool(noThreads);
        for (Thread thr: threads){
            executorService.execute(thr);
        }
        try {
            executorService.awaitTermination(20, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        final long stopTime = System.currentTimeMillis();
        final long elapsedTime = stopTime-startTime;
        System.out.println();
        System.out.println("\nBENCHMARK: addition took " + elapsedTime + " us.");
    }
}
