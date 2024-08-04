
package OrganwshArxeiwn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileManager {

    //File Handle
    RandomAccessFile file = null; //When null, no file is open
    int block_no = 0; //Excluding the header block
    int record_no = 0;
    int block_size = 128; //not used
    int record_size = 0; //How many bytes per record
    
    int key_type = 0; //0 - int, 1 - string
    int key_length = 0; //for string key, how many letters..
    //String key not implemented
    
    //Counters
    int disk_reads = 0;
    int disk_writes = 0;
    
    //Internal file state (not writen on header block)
    int block_at = 0; //Cursor of blocks
    
    
    //Buffer (as so we do not create arrays all the time)
    byte[] arr_buff;
    int block_at_arr = -1;
    
    FileManager(){
        
        //Creates and empties the interal buffer
        arr_buff = new byte[block_size];
        ByteHelp.clearZero(arr_buff);
        
    }
    
    static int createFile(String filename, int record_size){
        
        RandomAccessFile newfile;
        try {
            newfile = new RandomAccessFile(filename,"rw");
        } catch (FileNotFoundException ex) {
            return 0; //Error file not found
        }
        
        //Create and write the first header block to the file
        byte[] hblock = new byte[128];
        ByteHelp.clearZero(hblock);
        
        //ByteHelp.writeInt(hblock,8,block_size); //Write the block size (not used, block_size is  standard 128)
        ByteHelp.writeInt(hblock,12, record_size); //Write the record size
        
        //Write the changes to the first file block
        try{
            newfile.write(hblock);
        }catch(Exception e){
            //System.out.println("Wrong1");
            return 0; //I/O error
        }
        
        //Close the file
        try{
            newfile.close();
        }catch(Exception e){
            //System.out.println("Wrong2");
            return 0;
        }
        
        //Correctly exited
        return 1;
    }
    
    int openFile(String filename){
        
        //Opens a file first created with createFile
        
        //First check if another file is open and close/save it
        if(file != null){
            //Close the file (TODO)
            if(closeFile() == 0) System.out.println("open a file without closing?");;
        }
        
        
        //Open a file by its filename
        try{
            file = new RandomAccessFile(filename,"rw");
        }catch(Exception e){
            System.out.println("File not found");
            return 0; //File not found?
        }
        
        
        //Load the file handle from the first block
        ByteHelp.clearZero(arr_buff);
        
        try{
            file.seek(0);
            file.read(arr_buff);
        }catch(Exception e){
            System.out.println("Reading error");
            return 0; //Error reading the first page
        }
        
        //Load the values from the block
        
        block_no = ByteHelp.readInt(arr_buff, 0); 
        record_no = ByteHelp.readInt(arr_buff, 4);
        //block_size = ByteHelp.readInt(arr_buff, 8); Not used, standard 128 block size
        record_size = ByteHelp.readInt(arr_buff, 12);
        
        block_at = block_no+1; //Put the cursor right at the block you have not created
        
        
        return 1; //Normal function exit
        
    }
    
    int closeFile(){
        
        //Checks if you have a file open, and close it after updating the header block
        if(file == null){
            //No file open, return 0 as an error
            return 0;
        }
        
        //Create the updated header block
        ByteHelp.clearZero(arr_buff);
        
        //Write the information to save
        ByteHelp.writeInt(arr_buff,0,block_no);
        ByteHelp.writeInt(arr_buff,4,record_no);
        ByteHelp.writeInt(arr_buff,8,block_size);
        ByteHelp.writeInt(arr_buff,12,record_size);
        
        //Write the updated block to the file
        try{
            file.seek(0);
            file.write(arr_buff);
            file.close();
        }catch(Exception e){
            System.out.println("Nooeo");
            return 0; //File error
        }
        
        file = null;
        
        //Normal function exit
        return 1;
        
        
    }
    
    int readBlock(int pos){
        
       
        //Reads a block of the opened file

        //If you have it in memory then no need to read it
        
        if(pos == block_at_arr){
            return 1;
        }else{
     
        }
        
        
        if(file == null){
            //No file open
            System.out.println("File not open");
            return 0;
        }
        
        if(block_no < pos){
            //You are trying to read a block that does not exist
           
            return 0;
        }
        
        
        //Try to read the block to the buffer
        try{
            file.seek(block_size*pos);
            file.read(arr_buff);
            disk_reads+=1;
            block_at_arr = pos;
        }catch(Exception e){
            System.out.println("That went wrong");
            return 0; //Something went wrong
        }
        
        //Put the block_at cursor on the next block
        block_at = pos+1;
        
        //Everything is right
        return 1;
        
    }
    
    int readNextBlock(){
        
        //Same as read block but for the block on the cursor
        return readBlock(block_at);
        
    }
    
    int writeBlock(int pos){
        

        //Writes the buffer at the position specified
        if(file == null){
            //No file open
            return 0;
        }
        
        //Try to write the buffer to the file
        try{
            file.seek(block_size*pos);
            file.write(arr_buff);
            disk_writes+=1;
        }catch(Exception e){
            return 0; //Something went wrong
        }
        
        //Put the block_at cursor on the next block
        block_at = pos+1;
        if(block_at > block_no+1) block_no += 1;
        
        
        //The buffer is the same as the block you just written
        block_at_arr = pos;
        
        //Everything is right
        return 1;
        
        
        
    }
    
    int writeNextBlock(){
        
        //Same as write block but for the block_at position
        return writeBlock(block_at);
        
    }
    
    int appendBlock(){
        
        //Same as writeBlock but at the end of the file
        return writeBlock(block_no);
        
    }
    
    int deleteBlock(int pos){
        
        //Writes the buffer at the position specified
        if(file == null){
            //No file open
            return 0;
        }
        
        if(block_no <= pos){
            //You are trying to read a block that does not exist
            return 0;
        }
        
        //Read the final block 
        if(readBlock(block_no-1) == 0){
            return 0; //Something went wrong, reading the last block
        }
        
        //Write the final block at the position of the block you are deleting
        if(writeBlock(pos) == 0){
            return 0;
        }
        
        //Reduce the number of blocks in the file
        block_no -= 1;
        
        //Reduce the file cursor
        block_at -= 1;
        
        //The operation was complete
        return 1;
        
        
    }
    
    void setBuffer(byte[] newbuff){
        arr_buff = newbuff.clone();
        block_at_arr = -1;
    }
    
    byte[] copyBuffer(){
        
        //Returns a copy of the buffer
        byte[] buff_copy = arr_buff.clone();
        return buff_copy;
        
        
    }
    
    void printDetails(){
        
        if(file == null){
            System.out.println("No file open!");
        }else{
            System.out.println("block_no = " + block_no);
            System.out.println("record_no = " + record_no);
            System.out.println("block_size = " + block_size);
            System.out.println("record_size = " + record_size);
            System.out.println("block_at_arr = " + block_at_arr);
        }
        
    }

    void resetCounter(){
        disk_reads = 0;
        disk_writes = 0;
    }
    
    void printCounter(){
        System.out.println("Disk Reads: "+disk_reads);
        System.out.println("Disk Writes: "+disk_writes);
    }
    
    
}
