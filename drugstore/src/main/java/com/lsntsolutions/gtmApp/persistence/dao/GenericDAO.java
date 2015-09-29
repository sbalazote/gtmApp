package com.lsntsolutions.gtmApp.persistence.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T extends Serializable> {

	void save(T entity);

	T get(Class<T> type, Integer id);

	List<T> getAll(Class<T> type);

}
