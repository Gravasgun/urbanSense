package com.cqupt.urbansense.intercepter;

import com.cqupt.urbansense.bean.User;
import com.cqupt.urbansense.utils.AppThreadLocalUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppThreadLocalUtil.clear();
    }

    /**
     * 得到header中的用户信息，并把它存入ThreadLocal中
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取userId
        String userId = request.getHeader("userId");
        if (userId != null) {
            //存入当前线程
            User user = new User();
            user.setId(Integer.parseInt(userId));
            AppThreadLocalUtil.setUser(user);
        }
        return true;
    }

    /**
     * 清理线程中的数据
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //WmThreadLocalUtil.clear();
    }

}
