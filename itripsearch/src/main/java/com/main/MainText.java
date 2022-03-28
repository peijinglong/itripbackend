package com.main;

import itrip.common.ItripHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

public class MainText {
    static String url="http://localhost:8080/solr/hotelCore";

    public static void main(String[] args) throws SolrServerException, IOException {
        //new 一个solr客户端
        HttpSolrClient solr=new HttpSolrClient(url);
        //给solr客户端的解析器赋值为xml解析器
        solr.setParser(new XMLResponseParser());
        //设置solr客户端的过期时间
        solr.setConnectionTimeout(1000);
        //new 一个solrQuery的对象
        SolrQuery solrQuery=new SolrQuery();
        //查询全部数据
        solrQuery.setQuery("*:*");
        //solrQuery添加条件
        solrQuery.addFilterQuery("keyword:北京");
        //排序条件
        solrQuery.setSort("id",SolrQuery.ORDER.desc);
        //创建一个solrQuery的响应对象
        SolrResponse response=solr.query(solrQuery);
        //list获取查询到的数据
        List<ItripHotelVO> list=((QueryResponse) response).getBeans(ItripHotelVO.class);
        //遍历获取到的数据并输出结果
        for (ItripHotelVO i:list) {
            System.out.println(i.getHotelName()+"\t"+i.getAddress());
        }
    }
}
