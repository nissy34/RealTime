import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/*
 * Created on Tevet 5770 
 */



public class MyActionListener implements ActionListener
{
	Event64 evPressed;
    Event64 evShabat;
    Event64 evChol;

    public MyActionListener(Event64 evPressed, Event64 evShabat, Event64 evChol)
    {
        this.evPressed = evPressed;
        this.evShabat = evShabat;
        this.evChol = evChol;
    }


    public void actionPerformed(ActionEvent e)
    {

        JRadioButton butt = (JRadioButton) e.getSource();
        System.out.println(butt.getName());
        int key = Integer.parseInt(butt.getName());
        if (key==16)
            if (butt.isSelected())
                evShabat.sendEvent();
            else
                evChol.sendEvent();
        else
        {

            evPressed.sendEvent(key);

                butt.setSelected(false);
        }
    }

}
