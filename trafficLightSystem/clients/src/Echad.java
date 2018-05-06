import java.awt.Color;

import javax.swing.JPanel;
import javax.xml.transform.sax.SAXTransformerFactory;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */


class Echad extends Thread
{
	Ramzor ramzor;
	JPanel panel;
	enum State {ON,OFF};
	State state;
	public Echad( Ramzor ramzor,JPanel panel)
	{
		this.ramzor=ramzor;
		this.panel=panel;
		start();
	}

	public void run()
	{
		try 
		{
            state=State.ON;
            setToYellow();

            while (true)
			{

				switch (state){
					case OFF:
						sleep(500);
						state=State.ON;
						setToYellow();
						break;
					case ON:
						sleep(500);
						state=State.OFF;
						setToGray();
						break;
				}


			}
		} catch (InterruptedException e) {}

	}

	private void setToYellow()
	{
		setLight(1, Color.YELLOW);
	}
	private void setToGray()
	{
		setLight(1,Color.GRAY);
	}

	public void setLight(int place, Color color)
	{
		ramzor.colorLight[place-1]=color;
		panel.repaint();
	}
}
