package edu.uw.tcss422.util;
/**
 * PageParser.java
 * A simple parser using JSoup to extract elements from web pages.
 * Requires Page object of web page to parse.
 * Stores and returns all URLs and words found in the Document object.
 * 
 * @author yongyuwang
 * @version 02-01-2014
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageParser extends Thread {

	/**
	 * A collection of ParseObjects which stores the results for each parse for the PageAnalyzer.
	 */
	private static ArrayList<ParseObject> parseResults = new ArrayList<ParseObject>();

	/**
	 * The PageRetriever object.
	 */
	private PageRetriever retrieve;

	/**
	 * mRunning Indicates whether the thread is running.
	 */
	private volatile boolean mRunning = true;

	/**
	 * Constructor for PageParser.
	 * @param retrieve the PageRetriever object
	 */
	public PageParser(PageRetriever retrieve) {
		this.retrieve = retrieve;
	}
	

	/**
	 * Main parsing method using Page object as input.
	 * @param docPage the Page object to parse
	 */
	public synchronized void  parse(Page page) {
		
		long startTime = System.currentTimeMillis();

		ArrayList<String> untokenizdWords = new ArrayList<String>();
				
		// http://stackoverflow.com/questions/6803046/jsoup-baseuri-gone-after-select
		Document doc = Jsoup.parse(page.getContent(), page.getURL());
		
		
		// Parse the document for URLs.
		Elements links = doc.select("a[href]");

		// Adds all words in the HTML body to collection of words.
		untokenizdWords.add(doc.body().text());

		// Add link texts to list of words and back into PageRetriever
		for (Element link : links) {
			untokenizdWords.add(link.text());
			retrieve.addURL(link.attr("abs:href"));
		}
		
		Collection<String> words = stringTokenizer(untokenizdWords);
		
		// creates a new ParseObject, saves a copy of the words and links lists into ParseObject.
		ParseObject currentParse = new ParseObject(words, links);
		
		// saves ParseObject into the Collection of these objects.
		parseResults.add(currentParse);
		
		// saves total parse time into ParseObject
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		currentParse.setParseTime(duration);
	}
	
	
	/**
	 * Tokenizes the elements in the ArrayList into individual words where applicable, 
	 * stores them back into the ArrayList 
	 * @param source the ArrayList containing Strings to tokenize.
	 * @return tokenizedWords the tokenizedArrayList of strings.
	 */
	public synchronized Collection<String> stringTokenizer(ArrayList<String> source){
		Collection<String> tokenizedWords = new ArrayList<String>();
		for (Iterator<String> iterator = source.iterator(); iterator.hasNext();){
			String current = iterator.next();
			String[] tolken = current.split(" ");
			for (String element : tolken) {
				element = element.toLowerCase();
				tokenizedWords.add(element);
			}
		}
		return tokenizedWords;
	}
	
	 /**
	   * Terminates the thread.
	   */
	  public void terminate() {
	    mRunning = false;
	  }
	  
	  /**
	   * Lalala this doesn't work yet. 
	   * And I'm beginning to think "implements runnable" is better.
	   * Oh, and dubious design. 
	   */
	  public void run() {
	    while( mRunning ) {
//	      getParseObjects();
	    }
	  }
	
	
	/**
	 * Gets a ParseObject from parseResults.
	 * @return A ParseObject.
	 */
	public synchronized static ParseObject getParseObject() {
		return parseResults.remove(0);
	}
	
	/**
	 * Checks to see if parseResults contains any ParseObjects.
	 * @return whether or not the collection is empty.
	 */
	public synchronized static boolean hasParseObject() {
		return !parseResults.isEmpty();
	}
}