package com.itlaiba.lucene.optimize;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.itlaiba.lucene.util.LuceneUtil;
/**
 * 索引库优化
 * @author 杜翔
 * @版本 V 1.0 
 * @date 2018年6月21日 上午11:01:20
 */
public class ArticleDao {
	@Test
	public void add()throws Exception{
		Article article= new Article(1,"传智", "传智是一家培训机构", 10);
		Document doc = LuceneUtil.java2Document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		indexWriter.addDocument(doc);
		indexWriter.close();
	}
	
	/**
	 * 合并cfs文件，两个合并一次
	 * @throws Exception
	 */
	@Test
	public void type1()throws Exception{
		Article article = new Article(1,"传智", "传智是一家培训机构", 10);
		Document doc = LuceneUtil.java2Document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		indexWriter.addDocument(doc);
		indexWriter.optimize();
		indexWriter.close();
	}
	
	/**
	 * 合并cfs文件，自行设置多少个合并
	 * @throws Exception
	 */
	@Test
	public void type2()throws Exception{
		Article article = new Article(1,"传智", "传智是一家培训机构", 10);
		Document doc = LuceneUtil.java2Document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		indexWriter.addDocument(doc);
//		设置三个合并
		indexWriter.setMergeFactor(3);
		indexWriter.close();
	}
	
	
	/**
	 * 合并cfs文件，不设置默认10个自动合并
	 * @throws Exception
	 */
	@Test
	public void type3()throws Exception{
		Article article = new Article(1,"传智", "传智是一家培训机构", 10);
		Document doc = LuceneUtil.java2Document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		indexWriter.addDocument(doc);
//		设置三个合并
//		indexWriter.setMergeFactor(3);
		indexWriter.close();
	}
	
	@Test
	public void find()throws Exception{
		String keywords= "家";
		List<Article> lists = new ArrayList<Article>();
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getD());
		QueryParser parser = new QueryParser(LuceneUtil.getMatchVersion(),"content" , LuceneUtil.getA());
		Query query = parser.parse(keywords);
		TopDocs topDocs = indexSearcher.search(query, 100);
		for (int i = 0; i < topDocs.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article = (Article) LuceneUtil.Document2java(document, Article.class);
			lists.add(article);
		}
		for (Article article : lists) {
			System.out.println(article);
		}
	}
}
