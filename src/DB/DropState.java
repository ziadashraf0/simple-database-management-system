package DB;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wagoo on 4/25/2017.
 */
public class DropState implements State {
    String filename;

    @Override
    public boolean doAction(String query) throws SQLException {

        if(!isTrue(query))             throw new SQLException("syntax error");

        else
        {
            DeleteTable deleteTable=DeleteTable.getInstance();
            System.out.println(filename);
            deleteTable.dropTable(filename);
            return true;
        }



    }
    public boolean isTrue(String z){
        Pattern ptrn = Pattern.compile("DROP\\s+TABLE (\\w+);?",Pattern.CASE_INSENSITIVE);
        Matcher matcher = ptrn.matcher(z);
        if(matcher.find())
            filename=matcher.group(1);
        return matcher.matches();
    }

}
