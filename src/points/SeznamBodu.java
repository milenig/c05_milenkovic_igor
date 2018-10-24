package points;

import java.util.ArrayList;
import java.util.List;

public class SeznamBodu {

    private List<Integer> pointList = new ArrayList<>(); //seznam pro usecku
    private List<Integer> NPointList = new ArrayList<>(); //seznam bodu pro n uhlenik
    private List<Integer> PolynomPointList = new ArrayList<>(); //seznam bodu pro polynom

    public SeznamBodu() { //konstruktor
    }

    //gettry a settry:

    public List<Integer> getPointList() {
        return pointList;
    }

    public List<Integer> getNPointList() {
        return NPointList;
    }

    public List<Integer> getPolynomPointList() {
        return PolynomPointList;
    }

    public void addToPointlist(int point) {
        pointList.add(point);
    }

    public void addToNPointList(int point) {
        NPointList.add(point);
    }

    public void addToPolynomPointList(int point) {
        PolynomPointList.add(point);
    }
}
