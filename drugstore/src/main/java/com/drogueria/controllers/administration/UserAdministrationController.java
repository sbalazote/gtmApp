package com.drogueria.controllers.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.drogueria.dto.NewPasswordDTO;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.drogueria.dto.RoleDTO;
import com.drogueria.dto.UserDTO;
import com.drogueria.helper.EncryptionHelper;
import com.drogueria.model.Role;
import com.drogueria.model.User;
import com.drogueria.service.RoleService;
import com.drogueria.service.UserService;

@Controller
public class UserAdministrationController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ModelAndView users() {
		return new ModelAndView("users", "users", this.userService.getAll());
	}

	@RequestMapping(value = "/userAdministration", method = RequestMethod.GET)
	public String userAdministration(ModelMap modelMap) throws Exception {
		modelMap.put("roles", this.roleService.getAll());
		return "userAdministration";
	}

	@RequestMapping(value = "/existsUser", method = RequestMethod.GET)
	public @ResponseBody Boolean existsUser(@RequestParam String name) throws Exception {
		return this.userService.exists(name);
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public @ResponseBody User saveUser(@RequestBody UserDTO userDTO) throws Exception {
		User user = this.buildModel(userDTO);
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
		List<Role> roles = new ArrayList<Role>();
		for (Integer roleId : userDTO.getRoles()) {
			Role role = this.roleService.get(roleId);
			roles.add(role);
		}
		user.setRoles(roles);
		return user;
	}

	@RequestMapping(value = "/readUser", method = RequestMethod.GET)
	public @ResponseBody User readUser(ModelMap modelMap, @RequestParam Integer userId) throws Exception {
		User user = this.userService.get(userId);
		modelMap.put("roles", this.getSelectedRoles(user));
		return this.userService.get(userId);
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public @ResponseBody boolean deleteUser(@RequestParam Integer userId) throws Exception {
		return this.userService.delete(userId);
	}

	private List<RoleDTO> getSelectedRoles(User user) {
		List<Role> selectedRoles = user.getRoles();
		List<Role> allRoles = this.roleService.getAll();

		List<RoleDTO> rolesDTO = new ArrayList<RoleDTO>();

		for (Role role : allRoles) {
			if (selectedRoles.contains(role)) {
				rolesDTO.add(this.newRoleDTO(role, true));
			} else {
				rolesDTO.add(this.newRoleDTO(role, false));
			}
		}
		return rolesDTO;
	}

	private RoleDTO newRoleDTO(Role role, boolean selected) {
		RoleDTO roleDTO = new RoleDTO();

		roleDTO.setId(role.getId());
		roleDTO.setDescription(role.getDescription());
		roleDTO.setSelected(selected);

		return roleDTO;
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

		List<User> listUsers = null;
		if (searchPhrase.matches("")) {
			listUsers = this.userService.getPaginated(start, length);
			total = this.userService.getTotalNumber();
		} else {
			listUsers = this.userService.getForAutocomplete(searchPhrase, null);
			total = listUsers.size();
			if (total < start + length) {
				listUsers = listUsers.subList(start, (int) total);
			} else {
				listUsers = listUsers.subList(start, start + length);
			}
		}

		for (User user : listUsers) {
			JSONObject dataJson = new JSONObject();

			dataJson.put("id", user.getId());
			dataJson.put("name", user.getName());
			dataJson.put("password", user.getPassword());
			dataJson.put("isActive", user.isActive() == true ? "Si" : "No");
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
