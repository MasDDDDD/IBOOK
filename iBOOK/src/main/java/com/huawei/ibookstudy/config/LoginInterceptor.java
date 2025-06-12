package com.huawei.ibookstudy.config;

import com.auth0.jwt.interfaces.Claim;
import com.huawei.ibookstudy.constant.Const;
import com.huawei.ibookstudy.dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.huawei.ibookstudy.util.JWTUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private StudentDao studentDao;
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("进入拦截器");
        final String token = request.getHeader(Const.USER_TOKEN);
        Map<String, Claim> userData = JWTUtils.verifyToken(token);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");

        boolean isLegal = true;
        if (token == null) {
            PrintWriter out = response.getWriter();
            out.write("{\"message\": \"token is null!\"}");
            out.flush();
            out.close();
            isLegal = false;
        }
        else if (userData == null) {
            PrintWriter out = response.getWriter();
            out.write("{\"message\": \"token is illegal!\"}");
            out.flush();
            out.close();
            isLegal = false;
        }
        if (!isLegal) return false;

        String stuNum = userData.get("stuNum").asString();
        int roleId = userData.get("roleId").asInt();
        if (request.getServletPath().startsWith("/v1/admin") && roleId != 1) {
            log.debug("access" + request.getServletPath() + ", not logged in, return");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"message\": \"you're not admin!\"}");
            out.flush();
            out.close();
            return false;
        }
        log.debug("stuNum: " + stuNum + ", roleId: " + roleId + ", logged in!");
        request.setAttribute(Const.SESSION_USERNAME, stuNum);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 可以在这里添加一些后处理逻辑
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 可以在这里添加一些清理工作
    }
}

