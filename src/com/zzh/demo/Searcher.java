package com.zzh.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.zzh.util.analyzer.IKAnalyzer5x;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.zzh.util.Constants;
import com.zzh.util.QueryDoc;

public class Searcher {

	private static Logger logger = Logger.getLogger(Searcher.class);

	private String indexDirStr;

	private IndexSearcher searcher;
	private Analyzer analyzer;
	private Query query;
	private TopDocs topDocs;

	public Searcher() {
		indexDirStr = Constants.INDEXDIR;
		// This is the directory that hosts the Lucene index
		File indexDir = new File(indexDirStr);
		if (!indexDir.exists()) {
			System.out.println("The Lucene index is not exist");
			return;
		}

		try {
			FSDirectory directory = FSDirectory.open(Paths.get(indexDirStr));
			searcher = new IndexSearcher(DirectoryReader.open(directory));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		analyzer = new IKAnalyzer5x();
	}

	public int query(String queryStr, int n) {
		String[] fields = { Constants.TITLE, Constants.CONTENT};
		Map<String, Float> fieldBoostMap = new TreeMap<String, Float>();
		fieldBoostMap.put(fields[0], 2.0f);
		fieldBoostMap.put(fields[1], 1.0f);
		try {
//			BooleanClause booleanClause = new BooleanClause{ [BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD] }
			query = new MultiFieldQueryParser(fields, analyzer, fieldBoostMap).parse(queryStr.trim());
			System.out.println("query: " + query.toString());
			topDocs = searcher.search(query, n);

			// for logs
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			int length = scoreDocs.length;
			logger.error("query: " + queryStr + "==>" + query.toString());
			for (int i = 0; i < length; i++) {
				logger.error("doc" + i + " score: " + scoreDocs[i].score);
			}
			// end
			return topDocs.totalHits;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public ArrayList<QueryDoc> fetchQueryDocs(int startDocID, int endDocID) {
		ArrayList<QueryDoc> queryDocs = new ArrayList<QueryDoc>();
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		int maxDocID = scoreDocs.length - 1; // 当前查询结果中最大的文档ID
		if (maxDocID < endDocID && topDocs.totalHits > 0) { // 当前查询的结果数目不足以显示
			try {
				topDocs = searcher.search(query, 2 * endDocID);
				scoreDocs = topDocs.scoreDocs;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 返回检索结果
		int totalHits = topDocs.totalHits;
		for (int i = startDocID; i < endDocID && i < totalHits; i++) {
			QueryDoc queryDoc = fetchQueryDoc(scoreDocs[i].doc);
			queryDocs.add(queryDoc);
		}
		return queryDocs;
	}

	public QueryDoc fetchQueryDoc(int docID) {
        try {
            Document document = fetchDocument(docID);
            String title = document.get(Constants.TITLE);
            String content = document.get(Constants.CONTENT);
            return new QueryDoc(title, content);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
	}

	private Document fetchDocument(int docID) {
		try {
			return searcher.doc(docID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		String queryStr = "毛泽东";
		Searcher searcher = new Searcher();
		int totalHits = searcher.query(queryStr, 10);
		ArrayList<QueryDoc> queryDocs = searcher.fetchQueryDocs(10, 20);
		int length = queryDocs.size();
		for (int i = 0; i < length; i++) {
			System.out.println(queryDocs.get(i).getTitle() + '\t');
			System.out.println(queryDocs.get(i).getContent());
		}
	}
}
