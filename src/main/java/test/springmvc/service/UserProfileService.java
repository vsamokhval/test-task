package test.springmvc.service;

import java.util.List;

import test.springmvc.model.User;
import test.springmvc.model.UserProfile;


public interface UserProfileService {

	UserProfile findById(int id);

	UserProfile findByType(String type);
	
	List<UserProfile> findAll();

	List<UserProfile> findDependsOnUserType(User user);
	
}
