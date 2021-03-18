/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import models.ModelCase;
import models.Node;
import models.Vehicle;
import utils.RandomUtils;
/**
 *
 * @author Asus
 */
public class CaseSolver {
    private ModelCase modelCase;
    
    private List<Node> pilaNodosRestantes; //lista de nodos restantaes por tomar
    private List<List<Node>> pilaNodosRestantesPerVehicle; //lista de los 3 primeras opciones de nodoso por tomar segun el carro
    
    private List<List<Node>> listaSolucionActual; //lista de solucion para todos los carros
    
    private List<List<List<Node>>> listaSolucionesElite; //lista de solucion para todos los carros
    
    private Node salida;
    private Node llegada;
    private List<Integer> listLimitesFactivilidadPerVehicle_productivity = new ArrayList<>();
    private List<Integer> listLimitesFactivilidadPerVehicle_trash = new ArrayList<>();
             
    public CaseSolver(ModelCase modelCase) {
        this.listaSolucionesElite = new ArrayList<>();
        this.modelCase = modelCase;
        /*
        System.out.println(""+modelCase.getNodesList().size());
        // imprimir nodos restantes
        pilaNodosRestantes.forEach((nodo) -> {
            System.out.println(""+nodo.getId()+" -> "+nodo.getTimeFrame().getLowerLimit()+" "+nodo.getTimeFrame().getUpperLimit());
        });
        //imprimir lista de vehiculos
        modelCase.getVehiclesList().forEach(
        (vehicle) -> {
            System.out.println("v"+vehicle.getId()+" -> "+vehicle.getProductCapacity()+" "+vehicle.getTrashCapacity());
        });
        */
        
    }
    
    public void runIteration() {
        pilaNodosRestantes = new ArrayList<>();
        modelCase.getNodesList().stream().forEach( node -> {
            pilaNodosRestantes.add(node);
        });
        
        modelCase.getVehiclesList().stream().forEach(vehicle -> {
            vehicle.setCurrentProductCapacityUsed(0);
            vehicle.setCurrentTrashCapacityUsed(0);
            vehicle.setCurrentTime(0);
        });
        
        System.out.println("preConstruccion");
        preConstruccion (modelCase);
        System.out.println("construccion");
        construccion (modelCase);
    }
    
    
    public void preConstruccion (ModelCase modelCase) {
        int numNodos = pilaNodosRestantes.size();
        int numVehicles = modelCase.getVehiclesList().size();
        
        salida = pilaNodosRestantes.remove(0);
        llegada = pilaNodosRestantes.remove(
                pilaNodosRestantes.size()-1
        );
        
        System.out.println("salida -> " + salida.getId()+" -> "+salida.getTimeFrame().getLowerLimit()+" "+salida.getTimeFrame().getUpperLimit());
        System.out.println("llegada -> " + llegada.getId()+" -> "+llegada.getTimeFrame().getLowerLimit()+" "+llegada.getTimeFrame().getUpperLimit());
        // limite de factivilidad
        modelCase.getVehiclesList().stream().map(it -> it.getProductCapacity()).forEach(
                it -> {
                    listLimitesFactivilidadPerVehicle_productivity.add(it);
                });
        modelCase.getVehiclesList().stream().map(it -> it.getTrashCapacity()).forEach(
                it -> {
                    listLimitesFactivilidadPerVehicle_trash.add(it);
                });
    }
    
