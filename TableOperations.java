package database;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
*
* @author Enes
*/
public class TableOperations {

	
	public static void CreateTable(Catalog CAT,RandomAccessFile RdFile,BufferedReader in) throws IOException{
		
		File tabFile;
        File primFile;
        RandomAccessFile RdTFile;
        RandomAccessFile RdPrim;
        String s;
        String temp;
        byte[] b=new byte[256];
        boolean found=false;
        
        int foundindex=-1;
        int TableCount=CAT.NumberOfTables;
        
        System.out.println("Please Enter The Name of Table");
        s=in.readLine();
        while(s.length()>= 20)
        {
            System.out.println("TableName should be smaller than 20 charecters");
            s=in.readLine();

        }

        if(CAT.NumberOfTables==0){
            CAT.CatalogTable= new Table(s);
            CAT.CatalogTable.SetFields(in);
            CAT.CatalogTable.CurrentIndex = 1;
            writeToCatalogFile(CAT,RdFile);
            CAT.NumberOfTables++;
            writeNumberOftables(CAT,RdFile);
            
            tabFile = new File(CAT.CatalogTable.TableName +".dat");
            primFile = new File(CAT.CatalogTable.TableName + "PrimaryKey.dat");
            RdTFile = new RandomAccessFile(tabFile,"rw");
            RdPrim = new RandomAccessFile(primFile,"rw");
            RdTFile.close();
            RdPrim.close();
            System.out.println("Table is created");

        }
        else if(CAT.NumberOfTables < 10)
        {
            for(int i=0;i<TableCount;i++)
            {
                RdFile.seek(256 + i*256);
                RdFile.read(b);
                temp= new String(b);
                
                //Is Table name exist before
                if(temp.charAt(20)=='0'){
                    temp= temp.substring(0, 20);
                    temp=temp.trim();
                    if(temp.compareTo((s))== 0)
                    {
                        System.out.println("This Table is already exist please enter a new name");
                        i=0;
                        s=in.readLine();
                    }
                    else if(i==TableCount-1)
                    {
                        break;
                    }
                }
                //Checking deleted table
                else if(temp.charAt(20)=='1' && foundindex == -1)
                {
                    found=true;
                    TableCount++;
                    foundindex=i;
                }
                else if(temp.charAt(20)=='1' && foundindex != -1)
                {
                    TableCount++;
                }
            }
            //if deleted table found overwrite
            if(found==true)
            {
            	CAT.CatalogTable.setDeletedField(in, s);
                CAT.CatalogTable.CurrentIndex = foundindex + 1;
                writeToDeletedTable(CAT,RdFile,foundindex);
                tabFile = new File(CAT.CatalogTable.TableName +".dat");
                primFile = new File(CAT.CatalogTable.TableName + "PrimaryKey.dat");
                RdTFile = new RandomAccessFile(tabFile,"rw");
                RdPrim = new RandomAccessFile(primFile,"rw");
                RdTFile.close();
                RdPrim.close();
                CAT.NumberOfTables++;
                writeNumberOftables(CAT,RdFile);
                System.out.println("Table is created");

            }
            else if(found==false)
            {
            	CAT.CatalogTable= new Table(s);
            	CAT.CatalogTable.SetFields(in);
                writeToCatalogFile(CAT,RdFile);
                tabFile = new File(CAT.CatalogTable.TableName +".dat");
                primFile = new File(CAT.CatalogTable.TableName + "PrimaryKey.dat");
                RdTFile = new RandomAccessFile(tabFile,"rw");
                RdPrim = new RandomAccessFile(primFile,"rw");
                RdTFile.close();
                RdPrim.close();
                CAT.NumberOfTables++;
                CAT.CatalogTable.CurrentIndex = CAT.NumberOfTables;
                writeNumberOftables(CAT,RdFile);
                System.out.println("Table is created");
            }


        }
        else if(CAT.NumberOfTables==10)
        {
            System.out.println("You have already reach the table limit");
        }
        
	
    }
	
