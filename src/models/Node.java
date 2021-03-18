/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Asus
 */
public class Node implements Comparable, Cloneable{
    
    private int id;
    private Location location;
    private Position position;
    private TimeFrame timeFrame;
    private int productDemand;
    private int residualDemand;
    private double timeArrived;

    public Node(int id,
                Location location){
        this.id = id;
        this.location = location;
        this.timeArrived = 0;
        
    }
    public Node(){
        
    }
    
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the timeFrame
     */
    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    /**
     * @param timeFrame the timeFrame to set
     */
    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return the productDemand
     */
    public int getProductDemand() {
        return productDemand;
    }

    /**
     * @param productDemand the productDemand to set
     */
    public void setProductDemand(int productDemand) {
        this.productDemand = productDemand;
    }

    /**
     * @return the residualDemand
     */
    public int getResidualDemand() {
        return residualDemand;
    }

    /**
     * @param residualDemand the residualDemand to set
     */
    public void setResidualDemand(int residualDemand) {
        this.residualDemand = residualDemand;
    }

    @Override
    public int compareTo(Object t) {
        int compareage= ( (int)this.getTimeFrame().getLowerLimit());
        /* For Ascending order*/
        return (int)((Node)t).getTimeFrame().getLowerLimit() -compareage;
    }

    /**
     * @return the timeArrived
     */
    public double getTimeArrived() {
        return timeArrived;
    }

    /**
     * @param timeArrived the timeArrived to set
     */
    public void setTimeArrived(double timeArrived) {
        this.timeArrived = timeArrived;
    }
    
    
    
}
