package com.itlaiba.lucene.crud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Test;

import com.itlaiba.lucene.entity.Article;
import com.itlaiba.lucene.util.LuceneUtil;

/**
 * 使用lucene进行增删改查
 * @author 杜翔
 * @版本 V 1.0 
 * @date 2018年6月20日 下午4:23:32
 */
public class ArticleDao {

	@Test	
	public void add() throws Exception{
		Article article = new Article(1,"培训","传智java培训机构");	
		Document document = LuceneUtil.java2Document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		indexWriter.addDocument(document);
		indexWriter.close();
	}
	@Test	
	public void addAll() throws Exception{
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		Article article1 = new Article(1,"培训","传智java培训机构");
		Document document1 = LuceneUtil.java2Document(article1);		
		indexWriter.addDocument(document1);
		
		Article article2 = new Article(2,"培训","传智php培训机构");
		Document document2 = LuceneUtil.java2Document(article2);		
		indexWriter.addDocument(document2);
		
		Article article3 = new Article(3,"培训","传智c++培训机构");
		Document document3 = LuceneUtil.java2Document(article3);		
		indexWriter.addDocument(document3);
		
		Article article4 = new Article(4,"培训","传智net培训机构");
		Document document4 = LuceneUtil.java2Document(article4);		
		indexWriter.addDocument(document4);
		
		Article article5 = new Article(5,"培训","传智seo培训机构");
		Document document5 = LuceneUtil.java2Document(article5);		
		indexWriter.addDocument(document5);		
		indexWriter.close();
	}
	@Test	
	public void delete() throws Exception{
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		indexWriter.deleteDocuments(new Term("id", "2"));
		indexWriter.close();
	}
	@Test	
	public void deletelist() throws Exception{
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		Term term1 = new Term("id","1");
		Term term2 = new Term("id","2");
		Term term3 = new Term("id","3");
		indexWriter.deleteDocuments(term1,term2,term3);
		indexWriter.close();
	}
	@Test	
	public void deleteAll() throws Exception{
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		indexWriter.deleteAll();
		indexWriter.close();
	}
	@Test	
	public void update() throws Exception{
		Article article = new Article(5,"培训","传智SEO培训机构");
		Document document = LuceneUtil.java2Document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		/*
		 * 第一个参数是：trem表示需要更新的doucment对象，id=document中的id，5对应值
		 * 第二个参数：新的document的值
		 */
		Term term = new Term("id","5");
		indexWriter.updateDocument(term, document);
		indexWriter.close();
	}
	@Test	
	public void findAll() throws Exception{
		String keywords = "培";
		List<Article> articleList = new ArrayList<Article>();
		QueryParser queryParser = new QueryParser(LuceneUtil.getMatchVersion(),"content",LuceneUtil.getA());
		Query query = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getD());
		TopDocs topDocs = indexSearcher.search(query,100);
		for(int i=0;i<topDocs.scoreDocs.length;i++){
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article = (Article)LuceneUtil.Document2java(document,Article.class);
			articleList.add(article);
		}
		for(Article a : articleList){
			System.out.println( a );
		}
	}
}
