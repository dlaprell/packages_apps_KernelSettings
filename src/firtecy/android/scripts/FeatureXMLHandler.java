package firtecy.android.scripts;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author David Laprell
 *
 */ 
public class FeatureXMLHandler extends DefaultHandler {
 
    Boolean currentElement = false;
    String currentValue = "";
    Feature item = null;
    private ArrayList<Feature> itemsList;
 
    public ArrayList<Feature> getItemsList() {
        return itemsList;
    }
 
    // Called when tag starts 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 
        currentElement = true;
        currentValue = "";
        if (localName.equals("Features")) {
            itemsList = new ArrayList<Feature>();
        } else if (localName.equals("Feature")) {
            item = new Feature();
        } 
 
    }
 
    // Called when tag closing 
    @Override
    public void endElement(String uri, String localName, String qName)throws SAXException {
 
        currentElement = false;
 
        /** set value */
        if (localName.equalsIgnoreCase("name"))
        	item.setName(currentValue);
        else if (localName.equalsIgnoreCase("longname"))
        	item.setLongname(currentValue);
        else if (localName.equalsIgnoreCase("type"))
        	item.setType(currentValue);
        else if (localName.equalsIgnoreCase("path"))
        	item.setPath(currentValue);
        else if (localName.equalsIgnoreCase("from"))
        	item.setFrom(currentValue);
        else if (localName.equalsIgnoreCase("max"))
        	item.setMax(currentValue);
        else if (localName.equalsIgnoreCase("min"))
        	item.setMin(currentValue);
        else if (localName.equalsIgnoreCase("description"))
        	item.setDescription(currentValue);
        else if (localName.equalsIgnoreCase("category"))
        	item.setCategory(currentValue);
        else if (localName.equalsIgnoreCase("steps"))
        	item.setSteps(currentValue);
        else if (localName.equalsIgnoreCase("values"))
        	item.setValues(currentValue);
        else if(localName.equalsIgnoreCase("Feature"))
        	itemsList.add(item);
        
    }
 
    // Called to get tag characters 
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
 
        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }
 
    }
 
}
