package team.solution.teham.core.utils.xml;

import org.springframework.http.HttpMethod;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import team.solution.teham.core.elements.Connector;
import team.solution.teham.core.elements.Event;
import team.solution.teham.core.elements.Gateway;
import team.solution.teham.core.elements.TaskApi;
import team.solution.teham.core.elements.TaskView;
import team.solution.teham.core.exceptions.MalFormatedDocumentException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DocParserXMLDocImpl implements XMLDoc {
    
    private Element root;

    public DocParserXMLDocImpl(InputStream xmlInputStream) throws ParserConfigurationException, SAXException, IOException {

        // Instantiate the Factory
        var dbf = DocumentBuilderFactory.newDefaultInstance();

        // optional, but recommended
        // process XML securely, avoid attacks like XML External Entities (XXE)
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        // parse XML file
        var db = dbf.newDocumentBuilder();

        var xmlDoc = db.parse(xmlInputStream);

        // optional, but recommended
        // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        root = xmlDoc.getDocumentElement();
        root.normalize();
    }

    @Override
    public Event getStartEventElement() {
        try {
            var node = root.getFirstChild();
            while (node != null) {
                if (node instanceof Element) {
                    var elt = (Element) node;
                    if (elt.hasAttribute("type") && "Start".equalsIgnoreCase(elt.getAttribute("type"))) {
                        var createdE = createTehamElement(elt);
                        if (createdE instanceof Event) {
                            return (Event) createdE;
                        } 
                    }
                }
                node = node.getNextSibling();
            }
        } catch (NullPointerException e) {
            throw new MalFormatedDocumentException(e);
        }

        return null;
    }

    @Override
    public team.solution.teham.core.elements.Element getElementById(String id) {
        try {
            var node = root.getFirstChild();
            while (node != null) {
                if (node instanceof Element) {
                    var elt = (Element) node;
                    if (id.equalsIgnoreCase(elt.getAttribute("id"))) {
                        return createTehamElement(elt);
                    }
                }
                node = node.getNextSibling();
            }
        } catch (NullPointerException e) {
            throw new MalFormatedDocumentException(e);
        }

        return null;
    }


    private team.solution.teham.core.elements.Element createTehamElement(Element node) {
        var attrId = node.getAttribute("id");
        var attrName = node.getAttribute("name");
        var sources = toArr(node.getAttribute("source"));
        var targets = toArr(node.getAttribute("target"));
        var cases = toArr(node.getAttribute("cases"));
        var type = node.getAttribute("type");
        var tag = node.getNodeName();

        if (tag.equalsIgnoreCase(Connector.class.getSimpleName())) {
            return new Connector(attrId, attrName, sources, targets);
        }

        if (tag.equalsIgnoreCase(Gateway.class.getSimpleName())) {
            return new Gateway(attrId, attrName, sources, targets, cases);
        }

        if (tag.equalsIgnoreCase(Event.class.getSimpleName())) {            
            return new Event(attrId, attrName, sources, targets, type);
        }

        if (tag.equalsIgnoreCase("Task")) {
            if (type.equalsIgnoreCase("api")) {
                return new TaskApi(
                    attrId, 
                    attrName, 
                    sources, 
                    targets, 
                    
                    URI.create(node.getAttribute("uri")),
                    HttpMethod.resolve(node.getAttribute("method"))
                );
            } else {    // it is a view task
                return new TaskView(attrId, attrName, sources, targets);
            }
        }

        return null;
    }

    private String[] toArr(String str) {
        if (str != null) {
            return str.contains(",") ? str.split(",") : new String[] {str};
        }
        return new String[] {};
    }

}
