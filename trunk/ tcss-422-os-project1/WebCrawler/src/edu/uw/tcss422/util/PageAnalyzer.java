package edu.uw.tcss422.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.jsoup.select.Elements;

import edu.uw.tcss422.navigation.WebCrawler;

/**
 * Takes a collection of keywords and searches ParseObjects for those keywords.
 * Also keeps metrics on the pages search such as number of words and number
 * of URLs that can be displayed upon program completion.
 */
public class PageAnalyzer extends Thread{
	
	private SummaryObject sum;
	
	public PageAnalyzer() {
		this.sum = WebCrawler.sum;
	}
	
	/**
	 * Analyzes the incoming parseObjects.
	 * @param parseObjCol The collection of parseObjects
	 */
	public void analyze() {
		String next;
		ParseObject parsed;
		Collection<ParseObject> parseObjCol = WebCrawler.parseObjects;
		Iterator<ParseObject> parserItr = parseObjCol.iterator();
		
		while (parserItr.hasNext()) {
			sum.incrementPageCounter();
			parsed = parserItr.next();
			parserItr.remove();
			countURLs(parsed.getLinks());
			countWords(parsed.getWords());
			Iterator<String> keyItr = sum.getKeywords().keySet().iterator();
			while (keyItr.hasNext()) {
				next = keyItr.next();
				sum.updateKeyword(next, findKeywordCount(sum.getKeywords().get(next), next.toLowerCase(), parsed.getWords()));
			}
			addParseTime(parsed.getParseTime());
		}
		
	}

	/**
	 * 
	 * @return
	 */
	public synchronized SummaryObject getSummary() {
		return sum;
	}
	
	/**
	 * Adds the parse time for each ParseObject to the total parse time.
	 * @param parseTime Amount of time taken to parse a single ParseObject
	 */
	private synchronized void addParseTime(long parseTime) {
		sum.setTotalPageParseTime(sum.getTotalPageParseTime() + parseTime);
	}
	
	/**
	 * Adds the number of URLs in a single ParseObject to the running total. 
	 * @param urls The collection of URLs found in a single ParseObject
	 */
	private synchronized void countURLs(Elements urls) {
		sum.setTotalURLs(sum.getTotalURLs() + urls.size());
	}
	
	/**
	 * Counts the number of words in a single ParseObject and adds it to the running total.
	 * @param collection The collection of strings in a single ParseObject
	 */
	private synchronized void countWords(Collection<String> collection) {
		sum.setTotalWords(sum.getTotalWords() + collection.size());
	}
	
	/**
	 * Finds the number of times a specific keyword is found for a single ParseObject.
	 * @param currentCount Current number of instances (from previous pages)
	 * @param keyword The keyword being searched for
	 * @param collection The collection of tokenized strings for a single ParseObject
	 * @return The number of times the keyword has been found (previous and current)
	 */
	private synchronized int findKeywordCount(Integer currentCount, String keyword, Collection<String> collection) {
		int newCount = currentCount + Collections.frequency(collection, keyword);
		return newCount;
	}

	/**
	 * Finds the number of pages analyzed for limiting purposes.
	 * @return The number of pages analyzed so far.
	 */
	public synchronized int getPagesAnalyzed() {
		return sum.getPagesAnalyzed();
	}

	@Override
	public void run() {
		while (WebCrawler.parseObjects == null) {
			try {
				sleep(1000);	//Wait for parseObjects to be populated.
			} catch (InterruptedException e) {}
		}
		while (!WebCrawler.parseObjects.isEmpty()) {
			analyze();
			try {
				sleep(1000);
			} catch (InterruptedException e) {}
		}
	}

}
