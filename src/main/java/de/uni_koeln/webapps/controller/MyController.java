package de.uni_koeln.webapps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.uni_koeln.webapps.data.Script;
import de.uni_koeln.webapps.data.SpokenContent;
import de.uni_koeln.webapps.service.CorpusService;
@Controller

@RequestMapping(value = "/scripttool")
public class MyController {

	@Autowired
	CorpusService corpusService;

	@RequestMapping(value = {"", "/", "home"})
	public String index(){
		return "home";
	}

	@RequestMapping(value = "/script/{id}")
	public String work(@PathVariable("id") int id, Model model) {
		Script doc = corpusService.getScriptById(id);
		model.addAttribute("doc", doc);
		List<SpokenContent> docs = doc.getSpokenContent();
		model.addAttribute("docs", docs);
		return "singleScript";
	}

	@RequestMapping(value = "/script/list")
	public String work(Model model) {
		List<Script> docs = corpusService.getScripts();
		model.addAttribute("docs", docs);
		return "list";
	}
	
	@RequestMapping(value = {"/infoxml","/infoxml"})
	public String infoXml() {
		return "infoxml";
	}

}
