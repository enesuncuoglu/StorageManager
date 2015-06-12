package database;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
/**
 *
 * @author Enes
 */
public class Main {
	private static void wait(BufferedReader in) throws IOException {

        System.out.println("Press Anykey to Return menu");
        in.readLine();
    }
	

 	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

 		String tableinfo;
 	    String s;
 	    byte[] b=new byte[256];
 	    Catalog CAT = new Catalog();
 	    File file = new File("systemcatalog.dat");
 	    RandomAccessFile RdFile = new RandomAccessFile(file,"rw");       
       int choice = 0;
		
		boolean exit=false;
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
		
		if(RdFile.read(b)==-1)
		{
           
 		  while(!exit)
 			{
 			   System.out.println("Main Command list of database system");
 		 	   System.out.println("1= Create a table");
 		 	   System.out.println("2= Delete a table");
 		 	   System.out.println("3= List all the tables");
 		 	   System.out.println("4= Insert a record to specific table");
 		 	   System.out.println("5= Delete a record");
 		       System.out.println("6= Update a record");
 		       System.out.println("7= List all records in a specified table");
 		       System.out.println("8= Search for a record");
 		       System.out.println("9= Quit");
 		       System.out.println("Enter your choice");
 				 
 			     s=in.readLine();
 			     try 
 			     {
 			            choice = Integer.parseInt(s);
 			            
 			     }
 			        
 			     catch(NumberFormatException nFE)
 			     {
 			            System.out.println("it is not a number");
 			            
 			     }
 			     
 			     while(choice>9 || choice<=0)
 			        {
 			            System.out.println("Please enter a valid choice");
 			            s=in.readLine();
 			            try {
 			                choice = Integer.parseInt(s);
 			            }
 			            catch(NumberFormatException nFE){
 			                System.out.println("it is not a number");
 			            }
 			        }
 			
 				switch(choice)
 				{
 					
 					case 1:
 						TableOperations.CreateTable(CAT,RdFile,in);
 						wait(in);
 						break;
 					case 2:
 						TableOperations.DeleteTable(CAT, RdFile, in);
 						wait(in);
 						break;
 					case 3:
 						TableOperations.ListAllTables(CAT, RdFile);
 						wait(in);
 						break;
 					case 4:
 						RecordOperations.insertRecord(CAT, RdFile, in);
 						wait(in);
 						break;
 						
 					case 5:
 						RecordOperations.DeleteRecord(CAT, RdFile, in);
 						wait(in);
 						break;
 						
 					case 6:
 						RecordOperations.UpdateRecords(CAT, RdFile, in);
 						wait(in);
 		 		        break;
 		 		       
 					case 7:
 						RecordOperations.ListAllRecords(CAT, RdFile, in);
 						wait(in);
  		 		        break;
  		 		       
 					case 8: 						
 						//RecordOperations.SearchRecords(CAT, RdFile, in);
 						wait(in);
 						break;
 						
 					case 9:
 						exit=true;
 						System.out.println("System closed");
 						break;
 				
 						
 				}
 					
 			}
       
		}
		else
        {
            RdFile.seek(0);
            s=new String(b);
            RdFile.seek(256);
            RdFile.read(b);
            tableinfo= new String(b);
            CAT.RefreshCatalog(s,tableinfo);
            while(!exit)
 			{
 			 System.out.println("Main Command list of database system");
 		 	   System.out.println("1= Create a table");
 		 	   System.out.println("2= Delete a table");
 		 	   System.out.println("3= List all the tables");
 		 	   System.out.println("4= Insert a record to specific table");
 		 	   System.out.println("5= Delete a record");
 		       System.out.println("6= Update a record");
 		       System.out.println("7= List all records in a specified table");
 		       System.out.println("8= Search for a record");
 		       System.out.println("9= Quit");
 				System.out.println("Enter your choice");
 				 
 			     s=in.readLine();
 			     try {
 			            choice = Integer.parseInt(s);
 			        }
 			        catch(NumberFormatException nFE){
 			            System.out.println("it is not a number");
 			        }

 			        while(choice>9 || choice<=0)
 			        {
 			            System.out.println("Please enter a valid choice");
 			            s=in.readLine();
 			            try {
 			                choice = Integer.parseInt(s);
 			            }
 			            catch(NumberFormatException nFE){
 			                System.out.println("it is not a number");
 			            }
 			        }
 			
 			       switch(choice)
 	 				{
 	 					
 	 					case 1:
 	 						TableOperations.CreateTable(CAT,RdFile,in);
 	 						wait(in);
 	 						break;
 	 					case 2:
 	 						TableOperations.DeleteTable(CAT, RdFile, in);
 	 						wait(in);
 	 						break;
 	 					case 3:
 	 						TableOperations.ListAllTables(CAT, RdFile);
 	 						wait(in);
 	 						break;
 	 					case 4:
 	 						RecordOperations.insertRecord(CAT, RdFile, in);
 	 						wait(in);
 	 						break;
 	 						
 	 					case 5:
 	 						RecordOperations.DeleteRecord(CAT, RdFile, in);
 	 						wait(in);
 	 						break;
 	 						
 	 					case 6:
 	 						RecordOperations.UpdateRecords(CAT, RdFile, in);
 	 						wait(in);
 	 		 		        break;
 	 		 		       
 	 					case 7:
 	 						RecordOperations.ListAllRecords(CAT, RdFile, in);
 	 						wait(in);
 	  		 		        break;
 	  		 		       
 	 					case 8: 						
 	 						//RecordOperations.SearchRecords(CAT, RdFile, in);
 	 						wait(in);
 	 						break;
 	 						
 	 					case 9:
 	 						exit=true;
 	 						System.out.println("System closed");
 	 						break;
 	 				
 	 						
 	 				}
 			}
		}
 	}
}


