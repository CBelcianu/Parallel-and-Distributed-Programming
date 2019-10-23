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
            this.transaction.getSrc().mtx.acquire();
            if(this.transaction.getSrc().getBalance()>sum) {
                this.transaction.getSrc().alterBalance(-sum, this.transaction.getId(), this.transaction.getDest().getId());
                if (this.transaction.getDest().mtx.tryAcquire()) {
                    this.transaction.getDest().alterBalance(sum, this.transaction.getId(), this.transaction.getSrc().getId());
                    this.transaction.getDest().mtx.release();
                    this.transaction.getSrc().mtx.release();
                } else {
                    this.transaction.setStatus(true);
                    this.transaction.getSrc().alterBalance(sum, this.transaction.getId(), null);
                    this.transaction.getSrc().dropLast2();
                    this.transaction.getSrc().mtx.release();
                }
            }
            else{
                this.transaction.setStatus(true);
                this.transaction.getSrc().mtx.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
