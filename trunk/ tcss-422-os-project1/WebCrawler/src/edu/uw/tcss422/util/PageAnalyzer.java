package edu.uw.tcss422.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jsoup.select.Elements;


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

	public void analyze(Collection<ParseObject> parseObjCol) {
		pagesAnalyzed++;
		String next;
		ParseObject parsed = null;
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
	
	private void addParseTime(long parseTime) {
		totalPageParseTime += parseTime;
	}
	
	private void countURLs(Elements urls) {
		totalURLs += urls.size();
	}
	
	private void countWords(Collection<String> collection) {
		totalWords += collection.size();
	}
	
	private int getTotalKeywords() {
		int totalKeywords = 0;
		Iterator<String> itr = keywords.keySet().iterator();
		while (itr.hasNext()) {
			totalKeywords += keywords.get(itr.next());
		}
		return totalKeywords;
	}
	
	private int findKeywordCount(Integer currentCount, String keyword, Collection<String> collection) {
		int newCount = currentCount + Collections.frequency(collection, keyword);
		return newCount;
	}

}
