package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.NewPasswordDTO;
import com.lsntsolutions.gtmApp.dto.UserDTO;
import com.lsntsolutions.gtmApp.helper.EncryptionHelper;
import com.lsntsolutions.gtmApp.model.Profile;
import com.lsntsolutions.gtmApp.model.User;
import com.lsntsolutions.gtmApp.service.ProfileService;
import com.lsntsolutions.gtmApp.service.UserService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class UserAdministrationController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ModelAndView users(@RequestParam Map<String, String> parametersMap) {
		String searchPhrase = parametersMap.get("searchPhrase");
		if (searchPhrase.matches("")) {
			return new ModelAndView("users", "users", this.userService.getAll());
		} else {
			return new ModelAndView("users", "users", this.userService.getForAutocomplete(searchPhrase, null,null ,null ,null ,null ));
		}
	}

	@RequestMapping(value = "/userAdministration", method = RequestMethod.GET)
	public String userAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("profiles", profileService.getAll());
		return "userAdministration";
	}

	@RequestMapping(value = "/existsUser", method = RequestMethod.GET)
	public @ResponseBody Boolean existsUser(@RequestParam String name) throws Exception {
		return this.userService.exists(name);
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public @ResponseBody User saveUser(@RequestBody UserDTO userDTO) throws Exception {
		User user = this.buildModel(userDTO);
		Profile profile = this.profileService.get(userDTO.getProfileId());
		user.setProfile(profile);
		this.userService.save(user);
		return user;
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public @ResponseBody Boolean changePassword(@RequestBody NewPasswordDTO newPasswordDTO) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return this.userService.changePassword(auth.getName(),newPasswordDTO);
	}

	private User buildModel(UserDTO userDTO) {
		User user;
		if (userDTO.getId() != null) {
			user = this.userService.get(userDTO.getId());
			user.setActive(userDTO.isActive());
			if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
				user.setPassword(EncryptionHelper.generateHash(userDTO.getName() + userDTO.getPassword()));
			}
		} else {
			user = new User();
			user.setName(userDTO.getName());
			user.setPassword(EncryptionHelper.generateHash(userDTO.getName() + userDTO.getPassword()));
			user.setActive(userDTO.isActive());
		}
		return user;
	}

	@RequestMapping(value = "/readUser", method = RequestMethod.GET)
	public @ResponseBody User readUser(@RequestParam Integer userId) throws Exception {
		return this.userService.get(userId);
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public @ResponseBody boolean deleteUser(@RequestParam Integer userId) throws Exception {
		return this.userService.delete(userId);
	}

	@RequestMapping(value = "/getMatchedUsers", method = RequestMethod.POST)
	public @ResponseBody String getMatchedUsers(@RequestParam Map<String, String> parametersMap) throws JSONException {

		String searchPhrase = parametersMap.get("searchPhrase");
		Integer current = Integer.parseInt(parametersMap.get("current"));
		Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

		JSONArray jsonArray = new JSONArray();
		int start = (current - 1) * rowCount;
		int length = rowCount;
		long total;

		String sortId = parametersMap.get("sort[id]");
		String sortName = parametersMap.get("sort[name]");
		String sortActive = parametersMap.get("sort[isActive]");
		String sortProfile = parametersMap.get("sort[profile]");

		List<User> listUsers;
		listUsers = this.userService.getForAutocomplete(searchPhrase, null,sortId ,sortName ,sortActive ,sortProfile );
		total = listUsers.size();
		if (total < start + length) {
			listUsers = listUsers.subList(start, (int) total);
		} else if(length > 0) {
			listUsers = listUsers.subList(start, start + length);
		}

		for (User user : listUsers) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", user.getId());
			dataJson.put("name", user.getName());
			dataJson.put("isActive", user.isActive() ? "Si" : "No");
			dataJson.put("profile", user.getProfile().getDescription());
			jsonArray.put(dataJson);
		}

		JSONObject responseJson = new JSONObject();
		responseJson.put("current", current);
		responseJson.put("rowCount", (total < (start + length)) ? (total - length) : length);
		responseJson.put("rows", jsonArray);
		responseJson.put("total", total);

		return responseJson.toString();
	}
}
