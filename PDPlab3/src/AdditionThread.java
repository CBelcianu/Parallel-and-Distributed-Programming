import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AdditionThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private boolean both;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AdditionThread(int[][] A, int[][] B, int[][] C, boolean both){
        this.A = A;
        this.B = B;
        this.C = C;
        this.both = both;
    }

    public Future<int[][]> doYourJob() {
        int[][] RES = C;
        return executorService.submit(() -> {
            for (int i = 0; i < this.A[0].length; i++) {
                for (int j = 0; j < this.A[0].length; j++) {
                    RES[i][j] = RES[i][j] + RES[i][j];
                }
            }
            return RES;
        });
    }

    public void shutdown(){
        this.executorService.shutdown();
    }

    @Override
    public void run() {
        if (!both) {
            for (int i = 0; i < this.A[0].length; i++) {
                for (int j = 0; j < this.A[0].length; j++) {
                    C[i][j] = A[i][j] + B[i][j];
                }
            }
        }
        else {
            Future<int[][]> future = this.doYourJob();
            try {
                C = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            this.shutdown();
        }
    }

}
