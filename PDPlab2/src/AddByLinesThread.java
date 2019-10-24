public class AddByLinesThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private int i;

    public AddByLinesThread(int[][] A, int[][] B, int[][] C, int i){
        this.A = A;
        this.B = B;
        this.C = C;
        this.i = i;
    }

    @Override
    public void run() {
        for(int j=0; j<this.A[0].length; j++){
            C[i][j] = A[i][j] + B[i][j];
        }
    }
}
