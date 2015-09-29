package com.lsntsolutions.gtmApp.service;

import com.lsntsolutions.gtmApp.model.Profile;

import java.util.List;

public interface ProfileService {

    void save(Profile profile);

    Profile get(Integer id);

    List<Profile> getAll();

    List<Profile> getPaginated(int start, int length);

    long getTotalNumber();

    List<Profile> getForAutocomplete(String term, Boolean active);

    boolean delete(Integer profileId);

    Boolean exists(String name);
}
