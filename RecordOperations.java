package database;

import java.io.BufferedReader;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
*
* @author Enes
*/

public class RecordOperations {
	
	public static int Deletedindex=0;
    public static boolean deletedfound=false;
    public static int Primaryindex=0;
    public static boolean updating=false;
    
	public static void SetRecords(Catalog CAT,BufferedReader in,RandomAccessFile RdPrim) throws IOException
    {
        int NumberOffield=CAT.CatalogTable.fields.length;
        String s;
        
    	CAT.CatalogTable.records= new Record[NumberOffield];
        for(int i=0;i<NumberOffield;i++)
        	CAT.CatalogTable.records[i]=new Record();
        
        for(int i=0; i<NumberOffield ;i++)
        {
        	 //while updating, not ask primary
        	if(updating==true && i==0)
        		i++;
        	if(i==0)
            {
        		
                System.out.println("Please enter " +CAT.CatalogTable.fields[i].name);
                s=in.readLine();
                while(s.length() > CAT.CatalogTable.fields[i].size)
                {
                    System.out.println("Size is too big");
                    s=in.readLine();
                }
                
                while(CheckPrimary(CAT, RdPrim,s)==true){
                	System.out.println("This primaryname already used");
                    s=in.readLine();
                }
                CAT.CatalogTable.records[i].name=s;
                CAT.CatalogTable.records[i].size = s.length();
               
            }
            else
            {
                System.out.println("Please enter " +CAT.CatalogTable.fields[i].name);
                s=in.readLine();
                while(s.length() > CAT.CatalogTable.fields[i].size)
                {
                    System.out.println("Size is too big");
                    s=in.readLine();
                }
                CAT.CatalogTable.records[i].name=s;
                CAT.CatalogTable.records[i].size = s.length();

            }
        }
    }   

	public static void insertRecord(Catalog CAT,RandomAccessFile RdFile,BufferedReader in) throws IOException{
		
		File tabFile;
        File primFile;
        RandomAccessFile RdTFile;
        RandomAccessFile RdPrim;
        Boolean found=FoundTable(CAT,RdFile,in);
        if(found==true)
        
        {
        	tabFile = new File(CAT.CatalogTable.TableName +".dat");   
        	primFile = new File(CAT.CatalogTable.TableName + "PrimaryKey.dat");
            RdTFile = new RandomAccessFile(tabFile,"rw");
            RdPrim = new RandomAccessFile(primFile,"rw");
            SetRecords(CAT, in,RdPrim);
            CAT.CatalogTable.NumberofRecords++;
            writeToPrimaryFile(CAT, RdPrim);
            writeToTableFile(CAT,RdTFile);
            writeNumberOfrecords(CAT, RdTFile);
            writeNumberOfRecordToCatalog(CAT, RdFile);
            
            
            RdTFile.close();
            RdPrim.close();
            System.out.println("Record created");
            }
        
            
        else if(found==false)
        {
            	System.out.println("wrong table name");
            	return;
            	
        }

}
    
	private static void writeNumberOfRecordToCatalog(Catalog CAT,RandomAccessFile RdFile) throws IOException {
		
		String s;
		s=Integer.toString(CAT.CatalogTable.NumberofRecords);
		byte[] array=s.getBytes();
		byte[] b=new byte[array.length];
		
		for(int i=0;i<array.length;i++){
			
			b[i]=array[i];
			
		}
    RdFile.seek((CAT.CatalogTable.CurrentIndex*256)+(CAT.CatalogTable.fields.length*14)+22);
    RdFile.write(b);
	}

	public static void writeToTableFile(Catalog CAT,RandomAccessFile RdTFile) throws IOException
    {
        String s;
        byte[] b=new byte[256];
        int recordindex=0;
        int count=CAT.CatalogTable.records.length;
        byte[] array;
        array=CAT.CatalogTable.records[recordindex].name.getBytes();
        
        for(int i=0;i<256;i++)
        {
        	
            if(i<array.length)
            {
                b[i]=array[i];
                
            }
            
            else if(i==array.length){
            	
                i=19;
            }
            else if(i==20)
            {
                s=Integer.toString(CAT.CatalogTable.records[recordindex].isDeleted);
                array=s.getBytes();
                b[i]=array[0];
            }
            //primary key
            else if(i==21 && recordindex==0)
            {
                s="p";
                array=s.getBytes();
                b[i]=array[0];
                recordindex++;
            }
            
            else if(i>21 && i<(22*count))
            {
                if(i == (recordindex)*22)
                {
                    array=CAT.CatalogTable.records[recordindex].name.getBytes();
                    for(int j=0;j<array.length;i++,j++)
                        b[i]=array[j];
                    if(array.length<20)
                        i=i + (12- array.length);
                }
                if(i==20+((recordindex*22)))
                {
                    s=Integer.toString(CAT.CatalogTable.records[recordindex].isDeleted);
                    array=s.getBytes();
                    
                        b[i]=array[0];
                    
                    recordindex++;
                }
            }
            /*else if(i==(22*count))
            {
            	s=Integer.toString(CAT.CatalogTable.NumberofRecords);
                array=s.getBytes();
                    b[i]=array[0];
            }*/
        }
        if(deletedfound==true&& updating==false)
        {
        	RdTFile.seek(256+(Deletedindex+1)*256);
            RdTFile.write(b);
        }
        else if(updating==true)
        {
        	RdTFile.seek(256+(Primaryindex)*256);
            RdTFile.write(b);
        }
        else
        {
        RdTFile.seek(256+CAT.CatalogTable.NumberofRecords*256);
        RdTFile.write(b);
        }

    }
	
