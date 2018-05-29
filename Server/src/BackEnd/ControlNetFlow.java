package BackEnd;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by USER on 29/05/2018.
 */
public class ControlNetFlow {

    public List<IntersectionClient> intersectionClientsList;


    public void addIntersectionClient(IntersectionClient intersectionClient) {
        this.intersectionClientsList.add(intersectionClient);
        int size=intersectionClientsList.size();
        connectIntersections(intersectionClientsList.get(size), intersectionClientsList.get(size - 1));
    }

    private void connectIntersections(IntersectionClient newIntersectionClient, IntersectionClient oldIntersectionClient) {
        Event64 temp=new Event64();
        oldIntersectionClient.setEv_startNextIntersection(temp);
        newIntersectionClient.setEv_startIntersection(temp);


    }
}
