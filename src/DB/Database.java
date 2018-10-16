package DB;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;

public interface Database {
    /**
     * Creates/drops table or database.
     * @param query structure definition query (e.g., create or drop table)
     * @returns true if success, or false otherwise
     * @throws SQLException syntax error
     */
    public boolean executeStructureQuery(String query) throws java.sql.SQLException, IOException, XMLStreamException;
    /**
     * Select from table
     * @param query data retrieval query (e.g., select)
     * @return the selected records or an empty array if no records match. Columns
    types must be preserved, i.e, Columns of “int” data type return
    Integer objects, and columns of “varchar” data type return String
    objects)
     * @throws SQLException syntax error
     */
    public Object[][] executeRetrievalQuery(String query) throws java.sql.SQLException;
    /**
     * Insert or update or delete the data
     * @param query data manipulation query (e.g., insert, update or delete)
     * @return the updated rows count
     * @throws SQLException syntax error
     */
    public int executeUpdateQuery(String query) throws java.sql.SQLException;}