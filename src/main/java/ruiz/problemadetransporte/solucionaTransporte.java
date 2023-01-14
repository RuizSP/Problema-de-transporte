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
        ArrayList <Integer>registraDemanda = new ArrayList();
        ArrayList <Integer>registraOferta = new ArrayList();
        Matriz Peso;
        Matriz Valor;
       
   public  void iniciaMatrizes(int rows, int cols){
        this.Peso = new Matriz(rows, cols);
        this.Valor = new Matriz(rows, cols);
        
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
                registraDemanda.add(Integer.valueOf(vetDemanda[i]));
            }
            for(int i = 0; i< vetOferta.length;i++){
                Oferta.add(Integer.valueOf(vetOferta[i]));
                registraOferta.add(Integer.valueOf(vetOferta[i]));
            }
            
            
            balanceamento();
            preencheDados(arquivo, col);
            cantoNoroeste();
            int z= calculaZ(Valor);
           
            System.out.println("O valor de Z: " + z);
            System.out.println("--------------------------\n");
            
            
            SteppingStone stone = new SteppingStone(Valor, Peso, registraOferta, registraDemanda);
            stone.converteMatriz();
            stone.steppingStone();
            System.out.println("--------------Resultado final-----------------------");
            Matriz Resultado = stone.printResult();
            z= calculaZ(Resultado);
            Resultado.imprimir();
            System.out.println("Solução otima= " + z);
            
       
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
        for (int r = 0, cNoroeste = 0; r < Oferta.size(); r++) {
                for (int c = cNoroeste; c < Demanda.size(); c++) {
                    int quantidade;
                    quantidade = Math.min(Oferta.get(r), Demanda.get(c));
                    if (quantidade > 0) {
                        Valor.setCelula(r, c, quantidade);
                        Oferta.set(r, Oferta.get(r)-quantidade);
                        Demanda.set(c, Demanda.get(c)-quantidade);
                        if (Oferta.get(r) == 0) {
                            cNoroeste = c;
                            break;
                        }
                    }
                }
            }
            System.out.println("-------Solução inicial: Canto Noroeste-----");
            Valor.imprimir();
            corrigirDegeneracao();
  
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
                registraDemanda.add(somaOferta-somaDemanda);
                iniciaMatrizes(rowInicial, colInicial);
                for(int l = 0;l<rowInicial;l++){
                    Peso.setCelula(l, colInicial-1, 0);
                }
                
            }if(somaDemanda>somaOferta){
                rowInicial = rowInicial+1;
                Oferta.add(somaDemanda-somaOferta);
                registraOferta.add(somaDemanda-somaOferta);
                iniciaMatrizes(rowInicial, colInicial);
                for(int c = 0;c<colInicial;c++){
                    Peso.setCelula(rowInicial-1, c, 0);
                }
                
            }
           
        }
    }
    
    
    public int calculaZ(Matriz resultante){
        int soma =0, multPeso=0;
        for(int i =0; i<resultante.getRows();i++){
            for(int j = 0; j< resultante.getCols();j++){
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
        Origem.clear();
        Destino.clear();
        
    }
    /*public void testeOtimalidade(){
        IOPTIMAL=0;
        int contando = 1;
        while(IOPTIMAL == 0){
            Matriz MatrizBasica = new Matriz(rowInicial, colInicial);
            corrigirDegeneracao();
            for(int i = 0;i<rowInicial;i++){
                for(int j = 0;j<colInicial;i++){
                    if(custoAux.getCelula(i, j) != -1){
                        continue;
                    }else{
                        procuraCiclo(i,j);
                        aumenta(i,j);
                        
                    }
                }
            }
            if(XINFCC >=0){
                IOPTIMAL = 1;
            }else{
                IX = p.getCelula(3, 1);
                IY = p.getCelula(4, 1);
                for(int i =0;i< LT;i++){
                    IX = p.getCelula(3, i);
                    IY = p.getCelula(4, i);
                    if(i%2 == 0){
                        int trocaValor = custoAux.getCelula(IX, IY);
                        MatrizBasica.setCelula(IX, IY, (custoAux.getCelula(IX, IY)-TT));
                        custoAux.setCelula(IX,IY, (custoAux.getCelula(IX, IY)-TT));
                        if(trocaValor == TT){
                            custoAux.setCelula(IX, IY, -1);
                        }
                    }else{
                        if(custoAux.getCelula(IX, IY) == -1){
                            MatrizBasica.setCelula(IX, IY, TT);
                        }else{
                            MatrizBasica.setCelula(IX, IY, custoAux.getCelula(IX, IY)+TT);
                        }
                        custoAux.setCelula(IX,IY,custoAux.getCelula(IX, IY)+TT);
                    }
                }
            }
            contando ++;   
        }
    }
    

    
    public void procuraCiclo(int row, int cow){
      
       
        for(int i =0; i< rowInicial; i++){
            for(int j = 0;j< colInicial;i++){
                r.setCelula(i, j, custoAux.getCelula(i, j));
                if(custoAux.getCelula(i, j) == -1 || custoAux.getCelula(i, j) == 0 ){
                    r.setCelula(i, j, 0);
                }
            }
        }
        for (int i =0; i<rowInicial; i++){
           r.setCelula(i, 0, 0);
        }        
        for (int i =0; i<colInicial; i++){
           r.setCelula(0, i, 0);
        }
        r.setCelula(row, cow, 1);
        IF1 = 1;
        while(IF1 ==1){
            IF1 = 0;
            for(int i=0;i< colInicial;i++){
                if(r.getCelula(0, i) != 1){
                    NR =0;
                    for(int j =0; j< rowInicial;j++){
                        if(r.getCelula(j, i) != -1){
                            NR ++;
                        }
                    }
                    if(NR==1){
                        for(int j=0;j<rowInicial;j++){
                            r.setCelula(j, i, 0);
                        }
                        r.setCelula(0, i,1);
                        IF1 = 1;
                    }
                }
            }
            for(int i =0; i<rowInicial;i++){
                if(r.getCelula(1, 0) != 1){
                    NR =0;
                    for(int j =0; j<colInicial;j++){
                        if(r.getCelula(i,j) != -1){
                            NR++;
                        }
                    }
                    if(NR==1){
                        for(int j =0;j<colInicial;j++){
                            r.setCelula(i, j, 0);
                        }
                        r.setCelula(i,0,1);
                        IF1=1;
                    }
                }
            }
        }
        
    }
    
    public void aumenta(int row, int col){
        p.setCelula(1,1, row);  
        p.setCelula(2,1,col);  
        IX=row;   IY=col;   ID=1;   CC=0;     QT=999999;  
        int variavelPraNada = 0;
        int auxi = -1;
        int auxj  = -1;
        ArrayList<Integer>VetContas = new ArrayList();
        while(auxi != row || auxj != col){
            ID ++;
            IF1 =0;
            for(int i =0;i< rowInicial;i++){
                if(r.getCelula(i, IY)==-1||i==IX){
                    continue;
                }else{
                    p.setCelula(1, ID, i);
                    p.setCelula(2, ID, IY);
                    IX = i;
                    CC -= Peso.getCelula(IX, IY);
                    VetContas.add(Peso.getCelula(IX, IY));
                    IF1 =1;
                    i = rowInicial;
                    if(custoAux.getCelula(IX, IY) < QT && custoAux.getCelula(IX, IY) != -1 ){
                        QT = custoAux.getCelula(IX, IY);
                    }
                }
            }
            if(IF1==0){
                return;
            }
            ID++;
            IF1 =0 ;
            for(int j =0; j< colInicial;j++){
                if(r.getCelula(IX, j)==-1||j == IY){
                   continue;
                }
                else{
                    p.setCelula(1,ID,IX);
                    p.setCelula(2, ID, j);
                    IY = j;
                    CC += Peso.getCelula(IX, IY);
                    VetContas.add(Peso.getCelula(IX, IY));
                    IF1 =1;
                    j = colInicial;
                    
                }
            }
            if (IF1 == 0){
                return;
            }
            auxi =IX;
            auxj = IY;
               
        }
           if (!(CC >= 0 || CC > XINFCC)){
               TT = QT;
               XINFCC = CC;
               ID--;
               LT = ID;
               for(int i =0; i< ID;i++){
                   p.setCelula(3, i, p.getCelula(1, i));
                   p.setCelula(4, i, p.getCelula(2, i));
               }
           }
    
    
    
    }*/
    
    
    
}





/*Calcular valor de z */
// função que verifica linha vazia;
//encontrei marca qual a celula, 
//