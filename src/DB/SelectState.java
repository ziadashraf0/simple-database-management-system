package DB;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wagoo on 4/25/2017.
 */
public class SelectState implements State {
    String[] wantedColumns;
    String filename,fieldName;
    String op,value;


    @Override
    public boolean doAction(String query) throws SQLException {
        if(!isTrue(query))             throw new SQLException("syntax error");

        else
            return true;
    }
    public Object[][] doQuery()
    {

        Query query=Query.getInstance();

       return query.select(filename,wantedColumns,fieldName,op.charAt(0),value);


    }
    public boolean isTrue(String z){

        Pattern ptrn = Pattern.compile("SELECT\\s+((\\w+,?\\s*)+)\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(\\w+)\\s*(>|<|=)\\s*([a-zA-Z0-9]+);?",Pattern.CASE_INSENSITIVE);

        Matcher matcher = ptrn.matcher(z);
        if(matcher.find()) {
            filename = matcher.group(3);
            String data = matcher.group(1);
             wantedColumns = data.split(",");
            fieldName=matcher.group(4);
            op=matcher.group(5);
            value=matcher.group(6);

          /*  for (int i = 0; i< wantedColumns.length; i++) {
                System.out.println(wantedColumns[i]);
            }*/
        }
        return matcher.matches();
    }
}
