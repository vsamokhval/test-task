package test.springmvc.service;


import test.springmvc.model.RegisteredApp;
import test.springmvc.model.User;

import java.util.List;

public interface RegisteredAppsService {

    RegisteredApp findById(int id);

    void save(RegisteredApp registeredApp);

    void deleteById(Integer id);

    List<RegisteredApp> findAllRegisteredApps(User user);

    List<RegisteredApp> findRegisteredAppsByCreatorId(Integer createdBy);
}
