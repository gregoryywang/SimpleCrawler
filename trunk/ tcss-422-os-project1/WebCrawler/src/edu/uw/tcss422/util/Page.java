package edu.uw.tcss422.util;

public class Page {
  private String mURL;
  private String mContent;
  
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
  
}
