package DB;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by wagoo on 4/25/2017.
 */
public interface State {

    public boolean doAction(String query) throws SQLException;

}
