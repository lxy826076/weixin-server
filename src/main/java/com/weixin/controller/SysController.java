package com.weixin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.weixin.dao.RedisBaseDao;
import com.weixin.service.SysService;
import com.weixin.utils.JSONPUtil;

@Controller
@RequestMapping("/sys")
public class SysController {
	private static Logger logger = Logger.getLogger(UserController.class);
	
	@Resource(name="sysService")
	private SysService sysService;
	
	@RequestMapping(value="/getKeyt", method=RequestMethod.POST)
	public void getKeyt(HttpServletRequest request, HttpServletResponse response, String appId, String callback){
		StringBuffer requestURI = request.getRequestURL();
		String queryString = request.getQueryString();
		System.out.println("queryString=======>"+queryString);
		System.out.println("requestURI："+requestURI);
		
		JSONObject privateKeyJson = new JSONObject();
		try {
			privateKeyJson = sysService.getKeyt(appId);
			logger.info("获取的用户私钥接口返回："+privateKeyJson.toJSONString());
			response.getWriter()
			.print(JSONPUtil
					.transformationWrite(callback,privateKeyJson.toJSONString()));
		} catch (Exception e) {
			e.printStackTrace();
			privateKeyJson.put("state", false);
			privateKeyJson.put("msg", "服务器接口异常！");
			logger.info("获取的用户私钥失败！"+e.getMessage());
		}
		return ;
	}
}
