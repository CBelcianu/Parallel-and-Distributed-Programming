public class DownSweep implements Runnable {
    private int[] a;
    private int pos;
    private int d;
    private int power;

    DownSweep(int[] a, int pos, int power){
        this.a=a;
        this.pos=pos;
        this.power=power;
    }

    @Override
    public void run() {
        int aux = a[pos + power/2 - 1];
        a[pos + power/2 - 1] = a[pos + power - 1];
        a[pos + power - 1] += aux;
    }
}
