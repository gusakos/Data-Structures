
package OrganwshArxeiwn;


public class PointerRecord extends Record {

    int key;
    int pointer;
    
    
    PointerRecord(int k, int p){
        rec_size = 8;
        key = k;
        pointer = p;
    }
    
    
    int getPointer(){
        return pointer;
    }
    
    @Override
    int getKey() {
        return key;
    }

    @Override
    byte[] Serialize() {
        byte[] ret = new byte[rec_size];
        ByteHelp.writeInt(ret, 0, key);
        ByteHelp.writeInt(ret, 4, pointer);
        return ret;
    }

    @Override
    void Load(byte[] arr) {
        key = ByteHelp.readInt(arr, 0);
        pointer = ByteHelp.readInt(arr,4);
    }
    
}
