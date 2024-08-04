/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectnikolos2;

import java.util.Random;

/**
 * Class RandomRanger is a class that gives unique random
 * values within a given range. The order of those values
 * is preserved after generation.
 * 
 * @author Kostas Nikolos
 */
public class RandomRanger {
    
    private int[] arr;
    int lim;
    
    /**
     * Constructor for class RandomRanger that allows to
     * set the range of the returned values
     * 
     * @param limit sets the range of numbers to be [0, limit)
     */
    RandomRanger(int limit){
        
        lim = limit;
        arr = new int[limit];
        for(int i = 0; i < limit; i++) arr[i] = i;
        
        Random rnd = new Random();
        
        int at = limit-1;
        int tempat = 0;
        int tempelement = 0;
        while(at != 0){
            tempat = rnd.nextInt(at);
            if(tempat != at){
                //Swap
                tempelement = arr[at];
                arr[at] = arr[tempat];
                arr[tempat] = tempelement;
            }
            at--;
        }
    }
    
    /**
     * Function that returns a random number at the order specified.
     * The same order will produce the same number
     * 
     * Numbers will repeat if the order surpasses the range limit
     * 
     * @param i the order of the random number you seek
     * @return the random number of the range
     */
    int getSeriesNumber(int i){
        
        return arr[i % lim];
        
    }
    
    
    
}
