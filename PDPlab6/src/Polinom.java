import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Polinom {
    private int[] coefficients;
    private Lock[] locks;

    public Polinom(int[] coefficients){
        this.coefficients = coefficients;
    }

    public int get(final int index) {
        return this.coefficients[index];
    }

    public int len() {
        return this.coefficients.length;
    }

    public String toString() {
        final StringBuffer res = new StringBuffer();
        boolean firstTime = true;

        res.append("\n[");
        for (int i = 0; i < this.len() - 1; i++) {
            double coefficient = this.get(i);
            if (firstTime) {
                firstTime = false;
            } else {
                res.append(", ");
            }
            res.append(coefficient);
        }
        res.append("]\n");

        return res.toString();
    }

    public Polinom classicMultiplication(final Polinom multiplier){
        final int[] product = new int[multiplier.len() + this.coefficients.length];

        for (int multiplicandIndex = 0; multiplicandIndex < this.coefficients.length; ++multiplicandIndex) {
            for (int multiplierIndex = 0; multiplierIndex < multiplier.len(); ++multiplierIndex) {
                product[multiplicandIndex + multiplierIndex] += this.coefficients[multiplicandIndex] * multiplier.get(multiplierIndex);
            }
        }

        return new Polinom(product);
    }

    public Polinom classicParallelMultiplication(final Polinom polinom){
        int resultLen = this.coefficients.length + polinom.len();
        locks = new Lock[resultLen + 1];
        for (int i = 0; i <= resultLen; i++) {
            locks[i] = new ReentrantLock();
        }
        final int[] product = new int[polinom.len() + this.coefficients.length];

        ExecutorService service = Executors.newFixedThreadPool(this.coefficients.length);
        for (int i = 0; i < this.coefficients.length; i++) {
            final int pos = i;
            service.execute(() -> {
                for (int j = 0; j < polinom.len(); j++) {
                    locks[pos + j].lock();
                    product[pos + j] += this.coefficients[pos] * polinom.get(j);
                    locks[pos + j].unlock();
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Polinom(product);
    }
}
