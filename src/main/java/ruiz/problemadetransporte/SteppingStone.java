/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruiz.problemadetransporte;

import java.util.ArrayList;
import java.util.LinkedList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;

/**
 *
 * @author ruiz
 */


public class SteppingStone {
    Shipment [][] matrix;
    Matriz valor;
    Matriz peso;
    ArrayList<Integer> supply;
    ArrayList<Integer> demand;
    
    public SteppingStone(Matriz valor, Matriz peso, ArrayList oferta, ArrayList demanda){
        this.valor = valor;
        this.peso = peso;
        this.supply = oferta;
        this.demand = demanda;  
    }
    
   public void converteMatriz(){
        matrix = new Shipment[valor.getRows()][valor.getCols()];
        for(int i = 0; i< valor.getRows();i++){
            for(int j =  0; j< valor.getCols();j++){
                if(valor.getCelula(i, j) != -1){
                    Shipment t = new Shipment(valor.getCelula(i, j),peso.getCelula(i, j), i, j);
                    matrix[i][j] = t;
                }else{
                    matrix[i][j] = null;
                } 
                              // System.out.println(valor.getCelula(i,j));
            }
        }
        
    }
   
     public void steppingStone() {
        double maxReduction = 0;
        Shipment[] move = null;
        Shipment leaving = null;
        
        fixDegenerateCase();

        for (int r = 0; r < supply.size(); r++) {
            for (int c = 0; c < demand.size(); c++) {

                if (matrix[r][c] != null)
                    continue;

                Shipment trial = new Shipment(0, peso.getCelula(r, c), r, c);
                Shipment[] path = getClosedPath(trial);

                double reduction = 0;
                double lowestQuantity = Integer.MAX_VALUE;
                Shipment leavingCandidate = null;

                boolean plus = true;
                for (Shipment s : path) {
                    if (plus) {
                        reduction += s.costPerUnit;
                    } else {
                        reduction -= s.costPerUnit;
                        if (s.quantity < lowestQuantity) {
                            leavingCandidate = s;
                            lowestQuantity = s.quantity;
                        }
                    }
                    plus = !plus;
                }
                if (reduction < maxReduction) {
                    move = path;
                    leaving = leavingCandidate;
                    maxReduction = reduction;
                }
            }
        }

        if (move != null) {
            double q = leaving.quantity;
            boolean plus = true;
            for (Shipment s : move) {
                s.quantity += plus ? q : -q;
                matrix[s.r][s.c] = s.quantity == 0 ? null : s;
                plus = !plus;
            }
            steppingStone();
        }
    }

   public LinkedList<Shipment> matrixToList() {
        return stream(matrix)
                .flatMap(row -> stream(row))
                .filter(s -> s != null)
                .collect(toCollection(LinkedList::new));
    }

    public Shipment[] getClosedPath(Shipment s) {
        LinkedList<Shipment> path = matrixToList();
        path.addFirst(s);

        // remove (and keep removing) elements that do not have a
        // vertical AND horizontal neighbor
        while (path.removeIf(e -> {
            Shipment[] nbrs = getNeighbors(e, path);
            return nbrs[0] == null || nbrs[1] == null;
        }));

        // place the remaining elements in the correct plus-minus order
        Shipment[] stones = path.toArray(new Shipment[path.size()]);
        Shipment prev = s;
        for (int i = 0; i < stones.length; i++) {
            stones[i] = prev;
            prev = getNeighbors(prev, path)[i % 2];
        }
        return stones;
    }

     public Shipment[] getNeighbors(Shipment s, LinkedList<Shipment> lst) {
        Shipment[] nbrs = new Shipment[2];
        for (Shipment o : lst) {
            if (o != s) {
                if (o.r == s.r && nbrs[0] == null)
                    nbrs[0] = o;
                else if (o.c == s.c && nbrs[1] == null)
                    nbrs[1] = o;
                if (nbrs[0] != null && nbrs[1] != null)
                    break;
            }
        }
        return nbrs;
    }
     
    public void fixDegenerateCase() {
        final double eps = Double.MIN_VALUE;

        if (supply.size() + demand.size() - 1 != matrixToList().size()) {

            for (int r = 0; r < supply.size(); r++)
                for (int c = 0; c < demand.size(); c++) {
                    if (matrix[r][c] == null) {
                        Shipment dummy = new Shipment(eps, peso.getCelula(r, c), r, c);
                        if (getClosedPath(dummy).length == 0) {
                            matrix[r][c] = dummy;
                            return;
                        }
                    }
                }
        }
    }

     public Matriz printResult() {
        Matriz resultado = new Matriz(supply.size(), demand.size());
        for(int i =0; i< supply.size();i++){
            for(int j =0; j< demand.size();j++){
                if(matrix[i][j] != null){
                    resultado.setCelula(i, j,(int) matrix[i][j].quantity);
                }else{
                    resultado.setCelula(i, j, -1);
                }
            }
        } 
         
         return resultado;
    }

   
   

    
}
