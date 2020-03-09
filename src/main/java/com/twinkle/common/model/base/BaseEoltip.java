package com.twinkle.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseEoltip<M extends BaseEoltip<M>> extends Model<M> implements IBean {

	public M setId(Integer id) {
		set("id", id);
		return (M)this;
	}

	public Integer getId() {
		return getInt("id");
	}

	public M setUserid(Integer userid) {
		set("userid", userid);
		return (M)this;
	}

	public Integer getUserid() {
		return getInt("userid");
	}

	public M setSubject(String subject) {
		set("subject", subject);
		return (M)this;
	}

	public String getSubject() {
		return getStr("subject");
	}

	public M setTitle(String title) {
		set("title", title);
		return (M)this;
	}

	public String getTitle() {
		return getStr("title");
	}

	public M setAbort(String abort) {
		set("abort", abort);
		return (M)this;
	}

	public String getAbort() {
		return getStr("abort");
	}

	public M setUrl(String url) {
		set("url", url);
		return (M)this;
	}

	public String getUrl() {
		return getStr("url");
	}

	public M setDates(java.util.Date dates) {
		set("dates", dates);
		return (M)this;
	}
	
	public java.util.Date getDates() {
		return get("dates");
	}

}