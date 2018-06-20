package com.itlaiba.lucene.firstapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.itlaiba.lucene.entity.Article;

public class Fiest {
	@Test
	public void createIndexDB() throws Exception{
		Article article = new Article(1,"培训","传智是一个Java培训机构");
		Document document = new Document();
		document.add(new Field("id",article.getId().toString(),Store.YES,Index.ANALYZED));
		document.add(new Field("title",article.getTitle(),Store.YES,Index.ANALYZED));
		document.add(new Field("content",article.getContent(),Store.YES,Index.ANALYZED));
		Directory directory = FSDirectory.open(new File("E:/LuceneDBDBDBDBDBDBDBDBDB"));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		MaxFieldLength maxFieldLength = MaxFieldLength.LIMITED;
		IndexWriter indexWriter = new IndexWriter(directory,analyzer,maxFieldLength);
		indexWriter.addDocument(document);
		indexWriter.close();
	}
     
	@Test
	public void findIndexDB() throws Exception{
		List<Article> articleList = new ArrayList<Article>();
		String keywords = "传";
		Directory directory = FSDirectory.open(new File("E:/LuceneDBDBDBDBDBDBDBDBDB"));
		Version version = Version.LUCENE_30;
		Analyzer analyzer = new StandardAnalyzer(version);
		QueryParser queryParser = new QueryParser(version,"content",analyzer);
		Query query = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(directory);
		TopDocs topDocs = indexSearcher.search(query,10);
		for(int i=0;i<topDocs.scoreDocs.length;i++){
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			String id = document.get("id");
			String title = document.get("title");
			String content = document.get("content");
			Article article = new Article(Integer.parseInt(id),title,content);
			articleList.add(article);
		}
		for(Article article : articleList){
			System.out.println(article.getId()+":"+article.getTitle()+":"+article.getContent());
		}
	}

}
