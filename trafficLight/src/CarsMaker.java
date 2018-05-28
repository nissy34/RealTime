import javax.swing.JPanel;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * Created on Tevet 5770 
 */



public class CarsMaker extends Thread
{
	JPanel myPanel;
	private ShloshaAvot myRamzor;
	int key;
	ConcurrentLinkedQueue carQueue;
	Event64 evCar_finish,evCar_arrived;

	public CarsMaker(JPanel myPanel, ShloshaAvot myRamzor, int key, ConcurrentLinkedQueue carQueue,Event64 evCar_arrived,Event64 evCar_finish)
	{
		this.myPanel=myPanel;
		this.myRamzor=myRamzor;
		this.key=key;
		this.carQueue=carQueue;
		this.evCar_arrived=evCar_arrived;
		this.evCar_finish=evCar_finish;
		setDaemon(true);
		start();
	}

	public void run()
	{

		try {
			while (true)
			{
			while(!carQueue.isEmpty()) {
				if (!myRamzor.isStop()) {
					new CarMoovingWithNum(myPanel, myRamzor, key,(int)carQueue.poll());
				}
				yield();
			}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
