/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.ModelCase;
import models.Node;
import models.Location;
import models.TimeFrame;
import models.Vehicle;
/**
 *
 * @author Asus
 */
public class CaseImporter {
    
    private String path;
    
    public CaseImporter(String path)  {
        this.path = path;
        
    }
    
    public ModelCase extract() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileInputStream fstream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        final int cantNodes = getCantNodes(br);
        final int cantVehicles = getCantVehicles(br);
        ModelCase modelCase = new ModelCase(cantNodes);
        getDemandaProductos(br, modelCase);
        getDemandaResiduos(br, modelCase);
        getCapTotalProductos(br, modelCase);
        getCapTotalResiduos(br, modelCase);
        getTiempoLimInf(br, modelCase);
        getTiempoLimSup(br, modelCase);
        getTiempoEntreNodos(br, modelCase);
        br.close();
        return modelCase;
    }

    private int getCantNodes(BufferedReader br) throws IOException {
        String strLine;
        String text = "param cantNodos := ";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                String cantNodes = strLine.substring(text.length(),
                        strLine.indexOf(";")
                        );
                return Integer.parseInt(cantNodes);
            }
        }
        return -1;
    }
    
    private int getCantVehicles(BufferedReader br) throws IOException {
        String strLine;
        String text = "param cantVehiculos := ";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                String cantNodes = strLine.substring(text.length(),
                        strLine.indexOf(";")
                        );
                return Integer.parseInt(cantNodes);
            }
        }
        return -1;
    }
    
    private void getDemandaProductos(
            BufferedReader br,
            ModelCase modelCase
            ) throws IOException {
        String strLine;
        String text = "param nodo_demandaProductos :=";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                String strLine2;
                while((strLine2 = br.readLine())!=null) {
                    String[] keyValue = strLine2.trim().split("\t");
                    System.out.println(strLine2.trim());
                    int key = Integer.valueOf(keyValue[0]);
                    boolean isLastOne = keyValue[1].contains(";");
                    int value;
                    if(!isLastOne) {            
                        value = Integer.valueOf(keyValue[1]);
                    } else {
                        value = Integer.valueOf(keyValue[1].substring(0, Integer.valueOf(keyValue[1].length()-1)));
                    }
                    modelCase.addNode(
                            new Node(
                                    key,
                                    new Location(100d,100d)
                            )
                    );
                    modelCase.getNodesList().get(key).setProductDemand(value);
                    if(isLastOne) {
                        return;
                    }
                }
            }
        }
    }
    
    private void getDemandaResiduos(
            BufferedReader br,
            ModelCase modelCase
            ) throws IOException {
        String strLine;
        String text = "param nodo_demandaResiduos :=";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                String strLine2;
                while((strLine2 = br.readLine())!=null) {
                    String[] keyValue = strLine2.trim().split("\t");
                    System.out.println(strLine2.trim());
                    int key = Integer.valueOf(keyValue[0]);
                    boolean isLastOne = keyValue[1].contains(";");
                    int value;
                    if(!isLastOne) {            
                        value = Integer.valueOf(keyValue[1]);
                    } else {
                        value = Integer.valueOf(keyValue[1].substring(0, Integer.valueOf(keyValue[1].length()-1)));
                    }
                    modelCase.getNodesList().get(key).setResidualDemand(value);
                    if(isLastOne) {
                        return;
                    }
                }
            }
        }
    }

   private void getCapTotalProductos(
            BufferedReader br,
            ModelCase modelCase
            ) throws IOException {
        String strLine;
        String text = "param vehiculo_capacidadTotalProductos :=";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                String strLine2;
                while((strLine2 = br.readLine())!=null) {
                    String[] keyValue = strLine2.trim().split("\t");
                    System.out.println(strLine2.trim());
                    int key = Integer.valueOf(keyValue[0]);
                    boolean isLastOne = keyValue[1].contains(";");
                    int value;
                    if(!isLastOne) {            
                        value = Integer.valueOf(keyValue[1]);
                    } else {
                        value = Integer.valueOf(keyValue[1].substring(0, Integer.valueOf(keyValue[1].length()-1)));
                    }
                    modelCase.addVehicle(new Vehicle());
                    modelCase.getVehiclesList().get(key).setProductCapacity(value);
                    modelCase.getVehiclesList().get(key).setId(key);
                    if(isLastOne) {
                        return;
                    }
                }
            }
        }
    }
   
   private void getCapTotalResiduos(
            BufferedReader br,
            ModelCase modelCase
            ) throws IOException {
        String strLine;
        String text = "param vehiculo_capacidadTotalResiduos :=";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                String strLine2;
                while((strLine2 = br.readLine())!=null) {
                    String[] keyValue = strLine2.trim().split("\t");
                    System.out.println(strLine2.trim());
                    int key = Integer.valueOf(keyValue[0]);
                    boolean isLastOne = keyValue[1].contains(";");
                    int value;
                    if(!isLastOne) {            
                        value = Integer.valueOf(keyValue[1]);
                    } else {
                        value = Integer.valueOf(keyValue[1].substring(0, Integer.valueOf(keyValue[1].length()-1)));
                    }
                    modelCase.getVehiclesList().get(key).setTrashCapacity(value);
                    if(isLastOne) {
                        return;
                    }
                }
            }
        }
    }
   
   private void getTiempoLimInf(
            BufferedReader br,
            ModelCase modelCase
            ) throws IOException {
        String strLine;
        String text = "param nodo_tiempoLimiteInferior :=";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                String strLine2;
                while((strLine2 = br.readLine())!=null) {
                    String[] keyValue = strLine2.trim().split("\t");
                    System.out.println(strLine2.trim());
                    int key = Integer.valueOf(keyValue[0]);
                    boolean isLastOne = keyValue[1].contains(";");
                    int value;
                    if(!isLastOne) {            
                        value = Integer.valueOf(keyValue[1]);
                    } else {
                        value = Integer.valueOf(keyValue[1].substring(0, Integer.valueOf(keyValue[1].length()-1)));
                    }
                    TimeFrame timeFrame = new TimeFrame((long) value , 0);
                    modelCase.getNodesList()
                            .get(key).
                            setTimeFrame(timeFrame);
                    if(isLastOne) {
                        return;
                    }
                }
            }
        }
    }
   
   private void getTiempoLimSup(
            BufferedReader br,
            ModelCase modelCase
            ) throws IOException {
        String strLine;
        String text = "param nodo_tiempoLimiteSuperior :=";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                String strLine2;
                while((strLine2 = br.readLine())!=null) {
                    String[] keyValue = strLine2.trim().split("\t");
                    System.out.println(strLine2.trim());
                    int key = Integer.valueOf(keyValue[0]);
                    boolean isLastOne = keyValue[1].contains(";");
                    int value;
                    if(!isLastOne) {            
                        value = Integer.valueOf(keyValue[1]);
                    } else {
                        value = Integer.valueOf(keyValue[1].substring(0, Integer.valueOf(keyValue[1].length()-1)));
                    }
                    modelCase.getNodesList()
                            .get(key)
                            .getTimeFrame()
                            .setUpperLimit(value);
                    if(isLastOne) {
                        return;
                    }
                }
            }
        }
    }
   
   private void getTiempoEntreNodos(
            BufferedReader br,
                ModelCase modelCase
            ) throws IOException {
        double [][] data = new double[modelCase.getNodesNumber()+1][modelCase.getNodesNumber()+1];
        System.out.println("num nodes"+modelCase.getNodesNumber());
        String strLine;
        String text = "param nodo_tiempoEntreNodos :";
        while ((strLine = br.readLine()) != null)   {
            if (strLine.contains(text)) {
                br.readLine();br.readLine();
                for(int c = 0; c <= modelCase.getNodesNumber(); c++ ){
                    String strLine2;
                    strLine2 = br.readLine();
                    System.out.println(strLine2);
                    String[] line = strLine2.trim().split("\t");
                    System.out.println("size "+line.length);
                    System.out.println("*");
                    for(int r = 0; r <= modelCase.getNodesNumber(); r++) {
                        String num;
                        boolean isTheEnd = false;
                        System.out.println(line[r+1]);
                        if(line[r+1].contains(";")) {
                            num = line[r+1].substring(0, line[r+1].length()-1);
                            isTheEnd = true;
                        } else {
                            num = line[r+1];
                        }
                        System.out.println(""+c+" "+r+"="+num);
                        double value = Double.parseDouble(num);
                        data[c][r] =  value;
                        if(isTheEnd) {
                            modelCase.setTimeBetweenNodes(data);
                            return;
                        }
                    }
                    strLine2 = br.readLine();
                }
            }
        }
        modelCase.setTimeBetweenNodes(data);
    }
    
}
