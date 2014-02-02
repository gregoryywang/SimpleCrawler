/**
 * ParserTest.java
 * Tests the PageParser for correct functionality. 
 */

package edu.uw.tcss422.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserTest {
	public static void main(String[] args) throws IOException {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the URL address of the site you wish to parse (enter 1 to cheat): ");
		String url = scan.nextLine(); 
		if (url.equals("1")) {
			url = "http://www.aol.com";
		}
		System.out.print("Enter the number of pages to parse: [This does not work btw] ");
		int maxParse = scan.nextInt();
		
		// note that the PageRetriever does not actually do anything here.
		PageParser parser = new PageParser(new PageRetriever("",3));
		
        Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
        
        
        // simulate work done in PageRetriver for a single page.
        String content = doc.html();
        Page page = new Page(url, content);
        parser.parse(page);
        
        Collection <ParseObject> results = parser.getParseObjects();
        
        // displays words and links for each ParseObject.
        for (ParseObject object : results) {
        	
        	Elements links = object.getLinks();
        	System.out.println("\nParsing complete. Here's a list of links found on this site:");
            // Iterate over all links
            for (Element link : links) {
            	System.out.println(link.attr("abs:href"));
            }
        	
            Collection<String> words = object.getWords();
            System.out.println("\nWord list for this page: " + words);
            System.out.println("Enter the keyword you wish to search [MUST BE LOWERCASE!]: ");
            String keyword = scan.nextLine();
            System.out.println("Page contains the word [" + keyword + "]: " + words.contains(keyword));
            System.out.println("Total parsing time for this page was " + object.getParseTime() + " milliseconds.");
        }
    
	}

}
