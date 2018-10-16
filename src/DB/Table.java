package DB;


import org.wiztools.xsdgen.ParseException;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swidan on 12/04/17.
 */
public class Table {


    private static Map<String, Object> tableColumns = new HashMap<>();
    private static Table table;

    private Table() {
    }

    public static synchronized Table getInstance() {
        if (table == null)
            table = new Table();
        return table;
    }


    public void create(String[] columnNames, String[] dataTypes, String fileName) {


        tableColumns.put(fileName, new TableColumns().setColumnNames(columnNames));
XSDProducer xsdProducer=XSDProducer.getInstance();
xsdProducer.create(columnNames,dataTypes,fileName);

        try {
            XMLWriter xmlWriter = XMLWriter.getInstance();
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(fileName + ".xml"));
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent end = eventFactory.createDTD("\n");

            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            StartElement configStartElement = eventFactory.createStartElement("", "", "table");
            eventWriter.add(configStartElement);
            eventWriter.add(end);
            //xmlWriter.createEndNode(eventWriter,"row");
            eventWriter.add(eventFactory.createEndElement("", "", "table"));
            // eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static Map<String, Object> getTableColumns() {

        return tableColumns;
    }


}

