package com.wd.Tools;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSession;

import com.wd.CoilingDragon.Dao.CatalogueMapper;
import com.wd.CoilingDragon.Enity.Catalogue;
import com.wd.CoilingDragon.WebMagic.ContentWebMagic;

import us.codecraft.webmagic.selector.Html;

public class CoillingDragonTool {
	
    SqlSession session = DBTools.getSession();
    CatalogueMapper mapper = session.getMapper(CatalogueMapper.class);
    
	public void catalogueProceeor(Html html){
	       List<String> chapterList = html.xpath("//a").all();     
	       for (int i = 0; i < chapterList.size(); i++) {
	    	   String tmp = chapterList.get(i);
	    	   System.out.println("第"+i+"条"+tmp);
	    	   if(tmp.indexOf("http://www.wuxiaworld.com/cdindex-html/book")!=-1 && tmp.indexOf("href")!=-1){
	    		   Catalogue catalogue = new Catalogue();
	    		   catalogue.setBookId(1);
	    		   
	        	   final String titleRegex = "(?<=cdindex-html\\/).*?\\d+-chapter-.*?\\d+";
	        	   final Pattern titlePattern = Pattern.compile(titleRegex);
	        	   final Matcher titleMatcher = titlePattern.matcher(tmp);
	        	   while (titleMatcher.find()) {
	        	       System.out.println("title: " + titleMatcher.group(0));
	            	   catalogue.setTitle(titleMatcher.group(0));
	        	   }
	        	   
	        	   final String superTitleRegex = "(?<=cdindex-html\\/).*?\\d+";
	        	   final Pattern superTitlePattern = Pattern.compile(superTitleRegex);
	        	   final Matcher superTitleMatcher = superTitlePattern.matcher(tmp);
	        	   while (superTitleMatcher.find()) {
	        	       System.out.println("superTitle: " + superTitleMatcher.group(0));
	            	   catalogue.setSuperTitle(superTitleMatcher.group(0));
	        	   }
	        	   
	        	   final String hrefRegex = "(?<=href=\").*?(?=\")";
	        	   final Pattern hrefPattern = Pattern.compile(hrefRegex);
	        	   final Matcher hrefMatcher = hrefPattern.matcher(tmp);
	        	   while (hrefMatcher.find()) {
	           	    catalogue.setOriginUrl(hrefMatcher.group(0));
	        	   }
	        	   
	        	   final String catalogueRegex = "(?<=\">).*?(?=<\\/a>)";
	        	   final Pattern cataloguePattern = Pattern.compile(catalogueRegex);
	        	   final Matcher catalogueMatcher = cataloguePattern.matcher(tmp);
	        	   while (catalogueMatcher.find()) {
	           	    catalogue.setCatalogue(catalogueMatcher.group(0));
	        	   }	        	  
		            System.out.println("即将插入 :" + catalogue.toString());	
		            try {
	        	        mapper.insert(catalogue);
	    	            System.out.println("成功插入 :"+"第"+i+"条,"+"共"+chapterList.size()+"条" + catalogue.toString());
	        	        session.commit();
	        	        ContentWebMagic contentWebMagic = new ContentWebMagic();
	        	        contentWebMagic.setCatalogue(catalogue);
	        	        contentWebMagic.run();
	        	    } catch (Exception e) {
	        	        e.printStackTrace();
	        	        session.rollback();
	        	    }
	            }
	       	}
	    	   
	       }
	
}
