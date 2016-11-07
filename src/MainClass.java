
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author April Dae Bation
 */
public class MainClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("A. Deadlock Prevention\nB. Deadlock Avoidance\nChoice: ");
        String ch = sc.nextLine();
        
        if(ch.equals("A")){
            System.out.println("\nDEADLOCK PREVENTION");
            Deadlock_Prevention d = new Deadlock_Prevention();
            d.execute();
        }
        
        else if(ch.equals("B")){
            System.out.println("\nDEADLOCK AVOIDANCE");
            Deadlock_Avoidance d = new Deadlock_Avoidance();
            d.execute();
        }
        
    }
}
