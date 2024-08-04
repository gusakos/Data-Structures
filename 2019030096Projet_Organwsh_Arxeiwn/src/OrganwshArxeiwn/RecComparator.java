
package OrganwshArxeiwn;

import java.util.Comparator;

 

public class RecComparator implements Comparator<byte[]> {
    
    
    public int compare(byte[] o1, byte[] o2) {
        return ByteHelp.readInt(o1,0)-ByteHelp.readInt(o2,0); 
    }
}
