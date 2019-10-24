public class MulByElementsThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private int i, j;

    public MulByElementsThread(int[][] A, int[][] B, int[][] C, int i, int j){
        this.A = A;
        this.B = B;
        this.C = C;
        this.i = i;
        this.j = j;
    }

    @Override
    public void run() {
        C[i][j] = 0;

        for (int counter = 0; counter < A[0].length; counter++){
            C[i][j] += A[i][counter] * B[counter][j];
        }
    }
}
