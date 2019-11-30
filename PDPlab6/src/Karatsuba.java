import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Karatsuba {
    private Polinom p1;
    private Polinom p2;

    public Karatsuba(Polinom p1, Polinom p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public Polinom karatsubaMultiplicationWrapper(){
        return new Polinom(karatsubaMultiplication(p1, p2));
    }

    private int[] karatsubaMultiplication(Polinom p1, Polinom p2) {

        final int[] product = new int[p2.len() + p1.len()];

        //Handle the base case where the polynomial has only one coefficient
        if (p1.len() == 1) {
            product[0] = p1.get(0) * p2.get(0);
            return product;
        }

        final int halfArraySize = p1.len() / 2;
        final int halfArraySizeM = p2.len() / 2;

        //Declare arrays to hold halved factors
        final int[] multiplicandLow = new int[halfArraySize];
        final int[] multiplicandHigh = new int[halfArraySize];
        final int[] multiplierLow = new int[halfArraySizeM];
        final int[] multiplierHigh = new int[halfArraySizeM];

        final int[] multiplicandLowHigh = new int[halfArraySize];
        final int[] multiplierLowHigh = new int[halfArraySizeM];

        //Fill in the low and high arrays
        for (int halfSizeIndex = 0; halfSizeIndex < halfArraySize; ++halfSizeIndex) {

            multiplicandLow[halfSizeIndex] = p1.get(halfSizeIndex);
            multiplicandHigh[halfSizeIndex] = p1.get(halfSizeIndex + halfArraySize);
            multiplicandLowHigh[halfSizeIndex] = multiplicandLow[halfSizeIndex] + multiplicandHigh[halfSizeIndex];

            multiplierLow[halfSizeIndex] = p2.get(halfSizeIndex);
            multiplierHigh[halfSizeIndex] = p2.get(halfSizeIndex + halfArraySize);
            multiplierLowHigh[halfSizeIndex] = multiplierLow[halfSizeIndex] + multiplierHigh[halfSizeIndex];

        }

        //Recursively call method on smaller arrays and construct the low and high parts of the product
        final int[] productLow = karatsubaMultiplication(new Polinom(multiplicandLow), new Polinom(multiplierLow));
        final int[] productHigh = karatsubaMultiplication(new Polinom(multiplicandHigh), new Polinom(multiplierHigh));

        final int[] productLowHigh = karatsubaMultiplication(new Polinom(multiplicandLowHigh), new Polinom(multiplierLowHigh));

        //Construct the middle portion of the product
        final int[] productMiddle = new int[p1.len()];
        for (int halfSizeIndex = 0; halfSizeIndex < p1.len(); halfSizeIndex++) {
            productMiddle[halfSizeIndex] = productLowHigh[halfSizeIndex] - productLow[halfSizeIndex] - productHigh[halfSizeIndex];
        }

        //Assemble the product from the low, middle and high parts. Start with the low and high parts of the product.
        for (int halfSizeIndex = 0, middleOffset = p1.len() / 2; halfSizeIndex < p1.len(); ++halfSizeIndex) {
            product[halfSizeIndex] += productLow[halfSizeIndex];
            product[halfSizeIndex + p1.len()] += productHigh[halfSizeIndex];
            product[halfSizeIndex + middleOffset] += productMiddle[halfSizeIndex];
        }

        return product;
    }

    public Polinom karatsubaParallelMultiplicationWrapper(){
        return new Polinom(karatsubaParallelMultiplication(p1, p2));
    }

    private int[] karatsubaParallelMultiplication(final Polinom p1, final Polinom p2){

        final int[] product = new int[p2.len() + p1.len()];

        //Handle the base case where the polynomial has only one coefficient
        if (p1.len() == 1) {
            product[0] = p1.get(0) * p2.get(0);
            return product;
        }

        final int halfArraySize = p1.len() / 2;
        final int halfArraySizeM = p2.len() / 2;

        //Declare arrays to hold halved factors
        final int[] multiplicandLow = new int[halfArraySize];
        final int[] multiplicandHigh = new int[halfArraySize];
        final int[] multiplierLow = new int[halfArraySizeM];
        final int[] multiplierHigh = new int[halfArraySizeM];

        final int[] multiplicandLowHigh = new int[halfArraySize];
        final int[] multiplierLowHigh = new int[halfArraySizeM];

        //Fill in the low and high arrays
        for (int halfSizeIndex = 0; halfSizeIndex < halfArraySize; ++halfSizeIndex) {

            multiplicandLow[halfSizeIndex] = p1.get(halfSizeIndex);
            multiplicandHigh[halfSizeIndex] = p1.get(halfSizeIndex + halfArraySize);
            multiplicandLowHigh[halfSizeIndex] = multiplicandLow[halfSizeIndex] + multiplicandHigh[halfSizeIndex];

            multiplierLow[halfSizeIndex] = p2.get(halfSizeIndex);
            multiplierHigh[halfSizeIndex] = p2.get(halfSizeIndex + halfArraySize);
            multiplierLowHigh[halfSizeIndex] = multiplierLow[halfSizeIndex] + multiplierHigh[halfSizeIndex];

        }
        final ExecutorService service = Executors.newFixedThreadPool(this.p1.len());
        final Future<int[]> resultLow = service.submit(() -> karatsubaMultiplication(new Polinom(multiplicandLow), new Polinom(multiplierLow)));
        final Future<int[]> resultHigh = service.submit(() -> karatsubaMultiplication(new Polinom(multiplicandHigh), new Polinom(multiplierHigh)));
        final Future<int[]> resultLowHigh = service.submit(() -> karatsubaMultiplication(new Polinom(multiplicandLowHigh), new Polinom(multiplierLowHigh)));
        //Construct the middle portion of the product
        int[] productLow = new int[0];
        try {
            productLow = resultLow.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        int[] productHigh = new int[0];
        try {
            productHigh = resultHigh.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        int[] productLowHigh = new int[0];
        try {
            productLowHigh = resultLowHigh.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final int[] productMiddle = new int[p1.len()];
        for (int halfSizeIndex = 0; halfSizeIndex < p1.len(); ++halfSizeIndex) {
            productMiddle[halfSizeIndex] = productLowHigh[halfSizeIndex] - productLow[halfSizeIndex] - productHigh[halfSizeIndex];
        }

        //Assemble the product from the low, middle and high parts. Start with the low and high parts of the product.
        for (int halfSizeIndex = 0, middleOffset = p1.len() / 2; halfSizeIndex < p1.len(); ++halfSizeIndex) {
            product[halfSizeIndex] += productLow[halfSizeIndex];
            product[halfSizeIndex + p1.len()] += productHigh[halfSizeIndex];
            product[halfSizeIndex + middleOffset] += productMiddle[halfSizeIndex];
        }

        return product;
    }
}
