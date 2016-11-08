
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
public class Deadlock {
   
    static Matrix allocation;
    static Matrix maximum;
    static Matrix needed;
    static Matrix available;
    
    static Scanner sc = new Scanner(System.in);
    static int processes;
    static int resources;
        
    public static void main(String[] args) {
        System.out.print("\nA.Deadlock Prevention\nB.Deadlock Avoidance\nChoose: ");
        String ch = sc.nextLine();
            
        if(ch.equals("A")){
            deadlockPrevention();
        }
        else if(ch.equals("B")){
            System.out.print("Enter number of processes: ");
            processes = sc.nextInt();
            System.out.print("Enter number of resources: ");
            resources = sc.nextInt();
            System.out.println("----------\n");
            initMatrices(processes, resources);
            deadlockAvoidance();
        }
       
    }
    
    static Matrix originalAvailable;
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
        originalAvailable = new Matrix(available.getArrayCopy());
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
        
        //check deadlock state here. if kapila na balik sa queue pero wala na add
        //sa safe state, deadlock na
        
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
            if(isDeadlock()){
                System.out.println("State: Deadlock");
                safe=false;
                break;
            }
        }
        
        if(safe==true){
            System.out.println("State: Safe");
            System.out.println("Safe Sequence: ");
            for(int i=0; i<safeSequence.size(); i++){
                System.out.print("P" + (safeSequence.get(i)+1) + " ");
            }
            available = new Matrix(originalAvailable.getArray());
            return true;
        }
        return false;
    }
    
    private static boolean isDeadlock() {
        boolean flag = false;
        for(int i=0; i<processes; i++){
            if(checkSufficientResource(i)){
                flag = true;
            }
        }
        if(flag==true)
            return false;
        
        return true;
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
        available = new Matrix(originalAvailable.getArrayCopy());
        
        for(int i=0; i<available.getColumnDimension(); i++){
            if(available.get(0, i) < res.get(0, i)){
                System.out.println("Insufficient resource");
                return false;
            }
        }
        
        available.minusEquals(res);
        originalAvailable = new Matrix(available.getArrayCopy());
        
        
        for(int i=0; i<resources; i++){
            double tmp = allocation.get(pid, i);
            double r = res.get(0, i);
            allocation.set(pid, i, tmp+r);
        }
        for(int i=0; i<resources; i++){
            double tmp = needed.get(pid, i);
            double r = res.get(0, i);
            needed.set(pid, i, tmp-r);
        }
       
        return true;
    }
    
    private static void deadlockAvoidance() {
        showMatrix(allocation, "Allocation Matrix");
        showMatrix(maximum, "Maximum Needed Resources Matrix");
        showMatrix(available, "Available Resources");
        showMatrix(needed, "Needed Resources");
        calculateSafeSequence();
        System.out.println("");
       
        do{
            System.out.print("\nPress 1 for new request details: ");
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
    
    private static void deadlockPrevention() {
        ArrayList<Process> processes = new ArrayList();
        System.out.print("Enter number of processes: ");
        int num = sc.nextInt();
        for(int i=0; i<num; i++){
            System.out.print("Name of process " + (i+1) + ": " );
            String name = sc.next();
            System.out.print("Time for process " + name + ": ");
            int time = sc.nextInt();
            Process tmp = new Process(i, name, time);
            processes.add(tmp);
            System.out.println("");
        }
        System.out.println("Enter value of available resources: ");
        int avail = sc.nextInt();
        calculatePrev(processes, avail);
    }
    
    private static void calculatePrev(ArrayList<Process> processes, int avail) {
        ArrayList<Process> safeSeq = new ArrayList();
        boolean flag = false;
        Queue<Process> q = new LinkedList();
        for(int i=0; i<processes.size(); i++){
            q.add(processes.get(i));
        }
        
        while(!q.isEmpty()){
            Process tmp = q.remove();
            if(tmp.time <= avail){
                tmp.done=true;
                avail += tmp.time;
                safeSeq.add(tmp);
            }
            else{
                q.add(tmp);
            }
            if(PrevDeadlock(processes, avail)){
                flag = true;
                break;
            }
        }
        if(flag==false){
            System.out.println("State: Safe");
            System.out.println("Safe Sequence: ");
            for(int i=0; i<safeSeq.size(); i++){
                System.out.println(safeSeq.get(i).name);
            }
        }
        else{
            System.out.println("State: Deadlock");
            if(safeSeq.size() > 0){
                System.out.println("The processes that go in the safe sequence are only: ");
                for(int i=0; i<safeSeq.size(); i++){
                    System.out.println(safeSeq.get(i).name);
                }
            }
            else{
                System.out.println("No processes go in the safe sequence.");
            }
        }
    }

    private static boolean PrevDeadlock(ArrayList<Process> p, int avail) {
        for(int i=0; i<p.size(); i++){
            if(p.get(i).done == false && p.get(i).time <= avail)
                return false;
        }
        return true;
    }

   
}

class Process{
   int id;
   String name;
   int time;
   boolean done;

    Process(int i, String name, int time) {
        id = i;
        this.name = name;
        this.time = time;
        done = false;
    }
}
