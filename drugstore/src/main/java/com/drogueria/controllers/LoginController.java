package com.drogueria.controllers;

import com.drogueria.config.PropertyProvider;
import com.drogueria.service.PropertyService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    private static String IE_BROWSER = "IE";

    @Autowired
    private PropertyService propertyService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap modelMap, @RequestParam(value = "error", required = false) String error,HttpServletRequest request) throws Exception {
        String userAgent = request.getHeader("user-agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        String browserName = ua.getBrowser().toString();
        modelMap.put("sofwareName",PropertyProvider.getInstance().getProp(PropertyProvider.ARTIFACT_ID) );
        modelMap.put("name", propertyService.get().getName());
        modelMap.put("logPath",PropertyProvider.getInstance().getProp(PropertyProvider.LOGO));

        if(browserName.indexOf(IE_BROWSER) >= 0){
            modelMap.put("error", "Internet Explorer no es compatible con la aplicacion, utilice Chrome o Firefox");
            modelMap.put("loginDisabled", true);
            return "login";
        }else {
            if (error != null) {
                modelMap.put("error", "Usuario / Contraseña incorrecta, vuelva a intentar");
            }
        }
		return "login";
	}

}