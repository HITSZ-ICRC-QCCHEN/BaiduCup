package com.zzh.util;

public class QueryDoc {
	
	public QueryDoc(String title, String body) {
		this.title = title;
		this.content = body;
	}

	private String title;
	private String content;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