	public static void writeNumberOfrecords(Catalog CAT, RandomAccessFile RdTFile) throws IOException
    {
        byte[] b = new byte[256];
        byte[] array;
        RdTFile.seek(0);
        RdTFile.read(b);
        array = Integer.toString(CAT.CatalogTable.NumberofRecords).getBytes();
        if(array.length==2){
            b[0]=array[0];
            b[1]=array[1];
        }
        else
            b[0]=array[0];
        RdTFile.seek(0);
        RdTFile.write(b);
        
    }
	public static void writeToPrimaryFile(Catalog CAT, RandomAccessFile RdPrim) throws IOException{
		
		String s;
        byte[] b=new byte[24];
        byte[] array;
        array=CAT.CatalogTable.records[0].name.getBytes();
        
        for(int i=0;i<24;i++)
        {
        	if(i<array.length)
            {
                b[i]=array[i];
                
            }
        	else if(i==array.length){
            	
                i=19;
            }
            else if(i==20)
            {
                s=Integer.toString(CAT.CatalogTable.records[0].isDeleted);
                array=s.getBytes();
                b[i]=array[0];
            }
        	
        }
        CheckForDeleted(CAT, RdPrim);
        if(deletedfound==true)
        {
        	RdPrim.seek((Deletedindex)*24);
        	RdPrim.write(b);
        }
        else
        {
        	RdPrim.seek((CAT.CatalogTable.NumberofRecords-1)*24);
            RdPrim.write(b);
        }
        
	}
	public static boolean  CheckPrimary(Catalog CAT, RandomAccessFile RdPrim,String s) throws IOException{
		
		
        byte[] b=new byte[24];
        int NoOfRecord=CAT.CatalogTable.NumberofRecords;
        String temp;
		boolean PrimFound=false;
		String Primname;
		int Checkinteger=Integer.parseInt(s);
		s=Integer.toString(Checkinteger);
		
            for(int i=0;i<NoOfRecord;i++)
            {
                RdPrim.seek(i*24);
                RdPrim.read(b);
                temp = new String(b);
                if(temp.charAt(20)=='0')
                {
                    Primname = temp.substring(0, 20).trim();
                    if(Primname.compareTo(s)==0)
                    {
                        Primaryindex=i;
                        PrimFound = true;
                        return PrimFound;
                    }
                }
                else if(temp.charAt(20)=='1')
                {
                    NoOfRecord++;
                }
                else if(i==NoOfRecord-1)
                {
                	return PrimFound;
                      }
            }
        return PrimFound;
	}
	
