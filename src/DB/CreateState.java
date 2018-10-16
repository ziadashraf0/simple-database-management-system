package DB;


import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wagoo on 4/25/2017.
 */
public class CreateState implements State {
  private String[] columnNames,datatype;
  private String tableName;


    @Override
    public boolean doAction(String query) throws SQLException {

        if(!isTrue(query))             throw new SQLException("syntax error");

        else
        {
            Table table=Table.getInstance();
            table.create(columnNames,datatype, tableName);

            return true;
        }

    }
    public boolean isTrue(String s){
        Pattern ptrn = Pattern.compile("CREATE\\s+TABLE\\s+(\\w+)\\s*\\((\\s*((\\w+)\\s+(varchar|int)\\s*,?)+)\\);?",Pattern.CASE_INSENSITIVE);
        Matcher matcher= ptrn.matcher(s);
        if(matcher.find()){
            tableName =matcher.group(1);
            String data=matcher.group(2);
            columnNames =data.split("\\s*\\s+(varchar|int),?");
            datatype=data.split(",?\\s*\\s*\\w+\\s+\\s*,?");
            String[] strings=new String[datatype.length-1];
            for(int i=0;i<datatype.length-1;i++)
            { strings[i]=datatype[i+1];
            datatype[i]=strings[i];}
        }
       //datatype[datatype.length-1]=null;

        return matcher.matches();
    }
}
