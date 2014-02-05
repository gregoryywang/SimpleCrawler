package edu.uw.tcss422.navigation;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import edu.uw.tcss422.util.Page;
import edu.uw.tcss422.util.PageAnalyzer;
import edu.uw.tcss422.util.PageParser;
import edu.uw.tcss422.util.PageRetriever;
import edu.uw.tcss422.util.SummaryObject;

/**
 * A web crawler capable of using multiple threads to parse web pages in search of specific keywords.
 * @authors Oscar Hong, Roshun Jones, Trygve Stageberg, Yong Wang
 */
public class WebCrawler {

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

	/**
	 * Single-threaded implementation.
	 * @param maxPagesToParse max number of pages to parse before breaking out
	 * @param url starting url
	 * @param keywords HashSet containing the user-specified keywords to search for
	 */
	private static void single(int maxPagesToParse, String url, HashSet<String> keywords) {
		PageAnalyzer analyzer = new PageAnalyzer(keywords);

		PageRetriever pageRetriever = new PageRetriever(url, maxPagesToParse);

		PageParser parser = new PageParser(pageRetriever);
		Page page;

		do {
			pageRetriever.retrieve();
			page = pageRetriever.next();

			if (page != null) {
				parser.parse(page);
				pageRetriever.retrieve();
			}

		} while (pageRetriever.hasNext());

		analyzer.analyze();

		SummaryObject sum = analyzer.getSummary();
		sum.setPagesAnalyzed(pageRetriever.getRetrievedCount());
		System.out.println(generateString(sum));
	}

	/**
	 * Multi-threaded implementation.
	 * @param maxPagesToParse max number of pages to parse before breaking out
	 * @param url starting url
	 * @param keywords HashSet containing the user-specified keywords to search for
	 */
	private static void multi(int maxPagesToParse, String url, HashSet<String> keywords) {
		//Create PageAnalyzer thread and start
		PageAnalyzer pageAnalyzer = new PageAnalyzer(keywords);
		pageAnalyzer.start();
		
		//Create Page Retriever thread and start
		PageRetriever pageRetriever = new PageRetriever(url, maxPagesToParse);
		pageRetriever.start();

		PageParser parser = new PageParser(pageRetriever);
		parser.start();
		Page page;
		  
		page = pageRetriever.next();

		do {
			if (page != null) {
				parser.parse(page);
				page = pageRetriever.next();
			}
		} while (pageRetriever.hasNext());
		
//		Used to make sure the Analyzer has enough time to finish up.
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {}
		
		SummaryObject sum = pageAnalyzer.getSummary();
		sum.setPagesAnalyzed(pageRetriever.getRetrievedCount());
		System.out.println(generateString(sum));
		
		//Stop threads
		pageRetriever.terminate();
		pageAnalyzer.terminate();
		parser.terminate();
	}

	/**
	 * Takes the SummaryObject and formats everything nicely
	 * @param summary SummaryObject containing stats
	 * @return Nicely formatted string with all stats
	 */
	private static String generateString(SummaryObject summary) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nUnique pages retrieved: ");
		sb.append(summary.getPagesAnalyzed());
		sb.append("\nAverage words per page: ");
		sb.append(summary.getTotalWords() / summary.getPagesAnalyzed());
		sb.append("\nAverage keywords per page: ");
		sb.append(summary.getTotalKeywords() / summary.getPagesAnalyzed());
		sb.append("\nAverage URLs per page: ");
		sb.append(summary.getTotalURLs() / summary.getPagesAnalyzed());
		sb.append("\n\nKeywords\tAvg. hits per page\t    Total hits\n");
		
		// Keyword occurrences 
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