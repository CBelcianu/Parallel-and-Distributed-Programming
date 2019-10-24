public class MulByLinesThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private int i;

    public MulByLinesThread(int[][] A, int[][] B, int[][] C, int i){
        this.A = A;
        this.B = B;
        this.C = C;
        this.i = i;
    }

    @Override
    public void run() {
        for (int k = 0; k < A[0].length; k++) {
            C[i][k] = 0;
            for (int j = 0; j < A[0].length; j++)
                C[i][k] += A[i][j] * B[j][k];
        }
    }
}
