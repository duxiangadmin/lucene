package com.itlaiba.lucene.fy.service;

import java.util.List;

import com.itlaiba.lucene.fy.dao.ArticleDao;
import com.itlaiba.lucene.fy.entity.Article;
import com.itlaiba.lucene.fy.entity.Page;

/**
 * 业务层
 * @author 杜翔
 * @版本 V 1.0 
 * @date 2018年6月21日 上午12:14:51
 */
public class ArticleService {

	private ArticleDao dao = new ArticleDao();
	/**
	 * 根据条件查询所有数据
	 */
	
	public Page show(String keywords,int currPageNO)throws Exception{
		Page page = new Page();
		
		page.setCurrPageNO(currPageNO);
		
		//封装总记录数
		int allRecordNO = dao.getAllRecord(keywords);
		page.setAllRecordNO(allRecordNO);
		
		//封装总页数
		int allPageNO = 0;
		if(page.getAllRecordNO() % page.getPerPageSize() == 0){
			allPageNO = page.getAllRecordNO() / page.getPerPageSize();
		}else{
			allPageNO = page.getAllRecordNO() / page.getPerPageSize() + 1;
		}
		page.setAllPageNO(allPageNO);
		
		//封装内容
		int size = page.getPerPageSize();
		int start = (page.getCurrPageNO()-1) * size;
		List<Article> articleList = dao.findAll(keywords,start,size);
		page.setArticleList(articleList);
		
		return page;
	}
	
	//测试
		public static void main(String[] args) throws Exception{
			ArticleService test = new ArticleService();
			Page page = test.show("培",1);
			
			System.out.println(page.getCurrPageNO());
			System.out.println(page.getPerPageSize());
			System.out.println(page.getAllRecordNO());
			System.out.println(page.getAllPageNO());
			for(Article a : page.getArticleList()){
				System.out.println(a);
			}
		}
}
