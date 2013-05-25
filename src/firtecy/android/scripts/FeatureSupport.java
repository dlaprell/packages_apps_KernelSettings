package firtecy.android.scripts;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
 
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * @author David Laprell
 *
 */
public class FeatureSupport {
	
	private Context context;
	private String kInfo, dName;
	private ArrayList<ItemMaster> supportList;
	private ArrayList<Feature> features;
	private OnFeaturesListener lis;
	public final String LOG_TAG = "AdvancedSettings";
	
	/**
	 * @param lis the lis to set
	 */
	public void setLis(OnFeaturesListener lis) {
		this.lis = lis;
	}

	public FeatureSupport(Context con, String kernelInfo, String deviceName) {
		context = con;
		kInfo = kernelInfo;
		dName = deviceName;
	}
	
	public void buildFeatureList() {
		parseXMLforDevice();
		
		String path = deviceSupport();
		if(path != null) {
			parseXMLforFeatures(path);
			Log.i(LOG_TAG, "Found supported Device and Kernel");
		} else {
			Log.i(LOG_TAG, "No supported Device and Kernel");
		}
		if(lis != null) {
			lis.allReady(getFeatures());
		}
	}
	
	private Feature[] getFeatures() {
		Feature[] feat = new Feature[1];
		try {
			feat = features.toArray(feat);
		} catch(Exception ex) {
			Log.d("Advanced Settings", ex.toString());
		}
		return feat;
	}
	
	private void parseXMLforDevice() {
		AssetManager assetManager = context.getAssets();
		try {
			InputStream is = assetManager.open("supported.xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			ItemXMLHandler myXMLHandler = new ItemXMLHandler();
			xr.setContentHandler(myXMLHandler);
			InputSource inStream = new InputSource(is);
			xr.parse(inStream);



			supportList = myXMLHandler.getItemsList();
			
			is.close();

		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	private void parseXMLforFeatures(String path) {
		AssetManager assetManager = context.getAssets();
		try {
			InputStream is = assetManager.open(path + ".xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			FeatureXMLHandler myXMLHandler = new FeatureXMLHandler();
			xr.setContentHandler(myXMLHandler);
			InputSource inStream = new InputSource(is);
			xr.parse(inStream);
			features = myXMLHandler.getItemsList();
			
			Translator translator = new Translator(context);
			
			for(int i = 0; i < features.size();i++) {
				Feature item = features.get(i);
				if(item != null) {
					if(item.getLongname().length() > 0 && !item.getLongname().equals(translator.translate(item.getName()))) {
						item.setLongname(translator.translate(item.getName()));
					}
					if(item.getDescription().length() > 0 && !item.getDescription().equals(translator.translate(item.getName() + "_DES"))) {
						item.setDescription(translator.translate(item.getName() + "_DES"));
					}
					
				}
			}
			
			is.close();

		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	private String deviceSupport() {
		String support = null;
		
		for(ItemMaster item : supportList) {
			if(dName.contains(item.getDevice()) && kInfo.contains(item.getIdent())) {
				support = item.getPath();
				break;
			}
		}
		if(support != null) {
			AssetManager assetManager = context.getAssets();
			try {
				assetManager.open(support + ".xml");
				assetManager = null;
			} catch (IOException e) {
				support = null;
			}
		}
		return support;
	}
	
	public interface OnFeaturesListener  {
		public void allReady(Feature[] features);
	}
}
