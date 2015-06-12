# StorageManager
Writes and reads files index by index and get or set information by indexing method


In this project java programming language is used in order to create simple storage manager. Eclipse is used as IDE. 
Program creates one catalog file which keeps all tables information on it. For each 256 byte, file keeps one table’s information. These bytes keep name, field number, field name, and fields’ size.  Also for each table program creates that table’s file and that table’s primary file. Table file keep records name and primary file keep primary name. 
Program writes and reads files index by index and get or set information by indexing method. For example 20’Th byte keep 0 or 1 for table deleted or not. 

System has 7 classes which are Main, TableOperations, RecordOperations, Catalog, Table, Field, and Record. Main method starts program and gets input from the user. With this input, program calls table operations or record operations. 

Catalog files features:
Each 256 byte keeps information about different tables. Just first 256 byte keeps number of tables on the system. Tables information keeping like that 
0-19 table name
20- Shows 0 or 1 which means table is deleted or not.
21 Number of field keeping here(Program has at most 10 field)
22 -33 Field name (fieldname can be 12 byte at most)
34-35 Keep size of field.
After this byte, if there is any field, it adds 14 byte like before. After the last field byte, program writes number of current records. Sample figures:
Table features
Between 0 and 22
Field name
22 and 34
Field size
34 and 36

Other fields and sizes
Number of Records
After last field size

Example: One Table with 2 fields and 5 records. (Primary size =8 ) (field size=10)	
Table Name
isDeleted
Field number
Field name
Field size
Field Name
Field size
Number of records
Employees
0
2
ID
8
Name
10
5

Table File Features
Table file is similar to Catalog file index. Some of the features are not needed but it is used, because it may necessary for project 2. It may change  other method for project 2. For example isDeleted method for field 1 is unnecessary.
Record Name
Between 0 and 22
isDeleted
Write p for now 
Field[1]
isdeleted
14502
0
p
Enes
0

Primary file features
First 20 bytes keeps primary name, and 20 keeps isDeleted. Program writes primary information each 24 byte.
Classes 

Record Class: Has name, size and integer isDeleted objects.
Field Class:  Has name and size;
Table Class: Has fields, records, name objects. Also it has Constructor of class, SetFields method and setDeletedfield method. Tables has field array because one table may have many fields.
Constructor starts with table name.
SetFields: gets values and set fields of tables. Fields name cannot be higher than 12 bytes and also program get size of field’s records which cannot be higher than 20 byte. Size of bytes are getting because all information of one table cannot be bigger than 256 bytes
setDeletedfield: write over deleted table and calls SetFields method.
Catalog Class: Has Table object, and other necessary object for create class and fields. Also it has refreshTable and refreshCatalog method. It is using for operations over current table. When it is called it gets all Table data, number of records, numbers of fields.
refreshCatalog: Used for read all variables of one table from file and make it current table. It calls in main method in order to have one table on the hand.
refreshTable: It is the same as refreshCatalog but difference is that it finds table by index and make it current table.
TableOperations Class: Using for creation, deletion and get list of tables.  It has methods which are CreateTable, writeToCatalogFile, writeToDeletedTable, writeNumberOftables, DeleteTable, and ListAllTables.
CreateTable: It gets table name and calls setfield, writeToCatalogFile, writeNumberOftables, writeToDeletedTable.
writeToCatalogFile: It writes all catalog informations to Catalog file.
writeToDeletedTable:If there is a deleted table, it writes new table over it.
writeNumberOftables: It writes number of tables that system has on the first page of the catalog table.
DeleteTable: It gets table name and delete on the system by making isdeleted 1.
ListAllTables: It opens Catalog file and write all tables.
RecordOperations Class: Using for creation, deletion, updating, searching and get list of records.  It has methods which are
SetRecords:It gets values of records from the user. Also records size cannot bigger than fields mentioned size. When fields got from user, it also gets size of records from the user. Because of that user cannot write bigger than field’s size
insertRecord: Calls setrecords method and write values to table’s file and primary’s file by calling writeToTableFile and writeToPrimaryFile.
writeToTableFile: Writes records of specified table on table’s file.
writeNumberOfrecords: writes number of records to table’s file
writeToPrimaryFile: Writes primary records of specified table on primary’s file.
CheckPrimary:Checks is there any primary file on the system returns Boolean.
DeleteRecord: Delete records of table.
DeleteRecordsfromFiles: Delete records from table file and primary file.
CheckForDeleted: It returns Boolean and check is there any deleted records.
FoundTable:Find Finds desired table and makes it current table.
ListAllRecords: Writes on the screen all records of specified table.
UpdateRecords: Change records of specified table but not primary.
SearchRecords: Search for a record according to the primary key field 

Main Class: Writes menu of database system and gets input from the user. Calls method by switch function.

In conclusion, program keeps informations basically on 3 files. These are Catalog table which keeps all tables, specific table’s file which keeps records and specific primary’s file which keeps just primary name of records. Program reaches information by indexing. Number of table, number of field and its sizes are limited by the program in order to prevent limit over of 256 byte.