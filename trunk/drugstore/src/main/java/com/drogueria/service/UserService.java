package com.drogueria.service;

import java.util.List;

import com.drogueria.model.User;

public interface UserService {

	void save(User user);

	User get(Integer id);

	User getByName(String name);

	Boolean exists(String name);

	List<User> getForAutocomplete(String term, Boolean active);

	boolean delete(Integer userId);

	List<User> getAll();

	List<User> getPaginated(int start, int length);

	Long getTotalNumber();
}
