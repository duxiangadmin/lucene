package com.itlaiba.lucene.highlighter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.junit.Test;

import com.itlaiba.lucene.optimize.Article;
import com.itlaiba.lucene.util.LuceneUtil;
/**
 * 关键字高亮
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
	 * 内容和标题都是高亮展示
	 * @throws Exception
	 */
	@Test
	public void find()throws Exception{
		String keywords= "传智";
		List<Article> lists = new ArrayList<Article>();
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getD());
		QueryParser parser = new QueryParser(LuceneUtil.getMatchVersion(),"content" , LuceneUtil.getA());
		Query query = parser.parse(keywords);
		TopDocs topDocs = indexSearcher.search(query, 100);
//		以下代码会对关键字进行高亮
//		格式，在关键字前后加入html代码
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
//		关键字
		Scorer scorer = new QueryScorer(query);
//		高亮对象
		Highlighter highlighter = new Highlighter(formatter, scorer);
		for (int i = 0; i < topDocs.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			
//			返回需要设置的高亮关键字
//			返回标题
			String titleHighlighter = highlighter.getBestFragment(LuceneUtil.getA(), "title", document.get("title"));
//			返回内容
			String contentHighlighter = highlighter.getBestFragment(LuceneUtil.getA(), "content", document.get("content"));
//			将高亮字符再次替换进入document对象
//			System.out.println(contentHighlighter);
//			设置标题
			document.getField("title").setValue(titleHighlighter);
//			设置内容
			document.getField("content").setValue(contentHighlighter);
			Article article = (Article) LuceneUtil.Document2java(document, Article.class);
			lists.add(article);
		}
		for (Article article : lists) {
			System.out.println(article);
		}
	}
	
	/**
	 * 内容高亮
	 * @throws Exception
	 */
	@Test
	public void findand()throws Exception{
		String keywords= "家";
		List<Article> lists = new ArrayList<Article>();
		IndexSearcher indexSearcher = new IndexSearcher(LuceneUtil.getD());
		QueryParser parser = new QueryParser(LuceneUtil.getMatchVersion(),"content" , LuceneUtil.getA());
		Query query = parser.parse(keywords);
		TopDocs topDocs = indexSearcher.search(query, 100);
//		以下代码会对关键字进行高亮
//		格式，在关键字前后加入html代码
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
//		关键字
		Scorer scorer = new QueryScorer(query);
//		高亮对象
		Highlighter highlighter = new Highlighter(formatter, scorer);
		for (int i = 0; i < topDocs.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			
//			返回需要设置的高亮关键字
			String contentHighlighter = highlighter.getBestFragment(LuceneUtil.getA(), "content", document.get("content"));
//			将高亮字符再次替换进入document对象
//			System.out.println(contentHighlighter);
			document.getField("content").setValue(contentHighlighter);
			
			Article article = (Article) LuceneUtil.Document2java(document, Article.class);
			lists.add(article);
		}
		for (Article article : lists) {
			System.out.println(article);
		}
	}
}
