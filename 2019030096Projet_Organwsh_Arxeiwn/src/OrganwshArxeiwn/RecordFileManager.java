
package OrganwshArxeiwn;

import java.util.Comparator;



//This class handles the managing of records, as well as blocks
//Records are handled as byte arrays. (serialization is part of another class)

public class RecordFileManager extends FileManager{
    
    int recs_per_block;
    int block_need_write = -1;
    int record_at; 
    
    //Record buffer
    byte[] rec_buff;
    
    
    RecordFileManager(){
        super(); //Add the new constructor
    }
    
    int openFile(String filename){
        
        
        if(super.openFile(filename) == 0){
            return 0;
        }
        
        //Calculate the records per block
        recs_per_block = block_size/record_size;
        
        //Record cursor set
        record_at = record_no;
        
        
        //Reset the record buffer
        rec_buff = new byte[record_size];
        ByteHelp.clearZero(rec_buff);
        
        return 1;
        
    }
    
    int readRecord(int pos){
        
        //Reads the record specified
        if(file == null){
            return 0;
        }
        
        if(pos < 0 || pos >= record_no){
            return 0;
        }
        
        //Find the block of the record and the position of this block
        int bpos = pos % recs_per_block;
        int blockk_at = 1+(pos / recs_per_block);
        
        //Read the block that contains the record
        if(block_need_write != blockk_at){
            if(readBlock(blockk_at) == 0){
                return 0;
            }
        }
        
        //Read the record into the record buffer
        rec_buff = ByteHelp.readBytes(arr_buff, bpos, record_size);
        
        //Change the cursor
        record_at = pos+1;
        
        //Successful opperation
        return 1;
        
    }
    
    int readNextRecord(){
        
        return readRecord(record_at);
        
    }
    
    void writeFlush(){
        
        if(block_need_write != -1){
            writeBlock(block_need_write);
            block_need_write = -1;
        }
        
    }
    
    int writeRecord(int pos){
        
        //-1 means error of some kind
        //otherwise returns the block position
        
        //Writes the record to the file
        if(file == null){
            return -1;
        }
        
        if(pos > record_no){
            //Never write out of order
            System.out.println("Out of position");
            return -1;
        }
        
        //Find the block of the record and the position of this block
        int bpos = pos % recs_per_block;
        int blockk_at = 1+(pos / recs_per_block);
        
        if(block_need_write != 0 && block_need_write != blockk_at){
            //You are holding a block that contains new records and has not been written yet
            writeBlock(block_need_write);
            block_need_write = blockk_at;
            
            if(blockk_at == 1+block_no){
                //The block does not yet exist
                //Write zeros to the buffer
                ByteHelp.clearZero(arr_buff);
            }else{

                //Load the block to the buffer
                if(readBlock(blockk_at) == 0){
                    System.out.println("Yes problem");
                    return -1;
                }

            }
            
        }
        
        
    
        //Write the record to the buffer
        ByteHelp.writeBytes(arr_buff, bpos, rec_buff);
        
        //Write the buffer to the file
    
        //Add the record cursor
        record_at = pos+1;
        if(record_at > record_no) record_no += 1;
        
        //Operation successful
        return blockk_at;
        
        
        
    }
    
    int writeNextRecord(){
        
        return writeRecord(record_at);
        
    }
    
    byte[] copyRecordBuffer(){
        
        return rec_buff.clone();
    
    }
    
    void setRecordBuffer(byte[] newrec){
        
        rec_buff = newrec.clone();
        
    }
    
    int searchRecordBinary(int key){
        
        int top = record_no-1;
        int bot = 0;
        int mid;
        int res;
        
        while(top >= bot){
            
            mid = bot + (top - bot) / 2;
            //mid = (top+bot)/2;
            if(readRecord(mid) == 0){
                //Error in reading of record
                return 0;
            }
            
            res = compareKey(key);
            if(res == 0){
                //You found the record
                return 1;
            }else if(res < 0){
                top = mid-1;
            }else{
                bot = mid+1;
            }
            
            
        }
        
        return 0;
        
    }
    
