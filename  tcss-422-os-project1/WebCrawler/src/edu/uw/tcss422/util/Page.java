package edu.uw.tcss422.util;

import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.select.Elements;

public class Page {
  private String mURL;
  private String mContent;

  /**
   * A collection of URLs parsed from the this page by the PageRetriver.
   */
  private Elements links;

  /**
   * 	A collection of words parsed from this page by the PageRetriver.
   */
  private Collection<String> words = new ArrayList<String>();
  
  
  public Page(final String aURL, final String aContent) {
    mURL = aURL;
    mContent = aContent;
  }

  /**
   * @return the mURL
   */
  public String getURL() {
    return mURL;
  }

  /**
   * @param mURL the mURL to set
   */
  public void setURL(String mURL) {
    this.mURL = mURL;
  }

  /**
   * @return the mContent
   */
  public String getContent() {
    return mContent;
  }

  /**
   * @param mContent the mContent to set
   */
  public void setContent(String mContent) {
    this.mContent = mContent;
  }

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
