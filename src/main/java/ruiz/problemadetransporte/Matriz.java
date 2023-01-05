/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruiz.problemadetransporte;

/**
 *
 * @author ruiz
 */
public class Matriz {
        public int [][] matriz;
        private int cols, rows;
        
    public Matriz(int l, int c) {
        this.rows = l;  
        this.cols = c;
        this.matriz = new int[rows][cols];
        this.preencheMenosUm();
    }
    
    private void preencheMenosUm(){
        for(int i= 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                this.matriz[i][j] = -1;
            }
        }
    }
    
    public int getCelula(int l, int c){        
        return this.matriz[l][c];                    
    }
    
    public void setCelula(int l, int c, int v){
        this.matriz[l][c] = v;
    }
    
    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
    
    public void imprimir(){
        for(int i= 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                System.out.print("\t" + this.matriz[i][j]);
            }
            System.out.println("");
        }
    }
}