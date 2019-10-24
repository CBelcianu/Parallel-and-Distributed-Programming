import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class CheckerThread implements Runnable {
    private Integer noTrasnactions;
    private List<Account> accounts;

    public CheckerThread(Integer noTrasnactions, List<Account> accounts){
        this.noTrasnactions = noTrasnactions;
        this.accounts = accounts;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        CheckerTask checkerTask = new CheckerTask(this.noTrasnactions, this.accounts);
        timer.schedule(checkerTask, 50,50);
        try {
            TimeUnit.MILLISECONDS.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checkerTask.cancel();
        timer.cancel();

    }
}
