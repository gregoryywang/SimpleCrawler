package edu.uw.tcss422.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SummaryObject {
	private int pagesAnalyzed = 0;
	private int totalURLs = 0;
	private long totalWords = 0;
	private long totalPageParseTime = 0;
	private HashMap<String, Integer> keywords = new HashMap<String, Integer>();

	/**
	 * @return the pagesAnalyzed
	 */
	public int getPagesAnalyzed() {
		return pagesAnalyzed;
	}

	/**
	 * @param pagesAnalyzed the pagesAnalyzed to set
	 */
	public void setPagesAnalyzed(int pagesAnalyzed) {
		this.pagesAnalyzed = pagesAnalyzed;
	}

	/**
	 * @return the totalURLs
	 */
	public int getTotalURLs() {
		return totalURLs;
	}

	/**
	 * @param totalURLs the totalURLs to set
	 */
	public void setTotalURLs(int totalURLs) {
		this.totalURLs = totalURLs;
	}

	/**
	 * @return the totalWords
	 */
	public long getTotalWords() {
		return totalWords;
	}

	/**
	 * @param totalWords the totalWords to set
	 */
	public void setTotalWords(long totalWords) {
		this.totalWords = totalWords;
	}

	/**
	 * @return the totalPageParseTime
	 */
	public long getTotalPageParseTime() {
		return totalPageParseTime;
	}

	/**
	 * @param totalPageParseTime the totalPageParseTime to set
	 */
	public void setTotalPageParseTime(long totalPageParseTime) {
		this.totalPageParseTime = totalPageParseTime;
	}

	/**
	 * @return the keywords
	 */
	public HashMap<String, Integer> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(HashSet<String> words) {
		for (String word : words) {
			this.keywords.put(word, 0);
		}
	}
	
	
	public void updateKeyword(String key, Integer value) {
		keywords.put(key, value);
	}
	
	/**
	 * Finds the total number of keywords found in all pages parsed.
	 * @return the total number of keywords found
	 */
	public int getTotalKeywords() {
		int totalKeywords = 0;
		Iterator<String> itr = keywords.keySet().iterator();
		while (itr.hasNext()) {
			totalKeywords += keywords.get(itr.next());
		}
		return totalKeywords;
	}
}