    void setBuffer(byte[] arr){
        
        writeFlush();
        super.setBuffer(arr);
        
        
    }
    
    int searchRecord(int key){
        
        //Returns 1 if found, and key is in the record buffer
        //Else 0
        
        for(int i = 0; i < record_no; i++){
            
            if(readRecord(i) == 0){
                System.out.println("end at "+i);
                return 0;
            }
            if(compareKey(key) == 0){
                //System.out.println("found at " + i);
                return 1;
            }
        
        }
        
        return 0;
        
        
    }
    
    int searchRecordBlock(int key, int block){
        
        int recno = (block-1)*recs_per_block;
        
        for(int i = recno; i < record_no/*recno+recs_per_block*/; i++){
            
            if(readRecord(i) == 0) return 0;
            
            
            if(compareKey(key) == 0) return 1;
            
        
        }
        
        return 0;
        
    }
    
    int compareKey(int key){
        
        //Compares given integer key with the stored key in the buffer
        //Returns 0 if equal
        //Negative value if argument precedes
        //Positive value otherwise
        
        int rec_key = ByteHelp.readInt(rec_buff, 0);
      
        return key-rec_key;
        
        
    }
    
    private void sortArray(byte[] arr,int many){
        
        //This function will sort the given array
        //which is assumed to be the same size as one page
        //So it will hold exactly recs_per_block records
        //many used to denote how many records in the block
        if(many == 0) many = recs_per_block;
        
       
        //First put the elements on the array
        byte[][] recarr = new byte[many][];
        for(int i = 0; i < many; i++)
            recarr[i] = ByteHelp.readBytes(arr, i, record_size);
        
        //Sort the array using key
        java.util.Arrays.sort(recarr,new RecComparator());
        
        //Write the array back to the initial buffer
        for(int i = 0; i < many; i++)
            ByteHelp.writeBytes(arr, i, recarr[i]);
       
        
        
        
    }
    
    private void sortBlocks(){
        
        
        //This is the first pass where we sort the blocks by themselves
        
        for(int i = 1; i <= block_no; i++){
            readBlock(i);
            if(i == block_no)
                sortArray(arr_buff,record_no % recs_per_block);
            else
                sortArray(arr_buff,recs_per_block);
            writeBlock(i);
      
        }  
    }
    
