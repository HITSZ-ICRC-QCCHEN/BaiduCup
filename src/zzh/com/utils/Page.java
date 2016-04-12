package zzh.com.utils;

public class Page {
	
	public Page(String title, String body) {
		this.setTitle(title);
		this.setBody(body);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	private String title;
	private String body;
}