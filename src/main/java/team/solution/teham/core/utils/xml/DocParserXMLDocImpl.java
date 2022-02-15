package team.solution.teham.core.utils.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import team.solution.teham.core.elements.Element;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DocParserXMLDocImpl implements XMLDoc {
    
    private Document xmlDoc;

    public DocParserXMLDocImpl(InputStream xmlInputStream) throws ParserConfigurationException, SAXException, IOException {

        // Instantiate the Factory
        var dbf = DocumentBuilderFactory.newDefaultInstance();

        // optional, but recommended
        // process XML securely, avoid attacks like XML External Entities (XXE)
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        // parse XML file
        var db = dbf.newDocumentBuilder();

        xmlDoc = db.parse(xmlInputStream);

        // optional, but recommended
        // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        xmlDoc.getDocumentElement().normalize();
    }

    @Override
    public Element getElementById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

}
