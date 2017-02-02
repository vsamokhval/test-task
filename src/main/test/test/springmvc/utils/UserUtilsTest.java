package test.springmvc.utils;


import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.springmvc.model.User;
import test.springmvc.model.UserProfile;
import test.springmvc.model.UserProfileType;

import java.util.HashSet;
import java.util.Set;

public class UserUtilsTest {

    @InjectMocks
    UserUtils userUtils;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isAdminSuccess() {
        User user = new User();
        Set<UserProfile> userProfiles = getUserProfilesByType(UserProfileType.ADMIN.getUserProfileType());
        user.setUserProfiles(userProfiles);
        Assert.assertTrue(userUtils.isAdmin(user));
    }

    @Test
    public void isAdminFalse() {
        User user = new User();
        Set<UserProfile> userProfiles = getUserProfilesByType(UserProfileType.OPERATOR.getUserProfileType());
        user.setUserProfiles(userProfiles);
        Assert.assertFalse(userUtils.isAdmin(user));
    }

    @Test
    public void isOperatorSuccess() {
        User user = new User();
        Set<UserProfile> userProfiles = getUserProfilesByType(UserProfileType.OPERATOR.getUserProfileType());
        user.setUserProfiles(userProfiles);
        Assert.assertTrue(userUtils.isOperator(user));
    }


    @Test
    public void isOperatorFalse() {
        User user = new User();
        Set<UserProfile> userProfiles = getUserProfilesByType(UserProfileType.PUBLISHER.getUserProfileType());
        user.setUserProfiles(userProfiles);
        Assert.assertFalse(userUtils.isOperator(user));
    }

    private Set<UserProfile> getUserProfilesByType(String type) {
        Set<UserProfile> userProfiles = new HashSet<UserProfile>();
        UserProfile userProfile = new UserProfile();
        userProfile.setId(3);
        userProfile.setType(type);
        userProfiles.add(userProfile);
        return userProfiles;
    }
}