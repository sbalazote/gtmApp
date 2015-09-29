package com.lsntsolutions.gtmApp.service.impl;

import java.util.List;

import com.lsntsolutions.gtmApp.dto.NewPasswordDTO;
import com.lsntsolutions.gtmApp.helper.EncryptionHelper;
import com.lsntsolutions.gtmApp.persistence.dao.UserDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsntsolutions.gtmApp.model.User;
import com.lsntsolutions.gtmApp.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	@Override
	public void save(User user) {
		this.userDAO.save(user);
		logger.info("Se han guardado los cambios exitosamente. Id de Usuario: " + user.getId());
	}

	@Override
	public User get(Integer id) {
		return this.userDAO.get(id);
	}

	@Override
	public User getByName(String name) {
		return this.userDAO.getByName(name);
	}

	@Override
	public Boolean exists(String name) {
		return this.userDAO.exists(name);
	}

	@Override
	public List<User> getForAutocomplete(String term, Boolean active) {
		return this.userDAO.getForAutocomplete(term, active);
	}

	@Override
	public boolean delete(Integer userId) {
		return this.userDAO.delete(userId);
	}

	@Override
	public List<User> getAll() {
		return this.userDAO.getAll();
	}

	@Override
	public List<User> getPaginated(int start, int length) {
		return this.userDAO.getPaginated(start, length);
	}

	@Override
	public Long getTotalNumber() {
		return this.userDAO.getTotalNumber();
	}

	@Override
	public Boolean changePassword(String username,NewPasswordDTO newPasswordDTO) {
		User user = this.userDAO.getByName(username);
        String hashedPassword = EncryptionHelper.generateHash(username + newPasswordDTO.getActualPassword());

        if (hashedPassword.equals(user.getPassword())) {
            user.setPassword(EncryptionHelper.generateHash(username + newPasswordDTO.getNewPassword()));
			this.save(user);
			return true;
		}else{
			return false;
		}
	}
}
