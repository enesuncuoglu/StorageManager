package database;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.RandomAccessFile;



public class Table{
	
	int NumofTable;
	public int CurrentIndex;
    public int isDeleted;
    public String TableName;
    public int NumberofRecords;
    public Field[] fields;
    public Record[] records;
    
    
	public Table()
    {
        isDeleted = 0;
        NumberofRecords=0;
    }
    public Table(String s)
    {
        TableName=s;
        NumberofRecords=0;
        isDeleted=0;
    }
	public boolean IsEmpty()
    {
        if(NumberofRecords==0)
            return true;
        else
            return false;
    }
	
	public void SetFields(BufferedReader in) throws IOException
    {
        int flag=0;
        int NumberOfFields=0;
        int size=0;
        String s;
        System.out.println("Please enter number of fields");

        s=in.readLine();

        try{
            NumberOfFields=Integer.parseInt(s);
        }
        catch(NumberFormatException nFE){
            flag=1;
            System.out.println("it is not a number");
        }

        while(NumberOfFields>=10 || flag==1 ){
            if(flag==0)
            {
            System.out.println(NumberOfFields + "is so much for this design");
            System.out.println("Please use a number under 10");
            }
            flag =0;
            s=in.readLine();
            try{
                NumberOfFields=Integer.parseInt(s);
            }
            catch(NumberFormatException nFE){
                System.out.println("it is not a number");
                flag =1;

            }

        }
        
        fields= new Field[NumberOfFields];
        for(int i=0;i<NumberOfFields;i++)
            fields[i]=new Field();
        for(int i=0; i<NumberOfFields ;i++)
        {
            if(i==0)
            {
                System.out.println("Please enter the name of PrimaryKey");
                s=in.readLine();
                while(s.length() > 12)
                {
                    System.out.println("Please enter a name smaller than 12 characters");
                    s=in.readLine();
                }
                fields[i].name=s;
                System.out.println("Please enter the size of PrimaryKey");
                s=in.readLine();
                size = Integer.parseInt(s);
                while(size > 20)
                {
                    System.out.println("Please enter a size smaller than 20");
                    s=in.readLine();
                    size = Integer.parseInt(s);
                }
                fields[i].size=size;
            }
            else
            {
                System.out.println("Please enter the name of field " + (i+1));
                s=in.readLine();

                while(s.length() > 12)
                {
                    System.out.println("Please enter a name smaller than 12 characters");
                    s=in.readLine();
                }
                fields[i].name=s;

                System.out.println("Please enter the size of the field " + (i+1));
                s=in.readLine();
                size = Integer.parseInt(s);
                while(size > 20)
                {
                    System.out.println("Please enter a field size smaller than 20");
                    s=in.readLine();
                    size = Integer.parseInt(s);
                }
                fields[i].size=size;
            }
        }
    }   
	
	
	
	 public void setDeletedField(BufferedReader in,String s) throws IOException
	    {
	        this.isDeleted=0;
	        this.NumberofRecords=0;
	        this.TableName=s;
	        this.SetFields(in);

	    }
	 
		
	 
}