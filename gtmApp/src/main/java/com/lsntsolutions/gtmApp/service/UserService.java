package com.lsntsolutions.gtmApp.service;

import java.util.List;

import com.lsntsolutions.gtmApp.dto.NewPasswordDTO;
import com.lsntsolutions.gtmApp.model.User;

public interface UserService {

	void save(User user);

	User get(Integer id);

	User getByName(String name);

	Boolean exists(String name);

	List<User> getForAutocomplete(String searchPhrase, Boolean active, String sortId, String sortName, String sortActive, String sortProfile);

	boolean delete(Integer userId);

	List<User> getAll();

	List<User> getPaginated(int start, int length);

	Long getTotalNumber();

	Boolean changePassword(String username,NewPasswordDTO newPasswordDTO);
}
