package itrip.common;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

public class BaseDao<T> {
    static String url="http://localhost:8080/solr/hotelCore";
    HttpSolrClient solr;
    public BaseDao(){
        //new 一个solr客户端
        solr=new HttpSolrClient(url);
        //给solr客户端的解析器赋值为xml解析器
        solr.setParser(new XMLResponseParser());
        //设置solr客户端的过期时间
        solr.setConnectionTimeout(1000);
    }
    public List<T> BaseDao1(SolrQuery solrQuery) throws SolrServerException, IOException {
        //创建一个solrQuery的响应对象
        SolrResponse response=solr.query(solrQuery);
        //list获取查询到的数据
        List<T> list=(List<T>) (((QueryResponse) response).getBeans(ItripHotelVO.class));
        return list;
    }
}