	public static void writeToCatalogFile(Catalog CAT,RandomAccessFile RdFile) throws IOException
    {
        String s;
        byte[] b=new byte[256];
        int fieldindex=0;
        int count=CAT.CatalogTable.fields.length;
        byte[] array=CAT.CatalogTable.TableName.getBytes();
        for(int i=0;i<256;i++)
        {
            if(i<array.length)
            {
                b[i]=array[i];
            }
            else if(i==array.length)
                i=19;
            else if(i==20)
            {
                s=Integer.toString(CAT.CatalogTable.isDeleted);
                array=s.getBytes();
                b[i]=array[0];
            }
            else if(i==21)
            {
                s=Integer.toString(CAT.CatalogTable.fields.length);
                array=s.getBytes();
                b[i]=array[0];
            }
            else if(i>21 && i<21+(14*count))
            {
                if(i == 22+(fieldindex*14))
                {
                    array=CAT.CatalogTable.fields[fieldindex].name.getBytes();
                    for(int j=0;j<array.length;i++,j++)
                        b[i]=array[j];
                    if(array.length<12)
                        i=i + (12- array.length);
                }
                if(i==22+((fieldindex*14)+12))
                {
                    s=Integer.toString(CAT.CatalogTable.fields[fieldindex].size);
                    array=s.getBytes();
                    for(int j=0;j<array.length;i++,j++)
                    {
                        b[i]=array[j];
                    }
                    if(array.length==2)
                        i--;
                    fieldindex++;
                }
            }
            else if(i==22+(14*count))
            {
                s=Integer.toString(CAT.CatalogTable.NumberofRecords);
                array=s.getBytes();
                for(int j=0;j<array.length;j++,i++)
                    b[i]=array[j];
                if(array.length<3)
                {
                    i=i + (3-array.length);
                }
            }

        }
        RdFile.seek(256+CAT.NumberOfTables*256);
        RdFile.write(b);

    }
    
    public static void writeToDeletedTable(Catalog CAT,RandomAccessFile RdFile,int foundindex) throws IOException
    {
        String s;
        byte[] b=new byte[256];
        int fieldindex=0;
        int count=CAT.CatalogTable.fields.length;
        byte[] array=CAT.CatalogTable.TableName.getBytes();
        for(int i=0;i<256;i++)
        {
            if(i<array.length)
            {
                b[i]=array[i];
            }
            else if(i==array.length)
                i=19;
            else if(i==20)
            {
                s=Integer.toString(CAT.CatalogTable.isDeleted);
                array=s.getBytes();
                b[i]=array[0];
            }
            else if(i==21)
            {
                s=Integer.toString(CAT.CatalogTable.fields.length);
                array=s.getBytes();
                b[i]=array[0];
            }
            else if(i>21 && i<21+(14*count))
            {
                if(i == 22+(fieldindex*14))
                {
                    array=CAT.CatalogTable.fields[fieldindex].name.getBytes();
                    for(int j=0;j<array.length;i++,j++)
                        b[i]=array[j];
                    if(array.length<12)
                        i=i + (12- array.length);
                }
                if(i==22+((fieldindex*14)+12))
                {
                    s=Integer.toString(CAT.CatalogTable.NumberofRecords);
                    array=s.getBytes();
                    for(int j=0;j<array.length;i++,j++)
                    {
                        b[i]=array[j];
                    }
                    if(array.length==2)
                        i--;
                    fieldindex++;
                }
            }
            else if(i==22+(14*count))
            {
                s=Integer.toString(CAT.CatalogTable.NumberofRecords);
                array=s.getBytes();
                for(int j=0;j<array.length;j++,i++)
                    b[i]=array[j];
                if(array.length<3)
                {
                    i=i + (3-array.length);
                }
            }

        }
        RdFile.seek(256+foundindex*256);
        RdFile.write(b);

    }
    public static void writeNumberOftables(Catalog CAT, RandomAccessFile RdFile) throws IOException
    {
        byte[] b = new byte[256];
        byte[] array;
        RdFile.seek(0);
        RdFile.read(b);
        array = Integer.toString(CAT.NumberOfTables).getBytes();
        if(array.length==2){
            b[0]=array[0];
            b[1]=array[1];
        }
        else
            b[0]=array[0];
        RdFile.seek(0);
        RdFile.write(b);
    }
	
    
    public static void DeleteTable(Catalog CAT,RandomAccessFile RdFile,BufferedReader in) throws IOException
    {

        File tFile;
        File primF;
        RandomAccessFile tableFile;
        RandomAccessFile prim;


        String s;
        String temp;
        int TableCount= CAT.NumberOfTables;
        byte[] b = new byte[256];
        byte[] array;


        if(CAT.NumberOfTables==0)
        {
            System.out.println("There is no table in the database");
        }

        else
        {
            System.out.println("Please enter the name of table you want to delete");

            s=in.readLine();
            while(s.length() > 20)
            {
                System.out.println("Table name must be smaller than 20 characters please enter a valid name");
                s=in.readLine();
            }

            for(int i=0;i<TableCount;i++)
            {
                RdFile.seek(256 + i*256);
                RdFile.read(b);
                temp = new String(b);
                if(temp.charAt(20)=='0')
                {
                    temp = temp.substring(0, 20);
                    temp = temp.trim();
                    if(temp.compareTo(s)==0)
                    {
                        s=Integer.toString(1);
                        array=s.getBytes();
                        b[20]=array[0];
                        RdFile.seek(256 + i*256);
                        RdFile.write(b);
                        CAT.NumberOfTables--;
                        tFile = new File(CAT.CatalogTable.TableName +".dat");
                        primF = new File(CAT.CatalogTable.TableName + "PrimaryKey.dat");
                        tableFile = new RandomAccessFile(tFile,"rw");
                        prim = new RandomAccessFile(primF,"rw");
                        tableFile.close();
                        prim.close();
                        tFile.delete();
                        primF.delete();
                        writeNumberOftables(CAT,RdFile);
                        System.out.println("Table is deleted");
                        break;
                    }
                }
                else if(temp.charAt(20)=='1')
                {
                    TableCount++;
                }
            }
        }
    }
    public static void ListAllTables(Catalog CAT,RandomAccessFile RdFile) throws IOException
    {
        String s;
        byte[] b = new byte[256];
        int TableCount = CAT.NumberOfTables;
        if(CAT.NumberOfTables==0)
            System.out.println("There is no table in the database");
        else
        {
            for(int i=0;i<TableCount;i++)
            {
                RdFile.seek(256 + i*256);
                RdFile.read(b);
                s= new String(b);
                if(s.charAt(20)=='0')
                {
                    System.out.println("    "+s.substring(0, 20).trim());
                }
                else
                {
                    TableCount++;
                }
            }
        }

    }
    
