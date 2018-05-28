import javafx.scene.effect.Light;

import javax.swing.JRadioButton;
import java.util.concurrent.ConcurrentLinkedQueue;


public class BuildTrafficLight
{

	public static void main(String[] args) 
	{
		Controller controller;
		ConcurrentLinkedQueue[] carsQeque =new ConcurrentLinkedQueue[4];
		for (int i=0;i<4;++i) {
			carsQeque[i]=new ConcurrentLinkedQueue();
		}
		final int[] GROUP1=new int[]{1,2,4,5,6,7,12,13};
		final int[] GROUP2=new int[]{2,3,4,5,8,11,14,15};
		final int[] GROUP3=new int[]{0,6,7,9,10,12,13};

		ConcurrentLinkedQueue concurrentLinkedQueue=new ConcurrentLinkedQueue();
		Event64 evGroup1ToGreen=new Event64(),
				evGroup2ToGreen=new Event64(),
				evGroup3ToGreen=new Event64(),
                evTo_Shabat_client=new Event64(),
                evTo_weekday_client=new Event64(),
                evTo_freeze_client=new Event64(),
				evCar_arrived =new Event64();

		Event64[] evToGreen,evToRed,evToShabat,evToWeekday,evAtRed;


		final int numOfLights=4+12+1;
        evToGreen=new Event64[numOfLights-1];
        evToRed=new Event64[numOfLights-1];
        evToShabat=new Event64[numOfLights-1];
        evToWeekday=new Event64[numOfLights-1];
        evAtRed=new Event64[numOfLights-1];

        for (int i=0 ; i<numOfLights-1;i++)
        {
            evToGreen[i]=new Event64();
            evToRed[i]=new Event64();
            evToShabat[i]=new Event64();
            evToWeekday[i]=new Event64();
            evAtRed[i]=new Event64();
        }
		Ramzor ramzorim[]=new Ramzor[numOfLights];



        ramzorim[0]=new Ramzor(3,40,430,110,472,110,514,110);
		ramzorim[1]=new Ramzor(3,40,450,310,450,352,450,394);
		ramzorim[2]=new Ramzor(3,40,310,630,280,605,250,580);
		ramzorim[3]=new Ramzor(3,40,350,350,308,350,266,350);

		ramzorim[4]=new Ramzor(2,20,600,18,600,40);
		ramzorim[5]=new Ramzor(2,20,600,227,600,205);
		ramzorim[6]=new Ramzor(2,20,600,255,600,277);
		ramzorim[7]=new Ramzor(2,20,600,455,600,433);
		ramzorim[8]=new Ramzor(2,20,575,475,553,475);
		ramzorim[9]=new Ramzor(2,20,140,608,150,590);
		ramzorim[10]=new Ramzor(2,20,205,475,193,490);
		ramzorim[11]=new Ramzor(2,20,230,475,250,475);
		ramzorim[12]=new Ramzor(2,20,200,453,200,433);
		ramzorim[13]=new Ramzor(2,20,200,255,200,277);
		ramzorim[14]=new Ramzor(2,20,200,227,200,205);
		ramzorim[15]=new Ramzor(2,20,200,18,200,40);

		ramzorim[16]=new Ramzor(1,30,555,645);


        TrafficLightFrame tlf=new TrafficLightFrame("installation of traffic lights",ramzorim);

        for (int i=0;i<4;i++) {
			ShloshaAvot shloshaAvot = new ShloshaAvot(ramzorim[i], tlf.myPanel, i + 1, evToShabat[i], evToWeekday[i], evToGreen[i], evToRed[i], evAtRed[i]);
			new CarsMaker(tlf.myPanel, shloshaAvot, i+1,carsQeque[i],null,null);
		}
        for (int i=4;i<16;i++)
		    new ShneyLuchot(ramzorim[i],tlf.myPanel,i,evToShabat[i],evToWeekday[i],evToGreen[i],evToRed[i],evAtRed[i]);

		new Echad(ramzorim[16],tlf.myPanel);




        Event64 evP_ToShabat=new Event64();
        Event64 evP_Toweekday=new Event64();
		Event64 evP_Freeze=new Event64();
		Event64 evP_butt=new Event64();

		MyActionListener myListener=new MyActionListener(evP_butt,evP_ToShabat,evP_Toweekday,evP_Freeze);

		JRadioButton butt[]=new JRadioButton[14];

		for (int i=0;i<butt.length-1;i++) 
		{
			butt[i]  =new JRadioButton();
			butt[i].setName(Integer.toString(i+4));
			butt[i].setOpaque(false);
			butt[i].addActionListener(myListener);
			tlf.myPanel.add(butt[i]);
		}
		butt[0].setBounds(620, 30, 18, 18);
		butt[1].setBounds(620, 218, 18, 18);
		butt[2].setBounds(620, 267, 18, 18);
		butt[3].setBounds(620, 447, 18, 18);
		butt[4].setBounds(566, 495, 18, 18);
		butt[5].setBounds(162,608, 18, 18);
		butt[6].setBounds(213,495, 18, 18);
		butt[7].setBounds(240,457, 18, 18);
		butt[8].setBounds(220,443, 18, 18);
		butt[9].setBounds(220,267, 18, 18);
		butt[10].setBounds(220,218, 18, 18);
		butt[11].setBounds(220,30, 18, 18);

		butt[12]  =new JRadioButton();
		butt[12].setName(Integer.toString(16));
		butt[12].setBounds(50,30, 55, 20);
		butt[12].setText("שבת");
		butt[12].setOpaque(false);
		butt[12].addActionListener(myListener);
		tlf.myPanel.add(butt[12]);



		butt[13]  =new JRadioButton();
		butt[13].setName(Integer.toString(17));
		butt[13].setBounds(400,30, 55, 20);
		butt[13].setText("stop");
		butt[13].setOpaque(false);
		butt[13].addActionListener(myListener);
		tlf.myPanel.add(butt[13]);




        controller=new Controller(evP_Freeze,
				                  evToShabat,
                                  evToWeekday,
                                  evP_Toweekday,
                                  evP_ToShabat,
                                  evP_butt,//temp
                                  buildGroup(evToRed,GROUP1),
                                  buildGroup(evAtRed,GROUP1),
                                  buildGroup(evToGreen,GROUP1),
                                  buildGroup(evToRed,GROUP2),
                                  buildGroup(evAtRed,GROUP2),
                                  buildGroup(evToGreen,GROUP2),
                                  buildGroup(evToRed,GROUP3),
                                  buildGroup(evAtRed,GROUP3),
                                  buildGroup(evToGreen,GROUP3));




		Client770 client = new Client770(evTo_freeze_client,evTo_Shabat_client,evTo_weekday_client,evGroup1ToGreen,evGroup2ToGreen,evGroup3ToGreen);

		Thread t=new Thread(){
			@Override
			public void run()
			{
				while(true){
					if(evGroup1ToGreen.arrivedEvent()){
						evGroup1ToGreen.waitEvent();
                        System.out.println("###evGroup1ToGreen###");
						evP_butt.sendEvent(1);

					}
					else if(evGroup2ToGreen.arrivedEvent()){
						evGroup2ToGreen.resetEvent();
                        System.out.println("###evGroup2ToGreen##");

                        evP_butt.sendEvent(3);

                    }
					else if(evGroup3ToGreen.arrivedEvent()){
						evGroup3ToGreen.waitEvent();
                        System.out.println("###evGroup3ToGreen###");
                        evP_butt.sendEvent(0);

                    }
                    else if(evTo_freeze_client.arrivedEvent()){
                        evTo_freeze_client.waitEvent();
                        System.out.println("###evTo_freeze_client###");
                        butt[13].doClick();

                    }
                    else if(evTo_Shabat_client.arrivedEvent()){
                        evTo_Shabat_client.waitEvent();
                        System.out.println("###evTo_Shabat_client###");
                        butt[12].doClick();

                    }
                    else if(evTo_weekday_client.arrivedEvent()){
                        evTo_weekday_client.waitEvent();
                        System.out.println("###evTo_weekday_client###");
                        //butt[12].doClick();
                        evCar_arrived.sendEvent("0,1000");
						}
					else if(evCar_arrived.arrivedEvent()){
						String data =(evCar_arrived.waitEvent()).toString();
                    	int ramzor=Integer.parseInt(data.split(",")[0]);
						int carNum=Integer.parseInt(data.split(",")[1]);
						carsQeque[ramzor].add(carNum);
					}
                    else
                        yield();

				}

			}
		};
		t.start();
	}

	static Event64[] buildGroup(Event64[] from,int[] hwo){
	    Event64[] temp=new Event64[hwo.length];
	    for(int i=0;i<hwo.length;i++)
        {
            temp[i]=from[hwo[i]];
        }
        return temp;
    }
}
