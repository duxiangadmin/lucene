package com.itlaiba.lucene.analyzer;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import com.itlaiba.lucene.util.LuceneUtil;
/**
 * 测试分词器
 * 看分词器版本具备的分词器也不同
 * @author 杜翔
 * @版本 V 1.0 
 * @date 2018年6月22日 上午12:14:11
 */
public class TestAnaliyzer {
	private static void testAnalyzer(Analyzer analyzer, String text) throws Exception {
		System.out.println("当前使用的分词器：" + analyzer.getClass());
		TokenStream tokenStream = analyzer.tokenStream("content",new StringReader(text));
		tokenStream.addAttribute(TermAttribute.class);
		while (tokenStream.incrementToken()) {
			TermAttribute termAttribute = tokenStream.getAttribute(TermAttribute.class);
			System.out.println(termAttribute.term());
		}
	}
	public static void main(String[] args) throws Exception{
		//Lucene内存的分词器
//		标准分词器,此处an 和it不能拆分
//		testAnalyzer(new StandardAnalyzer(LuceneUtil.getMatchVersion()),"传智播客说我们的首都是北京呀i an it");
//		好像是法语分词器
//		testAnalyzer(new FrenchAnalyzer(LuceneUtil.getMatchVersion()),"传智播客说我们的首都是北京呀it");
//		俄罗斯，差！
//		testAnalyzer(new RussianAnalyzer(LuceneUtil.getMatchVersion()),"传智播客说我们的首都是北京呀it");
//		中国的，不能拆分英文
//		testAnalyzer(new ChineseAnalyzer(),"传智播客说我们的首都是北京呀it");
//		两两分词器，没有英文
		testAnalyzer(new CJKAnalyzer(LuceneUtil.getMatchVersion()),"传智播客说我们的首都是北京呀it");
//		testAnalyzer(new CJKAnalyzer(LuceneUtil.getMatchVersion()),"传智是一家IT培训机构");
		
		
	}
}
