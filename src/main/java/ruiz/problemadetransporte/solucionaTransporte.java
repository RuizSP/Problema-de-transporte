/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruiz.problemadetransporte;
/*Fontes: https://rosettacode.org/wiki/Transportation_problem#Java
          https://drive.google.com/file/d/1eaEVtChe0vleXB_VQRAzwKk9xgWtSlvo/view (código da  Priscyla )





*/




import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author ruiz
 */
public class solucionaTransporte {
        int colInicial;
        int rowInicial;
        ArrayList<Integer> Oferta = new ArrayList();
        ArrayList<Integer> Demanda = new ArrayList();
        Matriz Peso;
        Matriz Valor;
        Matriz Voguel;
        Matriz CustoMin;
   public  void iniciaMatrizes(int rows, int cols){
        this.Peso = new Matriz(rows, cols);
        this.Valor = new Matriz(rows, cols);
        this.Voguel = new Matriz(rows, cols);
        this.CustoMin= new Matriz(rows,cols);
    }
   
   public void preencheDemandaOferta(String nomeArquivo) throws FileNotFoundException{
            FileReader file = new FileReader(nomeArquivo);
            Scanner arquivo = new Scanner(file);   
            arquivo.useDelimiter("\n");

            String dimensoesMatrizes = arquivo.next();
            String[] vet = dimensoesMatrizes.split(";");
            rowInicial = Integer.parseInt(vet[0]);
            colInicial = Integer.parseInt(vet[1]);
            int col = colInicial;
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
            balanceamento();
            preencheDados(arquivo, col);
            cantoNoroeste();
            int z = calculaZ(Valor);
            System.out.println("O valor de Z: " + z);
            
       
   }
   
   
   
public void preencheDados( Scanner Arquivo, int col) throws FileNotFoundException{
        
                Arquivo.useDelimiter("\n");
      
                String[] vet;
                String linhaCorrente;
                //int linha, coluna;
                int i =0;
                while(Arquivo.hasNext()){
                    linhaCorrente = Arquivo.next();
                    vet = linhaCorrente.split(";");
                    for (int j =0; j<col;j++){
                            Peso.setCelula(i, j, Integer.parseInt(vet[j]));
                        }
                    i++;
                }
                System.out.println("------- Pesos-----");
                Peso.imprimir();
                System.out.println("-------------------");
                
    }
    
    public void cantoNoroeste(){//comparar se oferta esta igual a demanda
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
            System.out.println("-------Solução inicial: Canto Noroeste-----");
            Valor.imprimir();
            corrigirDegeneracao();
            System.out.println("------Degeneração Corrigida--------");
            Valor.imprimir();
            /*boolean degenerado = isDegenerado();
            if(degenerado == true){
                System.out.println("Sim");
            }else{
                System.out.println("Não");
            }*/

  
    }
    public void custoMinimo(){
        
    }
    
    public void aproxVoguel(){
        
    }
    
    public void balanceamento(){
        int somaOferta = Oferta.stream().mapToInt(Integer::intValue).sum();
        int somaDemanda = Demanda.stream().mapToInt(Integer::intValue).sum();
        
        
        if (somaOferta == somaDemanda){
            iniciaMatrizes(rowInicial, colInicial);
        }else{
            if(somaOferta > somaDemanda){
                colInicial = colInicial+1;
                Demanda.add(somaOferta-somaDemanda);
                iniciaMatrizes(rowInicial, colInicial);
                for(int l = 0;l<rowInicial;l++){
                    Peso.setCelula(l, colInicial-1, 0);
                }
                
            }if(somaDemanda>somaOferta){
                rowInicial = rowInicial+1;
                Oferta.add(somaDemanda-somaOferta);
                iniciaMatrizes(rowInicial, colInicial);
                for(int c = 0;c<colInicial;c++){
                    Peso.setCelula(rowInicial-1, c, 0);
                }
                
            }
           
        }
    }
    public int calculaZ(Matriz resultante){
        int soma =0, multPeso=0;
        for(int i =0; i<rowInicial;i++){
            for(int j = 0; j< colInicial;j++){
                if(resultante.getCelula(i, j) != -1){
                    multPeso = resultante.getCelula(i, j)*Peso.getCelula(i, j);
                    soma += multPeso;
                }
            }
        }
        return soma;
    }
    
    /*public boolean isDegenerado(){
       int variaveisBasicas =0;
       for(int i =0; i<rowInicial;i++){
           for(int j=0;j<colInicial;j++){
               if(Valor.getCelula(i,j)!=-1){
                   variaveisBasicas +=1;
               }
           }
       }
        return variaveisBasicas< (rowInicial + colInicial-1);
    }*/
    
    public void corrigirDegeneracao(){
        //int minval = Integer.MIN_VALUE;
        ArrayList<Integer> Origem = new ArrayList();
        ArrayList<Integer> Destino = new ArrayList();

        
        int k =1;
        int l =0;
        int c = 0;
        int variaveisBasicas =0;
        for(int i =0; i<rowInicial;i++){
            for(int j=0;j<colInicial;j++){
                if(Valor.getCelula(i,j)!=-1){
                    variaveisBasicas +=1;
                    Origem.add(i);
                    Destino.add(j);
  
                    
                }
            }
        }
        
        int base = rowInicial+colInicial-1;
        int varDegenerada = base - variaveisBasicas;
        if (base != variaveisBasicas){
            for(int i =0; i<variaveisBasicas;i++){
                if (varDegenerada!= 0){
                    l = Origem.get(i);
                    c = Destino.get(i+1);
                    if(Valor.getCelula(l,c) == -1  && !Objects.equals(Origem.get(i), Origem.get(i+1))){
                        Valor.setCelula(l, c,0 );
                        varDegenerada -= 1;
                    }
                }else{
                    break;
                }
            }
        }
        
    }
    
    public void steppingStone(){
        
    }
}





/*Calcular valor de z */
// função que verifica linha vazia;
//encontrei marca qual a celula, 
//