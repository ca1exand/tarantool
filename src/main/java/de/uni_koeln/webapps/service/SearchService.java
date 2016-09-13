package de.uni_koeln.webapps.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.webapps.data.SpokenContent;

/**
 * Der SearchService regelt die Suche im Lucene-Index.
 */
@Service
public class SearchService {

	private String indexDir = "index";

	/**
	 * Durchsucht den Index.
	 * 
	 * @param q
	 *            Das Suchwort
	 * @return Eine Ergebnisliste, besteht aus einer Reihe von
	 *         {@link ShakespeareDocument}.
	 * @throws IOException
	 * @throws ParseException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public List<SpokenContent> search(String q) throws IOException, ParseException, NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		
		Directory dir = new SimpleFSDirectory(new File(indexDir).toPath());
		DirectoryReader dirReader = DirectoryReader.open(dir);
		IndexSearcher is = new IndexSearcher(dirReader);

		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		Query query = parser.parse(q);

		TopDocs hits = is.search(query, dirReader.numDocs());
		int hitSize = hits.totalHits;
		List<SpokenContent> resultList = new ArrayList<SpokenContent>();
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = hits.scoreDocs[i];
			Document doc = is.doc(scoreDoc.doc);
			SpokenContent result = wrapFieldResults(doc);
			resultList.add(result);
		}
		dirReader.close();
		return resultList;
	}

	/*
	 * Konvertiert das Lucene-Document wieder zurück in ein ShakespeareDocument
	 * (unser Datenmodell).
	 */
	private SpokenContent wrapFieldResults(Document doc) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		SpokenContent sc = new SpokenContent();
		sc.setContent(doc.get("content"));
		sc.setId(doc.get("id"));
		sc.setSpeaker(doc.get("speaker"));
		sc.setTitle(doc.get("title"));
		return sc;
}

	
	
}