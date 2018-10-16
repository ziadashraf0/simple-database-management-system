package DB;

import javax.xml.stream.*;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by swidan and abdallah on 16/04/17.
 */
public class WantedRows {


    private static WantedRows wantedRows;
    private void WantedRows()
    {

    }
    public static synchronized WantedRows getInstance(){
        if(wantedRows == null)
            wantedRows = new WantedRows();
        return wantedRows;
    }





    public ArrayList<Integer> getWantedRows(String tableName, String fieldName, char operator, Object value) {

        ArrayList<Integer> rowIndex=new ArrayList<>();
        int index=0,count=0;
        try {
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inFactory.createXMLEventReader(new BufferedInputStream(new FileInputStream(tableName + ".xml")));
            Map<String, Object> tableColumnsMap = Table.getTableColumns();

            TableColumns tableColumns = (TableColumns) tableColumnsMap.get(tableName);
            String[] strings ;
            strings= tableColumns.getColumnNames();


            int found =0;
            for (int h=0;h<strings.length;h++){
                if(fieldName.equals(strings[h]))
                {
                    found=1;
                    break;
                }
            }

            if(found==0){
                System.out.println("field name not found in table");
                return rowIndex;
            }
            while (eventReader.hasNext()) {

                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    if (event.asStartElement().getName().toString().equalsIgnoreCase("row")) {
                        index++;
                    } else if (event.asStartElement().getName().toString().equalsIgnoreCase(fieldName)) {

                        switch (operator) {
                            case '>':
                                int x=(int)value;
                                if(Integer.parseInt(eventReader.peek().asCharacters().getData())>x)
                                {

                                    rowIndex.add(index);
                                    count++;
                                }
                                break;
                            case '<':
                                int y=(int)value;
                                if(Integer.parseInt(eventReader.peek().asCharacters().getData())<y)
                                {
                                    rowIndex.add(index);
                                    count++;
                                }

                                break;
                            case '=':
                                if (value.equals(eventReader.peek().asCharacters().getData())) {
                                    XMLEvent e= eventReader.peek();

                                    rowIndex.add(index);
                                    count++;
                                }
                                break;

                        }

                    }
                }

            }
            eventReader.close();


        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return rowIndex;
    }

}
