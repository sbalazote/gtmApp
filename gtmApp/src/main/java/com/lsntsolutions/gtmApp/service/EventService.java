package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.Event;

import java.util.List;

public interface EventService {

	void save(Event event);

	Event get(Integer id);

	Boolean exists(Integer code);

	List<Event> getForAutocomplete(String term, Boolean active, String sortId, String sortCode, String sortDescription, String sortOriginAgent, String sortDestinationAgent, String sortIsActive);

	List<Event> getAll();

	List<Event> getInputOutput(boolean input);

	boolean delete(Integer eventId);

	List<Event> getPaginated(int start, int length);

	Long getTotalNumber();
}
