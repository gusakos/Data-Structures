package projectnikolos2;

import static java.lang.Integer.min;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Class ArraySorted is a wrapper class for an array,
 * provides sorting capabilities, and searching for range of values
 * 
 * @author Kostas Nikolos
 */
public class ArraySorted {
    
    //This is a wrapper class for an array just to compare elements
    
    int[] array;
    int elems;
    static int test = 0;
    
    int compares;
    
    /**
     * Sets the compare counter back to zero,
     * for use between the statistic counts.
     * 
     */
    public void resetCompares(){
        
        compares = 0;

    }
    
    /**
     * Returns the counted 'comparisons' so far,
     * this includes comparisons, assignments and value returns.
     * 
     * 
     * @return The counted compares
     */
    public int getCompares(){
        
        return compares;
    }
    
    /**
     * Increases the comparison counter by one,
     * returns true, so it can be used directly on if() and while().
     * 
     * @return always true
     */
    protected boolean incrCompares(){
        
        compares++;
        return true;
        
    }
    
    /**
     * Increases the compares counter by a some number
     * returns true, so it can be used directly on if() and while().
     * 
     * @param times How many times to increase
     * @return 
     */
    protected boolean incrCompares(int times){
        
        compares+= times;
        return true;
        
    }
    
    
    /**
     * Constructor for the ArraySorted class,
     * allows to set the size of the array
     * 
     * @param sz the size of the array to create
     */
    ArraySorted(int sz){
        
        array = new int[sz];
        elems = 0;
        compares = 0;
    }
    
    /**
     * Adds an element to the array
     * 
     * @param el the element to add
     */
    public void addElement(int el){
        
        array[elems++] = el;
        
    }
    
    /**
     * Sorts the elements of the array in ascending order
     * 
     */
    public void Sort(){
        
        java.util.Arrays.sort(array);
        
    }
    
    /**
     * Internal function that performs binary search on the array and returns the index
     * of the searched element.
     * 
     * It returns an index even if the search fails, so the value needs
     * to be checked. If the search failed, the returned index is of the
     * next closest value or previous closest value
     * 
     * @param l the minimum index of the search
     * @param r the maximum index of the search
     * @param el the element you are searching for
     * @return the index where the search element resides
     */
    private int bsearch(int l, int r, int el){
        incrCompares(3);
        
        if (r >= l && incrCompares()) {
            
            incrCompares(2);
            int mid = l + (r - l) / 2;
  

            if (array[mid] == el && incrCompares())
                return mid;
  

            if (array[mid] > el && incrCompares())
                return bsearch(l, mid - 1, el);

            return bsearch(mid + 1, r, el);
        }
  
        //If you didn't find get return the index where you stopped
        incrCompares();
        return l;
    }
    
    /**
     * Runs a search of the element specified, and returns if the
     * element is present within the array.
     * 
     * FOR THIS FUNCTION TO WORK, YOU MUST FIRST CALL Sort()
     * 
     * @param el element to search
     * @return weather the search was a success
     */
    public boolean search(int el){
        incrCompares();
        
        incrCompares(2);
        int pos = min(bsearch(0,elems-1,el),elems-1);
        if(array[pos] == el  && incrCompares()) return true;
        return false;
        
        
    }
    
    /**
     * Searches for a range of values within the array.
     * It returns all the found values on an ArrayList
     * 
     * FOR THIS FUNCTION TO WORK, YOU MUST FIRST CALL Sort()
     * 
     * @param Min the minimum value of the search range
     * @param Max the maximum value of the search range
     * @return the arraylist containing all the values in range
     */
    public ArrayList<Integer> searchRange(int Min, int Max){
        incrCompares(3);
        
        int at = bsearch(0,elems-1,Min);
        
        incrCompares();
        ArrayList<Integer> al = new ArrayList<Integer>();
        if(at >= elems && incrCompares()){
            incrCompares();
            return al;
        }
        
        while(array[at] <= Max && incrCompares()){
            
            al.add(new Integer(array[at]));
            
            incrCompares();
            at++;
            if(at == elems && incrCompares()) break;
            
            
        }
        
        
        incrCompares();
        return al;
        
    }
    
    
}
