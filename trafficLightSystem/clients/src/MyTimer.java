

public class MyTimer extends Thread
{
    private final long time;
    private final Event64 evTime;

    public MyTimer(long time,Event64 evTime)
    {
        this.time=time;
        this.evTime=evTime;
        setDaemon(true);
        start();
    }

    public void run()
    {
        try 
		{
            sleep(time);
        } catch (InterruptedException ex) {
            int x=5;
        }
        evTime.sendEvent();
    }

}