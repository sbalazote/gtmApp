package com.drogueria.service;

import com.drogueria.model.Profile;

import java.util.List;

public interface ProfileService {

    void save(Profile profile);

    Profile get(Integer id);

    List<Profile> getAll();

}
