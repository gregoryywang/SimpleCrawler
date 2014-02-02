package edu.uw.tcss422.util;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * PageRetriever retrieves html documents and generates Page objects.
 * @author Roshun Jones
 * @version 1.0
 *
 */
public class PageRetriever extends Thread {
 
  /**
   * @param mPendingLinks collection to store pending links
   */
  private ArrayDeque<String> mPendingLinks = new ArrayDeque<String>();
  
  /**
   * @param mAvailPags collection to store retrieved pages.
   */
  private ArrayDeque<Page> mAvailPages = new ArrayDeque<Page>();
  
  /**
   * @param mPageRepos repository for storing retrieved pages.
   */
  private HashMap<String, Page> mPageRepos = new HashMap<String, Page>();
  
  /**
   * @param mMaxPages The maximum number of pages to be retrieved.
   */
  private final int mMaxPages;
  
  /**
   * @param mRetrievedCount The current number of pages retrieved.
   */
  private int mRetrievedCount = 0;
  
  
  /**
   * Single arg constructor.
   * @param aURL initial url to retrieve. 
   */
  public PageRetriever(String aURL, int aMaxPages) {
    if(!aURL.isEmpty())
      //Add initial link to queue to retrieve
      mPendingLinks.add(aURL);
    
    mMaxPages = aMaxPages;
  }
  
  /**
   * Adds a url to the pending list to be retrieved.
   * @param aURL The URL to add to the pending list.
   */
  public synchronized void addURL(String aURL) {
    //Add additional link to queue to retrieve
   if(!aURL.isEmpty()) //No null urls
     mPendingLinks.add(aURL);
  } //End addURL()
  
  /**
   * Returns true if there are retrieved pages available.
   * @return true if there are retrieved pages available, false otherwise.
   */
  public synchronized boolean hasNext() {
    return !mAvailPages.isEmpty();
  } //End hasNext()
  
  /**
   * Retruns the next available retrieved Page object.
   * @return the next available retrieved Page object.
   */
  public synchronized Page next() {
    if( !mAvailPages.isEmpty() ) 
      return mAvailPages.remove();
    
    return null; 
  } //End next()
  
  /**
   * Retrieves HTML content and generates Page objects of all URLS in pending queue.
   */
  public synchronized void retrieve() {
    
    while( !mPendingLinks.isEmpty() && (mRetrievedCount < mMaxPages) ) {
      final String url = mPendingLinks.remove(); //Get next url
      
      //Check the repository first. If the Page has been retrieved before, return it; else retrieve the page and store for future.
      if(!mPageRepos.containsKey(url)) { 
        try {
          //Connect to page and create page object
          Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
          String content = doc.html();
          Page page = new Page(url, content);
          mAvailPages.add(page); //add retrieved page to avail queue
          mPageRepos.put(url, page); //add page to repos
          mRetrievedCount++;
          
        } catch( IOException e ) {
        	//System.err.println(e.toString()); Only use for debugging
        }
      }
    }
  }// End retrieve()
  
  /**
   * Checks whether links queue is empty.
   */
  private synchronized boolean linksQueueIsEmpty() {
    return mPendingLinks.isEmpty();
  }
  
  /**
   * Gets the current retrieved count.
   * @return Returns the current retrieved page count.
   */
  public int getRetrievedCount() {
    return mRetrievedCount;
  }
  
  /**
   * Main loop for PageRetriever loop.
   */
  public void run() {
    while( true ) {
      if( !linksQueueIsEmpty() )
          retrieve(); //
    }
  }//End run()
}
