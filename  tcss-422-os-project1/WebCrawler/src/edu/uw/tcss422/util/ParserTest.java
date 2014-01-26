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
	    
		PageParser parser = new PageParser();
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the URL address of the site you wish to parse: ");
		String url = scan.nextLine(); 
		if (url.equals("1")) {
			url = "http://www.aol.com";
		}
		
        Document doc = Jsoup.connect(url).get();
        
        parser.parse(doc);

        Elements links = parser.getLinks();
        Collection<String> words = parser.getWords();
        
        // Iterate over all links
        for (Element link : links) {
        	System.out.println(link.attr("abs:href"));
        }

        // keyword search test
        System.out.println("Word list for this page: " + words);
        System.out.println("Enter the keyword you wish to search: ");
        String keyword = scan.nextLine();
        System.out.print("Page contains the word " + keyword + ": " + words.contains(keyword));
    
	}

}
