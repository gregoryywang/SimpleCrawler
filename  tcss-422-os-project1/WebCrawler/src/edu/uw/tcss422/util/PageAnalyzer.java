package edu.uw.tcss422.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jsoup.select.Elements;

/**
 * Takes a collection of keywords and searches ParseObjects for those keywords.
 * Also keeps metrics on the pages search such as number of words and number
 * of URLs that can be displayed upon program completion.
 */
public class PageAnalyzer {
	
	public PageAnalyzer(HashSet<String> words) {
		for (int i = 0; i < words.size(); i++) {
			keywords.put(words.toArray()[i].toString(), 0);
		}
	}
	
	private HashMap<String, Integer> keywords = new HashMap<String, Integer>();
	private int pagesAnalyzed = 0;
	private int totalURLs = 0;
	private long totalWords = 0;
	private long totalPageParseTime = 0;

	/**
	 * Analyzes the incoming parseObjects.
	 * @param parseObjCol The collection of parseObjects
	 */
	public void analyze(Collection<ParseObject> parseObjCol) {
		pagesAnalyzed++;
		String next;
		ParseObject parsed;
		Iterator<ParseObject> parserItr = parseObjCol.iterator();
		
		while (parserItr.hasNext()) {
			parsed = parserItr.next();
			countURLs(parsed.getLinks());
			countWords(parsed.getWords());
			Iterator<String> keyItr = keywords.keySet().iterator();
			while (keyItr.hasNext()) {
				next = keyItr.next();
				keywords.put(next, findKeywordCount(keywords.get(next), next.toLowerCase(), parsed.getWords()));
			}
			addParseTime(parsed.getParseTime());
		}
		
	}
	
	/**
	 * Builds the output string.
	 * @return Output string
	 */
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nPages retrieved: ");
		sb.append(pagesAnalyzed);
		sb.append("\nAverage words per page: ");
		sb.append(totalWords / pagesAnalyzed);
		sb.append("\nAverage keywords per page: ");
		sb.append(getTotalKeywords());
		sb.append("\nAverage URLs per page: ");
		sb.append(totalURLs / pagesAnalyzed);
		sb.append("\n\nKeywords\tAvg. hits per page\t    Total hits\n");
		
		Iterator<String> keyItr = keywords.keySet().iterator();
		String next;
		while (keyItr.hasNext()) {
			next = keyItr.next();
			if (next.length() >= 8) {
				sb.append(next + "\t\t" + String.format("%.2f", (double) keywords.get(next) / pagesAnalyzed)
						+ "\t\t\t" + keywords.get(next) + "\n");
			} else {
				sb.append(next + "\t\t\t" + String.format("%.2f", (double) keywords.get(next) / pagesAnalyzed)
					+ "\t\t\t" + keywords.get(next) + "\n");
			}
		}
		
		sb.append("\nAverage parse time per page: ");
		sb.append(totalPageParseTime / pagesAnalyzed);
		sb.append(" ms");
		
		return sb.toString();
	}
	
	/**
	 * Adds the parse time for each ParseObject to the total parse time.
	 * @param parseTime Amount of time taken to parse a single ParseObject
	 */
	private void addParseTime(long parseTime) {
		totalPageParseTime += parseTime;
	}
	
	/**
	 * Adds the number of URLs in a single ParseObject to the running total. 
	 * @param urls The collection of URLs found in a single ParseObject
	 */
	private void countURLs(Elements urls) {
		totalURLs += urls.size();
	}
	
	/**
	 * Counts the number of words in a single ParseObject and adds it to the running total.
	 * @param collection The collection of strings in a single ParseObject
	 */
	private void countWords(Collection<String> collection) {
		totalWords += collection.size();
	}
	
	/**
	 * Finds the total number of keywords found in all pages parsed.
	 * @return The total number of keywords found
	 */
	private int getTotalKeywords() {
		int totalKeywords = 0;
		Iterator<String> itr = keywords.keySet().iterator();
		while (itr.hasNext()) {
			totalKeywords += keywords.get(itr.next());
		}
		return totalKeywords;
	}
	
	/**
	 * Finds the number of times a specific keyword is found for a single ParseObject.
	 * @param currentCount Current number of instances (from previous pages)
	 * @param keyword The keyword being searched for
	 * @param collection The collection of tokenized strings for a single ParseObject
	 * @return The number of times the keyword has been found (previous and current)
	 */
	private int findKeywordCount(Integer currentCount, String keyword, Collection<String> collection) {
		int newCount = currentCount + Collections.frequency(collection, keyword);
		return newCount;
	}

}
