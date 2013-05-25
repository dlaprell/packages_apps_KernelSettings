package firtecy.android.scripts;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author David Laprell
 *
 */ 
public class ItemXMLHandler extends DefaultHandler {
 
    Boolean currentElement = false;
    String currentValue = "";
    ItemMaster item = null;
    private ArrayList<ItemMaster> itemsList;
 
    public ArrayList<ItemMaster> getItemsList() {
        return itemsList;
    }
 
    // Called when tag starts 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 
        currentElement = true;
        currentValue = "";
        if(localName.equals("Devices")) {
        	itemsList = new ArrayList<ItemMaster>();
        } else if (localName.equals("Device")) {
            item = new ItemMaster();
        } 
 
    }
 
    // Called when tag closing 
    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException {
 
        currentElement = false;
 
        /** set value */
        if (localName.equalsIgnoreCase("name"))
            item.setDevice(currentValue);
        else if (localName.equalsIgnoreCase("kernel"))
            item.setKernel(currentValue);
        else if (localName.equalsIgnoreCase("identifier"))
            item.setIdent(currentValue);
        else if (localName.equalsIgnoreCase("path"))
            item.setPath(currentValue);
        else if(localName.equalsIgnoreCase("Device"))
        	itemsList.add(item);
    }
 
    // Called to get tag characters 
    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {
 
        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }
 
    }
 
}
