package test.springmvc.utils;


import test.springmvc.model.User;
import test.springmvc.model.UserProfile;
import test.springmvc.model.UserProfileType;

public class UserUtils {
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
}
