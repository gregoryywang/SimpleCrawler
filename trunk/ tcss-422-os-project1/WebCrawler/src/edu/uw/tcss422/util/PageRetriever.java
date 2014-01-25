package edu.uw.tcss422.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageRetriever {
 
  //Create collections to store pending links
  private LinkedHashSet<String> mPendingLinks = new LinkedHashSet<String>();
  private Iterator<String> mPendingLinksIt = null;
  
  //Create collections to store retrieved pages
  private LinkedHashSet<Page> mAvailPages = new LinkedHashSet<Page>();
  private Iterator<Page> mAvailPagesIt = mAvailPages.iterator();
  
  public PageRetriever(String aURL) {
    //Add initial link to queue to retrieve
    mPendingLinks.add(aURL);
    mPendingLinksIt = mPendingLinks.iterator();
  }
  
  public void addURL(String aURL) {
    //Add additional link to queue to retrieve
    mPendingLinks.add(aURL);
  }
  
  public boolean hasNext() {
    return mAvailPagesIt.hasNext();
  }
  
  public Page next() {
    return mAvailPagesIt.next();
  }
  
  public void retrieve() {
    while(mPendingLinksIt.hasNext()) {
      final String url = mPendingLinksIt.next(); //Get next url
      
      if( url == null || url.isEmpty())
        return;
      
      try {
        //Connect to page and create page object
        Document doc = Jsoup.connect(url).get();
        String content = doc.html();
        System.out.println(content);
        //mAvailPages.add(new Page(url, content));
      } catch( IOException e ) {}
    }
  }
}
