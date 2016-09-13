package de.uni_koeln.webapps.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.uni_koeln.webapps.utils.ColorChooser;

@XmlRootElement(name = "script")
public class Script {

	private ColorChooser cc = new ColorChooser();
	int speakercount;
	int id;
	private String title;
	private String author;
	private int runtime;
	private int year;
	private List<SpokenContent> inhalt = new ArrayList<>();
	private Set<String> speakerNames = new HashSet<>();

	// Map beinhaltet die ID als Key und als Value den SpeakerNamen und den
	// gesprochenen Text

	@XmlAttribute(name = "year")
	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	@XmlAttribute
	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public int getRuntime() {
		return runtime;
	}

	@XmlAttribute
	public void setauthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	@XmlAttribute
	public void setTitle(String title) {
		this.title = title;
		id = Math.abs(title.hashCode());
	}

	public String getTitle() {
		return title;
	}

	public int getId() {
		return id;
	}

	@XmlElement
	public void setContent(SpokenContent[] content) {
		for (SpokenContent spokenContent : content) {
			spokenContent.setTitle(title);
			spokenContent.setId(Math.abs(title.hashCode()) + "-" + spokenContent.getId());
			inhalt.add(spokenContent);
			String currentSpeakerString = spokenContent.getSpeaker();
			String speakerName = spokenContent.getSpeaker().replaceAll("'S VOICE", "").replace("THE", "");
			if (speakerName.matches(".+\\(.+")) {
				currentSpeakerString = speakerName.split("\\(")[0];
			}
			speakerNames.add(currentSpeakerString);
		}
		for (String name : speakerNames) {
			int[] intArray = cc.getColor();
			String color = intArray[0] + "," + intArray[1] + "," + intArray[2] + ", 0.3";
			for (SpokenContent sc : inhalt) {
				if (sc.getSpeaker().contains(name)) {
					sc.setColor(color);
				}
			}
		}
	}

	public List<SpokenContent> getSpokenContent() {
		return inhalt;
	}

	@Override
	public String toString() {
		return "Script [title=" + title + ", author=" + author + ", runtime=" + runtime + ", year=" + year + " "
				+ inhalt.size() + " Textfelder id: " + id + " Anzahl der Sprecher: " + speakerNames.size() + " ]";
	}

	public Set<String> getSpeakerNames() {
		return speakerNames;
	}

}
