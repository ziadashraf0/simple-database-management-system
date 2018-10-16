package DB;




import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import org.wiztools.commons.Charsets;
import org.wiztools.commons.StringUtil;
import DB.MyTypeInferenceUtil;
import org.wiztools.xsdgen.ParseException;
import org.wiztools.xsdgen.XsdConfig;

public  class MyXsdGen {
    private static final String XSD_NS_URI = "http://www.w3.org/2001/XMLSchema";
    private final String xsdPrefix;
    private final boolean enableMaxOccursOnce;
    private Document doc;


    public MyXsdGen() {
        this(new XsdConfig());
    }

    public MyXsdGen(XsdConfig config) {
        this.doc = null;
        this.xsdPrefix = config.getXsdPrefix();
        this.enableMaxOccursOnce = config.isEnableMaxOccursOnce();
    }

    private void processAttributes(Element inElement, Element outElement) {
        for(int i = 0; i < inElement.getAttributeCount(); ++i) {
            Attribute attr = inElement.getAttribute(i);
            String name = attr.getLocalName();
            String value = attr.getValue();
            Element attrElement = new Element(this.xsdPrefix + ":attribute", "http://www.w3.org/2001/XMLSchema");
            attrElement.addAttribute(new Attribute("name", name));
            attrElement.addAttribute(new Attribute("type", this.xsdPrefix + MyTypeInferenceUtil.getTypeOfContent(value)));
            attrElement.addAttribute(new Attribute("use", "required"));
            outElement.appendChild(attrElement);
        }

    }

    private void recurseGen(Element parent, Element parentOutElement) {
        Element complexType = new Element(this.xsdPrefix + ":complexType", "http://www.w3.org/2001/XMLSchema");
        complexType.addAttribute(new Attribute("mixed", "true"));
        Element sequence = new Element(this.xsdPrefix + ":sequence", "http://www.w3.org/2001/XMLSchema");
        complexType.appendChild(sequence);
        this.processAttributes(parent, complexType);
        parentOutElement.appendChild(complexType);
        Elements children = parent.getChildElements();
        Set<String> elementNamesProcessed = new HashSet();

        for(int i = 0; i < children.size(); ++i) {
            Element e = children.get(i);
            String localName = e.getLocalName();
            String nsURI = e.getNamespaceURI();
            String nsName = e.getQualifiedName();
            if(!elementNamesProcessed.contains(nsName)) {
                if(e.getChildElements().size() > 0) {
                    Element element = new Element(this.xsdPrefix + ":element", "http://www.w3.org/2001/XMLSchema");
                    element.addAttribute(new Attribute("name", localName));
                    this.processOccurences(element, parent, localName, nsURI);
                    this.recurseGen(e, element);
                    sequence.appendChild(element);
                } else {
                    String cnt = e.getValue();
                    String eValue = cnt == null?null:cnt.trim();
                    String type = this.xsdPrefix + MyTypeInferenceUtil.getTypeOfContent(eValue);
                    Element element = new Element(this.xsdPrefix + ":element", "http://www.w3.org/2001/XMLSchema");
                    element.addAttribute(new Attribute("name", localName));
                    this.processOccurences(element, parent, localName, nsURI);
                    int attrCount = e.getAttributeCount();
                    if(attrCount > 0) {
                        Element complexTypeCurrent = new Element(this.xsdPrefix + ":complexType", "http://www.w3.org/2001/XMLSchema");
                        complexType.addAttribute(new Attribute("mixed", "true"));
                        Element simpleContent = new Element(this.xsdPrefix + ":simpleContent", "http://www.w3.org/2001/XMLSchema");
                        Element extension = new Element(this.xsdPrefix + ":extension", "http://www.w3.org/2001/XMLSchema");
                        extension.addAttribute(new Attribute("base", type));
                        this.processAttributes(e, extension);
                        simpleContent.appendChild(extension);
                        complexTypeCurrent.appendChild(simpleContent);
                        element.appendChild(complexTypeCurrent);
                    } else {
                        element.addAttribute(new Attribute("type", type));
                    }

                    sequence.appendChild(element);
                }
            }

            elementNamesProcessed.add(nsName);
        }

    }

    private void processOccurences(Element element, Element parent, String localName, String nsURI) {
        if(parent.getChildElements(localName, nsURI).size() > 0) {
            element.addAttribute(new Attribute("maxOccurs", "unbounded"));
        } else {
            element.addAttribute(new Attribute("minOccurs", "0"));
            if(this.enableMaxOccursOnce) {
                element.addAttribute(new Attribute("maxOccurs", "1"));
            }
        }

    }

    private Document getDocument(InputStream is) throws ParsingException, IOException {
        try {
            Builder parser = new Builder();
            Document d = parser.build(is);
            Element rootElement = d.getRootElement();
            Element outRoot = new Element(this.xsdPrefix + ":schema", "http://www.w3.org/2001/XMLSchema");
            Document outDoc = new Document(outRoot);
            String nsPrefix = rootElement.getNamespacePrefix();
            boolean hasXmlns = rootElement.getNamespaceDeclarationCount() > 0;
            if(hasXmlns || StringUtil.isNotEmpty(nsPrefix)) {
                outRoot.addAttribute(new Attribute("targetNamespace", rootElement.getNamespaceURI()));
                outRoot.addAttribute(new Attribute("elementFormDefault", "qualified"));
            }

            for(int i = 0; i < rootElement.getNamespaceDeclarationCount(); ++i) {
                String nsPrefix2 = rootElement.getNamespacePrefix(i);
                String nsURI = rootElement.getNamespaceURI(nsPrefix2);
                outRoot.addNamespaceDeclaration(nsPrefix, nsURI);
            }

            Element rootElementXsd = new Element(this.xsdPrefix + ":element", "http://www.w3.org/2001/XMLSchema");
            rootElementXsd.addAttribute(new Attribute("name", rootElement.getLocalName()));
            outRoot.appendChild(rootElementXsd);
            this.recurseGen(rootElement, rootElementXsd);
            Document var13 = outDoc;
            return var13;
        } finally {
            if(is != null) {
                is.close();
            }

        }
    }

    public MyXsdGen parse(File file) throws IOException, ParseException {
        return this.parse((InputStream)(new FileInputStream(file)));
    }

    public MyXsdGen parse(InputStream is) throws IOException, ParseException {
        try {
            this.doc = this.getDocument(is);
            return this;
        } catch (ParsingException var3) {
            throw new ParseException(var3);
        }
    }

    public void write(OutputStream os) throws IOException {
        if(this.doc == null) {
            throw new IllegalStateException("Call parse() before calling this method!");
        } else {
            this.write(os, Charsets.UTF_8);
        }
    }

    public void write(OutputStream os, Charset charset) throws IOException {
        if(this.doc == null) {
            throw new IllegalStateException("Call parse() before calling this method!");
        } else {
            Serializer serializer = new Serializer(os, charset.name());
            serializer.setIndent(4);
            serializer.write(this.doc);
        }
    }

    public String toString() {
        return this.doc.toXML();
    }
}
