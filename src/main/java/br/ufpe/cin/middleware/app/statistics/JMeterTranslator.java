package br.ufpe.cin.middleware.app.statistics;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JMeterTranslator extends DefaultHandler
{
	private StatFileWriter writer;
	
	public JMeterTranslator(StatFileWriter writer)
	{
		this.writer = writer;
	}
	
	private static final Pattern tgRegex = Pattern.compile("TG-([0-9]+)-([0-9]+)\\s.+");
	
	public void startElement(String uri, String localName, String qName, Attributes attrs)
	{
		long duration = 0;
		boolean success = false;
		int workload = 0;
		long delay = 0;
		if(!qName.equals("sample"))
		{
			return;
		}
		for(int i = 0; i < attrs.getLength(); i++)
		{
			String attrName = attrs.getQName(i);
			String attrValue = attrs.getValue(i);
			if(attrName.equals("s"))
			{
				try {
					success = Boolean.parseBoolean(attrValue);
				}
				catch(Exception e)
				{
					success = false;
				}
			}
			if(attrName.equals("t"))
			{
				try {
					duration = Long.parseLong(attrValue);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					duration = 0;
				}
			}
			if(attrName.equals("tn"))
			{
				Matcher matcher = tgRegex.matcher(attrValue);
				int count = matcher.groupCount();
				matcher.find();
				try 
				{
					workload = Integer.parseInt(matcher.group(1));
					delay = Long.parseLong(matcher.group(2));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					workload = 0;
					delay = 0;
				}
			}
		}
		
		writer.write(duration, success, workload, delay);
	}
	
	public static void main(String[] args) 
	{
		StatFileWriter writer = null;
		
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		try 
		{
			writer = new StatFileWriter(new File("/home/diego/development/statistics/middleware-exp/nomon-high-client-jmeter-response-times.dat"));
			JMeterTranslator tranlator = new JMeterTranslator(writer);
			SAXParser parser = saxFactory.newSAXParser();
			parser.parse("/home/diego/development/statistics/middleware-exp/expMiddleware-high-nomon-processed.jtl", tranlator);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			writer.close();
			System.out.println("Finished");
			
		}
	}
}
