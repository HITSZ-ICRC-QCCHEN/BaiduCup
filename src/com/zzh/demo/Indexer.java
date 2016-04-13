package com.zzh.demo;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import com.zzh.util.analyzer.IKAnalyzer5x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;

import com.zzh.util.Constants;
import com.zzh.util.DocX;

/**
 * This class demonstrate the process of creating index with Lucene for text
 * files
 */
public class Indexer {

	private String indexDirStr;

	private DataSet dataSet;
	private IndexWriter indexWriter;

	public Indexer() {
		indexDirStr = Constants.INDEXDIR;
		try {
			// indexDir is the directory that hosts Lucene's index files
			FSDirectory indexDir = FSDirectory.open(Paths.get(indexDirStr));

			// dataSet stores the source data
			dataSet = new DataSet();

			Analyzer analyzer = new IKAnalyzer5x();
			IndexWriterConfig lWriterConfig = new IndexWriterConfig(analyzer);
			lWriterConfig.setOpenMode(OpenMode.CREATE);

			indexWriter = new IndexWriter(indexDir, lWriterConfig);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		long startTime = new Date().getTime();
		try {
			ArrayList<DocX> docs;

			docs = dataSet.fecthDocs();
			for (int i = 0; i < docs.size(); i++) {
				indexDoc(docs.get(i));
			}
			System.out.println("index done!");

			indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = new Date().getTime();
		System.out.println("It takes " + (endTime - startTime)
				+ " milliseconds to create index.");
	}

	private void indexDoc(DocX doc) {
		try {
			Document document = new Document();
			Field titleField = new TextField(Constants.TITLE,
					doc.getTitle(), Field.Store.YES);
			document.add(titleField);
			titleField.setBoost(2); // set boost factor for title
			Field contentField = new TextField(Constants.CONTENT,
					doc.getContent(), Field.Store.YES);
			contentField.setBoost(1);
			document.add(contentField);
			indexWriter.addDocument(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Indexer indexer = new Indexer();
		indexer.start();
	}
}