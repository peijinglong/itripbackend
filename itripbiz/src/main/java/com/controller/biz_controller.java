package com.controller;

import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.dao.itripUserLinkUser.ItripUserLinkUserMapper;
import cn.itrip.pojo.ItripAreaDic;
import cn.itrip.pojo.ItripLabelDic;
import cn.itrip.pojo.ItripUser;
import cn.itrip.pojo.ItripUserLinkUser;
import itrip.common.*;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class biz_controller {
    @Resource
    ItripAreaDicMapper dao;

    @RequestMapping(value="/api/hotel/queryhotcity/{type}",produces="application/json; charset=utf-8",method = RequestMethod.GET)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto getCitty(@PathVariable("type")int type) throws Exception {
        Map map=new HashMap();
        map.put("a",type);
        List<ItripAreaDic> list=dao.getItripAreaDicListByCS(map);
        return DtoUtil.returnDataSuccess(list);
    }

    @Resource
    ItripLabelDicMapper dao1;
    @RequestMapping(value="/api/hotel/queryhotelfeature",produces="application/json; charset=utf-8",method = RequestMethod.GET)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto getCittyRemen() throws Exception{
        List<ItripLabelDic> list=dao1.firstTop();
        return DtoUtil.returnDataSuccess(list);
    }

    @Resource
    ItripUserLinkUserMapper dao2;

    @RequestMapping(value="/api/userinfo/queryuserlinkuser",produces="application/json; charset=utf-8",method = RequestMethod.POST)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto queryuserlinkuser(@RequestBody ItripSearchUserLinkUserVO itripSearchUserLinkUserVO) throws Exception{
        Map map=new HashMap();
        map.put("linkUserName",itripSearchUserLinkUserVO.getLinkUserName());
        List<ItripUserLinkUser> list= dao2.getItripUserLinkUserListByMap(map);
        if(list!=null){
            return DtoUtil.returnDataSuccess(list);
        }else {
            return DtoUtil.returnFail("查询失败！","10000");
        }

    }

    @RequestMapping(value="/api/userinfo/adduserlinkuser",produces="application/json; charset=utf-8",method = RequestMethod.POST)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto adduserlinkuser(@RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO) throws Exception{
        ItripUserLinkUser i=new ItripUserLinkUser();
        i.setLinkUserName(itripAddUserLinkUserVO.getLinkUserName());
        i.setLinkIdCard(itripAddUserLinkUserVO.getLinkIdCard());
        i.setLinkPhone(itripAddUserLinkUserVO.getLinkPhone());
        int num= dao2.insertItripUserLinkUser(i);
        if(num>0){
            return DtoUtil.returnSuccess("新增成功！","10000");
        }else {
            return DtoUtil.returnFail("新增失败！","10000");
        }

    }

    @RequestMapping(value="/api/userinfo/modifyuserlinkuser",produces="application/json; charset=utf-8",method = RequestMethod.POST)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto modifyuserlinkuser(@RequestBody ItripModifyUserLinkUserVO itripModifyUserLinkUserVO) throws Exception{
        ItripUserLinkUser i=new ItripUserLinkUser();
        i.setLinkUserName(itripModifyUserLinkUserVO.getLinkUserName());
        i.setLinkPhone(itripModifyUserLinkUserVO.getLinkPhone());
        i.setLinkIdCard(itripModifyUserLinkUserVO.getLinkIdCard());
        i.setId(itripModifyUserLinkUserVO.getId());
        int num= dao2.updateItripUserLinkUser(i);
        if(num>0){
            return DtoUtil.returnSuccess("修改成功！","10000");
        }else {
            return DtoUtil.returnFail("修改失败！","10000");
        }

    }

    @RequestMapping(value="/api/userinfo/deluserlinkuser",produces="application/json; charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public Dto deluserlinkuser(Integer[] ids) throws Exception{

        int num= dao2.deleteItripUserLinkUserById(ids);
        if(num>0){
            return DtoUtil.returnSuccess("删除成功！","10000");
        }else {
            return DtoUtil.returnFail("删除失败！","10000");
        }

    }

    @RequestMapping(value="/api/hotel/querytradearea/{cityId}",produces="application/json; charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public Dto querytradearea(@PathVariable("cityId")Integer cityId) throws Exception{
        BaseDao bd=new BaseDao();
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery("cityId:"+cityId);
        List list=bd.BaseDao1(solrQuery);
        return DtoUtil.returnDataSuccess(list);
    }



}
