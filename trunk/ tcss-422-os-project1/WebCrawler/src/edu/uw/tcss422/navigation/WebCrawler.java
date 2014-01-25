package edu.uw.tcss422.navigation;

import edu.uw.tcss422.util.Page;
import edu.uw.tcss422.util.PageRetriever;

public class WebCrawler {

  /**
   * @param args Command line arguments passed by user
   */
  public static void main(String[] args) {
    PageRetriever pageRetriever = new PageRetriever("http://www.aol.com");
    
    do {
      pageRetriever.retrieve();
      Page page = pageRetriever.next();
      System.out.println(page.getContent());
    
    } while(pageRetriever.hasNext());
  }

}
