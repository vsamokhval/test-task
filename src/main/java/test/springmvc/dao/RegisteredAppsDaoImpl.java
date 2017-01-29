package test.springmvc.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import test.springmvc.model.RegisteredApp;

import java.util.List;

@Repository("registeredAppsDao")
public class RegisteredAppsDaoImpl extends AbstractDao<Integer, RegisteredApp> implements RegisteredAppsDao  {

    static final Logger logger = LoggerFactory.getLogger(RegisteredAppsDaoImpl.class);

    @Override
    public RegisteredApp findById(int id) {
        RegisteredApp registeredApp = getByKey(id);
        return registeredApp;
    }

    @Override
    public void save(RegisteredApp registeredApp) {
        persist(registeredApp);
    }

    @Override
    public void deleteById(Integer id) {
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("id", id));
        RegisteredApp registeredApp = (RegisteredApp)crit.uniqueResult();
        delete(registeredApp);
    }

    @Override
    public List<RegisteredApp> findAllRegisteredApps() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
        List<RegisteredApp> registeredApp = (List<RegisteredApp>) criteria.list();

        return registeredApp;
    }

    @Override
    public List<RegisteredApp> findRegisteredAppsByCreatorId(Integer addedBy) {
        Criteria criteria = createEntityCriteria().add(Restrictions.eq("addedBy", addedBy)).addOrder(Order.asc("name"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<RegisteredApp> registeredApp = (List<RegisteredApp>) criteria.list();

        return registeredApp;
    }
}
