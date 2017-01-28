package com.websystique.springmvc.utils;


import com.websystique.springmvc.model.User;
import com.websystique.springmvc.model.UserProfile;
import com.websystique.springmvc.model.UserProfileType;

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
