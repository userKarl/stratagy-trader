package com.zd.config;

import com.auth0.jwt.interfaces.Claim;

import com.zd.common.utils.JWT;
import com.zd.common.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class loginFilter implements Filter {

    @Autowired
    private RedisUtils redisUtil;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        try {
            String uri = request.getRequestURI();
            if( -1 != uri.indexOf("zd")){
                System.out.println(uri);
                String clientNo = request.getHeader("clientNo");
                String tokenInCache = redisUtil.get("obh_" + clientNo);
                String token = request.getHeader("token");
                if(StringUtils.isBlank(tokenInCache) ){
                    throw new RuntimeException("登录信息过期,请重新登录");
                }
                if(tokenInCache != null && !tokenInCache.equals(token)){
                    throw new RuntimeException("登录信息有误,请重新登录");
                }

                System.out.println("登录验证"+ token);
                Map<String, Claim> stringClaimMap = JWT.verifyToken(token);

            }
        } catch (Exception e) {
            logger.error( "login check occur exception :" + e.getMessage());
            servletRequest.setAttribute("message","TokenCheckError");
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
