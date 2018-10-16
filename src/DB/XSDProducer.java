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
public class XSDProducer {


    private static Map<String, Object> tableColumns = new HashMap<>();
    private static XSDProducer xsdProducer;

    private XSDProducer() {
    }

    public static synchronized XSDProducer getInstance() {
        if (xsdProducer == null)
            xsdProducer = new XSDProducer();
        return xsdProducer;
    }


    public void create(String[] columnNames, String[] dataTypes, String fileName) {


        tableColumns.put(fileName, new TableColumns().setColumnNames(columnNames));


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
            xmlWriter.createStartNode(eventWriter, "row", null, null);
            eventWriter.add(end);

            for (int i = 0; i < columnNames.length; i++) {
                xmlWriter.createStartNode(eventWriter, columnNames[i], null, null);
                if (dataTypes[i].equals("int")) {
                    xmlWriter.createCharacter(eventWriter, "10");
                } else xmlWriter.createCharacter(eventWriter, "string");
                xmlWriter.createEndNode(eventWriter, columnNames[i]);

            }
            //xmlWriter.createEndNode(eventWriter,"row");
            eventWriter.add(eventFactory.createEndElement("", "", "table"));
            // eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();

            MyValidation myValidation = new MyValidation();
            myValidation.CreateXSD(fileName);



        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public static Map<String, Object> getTableColumns() {

        return tableColumns;
    }


}