	public static void DeleteRecord(Catalog CAT,RandomAccessFile RdFile,BufferedReader in) throws IOException{
		File tabFile;
        File primFile;
        RandomAccessFile RdTFile;
        RandomAccessFile RdPrim;
        boolean tablefound=false;
        tablefound=FoundTable(CAT, RdFile, in);
        
        if(tablefound==true)
            {
            	tabFile = new File(CAT.CatalogTable.TableName +".dat");
                primFile = new File(CAT.CatalogTable.TableName + "PrimaryKey.dat");
                RdTFile = new RandomAccessFile(tabFile,"rw");
                RdPrim = new RandomAccessFile(primFile,"rw");
                DeleteRecordsfromFiles(CAT, in,RdPrim,RdTFile);
                System.out.println("Record deleted");
            	CAT.CatalogTable.NumberofRecords--;
                writeNumberOfrecords(CAT, RdTFile);
                writeNumberOfRecordToCatalog(CAT, RdFile);
                RdTFile.close();
                RdPrim.close();
            }
        else if(tablefound==false)
        {
            	System.out.println("wrong table name");
            	return;
        }
        
	}

	
	private static void DeleteRecordsfromFiles(Catalog CAT, BufferedReader in,RandomAccessFile RdPrim,RandomAccessFile RdTFile) throws IOException {
		
	        int NoOfRecord=CAT.CatalogTable.NumberofRecords;
	        String s;
	        String temp;
	        String One;
	        byte[] b=new byte[24];
	        byte[] c=new byte[256];
	        System.out.println("Please enter " +CAT.CatalogTable.fields[0].name +" that you want to delete");
	        s=in.readLine();
	        String primaryname;
	        byte[] array;
	        
	        while(CheckPrimary(CAT, RdPrim,s)==false)
	        {
	        	System.out.println("No such Primary key");
	            s=in.readLine();
	        }
	        
	            for(int i=0;i<NoOfRecord;i++)
	            {
	                RdPrim.seek(i*24);
	                RdPrim.read(b);
	                temp = new String(b);
	                if(temp.charAt(20)=='0')
	                {
	                    primaryname = temp.substring(0, 20).trim();
	                    if(primaryname.compareTo(s)==0)
	                    {
	                    	One=Integer.toString(1);
	                        array=One.getBytes();
	                        b[20]=array[0];
	                        RdPrim.seek(i*24);
	                        RdPrim.write(b);
	                        
	                        break;
	                    }
	                }
	                else if(temp.charAt(20)=='1')
	                {
	                    NoOfRecord++;
	                }
	                else if(i==NoOfRecord-1)
	                {
	                             break;
	                
	                }
	                
	            }
	            //delete from table file
	            
	            for(int i=0;i<NoOfRecord;i++)
	            {
	            	RdTFile.seek(256 + i*256);
	                RdTFile.read(c);
	                temp = new String(c);
	                if(temp.charAt(20)=='0')
	                {
	                    primaryname = temp.substring(0, 20).trim();
	                    if(primaryname.compareTo(s)==0)
	                    {
	                    	s=Integer.toString(1);
	                        array=s.getBytes();
	                        c[20]=array[0];
	                        RdTFile.seek(256+i*256);
	                        RdTFile.write(c);
	                        
	                        break;
	                    
	                    }
	                
	                }
	                else if(temp.charAt(20)=='1')
	                {
	                    NoOfRecord++;
	                }
	                else if(i==NoOfRecord-1)
	                 break;
	            }
	}
	
