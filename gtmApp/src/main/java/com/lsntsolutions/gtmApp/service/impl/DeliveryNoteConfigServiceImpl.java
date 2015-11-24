package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.model.DeliveryNoteConfig;
import com.lsntsolutions.gtmApp.persistence.dao.DeliveryNoteConfigDAO;
import com.lsntsolutions.gtmApp.service.DeliveryNoteConfigService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeliveryNoteConfigServiceImpl implements DeliveryNoteConfigService {

    // The coordinates are measured in points. 1 inch is divided into 72 points so that 1 Millimeter equals 2.8346 points.
    private float MILLIMITER_TO_POINTS_FACTOR = 2.8346f;

    private float PAGE_HEIGTH = 297.0f;

    private static final Logger logger = Logger.getLogger(DeliveryNoteConfigServiceImpl.class);

    @Autowired
    private DeliveryNoteConfigDAO deliveryNoteConfigDAO;

    @Override
    public void save(DeliveryNoteConfig deliveryNoteConfig) {
        this.deliveryNoteConfigDAO.save(deliveryNoteConfig);
    }

    @Override
    public DeliveryNoteConfig get(String key) {
        return this.deliveryNoteConfigDAO.get(key);
    }

    @Override
    public Map<String, Integer> getAll() {
        Map<String, Integer> dnConfigMap = new HashMap<String, Integer>();
        List<DeliveryNoteConfig> dnConfigList = this.deliveryNoteConfigDAO.getAll();
        Iterator<DeliveryNoteConfig> it = dnConfigList.iterator();
        while (it.hasNext()) {
            DeliveryNoteConfig dnConfig = it.next();
            dnConfigMap.put(dnConfig.getName(), dnConfig.getValue());
        }
        return dnConfigMap;
    }

    @Override
    public Map<String, Float> getAllInMillimiters() {
        Map<String, Float> dnConfigMap = new HashMap<String, Float>();
        List<DeliveryNoteConfig> dnConfigList = this.deliveryNoteConfigDAO.getAll();
        Iterator<DeliveryNoteConfig> it = dnConfigList.iterator();
        while (it.hasNext()) {
            DeliveryNoteConfig dnConfig = it.next();
            if (dnConfig.getName().contains("PRINT") || dnConfig.getName().contains("FONT")) {
                dnConfigMap.put(dnConfig.getName(), (float)dnConfig.getValue());
            } else if (dnConfig.getName().endsWith("_Y")) {
                dnConfigMap.put(dnConfig.getName(), (PAGE_HEIGTH - dnConfig.getValue()) * MILLIMITER_TO_POINTS_FACTOR);
            } else {
                dnConfigMap.put(dnConfig.getName(), dnConfig.getValue() * MILLIMITER_TO_POINTS_FACTOR);
            }

        }
        return dnConfigMap;
    }

    @Override
    public void saveAll(Map<String, Integer> deliveryNoteConfigServiceAll) {
        List<DeliveryNoteConfig> deliveryNoteConfigs = this.deliveryNoteConfigDAO.getAll();
        Iterator it = deliveryNoteConfigs.iterator();
        while (it.hasNext()) {
            DeliveryNoteConfig deliveryNoteConfig = (DeliveryNoteConfig) it.next();
            deliveryNoteConfig.setValue(deliveryNoteConfigServiceAll.get(deliveryNoteConfig.getName()));
            this.deliveryNoteConfigDAO.save(deliveryNoteConfig);
            logger.info("Actualizado Propiedad del Remito: " + deliveryNoteConfig.getName() + " al valor: " + deliveryNoteConfig.getValue());
        }
    }
}
