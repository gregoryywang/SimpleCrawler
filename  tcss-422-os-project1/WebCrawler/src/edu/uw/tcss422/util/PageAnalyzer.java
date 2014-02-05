package edu.uw.tcss422.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.jsoup.select.Elements;

/**
 * Takes a collection of keywords and searches ParseObjects for those keywords.
 * Also keeps metrics on the pages search such as number of words and number
 * of URLs that can be displayed upon program completion.
 */
public class PageAnalyzer extends Thread{
	
	/**
	 * SummaryObject that keeps track of all the stats for the analyzed pages.
	 */
	private static volatile SummaryObject sum = new SummaryObject();

	/**
	 * mRunning Indicates whether the thread is running.
	 */
	private volatile boolean mRunning = true;

	public PageAnalyzer(HashSet<String> keywords) {
		sum.setKeywords(keywords);
	}
	
	/**
	 * Analyzes the incoming parseObjects.
	 * @param parseObjCol The collection of parseObjects
	 */
	public void analyze() {
		String next;
		ParseObject parsed;
		Collection<ParseObject> parseObjCol = new ArrayList<ParseObject>();
		while (PageParser.hasParseObject()) {
			parseObjCol.add(PageParser.getParseObject());
		}
		Iterator<ParseObject> parserItr = parseObjCol.iterator();
		
		while (parserItr.hasNext()) {
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
	 * Return the SummaryObject.
	 * @return The SummaryObject containing all the stats for the parsed pages
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
	 * Terminates the thread.
	 */
	public void terminate() {
		mRunning = false;
	}

	@Override
	public void run() {
		while (mRunning) {
			analyze();
		}
	}
}