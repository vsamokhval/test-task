package test.springmvc.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import test.springmvc.model.User;
import test.springmvc.model.UserProfile;
import test.springmvc.model.UserProfileType;

@Service("userUtils")
public class UserUtils {

    static final Logger logger = LoggerFactory.getLogger(UserUtils.class);

    public boolean isAdmin(User user) {
        return isUserHasCurrentType(user, UserProfileType.ADMIN.getUserProfileType());
    }

    public boolean isOperator(User user) {
        return isUserHasCurrentType(user, UserProfileType.OPERATOR.getUserProfileType());
    }

    private boolean isUserHasCurrentType(User user, String type) {
        boolean result = false;
        if (null == user) {
            return result;
        }
        for(UserProfile userProfile : user.getUserProfiles()){
            if (userProfile.getType().equals(type)) {
                result = true;
            }
        }
        return result;
    }
}
