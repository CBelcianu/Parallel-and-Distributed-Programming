public class Thread implements Runnable {
    private Integer iD;
    private Transaction transaction;

    public Thread(Integer iD, Transaction transaction){
        this.iD = iD;
        this.transaction = transaction;
    }

    @Override
    public void run() {
        Integer sum = this.transaction.getSum();
        try {
            if(this.transaction.getSrc().getBalance()>sum) {
                this.transaction.getSrc().mtx.acquire();
                this.transaction.getSrc().alterBalance(-sum, this.transaction.getId());
                if (this.transaction.getDest().mtx.tryAcquire()) {
                    this.transaction.getDest().alterBalance(sum, this.transaction.getId());
                    this.transaction.getDest().mtx.release();
                    this.transaction.getSrc().mtx.release();
                } else {
                    this.transaction.setStatus(true);
                    this.transaction.getSrc().alterBalance(sum, this.transaction.getId());
                    this.transaction.getSrc().dropLast2();
                    this.transaction.getSrc().mtx.release();
                }
            }
            else{
                this.transaction.setStatus(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