	public static void CheckForDeleted(Catalog CAT,RandomAccessFile RdPrim) throws IOException
    {
		int NoOfRecords= CAT.CatalogTable.NumberofRecords;
		String temp;
		byte [] b=new byte[24];
		for(int i=0;i<NoOfRecords;i++)
        {
            RdPrim.seek(i*24);
            RdPrim.read(b);
            temp= new String(b);
            if(temp.charAt(20)=='0'){
            	deletedfound=false;
            }
            else if(temp.charAt(20)=='1')
            {
                Deletedindex=i;
                deletedfound=true;
                break;
            }
            
        }		
    }
	private static boolean FoundTable(Catalog CAT,RandomAccessFile RdFile, BufferedReader in) throws IOException
    {
        String s;
        String temp;
        String tableName;
        byte[] b=new byte[256];
        boolean tablefound=false;
        int TableCount=CAT.NumberOfTables;
        
		if(CAT.NumberOfTables==0)
		{
        	
        	System.out.println("There is no table in database");
        	return false;
        	
        }
        
		System.out.println("Please Enter The Name of Table");
		s = in.readLine();
        while(s.length()>= 20)
        {
            System.out.println("TableName should be smaller than 20 charecters");
            s=in.readLine();

        }
        if(CAT.CatalogTable.TableName.compareTo(s)!= 0)
        {
            for(int i=0;i<TableCount;i++)
            {
                RdFile.seek(256 + i*256);
                RdFile.read(b);
                temp = new String(b);
                if(temp.charAt(20)=='0')
                {
                    tableName = temp.substring(0, 20).trim();
                    if(tableName.compareTo(s)==0)
                    {
                        CAT.RefreshTable(temp,i);
                        tablefound = true;
                        CAT.CatalogTable.CurrentIndex = i + 1;
                        return tablefound;
                    }
                }
                else if(temp.charAt(20)=='1')
                {
                    TableCount++;
                }
                else if(i==TableCount-1)
                {
                	return tablefound;
                             }
            }
            
        }
        else if(CAT.CatalogTable.TableName.compareTo(s)== 0)
        {
            tablefound = true;
        }
        if (tablefound == true)
            return true;
        else
            return false;
		
    }
	public static void ListAllRecords(Catalog CAT,RandomAccessFile RdFile,BufferedReader in) throws IOException
    {
        String s;
        byte[] b = new byte[256];
        Boolean found=FoundTable(CAT,RdFile,in);
        File tabFile = new File(CAT.CatalogTable.TableName +".dat");   
        RandomAccessFile RdTFile = new RandomAccessFile(tabFile,"rw");
        int NoOfRecords= CAT.CatalogTable.NumberofRecords;
        int NumberOfField=CAT.CatalogTable.fields.length;
        
        if(found==true && NoOfRecords==0 ){
        	
        	System.out.println("There is No record");
        	return;
        }
        	
        else if(found==true)
        {
        	
            
            for(int i=0;i<=NoOfRecords;i++)
            {
            	RdTFile.seek(256 + i*256);
            	RdTFile.read(b);
            	s= new String(b);
            	if(s.charAt(20)=='0')
            	{
            		
            		for(int j=0;j<NumberOfField;j++)
            		{
            			System.out.print("  "+s.substring(j*22, (j*22)+20).trim());
            		}
            		System.out.println("\n");
            	}
            	else if(s.charAt(20)=='1')
            	{
            		NoOfRecords++;
            	}
            }
        } 
        else if(found==false)
        {
            	System.out.println("wrong table name");
            	return;
            	
        }
        RdTFile.close();
    }
	
	
	public static void UpdateRecords(Catalog CAT,RandomAccessFile RdFile,BufferedReader in) throws IOException
    {
        String s;
        Boolean found=FoundTable(CAT,RdFile,in);
        File tabFile = new File(CAT.CatalogTable.TableName +".dat");
        File primFile= new File(CAT.CatalogTable.TableName + "PrimaryKey.dat");
        RandomAccessFile RdTFile = new RandomAccessFile(tabFile,"rw");
        RandomAccessFile RdPrim = new RandomAccessFile(primFile,"rw");
        int NoOfRecords= CAT.CatalogTable.NumberofRecords;
        if(found==true && NoOfRecords==0 ){
        	
        	System.out.println("There is No record");
        	return;
        }
        	
        else if(CAT.CatalogTable.fields.length==1 ){
        	
        	System.out.println("You have only primary key and not updatable");
        	return;
        }
        
        System.out.println("Please Enter The "+CAT.CatalogTable.fields[0].name +" that you want to update");
		s = in.readLine();
		
		while(CheckPrimary(CAT, RdPrim,s)==false){
        	System.out.println("No such primary");
            s=in.readLine();
        }
		System.out.println("Primary cannot updatable so just update fields");
		updating=true;
		SetRecords(CAT, in, RdPrim);
		CAT.CatalogTable.records[0].name=s;
        CAT.CatalogTable.records[0].size = s.length();
        writeToTableFile(CAT, RdTFile);
        updating=false;
        RdTFile.close();
    
    }
	/*public static void SearchRecords(Catalog CAT,RandomAccessFile RdFile,BufferedReader in) throws IOException
    {
        String s;
        Boolean found=FoundTable(CAT,RdFile,in);
        File tabFile = new File(CAT.CatalogTable.TableName +".dat");
        File primFile= new File(CAT.CatalogTable.TableName + "PrimaryKey.dat");
        byte[] b = new byte[256];
        RandomAccessFile RdTFile = new RandomAccessFile(tabFile,"rw");
        RandomAccessFile RdPrim = new RandomAccessFile(primFile,"rw");
        int NoOfRecords= CAT.CatalogTable.NumberofRecords;
        int NumberOfField=CAT.CatalogTable.fields.length;
        int userPrimary;
        int tablePrimary;
        if(found==true && NoOfRecords==0 ){
        	
        	System.out.println("There is No record");
        	return;
        }
        	
        else if(CAT.CatalogTable.fields.length==1 ){
        	
        	System.out.println("You have only primary key and not updatable");
        	return;
        }
        
        System.out.println("Please Enter The "+CAT.CatalogTable.fields[0].name +" that you want to update");
		s = in.readLine();
		
		while(CheckPrimary(CAT, RdPrim,s)==false){
        	System.out.println("No such primary");
            s=in.readLine();
        }
		currentPrimary=Integer.parseInt(s);
		System.out.println("Which records do you wanna see bigger(>), less(<) or equal(=)");
		s=in.readLine();
		while(s!="<" ||s!=">" ||s!="=")
        {
            System.out.println("Wrong character");
            s=in.readLine();
        }
		
		for(int i=0;i<=NoOfRecords;i++)
        {
        	RdTFile.seek(256 + i*256);
        	RdTFile.read(b);
        	s= new String(b);
        	tablePrimary = Integer.parseInt(s.substring(0, 20).trim());
        	if(s.charAt(20)=='0' && userPrimary(s) tablePrimary)
        	{
        		
        		for(int j=0;j<NumberOfField;j++)
        		{
        			System.out.print("  "+s.substring(j*22, (j*22)+20).trim());
        		}
        		System.out.println("\n");
        	}
        	else if(s.charAt(20)=='1')
        	{
        		NoOfRecords++;
        	}
        }
		updating=true;
		SetRecords(CAT, in, RdPrim);
		CAT.CatalogTable.records[0].name=s;
        CAT.CatalogTable.records[0].size = s.length();
        writeToTableFile(CAT, RdTFile);
        updating=false;
        RdTFile.close();
    
    }*/
	
}
