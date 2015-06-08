package com.drogueria.controllers;

import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Request;

@Controller
public class LoginController {

    private static String IE_BROWSER = "IE";

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap modelMap, @RequestParam(value = "error", required = false) String error,HttpServletRequest request) throws Exception {
        String userAgent = request.getHeader("user-agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        String browserName = ua.getBrowser().toString();
        if(browserName.indexOf(IE_BROWSER) >= 0){
            modelMap.put("error", "Internet Explorer no es compatible con la aplicacion, utilice Chrome o Firefox");
            modelMap.put("loginDisabled", true);
            return "login";
        }else {
            if (error != null) {
                modelMap.put("error", "Usuario / Contrasenia incorrecta, vuelva a intentar");
            }
        }
		return "login";
	}

}