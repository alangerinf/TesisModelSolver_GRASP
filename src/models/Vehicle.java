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
public class Vehicle {
    
    private long id;
    private int productCapacity;
    private int trashCapacity;
    private int currentProductCapacityUsed;
    private int currentTrashCapacityUsed;
    private long currentTime;
    

    public Vehicle() {
        this.currentProductCapacityUsed = 0;
        this.currentTrashCapacityUsed = 0;
        this.currentTime = 0;
    }
    
    
    /**
     * @return the productCapacity
     */
    public int getProductCapacity() {
        return productCapacity;
    }

    /**
     * @param productCapacity the productCapacity to set
     */
    public void setProductCapacity(int productCapacity) {
        this.productCapacity = productCapacity;
    }

    /**
     * @return the trashCapacity
     */
    public int getTrashCapacity() {
        return trashCapacity;
    }

    /**
     * @param trashCapacity the trashCapacity to set
     */
    public void setTrashCapacity(int trashCapacity) {
        this.trashCapacity = trashCapacity;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the currentProductCapacityUsed
     */
    public int getCurrentProductCapacityUsed() {
        return currentProductCapacityUsed;
    }

    /**
     * @param currentProductCapacityUsed the currentProductCapacityUsed to set
     */
    public void setCurrentProductCapacityUsed(int currentProductCapacityUsed) {
        this.currentProductCapacityUsed = currentProductCapacityUsed;
    }
    
    public void addToCurrentProductCapacityUsed(int cantidad) {
        this.currentProductCapacityUsed += cantidad;
    }
    
    

    /**
     * @return the currentTrashCapacityUsed
     */
    public int getCurrentTrashCapacityUsed() {
        return currentTrashCapacityUsed;
    }

    /**
     * @param currentTrashCapacityUsed the currentTrashCapacityUsed to set
     */
    public void setCurrentTrashCapacityUsed(int currentTrashCapacityUsed) {
        this.currentTrashCapacityUsed = currentTrashCapacityUsed;
    }
    
    public void addToCurrentTrashCapacityUsed(int cantidad) {
        this.currentTrashCapacityUsed += cantidad;
    }

    /**
     * @return the currentTime
     */
    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * @param currentTime the currentTime to set
     */
    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    
    
}
