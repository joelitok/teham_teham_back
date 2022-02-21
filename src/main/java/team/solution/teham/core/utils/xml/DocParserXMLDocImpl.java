package team.solution.teham.core.utils.xml;

import org.springframework.http.HttpMethod;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import team.solution.teham.core.elements.Connector;
import team.solution.teham.core.elements.Event;
import team.solution.teham.core.elements.MultiTargetElement;
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
            var node = (Element) root.getFirstChild();
            while (node != null) {
                if (node.hasAttribute("type") && "Start".equalsIgnoreCase(node.getAttribute("type"))) {
                    var createdE = createTehamElementFromNode(node);
                    if (createdE instanceof Event) {
                        return (Event) createdE;
                    } 
                }
                node = (Element) node.getNextSibling();
            }
        } catch (NullPointerException e) {
            throw new MalFormatedDocumentException(e);
        }

        return null;
    }

    @Override
    public team.solution.teham.core.elements.Element getElementById(String id) {
        try {
            var node = (Element) root.getFirstChild();
            while (node != null) {
                if (id.equalsIgnoreCase(node.getAttribute("id"))) {
                    var createdE = createTehamElementFromNode(node);
                    if (node.hasChildNodes() && createdE instanceof MultiTargetElement) {
                        var childs = node.getChildNodes();
                        for (int i=0, l = childs.getLength(); i < l; i++) {
                            var target = (Element) childs.item(i);
                            ((MultiTargetElement) createdE).addTarget(
                                target.getAttribute("id"),
                                target.getAttribute("name")
                            );
                        }
                    } 
                    return createdE;
                }
                node = (Element) node.getNextSibling();
            }
        } catch (NullPointerException e) {
            throw new MalFormatedDocumentException(e);
        }

        return null;
    }


    private team.solution.teham.core.elements.Element createTehamElementFromNode(Element node) {
        var attrId = node.getAttribute("id");
        var attrName = node.getAttribute("name");
        var attrSource = node.getAttribute("source");
        var attrTarget = node.getAttribute("target");
        var type = node.getAttribute("type");
        var tag = node.getNodeName();

        if (tag.equalsIgnoreCase("Connector")) {
            return new Connector(attrId, attrName, attrSource, attrTarget);
        }

        if (tag.equalsIgnoreCase("Event")) {            
            return new Event(attrId, attrName, attrSource, attrTarget, type);
        }

        if (tag.equalsIgnoreCase("Task")) {
            if (type.equalsIgnoreCase("api")) {
                return new TaskApi(
                    attrId, 
                    attrName, 
                    attrSource, 
                    attrTarget, 
                    
                    URI.create(node.getAttribute("uri")),
                    HttpMethod.resolve(node.getAttribute("method"))
                );
            } else {    // it is a view task
                return new TaskView(attrId, attrName, attrSource, attrTarget);
            }
        }

        return null;
    }

}
