package test.springmvc.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.springmvc.dao.RegisteredAppsDao;
import test.springmvc.model.RegisteredApp;
import test.springmvc.model.User;
import test.springmvc.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class RegisteredAppsServiceImplTest {

    @Mock
    RegisteredAppsDao dao;

    @Mock
    UserUtils userUtils;

    @InjectMocks
    RegisteredAppsServiceImpl registeredAppsService;

    @Spy
    List<RegisteredApp> registeredApps = new ArrayList<RegisteredApp>();

    @BeforeClass
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        registeredApps = getRegisteredApps();
    }

    @Test
    public void findById() {
        RegisteredApp registeredApp = getRegisteredApps().get(0);
        when(dao.findById(anyInt())).thenReturn(registeredApp);
        Assert.assertEquals(registeredAppsService.findById(registeredApp.getId()), registeredApp);
    }

    @Test
    public void findRegisteredAppsByCreatorId() {
        when(dao.findRegisteredAppsByCreatorId(anyInt())).thenReturn(registeredApps);
        Assert.assertEquals(registeredAppsService.findRegisteredAppsByCreatorId(anyInt()), registeredApps);
    }

    @Test
    public void findAllRegisteredAppsWhenUserIsOperator() {
        when(dao.findAllRegisteredApps()).thenReturn(registeredApps);
        when(userUtils.isOperator(getUser())).thenReturn(true);
        Assert.assertEquals(registeredAppsService.findAllRegisteredApps(getUser()), registeredApps);
        verify(dao, atLeastOnce()).findAllRegisteredApps();
    }

    @Test
    public void findAllRegisteredAppsWhenUserIsNotOperator() {
        when(dao.findRegisteredAppsByCreatorId(anyInt())).thenReturn(registeredApps);
        when(userUtils.isOperator(getUser())).thenReturn(false);
        Assert.assertEquals(registeredAppsService.findAllRegisteredApps(getUser()), registeredApps);
        verify(dao, atLeastOnce()).findRegisteredAppsByCreatorId(anyInt());
    }

    @Test
    public void save() {
        doNothing().when(dao).save(any(RegisteredApp.class));
        registeredAppsService.save(any(RegisteredApp.class));
        verify(dao, atLeastOnce()).save(any(RegisteredApp.class));
    }

    @Test
    public void deleteById() {
        doNothing().when(dao).deleteById(anyInt());
        registeredAppsService.deleteById(anyInt());
        verify(dao, atLeastOnce()).deleteById(anyInt());
    }

    private List<RegisteredApp> getRegisteredApps() {
        RegisteredApp registeredApp = new RegisteredApp();
        registeredApp.setId(1);
        registeredApp.setAddedBy(11);
        registeredApp.setDescription("desc");
        registeredApp.setName("app");
        registeredApp.setUrl("url");

        RegisteredApp registeredApp2 = new RegisteredApp();
        registeredApp.setId(2);
        registeredApp.setAddedBy(11);
        registeredApp.setDescription("desc2");
        registeredApp.setName("app2");
        registeredApp.setUrl("url2");

        registeredApps.add(registeredApp);
        registeredApps.add(registeredApp2);
        return registeredApps;
    }

    public User getUser() {
        User user = new User();
        user.setId(1);
        user.setSsoId("admin");

        return user;
    }


}