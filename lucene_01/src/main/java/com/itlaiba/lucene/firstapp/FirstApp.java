package com.itlaiba.lucene.firstapp;


import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.itlaiba.lucene.entity.Article;

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
		indexWriter.close();
				
	}
}
