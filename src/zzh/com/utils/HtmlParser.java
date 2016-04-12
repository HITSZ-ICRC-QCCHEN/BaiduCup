package zzh.com.utils;

import java.io.File;
import java.io.IOException;

//import org.htmlparser.Node;
//import org.htmlparser.Parser;
//import org.htmlparser.nodes.TextNode;
//import org.htmlparser.util.NodeList;
//import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

	public static Page parseHtml(String url, String path) {
		assert (url != null);
		String baseUrl = parseBaseUrl(url);
		File htmlFile = new File(path);
		try {
			Document doc = Jsoup.parse(htmlFile, null, baseUrl);
			String title = doc.title();
			Element bodyElem = doc.body();
			if (bodyElem != null) {
				String body = bodyElem.text();
				return new Page(title, body);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Page parseHtml(String url, File htmlFile) {
		assert (url != null);
		String baseUrl = parseBaseUrl(url);
		try {
			Document doc = Jsoup.parse(htmlFile, null, baseUrl);
			String title = doc.title();
			Element bodyElem = doc.body();
			if (bodyElem != null) {
				String body = bodyElem.text();
				return new Page(title, body);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Page parseHtml(String text) {
		Document doc = Jsoup.parse(text);
		String title = doc.title();
		Element bodyElem = doc.body();
		if (bodyElem != null) {
			String body = bodyElem.text();
			return new Page(title, body);
		}
		return null;
	}
	
	// 优化的解析方法
	public static Page parseHtmlOpt(String url, String path) {
		assert (url != null);
		String baseUrl = parseBaseUrl(url);
		File htmlFile = new File(path);
		try {
			Document doc = Jsoup.parse(htmlFile, null, baseUrl);
			String title = doc.title();
			String body = doc.getElementsByTag("p").text();
			return new Page(title, body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Page parseHtmlOpt(String url, File htmlFile) {
		assert (url != null);
		String baseUrl = parseBaseUrl(url);
		try {
			Document doc = Jsoup.parse(htmlFile, null, baseUrl);
			String title = doc.title();
			String body = doc.getElementsByTag("p").text();
			return new Page(title, body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Page parseHtmlOpt(String text) {
		Document doc = Jsoup.parse(text);
		String title = doc.title();
		String body = doc.getElementsByTag("p").text();
		return new Page(title, body);
	}

	private static String parseBaseUrl(String url) {
		int index = url.indexOf("/", 8);
		if (index != -1)
			return url.substring(0, index + 1);
		return url;
	}
	
	public static PageExt parseHtmlExt(String url, String path) {
		assert (url != null);
		String baseUrl = parseBaseUrl(url);
		File htmlFile = new File(path);
		try {
			Document doc = Jsoup.parse(htmlFile, null, baseUrl);
			String title = doc.title();
			String keywords = "";
			String description = "";
			Elements metaElements = doc.getElementsByTag("meta");
			for (Element element : metaElements) {
				if (element.attr("name").equals("keywords")) {
					keywords += element.attr("content");
				}
				if (element.attr("name").equals("description")) {
					description += element.attr("content");
				}
			}
			String body = doc.getElementsByTag("p").text();
//			Elements divElements = doc.getElementsByTag("div");
//			for (Element element : divElements) {
//				if (element.ownText().length() > 50) {
//					body += element.ownText();
//				}
//			}
			if (body.equals("")) 
				return null;
			else 
				return new PageExt(title, keywords, description, body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String baseUrl = "http://www.cnblogs.com/";
		String path = "D:\\Temp\\luceneData\\html\\d00000\\f00000";
		Page page = HtmlParser.parseHtml(baseUrl, path);
		System.out.println(page.getBody());
		PageExt pageExt = HtmlParser.parseHtmlExt(baseUrl, path);
		System.out.println(pageExt.getKeywords());
		System.out.println(pageExt.getBody());
	}
}
