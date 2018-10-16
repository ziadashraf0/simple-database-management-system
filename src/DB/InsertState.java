package DB;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wagoo on 4/25/2017.
 */
public class InsertState implements State{
    private String filename,columnnamess,valuess;
    private String[] columnname,values;

    @Override
    public boolean doAction(String query) throws SQLException {
        if(!isTrue(query))
        {
            throw new SQLException("syntax error");
        }
        else
        {

            try {
                Record record=Record.getInstance();
               if(record.insert(filename,columnname,values)==1)
                   return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
    public boolean isTrue(String z) {
        Pattern ptrn = Pattern.compile("INSERT\\s+INTO\\s+(\\w+)\\s*\\((((\\w+),?)+)\\)\\s*VALUES\\s*\\(\\s*((([a-zA-Z0-9]+),?)+)\\);?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = ptrn.matcher(z);
        if(matcher.find()){
            filename = matcher.group(1);
            columnnamess=matcher.group(2);
            columnname=columnnamess.split(",");
            valuess=matcher.group(5);
            values=valuess.split(",");

        }
        return matcher.matches();
    }
}
