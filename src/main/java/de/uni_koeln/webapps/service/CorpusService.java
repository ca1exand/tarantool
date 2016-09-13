package de.uni_koeln.webapps.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import de.uni_koeln.webapps.data.Script;
import de.uni_koeln.webapps.utils.XMLScriptConverter;

@Service
public class CorpusService {

	private String fileName = "src/main/resources/";

	private List<Script> scripts = new ArrayList<>();

	public CorpusService() {
		init();
	}

	public void init() {
		/* Datei einlesen ... */

		for (File file : new File(fileName).listFiles()) {
			if (file.getPath().contains(".xml") && file.canRead()) {
				scripts.add(XMLScriptConverter.convert(file.getAbsolutePath()));
			}
		}

	}

	public int random(int num) {
		Random random = new Random();
		return random.nextInt(num);
	}

	public List<Script> getScripts() {
		return scripts;
	}

	public Script getScriptById(int id) {
		for (Script script : scripts) {
			if (script.getId() == id) {
				return script;
			}
		}
		return null;
	}
	

}
