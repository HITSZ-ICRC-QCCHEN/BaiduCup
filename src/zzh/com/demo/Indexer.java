package zzh.com.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import zzh.com.utils.Constants;
import zzh.com.utils.HtmlParser;
import zzh.com.utils.JCSimilarity;
import zzh.com.utils.Page;
import zzh.com.utils.PageExt;

/**
 * This class demonstrate the process of creating index with Lucene for text
 * files
 */
public class Indexer {

	private String indexDirStr;

	private DataSet dataSet;
	private TFIDFSimilarity similarity;
	private IndexWriter indexWriter;

	public Indexer() {
		indexDirStr = Constants.INDEXDIR;
		try {
			// indexDir is the directory that hosts Lucene's index files
			FSDirectory indexDir = FSDirectory.open(Paths.get(indexDirStr));

			// dataSet stores the source data
			dataSet = new DataSet();
			similarity = new JCSimilarity();

			// Analyzer analyzer = new SmartChineseAnalyzer(true);
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig lWriterConfig = new IndexWriterConfig(analyzer);
			lWriterConfig.setOpenMode(OpenMode.CREATE);
			// lWriterConfig.setSimilarity(similarity);

			indexWriter = new IndexWriter(indexDir, lWriterConfig);

			// bloomFilter = BloomFilter.getInstance();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		long startTime = new Date().getTime();
		try {
			ArrayList<Html> htmls;

			int loop = 0;

			while ((htmls = dataSet.fecthHtmls()) != null) {
				int length = htmls.size();
				for (int i = 0; i < length; i++) {
					String urlStr = htmls.get(i).url;
					indexHtml(htmls.get(i).path, urlStr);
				}
				System.out.println("done " + ++loop);
			}
			indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = new Date().getTime();
		System.out.println("It takes " + (endTime - startTime)
				+ " milliseconds to create index.");
	}

	private void indexHtml(String pathStr, String urlStr) {
		try {
			Document document = new Document();
			document.add(new StringField(Constants.URL, urlStr, Field.Store.YES));
			// Page page = HtmlParser.parseHtml(pathStr);

			// Page page = HtmlParser.parseHtml(urlStr, pathStr);
			// File htmlFile = new File(pathStr);
			// Page page = HtmlParser.parseHtml(urlStr, htmlFile);
			String text = getText(pathStr);
			// Page page = HtmlParser.parseHtmlOpt(text);
			PageExt pageExt = HtmlParser.parseHtmlExt(urlStr, pathStr);
			if (pageExt == null)
				return;
			// if (bloomFilter.contains(page.getBody()))
			// return;
			// bloomFilter.add(page.getBody());

			Field titleField = new TextField(Constants.TITLE,
					pageExt.getTitle(), Field.Store.YES);
			document.add(titleField);
			titleField.setBoost(4); // set boost factor for title
			Field keywordField = new TextField(Constants.KEYWORDS,
					pageExt.getKeywords(), Field.Store.YES);
			keywordField.setBoost(3);
			document.add(keywordField);
			Field descField = new TextField(Constants.DESCRIPTION,
					pageExt.getDescription(), Field.Store.YES);
			descField.setBoost(2);
			document.add(descField);
			document.add(new TextField(Constants.BODY, pageExt.getBody(),
					Field.Store.YES));
			indexWriter.addDocument(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getText(String pathStr) {
		File htmlFile = new File(pathStr);
		String text = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					htmlFile));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				text += line;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	public static void main(String[] args) {
		Indexer indexer = new Indexer();
		indexer.start();
	}
}