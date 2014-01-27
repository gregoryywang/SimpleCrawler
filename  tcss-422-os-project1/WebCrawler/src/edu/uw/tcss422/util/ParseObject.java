package edu.uw.tcss422.util;

import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.select.Elements;

public class ParseObject {
	
	  /**
	   * A collection of URLs parsed from the this page by the PageRetriver.
	   */
	  private Elements links;

	  /**
	   * 	A collection of words parsed from this page by the PageRetriver.
	   */
	  private Collection<String> words = new ArrayList<String>();
	
	  /**
	   * Sets the list of all links parsed from this page by the PageRetriver.
	   * @param a list of links
	   */
	  public void setLinks(Elements links) {
		  this.links = links;
	  }

	  /**
	   * Sets the list of all words parsed from this page by the PageRetriver.
	   * @param a list of words
	   */
	  public void setWords(Collection<String> words) {
		  this.words = words;
	  }
	  
	  /**
	   * Gets all links stored for this page.
	   * @return a list of links
	   */
	  public Elements getLinks() {
		  return links;
	  }

	  /**
	   * Gets all words stored for this page.
	   * @return a list of words
	   */
	  public Collection<String> getWords() {
		  return words;
	  }
}
