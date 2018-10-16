package DB;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ziad on 25/04/17.
 */
public class DeleteState implements State {
    String filename,fieldname,op,value;
    @Override
    public boolean doAction(String query) throws SQLException {
        if(!istrue(query))
        {
            throw new SQLException("syntax error");

        }
        else
        {
            RecordRemover recordRemover=RecordRemover.getInstance();
            if(recordRemover.remove(filename,fieldname,op.charAt(0),value)!=0) return true;

        }

        return false;
    }
    public boolean istrue(String z){
        Pattern ptrn = Pattern.compile("DELETE\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(\\w+)\\s*(>|<|=)\\s*([a-zA-Z0-9]+);?",Pattern.CASE_INSENSITIVE);
        Matcher matcher=ptrn.matcher(z);
        if(matcher.find()){
             filename=matcher.group(1);
            fieldname=matcher.group(2);
             op =matcher.group(3);
             value=matcher.group(4);

        }
        return matcher.matches();
    }

}
