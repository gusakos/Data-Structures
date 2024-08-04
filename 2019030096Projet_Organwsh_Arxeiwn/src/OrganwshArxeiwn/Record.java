
package OrganwshArxeiwn;


public abstract class Record {
    
    int rec_size;
    
    
    Record(){
        
    }
    
    Record(byte[] arr){
        
        //Loads the record for the first time
        Load(arr);
    }
    
    abstract int getKey();
    abstract byte[] Serialize();
    abstract void Load(byte[] arr);
    
    
    
}
