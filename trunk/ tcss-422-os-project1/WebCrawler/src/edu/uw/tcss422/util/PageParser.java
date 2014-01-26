package edu.uw.tcss422.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageParser {

	// A collection of URLs parsed from Documents.
	private Elements links;
	
	public void parse(Document doc) {
		
		// parse the document for URLs
		links = doc.select("a[href]");
		
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
	
}
