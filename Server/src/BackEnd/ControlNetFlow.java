package BackEnd;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 29/05/2018.
 */
public class ControlNetFlow {

    public List<IntersectionClient> intersectionClientsList=new ArrayList<>();


    public void addIntersectionClient(IntersectionClient intersectionClient) {
        this.intersectionClientsList.add(intersectionClient);

        if(intersectionClientsList.size() >1)
            connectIntersections();
    }

    private void connectIntersections() {
        Event64 temp=new Event64();
        int size=intersectionClientsList.size()-1;
        IntersectionClient oldIntersectionClient= intersectionClientsList.get(size-1);
        IntersectionClient newIntersectionClient = intersectionClientsList.get(size);
        oldIntersectionClient.setEv_startNextIntersection(temp);
        newIntersectionClient.setEv_startIntersection(temp);
       if(!oldIntersectionClient.isAlive())
            oldIntersectionClient.start();
        newIntersectionClient.start();

    }
}
