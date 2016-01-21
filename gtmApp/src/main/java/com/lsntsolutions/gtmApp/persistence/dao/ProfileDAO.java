package com.lsntsolutions.gtmApp.persistence.dao;

import com.lsntsolutions.gtmApp.model.Profile;

import java.util.List;

public interface ProfileDAO {

    void save(Profile profile);

    Profile get(Integer id);

    List<Profile> getAll();

    List<Profile> getPaginated(int start, int length);

    long getTotalNumber();

    List<Profile> getForAutocomplete(String term, Boolean active, String sortId, String sortDescription);

    boolean delete(Integer profileId);

    Boolean exists(String name);
}
