package edu.uw.tcss422.util;
/**
 * PageParser.java
 * A simple parser using JSoup to extract elements from web pages.
 * Requires Document object of web page to parse, or the page URL.
 * Stores and returns all URLs and words found in the Document object.
 * 
 * @author yongyuwang
 * @version 01-28-2014
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageParser {

	/**
	 * A collection of ParseObjects which stores the results for each parse for the PageAnalyzer.
	 */
	private Collection<ParseObject> parseResults = new ArrayList<ParseObject>();
	
	/**
	 * The PageRetriever object.
	 */
	private PageRetriever retrieve;
	
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
	public void parse(Page page) {
		
		long startTime = System.currentTimeMillis();

		ArrayList<String> untokenizdWords = new ArrayList<String>();
				
		Document doc = Jsoup.parse(page.getContent());
		
		// Parse the document for URLs.
		Elements links = doc.select("a[href]");
		
		// Add these links back to the PageRetriever.
		addLinks(links);

		// Adds all words in the HTML body to collection of words.
		untokenizdWords.add(doc.body().text());

		// DEBUG
		// System.out.println("Word list for this page: " + words);

		// Add link texts to list of words
		for (Element link : links) {
			untokenizdWords.add(link.text());

			//DEBUG
			//System.out.println(link.text());
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
	 * Loops over the list of links and pass the pack to PageRetriever.
	 * @param links the list of links to pass back.
	 */
	public void addLinks(Elements links) {
		for (Element link : links) {
        	retrieve.addURL(link.attr("abs:href"));
        }
	}
	
	/**
	 * Tokenizes the elements in the ArrayList into individual words where applicable, 
	 * stores them back into the ArrayList 
	 * @param source the ArrayList containing Strings to tokenize.
	 * @return tolkenizedWords the tokenizedArrayList of strings.
	 */
	public Collection<String> stringTokenizer(ArrayList<String> source){
		Collection<String> tolkenizedWords = new ArrayList<String>();
		for (Iterator<String> iterator = source.iterator(); iterator.hasNext();){
			String current = iterator.next();
			String[] tolken = current.split(" ");
			for (String element : tolken) {
				element = element.toLowerCase();
				tolkenizedWords.add(element);
			}
		}
		return tolkenizedWords;
	}
	
	
	/**
	 * Gets the collection of ParseObjects.
	 * @return A collection of ParseObjects.
	 */
	public Collection<ParseObject> getParseObjects() {
		return parseResults;
	}
	
	/**
	 * Clears the collection of ParseObjects.
	 */
	public void clearParseObjects() {
		parseResults.clear();
	}
	
}
