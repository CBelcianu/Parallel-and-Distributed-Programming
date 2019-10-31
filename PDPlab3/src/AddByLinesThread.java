import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddByLinesThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private int i;
    private boolean both;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    public AddByLinesThread(int[][] A, int[][] B, int[][] C, int i, boolean both){
        this.A = A;
        this.B = B;
        this.C = C;
        this.i = i;
        this.both = both;
    }

    public Future<int[]> doYourJob(){
        int[][] RES = C;
        return executorService.submit(() -> {
            for(int j=0; j<this.A[0].length; j++){
                RES[i][j] = RES[i][j] + RES[i][j];
            }
            return RES[i];
        });
    }

    public void shutdown(){
        this.executorService.shutdown();
    }

    @Override
    public void run() {
        if (!both) {
            for (int j = 0; j < this.A[0].length; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        else {
            Future<int[]> future = this.doYourJob();
            try {
                C[i] = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            this.shutdown();
        }
    }
}
