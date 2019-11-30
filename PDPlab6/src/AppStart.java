public class AppStart {

    public static void main(String args[]) {
        final int[] p1Coefficients = {3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12,
                3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12};
        final int[] p2Coefficients = {3, 4, 6, 7, 14, 15, 20, 23, 3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12,
                3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12, 3, 4, 6, 7, 9, 10, 15, 12};

        final Polinom p1 = new Polinom(p1Coefficients);
        final Polinom p2 = new Polinom(p2Coefficients);

        long startTime;
        long stopTime;
        long elapsedTime;

        startTime = System.currentTimeMillis();
        Polinom resultNormal = p1.classicMultiplication(p2);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Classic " + resultNormal.toString());
        System.out.println("\tBENCHMARK TOOK: " + elapsedTime + " us.\n\n");

        startTime = System.currentTimeMillis();
        resultNormal = p1.classicParallelMultiplication(p2);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Parallel classic " + resultNormal.toString());
        System.out.println("\tBENCHMARK TOOK: " + elapsedTime + " us.\n\n");

        Karatsuba karatsuba = new Karatsuba(p1, p2);
        startTime = System.currentTimeMillis();
        System.out.println("Karatsuba: " + karatsuba.karatsubaMultiplicationWrapper());
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("\tBENCHMARK TOOK: " + elapsedTime + " us.\n\n");

        startTime = System.currentTimeMillis();
        System.out.println("Parallel karatsuba" + karatsuba.karatsubaParallelMultiplicationWrapper());
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("\tBENCHMARK TOOK: " + elapsedTime + " us.\n\n");

    }

}
