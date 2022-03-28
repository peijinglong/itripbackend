package com.controller;

import itrip.common.*;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
public class Hotel_list_controller {
    @RequestMapping(value = "/api/hotellist/searchItripHotelListByHotCity",produces = "application/json;charset=utf-8",method = RequestMethod.POST)
    @ResponseBody
    public Dto searchItripHotelListByHotCity(@RequestBody SearchHotCityVO vo) throws SolrServerException, IOException {
        BaseDao baseDao=new BaseDao();
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery("cityId:"+vo.getCityId());
        solrQuery.setRows(vo.getCount());
        List list=baseDao.BaseDao1(solrQuery);
        return DtoUtil.returnDataSuccess(list);
    }

    @RequestMapping(value = "/api/hotellist/searchItripHotelPage",produces = "application/json;charset=utf-8",method = RequestMethod.POST)
    @ResponseBody
    public Dto searchItripHotelPage(@RequestBody SearchHotelVO vo) throws SolrServerException, IOException {
        BaseDao dao=new BaseDao();
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery("*:*");
        if (vo.getPageNo()==null){
            vo.setPageNo(1);
        }
        if (vo.getFeatureIds()!=null&&!vo.getFeatureIds().equals(null)){
            String []number=vo.getFeatureIds().split(",");
            String str="";
            for (int i=0;i<number.length;i++){
                if (i==0){
                    str+="featureIds:*,"+number[i]+",*";
                }else {
                    str+="or featureIds:*,"+number[i]+",*";
                }
            }
            solrQuery.addFilterQuery("featureIds",str);
        }
        if (!vo.getTradeAreaIds().equals(null)){
            String []number=vo.getTradeAreaIds().split(",");
            String str="";
            for (int i=0;i<number.length;i++){
                if (i==0){
                    str+="tradingAreaIds:*,"+number[i]+",*";
                }else {
                    str+="or tradingAreaIds:*,"+number[i]+",*";
                }
            }
            solrQuery.addFilterQuery("tradingAreaIds",str);
        }
        if (vo.getPageSize()==null){
            vo.setPageSize(6);
        }
        if (vo.getHotelLevel()!=null){
            solrQuery.addFilterQuery("hotelLevel:"+vo.getHotelLevel());
        }
        if (vo.getAscSort()!=null){
            solrQuery.addSort(vo.getAscSort(),SolrQuery.ORDER.asc);
        }
        if (vo.getDescSort()!=null){
            solrQuery.addSort(vo.getDescSort(),SolrQuery.ORDER.desc);
        }
        if (vo.getDestination()!=null&&!vo.getDestination().equals(null)){
            solrQuery.addFilterQuery("destination:"+vo.getDestination());
        }
        if (vo.getKeywords()!=null&&!vo.getKeywords().equals(null)){
            solrQuery.addFilterQuery("keyword:"+vo.getKeywords());
        }
        if (vo.getMinPrice()!=null){
            solrQuery.addFilterQuery("minPrice:"+vo.getMinPrice());
        }
        if (vo.getMaxPrice()!=null){
            solrQuery.addFilterQuery("maxPrice:"+vo.getMaxPrice());
        }
        if (vo.getCheckInDate()!=null){
            solrQuery.addFilterQuery("creationDate:"+vo.getCheckInDate());
        }
        if (vo.getCheckOutDate()!=null){
            solrQuery.addFilterQuery("modifyDate:"+vo.getCheckOutDate());
        }

        List list=dao.BaseDao1(solrQuery);
        return DtoUtil.returnDataSuccess(list);
    }
}
