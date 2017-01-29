package test.springmvc.dao;


import test.springmvc.model.RegisteredApp;

import java.util.List;

public interface RegisteredAppsDao {

    RegisteredApp findById(int id);

    void save(RegisteredApp registeredApp);

    void deleteById(Integer id);

    List<RegisteredApp> findAllRegisteredApps();

    List<RegisteredApp> findRegisteredAppsByCreatorId(Integer createdBy);
}
