package de.uni_koeln.webapps.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.util.fst.FST.INPUT_TYPE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scripting.ScriptSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tartarus.snowball.ext.EnglishStemmer;

import de.uni_koeln.webapps.data.Script;
import de.uni_koeln.webapps.data.SpokenContent;
import de.uni_koeln.webapps.service.CorpusService;
import de.uni_koeln.webapps.service.SearchService;
import de.uni_koeln.webapps.utils.Utils;

@Controller
@RequestMapping(value = "/scripttool/analyzer")
public class AnalyzeController {

	private List<String> stopWords = new ArrayList<>();
	private List<String> swearWordsList = new ArrayList<>();

	@Autowired
	CorpusService corpusService;

	@Autowired
	SearchService searchService;

	@RequestMapping(value = { "", "/" })
	public String analyze(Model model) throws FileNotFoundException {
		model.addAttribute("movies", corpusService.getScripts());
		return "analyzer";
	}

	@RequestMapping(value = "/wordCounter")
	public String wordCount(@RequestParam("word") String word, @RequestParam("movie1") String movie1,
			@RequestParam("movie2") String movie2, Model model) throws IOException, ParseException {
		Map<String[], Float> movieMap = new LinkedHashMap<String[], Float>();
		for (Script script : corpusService.getScripts()) {
			if (script.getTitle().equals(movie1) || script.getTitle().equals(movie2)) {
				StringBuilder sb = new StringBuilder();
				for (SpokenContent sc : script.getSpokenContent()) {
					sb.append(sc.getContent().toLowerCase() + " ");
				}
				float searchSize = sb.toString().split(word.toLowerCase()).length - 1;
				float runTime = script.getRuntime();
				float division = (searchSize / runTime);
				String key = searchSize + " '" + word + "s' in " + script.getRuntime() + " Minuten. Durchschnittlich ist jede "
						+ division + "te Minuten das Wort '" + word + "' zu hören";
				String[] keyArray = { key, script.getTitle() };
				movieMap.put(keyArray, division);
			}
		}
		model.addAttribute("movieMap", movieMap);
		return "countingwords";
	}

	@RequestMapping(value = "/quotesearch")
	public String similarPhrases(@RequestParam("inputPhrase") String inputPhrase, Model model)
			throws IOException, ParseException, NoSuchFieldException, SecurityException, ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException {
		Utils.txtToList("src/main/resources/stopwords.txt");
		StringBuilder sb = new StringBuilder();
		// Input wird in einzelne Worte zerlegt und in ein Set gepackt
		String[] inputArray = inputPhrase.toLowerCase().replaceAll("^abc", " ").replaceAll("//s+", " ").split(" ");
		for (int i = 0; i < inputArray.length; i++) {
			if (!stopWords.contains(inputArray[i])) {
				if (i + 1 == inputArray.length) {
					sb.append(inputArray[i]);
				} else {
					sb.append(inputArray[i] + " AND ");
				}
			}
		}
		List<SpokenContent> resultList = new ArrayList<>();
		List<SpokenContent> luceneList;
		luceneList = searchService.search(sb.toString());
		for (SpokenContent sc : luceneList) {
			for (String sentence : sc.getContent().split("[!?.]")) {
				int n = inputArray.length;
				for (int i = 0; i < inputArray.length; i++) {
					if (sentence.toLowerCase().replaceAll("^abc", " ").replaceAll("//s+", " ")
							.contains(inputArray[i])) {
						n -= 1;
						if (n == 0) {
							SpokenContent spokenContent = new SpokenContent();
							spokenContent.setColor(sc.getColor());
							spokenContent.setContent(sentence);
							spokenContent.setId(sc.getId());
							spokenContent.setSpeaker(sc.getSpeaker());
							resultList.add(spokenContent);
						}
					} else {
						break;
					}
				}
			}
		}
		if (resultList.size() <= 1) {
			String query = sb.toString().substring(0, sb.toString().lastIndexOf("AND"));
			resultList = searchService.search(query);
		}
		model.addAttribute("docs", resultList);
		return "result";
	}
	
	@RequestMapping(value = "/activevoc")
	public String getWordCounts(@RequestParam("scriptName") String scriptName, Model model) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, ParseException{
		Map<String, List<SpokenContent>> charakterMap = getContentByCharakter(scriptName);
		Map<String, Float> resultMap = new HashMap<>();
		Set<String> currentSet = new HashSet<>();
		for(String speaker : charakterMap.keySet()){
			for(SpokenContent content : charakterMap.get(speaker)){
				for(String word : content.getContent().toLowerCase().replaceAll("^abc", " ").replaceAll("\\s+", " ").split("\\s")){
					currentSet.add(word);
				}
			}
			resultMap.put(speaker, Float.valueOf(currentSet.size()));
			currentSet = new HashSet<>();
		}
		
		
		model.addAttribute("activeVocs", Utils.sortByValue(resultMap));
		model.addAttribute("title", scriptName);
		return "map";
	}

	
	@RequestMapping(value = "/swearwords")
	public String getSwearWords(@RequestParam("scriptName") String scriptName, Model model) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, ParseException{
		Map<String, List<SpokenContent>> charakterMap = getContentByCharakter(scriptName);
		swearWordsList = Utils.txtToList("src/main/resources/badwords.txt");
		Map<String, Float> resultMap = new HashMap<>();
		List<String> currentList = new ArrayList<>();
		for(String speaker : charakterMap.keySet()){
			int swearWordsCounter = 0;
			for(SpokenContent content : charakterMap.get(speaker)){
				for(String word : content.getContent().toLowerCase().replaceAll("^abc", " ").replaceAll("\\s+", " ").split("\\s")){
					if(swearWordsList.contains(word.replaceAll("abc", ""))){
						swearWordsCounter += 1;
					}
					currentList.add(word);
				}
			}
			if(!currentList.isEmpty()){
			resultMap.put(speaker, Float.valueOf((float)swearWordsCounter/(float)currentList.size()));}
			currentList = new ArrayList<>();
			swearWordsCounter = 0;
		}
		
		model.addAttribute("activeVocs", Utils.sortByValue(resultMap));
		model.addAttribute("title", scriptName);
		return "map";
	}
	
	
	private Map<String, List<SpokenContent>> getContentByCharakter(String scriptName) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException, ParseException{
		List<Script> scripts = corpusService.getScripts();
		Map<String, List<SpokenContent>> chrakterMap = new HashMap<>();
		for (Script script : scripts) {
			if(script.getTitle().toLowerCase().replaceAll("\\s+", "").equals(scriptName.toLowerCase().replaceAll("\\s+", ""))){
			for(String speaker : script.getSpeakerNames()){
				try {
					chrakterMap.put(speaker, searchService.search("speaker: '" + speaker + "~0.8'"));
				} catch (Exception e) {
					chrakterMap.put(speaker, searchService.search("speaker: '" + speaker + "'"));
				}
				
			}
			break;
			}
		}
		return chrakterMap;
	}
	
}
