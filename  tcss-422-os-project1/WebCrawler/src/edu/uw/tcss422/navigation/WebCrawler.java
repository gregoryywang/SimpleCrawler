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
	 * @param args Command line arguments passed by user
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the URL address of the site you wish to parse: ");
		String url = scan.nextLine();
		System.out.print("Enter the max number of webpages you would like to parse: ");
		int maxPagesToParse = scan.nextInt();
		System.out.print("Please enter how many keywords (max 10): ");
		int numKeywords = scan.nextInt();
		System.out.println("Please enter your desired keywords:");
		HashSet<String> keywords = new HashSet<String>();
		for (int i = 0; i < Math.min(10, numKeywords); i++) {
			keywords.add(scan.next());
		}
		scan.close();
		System.out.println("Webcrawler running...");
		
		long startTime = System.currentTimeMillis();

		PageAnalyzer analyzer = new PageAnalyzer(keywords);

		PageRetriever pageRetriever = new PageRetriever(url);

		PageParser parser = new PageParser(pageRetriever);
		Page page;

		do {
			pageRetriever.retrieve();
			page = pageRetriever.next();

			if (page != null) {
//				 System.out.println(page.getURL());
				parser.parse(page);
			}
			
			Collection<ParseObject> results = parser.getParseObjects();
			analyzer.analyze(results);

		} while (pageRetriever.hasNext() && analyzer.getPagesAnalyzed() <= maxPagesToParse);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		
		
		System.out.println(analyzer.getSummary());
		System.out.println("Total execution time: " + duration + " ms");
	}
}