package edu.uw.tcss422.util;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageRetriever {
 
  //Create collections to store pending links
  private ArrayDeque<String> mPendingLinks = new ArrayDeque<String>();
  
  //Create collections to store retrieved pages
  private ArrayDeque<Page> mAvailPages = new ArrayDeque<Page>();
  
  //Page repository
  private HashMap<String, Page> mPageRepos = new HashMap<String, Page>();
  
  
  public PageRetriever(String aURL) {
    if(!aURL.isEmpty())
      //Add initial link to queue to retrieve
      mPendingLinks.add(aURL);
  }
  
  public void addURL(String aURL) {
    //Add additional link to queue to retrieve
   if(!aURL.isEmpty()) //No null urls
     mPendingLinks.add(aURL);
  }
  
  public boolean hasNext() {
    return !mAvailPages.isEmpty();
  }
  
  public Page next() {
    if( !mAvailPages.isEmpty() ) 
      return mAvailPages.remove();
    
    return null; 
  }
  
  public void retrieve() {
    
    while( !mPendingLinks.isEmpty() ) {
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
        } catch( IOException e ) {
        	//System.err.println(e.toString()); Only use for debugging
        }
      }
    }
  }
}
