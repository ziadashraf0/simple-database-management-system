package DB;


import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wagoo on 4/17/2017.
 */
public class Query {


    private static Query query;

    private void Query() {

    }

    public static synchronized Query getInstance() {
        if (query == null)
            query = new Query();
        return query;
    }

    public Object[][] select(String tableName, String[] wantedColumns, String fieldName, char operator, Object value) {

        int row = 0, i = 0, j;
        ArrayList<Integer> rowIndex;
        Object[][] list;
        WantedRows w = WantedRows.getInstance();
        rowIndex = w.getWantedRows(tableName, fieldName, operator, value);

        Map<String, Object> tableColumnsMap = Table.getTableColumns();
        TableColumns tableColumns = (TableColumns) tableColumnsMap.get(tableName);
        String[] strings = new String[tableColumns.getColumnNames().length];

        for (int k=0;k<tableColumns.getColumnNames().length;k++)
            strings[k] = tableColumns.getColumnNames()[k];


   /*  String[] strings=new String[3];
        strings[0]="firstName";
        strings[1]="middleName";
        strings[2]="lastName";*/
        list = new Object[rowIndex.size()][strings.length];

        XMLInputFactory inFactory = XMLInputFactory.newInstance();
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        try {
            XMLEventReader eventReader = inFactory.createXMLEventReader(new BufferedInputStream(new FileInputStream(tableName + ".xml")));

            if (rowIndex.size() == 0) {
                System.out.println("No Matches Found");
                return null;
            }
            //rowIndex.add(0,1);

            XMLEvent event = eventReader.nextEvent();

            while (eventReader.hasNext()) {

                if (event.isStartElement() && event.asStartElement().getName().toString().equalsIgnoreCase("row")) {
                    row++;
                    if (i < rowIndex.size() && row == rowIndex.get(i)) {
                        j = 0;
                        while (eventReader.hasNext()) {
                            if (event.isEndElement() && event.asEndElement().getName().toString().equalsIgnoreCase("row"))
                                break;
                            if (!event.isCharacters()) {
                                event = eventReader.nextEvent();
                                continue;
                            }
                            if (!event.asCharacters().getData().toString().equalsIgnoreCase("\n")) {
                               // System.out.println(event.asCharacters().getData());
                                list[i][j] = event.asCharacters().getData().toString();

                                j++;
                            }
                            event = eventReader.nextEvent();

                        }


                        i++;
                    }

                }

                event = eventReader.nextEvent();

            }




            eventReader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*for (int k = 0; k <list.length ; k++) {



        }*/
        Object[][] wantedQuery=new Object[rowIndex.size()+1][wantedColumns.length];
        ArrayList<Integer> columnsIndex=new ArrayList<>();
        for(i=0;i<strings.length;i++)
            for(j=0;j<wantedColumns.length;j++)
                if(strings[i].equals(wantedColumns[j]))
                {
                    columnsIndex.add(i);
                    break;
                }
                //LOOP through COLUMN nAmeS  TO INsERT COLUMN NAMeS IN THE WANTEDQUERY's FIRST ROW
                for(int m=0;m<wantedColumns.length;m++)
                {
                    wantedQuery[0][m]=wantedColumns[m];
                }

            int columns=0;
            for(i=0;i<columnsIndex.size();i++)
            {
                for(j=0;j<rowIndex.size();j++)
                {
                    wantedQuery[j+1][columns]=list[j][columnsIndex.get(i)];


                }

                columns++;
            }



       for (int s = 0; s < wantedColumns.length;s++)
            for (int f = 0; f < rowIndex.size()+1; f++) {
                System.out.println(wantedQuery[s][f]);
            }


        return wantedQuery;

    }

  /*public static  void main(String[] args) {
        Query s = Query.getInstance();
        String[] a = new String[2];
        a[0] = "firstName";
        a[1] = "lastName";

        Object[][] x = s.select("O", a, "firstName", '=', "Ahmed");
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 2; j++) {
                System.out.println(x[i][j]);
            }
    }*/
}
