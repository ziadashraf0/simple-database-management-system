package DB;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by wagoo on 4/25/2017.
 */
public class Context implements State {
    private State state;

    public Context(){
        state = null;
    }

    public void setState(State state){
        this.state = state;
    }

    public State getState(){
        return state;
    }

    @Override
    public boolean doAction(String query) throws SQLException {
       return this.state.doAction(query);
    }
}
