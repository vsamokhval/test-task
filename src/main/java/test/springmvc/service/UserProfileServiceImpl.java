package test.springmvc.service;

import java.util.ArrayList;
import java.util.List;

import test.springmvc.model.User;
import test.springmvc.model.UserProfileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import test.springmvc.dao.UserProfileDao;
import test.springmvc.model.UserProfile;
import test.springmvc.utils.UserUtils;


@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService{

	@Autowired
    UserProfileDao dao;

	@Autowired
	private UserUtils userUtils;

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

		if (userUtils.isAdmin(user)) {
			result = dao.findAll();
		} else {
			UserProfile userProfile = dao.findByType(UserProfileType.PUBLISHER.getUserProfileType());
			result = new ArrayList<UserProfile>();
			result.add(userProfile);
		}
		return result;
	}
}
