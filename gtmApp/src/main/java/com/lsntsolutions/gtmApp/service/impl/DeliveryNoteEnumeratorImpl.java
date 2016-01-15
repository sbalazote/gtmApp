package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.persistence.dao.DeliveryNoteEnumeratorDAO;
import com.lsntsolutions.gtmApp.service.DeliveryNoteEnumeratorService;
import com.lsntsolutions.gtmApp.model.DeliveryNoteEnumerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryNoteEnumeratorImpl implements DeliveryNoteEnumeratorService {

	private static final Logger logger = Logger.getLogger(AgentServiceImpl.class);

	@Autowired
	private DeliveryNoteEnumeratorDAO deliveryNoteEnumeratorDAO;

	@Override
	public void save(DeliveryNoteEnumerator deliveryNoteEnumerator) {
		this.deliveryNoteEnumeratorDAO.save(deliveryNoteEnumerator);
		logger.info("Se han guardado los cambios exitosamente. Id de Punto de Venta: " + deliveryNoteEnumerator.getId());
	}

	@Override
	public DeliveryNoteEnumerator get(Integer id) {
		return this.deliveryNoteEnumeratorDAO.get(id);
	}

	@Override
	public Boolean exists(Integer deliveryNotePOS, Boolean fake) {
		return this.deliveryNoteEnumeratorDAO.exists(deliveryNotePOS, fake);
	}

	@Override
	public List<DeliveryNoteEnumerator> getForAutocomplete(String term, Boolean active, Boolean fake) {
		return this.deliveryNoteEnumeratorDAO.getForAutocomplete(term, active, fake);
	}

	@Override
	public List<DeliveryNoteEnumerator> getAll() {
		return this.deliveryNoteEnumeratorDAO.getAll();
	}

	@Override
	public List<DeliveryNoteEnumerator> getReals(boolean fake) {
		return this.deliveryNoteEnumeratorDAO.getReals(fake);
	}

	@Override
	public Boolean checkNewDeliveryNoteNumber(Integer deliveryNotePOS, Integer lastDeliveryNoteNumberInput) {
		return this.deliveryNoteEnumeratorDAO.checkNewDeliveryNoteNumber(deliveryNotePOS,lastDeliveryNoteNumberInput);
	}

	@Override
	public boolean delete(Integer agentId) {
		return this.deliveryNoteEnumeratorDAO.delete(agentId);
	}

	@Override
	public List<DeliveryNoteEnumerator> getPaginated(int start, int length) {
		return this.deliveryNoteEnumeratorDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.deliveryNoteEnumeratorDAO.getTotalNumber();
	}
}
