import javax.swing.JPanel;

/*
 * Created on Tevet 5770 
 */



public class CarsMaker extends Thread
{
	JPanel myPanel;
	private ShloshaAvot myRamzor;
	int key;
	public CarsMaker(JPanel myPanel,ShloshaAvot myRamzor, int key) 
	{
		this.myPanel=myPanel;
		this.myRamzor=myRamzor;
		this.key=key;
		setDaemon(true);
		start();
	}

	public void run()
	{
		int i=0;
		try {
			while (true)
			{
				sleep(300);
				if ( !myRamzor.isStop())
				{
					new CarMoovingWithNum(myPanel,myRamzor,key,i++);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
