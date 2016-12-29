package com.wd.CoilingDragon.WebMagic;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.w3c.dom.ls.LSException;

import com.wd.CoilingDragon.Dao.CatalogueMapper;
import com.wd.CoilingDragon.Enity.Catalogue;
import com.wd.Tools.CoillingDragonTool;
import com.wd.Tools.DBTools;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.inject.New;

public class CoilingDragon implements PageProcessor{

	public static final String URL_Base = "http://www.wuxiaworld.com/cdindex-html/";
//	public static final String Regex = "http://www.wuxiaworld.com/cdindex-html/(\\w+)/.*";

	  // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(5000);
    private CoillingDragonTool coillingDragonTool = new CoillingDragonTool();

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        System.out.println("start");

        // 部分二：定义如何抽取页面信息，并保存下来
        coillingDragonTool.catalogueProceeor(page.getHtml());;  
       
       System.out.println("over");
    }

//    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new CoilingDragon())
                //从"https://github.com/code4craft"开始抓
                .addUrl(URL_Base)
                .addPipeline(new JsonFilePipeline("/Users/wangda/Desktop/scrapy/CoilingDragon"))
                //开启5个线程抓取
                .thread(3)
                //启动爬虫
                .run();
    }

    @Test
    void CoilingDragonTest(){
    			
    	 Spider.create(new CoilingDragon())
         //从"https://github.com/code4craft"开始抓
         .addUrl(URL_Base)
         .addPipeline(new JsonFilePipeline("/Users/wangda/Desktop/scrapy/CoilingDragon"))
         //开启5个线程抓取
         .thread(3)
         //启动爬虫
         .run();
    }
}
