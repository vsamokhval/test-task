package test.springmvc.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.springmvc.dao.UserDao;
import test.springmvc.model.User;
import test.springmvc.model.UserProfile;
import test.springmvc.model.UserProfileType;
import test.springmvc.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    UserDao dao;

    @Mock
    UserUtils userUtils;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Spy
    List<User> users = new ArrayList<User>();

    @BeforeClass
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        users = getUsers();
    }


    @Test
    public void findById() {
        User user = getUser();
        when(dao.findById(anyInt())).thenReturn(user);
        Assert.assertEquals(userService.findById(anyInt()), user);
    }

    @Test
    public void findBySSO() {
        User user = getUser();
        when(dao.findBySSO(anyString())).thenReturn(user);
        Assert.assertEquals(userService.findBySSO(anyString()), user);
    }

    @Test
    public void saveUser() {
        doNothing().when(dao).save(any(User.class));
        when(passwordEncoder.encode(anyString())).thenReturn("abc125");
        userService.saveUser(getUser());
        verify(dao, atLeastOnce()).save(any(User.class));
    }

    @Test
    public void updateUser() {
        User updatedUser = getOldUser();
        User user = getUser();
        when(dao.findById(anyInt())).thenReturn(user);
        userService.updateUser(updatedUser);
        verify(dao, atLeastOnce()).findById(anyInt());

        Assert.assertEquals(user.getSsoId(), updatedUser.getSsoId());
        Assert.assertEquals(user.getEmail(), updatedUser.getEmail());
        Assert.assertEquals(user.getFirstName(), updatedUser.getFirstName());
        Assert.assertEquals(user.getLastName(), updatedUser.getLastName());
        Assert.assertEquals(user.getUserProfiles(), updatedUser.getUserProfiles());
    }

    @Test
    public void deleteUserBySSO() {
        doNothing().when(dao).deleteBySSO(anyString());
        userService.deleteUserBySSO(anyString());
        verify(dao, atLeastOnce()).deleteBySSO(anyString());
    }

    @Test
    public void findAllUsersWhenUserIsAdmin() {
        when(userUtils.isAdmin(getUser())).thenReturn(true);
        when(dao.findAllUsers()).thenReturn(users);

        Assert.assertEquals(userService.findAllUsers(getUser()), users);
        verify(dao, atLeastOnce()).findAllUsers();
    }

    @Test
    public void findAllUsersWhenUserIsNotAdmin() {
        when(userUtils.isAdmin(getUser())).thenReturn(false);
        when(dao.findUsersByCreatorId(anyInt())).thenReturn(users);

        Assert.assertEquals(userService.findAllUsers(getUser()), users);
        verify(dao, atLeastOnce()).findUsersByCreatorId(anyInt());
    }

    @Test
    public void isUserSSOUniqueShouldBeFalseWhenFindBySSOReturnsNotNullAndIdIsNull() {
        User user = getUser();
        when(dao.findBySSO(anyString())).thenReturn(user);
        Assert.assertFalse(userService.isUserSSOUnique(null, "admin"));
    }

    @Test
    public void isUserSSOUniqueShouldBeFalseWhenFindBySSOReturnsNotNullAndIdIsNotEqualsUserId() {
        User user = getUser();
        when(dao.findBySSO(anyString())).thenReturn(user);
        Assert.assertFalse(userService.isUserSSOUnique(5, "admin"));
    }

    @Test
    public void isUserSSOUniqueShouldBeTrueWhenFindBySSOReturnsNotNullAndIdIsEqualsUserId() {
        User user = getUser();
        when(dao.findBySSO(anyString())).thenReturn(user);
        Assert.assertTrue(userService.isUserSSOUnique(1, "admin"));
    }

    @Test
    public void isUserSSOUniqueShouldBeTrueWhenFindBySSOReturnsNull() {
        when(dao.findBySSO(anyString())).thenReturn(null);
        Assert.assertTrue(userService.isUserSSOUnique(anyInt(), "admin"));
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(getUser());

        return users;
    }

    public User getUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setFirstName("First");
        user.setLastName("User");
        user.setPassword("abc125");
        user.setSsoId("admin");
        user.setUserProfiles(getUserProfiles());

        return user;
    }

    public User getOldUser() {
        User user = new User();
        user.setId(1);
        user.setPassword("abc125");
        user.setSsoId("admin");

        return user;
    }

    public Set<UserProfile> getUserProfiles() {
        Set<UserProfile> userProfiles = new HashSet<UserProfile>();
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1);
        userProfile.setType(UserProfileType.OPERATOR.getUserProfileType());

        userProfiles.add(userProfile);
        return userProfiles;
    }

}