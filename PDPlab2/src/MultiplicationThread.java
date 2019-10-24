public class MultiplicationThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;

    public MultiplicationThread(int[][] A, int[][] B, int[][] C){
        this.A = A;
        this.B = B;
        this.C = C;
    }

    @Override
    public void run() {
        for (int i = 0; i < A[0].length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                C[i][j] = 0;
                for (int k = 0; k < A[0].length; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
    }
}
