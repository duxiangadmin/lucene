package com.itlaiba.lucene.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.itlaiba.lucene.entity.Article;

/**
 * 工具类
 * @author 杜翔
 * @版本 V 1.0 
 * @date 2018年6月20日 上午11:12:54
 */
public class LuceneUtil {
	
	private static Directory d ;
	private static Version matchVersion ;
	private static Analyzer a;
	private static MaxFieldLength mfl;	
	
	static{
		try {
			d =  FSDirectory.open(new File("f:/arcitleDBDB"));
			matchVersion =Version.LUCENE_30;
			a = new  StandardAnalyzer(matchVersion);
			mfl = MaxFieldLength.LIMITED;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}	
	}
	
	
	public static Directory getD() {
		return d;
	}

	public static Version getMatchVersion() {
		return matchVersion;
	}

	public static Analyzer getA() {
		return a;
	}

	public static MaxFieldLength getMfl() {
		return mfl;
	}

	
	
//	私有构造不让人直接访问
	private LuceneUtil() {
	}

	/*
	 * 将一个javabean对象转换为document对象
	 */
	public static Document java2Document(Object obj)throws Exception{
		Document document = new Document();
//		获取字节码文件
		Class clazz = obj.getClass();
//		获取私有属性
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
//			私有属性强力反射
			field.setAccessible(true);
//			获取属性名
			String name = field.getName();
//			人工拼接方法名
			String methodName ="get"+name.substring(0, 1).toUpperCase()+name.substring(1);
//			根据名称和参数获取方法
			Method method = clazz.getMethod(methodName, null);
			String value = method.invoke(obj, null).toString();
//			//加入到Document对象中去，这时javabean的属性与document对象的属性相同
			document.add(new org.apache.lucene.document.Field(name,value,Store.YES,Index.ANALYZED));
		}
		return document;
	}
	
	/*
	 * 将一个document对象转换为javabean对象
	 */
	public static Object Document2java(Document document,Class clazz)throws Exception{
//		通过字节码文件获得javabean对象
		Object obj = clazz.newInstance();
//		获得属性
		java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
		for(java.lang.reflect.Field reflectField : reflectFields){
			reflectField.setAccessible(true);
			String name = reflectField.getName();//id/title/content
			String value = document.get(name);//1/培训/传智是一家培训机构
			BeanUtils.setProperty(obj,name,value);//封装javabean对应的属性中去，通过setXxx()方法
		}
		return obj;
	}
	
	public static void main(String[] args) throws Exception {
		Article article = new Article(1, "传智", "传智是一个培训机构");
		Document document = LuceneUtil.java2Document(article);
//		Article a = (Article) LuceneUtil.Document2java(document, Article.class);
//		System.out.println(a);
	}
}
