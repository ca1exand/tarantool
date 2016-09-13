package de.uni_koeln.webapps.utils;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import de.uni_koeln.webapps.data.Script;

public class XMLScriptConverter {

	public static Script convert(String path) {
		try {
			JAXBContext context = JAXBContext.newInstance(Script.class);
			Unmarshaller un = context.createUnmarshaller();
			Script script = (Script) un.unmarshal(new File(path));
			return script;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
