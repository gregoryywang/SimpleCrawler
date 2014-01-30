package edu.uw.tcss422.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PageRetrieverTest {

	@Test
	public void pageRetrievingTest() {
		String url = "http://www.oscarhong.com/wtest/";
		String imageUrl = "http://www.oscarhong.com/wtest/3/Don%27t%20download%20me.png";
		PageRetriever pageretriever = new PageRetriever(url);
		assertFalse("The page retriever should not contain anything until it retrieves stuff", pageretriever.hasNext());
		pageretriever.retrieve();
		assertTrue("The page retriever should contain a page after retrieving", pageretriever.hasNext());
		Page nextPage = pageretriever.next();
		assertEquals("The url of the page should equals to the original url", nextPage.getURL(), url);
		pageretriever.addURL(imageUrl);
		pageretriever.retrieve();
		assertNull("The page retriever should skip non-HTML files", pageretriever.next());
		pageretriever.addURL(url);
		pageretriever.addURL(url);
		pageretriever.retrieve();
		nextPage = pageretriever.next();
		assertNull("The page retriever should not retrieve duplicated urls", pageretriever.next());
		pageretriever.retrieve();
		assertNull("The page retriever should not raise any error for retrieving without an URL", pageretriever.next());
		pageretriever.addURL("");
		pageretriever.retrieve();
		assertNull("The page retriever should not raise any error for retrieving without an empty string", pageretriever.next());
		PageRetriever emptyRetriever = new PageRetriever("");
		assertNull("The page retriever should not raise any error for retrieving without an empty string", emptyRetriever.next());

	}

}
