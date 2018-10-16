package DB;

import org.wiztools.xsdgen.ParseException;
import org.wiztools.xsdgen.XsdGen;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.xml.sax.SAXException;


public class MyValidation {
    public static boolean validate(String xsdPath, String xmlPath){

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            //System.out.println("Exception: "+e.getMessage());
            return false;

        }
        return true;
    }
    public void CreateXSD(String fileName) throws IOException, ParseException {
        MyXsdGen gen = new MyXsdGen();
        File f=new File(fileName+".xml");
        gen.parse(f);
        File out = new File(fileName+".xsd");
        FileOutputStream fo=new FileOutputStream(out);
        gen.write(fo);

        fo.flush();
        fo.close();
        System.gc();


    }

}
