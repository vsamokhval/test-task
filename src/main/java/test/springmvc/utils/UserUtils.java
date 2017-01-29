package test.springmvc.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.springmvc.model.User;
import test.springmvc.model.UserProfile;
import test.springmvc.model.UserProfileType;

public class UserUtils {

    static final Logger logger = LoggerFactory.getLogger(UserUtils.class);

    public static boolean isAdmin(User user) {
        boolean result = false;
        if (null == user) {
            return result;
        }
        for(UserProfile userProfile : user.getUserProfiles()){
            if (userProfile.getType().equals(UserProfileType.ADMIN.getUserProfileType())) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isOperator(User user) {
        boolean result = false;
        if (null == user) {
            return result;
        }
        for(UserProfile userProfile : user.getUserProfiles()){
            if (userProfile.getType().equals(UserProfileType.OPERATOR.getUserProfileType())) {
                result = true;
            }
        }
        return result;
    }
}
