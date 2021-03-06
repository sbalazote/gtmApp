package com.lsntsolutions.gtmApp.persistence.dao;

import java.util.List;

import com.lsntsolutions.gtmApp.model.User;

public interface UserDAO {

	void save(User user);

	User get(Integer id);

	User getByName(String name);

	Boolean exists(String name);

	List<User> getForAutocomplete(String searchPhrase, Boolean active, String sortId, String sortName, String sortIsActive, String sortProfile);

	List<User> getAll();

	boolean delete(Integer userId);

	List<User> getPaginated(int start, int length);

	Long getTotalNumber();
}
