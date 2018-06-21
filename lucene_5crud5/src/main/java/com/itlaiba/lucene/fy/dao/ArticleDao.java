package com.itlaiba.lucene.fy.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.itlaiba.lucene.fy.entity.Article;
import com.itlaiba.lucene.util.LuceneUtil;

/**
 * 持久层
 * @author 杜翔
 * @版本 V 1.0 
 * @date 2018年6月20日 下午5:13:17
 */
public class ArticleDao {
	/**
	 * 根据关键字查总记录数
	 * 
	 */
	
	public int getAllRecord(String keywords)throws Exception{
		List<Article> articleList = new ArrayList<Article>();
		QueryParser queryParser = new QueryParser(LuceneUtil.getMatchVersion(),"content",LuceneUtil.getA());
		Query query = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getD());
		TopDocs topDocs = indexSearcher.search(query,100);
//		返回不受前面的100的影响的总记录数 
		return topDocs.totalHits;
	}
	
	/**
	 * 分页数据
	 */
	public List<Article> findAll(String keywords,int start ,int size)throws Exception{
		List<Article> articles = new ArrayList<Article>();
		QueryParser queryParser = new QueryParser(LuceneUtil.getMatchVersion(),"content",LuceneUtil.getA());
		Query query = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getD());
		TopDocs topDocs = indexSearcher.search(query,100);
		/*for (int i = start; i < size+start&&i<topDocs.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article = (Article)LuceneUtil.Document2java(document,Article.class);
			articles.add(article);
		}*/
		int middle = Math.min(start+size, topDocs.totalHits);
		for (int i = start; i < middle; i++) {
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article = (Article)LuceneUtil.Document2java(document,Article.class);
			articles.add(article);
		}
		return articles;
	}
	
	public static void main(String[] args) throws Exception {
		String keywords = "培";
		ArticleDao dao = new ArticleDao();
		System.out.println(dao.getAllRecord(keywords));
		
		System.out.println("第一页");
		List<Article> list = dao.findAll("培训", 0, 2);
		for (Article article : list) {
			System.out.println(article);
		}
		
		System.out.println("第2页");
		List<Article> list2 = dao.findAll("培训", 2, 2);
		for (Article article : list2) {
			System.out.println(article);
		}
		
		System.out.println("第3页");
		List<Article> list3 = dao.findAll("培训", 4, 2);
		for (Article article : list3) {
			System.out.println(article);
		}
	}
}
