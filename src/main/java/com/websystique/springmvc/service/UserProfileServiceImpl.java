package com.websystique.springmvc.service;

import java.util.ArrayList;
import java.util.List;

import com.websystique.springmvc.model.User;
import com.websystique.springmvc.model.UserProfileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springmvc.dao.UserProfileDao;
import com.websystique.springmvc.model.UserProfile;

import static com.websystique.springmvc.utils.UserUtils.isAdmin;


@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService{

	static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);
	
	@Autowired
	UserProfileDao dao;
	
	public UserProfile findById(int id) {
		return dao.findById(id);
	}

	public UserProfile findByType(String type){
		return dao.findByType(type);
	}

	public List<UserProfile> findAll() {
		return dao.findAll();
	}

	public List<UserProfile> findDependsOnUserType(User user) {
		List<UserProfile> result;

		logger.info("isAdmin: ", isAdmin(user));

		if (isAdmin(user)) {
			result = this.findAll();
		} else {
			UserProfile userProfile = this.findByType(UserProfileType.PUBLISHER.getUserProfileType());
			result = new ArrayList<UserProfile>();
			result.add(userProfile);
		}
		return result;
	}
}
