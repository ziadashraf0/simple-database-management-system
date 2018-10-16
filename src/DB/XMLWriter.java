package DB;


import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;


/**
 * Created by swidan on 12/04/17.
 */
public class XMLWriter {
    private String configFile;


    private static XMLWriter xmlWriter;
    private void XMLWriter()
    {

    }
    public static synchronized XMLWriter getInstance(){
        if(xmlWriter == null)
            xmlWriter = new XMLWriter();
        return xmlWriter;
    }

    public void createStartNode(XMLEventWriter eventWriter, String name,String attributeTag,String attribute)
    {

        try {

            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            XMLEvent tab = eventFactory.createDTD("\t");

            StartElement sElement = eventFactory.createStartElement("","",name);
            eventWriter.add(sElement);

            if(attribute!=null)
                eventWriter.add(eventFactory.createAttribute(attributeTag,attribute));





        } catch (XMLStreamException e) {
            e.printStackTrace();
        }



    }
    /*public static void main(String[] args) {
        XMLWriter configFile = new XMLWriter();
        configFile.setFile("config2.xml");
        try {
            configFile.saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public void createCharacter(XMLEventWriter eventWriter,String value){

        try {

            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            Characters characters = eventFactory.createCharacters(value);
            eventWriter.add(characters);


        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void createEndNode(XMLEventWriter eventWriter, String name) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
       eventWriter.add(end);

    }


}



