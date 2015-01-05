package com.drogueria.service;

import java.util.List;

import com.drogueria.model.Event;

public interface EventService {

	void save(Event event);

	Event get(Integer id);

	Boolean exists(Integer code);

	List<Event> getForAutocomplete(String term, Boolean active);

	List<Event> getAll();

	List<Event> getInputOutput(boolean input);

	boolean delete(Integer eventId);

	List<Event> getPaginated(int start, int length);

	Long getTotalNumber();
}
