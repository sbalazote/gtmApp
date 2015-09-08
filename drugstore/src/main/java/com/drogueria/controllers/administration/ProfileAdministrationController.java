package com.drogueria.controllers.administration;

import com.drogueria.dto.RoleDTO;
import com.drogueria.model.Profile;
import com.drogueria.model.Role;
import com.drogueria.model.User;
import com.drogueria.service.ProfileService;
import com.drogueria.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileAdministrationController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private ProfileService profileService;

    @RequestMapping(value = "/readProfile", method = RequestMethod.GET)
    public @ResponseBody
    Profile readProfile(ModelMap modelMap, @RequestParam Integer profileId) throws Exception {
        Profile user = this.profileService.get(profileId);
        modelMap.put("roles", this.getSelectedRoles(user));
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
}
