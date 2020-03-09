package com.twinkle.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BasePost<M extends BasePost<M>> extends Model<M> implements IBean {

	public M setId(Integer id) {
		set("id", id);
		return (M)this;
	}

	public Integer getId() {
		return getInt("id");
	}

	public M setTitle(String title) {
		set("title", title);
		return (M)this;
	}

	public String getTitle() {
		return getStr("title");
	}

	public M setAuthor(String author) {
		set("author", author);
		return (M)this;
	}

	public String getAuthor() {
		return getStr("author");
	}

	public M setContent(String content) {
		set("content", content);
		return (M)this;
	}

	public String getContent() {
		return getStr("content");
	}

	public M setPic(String pic) {
		set("pic", pic);
		return (M)this;
	}

	public String getPic() {
		return getStr("pic");
	}

	public M setTopic(String topic) {
		set("topic", topic);
		return (M)this;
	}

	public String getTopic() {
		return getStr("topic");
	}

	public M setPlace(String place) {
		set("place", place);
		return (M)this;
	}

	public String getPlace() {
		return getStr("place");
	}

	public M setUrl(String url) {
		set("url", url);
		return (M)this;
	}

	public String getUrl() {
		return getStr("url");
	}

	public M setHot(String hot) {
		set("hot", hot);
		return (M)this;
	}

	public String getHot() {
		return getStr("hot");
	}

	public M setTypes(String types) {
		set("types", types);
		return (M)this;
	}

	public String getTypes() {
		return getStr("types");
	}

	public M setNewsdate(String newsdate) {
		set("newsdate", newsdate);
		return (M)this;
	}

	public String getNewsdate() {
		return getStr("newsdate");
	}

	public M setDates(java.util.Date dates) {
		set("dates", dates);
		return (M)this;
	}
	
	public java.util.Date getDates() {
		return get("dates");
	}

}