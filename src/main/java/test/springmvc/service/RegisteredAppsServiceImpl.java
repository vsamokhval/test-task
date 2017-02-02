package test.springmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.springmvc.dao.RegisteredAppsDao;
import test.springmvc.model.RegisteredApp;
import test.springmvc.model.User;
import test.springmvc.utils.UserUtils;

import java.util.List;

@Service("registeredAppsService")
@Transactional
public class RegisteredAppsServiceImpl implements RegisteredAppsService{

    @Autowired
    private RegisteredAppsDao dao;

    @Autowired
    private UserUtils userUtils;

    @Override
    public RegisteredApp findById(int id) {
        return dao.findById(id);
    }

    @Override
    public void save(RegisteredApp registeredApp) {
        dao.save(registeredApp);
    }

    @Override
    public void deleteById(Integer id) {
        dao.deleteById(id);
    }

    @Override
    public List<RegisteredApp> findAllRegisteredApps(User user) {
        List<RegisteredApp> result;
        if (userUtils.isOperator(user)) {
            result = dao.findAllRegisteredApps();
        } else {
            result = dao.findRegisteredAppsByCreatorId(user.getId());
        }
        return result;
    }

    @Override
    public List<RegisteredApp> findRegisteredAppsByCreatorId(Integer createdBy) {
        return dao.findRegisteredAppsByCreatorId(createdBy);
    }
}
