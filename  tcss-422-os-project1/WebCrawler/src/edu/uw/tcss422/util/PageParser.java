package edu.uw.tcss422.util;

import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageParser {

	// A collection of URLs parsed from the Document.
	private Elements links;
	
	// A collection of keywords parsed from the Document.
	Collection<String> keywords = new ArrayList<String>();
	
	/**
	 * Parses an HTML Document object and extracts URLs and keywords.
	 * @param doc the HTML document to parse
	 */
	public void parse(Document doc) {
		
		// Parse the document for URLs.
		links = doc.select("a[href]");
		
		// Add link text to list of keywords
		for (Element link : links) {

			keywords.add(link.text());
			
			//DEBUG
			// System.out.println(link.text());

        }
		
		
		// Add all links from this document into queue in PageRetriever. Collisions are handled by the retriever.
		// ???? This functionality should be implemented in a controller class, probably WebCrawler.java
		
		/* DEBUG
		System.out.printf("\nLinks: (%d)", links.size());
        for (Element link : links) {
            System.out.printf(" * a: <%s>  (%s)", link.attr("abs:href"), link.text());
        }
        */
	}
	
	/**
	 * Gets all links stored for current Document.
	 * @return a list of links
	 */
	public Elements getLinks() {
		return links;
	}
	
	/**
	 * Gets all keywords stored for current Document.
	 * @return a list of keywords
	 */
	public Collection<String> getKeywords() {
		return keywords;
	}
	
	/**
	 * Clears the URL list to parse another Document.
	 */
	public void clearLinks() {
		links.clear();
	}
	
	/**
	 * Clears the keywords list to parse another Document.
	 */
	public void clearKeywords() {
		keywords.clear();
	}
	
}