    private void mergePass(int many,String filename){
        
        if(many > block_no || many < 1) return;
        
        //Many denotes how many blocks are sorted at once
        //Example. performing a pass with many = 4, creates sorted clusteres of 8 blocks
        
        int times = block_no/(2*many);
        if(block_no % (2*many) != 0) times++;
        
        
        byte[] buff_one = new byte[block_size];
        int one_value = 0;
        int one_block_at = 0;
        int one_record_at = 0;
        int one_base = 0;
        
        byte[] buff_two = new byte[block_size];
        int two_value = 0;
        int two_block_at = 0;
        int two_record_at = 0;
        int two_base = 0;
        
        
        byte[] buff_out = new byte[block_size];
        int out_record_at = 0;
        //int out_block_at = 0;
        
        //Temp file to write to
        FileManager.createFile(filename, record_size);
        FileManager mng = new FileManager();
        mng.openFile(filename);

        for(int i = 0; i < times; i++){
            
            
           
            //Initialize the subpass
            one_base = 1+i*2*many;
            if(one_base <= block_no){
                one_block_at = 0;
                one_record_at = 0;
                readBlock(one_base);
                buff_one = copyBuffer();
                one_value = ByteHelp.readInt(buff_one, 0);
            }
            
            two_base = 1+i*2*many+many;
            if(two_base <= block_no){
                two_block_at = 0;
                two_record_at = 0;
                readBlock(two_base);
                buff_two = copyBuffer();
                two_value = ByteHelp.readInt(buff_two, 0);
            }
            
            while(true){
                
                boolean one_ended = (one_block_at >= many) || ((one_base+one_block_at-1)*recs_per_block+one_record_at >= record_no);
                boolean two_ended = (two_block_at >= many) || ((two_base+two_block_at-1)*recs_per_block+two_record_at >= record_no);
                
                if(one_ended && two_ended) break;
                
                
                //Decide which you choose
                boolean choose_one = two_value >= one_value;
                
                if(one_ended) choose_one = false;
                if(two_ended) choose_one = true;
                
                
                if(choose_one){
                    
                    ByteHelp.writeBytes(buff_out, out_record_at++, ByteHelp.readBytes(buff_one, one_record_at++, record_size));
                   
                    if(one_record_at >= recs_per_block){
                        one_record_at = 0;
                        one_block_at++;
                        if(one_base+one_block_at <= block_no)
                            readBlock(one_base+one_block_at);
                        buff_one = copyBuffer();
                        
                    }
                    if(one_block_at < many)
                        one_value = ByteHelp.readInt(ByteHelp.readBytes(buff_one, one_record_at, record_size),0);
                    
                }else{
                    
                    ByteHelp.writeBytes(buff_out, out_record_at++, ByteHelp.readBytes(buff_two, two_record_at++, record_size));
                    
                    if(two_record_at >= recs_per_block){
                        two_record_at = 0;
                        two_block_at++;
                        if(two_base+two_block_at <= block_no)
                            readBlock(two_base+two_block_at);
                        buff_two = copyBuffer();
                    }
                    if(two_block_at < many)
                        two_value = ByteHelp.readInt(ByteHelp.readBytes(buff_two, two_record_at, record_size),0);
                    
                }
                
                if(out_record_at >= recs_per_block){
                    //Reset the block
                    out_record_at = 0;
                    mng.setBuffer(buff_out);
                    mng.writeNextBlock();
                    
                }
                
            }
            
            
            
            
        }
        
        if(out_record_at > 0){
            //Write any leftover block
            out_record_at = 0;
            mng.setBuffer(buff_out);
            mng.writeNextBlock();
        }
        
        
        
        //Close the temp file
        mng.record_no = record_no;
        
        //Stats adding
        disk_reads += mng.disk_reads;
        disk_writes += mng.disk_writes;
        
        
        mng.closeFile();
        
    }
    
    void sort(){
        
        //Sorts the file using external merge sort
        //Hope this works
        
        //First of all sort the blocks
        sortBlocks();
        
        //Then perform the initial sort
        mergePass(1,"sortfile1");
        
        int turn = 0;
        int many = 2;
        
        RecordFileManager tempmgr = new RecordFileManager();
        
        while(many < block_no){
            
            if(turn == 0){
                
                if(tempmgr.openFile("sortfile1") == 0) System.out.println("1");
                tempmgr.mergePass(many,"sortfile2");
                
                if(tempmgr.closeFile() == 0) System.out.println("2");
                
            }else{
                
                if(tempmgr.openFile("sortfile2") == 0) System.out.println("3");
                tempmgr.mergePass(many,"sortfile1");
                
                
                if(tempmgr.closeFile() == 0) System.out.println("4");
                
            }
            
            
            many *= 2;
            turn = 1-turn;
            
            
        }
        
        if(turn == 0){
            tempmgr.openFile("sortfile1");
        }else{
            tempmgr.openFile("sortfile2");
        }
        
        //Copy the temporary file into the new one
        for(int i = 1; i <= tempmgr.block_no; i++){
            tempmgr.readBlock(i);
            setBuffer(tempmgr.copyBuffer());
            writeBlock(i);
        }
        
        //Add statistics
        disk_reads += tempmgr.disk_reads;
        disk_writes += tempmgr.disk_writes;
        
        //Close the temp files
        tempmgr.closeFile();
        
        
    }
    
    
}
