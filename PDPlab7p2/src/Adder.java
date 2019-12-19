import java.util.Collections;

class Adder {
    static void add(final Node node) throws InterruptedException {
        int carry = 0;
        if (node.isFirst) {
            for (int i = node.number.length - 1; i >= 0; i--) {
                final int res = node.number[i] + node.initialNumber[i] + carry;
                carry = res / 10;
                node.parent.queue.put(res % 10);
            }
            if (carry > 0)
                node.parent.queue.put(carry);
            node.parent.queue.put(-1);
        } else {
            int i = node.number.length - 1;
            boolean endDigit = false;
            while (true) {
                int res = 0;
                int digit = 0;

                if (!endDigit)
                    digit = node.queue.take();

                if (digit == -1)
                    endDigit = true;

                if (endDigit && i < 0)
                    break;

                if (!endDigit)
                    res += digit;
                if (i >= 0) res += node.number[i];
                res += carry;
                carry = res / 10;
                if (!node.isLast)
                    node.parent.queue.put(res % 10);
                else
                    node.result.add(res % 10);
                i--;
            }
            if (node.isLast) {
                if (carry > 0)
                    node.result.add(carry);
                Collections.reverse(node.result);
                System.out.println("\n------------------");
                System.out.print("Result: ");
                for (final int digit : node.result) {
                    System.out.print(digit);
                }
                return;
            }
            if (carry > 0)
                node.parent.queue.put(carry);
            node.parent.queue.put(-1);
        }
    }
}
