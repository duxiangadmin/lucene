package com.itlaiba.lucene.secondApp;

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

import com.itlaiba.lucene.entity.Article;
import com.itlaiba.lucene.util.LuceneUtil;

/**
 * 根据工具类重写FirstApp
 * @author 杜翔
 * @版本 V 1.0 
 * @date 2018年6月20日 上午11:55:13
 */
public class SecondApp {

//	创建索引库
	@Test
	public void createDb() throws Exception{
//		Article article = new Article(1, "培训", "传智是一家培训机构");
		Article article = new Article(2, "培训", "北大是一家培训机构");
		Document document = LuceneUtil.java2Document(article);
		IndexWriter indexWriter = new IndexWriter(LuceneUtil.getD(), LuceneUtil.getA(), LuceneUtil.getMfl());
		indexWriter.addDocument(document);
		indexWriter.close();
	}
	
	/**
	 * 根据关键字从索引库中查询符合条件的数据
	 */
	@Test
	public void findIndexDB() throws Exception{
		String keywords = "培训";
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
