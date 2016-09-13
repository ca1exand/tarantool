package de.uni_koeln.webapps.service;

import java.io.File;import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.query.Field;
import org.springframework.stereotype.Service;

import de.uni_koeln.webapps.data.Script;
import de.uni_koeln.webapps.data.SpokenContent;

/**
 * Der IndexService dient nur dem Aufbau eines Lucene-Index. Der Service wird in
 * diesem Beispiel nur einmal direkt beim Start der Applikation ausgeführt.
 */
@Service
public class IndexService {

	private String indexDir = "index";

	@Autowired
	private CorpusService corpusService;

	/**
	 * Bei der Initialisierung wird zunächst der Such-Index erstellt.
	 * Die @PostConstruct-Annotation sorgt dafür, dass die Methode ausgeführt
	 * wird, sobald alle Services bereit sind. Alternativ kann die
	 * init()-Methode auch im default-Konstruktor aufgerufen werden (vgl.
	 * CorpusService).
	 * 
	 * @throws IOException
	 */
	@PostConstruct
	public void initIndex() throws IOException {

		Directory dir = new SimpleFSDirectory(new File(indexDir).toPath());
		IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(dir, writerConfig);
		writer.deleteAll();
		
		List<Script> scripts = corpusService.getScripts();
		for (Script s : scripts) {
			/* ShakespeareDocument umwandeln ... */
			List<Document> docs = convertToLuceneDocs(s);
			/* ... und zum Index hinzufügen: */
			for (Document document : docs) {
				writer.addDocument(document);
			}
		}
		writer.close();
	}

	private List<Document> convertToLuceneDocs(Script s) {
		List<Document> docs = new ArrayList<>();
		for(SpokenContent sc : s.getSpokenContent()){
			Document doc = new Document();
			doc.add(new TextField("content", sc.getContent(), Store.YES));
			doc.add(new TextField("speaker", sc.getSpeaker(), Store.YES));
			doc.add(new TextField("title", s.getTitle(), Store.YES));
			doc.add(new TextField("id", String.valueOf(sc.getId()) ,Store.YES));
			docs.add(doc);
		}
		return docs;
	}

}
