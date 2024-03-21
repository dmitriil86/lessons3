package classes;

public class ThreadTest extends Thread {
    void readCache(Double aDouble)
    {

    }

    @Override
    public void run()
    {
        do
        {
            System.out.println("thread!!!");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while(true);
    }

}
