import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class CheckerTask extends TimerTask {
    private Integer noTransactions;
    private List<Account> accounts;
    private List<Pair<Account, List<Pair<Integer, Integer>>>> toCheck;

    public CheckerTask(Integer noTransactions, List<Account> accounts){
        this.noTransactions = noTransactions;
        this.accounts = accounts;
        this.toCheck = new ArrayList<>();
    }

    @Override
    public void run() {
        System.out.println("CHECKING....");
        Integer srcSum = 0;
        Integer destSum = 0;
        boolean isCorrupted = false;
        for (Account acc : this.accounts) {
            try {
                acc.mtx.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.toCheck.add(new Pair(acc, acc.getLogs()));
            acc.mtx.release();
        }
        for(Integer tid=1; tid<=noTransactions; tid++) {
            for (Pair p : this.toCheck) {
                for (Pair l : (List<Pair<Integer, Integer>>) p.getRight()) {
                    if (l.getLeft() == tid) {
                        if (srcSum != 0) {
                            destSum = (Integer) l.getRight();
                        } else {
                            srcSum = (Integer) l.getRight();
                        }
                    }
                }
            }
            if (!(srcSum + destSum == 0)) {
                isCorrupted = true;
            }
            srcSum = 0;
            destSum = 0;
        }
        if (isCorrupted){
            System.out.println("Inconsistency");
        }
        else {
            System.out.println("Success");
        }
    }
}
