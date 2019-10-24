public class AddByElementsThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private int i, j;

    public AddByElementsThread(int[][] A, int[][] B, int[][] C, int i, int j){
        this.A = A;
        this.B = B;
        this.C = C;
        this.i = i;
        this.j = j;
    }

    @Override
    public void run() {
        this.C[i][j] = A[i][j] + B[i][j];
    }
}
