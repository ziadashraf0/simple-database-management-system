package DB;

import javax.xml.stream.*;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by swidan on 16/04/17.
 */


public class RecordRemover {

    private static RecordRemover recordRemover;
    private void RecordRemover()
    {

    }
    public static synchronized RecordRemover getInstance(){
        if(recordRemover == null)
            recordRemover = new RecordRemover();
        return recordRemover;
    }




    public int remove(String tableName, String fieldName, char operator, Object value) {
        ArrayList<Integer> rowIndex;

        int parallelIndex = 0;

        try {
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inFactory.createXMLEventReader(new BufferedInputStream(new FileInputStream(tableName + ".xml")));
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            FileOutputStream f=  new FileOutputStream("temp.xml");
            XMLEventWriter eventWriter = factory.createXMLEventWriter(f);
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end =eventFactory.createDTD("\n");



            WantedRows w = WantedRows.getInstance();
            rowIndex = w.getWantedRows(tableName, fieldName, operator, value);
            if(rowIndex.size()==0)
                 return 0;

            int j = 0;

            XMLWriter xmlWriter = XMLWriter.getInstance();

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
               // System.out.println(event.toString());
                if (event.isStartElement() &&
                        event.asStartElement().getName().
                                toString().equalsIgnoreCase("row")) {
                    parallelIndex++;

                    if (j<rowIndex.size()&&parallelIndex == rowIndex.get(j)) {



                        while(eventReader.hasNext())
                        {
                            event = eventReader.nextEvent();
                            if(event.isEndElement()
                                    &&event.asEndElement().getName().toString().equalsIgnoreCase("row"))
                                break;

                        }
                        /*event.asEndElement().getName().toString().equalsIgnoreCase("row");*/

                         j++;
                        continue;
                    }

                }
                     eventWriter.add(event);
               // System.out.println(event.toString());
            }
            eventReader.close();
            eventWriter.close();
            Path p = Paths.get( tableName + ".xml");
            Files.delete(p);
            f.close();
            Path source = Paths.get("temp.xml");

            Files.move(source, source.resolveSibling(tableName + ".xml"));

            return rowIndex.size();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

   return 1;
    }


    public static void main(String[] args) throws IOException, XMLStreamException {

        RecordRemover table = new RecordRemover();
        String x = "20";
        table.remove("ziad", "firstName", '=', x);

    }
}
