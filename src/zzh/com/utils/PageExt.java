package zzh.com.utils;

public class PageExt {
	
	private String title;
	private String keywords;
	private String description;
	private String body;
	
	public PageExt(String title, String keywords, String description, String body) {
		this.setTitle(title);
		this.setKeywords(keywords);
		this.setDescription(description);
		this.setBody(body);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
