package edu.uw.tcss422.navigation;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import edu.uw.tcss422.util.Page;
import edu.uw.tcss422.util.PageAnalyzer;
import edu.uw.tcss422.util.PageParser;
import edu.uw.tcss422.util.PageRetriever;
import edu.uw.tcss422.util.ParseObject;
import edu.uw.tcss422.util.SummaryObject;

public class WebCrawler {
	
	public static volatile SummaryObject sum = new SummaryObject();
	
	public static volatile Collection<ParseObject> parseObjects;

	/**
	 * @param args Command line arguments passed by user
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the URL address of the site you wish to parse: ");
		String url = scan.nextLine();
		System.out.print("Please enter 1 for single-threaded or 2 for multi-threaded operation: ");
		int mode = scan.nextInt();
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
		
		if (mode == 1) {
			single(maxPagesToParse, url, keywords);
		} else {
			multi(maxPagesToParse, url, keywords);
		}

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		
		System.out.println("Total execution time: " + duration + " ms");
	}

	private static void single(int maxPagesToParse, String url, HashSet<String> keywords) {
		sum.setKeywords(keywords);
		PageAnalyzer analyzer = new PageAnalyzer();

		PageRetriever pageRetriever = new PageRetriever(url, maxPagesToParse);

		PageParser parser = new PageParser(pageRetriever, maxPagesToParse);
		Page page;

		do {
			pageRetriever.retrieve();
			page = pageRetriever.next();

			if (page != null) {
				parser.parse(page);
				pageRetriever.retrieve();
			}

		} while (pageRetriever.hasNext());
			
		parseObjects = parser.getParseObjects();
		analyzer.analyze();

		System.out.println(generateString(sum));
	}

	private static void multi(int maxPagesToParse, String url, HashSet<String> keywords) {
		sum.setKeywords(keywords);
		Thread pageAnalyzer = new Thread(new PageAnalyzer());
		pageAnalyzer.start();
		
		//Create Page Retriever thread and start
	  PageRetriever pageRetriever = new PageRetriever(url, maxPagesToParse);
	  pageRetriever.start();
	  
	  PageAnalyzer analyzer = new PageAnalyzer();
    PageParser parser = new PageParser(pageRetriever, maxPagesToParse);
    Page page;
		  
		try {
	        Thread.sleep(3000); //Temp sleep to give thread time to work
	      } catch (InterruptedException e) {}
		
		page = pageRetriever.next();
		
		if( page != null) {
		      parser.parse(page);
		}
			
		parseObjects = parser.getParseObjects();
		
		
		try {
			pageAnalyzer.join();
		} catch (InterruptedException e) {}
		
		System.out.println(generateString(sum));
	}

	private static String generateString(SummaryObject summary) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nUnique pages retrieved: ");
		sb.append(summary.getPagesAnalyzed());
		sb.append("\nAverage words per page: ");
		sb.append(summary.getTotalWords() / summary.getPagesAnalyzed());
		sb.append("\nAverage keywords per page: ");
		sb.append(summary.getTotalKeywords());
		sb.append("\nAverage URLs per page: ");
		sb.append(summary.getTotalURLs() / summary.getPagesAnalyzed());
		sb.append("\n\nKeywords\tAvg. hits per page\t    Total hits\n");
		
		Iterator<String> keyItr = summary.getKeywords().keySet().iterator();
		String next;
		double num;
		while (keyItr.hasNext()) {
			next = keyItr.next();
			if (next.length() >= 8) {
				sb.append(next + "\t\t");
			} else {
				sb.append(next + "\t\t\t");
			}
			
			num = (double) summary.getKeywords().get(next) / summary.getPagesAnalyzed();
			DecimalFormat df = new DecimalFormat("#.##");
			sb.append(df.format(num));
			
			sb.append("\t\t\t" + summary.getKeywords().get(next) + "\n");
		}
		
		sb.append("\nAverage parse time per page: ");
		sb.append(summary.getTotalPageParseTime() / summary.getPagesAnalyzed());
		sb.append(" ms");
		
		return sb.toString();
	}
}