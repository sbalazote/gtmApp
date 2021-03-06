package com.lsntsolutions.gtmApp.controllers;

import com.lsntsolutions.gtmApp.config.PropertyProvider;
import com.lsntsolutions.gtmApp.service.PropertyService;
import com.verhas.licensor.License;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.log4j.Logger;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
public class LoginController {
    private static String IE_BROWSER = "IE";
    private static final Logger logger = Logger.getLogger(LoginController.class);
    private License lic = null;

    byte [] digest = new byte[] {
            (byte)0xA0,
            (byte)0x71, (byte)0x0D, (byte)0x3D, (byte)0x78, (byte)0x82, (byte)0x9E, (byte)0x4A, (byte)0x4F,
            (byte)0x2F, (byte)0xDE, (byte)0x04, (byte)0x10, (byte)0x06, (byte)0x7A, (byte)0x22, (byte)0x25,
            (byte)0xA3, (byte)0xF5, (byte)0xDB, (byte)0x22, (byte)0xF8, (byte)0x0D, (byte)0x83, (byte)0x9A,
            (byte)0x29, (byte)0x62, (byte)0x8B, (byte)0xEB, (byte)0xF6, (byte)0x42, (byte)0xCE, (byte)0xB4,
            (byte)0x3F, (byte)0x22, (byte)0x2A, (byte)0xAF, (byte)0x73, (byte)0xAB, (byte)0xDF, (byte)0xCC,
            (byte)0x50, (byte)0xA5, (byte)0x35, (byte)0xFA, (byte)0x71, (byte)0x91, (byte)0x03, (byte)0xF2,
            (byte)0xD9, (byte)0x5C, (byte)0x48, (byte)0x94, (byte)0x43, (byte)0x90, (byte)0xEC, (byte)0xC4,
            (byte)0x14, (byte)0x1F, (byte)0x7C, (byte)0xA0, (byte)0x23, (byte)0xC6, (byte)0xD9,
    };

    @Autowired
    private PropertyService propertyService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap modelMap,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "expiredUrl", required = false) String expiredUrl,
                        HttpServletRequest request) throws Exception {
        String userAgent = request.getHeader("user-agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        String browserName = ua.getBrowser().toString();
        modelMap.put("softwareName",PropertyProvider.getInstance().getProp(PropertyProvider.ARTIFACT_ID) );
        modelMap.put("name", propertyService.get().getName());
        String relativePath = "";
        //String realPath = request.getServletContext().getRealPath("/images/uploadedLogo.png");
        File file = new File(request.getServletContext().getInitParameter("upload.location") + "/uploadedLogo.png");
        PropertyProvider.getInstance().setProp(PropertyProvider.NAME, propertyService.get().getName());
        //File file = new File(realPath);

        if (file.exists()) {
            FileCopyUtils.copy(file, new File(request.getSession().getServletContext().getRealPath("/images/") + "/uploadedLogo.png"));
            relativePath = "./images/uploadedLogo.png";
        } else {
            relativePath = "./images/logo.png";
        }
        modelMap.put("logoPath", relativePath);

        if (logout != null) {
            modelMap.put("logout", true);
        } else if (expiredUrl != null) {
            modelMap.put("expiredUrl", true);
        } else {
            boolean validLicense = true;

//            if (browserName.indexOf(IE_BROWSER) >= 0) {
//                modelMap.put("error", "Internet Explorer no es compatible con la aplicacion, utilice Chrome o Firefox");
//                modelMap.put("loginDisabled", true);
//            } else {
                if (error != null) {
                    modelMap.put("error", "Usuario / Contraseña incorrecta, vuelva a intentar");
                }
//            }
            if (!validLicense) {
                modelMap.put("error", "La Licencia ha caducado. Comuníquese con el Administrador.");
                modelMap.put("loginDisabled", true);
            }
        }

		return "login";
	}

    private boolean isValidLicense() {
        lic = new License();
        try {
            lic.loadKeyRing(this.getClass().getClassLoader().getResourceAsStream("license/pubring.gpg"), digest);
            lic.setLicenseEncoded(this.getClass().getClassLoader().getResourceAsStream("license/license.lic"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PGPException e) {
            e.printStackTrace();
        }
        return isValidDate();
    }

    protected boolean isValidDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat spFormatter = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
        Date validUntilDate = null;
        try {
            validUntilDate = sdf.parse(lic.getFeature("valid-until"));
            PropertyProvider.getInstance().setProp(PropertyProvider.LICENSE_EXPIRATION, spFormatter.format(validUntilDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date();
        return currentDate.before(validUntilDate);
    }
}