
package OrganwshArxeiwn;


public class ByteHelp {
    
    
    static void writeInt(byte[] arr,int offset,int data){
        
        //Writes an int in a byte array
        int k = 0;
        for(int i = offset; i < arr.length; i++){
            
            arr[i] = (byte)(data >>> (24-8*k)); 
            k += 1;
            if(k == 4) break;
            
        }
        
        
    }
    
    static byte[] readBytes(byte[] arr, int offset, int size){
        
        byte[] newarr = new byte[size];
        
        for(int i = 0; i < size; i++){
            
            if(i+offset*size >= arr.length) break;
            newarr[i] = arr[i+offset*size];
        }
        
        return newarr;
        
    }
    
    static void writeBytes(byte[] arr, int offset, byte[] data){
        
        
        for(int i = 0; i < data.length; i++){
            
            if(i+offset*data.length >= arr.length) break;
            arr[i+offset*data.length] = data[i];
        }
        
        
    }
    
    
    static int readInt(byte[] arr, int offset){
        
        int data = 0;
        int k = 0;
        for(int i = offset; i < arr.length; i++){
            
            
            data |= ( arr[i] & 0xFF) << (24-8*k); 
            k += 1;
            if(k == 4) break;
            
        }
        return data;
        
    }
    
    static void clearZero(byte[] arr){
        
        //Clears the array with zeros
        for(int i = 0; i < arr.length; i++){
            arr[i] = 0;
        }
    
    }
    
    
    
    
}