    public static void writeCurrentTable(Catalog CAT,RandomAccessFile RdFile,int foundindex) throws IOException
    {
        String s;
        byte[] b=new byte[256];
        int fieldindex=0;
        int count=CAT.CatalogTable.fields.length;
        byte[] array=CAT.CatalogTable.TableName.getBytes();
        for(int i=0;i<256;i++)
        {
            if(i<array.length)
            {
                b[i]=array[i];
            }
            else if(i==array.length)
                i=19;
            else if(i==20)
            {
                s=Integer.toString(CAT.CatalogTable.isDeleted);
                array=s.getBytes();
                b[i]=array[0];
            }
            else if(i==21)
            {
                s=Integer.toString(CAT.CatalogTable.fields.length);
                array=s.getBytes();
                b[i]=array[0];
            }
            else if(i>21 && i<21+(14*count))
            {
                if(i == 22+(fieldindex*14))
                {
                    array=CAT.CatalogTable.fields[fieldindex].name.getBytes();
                    for(int j=0;j<array.length;i++,j++)
                        b[i]=array[j];
                    if(array.length<12)
                        i=i + (12- array.length);
                }
                if(i==22+((fieldindex*14)+12))
                {
                    s=Integer.toString(CAT.CatalogTable.fields[fieldindex].size);
                    array=s.getBytes();
                    for(int j=0;j<array.length;i++,j++)
                    {
                        b[i]=array[j];
                    }
                    if(array.length==2)
                        i--;
                    fieldindex++;
                }
            }
            else if(i==22+(14*count))
            {
                s=Integer.toString(CAT.CatalogTable.NumberofRecords);
                array=s.getBytes();
                for(int j=0;j<array.length;j++,i++)
                    b[i]=array[j];
                if(array.length<3)
                {
                    i=i + (3-array.length);
                }
            }

        }
        RdFile.seek(256+foundindex*256);
        RdFile.write(b);

    }
}
 

