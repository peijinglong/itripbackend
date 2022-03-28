package com.controller;

import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripUser;
import com.alibaba.fastjson.JSONArray;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import itrip.common.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class Login_controller {
    @Resource
    ItripUserMapper dao;
    @Resource
    TokenBiz biz;
    @Resource
    RedisUtil RedisUtil;
    @Resource
    Client c;

    @RequestMapping(value="/api/dologin",produces="application/json; charset=utf-8",method = RequestMethod.POST)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto login(String name, String password, HttpServletRequest requst) throws Exception {
        Map b=new HashMap<>();
        b.put("name",name);
        b.put("password",password);
        ItripUser user=dao.getItripUserListByMap(b);
        if(user!=null){
            //模拟session票据
            String token=biz.generateToken(requst.getHeader("User-Agent"),user);
            //把token存储到redis中
            //用fastjson把当前用户转换为字符串
            RedisUtil.setRedis(token,JSONArray.toJSONString(user));
            ItripTokenVO obj=new ItripTokenVO(token,Calendar.getInstance().getTimeInMillis()+7200,Calendar.getInstance().getTimeInMillis());
            return DtoUtil.returnDataSuccess(obj);
        }

        //通过fast json 把当前的对象转成字符串
        //  return JSONArray.toJSONString(user);
       return DtoUtil.returnDataSuccess(JSONArray.toJSONString(user));
    }

    //手机验证码
    public void SDKTestSendTemplateSMS(String phone,String message){
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8a216da87f63aaf1017f6cbe5ffa024d";
        String accountToken = "7e23726009004125b2e6a65b787cf395";
        //请使用管理控制台中已创建应用的APPID
        String appId = "8a216da87f63aaf1017f6cbe61450254";
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_XML);
        String to = phone;
        String templateId= "1";
        String[] datas = {message};
        //  String subAppend="1234";  //可选	扩展码，四位数字 0~9999
        //  String reqId="***";  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
        HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
        //  HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas,subAppend,reqId);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }

    //用户手机号码注册
    @RequestMapping(value="/api/registerbyphone",produces="application/json; charset=utf-8",method = RequestMethod.POST)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto Resgiter(@RequestBody ItripUserVO userVO) throws Exception {
        //把前台信息存储到数据库中
        ItripUser user=new ItripUser();
        user.setUserCode(userVO.getUserCode());
        user.setUserName(userVO.getUserName());
        user.setUserPassword(userVO.getUserPassword());
        dao.insertItripUserCS(user);
        //发送短信验证码

        int yzm=(int)((Math.random()*10000)+1000);
        SDKTestSendTemplateSMS(userVO.getUserCode(),""+yzm);
        //存入redis中，验证时用来匹配
        RedisUtil.setRedis(userVO.getUserCode(),""+yzm);
        return DtoUtil.returnSuccess("注册成功");
    }

    //用户手机验证码激活
    @RequestMapping(value="/api/validatephone",produces="application/json; charset=utf-8",method = RequestMethod.PUT)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto validatephone(String user,String code) throws Exception {
        String a=RedisUtil.getRedis(user);
        if (a!=null&&a.equals(code)){
            ItripUser u=new ItripUser();
            u.setActivated(1);
            u.setUserCode(user);
            dao.updateItripUser(u);
            return DtoUtil.returnSuccess();
        }else {
            return DtoUtil.returnFail("激活失败！","10000");
        }
    }

    //用户名验证是否存在
    @RequestMapping(value="/api/ckusr",produces="application/json; charset=utf-8",method = RequestMethod.GET)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto ckusr(String name) throws Exception {
        String newStr = new String(name.getBytes(), "UTF-8");
        List<ItripUser> list=dao.getItripUserByUserName(newStr);
        if(list.size()<=0){
            return DtoUtil.returnSuccess();
        }else{
            return DtoUtil.returnFail("用户名已存在！","10000");
        }
    }

    //用户邮箱注册
    @RequestMapping(value="/api/doregister",produces="application/json; charset=utf-8",method = RequestMethod.POST)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto doregister(@RequestBody ItripUserVO userVO) throws Exception {
        //把前台信息存储到数据库中
        ItripUser user=new ItripUser();
        user.setUserCode(userVO.getUserCode());
        user.setUserName(userVO.getUserName());
        user.setUserPassword(userVO.getUserPassword());
        dao.insertItripUserCS(user);
        //发送短信验证码

        int yzm=(int)((Math.random()*9999)+1000);
        c.email(userVO.getUserCode(),""+yzm);
        //存入redis中，验证时用来匹配
        RedisUtil.setRedis(userVO.getUserCode(),""+yzm);
        return DtoUtil.returnSuccess("注册成功");
    }

    //用户邮箱验证码激活
    @RequestMapping(value="/api/activate",produces="application/json; charset=utf-8",method = RequestMethod.PUT)
    //@ResponseBody 当前控制器直接返回对象或者字符串，
    //不在走逻辑视图
    @ResponseBody
    //String 不是逻辑视图名，也不是重定向，是直接返回的数据
    public Dto activate(String user,String code) throws Exception {
        String a=RedisUtil.getRedis(user);
        if (a!=null&&a.equals(code)){
            ItripUser u=new ItripUser();
            u.setActivated(1);
            u.setUserCode(user);
            dao.updateItripUser(u);
            return DtoUtil.returnSuccess();
        }else {
            return DtoUtil.returnFail("激活失败！","10000");
        }
    }
}