    private void construccion (ModelCase modelCase) {
        pilaNodosRestantesPerVehicle = new ArrayList<>();
        listaSolucionActual = new ArrayList<>();
        for (int i=0; i< modelCase.getVehiclesList().size(); i++)  {
            pilaNodosRestantesPerVehicle.add(new ArrayList<>());
            listaSolucionActual.add(new ArrayList<>());
        }
        //System.out.println("agregando  el nodo de prtida a la solucion como base");
        listaSolucionActual.stream().forEach(it -> {
            //System.out.println("agregando  el nodo de prtida a la solucion como base");
            it.add(salida);
        });
        while (pilaNodosRestantes.size() > 0) {
            //elegir la lista reducida para cada vehicle
            pilaNodosRestantesPerVehicle = new ArrayList<>();
            for (int i=0; i< modelCase.getVehiclesList().size(); i++)  {
                pilaNodosRestantesPerVehicle.add(new ArrayList<>());
            }
        
            // restricciones
            pilaNodosRestantes.stream().forEach(
             nodo -> {
                 modelCase.getVehiclesList().forEach(vehicle -> {
                     
                     System.out.println("tiempo actual del vehiculo "+vehicle.getId()+": "+vehicle.getCurrentTime());
                     System.out.println("limite inferior del nodo "+nodo.getId()+": "+nodo.getTimeFrame().getLowerLimit());
                     System.out.println("limite supersior del nodo "+nodo.getId()+": "+nodo.getTimeFrame().getUpperLimit());
                     
                     List<Node> pilaNodosVehicle = listaSolucionActual.get((int)vehicle.getId());
                     Node lastNodePicked =  pilaNodosVehicle.isEmpty()? null : pilaNodosVehicle.get(pilaNodosVehicle.size()-1);
                     //System.out.println("ultimo nodo "+lastNodePicked.getId()+": "+lastNodePicked.getTimeFrame().getLowerLimit()+" "+lastNodePicked.getTimeFrame().getUpperLimit());
                     double timeBetweenNodes = modelCase.getTimeBetweenNodes()
                             [( lastNodePicked==null? salida: lastNodePicked).getId()]
                             [nodo.getId()];
                     
                     if( (vehicle.getProductCapacity() > vehicle.getCurrentProductCapacityUsed() + nodo.getProductDemand()) //aplicando restriccion de espacio
                             &&
                         (vehicle.getTrashCapacity() > vehicle.getCurrentTrashCapacityUsed()+ nodo.getResidualDemand()) //aplicando restriccion de espacio
                             &&
                         (vehicle.getCurrentTime() + timeBetweenNodes <= nodo.getTimeFrame().getUpperLimit() ) //restriccion de ventana de tiempo
                        ){ 
                         /*
                         System.out.println("time "+nodo.getTimeFrame().getLowerLimit()+"<"+vehicle.getCurrentTime()+"<"+nodo.getTimeFrame().getUpperLimit()+" = "+timeBetweenNodes);
                         System.out.println("se aplica: "+vehicle.getCurrentTime());
                         */
                         Node nodoCloned = new Node();
                         try {
                             nodoCloned = (Node) nodo.clone();
                         } catch (CloneNotSupportedException ex) {
                             Logger.getLogger(CaseSolver.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         if(vehicle.getCurrentTime() + timeBetweenNodes <= nodo.getTimeFrame().getLowerLimit()) {
                             nodoCloned.setTimeArrived((long) nodo.getTimeFrame().getLowerLimit());
                         } else {
                             nodoCloned.setTimeArrived((long)(vehicle.getCurrentTime() + timeBetweenNodes));
                         }
                         pilaNodosRestantesPerVehicle.get((int)vehicle.getId())
                                 .add(nodoCloned);
                     } 
                 });
             }
            );
            System.out.println("filtrando lista reducida");
            // fin de la parte de creacion de lista reducida
            //ahora seleccionamos los primeros dependiendo de los restantes
            
            for(int v=0; v< pilaNodosRestantesPerVehicle.size(); v++) {
                //System.out.println("******");
                Node ultimoNodoRecorridoDeVehiculo = listaSolucionActual.get(v).get(listaSolucionActual.get(v).size()-1);
                long currentTimeOfVehicle = modelCase.getVehiclesList().get(v).getCurrentTime();
                // modelCase.getTimeBetweenNodes()[(int)ultimoNodoRecorridoDeVehiculo.getId()] [(int)o.getId()]
                
                System.out.println("lista reducida de vehicle "+v+": sin filtro");
                pilaNodosRestantesPerVehicle.get(v).stream().forEach(
                 (node) -> {
                     System.out.println("ultimoNodoRecorridoDeVehiculo "+ultimoNodoRecorridoDeVehiculo.getId()+"\n..time"+modelCase.getTimeBetweenNodes()[(int)ultimoNodoRecorridoDeVehiculo.getId()] [(int)node.getId()]);
                     System.out.println("...proximo"+(node.getTimeFrame().getLowerLimit() - currentTimeOfVehicle));
                 }
                );
                
               pilaNodosRestantesPerVehicle.set(
                        v, 
                       sortListNodeByTime(ultimoNodoRecorridoDeVehiculo, pilaNodosRestantesPerVehicle.get(v))
                               .stream()
                               .limit(3)
                               .collect(Collectors.toList())
                );
                
                System.out.println("lista reducida de vehicle "+v+": primer filtro");
                pilaNodosRestantesPerVehicle.get(v).stream().forEach(
                 (node) -> {
                     System.out.println("ultimoNodoRecorridoDeVehiculo "+ultimoNodoRecorridoDeVehiculo.getId()+"\n..time"+modelCase.getTimeBetweenNodes()[(int)ultimoNodoRecorridoDeVehiculo.getId()] [(int)node.getId()]);
                     System.out.println("...proximo"+(node.getTimeFrame().getLowerLimit() - currentTimeOfVehicle));
                 }
                );
               
                //filtro para entregar lo mas antes posible
                pilaNodosRestantesPerVehicle.set(
                    v, 
                    pilaNodosRestantesPerVehicle.get(v).stream().sorted(
                        (a,b) -> (int)(b.getTimeFrame().getLowerLimit()*1000 -
                                a.getTimeFrame().getLowerLimit()*1000 )
                    ).limit(3).collect(Collectors.toList())
                ); //reducimos la lista a un maximo de 5
                
                System.out.println("lista reducida de vehicle "+v+": segundo filtro");
                pilaNodosRestantesPerVehicle.get(v).stream().forEach(
                 (node) -> {
                     System.out.println("ultimoNodoRecorridoDeVehiculo "+ultimoNodoRecorridoDeVehiculo.getId()+"\n..time"+modelCase.getTimeBetweenNodes()[(int)ultimoNodoRecorridoDeVehiculo.getId()] [(int)node.getId()]);
                     System.out.println("...proximo"+(node.getTimeFrame().getLowerLimit() - currentTimeOfVehicle));
                 }
                );
                System.out.println("******");
            }
            /*
            for(int i=0; i< pilaNodosRestantesPerVehicle.size(); i++) {
                 List<Node> listaReducidaPorVehiculo = pilaNodosRestantesPerVehicle.get(i);
                 Node ultimoNodoRecorridoDeVehiculo = listaSolucionActual.get(i).get(listaSolucionActual.get(i).size()-1);
                 System.out.println("******");
                listaReducidaPorVehiculo.stream().forEach(it -> {
                    System.out.println(""+ultimoNodoRecorridoDeVehiculo.getId()+"->"+it.getId()+": "+
                            modelCase.getTimeBetweenNodes()[(int)ultimoNodoRecorridoDeVehiculo.getId()] [(int)it.getId()]);
                });
                
                System.out.println("******");
            }
            */
            
            /*elegimos los mejores de cada lista al azar
              y los borramos de las otras listas reducidad de lso otros vehiculos
              y de la lista general de nodos restantes
            */
            System.out.println("total de nodos restantes: "+pilaNodosRestantes.size());
            System.out.println("cantidad de listas de reducidas de vehiculos: "+pilaNodosRestantesPerVehicle.size());
            for(int i=0; i<pilaNodosRestantesPerVehicle.size(); i++) {
                //elegimos uno al azar
                if(!pilaNodosRestantesPerVehicle.get(i).isEmpty()) {
                    int ramdonPos = (int) RandomUtils.nextLong(pilaNodosRestantesPerVehicle.get(i).size());
                    System.out.println("pos picked"+ramdonPos);
                    Node nodePicked = pilaNodosRestantesPerVehicle.get(i).get(ramdonPos);
                    pilaNodosRestantes.removeIf(t -> t.getId() == nodePicked.getId()); // removemos de la lista general
                    for(int j=i; j<pilaNodosRestantesPerVehicle.size(); j++) {
                        pilaNodosRestantesPerVehicle.get(j).removeIf(t -> t.getId() == nodePicked.getId());
                    } // removi todos de las otras listas reducidas el elemento que escogi
                    modelCase.getVehiclesList().get(i)
                            .addToCurrentProductCapacityUsed(nodePicked.getProductDemand());
                    modelCase.getVehiclesList().get(i)
                            .addToCurrentTrashCapacityUsed(nodePicked.getResidualDemand());
                    modelCase.getVehiclesList().get(i)
                            .setCurrentTime(nodePicked.getTimeArrived());
                    listaSolucionActual.get(i).add(nodePicked);
                    //System.out.println("nodo elegido para vehiculo"+i+": "+nodePicked.getId()+" -> "+nodePicked.getTimeFrame().getLowerLimit()+" "+nodePicked.getTimeFrame().getUpperLimit());
                } else {
                    //System.out.println("lista vacia para vehiculo"+i);
                }
            }
            System.out.println("fin de iteracion");
            printCurrentSolution();
        }
        for (int v=0; v<listaSolucionActual.size(); v++) {
            List<Node> listaVehiculo = listaSolucionActual.get(v);
            Vehicle vehicle = modelCase.getVehiclesList().get(v);
            //System.out.println("agregando  el nodo de llegada a la solucion");
            List<Node> pilaNodosVehicle = listaSolucionActual.get(v);
            Node lastNodePicked =  pilaNodosVehicle.isEmpty()? null : pilaNodosVehicle.get(pilaNodosVehicle.size()-1);
            //System.out.println("ultimo nodo "+lastNodePicked.getId()+": "+lastNodePicked.getTimeFrame().getLowerLimit()+" "+lastNodePicked.getTimeFrame().getUpperLimit());
            double timeBetweenNodes = modelCase.getTimeBetweenNodes()
                    [( lastNodePicked==null? salida: lastNodePicked).getId()]
                    [llegada.getId()];
            Node nodoCloned = new Node();
            
            try {
                nodoCloned = (Node) llegada.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CaseSolver.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(vehicle.getCurrentTime() + timeBetweenNodes <= llegada.getTimeFrame().getLowerLimit()) {
                nodoCloned.setTimeArrived((long) llegada.getTimeFrame().getLowerLimit());
            } else {
                nodoCloned.setTimeArrived((long)(vehicle.getCurrentTime() + timeBetweenNodes));
            }
            listaVehiculo.add(nodoCloned);
        }
      
        printSolutionAndSave();
    }
    
    public List<Node> sortListNodeByTime(Node ultimoNodoRecorridoDeVehiculo, List<Node> pilaNodosRestantes) {
        for (int a=0; a<pilaNodosRestantes.size(); a++) {
            Node A = pilaNodosRestantes.get(a);
            double at = modelCase.getTimeBetweenNodes()[(int)ultimoNodoRecorridoDeVehiculo.getId()] [(int)A.getId()];
            for (int b=1; b<pilaNodosRestantes.size()-1; b++) {
                Node B = pilaNodosRestantes.get(b);
                double bt = modelCase.getTimeBetweenNodes()[(int)ultimoNodoRecorridoDeVehiculo.getId()] [(int)B.getId()];
                if(at>bt) {
                    try {
                        Node temp = (Node) A.clone();
                        A = (Node) B.clone();
                        B = (Node) temp.clone();
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(CaseSolver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return pilaNodosRestantes;
    }
    
    public void printCurrentSolution() {
        System.out.println("============");
        System.out.println("************");
        System.out.println("numero de listas: "+listaSolucionActual.size());
        for(int v=0; v<listaSolucionActual.size(); v++) {
            List<Node> nodesPerVehicle = listaSolucionActual.get(v);
            System.out.println();
            System.out.println("v:"+v+": "+nodesPerVehicle.get(nodesPerVehicle.size()-1).getTimeArrived());
            for (int i=0; i<nodesPerVehicle.size(); i++) {
                System.out.printf("n"+nodesPerVehicle.get(i).getId()+"("+nodesPerVehicle.get(i).getTimeArrived()+") ->");
            }
        }
        
        System.out.println();
        System.out.println("************");
        System.out.println("============");
    }

    private void printSolutionAndSave() {
        printCurrentSolution();
        try{
            listaSolucionesElite.add(listaSolucionActual);
        }catch(Exception e) {
            System.out.println("error: "+e.toString());
        }
    }


}



