
package OrganwshArxeiwn;



public class MainRecord extends Record {

    int key;
    char letter;
    
    MainRecord(int key,char letter){
        super();
        rec_size = 32;
        this.key = key;
        this.letter = letter;
    }
    
    @Override
    int getKey(){
        return key;
    }
    
    @Override
    byte[] Serialize() {
        
        byte[] arr = new byte[rec_size];
        ByteHelp.clearZero(arr);
        ByteHelp.writeInt(arr, 0, key);
        ByteHelp.writeInt(arr, 4, (int) letter);
        
        return arr;
    }

    @Override
    void Load(byte[] arr) {
        
        key = ByteHelp.readInt(arr, 0);
        letter = (char) ByteHelp.readInt(arr, 4);
        
    }
    
    
    
    
    
    
}

