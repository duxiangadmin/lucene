package com.itlaiba.lucene.entity;

/**
 * 文章实体
 * @author 杜翔
 * @版本 V 1.0 
 * @date 2018年6月20日 上午1:01:50
 */
public class Article {
	private Integer id;
	private String title;
	private String content;
	public Article() {
		super();
	}
	public Article(Integer id, String title, String content) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
