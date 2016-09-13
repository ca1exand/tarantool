package de.uni_koeln.webapps.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.webapps.data.Script;
import de.uni_koeln.webapps.data.SpokenContent;
import de.uni_koeln.webapps.service.CorpusService;
import de.uni_koeln.webapps.service.SearchService;

@Controller
@RequestMapping(value = "/scripttool/search")
public class SearchController {

	@Autowired
	SearchService searchService;

	@RequestMapping(value = { "/", "" })
	public String searchForm() {
		return "search";
	}

	@RequestMapping(value = { "/result" })
	public String search(@RequestParam("searchForm") String searchPhrase, Model model) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		List<SpokenContent> resultList;
		try {
			resultList = searchService.search(searchPhrase);
			model.addAttribute("docs", resultList);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return "result";
	}
}
