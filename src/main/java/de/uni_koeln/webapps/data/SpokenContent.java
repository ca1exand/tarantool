package de.uni_koeln.webapps.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;


public class SpokenContent {

	private String id;
	private String content;
	private String speaker;
	private String color;
	private String title;
	
	@XmlValue
	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	@XmlAttribute
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	
	public String getSpeaker(){
		return speaker;
	}
	
	@XmlTransient
	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
	
	@XmlAttribute(name = "ID")
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@XmlTransient
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle(){
		return title;
	}
	
}
