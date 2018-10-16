package DB;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wagoo on 4/15/2017.
 */
public class DeleteTable {

    private static DeleteTable deleteTable;
    private void DeleteTable()
    {

    }
    public static synchronized DeleteTable getInstance(){
        if(deleteTable == null)
            deleteTable = new DeleteTable();
        return deleteTable;
    }





    public  void dropTable(String tableName)
    {
        File file =new File(tableName+".xml");
        File f=new File(tableName+".xsd");
        if(file.delete()&&f.delete())
            System.out.println("FILE DELETED");
        else
            System.out.println("FILE IS NOT FOUND");

    }
    public static void main(String[] args)
    {
        DeleteTable x= getInstance();
        x.dropTable("ziad");

    }




}
