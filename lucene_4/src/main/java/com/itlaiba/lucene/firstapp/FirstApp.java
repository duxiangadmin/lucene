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
import org.junit.experimental.max.MaxCore;

import com.itlaiba.lucene.entity.Article;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class FirstApp {
	/**
	 * 创建索引库
	 * 将article放入索引库中的原始记录表，形成词汇表（原始数据变为索引表）e
	 * @throws Exception
	 */
	@Test
	public void createDB()throws Exception{
//		创建article对象
		Article article = new Article(1, "传智", "传智是一个培训机构");
//		创建document对象
		Document document = new Document();
//		将arcitle对象中的三个属性分别绑定到document对象中
		/*
		 * 第一个参数：document对象中的名字叫xid，article叫做id，项目提倡一致
		 * 第二个参数：document对象中的值，根据对象获取
		 * 第3个参数：是否将该属性写入词汇表，有yes有no，项目提倡非id都存入
		 * 第4个参数：是否将该属性进行分词算法，（也就是把该字段从一个string拆成char）
		 * 			Index.ANALYZED该属性表示是
		 * 			Index.NOT_ANALYZED表示不进行分词
		 */
		document.add(new Field("xid", article.getId().toString(), Store.YES, Index.ANALYZED));
		document.add(new Field("xtitle", article.getTitle(), Store.YES, Index.ANALYZED));
		document.add(new Field("xcontent", article.getContent(), Store.YES, Index.ANALYZED));
//		将document写入索引库
		/*
		 * 第一个参数：表示索引库最终对于硬盘中的哪个目录
		 * 第二个参数：采用什么策略来进行拆分
		 * 第3个参数：最多将文本拆分成多少词汇，limmit为1W
		 */
		Directory d =  FSDirectory.open(new File("f:/arcitleDBDB"));
		Version matchVersion =Version.LUCENE_30;
		Analyzer a = new  StandardAnalyzer(matchVersion);
		MaxFieldLength mfl = MaxFieldLength.LIMITED;		
		IndexWriter indexWriter = new IndexWriter(d, a, mfl);
//		写入document对象，忘记了一次
		indexWriter.addDocument(document);
		indexWriter.close();
				
	}
	
	
	@Test
	public void findDB()throws Exception{
//		准备工作
		String keywords = "培";
		List<Article> articleList = new ArrayList<Article>();
		
		Directory d =  FSDirectory.open(new File("f:/arcitleDBDB"));
		Version matchVersion =Version.LUCENE_30;
		Analyzer a = new  StandardAnalyzer(matchVersion);
		MaxFieldLength mfl = MaxFieldLength.LIMITED;
//		创建indexsearcher字符流对象
		IndexSearcher indexSearcher = new IndexSearcher(d);
		QueryParser queryParser = new QueryParser(matchVersion, "xcontent", a);
		Query query = queryParser.parse(keywords);
//		根据关键字去索引库中的字汇表中查询
//		第一个参数：表示封装关键字的查询对象，其他queryParser表示查询解析器
//		第二个参数:MAX_RECODE表示如果 这个对象搜索出来的内容较多，那么我给它设定一下最多查询出来多少个内容
//				即超出按MAX_rECODE为准，不超按实际为准		
		int MAX_RECODE=100;
		TopDocs topDocs = indexSearcher.search(query, MAX_RECODE);
//		迭代词汇表中符合条件的编号
		for(int i =0;i<topDocs.scoreDocs.length;i++){
//			取出封装编号和分数的scoreDoc对象
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
//			取出每一个编号
			int  no = scoreDoc.doc;
//			根据编号去索引库中的原始记录查找对应document对象
			Document document = indexSearcher.doc(no);
			String id = document.get("xid");
			String title = document.get("xtitle");
			String content = document.get("xcontent");
			Article article = new Article(Integer.parseInt(id), title, content);
			articleList.add(article);
		}
		for (Article article : articleList) {
			System.out.println(article);
		}
	}
}
