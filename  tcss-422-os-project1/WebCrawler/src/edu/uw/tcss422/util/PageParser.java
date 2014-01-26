package edu.uw.tcss422.util;

import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageParser {

	// A collection of URLs parsed from Documents.
	private Elements links;
	
	// A collection of keywords parsed from Documents.
	Collection<String> keywords = new ArrayList<String>();
	
	public void parse(Document doc) {
		
		// Parse the document for URLs.
		links = doc.select("a[href]");
		
		// Add link text to list of keywords
		for (Element link : links) {
			if (link != null) {
	            keywords.add(link.text());
			}
            
            //DEBUG
			// System.out.println(link.text());

        }
		
		
		// Add all links from this document into queue in PageRetriever. Collisions are handled by the retriever.
		// ????
		
		/* DEBUG
		System.out.printf("\nLinks: (%d)", links.size());
        for (Element link : links) {
            System.out.printf(" * a: <%s>  (%s)", link.attr("abs:href"), link.text());
        }
        */
	}
	
	public Elements getLinks() {
		return links;
	}
	
	public Collection<String> getKeywords() {
		return keywords;
	}
	
	public void clearLinks() {
		links.clear();
	}
	
	public void clearKeywords() {
		keywords.clear();
	}
	
}
