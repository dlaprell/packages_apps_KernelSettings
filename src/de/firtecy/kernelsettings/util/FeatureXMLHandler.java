/*
*  Copyright 2013 Firtecy
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package de.firtecy.kernelsettings.util;

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
    private int counter;
 
    public ArrayList<Feature> getItemsList() {
        return itemsList;
    }
 
    public FeatureXMLHandler (){
    	super();
    	counter = 0;
    }
    
    // Called when tag starts 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 
        currentElement = true;
        currentValue = "";
        if (localName.equals("Features")) {
            itemsList = new ArrayList<Feature>();
        } else if (localName.equals("Feature")) {
        	counter++;
            item = new Feature(counter);
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
        else if (localName.equalsIgnoreCase("file"))
        	item.setFile(currentValue);
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
