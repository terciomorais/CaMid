package br.ufpe.cin.middleware.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class ParserXML {

	public static Document toDocument(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			InputSource is = new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8")));
			db = dbf.newDocumentBuilder();
			doc = db.parse(is);
		} catch (ParserConfigurationException e) {
			// TODO handle exception
			e.printStackTrace();		
		} catch (SAXException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO handle exception
			e.printStackTrace();
		}
		return doc;
	}
	
	public static NodeList findElement(Document doc, String tagName){
		NodeList nodeList = doc.getElementsByTagName(tagName);
		return nodeList;
	}
	
	public static Node firstElement(Document doc, String tagName){
		NodeList nodeList = doc.getElementsByTagName(tagName);
		return nodeList.item(0);
	}

//	public static JSONArray toJSON(String xml){
//		XMLSerializer xmlSerializer = new XMLSerializer();
//		return (JSONArray)xmlSerializer.read(xml);
//	}
	
	public static List<String> getChildXML(String xml){
		List<String> list = new ArrayList<String>();
		Document doc = ParserXML.toDocument(xml);
		for(int index = 0; index < doc.getChildNodes().getLength(); index++)
			list.add(ParserXML.toString(doc.getChildNodes().item(index)));
		if(list.isEmpty())
			return null;
		return list;
	}
	
	public static String toString(Node node) {
		 StringWriter sw = new StringWriter();
		 try {
		   Transformer t = TransformerFactory.newInstance().newTransformer();
		   t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		   t.transform(new DOMSource(node), new StreamResult(sw));
		 } catch (TransformerException te) {
		   System.out.println("nodeToString Transformer Exception");
		 }
		 return sw.toString();
	}
	
	
}
