package com.drogueria.service.impl;

import com.drogueria.model.Agent;
import com.drogueria.model.DeliveryNoteEnumerator;
import com.drogueria.persistence.dao.AgentDAO;
import com.drogueria.persistence.dao.DeliveryNoteEnumeratorDAO;
import com.drogueria.service.DeliveryNoteEnumeratorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeliveryNoteEnumeratorImpl implements DeliveryNoteEnumeratorService{

    private static final Logger logger = Logger.getLogger(AgentServiceImpl.class);

    @Autowired
    private DeliveryNoteEnumeratorDAO deliveryNoteEnumeratorDAO;

    @Override
    public void save(DeliveryNoteEnumerator deliveryNoteEnumerator){
        this.deliveryNoteEnumeratorDAO.save(deliveryNoteEnumerator);
        logger.info("Se han guardado los cambios exitosamente. Id de Punto de Venta: " + deliveryNoteEnumerator.getId());
    }

    @Override
    public DeliveryNoteEnumerator get(Integer id) {
        return this.deliveryNoteEnumeratorDAO.get(id);
    }

    @Override
    public Boolean exists(Integer deliveryNotePOS,Boolean fake) {
        return this.deliveryNoteEnumeratorDAO.exists(deliveryNotePOS,fake);
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
