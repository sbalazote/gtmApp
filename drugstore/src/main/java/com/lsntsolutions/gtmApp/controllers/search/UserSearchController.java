package com.lsntsolutions.gtmApp.controllers.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lsntsolutions.gtmApp.model.User;
import com.lsntsolutions.gtmApp.service.UserService;

@Controller
public class UserSearchController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public @ResponseBody
	List<User> getUsers() {
		return this.userService.getAll();
	}
}