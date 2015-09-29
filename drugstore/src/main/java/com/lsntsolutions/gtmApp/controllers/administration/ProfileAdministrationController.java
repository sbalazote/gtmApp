package com.lsntsolutions.gtmApp.controllers.administration;

import com.lsntsolutions.gtmApp.dto.ProfileDTO;
import com.lsntsolutions.gtmApp.dto.RoleDTO;
import com.lsntsolutions.gtmApp.model.Profile;
import com.lsntsolutions.gtmApp.model.Role;
import com.lsntsolutions.gtmApp.service.ProfileService;
import com.lsntsolutions.gtmApp.service.RoleService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileAdministrationController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private ProfileService profileService;

    @RequestMapping(value = "/profileAdministration", method = RequestMethod.GET)
    public String profileAdministration(ModelMap modelMap) throws Exception {
        modelMap.put("roles", roleService.getAll());
        return "profileAdministration";
    }

    @RequestMapping(value = "/readProfile", method = RequestMethod.GET)
    public @ResponseBody
    Profile readProfile(ModelMap modelMap, @RequestParam Integer profileId) throws Exception {
        Profile profile = this.profileService.get(profileId);
        modelMap.put("roles", this.getSelectedRoles(profile));
        return this.profileService.get(profileId);
    }

    private List<RoleDTO> getSelectedRoles(Profile profile) {
        List<Role> selectedRoles = profile.getRoles();
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

    @RequestMapping(value = "/existsProfile", method = RequestMethod.GET)
    public @ResponseBody Boolean existsProfile(@RequestParam String description) throws Exception {
        return this.profileService.exists(description);
    }

    @RequestMapping(value = "/saveProfile", method = RequestMethod.POST)
    public @ResponseBody Profile saveProfile(@RequestBody ProfileDTO profileDTO) throws Exception {
        Profile profile = this.buildModel(profileDTO);
        this.profileService.save(profile);
        return profile;
    }

    private Profile buildModel(ProfileDTO profileDTO) {
        Profile profile;
        if (profileDTO.getId() != null) {
            profile = this.profileService.get(profileDTO.getId());
        } else {
            profile = new Profile();
            profile.setDescription(profileDTO.getDescription());
        }
        List<Role> roles = new ArrayList<>();
        for (Integer roleId : profileDTO.getRoles()) {
            Role role = this.roleService.get(roleId);
            roles.add(role);
        }
        profile.setRoles(roles);
        return profile;
    }

    @RequestMapping(value = "/deleteProfile", method = RequestMethod.POST)
    public @ResponseBody boolean deleteProfile(@RequestParam Integer profileId) throws Exception {
        return this.profileService.delete(profileId);
    }

    @RequestMapping(value = "/getMatchedProfiles", method = RequestMethod.POST)
    public @ResponseBody String getMatchedProfiles(@RequestParam Map<String, String> parametersMap) throws JSONException {

        String searchPhrase = parametersMap.get("searchPhrase");
        Integer current = Integer.parseInt(parametersMap.get("current"));
        Integer rowCount = Integer.parseInt(parametersMap.get("rowCount"));

        JSONArray jsonArray = new JSONArray();
        int start = (current - 1) * rowCount;
        int length = rowCount;
        long total;

        List<Profile> listProfiles;
        if (searchPhrase.matches("")) {
            listProfiles = this.profileService.getPaginated(start, length);
            total = this.profileService.getTotalNumber();
        } else {
            listProfiles = this.profileService.getForAutocomplete(searchPhrase, null);
            total = listProfiles.size();
            if (total < start + length) {
                listProfiles = listProfiles.subList(start, (int) total);
            } else {
                listProfiles = listProfiles.subList(start, start + length);
            }
        }

        for (Profile profile : listProfiles) {
            JSONObject dataJson = new JSONObject();

            dataJson.put("id", profile.getId());
            dataJson.put("description", profile.getDescription());
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
