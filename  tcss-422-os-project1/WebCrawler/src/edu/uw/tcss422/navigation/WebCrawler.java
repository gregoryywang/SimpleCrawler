package edu.uw.tcss422.navigation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

import edu.uw.tcss422.util.Page;
import edu.uw.tcss422.util.PageAnalyzer;
import edu.uw.tcss422.util.PageParser;
import edu.uw.tcss422.util.PageRetriever;
import edu.uw.tcss422.util.ParseObject;

public class WebCrawler {

	/**
	 * @param args
	 *            Command line arguments passed by user
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Please enter how many keywords: ");
		int numKeywords = scan.nextInt();
		System.out.println("Please enter your desired keywords");
		HashSet<String> keywords = new HashSet<String>();
		for (int i = 0; i < numKeywords; i++) {
			keywords.add(scan.next());
		}

		PageAnalyzer analyzer = new PageAnalyzer(keywords);

		PageRetriever pageRetriever = new PageRetriever("http://www.facebook.com");
//		pageRetriever.addURL("http://uw.edu");
//		pageRetriever.addURL("http://en.wikipedia.org");

		PageParser parser = new PageParser(pageRetriever);
		Page page;

		do {
			pageRetriever.retrieve();
			page = pageRetriever.next();

			if (page != null) {
//				System.out.println(page.getURL());
				parser.parse(page);
				Collection<ParseObject> results = parser.getParseObjects();
				
				analyzer.analyze(results);
			}

		} while (pageRetriever.hasNext());
		
		System.out.println(analyzer.getSummary());
	}

}
