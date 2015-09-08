package com.drogueria.service.impl;

import com.drogueria.model.Profile;
import com.drogueria.model.Province;
import com.drogueria.persistence.dao.GenericDAO;
import com.drogueria.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    private GenericDAO<Profile> genericDAO;

    @Override
    public void save(Profile profile) {
        this.genericDAO.save(profile);
    }

    @Override
    public Profile get(Integer id) {
        return this.genericDAO.get(Profile.class, id);
    }

    @Override
    public List<Profile> getAll(){
        return this.genericDAO.getAll(Profile.class);
    }
}
