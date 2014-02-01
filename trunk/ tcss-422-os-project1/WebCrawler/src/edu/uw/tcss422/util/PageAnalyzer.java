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
		for (String word : words) {
			keywords.put(word, 0);
		}
		sum.setKeywords(keywords);
	}
	
	private HashMap<String, Integer> keywords = new HashMap<String, Integer>();
	SummaryObject sum = new SummaryObject();
	

	/**
	 * Analyzes the incoming parseObjects.
	 * @param parseObjCol The collection of parseObjects
	 */
	public void analyze(Collection<ParseObject> parseObjCol) {
		sum.incrementPageCounter();
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
	 * 
	 * @return
	 */
	public SummaryObject getSummary() {
		return sum;
	}
	
	/**
	 * Adds the parse time for each ParseObject to the total parse time.
	 * @param parseTime Amount of time taken to parse a single ParseObject
	 */
	private void addParseTime(long parseTime) {
		sum.setTotalPageParseTime(sum.getTotalPageParseTime() + parseTime);
	}
	
	/**
	 * Adds the number of URLs in a single ParseObject to the running total. 
	 * @param urls The collection of URLs found in a single ParseObject
	 */
	private void countURLs(Elements urls) {
		sum.setTotalURLs(sum.getTotalURLs() + urls.size());
	}
	
	/**
	 * Counts the number of words in a single ParseObject and adds it to the running total.
	 * @param collection The collection of strings in a single ParseObject
	 */
	private void countWords(Collection<String> collection) {
		sum.setTotalWords(sum.getTotalWords() + collection.size());
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

	/**
	 * Finds the number of pages analyzed for limiting purposes.
	 * @return The number of pages analyzed so far.
	 */
	public int getPagesAnalyzed() {
		return sum.getPagesAnalyzed();
	}

}