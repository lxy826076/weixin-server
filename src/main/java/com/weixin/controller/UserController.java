package com.weixin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.weixin.dao.RedisBaseDao;
import com.weixin.datasource.DataSource;
import com.weixin.datasource.DynamicDataSourceGlobal;
import com.weixin.model.User;
import com.weixin.service.UserCrudService;
import com.weixin.utils.JSONPUtil;
import com.weixin.utils.RSAUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class);

	@Resource(name="userCrudService")
	private UserCrudService ucs;

	//		@Autowired
	//		private JedisCluster jedisCluster;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private RedisBaseDao redis;

	//添加
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String addUser(User user){
		logger.info(user.toString());
		try {
			ucs.addUser(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "error";
		}
		logger.info("add成功");
		return "sucess";
	}

	//更新
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String updateUser(User user){
		logger.info(user.toString());
		try {
			ucs.updateUser(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "error";
		}
		logger.info("update成功");
		return "sucess";
	}

	//查找全部
	@RequestMapping(value="/getAllUser", method=RequestMethod.POST)
	public @ResponseBody List<User> getUsers(){
		List<User> allUser = new ArrayList<User>();
		try {
			allUser = ucs.getAllUser();
			logger.info(allUser);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取所有用户出错！"+e.getMessage());
		}
		return allUser;
	}

	//通过编号查询用户信息
	@RequestMapping(value="/getUserById", method=RequestMethod.POST)
	@DataSource(value=DynamicDataSourceGlobal.READ)
	public @ResponseBody Map<String, Object> getUserById(HttpServletResponse response, @RequestParam("id") int id, @RequestParam("target") String target) throws Exception{
		User user = null;
		Map<String, Object> umap = new HashMap<String, Object>();
		umap.put("msg", "sucess");
		try {
			user = ucs.selectUserByID(id);
			logger.info(user);
		} catch (Exception e) {
			e.printStackTrace();
			umap.put("msg", "false");
			logger.info("获取用户出错！"+e.getMessage());
		}
		//		jedisCluster.set("result", "true");
		//		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		//		logger.info("clusterNodes:"+clusterNodes.toString());

		//		ValueOperations<String, String> stringOperations = redisTemplate.opsForValue();
		if (StringUtils.isEmpty(user)) {
			umap.put("msg", "false");
		}
		umap.put("user", user);
		return umap;
	}

	//删除
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String deleteUser(int id){
		try {
			ucs.deleteUser(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "error";
		}
		logger.info("uId"+id+",删除成功");
		return "sucess";
	}


	//用户登录
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView uLogin(HttpServletRequest request, User user, HttpSession session){
		String addr = request.getLocalAddr();    //取得本地IP     
		int port = request.getLocalPort(); 
		String sessionId = session.getId();
		ModelAndView mad = new ModelAndView("login_sucess");
		String uName = user.getUserName();
		try{
			User u = ucs.findPwdByUname(uName);
			if (!StringUtils.isEmpty(u)&&u.getUserPwd().equals(user.getUserPwd())) {
				session.setAttribute(u.getId()+"", u.getUserName());
				logger.info("用户："+u.getId()+"登陆成功！");
				mad.addObject("result", true);
				mad.addObject("msg", "登陆成功！<br/>服务器ip："+addr+":"+port+"<br/>    SESSIONID:"+sessionId);
			}else{
				logger.error("密码错误！用户："+user.getUserName()+"登陆失败！");
				mad.addObject("result", false);
				mad.addObject("msg", "用户名或密码错误，登陆失败！<br/>服务器ip："+addr+":"+port+"<br/>    SESSIONID:"+sessionId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			mad.addObject("result", false);
			mad.addObject("msg", "服务器异常！");
		}
		return mad;
	}

	//获取用户资料
	@RequestMapping(value="/getUserInfo", method=RequestMethod.GET)
	public String getInfo(HttpServletRequest request, int userId, ModelMap map, HttpSession session){
		String addr = request.getLocalAddr();    //取得本地IP     
		int port = request.getLocalPort(); 
		String sessionId = session.getId();
		String name = (String) session.getAttribute(userId+"");
		try{
			if (StringUtils.isEmpty(name)) {
				map.put("result", false);
				map.put("msg", "session失效，获取失败！<br/>服务器ip："+addr+":"+port+"<br/>    SESSIONID:"+sessionId);
			}else{
				User user = ucs.selectUserByID(userId);
				map.put("result", true);
				map.put("msg", "获取用户信息成功！<br/>服务器ip："+addr+":"+port+" <br/>   SESSIONID:"+sessionId);
				logger.info("用户信息"+user.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("result", false);
			map.put("msg", "服务器异常，获取失败！");
		}
		logger.info("接口返回"+map.toString());
		return "login_sucess";
	}

	/**	                                            服务接口，供远程接口调用
	 * @param response
	 * @param uId	               用户请求参数
	 * @param appId       用户向我平台申请的appId
	 * @param sign	               数据签名
	 * @param callback
	 */
	@RequestMapping(value="/getUserInfoWithKey", method=RequestMethod.POST)
	public void getUserInfoWithKey(HttpServletResponse response, int uId, String appId, String sign, String callback){
		JSONObject json = new JSONObject();
		User user = null;
		Map<String,String> dataMap = new TreeMap<String, String>();
		dataMap.put("sign", sign);
		dataMap.put("uId", uId+"");
		dataMap.put("appId", appId);
		try {
			json = ucs.validateUserSign(dataMap);
			boolean state = (Boolean) json.get("state");
			if (!state) {
				response.getWriter()
					.print(JSONPUtil
						.transformationWrite(callback,json.toJSONString()));
				logger.info("获取的用户信息接口返回："+json.toString());
				return;
			}
		} catch (Exception e1) {
			json.put("state", false);
			json.put("msg", "服务器接口异常！");
			logger.info("验证签名服务器接口异常！"+e1.getMessage());
		}

		try {
			user = ucs.selectUserByID(uId);
			json.put("state", true);
			json.put("userinfo", user);
			logger.info("获取的用户信息接口返回："+user.toString());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("state", false);
			json.put("msg", "服务器接口异常！");
			logger.info("获取的用户用户信息失败！"+e.getMessage());
		}
		try {
			response.getWriter()
			.print(JSONPUtil
					.transformationWrite(callback,json.toJSONString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ;
	}



}
