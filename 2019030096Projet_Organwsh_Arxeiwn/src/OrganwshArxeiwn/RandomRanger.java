
package OrganwshArxeiwn;

import java.util.Random;




public class RandomRanger {
    
    private int[] arr;
    int lim;
    
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
    
    
    int getSeriesNumber(int i){
        
        return arr[i % lim];
        
    }
    
    
    
}
