package edu.uw.tcss422.util;
/**
 * PageParser.java
 * A simple parser using JSoup to extract elements from web pages.
 * Requires Document object of web page to parse, or the page URL.
 * Stores and returns all URLs and words found in the Document object.
 * 
 * @author yongyuwang
 * @version 01-26-2014
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageParser {

	/**
	 * A collection of URLs parsed from the Document. This should be mirrored in the Page object.
	 */
	private Elements links;
	
	/**
	 * 	A collection of words parsed from the Document. This should be mirrored in the Page object.
	 */
	private Collection<String> words = new ArrayList<String>();
	
	/**
	 * Alternative parsing using Page object as input.
	 * @param docPage the Page object to parse
	 */
	public void parse(Page docPage) {
		try {
			Document doc = Jsoup.connect(docPage.getURL()).get();
			// calls on main parse method to parse this Document.
			parse(doc);
			// saves words and links list back into Page object.
			docPage.setLinks(links);
			docPage.setWords(words);
		} catch (IOException e) {
			System.out.println("Page Object to URL Parsing failed!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Alternative parsing using URL address as input. 
	 * @param url the URL address to parse.
	 */
	public void parse(String url) {
        try {
			Document doc = Jsoup.connect(url).get();
			parse(doc);
		} catch (IOException e) {
			System.out.println("URL Parsing failed!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses a HTML Document object and extracts URLs and keywords.
	 * @param doc the HTML document to parse
	 */
	public void parse(Document doc) {
		
		// Parse the document for URLs.
		links = doc.select("a[href]");
		
		// Adds all words in the HTML body to collection of words.
		words.add(doc.body().text());
		
		// DEBUG
		// System.out.println("Word list for this page: " + words);
		
		// Add link texts to list of words
		for (Element link : links) {
			words.add(link.text());
			
			//DEBUG
			//System.out.println(link.text());
        }
		
		/* DEBUG
		System.out.printf("\nLinks: (%d)", links.size());
        for (Element link : links) {
            System.out.printf(" * a: <%s>  (%s)", link.attr("abs:href"), link.text());
        }
        */
	}
	
	/**
	 * Tokenizes the elements in the ArrayList into individual words where applicable, 
	 * stores them back into the ArrayList 
	 * @param source the ArrayList containing Strings to tokenize.
	 */
	public void stringTokenizer(ArrayList<String> source){
		// TODO: implement
	}
	
	/**
	 * Gets all links stored for current Document.
	 * @return a list of links
	 */
	public Elements getLinks() {
		return links;
	}
	
	/**
	 * Gets all words stored for current Document.
	 * @return a list of words
	 */
	public Collection<String> getWords() {
		return words;
	}
	
	/**
	 * Clears the URL list to parse another Document.
	 */
	public void clearLinks() {
		links.clear();
	}
	
	/**
	 * Clears the words list to parse another Document.
	 */
	public void clearWords() {
		words.clear();
	}
	
}
