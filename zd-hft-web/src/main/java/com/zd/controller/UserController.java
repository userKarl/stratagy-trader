package com.zd.controller;

import com.zd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zd.common.annotation.Log;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login" ,method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Log("登陆")
    public String login( @RequestBody String json)  {
        String jsonDecode = null;
        try {
            jsonDecode = URLDecoder.decode(json, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        }
        String result = userService.login(jsonDecode);
        return result;
    }

    @RequestMapping(value = "/createuser" ,method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String cearteuser( @RequestBody String json) {
        String jsonDecode = null;
        try {
            jsonDecode = URLDecoder.decode(json, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        }
        String result = userService.Createuser(jsonDecode);
        return result;
    }

    //发送短信
    @RequestMapping(value = "/sms" ,method = RequestMethod.POST ,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String sms(@RequestBody String json, HttpServletRequest request) throws Exception {
        String jsonDecode = null;
        try {
            jsonDecode = URLDecoder.decode(json, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        }
        return userService.sendMessage(request,jsonDecode);
    }
}
