/**
 * @author Florian
 */
package Prim;

public class WeightedVertex implements Comparable<WeightedVertex>, hasID {
    
    private final int v;
    private double distance;
    private int priority;
    
    private static int timestamp = 0;
    
    public WeightedVertex(int v, double distance){
        this.v = v;
        this.distance = distance;
        this.priority = timestamp;
        timestamp++;
    }
    
    public double getDistance(){
        return distance;
    }
    
    public void setDistance(double distance){
        this.distance = distance;
    }
    
    public int getV(){
        return v;
    }

    public int compareTo(WeightedVertex that) {
        if (this.getDistance() < that.getDistance())
            return -1;
        else if (this.getDistance() > that.getDistance())
            return 1;
        else {
        	if(this.priority() < that.priority()){
				return -1;
			}
			else{
				return 1;
			}
        }
    }

    private int priority() {
        return priority;
    }
    
	@Override
	public int getID() {
		return v;
	}

}