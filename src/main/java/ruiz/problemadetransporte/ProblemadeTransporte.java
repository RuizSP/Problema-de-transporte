/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package ruiz.problemadetransporte;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruiz
 */
public class ProblemadeTransporte {
   
    
    public static void main(String[] args) {
        solucionaTransporte Sol = new solucionaTransporte();
        try {
            Sol.preencheDemandaOferta("problema_38.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProblemadeTransporte.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    

    
    
    
    
}
