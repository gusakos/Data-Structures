/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectnikolos2;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author user
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //Create a random ranger and a random to help
        Random r = new Random();
        RandomRanger rr = new RandomRanger(1000000); //Maximum 10^6
        
        
        //Create the trees and the array
        ArrayBST tree = new ArrayBST(100000,7);
        ArrayThreadedBST  ttree = new ArrayThreadedBST(100000);
        ArraySorted arr = new ArraySorted(100000);
        
        
        //Add the keys to the data structures
        for(int i = 0; i < 100000; i++){
            
            tree.addKey(rr.getSeriesNumber(i));
            ttree.addKey(rr.getSeriesNumber(i));
            arr.addElement(rr.getSeriesNumber(i));
            
        }
        
        arr.Sort();
        
        //Print statistics for the insertions
        System.out.println("--INSERTION STATISTICS--");
        System.out.println("BST: "+((int)(0.5+tree.getCompares()/100000.0))+" comparisons per insertion");
        System.out.println("Threaded BST: "+((int)(0.5+ttree.getCompares()/100000.0))+" comparisons per insertion");
        System.out.println("");
        
        
        tree.resetCompares();
        ttree.resetCompares();
        
        //Perform the 100 searches
        for(int i = 0; i < 100; i++){
            
            int sk = r.nextInt(1000000);
            tree.findKey(sk);
            ttree.findKey(sk);
            arr.search(sk);
            
        }
        
        System.out.println("--SEARCH STATISTICS--");
        System.out.println("BST: "+((int)(0.5+tree.getCompares()/100.0))+" comparisons per search");
        System.out.println("Threaded BST: "+((int)(0.5+ttree.getCompares()/100.0))+" comparisons per search");
        System.out.println("Sorted Array: "+((int)(0.5+arr.getCompares()/100.0))+" comparisons per search");
        System.out.println("");
        tree.resetCompares();
        ttree.resetCompares();
        arr.resetCompares();
        
        
        for(int k = 100; k <= 1000; k+= 900){
            
            
            //Perform the 100 searches
            for(int i = 0; i < 100; i++){
                
                int sk = r.nextInt(1000000);
                ArrayList<Integer> i1 = tree.searchInorderRange(sk,sk+k);
                ArrayList<Integer> i2 = ttree.searchInorderRange(sk,sk+k);
                ArrayList<Integer> i3 = arr.searchRange(sk,sk+k);
                
                if(i == 0){
                    System.out.println("Show the result:");
                    //Print if it's your first try
                    for(int j = 0; j < i1.size(); j++) System.out.print(i1.get(j)+" ");
                    System.out.println("");
                    for(int j = 0; j < i2.size(); j++) System.out.print(i2.get(j)+" ");
                    System.out.println("");
                    for(int j = 0; j < i3.size(); j++) System.out.print(i3.get(j)+" ");
                    System.out.println("");
                    System.out.println("");
                }
                
            }
            
            
            System.out.println("--SEARCH RANGE WITH K = "+k+" STATISTICS--");
            System.out.println("BST: "+((int)(0.5+tree.getCompares()/100.0))+" comparisons per search");
            System.out.println("Threaded BST: "+((int)(0.5+ttree.getCompares()/100.0))+" comparisons per search");
            System.out.println("Sorted Array: "+((int)(0.5+arr.getCompares()/100.0))+" comparisons per search");
            System.out.println(((double)(0.5+tree.getCompares()/100.0))/((int)(0.5+ttree.getCompares()/100.0)));
            System.out.println("");
            tree.resetCompares();
            ttree.resetCompares();
            arr.resetCompares();
            
            
            
        }
        
        
        
        
        
        
    }
    
}
