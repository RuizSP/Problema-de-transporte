/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruiz.problemadetransporte;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ruiz
 */
public class solucionaTransporte {
        ArrayList<Integer> Oferta = new ArrayList();
        ArrayList<Integer> Demanda = new ArrayList();
        Matriz Peso;
        Matriz Valor; 
        Matriz CustoNorm;
   public  void iniciaMatrizes(int rows, int cols){
        this.Peso = new Matriz(rows, cols);
        this.Valor = new Matriz(rows, cols);
         CustoNorm= new Matriz(rows,cols);
    }
    public void preencheDados( String nomeArquivo) throws FileNotFoundException{
                FileReader file = new FileReader(nomeArquivo);
                Scanner arquivo = new Scanner(file);   
                arquivo.useDelimiter("\n");
                String dimensoesMatrizes = arquivo.next();
                String[] vet = dimensoesMatrizes.split(";");
                int rows = Integer.parseInt(vet[0]);
                int cols = Integer.parseInt(vet[1]);
                this.iniciaMatrizes(rows, cols);
                String valoresDemanda = arquivo.next();
                String[] vetDemanda = valoresDemanda.split(";");
                String valoresOferta = arquivo.next();
                String[] vetOferta = valoresOferta.split(";");
                for(int i = 0; i< vetDemanda.length;i++){
                    Demanda.add(Integer.valueOf(vetDemanda[i]));
                }
                for(int i = 0; i< vetOferta.length;i++){
                    Oferta.add(Integer.valueOf(vetOferta[i]));
                }
                
                String linhaCorrente;
                int linha, coluna;
                int i =0;
                while(arquivo.hasNext()){
                    linhaCorrente = arquivo.next();
                    vet = linhaCorrente.split(";");
                    for (int j =0; j<cols;j++){
                            Peso.setCelula(i, j, Integer.parseInt(vet[j]));
                        }
                    i++;
                }
                cantoNoroeste();
    }
    
    public void cantoNoroeste(){
        for (int r = 0, northwest = 0; r < Oferta.size(); r++) {
                for (int c = northwest; c < Demanda.size(); c++) {
                    int quantity;
                    quantity = Math.min(Oferta.get(r), Demanda.get(c));
                    if (quantity > 0) {
                        Valor.setCelula(r, c, quantity);
                        Oferta.set(r, Oferta.get(r)-quantity);
                        Demanda.set(c, Demanda.get(c)-quantity);
                        if (Oferta.get(r) == 0) {
                            northwest = c;
                            break;
                        }
                    }
                }
            }
            Valor.imprimir();

  
    }
    public void testeOtimo(){
        
    }
    
    
}
