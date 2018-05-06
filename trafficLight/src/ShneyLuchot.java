import javafx.event.Event;

import java.awt.Color;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */


class ShneyLuchot extends Thread
{
	Ramzor ramzor;
	JPanel panel;
	Event64 evToShabat,evToWeekday,evToGreen,evToRed,evAtRed;
	private int key;
	enum OutState {ON_SHABAT,ON_WEEKDAY };
	enum InState {ON_RED,ON_GREEN};
	OutState outState;
	InState inState;


	public ShneyLuchot( Ramzor ramzor, JPanel panel,int key, Event64 evToShabat, Event64 evToWeekday, Event64 evToGreen, Event64 evToRed, Event64 evAtRed)
	{
		super();
		this.ramzor = ramzor;
		this.panel = panel;
		this.evToShabat = evToShabat;
		this.evToWeekday = evToWeekday;
		this.evToGreen = evToGreen;
		this.evToRed = evToRed;
		this.evAtRed = evAtRed;
		this.key=key;
		start();

	}

	public void run()
	{

			outState=OutState.ON_WEEKDAY;

			while (true)
			{
				switch (outState){
					case ON_SHABAT:{
						evToWeekday.waitEvent();
						outState=OutState.ON_WEEKDAY;

						break;
					}
					case ON_WEEKDAY:{
						inState=InState.ON_RED;
						SetToRed();

						while (outState == OutState.ON_WEEKDAY)
						{
							switch (inState)
							{
								case ON_RED:
								{

									while (true){

										if(evToGreen.arrivedEvent()){
											evToGreen.waitEvent();
											SetToGreen();
											inState=InState.ON_GREEN;
											break;
										}
										else if(evToShabat.arrivedEvent())
										{
											evToShabat.waitEvent();
											SetToGray();
											outState = OutState.ON_SHABAT;
											break;
										}
										else
											yield();
									}
									break;
								}

								case ON_GREEN:
								{
									while (true){
										if(evToRed.arrivedEvent()){
											evToRed.waitEvent();
											SetToRed();
											evAtRed.sendEvent();
											inState=InState.ON_RED;
											break;
										}
										else if(evToShabat.arrivedEvent())
										{
											evToShabat.waitEvent();
											SetToGray();
											outState = OutState.ON_SHABAT;
											break;
										}
										else
											yield();
									}

									break;
								}
							}
						}
						break;
					}

				}

			}

	}

	private void SetToGray()
	{
		setLight(1, Color.GRAY);
		setLight(2,Color.GRAY);
	}


	private void SetToGreen()
	{
		setLight(1, Color.GRAY);
		setLight(2,Color.GREEN);
	}

	private void SetToRed()
	{
		setLight(1, Color.RED);
		setLight(2,Color.GRAY);
	}

	public void setLight(int place, Color color)
	{
		ramzor.colorLight[place-1]=color;
		panel.repaint();
	}
}
