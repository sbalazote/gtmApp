package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.DeliveryNoteConfig;

import java.util.Map;

public interface DeliveryNoteConfigService {

    void save(DeliveryNoteConfig agent);

    DeliveryNoteConfig get(String key);

    Map<String, Integer> getAll();

    Map<String, Float> getAllInMillimiters();

    void saveAll(Map<String, Integer> deliveryNoteConfigServiceAll);
}
