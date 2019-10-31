import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MulByElementsThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private int i, j;
    private boolean both;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MulByElementsThread(int[][] A, int[][] B, int[][] C, int i, int j, boolean both){
        this.A = A;
        this.B = B;
        this.C = C;
        this.i = i;
        this.j = j;
        this.both = both;
    }

    public Future<Integer> doYourJob(){
        int[][] RES = C;
        return executorService.submit(() -> {
            RES[i][j] = 0;

            for (int counter = 0; counter < A[0].length; counter++){
                RES[i][j] += A[i][counter] * B[counter][j];
            }

            return RES[i][j];
        });
    }

    public void shutdown(){
        this.executorService.shutdown();
    }

    @Override
    public void run() {
        if (!both) {
            C[i][j] = 0;

            for (int counter = 0; counter < A[0].length; counter++) {
                C[i][j] += A[i][counter] * B[counter][j];
            }
        }
        else{
            Future<Integer> future = this.doYourJob();
            try {
                C[i][j] = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            this.shutdown();
        }
    }
}
