package test.springmvc.controller;

import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.springmvc.model.*;
import test.springmvc.service.RegisteredAppsService;
import test.springmvc.service.UserProfileService;
import test.springmvc.service.UserService;
import test.springmvc.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class AppControllerTest {
    static final Logger logger = LoggerFactory.getLogger(AppControllerTest.class);


    @Mock
    UserService userService;

    @Mock
    UserProfileService userProfileService;

    @Mock
    RegisteredAppsService registeredAppsService;

    @InjectMocks
    AppController appController;

    @Spy
    List<User> users = new ArrayList<User>();

    @Spy
    List<UserProfile> userRoles = new ArrayList<UserProfile>();

    @Spy
    List<RegisteredApp> registeredApps = new ArrayList<RegisteredApp>();

    @Spy
    ModelMap model;

    @Mock
    BindingResult result;

    @Mock
    MessageSource messageSource;

    @Mock
    AuthenticationTrustResolver authenticationTrustResolver;

    @Mock
    UserUtils userUtils;

    Authentication authentication;
    SecurityContext securityContext;
    UserPrincipal userPrincipal;

    @BeforeClass
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        users = getUsersList();
        userRoles = getUsersProfileList();
        registeredApps = getRegisteredApps();

        securityContext = Mockito.mock(SecurityContext.class);
        authentication = Mockito.mock(Authentication.class);
        userPrincipal = Mockito.mock(UserPrincipal.class);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userPrincipal.getUser()).thenReturn(getUser());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void newUser(){
        when(userProfileService.findDependsOnUserType(getUser())).thenReturn(userRoles);
        Assert.assertEquals(appController.newUser(model), "registration");
        Assert.assertNotNull(model.get("user"));
        Assert.assertEquals(model.get("roles"), userRoles);
        Assert.assertFalse((Boolean)model.get("edit"));
        Assert.assertTrue((Boolean)model.get("userlist"));
        Assert.assertEquals(((User)model.get("user")).getId(), null);
    }

    @Test
    public void listUsers() {
        when(userService.findAllUsers(getUser())).thenReturn(users);
        Assert.assertEquals(appController.listUsers(model), "userslist");
        Assert.assertEquals(model.get("users"), users);
        Assert.assertTrue((Boolean)model.get("userlist"));
        Assert.assertEquals(model.get("loggedinuser"), "admin");
    }

    @Test
    public void listRegisteredApps() {
        when(registeredAppsService.findAllRegisteredApps(getUser())).thenReturn(registeredApps);
        Assert.assertEquals(appController.listRegisteredApps(model), "appslist");
        Assert.assertEquals(model.get("registeredApps"), registeredApps);
        Assert.assertFalse((Boolean)model.get("userlist"));
        Assert.assertEquals(model.get("loggedinuser"), "admin");
    }

    @Test
    public void newApp() {
        RegisteredApp newApp = new RegisteredApp();
        newApp.setAddedBy(1);
        Assert.assertEquals(appController.newApp(model), "addApp");
        Assert.assertEquals(model.get("newApp"), newApp);
        Assert.assertFalse((Boolean)model.get("userlist"));
        Assert.assertFalse((Boolean)model.get("edit"));
        Assert.assertEquals(model.get("loggedinuser"), "admin");

    }

    @Test
    public void saveApp() {
        model.addAttribute("success", null);
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(registeredAppsService).save(any(RegisteredApp.class));
        Assert.assertEquals(appController.saveApp(registeredApps.get(0), result, model), "appregistrationsuccess");
        Assert.assertEquals(model.get("success"), "Application app registered successfully");
    }

    @Test
    public void saveAppWithValidationError() {
        model.addAttribute("success", null);
        when(result.hasErrors()).thenReturn(true);
        doNothing().when(registeredAppsService).save(any(RegisteredApp.class));
        Assert.assertEquals(appController.saveApp(registeredApps.get(0), result, model), "addApp");
        Assert.assertNull(model.get("success"));
    }

    @Test
    public void deleteApp() {
        doNothing().when(registeredAppsService).deleteById(anyInt());
        Assert.assertEquals(appController.deleteApp(12), "redirect:/appslist");
    }

    @Test
    public void saveUserWIthSuccess() {
        when(result.hasErrors()).thenReturn(false);
        when(userService.isUserSSOUnique(anyInt(), anyString())).thenReturn(true);
        doNothing().when(userService).saveUser(any(User.class));
        Assert.assertEquals(appController.saveUser(users.get(0), result, model), "registrationsuccess");
        Assert.assertEquals(model.get("success"), "User First User registered successfully");
    }

    @Test
    public void saveUserWIthValidationError() {
        when(result.hasErrors()).thenReturn(true);
        doNothing().when(userService).saveUser(any(User.class));
        Assert.assertEquals(appController.saveUser(users.get(0), result, model), "registration");
    }

    @Test
    public void saveUserWIthNonUniqueSSO() {
        when(result.hasErrors()).thenReturn(false);
        when(userService.isUserSSOUnique(anyInt(), anyString())).thenReturn(false);
        doNothing().when(userService).saveUser(any(User.class));
        Assert.assertEquals(appController.saveUser(users.get(0), result, model), "registration");
    }

    @Test
    public void editUser(){
        User user = users.get(0);
        when(userService.findBySSO(anyString())).thenReturn(user);
        when(userProfileService.findDependsOnUserType(getUser())).thenReturn(userRoles);
        Assert.assertEquals(appController.editUser(anyString(), model), "registration");
        Assert.assertNotNull(model.get("user"));
        Assert.assertNotNull(model.get("roles"));
        Assert.assertTrue((Boolean)model.get("userlist"));
        Assert.assertTrue((Boolean)model.get("edit"));
        Assert.assertEquals(model.get("loggedinuser"), "admin");
    }

    @Test
    public void updateUserWithSuccess(){
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(userService).updateUser(any(User.class));
        Assert.assertEquals(appController.updateUser(users.get(0), result, model, ""), "registrationsuccess");
        Assert.assertEquals(model.get("success"), "User First User updated successfully");
    }

    @Test
    public void updateUserWithValidationError() {
        when(result.hasErrors()).thenReturn(true);
        doNothing().when(userService).saveUser(any(User.class));
        Assert.assertEquals(appController.saveUser(users.get(0), result, model), "registration");
    }

    @Test
    public void deleteUser() {
        doNothing().when(userService).deleteUserBySSO(anyString());
        Assert.assertEquals(appController.deleteUser("123"), "redirect:/list");
    }

    @Test
    public void loginPageWhenAnonymous() {
        when(authenticationTrustResolver.isAnonymous(authentication)).thenReturn(true);
        Assert.assertEquals(appController.loginPage(), "login");
    }

    @Test
    public void loginPageWhenAdmin() {
        when(authenticationTrustResolver.isAnonymous(authentication)).thenReturn(false);
        when(userUtils.isAdmin(getUser())).thenReturn(true);
        Assert.assertEquals(appController.loginPage(), "redirect:/list");
    }

    @Test
    public void loginPageWhenOperatorOrPublisher() {
        when(authenticationTrustResolver.isAnonymous(authentication)).thenReturn(false);
        when(userUtils.isAdmin(getUser())).thenReturn(false);
        Assert.assertEquals(appController.loginPage(), "redirect:/appslist");
    }

    public List<User> getUsersList() {
        User user1 = new User();
        user1.setId(10);
        user1.setSsoId("first-user");
        user1.setFirstName("First");
        user1.setLastName("User");
        user1.setEmail("first-user@example.com");
        user1.setPassword("abc125");
        user1.setCreatedBy(2);

        User user2 = new User();
        user2.setId(11);
        user2.setSsoId("second-user");
        user2.setFirstName("Second");
        user2.setLastName("User");
        user2.setEmail("second-user@example.com");
        user2.setPassword("abc125");
        user2.setCreatedBy(2);

        users.add(user1);
        users.add(user2);
        return users;
    }

    public List<UserProfile> getUsersProfileList() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1);
        userProfile.setType(UserProfileType.PUBLISHER.getUserProfileType());

        userRoles.add(userProfile);
        return userRoles;
    }

    public List<RegisteredApp> getRegisteredApps() {
        RegisteredApp registeredApp = new RegisteredApp();
        registeredApp.setId(1);
        registeredApp.setAddedBy(11);
        registeredApp.setDescription("desc");
        registeredApp.setName("app");
        registeredApp.setUrl("url");

        registeredApps.add(registeredApp);
        return registeredApps;
    }

    public User getUser() {
        User user = new User();
        user.setId(1);
        user.setSsoId("admin");

        return user;
    }
}