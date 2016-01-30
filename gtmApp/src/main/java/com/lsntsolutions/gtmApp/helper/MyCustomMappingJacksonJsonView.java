package com.lsntsolutions.gtmApp.helper;

import com.fasterxml.jackson.core.JsonEncoding;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyCustomMappingJacksonJsonView extends MappingJackson2JsonView {

    private JsonEncoding encoding = JsonEncoding.UTF8;

    private boolean disableCaching = true;


    @Override
    protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
        setResponseContentType(request, response);
        response.setCharacterEncoding(this.encoding.getJavaName());
        if (disableCaching) {
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
            response.addDateHeader("Expires", 1L);
        }
        Cookie cookie = new Cookie("fileDownloadToken", request.getParameterValues("fileDownloadToken")[0]);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
    }
}