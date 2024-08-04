
package OrganwshArxeiwn;

import java.util.Random;



public class Main {

	 public static void main(String[] args) {
	      
	        
	        int mode = 3;
	        /*
	            0 - no index, no sorting
	            1 - index, no sorting
	            2 - non index, sorting
	            3 - index, sorting
	        */
	        
	        //Statistics variables
	        int main_reads = 0;
	        int main_writes = 0;
	        
	        int sec_reads = 0;
	        int sec_writes = 0;
	        
	        
	        //First of all, create the file manager
	        RecordFileManager Mngr1 = new RecordFileManager();
	        RecordFileManager Mngr2 = new RecordFileManager();
	        
	        //Create the main file and the index
	        RecordFileManager.createFile("test.db", 32);
	        
	        if(mode % 2 != 0){
	            RecordFileManager.createFile("test.index",8);
	        }
	        
	        //Open the two files to add the records
	        if(Mngr1.openFile("test.db") == 0){
	            //System.out.println("Wrong");
	        }
	        
	        if(mode % 2 != 0){
	            if(Mngr2.openFile("test.index") == 0){
	                //System.out.println("Wrong");
	            }
	        }
	        
	        //Add the records to the two files
	        MainRecord rc;
	        PointerRecord prc;
	        
	        //Create the unique random generator
	        RandomRanger rng = new RandomRanger(1000000);
	        
	        for(int i = 0; i < 10000; i++){
	            
	            int kk = rng.getSeriesNumber(i);
	            rc = new MainRecord(kk,'a');
	            //System.out.println(rng.getSeriesNumber(i));
	            //Add the new record to the file
	            Mngr1.setRecordBuffer(rc.Serialize());
	            int pos = Mngr1.writeNextRecord();
	            
	            //Create the pointer record
	            prc = new PointerRecord(kk,pos);
	            
	            //Add the pointer record to the other file
	            Mngr2.setRecordBuffer(prc.Serialize());
	            Mngr2.writeNextRecord();
	            
	           
	        }
	        
	        Mngr1.writeFlush();
	        
	        if(mode % 2 != 0){
	            Mngr2.writeFlush();
	        }
	        
	        
	        
	        //Creation and loading stats
	        System.out.println("CREATION AND LOADING STATS");
	        System.out.println("test.db disk usage:");
	        Mngr1.printCounter();
	        main_reads += Mngr1.disk_reads;
	        main_writes += Mngr1.disk_writes;
	        Mngr1.resetCounter();
	        System.out.println("");
	        if(mode % 2 != 0){
	            System.out.println("test.index disk usage:");
	            Mngr2.printCounter();
	            sec_reads += Mngr2.disk_reads;
	            sec_writes += Mngr2.disk_writes;
	            Mngr2.resetCounter();
	            System.out.println("");
	        }
	        
	        
	        //Sorting stage
	        if(mode == 2)
	            Mngr1.sort();
	        
	        if(mode == 3){
	            Mngr2.sort();
	        }
	        
	        
	        System.out.println("EXTERNAL SORTING STATS");
	        if(mode == 2){
	            System.out.println("test.db disk usage:");
	            Mngr1.printCounter();
	            main_reads += Mngr1.disk_reads;
	            main_writes += Mngr1.disk_writes;
	            Mngr1.resetCounter();
	            System.out.println("");
	        }else if(mode == 3){
	            System.out.println("test.index disk usage:");
	            Mngr2.printCounter();
	            sec_reads += Mngr2.disk_reads;
	            sec_writes += Mngr2.disk_writes;
	            Mngr2.resetCounter();
	            System.out.println("");
	        }else{
	            System.out.println("No sorting is done in mode "+mode);
	            
	        }
	        System.out.println("");
	        
	        //Decide the 20 keys you will check
	        Random rn = new Random();
	        int[] checks = new int[20];
	        for(int i = 0; i < 20; i++){
	            checks[i] = rng.getSeriesNumber(rn.nextInt(10000));
	        }
	        
	     
	        int search_success = 0;
	        if(mode % 2 == 0){
	            //Mode 1 means you search the initial unsorted file
	            for(int i = 0; i < 20; i++){
	                if(mode == 0)
	                    search_success += Mngr1.searchRecord(checks[i]);
	                else
	                    search_success += Mngr1.searchRecordBinary(checks[i]);
	            }
	            
	            
	        }
	        
	        if(mode % 2 == 1){
	            
	            //Mode 2 means you search the index file and then the first file
	            prc = new PointerRecord(0,0);
	            
	            for(int i = 0; i < 20; i++){
	                if(mode == 1)
	                    Mngr2.searchRecord(checks[i]);
	                else
	                    Mngr2.searchRecordBinary(checks[i]);
	                prc.Load(Mngr2.copyRecordBuffer());
	                search_success += Mngr1.searchRecordBlock(checks[i], prc.getPointer());
	                
	            }
	            
	        }
	        
	        System.out.println("Searching found "+search_success+"/20 elements");
	        
	        
	        //Searching stats
	        System.out.println("SEARCHING STATS");
	        System.out.println("test.db disk usage:");
	        Mngr1.printCounter();
	        System.out.println("Average: Reads = "+(int)(0.5+Mngr1.disk_reads/20.0)+" Writes = "+(int)(0.5+Mngr1.disk_writes/20.0));
	        main_reads += Mngr1.disk_reads;
	        main_writes += Mngr1.disk_writes;
	        System.out.println("");
	        if(mode % 2 != 0){
	            System.out.println("test.index disk usage:");
	            sec_reads += Mngr2.disk_reads;
	            sec_writes += Mngr2.disk_writes;
	            Mngr2.printCounter();
	            System.out.println("Average: Reads = "+(int)(0.5+Mngr2.disk_reads/20.0)+" Writes = "+(int)(0.5+Mngr2.disk_writes/20.0));
	        }
	        System.out.println("");
	        //Final statistics
	        System.out.println("SUMED UP STATS");
	        System.out.println("test.db disk usage:");
	        System.out.println("Disk reads: "+main_reads);
	        System.out.println("Disk writes: "+main_writes);
	        System.out.println("");
	        System.out.println("test.index disk usage:");
	        System.out.println("Disk reads: "+sec_reads);
	        System.out.println("Disk writes: "+sec_writes);
	        System.out.println("");
	        System.out.println("");
	        System.out.println("..giving a final of "+(main_reads+sec_reads)+" reads and "+(main_writes+sec_writes) + " writes.");
	        
	        
	        Mngr1.closeFile();
	        Mngr2.closeFile();
	        
	        
	        
	    }
	    
	}
