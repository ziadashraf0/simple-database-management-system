package DB;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swidan on 14/04/17.
 */
public class Record {
    /*private String tableName;
    private ArrayList<String> columnNames;
    private ArrayList<Object> values;*/

    private static Record record;

    private void Record() {

    }

    public static synchronized Record getInstance() {
        if (record == null)
            record = new Record();
        return record;
    }


    public int insert(String tableName, String[] columnNames, String[] values) throws IOException, XMLStreamException {


        XMLInputFactory inFactory = XMLInputFactory.newInstance();
        FileInputStream fr = new FileInputStream(tableName + ".xml");
        XMLEventReader eventReader = inFactory.createXMLEventReader(fr);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        FileOutputStream f = new FileOutputStream("temp.xml");
        XMLEventWriter eventWriter = factory.createXMLEventWriter(f);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        XMLWriter xmlWriter = XMLWriter.getInstance();

        while (eventReader.hasNext()) {
            XMLEvent e = eventReader.nextEvent();

            if (e.isEndElement()) {
                if (e.asEndElement().getName().toString().equalsIgnoreCase("table")) {

                    xmlWriter.createStartNode(eventWriter, "row", null, null);
                    XMLEvent end = eventFactory.createDTD("\n");
                    eventWriter.add(end);
                    Map<String, Object> tableColumnsMap = Table.getTableColumns();
                    TableColumns tableColumns = (TableColumns) tableColumnsMap.get(tableName);

                    String[] strings = new String[tableColumns.getColumnNames().length];

                    for (int k = 0; k < tableColumns.getColumnNames().length; k++) {
                        strings[k] = tableColumns.getColumnNames()[k];
                    }

                    int flag = 0, found = 0;

                    for (int l = 0; l < columnNames.length; l++) {
                        found = 0;
                        for (int h = 0; h < strings.length; h++) {
                            if (columnNames[l].equals(strings[h])) {
                                found = 1;
                                break;
                            }
                        }
                        if (found == 0) {
                            System.out.println("columns not found in table");
                            return 0;
                        }

                    }

                    for (int j = 0; j < strings.length; j++) {
                        for (int i = 0; i < columnNames.length; i++) {
                            if (strings[j].equals(columnNames[i])) {
                                xmlWriter.createStartNode(eventWriter, columnNames[i], null, null);
                                xmlWriter.createCharacter(eventWriter, values[i]);
                                xmlWriter.createEndNode(eventWriter, columnNames[i]);
                                flag = 1;
                                break;

                            }
                        }
                        if (flag == 0) {
                            xmlWriter.createStartNode(eventWriter, strings[j], null, null);
                            xmlWriter.createCharacter(eventWriter, "0");
                            xmlWriter.createEndNode(eventWriter, strings[j]);
                        }
                        flag = 0;


                    }

                }

            }
            eventWriter.add(e);
        }
        eventReader.close();
        eventWriter.close();
        if (MyValidation.validate(tableName + ".xsd", "temp.xml")) {
            System.out.println("valid");
            f.flush();
            f.close();
            fr.close();
            System.gc();
            File file = new File(tableName + ".xml");
            file.delete();


            Path source = Paths.get("temp.xml");
            Files.move(source, source.resolveSibling(tableName + ".xml"));

            return 1;
        } else {
            System.out.println("not valid");
            f.flush();
            f.close();
            fr.close();
            System.gc();
            File x = new File("temp.xml");
            x.delete();
            return 0;
        }
    }
}