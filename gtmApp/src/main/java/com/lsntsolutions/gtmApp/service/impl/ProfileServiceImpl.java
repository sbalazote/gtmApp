package com.lsntsolutions.gtmApp.service.impl;

import com.lsntsolutions.gtmApp.model.Profile;
import com.lsntsolutions.gtmApp.persistence.dao.ProfileDAO;
import com.lsntsolutions.gtmApp.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileDAO profileDAO;

    @Override
    public void save(Profile profile) {
        this.profileDAO.save(profile);
    }

    @Override
    public Profile get(Integer id) {
        return this.profileDAO.get(id);
    }

    @Override
    public List<Profile> getAll(){
        return this.profileDAO.getAll();
    }

    @Override
    public List<Profile> getPaginated(int start, int length) {
        return this.profileDAO.getPaginated(start,length);
    }

    @Override
    public long getTotalNumber() {
        return this.profileDAO.getTotalNumber();
    }

    @Override
    public List<Profile> getForAutocomplete(String term, Boolean active) {
        return this.profileDAO.getForAutocomplete(term, active);
    }

    @Override
    public boolean delete(Integer profileId) {
        return this.profileDAO.delete(profileId);
    }

    @Override
    public Boolean exists(String description) {
        return this.profileDAO.exists(description);
    }
}
