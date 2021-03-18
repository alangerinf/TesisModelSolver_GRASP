/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asus
 */
public class ModelCase implements Cloneable{
    
    private List<Vehicle> vehiclesList;
    private List<Node> nodesList;
    private long totalTime; //total time in sec
    private double[][] timeBetweenNodes;
    private double[][] distanceBetweenNodes;
    private int nodesNumber;
    
    public ModelCase(int nodesNumber) {
        this.nodesNumber = nodesNumber;
        this.timeBetweenNodes = new double[nodesNumber][nodesNumber];
        this.distanceBetweenNodes = new double[nodesNumber][nodesNumber];
    }
    
    public boolean addNode(Node node) {
        if(getNodesList()==null){
            setNodesList(new ArrayList<>());
        }
        return getNodesList().add(node);
    }
    
    public boolean addVehicle(Vehicle vehicle) {
        if(getVehiclesList()==null){
            setVehiclesList(new ArrayList<>());
        }
        return getVehiclesList().add(vehicle);
    }
    
    public boolean removeNode(Node node) {
        if(getNodesList()==null){
            setNodesList(new ArrayList<>());
        }
        return getNodesList().removeIf(p -> p.getId() == node.getId());
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    /**
     * @return the vehiclesList
     */
    public List<Vehicle> getVehiclesList() {
        return vehiclesList;
    }

    /**
     * @param vehiclesList the vehiclesList to set
     */
    public void setVehiclesList(List<Vehicle> vehiclesList) {
        this.vehiclesList = vehiclesList;
    }

    /**
     * @return the nodesList
     */
    public List<Node> getNodesList() {
        return nodesList;
    }

    /**
     * @param nodesList the nodesList to set
     */
    public void setNodesList(List<Node> nodesList) {
        this.nodesList = nodesList;
    }

    /**
     * @return the totalTime
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * @param totalTime the totalTime to set
     */
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * @return the timeBetweenNodes
     */
    public double[][] getTimeBetweenNodes() {
        return timeBetweenNodes;
    }


    /**
     * @return the nodesNumber
     */
    public int getNodesNumber() {
        return nodesNumber;
    }

    /**
     * @param nodesNumber the nodesNumber to set
     */
    public void setNodesNumber(int nodesNumber) {
        this.nodesNumber = nodesNumber;
    }

    /**
     * @return the distanceBetweenNodes
     */
    public double[][] getDistanceBetweenNodes() {
        return distanceBetweenNodes;
    }

    /**
     * @param distanceBetweenNodes the distanceBetweenNodes to set
     */
    public void setDistanceBetweenNodes(double[][] distanceBetweenNodes) {
        this.distanceBetweenNodes = distanceBetweenNodes;
    }

    /**
     * @param timeBetweenNodes the timeBetweenNodes to set
     */
    public void setTimeBetweenNodes(double[][] timeBetweenNodes) {
        this.timeBetweenNodes = timeBetweenNodes;
        System.out.println("editando time");
        for(int i=0;i< timeBetweenNodes.length;i++) {
            double[] data = timeBetweenNodes[i];
            for (int j=0;j< data.length;j++) {
                System.out.print("\t"+i+","+j+" "+timeBetweenNodes[i][j]);
            }
            System.out.println();
        }
    }

    public void sortNodeList() {
       Node temp = new Node();
        for(int i=1; i<getNodesList().size()-1; i++) {
            Node nodeI = getNodesList().get(i);
            for(int j=i+1;j<getNodesList().size()-1; j++) {
                Node nodeJ = getNodesList().get(j);
                if(nodeJ.getTimeFrame().getLowerLimit()<nodeI.getTimeFrame().getLowerLimit()){
                    temp.setId(nodeI.getId());
                    temp.setLocation(nodeI.getLocation());
                    temp.setPosition(nodeI.getPosition());
                    temp.setProductDemand(nodeI.getProductDemand());
                    temp.setResidualDemand(nodeI.getResidualDemand());
                    temp.setTimeFrame(nodeI.getTimeFrame());
                    
                    nodeI.setId(nodeJ.getId());
                    nodeI.setLocation(nodeJ.getLocation());
                    nodeI.setPosition(nodeJ.getPosition());
                    nodeI.setProductDemand(nodeJ.getProductDemand());
                    nodeI.setResidualDemand(nodeJ.getResidualDemand());
                    nodeI.setTimeFrame(nodeJ.getTimeFrame());
                    
                    nodeJ.setId(temp.getId());
                    nodeJ.setLocation(temp.getLocation());
                    nodeJ.setPosition(temp.getPosition());
                    nodeJ.setProductDemand(temp.getProductDemand());
                    nodeJ.setResidualDemand(temp.getResidualDemand());
                    nodeJ.setTimeFrame(temp.getTimeFrame());
                }
            }
        }
    }

    
}
