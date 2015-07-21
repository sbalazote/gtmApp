package com.drogueria.helper;

import org.springframework.oxm.Marshaller;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.xml.MarshallingView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class MyCustomMarshallingView extends MarshallingView {
    private Marshaller marshaller;

    public MyCustomMarshallingView(Marshaller marshaller) {
        super(marshaller);
        //Assert.notNull(marshaller, "'marshaller' must not be null");
        //setContentType(DEFAULT_CONTENT_TYPE);
        this.marshaller = marshaller;
        //setExposePathVariables(false);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        Object toBeMarshalled = locateToBeMarshalled(model);
        if (toBeMarshalled == null) {
            throw new ServletException("Unable to locate object to be marshalled in model: " + model);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        marshaller.marshal(toBeMarshalled, new StreamResult(bos));

        setResponseContentType(request, response);
        response.setContentLength(bos.size());

        Cookie cookie = new Cookie("fileDownloadToken", request.getParameterValues("fileDownloadToken")[0]);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);

        FileCopyUtils.copy(bos.toByteArray(), response.getOutputStream());
    }
}
