package test.springmvc.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.springmvc.dao.UserProfileDao;
import test.springmvc.model.User;
import test.springmvc.model.UserProfile;
import test.springmvc.model.UserProfileType;
import test.springmvc.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UserProfileServiceImplTest {

    @Mock
    UserProfileDao dao;

    @Mock
    UserUtils userUtils;

    @InjectMocks
    UserProfileServiceImpl profileService;

    @Spy
    List<UserProfile> userProfiles = new ArrayList<UserProfile>();

    @BeforeClass
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        userProfiles = getUserProfiles();
    }

    @Test
    public void findById() {
        UserProfile userProfile = userProfiles.get(0);
        when(dao.findById(anyInt())).thenReturn(userProfile);
        Assert.assertEquals(profileService.findById(anyInt()), userProfile);
    }

    @Test
    public void findByType() {
        UserProfile userProfile = userProfiles.get(0);
        when(dao.findByType(anyString())).thenReturn(userProfile);
        Assert.assertEquals(profileService.findByType(anyString()), userProfile);
    }

    @Test
    public void findAll() {
        when(dao.findAll()).thenReturn(userProfiles);
        Assert.assertEquals(profileService.findAll(), userProfiles);
    }

    @Test
    public void findDependsOnUserTypeIsAdminUser() {
        when(userUtils.isAdmin(getUser())).thenReturn(true);
        when(dao.findAll()).thenReturn(userProfiles);

        Assert.assertEquals(profileService.findDependsOnUserType(getUser()), userProfiles);
        verify(dao, atLeastOnce()).findAll();
    }

    @Test
    public void findDependsOnUserTypeIsNotAdminUser() {
        User user = getUser();
        UserProfile userProfile = userProfiles.get(0);
        when(userUtils.isAdmin(user)).thenReturn(false);
        when(dao.findByType(anyString())).thenReturn(userProfile);

        Assert.assertEquals(profileService.findDependsOnUserType(user), userProfiles);
        verify(dao, atLeastOnce()).findByType(anyString());
    }

    public List<UserProfile> getUserProfiles() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1);
        userProfile.setType(UserProfileType.OPERATOR.getUserProfileType());

        userProfiles.add(userProfile);
        return userProfiles;
    }
    public User getUser() {
        User user = new User();
        user.setId(1);
        user.setSsoId("admin");

        return user;
    }

}