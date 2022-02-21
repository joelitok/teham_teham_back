package team.solution.teham.core.utils.xml;

import team.solution.teham.core.elements.Element;
import team.solution.teham.core.elements.Event;

public interface XMLDoc {

    Event getStartEventElement();

    Element getElementById(String id);
    
}
