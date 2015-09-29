package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.service.EventService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.model.Event;
import com.lsntsolutions.gtmApp.persistence.dao.EventDAO;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	private static final Logger logger = Logger.getLogger(EventServiceImpl.class);

	@Autowired
	private EventDAO eventDAO;

	@Override
	public void save(Event event) {
		this.eventDAO.save(event);
		logger.info("Se han guardado los cambios exitosamente. Id de Evento: " + event.getId());
	}

	@Override
	public Event get(Integer id) {
		return this.eventDAO.get(id);
	}

	@Override
	public Boolean exists(Integer code) {
		return this.eventDAO.exists(code);
	}

	@Override
	public List<Event> getForAutocomplete(String term, Boolean active) {
		return this.eventDAO.getForAutocomplete(term, active);
	}

	@Override
	public List<Event> getAll() {
		return this.eventDAO.getAll();
	}

	@Override
	public List<Event> getInputOutput(boolean input) {
		return this.eventDAO.getInputOutput(input);
	}

	@Override
	public boolean delete(Integer eventId) {
		return this.eventDAO.delete(eventId);
	}

	@Override
	public List<Event> getPaginated(int start, int length) {
		return this.eventDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.eventDAO.getTotalNumber();
	}
}
