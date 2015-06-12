package database;

/**
 *
 * @author Enes
 */
public class Catalog {
	
	
	public Table CatalogTable = new Table();
    public int NumberOfTables=0;
    
    String NoofTable;
    String TableName;
    String FieldName;
    String FieldSize;
    String NoofRecords;
    String RecordName;
    
    
    public void RefreshCatalog(String s,String tablecatalog) {
        

        NoofTable=s.substring(0,2);
        NoofTable=NoofTable.trim();
        NumberOfTables=Integer.parseInt(NoofTable);

        TableName = tablecatalog.substring(0, 20).trim();

        CatalogTable = new Table(TableName);
        CatalogTable.CurrentIndex=1;
        CatalogTable.isDeleted= Integer.parseInt(tablecatalog.substring(20,21));
        CatalogTable.fields = new Field[Integer.parseInt(tablecatalog.substring(21, 22))];
        for(int i=0;i<CatalogTable.fields.length;i++)
        {
            FieldName = tablecatalog.substring(22+i*14, 34+i*14).trim();
            FieldSize = tablecatalog.substring(34+i*14, 36 + i*14).trim();
            CatalogTable.fields[i]= new Field();
            CatalogTable.fields[i].name=FieldName;
            CatalogTable.fields[i].size=(Integer.parseInt(FieldSize));
        }

        NoofRecords = tablecatalog.substring(CatalogTable.fields.length*14+22, CatalogTable.fields.length*14+25).trim();
        CatalogTable.NumberofRecords = Integer.parseInt(NoofRecords);
    }

    public void RefreshTable(String tableinfo,int index)
    {
       

        TableName = tableinfo.substring(0, 20).trim();

        CatalogTable= new Table(TableName);
        CatalogTable.CurrentIndex=(index+1);
        CatalogTable.isDeleted= Integer.parseInt(tableinfo.substring(20,21));
        CatalogTable.fields = new Field[Integer.parseInt(tableinfo.substring(21, 22))];
        for(int i=0;i<CatalogTable.fields.length;i++)
        {
            FieldName = tableinfo.substring(22+i*14, 34+i*14).trim();
            FieldSize = tableinfo.substring(34+i*14, 36 + i*14).trim();
            CatalogTable.fields[i]= new Field();
            CatalogTable.fields[i].name=FieldName;
            CatalogTable.fields[i].size=(Integer.parseInt(FieldSize));
        }

        NoofRecords = tableinfo.substring(CatalogTable.fields.length*14+22, CatalogTable.fields.length*14+25).trim();
        CatalogTable.NumberofRecords = Integer.parseInt(NoofRecords);

    }

    
}
