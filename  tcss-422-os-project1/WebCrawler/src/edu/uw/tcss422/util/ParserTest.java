package edu.uw.tcss422.util;

import java.io.IOException;
import java.util.Collection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserTest {
	public static void main(String[] args) throws IOException {
	    
		PageParser parser = new PageParser();
		
		String url = "http://www.aol.com"; 
		
        Document doc = Jsoup.connect(url).get();
        
        parser.parse(doc);

        Elements links = parser.getLinks();
        Collection<String> words = parser.getWords();
        
        // Iterate over all links
        for (Element link : links) {
        	System.out.println(link.attr("abs:href"));
        }

        System.out.println(words.contains("Sports"));
    
	}

}
