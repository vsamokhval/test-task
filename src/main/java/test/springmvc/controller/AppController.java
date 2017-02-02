package test.springmvc.controller;

import test.springmvc.model.RegisteredApp;
import test.springmvc.model.User;
import test.springmvc.model.UserPrincipal;
import test.springmvc.model.UserProfile;
import test.springmvc.service.RegisteredAppsService;
import test.springmvc.service.UserProfileService;
import test.springmvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import test.springmvc.utils.UserUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;



@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	static final Logger logger = LoggerFactory.getLogger(AppController.class);

	@Autowired
	UserService userService;

	@Autowired
	RegisteredAppsService registeredAppsService;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;

	@Autowired
	UserUtils userUtils;


	/**
	 * This method will list users.
	 */
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers(getPrincipal());
		model.addAttribute("users", users);
		model.addAttribute("userlist", true);
		model.addAttribute("loggedinuser", getUsername());
		return "userslist";
	}

	/**
	 * This method will list registered applications.
	 */
	@RequestMapping(value = { "/appslist" }, method = RequestMethod.GET)
	public String listRegisteredApps(ModelMap model) {

		List<RegisteredApp> registeredApps = registeredAppsService.findAllRegisteredApps(getPrincipal());
		model.addAttribute("registeredApps", registeredApps);
		model.addAttribute("userlist", false);
		model.addAttribute("loggedinuser", getUsername());
		return "appslist";
	}

	/**
	 * This method will provide the medium to add a new app.
	 */
	@RequestMapping(value = { "/addApp" }, method = RequestMethod.GET)
	public String newApp(ModelMap model) {
		RegisteredApp newApp = new RegisteredApp();
		newApp.setAddedBy(getPrincipal().getId());
		model.addAttribute("newApp", newApp);
		model.addAttribute("edit", false);
		model.addAttribute("userlist", false);
		model.addAttribute("loggedinuser", getUsername());
		return "addApp";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving app in database. It also validates the app input
	 */
	@RequestMapping(value = { "/addApp" }, method = RequestMethod.POST)
	public String saveApp(@Valid RegisteredApp app, BindingResult result,
						   ModelMap model) {

		if (result.hasErrors()) {
			return "addApp";
		}

		registeredAppsService.save(app);

		model.addAttribute("success", "Application " + app.getName() + " registered successfully");
		model.addAttribute("userlist", false);
		model.addAttribute("loggedinuser", getUsername());

		return "appregistrationsuccess";
	}


	/**
	 * This method will delete an app by it's ID value.
	 */
	@RequestMapping(value = { "/delete-app-{id}" }, method = RequestMethod.GET)
	public String deleteApp(@PathVariable Integer id) {
		registeredAppsService.deleteById(id);
		return "redirect:/appslist";
	}

	/**
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		User user = new User();
		List<UserProfile> roles= userProfileService.findDependsOnUserType(getPrincipal());
		model.addAttribute("user", user);
		model.addAttribute("roles", roles);
		model.addAttribute("edit", false);
		model.addAttribute("userlist", true);
		model.addAttribute("loggedinuser", getUsername());
		return "registration";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation
		 * and applying it on field [sso] of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
		 * framework as well while still using internationalized messages.
		 * 
		 */
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}

		user.setCreatedBy(getPrincipal().getId());
		userService.saveUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " registered successfully");
		model.addAttribute("userlist", true);
		model.addAttribute("loggedinuser", getUsername());
		//return "success";
		return "registrationsuccess";
	}


	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssoId, ModelMap model) {
		User user = userService.findBySSO(ssoId);
		List<UserProfile> roles= userProfileService.findDependsOnUserType(getPrincipal());
		model.addAttribute("user", user);
		model.addAttribute("roles", roles);
		model.addAttribute("edit", true);
		model.addAttribute("userlist", true);
		model.addAttribute("loggedinuser", getUsername());
		return "registration";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
	public String updateUser(@Valid User user, BindingResult result,
			ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}*/


		userService.updateUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " updated successfully");
		model.addAttribute("userlist", true);
		model.addAttribute("loggedinuser", getUsername());
		return "registrationsuccess";
	}


	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssoId) {
		userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}


	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getUsername());
		return "accessDenied";
	}

	/**
	 * This method handles login GET requests.
	 * If users is already logged-in and tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String loginPage() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
	    } else if (userUtils.isAdmin(getPrincipal())){
	    	return "redirect:/list";
	    } else {
			return "redirect:/appslist";
		}
	}

	/**
	 * This method handles logout requests.
	 * Toggle the handlers if you are RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private User getPrincipal(){
		User user = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

 		if (principal instanceof UserPrincipal) {
			user = ((UserPrincipal) principal).getUser();
		}

		logger.info("user : {}", user);

		return user;
	}

	private String getUsername() {
		return getPrincipal() != null ? getPrincipal().getSsoId() : "";
	}

	/**
	 * This method returns true if users is already authenticated [logged-in], else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
	    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return authenticationTrustResolver.isAnonymous(authentication);
	}


}