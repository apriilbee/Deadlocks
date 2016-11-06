
import Jama.Matrix;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
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
public class Deadlocks {
   
    static Matrix allocation;
    static Matrix maximum;
    static Matrix needed;
    static Matrix available;
    
    static Scanner sc = new Scanner(System.in);
    static int processes;
    static int resources;
        
    public static void main(String[] args) {
        System.out.print("Enter number of processes: ");
        processes = sc.nextInt();
        System.out.print("Enter number of resources: ");
        resources = sc.nextInt();
        System.out.println("----------\n");
        
        initMatrices(processes, resources);
        
       
        do{
            System.out.print("Press 1 for new request details: ");
            int choice = sc.nextInt();
            if(choice!=1)
                break;
            else{
                System.out.print("Enter Process Id: ");
                int pid = sc.nextInt() - 1;
                Matrix res = new Matrix(1, resources);
                for(int i=0; i<resources; i++){
                    System.out.print("Request for Resource " + (i+1) + ": ");
                    int tmp = sc.nextInt();
                    res.set(0, i, tmp);
                }
                if(processRequest(pid, res)){
                    showMatrix(allocation, "Allocation Matrix");
                    showMatrix(maximum, "Maximum Needed Resources Matrix");
                    showMatrix(available, "Available Resources");
                    showMatrix(needed, "Needed Resources");

                    if (calculateSafeSequence())
                        System.out.println("\nP" + (pid+1) + " is successfully granted! \n");
                    else 
                        System.out.println("P" + (pid+1) + " request denied.");
                }
                else{
                    System.out.println("P" + (pid+1) + " request denied.");
                }
            }
            
        } while(true);
        
        
    }
    
    private static void initMatrices(int processes, int resources) {
        allocation = new Matrix(processes, resources, 0);
        maximum = new Matrix(processes, resources, 0);
        needed = new Matrix(processes, resources, 0);
        available = new Matrix(1, resources, 0);
         
        for(int i=0; i<processes; i++){
            System.out.println("PROCESS " + (i+1) + " " );
            for(int j=0; j<resources; j++){
                System.out.println("Resource " + (j+1) + "");
                
                System.out.print("Allocation: ");
                int all = sc.nextInt();
                allocation.set(i, j, all);
                
                System.out.print("Maximum Needed: ");
                int max = sc.nextInt();
                maximum.set(i, j, max);
            }
            System.out.println("");
        }
        
        System.out.println("Enter available resources for");
        for(int i=0; i<resources; i++){
            System.out.print("Process " + (i+1) + ": " );
            int tmp = sc.nextInt();
            available.set(0, i, tmp);
        }
        System.out.println("");
        needed = maximum.minus(allocation);
       
    }
    
    
    private static void showMatrix(Matrix m, String title){
        System.out.println(title);
        m.print(2, 2);
        System.out.println("");
    }

    //wa pa ko ga-handle ug unsafe state sa pagcalculate
    private static boolean calculateSafeSequence() {
        boolean safe = true;
        ArrayList<Integer> safeSequence = new ArrayList();
        
        Queue p= new LinkedList();
        for(int i=0; i<processes; i++){
            p.add(i);
        }
        
        while(!p.isEmpty()){
            int pid = (int) p.remove();
            boolean allow = checkSufficientResource(pid);
            if(allow==false){
                p.add(pid);
            }
            else{
                updateAvailableResources(pid);
                safeSequence.add(pid);
            }
        }
        if(safe==true){
            System.out.println("State: Safe");
            System.out.println("Safe Sequence: ");
            for(int i=0; i<safeSequence.size(); i++){
                System.out.print("P" + (safeSequence.get(i)+1) + " ");
            }
            return true;
        }
        return false;
    }

    private static boolean checkSufficientResource(int pid) {
        for(int i=0; i<resources; i++){
            int tmp_needed = (int) needed.get(pid, i);
            int res = (int) available.get(0, i);
            if(tmp_needed > res)
                return false;
        }
            
        return true;
    }

    private static void updateAvailableResources(int pid) {
        Matrix p_alloc = new Matrix(1,resources);
        for(int i=0; i<resources; i++){
           p_alloc.set(0, i,allocation.get(pid, i));
        }
        available.plusEquals(p_alloc);
        //showMatrix(available, "Available");
    }

    private static boolean processRequest(int pid, Matrix res) {
        available.minusEquals(res);
        for(int i=0; i<available.getColumnDimension(); i++){
            if(available.get(0, i) < 0){
                available.plusEquals(res);
                return false;
            }
        }
        for(int i=0; i<resources; i++){
            double tmp = allocation.get(pid, i);
            double r = res.get(0, i);
            allocation.set(pid, i, tmp+r);
        }
        return true;
    }
}
