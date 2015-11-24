package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.DeliveryNoteConfig;

import java.util.List;

public interface DeliveryNoteConfigDAO {

    void save(DeliveryNoteConfig deliveryNoteConfig);

    DeliveryNoteConfig get(String key);

    List<DeliveryNoteConfig> getAll();
}
