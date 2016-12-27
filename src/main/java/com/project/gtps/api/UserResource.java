package com.project.gtps.api;

import com.project.gtps.domain.Device;
import com.project.gtps.domain.Group;
import com.project.gtps.domain.User;
import com.project.gtps.service.UserService;
import com.project.gtps.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suresh on 12/24/16.
 */
@Path("/user")
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private UserService userService = new UserServiceImpl();


    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_PLAIN})
    public Response userLogin(@FormParam("userName") String userName, @FormParam("password") String password) {

        logger.info("logging in user with username = '{}'", userName);

        try {
            Boolean isValid = userService.validateUser(userName, password);
            if (isValid) {
                logger.info("Successful login of User '{}' ", userName);
                return Response.ok("User login sucessfully!!").build();
            } else {
                logger.info("Login of User '{}' is failed", userName);
                return Response.status(401).build();
            }


        } catch (Exception ex) {
            logger.error(" Exception while logging user '{}' : '{}'", userName, ex);
            return Response.status(500).build();
        }

    }


    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_PLAIN})
    public Response addUser(@FormParam("username") String username, @FormParam("email") String email, @FormParam("password") String password,
                            @FormParam("deviceId") String deviceId, @FormParam("deviceName") String deviceName,
                            @FormParam("platform") String platform) {


        logger.info("Adding user with name = '{}' and email = '{}'", username, email);
        try {
            User user = new User();
            user.setUserName(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setIsDelete(0);

            List<Device> deviceList = new ArrayList<Device>();
            Device device = new Device();
            device.setDeviceName(deviceName);
            device.setDeviceId(deviceId);
            device.setPlatform(platform);
            deviceList.add(device);

            user.setDevices(deviceList);

            List<Group> groupList = new ArrayList<Group>();
            user.setGroups(groupList);
            userService.save(user);

            logger.info("User added sucessfully with username : '{}'", username);
            return Response.ok("User added sucessfully!!").build();

        } catch (Exception ex) {
            logger.error(" Exception while adding user : '{}'", ex);
            return Response.status(500).build();
        }


    }

    @GET
    @Path("search")
    @Produces({MediaType.APPLICATION_JSON})
    public Response display(@QueryParam("username") String username) {

        logger.info("Searching user with usernames matching with '{}'", username);
        try {
            List<User> userList = userService.findMatchingUser(username);
            userList.forEach(user -> System.out.println(user));

            logger.info("Searching completed for name : '{}'", username);
            return Response.ok(userList).build();

        } catch (Exception ex) {
            logger.error(" Exception while searching user with name '{}' : '{}'", username, ex);
            return Response.status(500).build();
        }


    }

    @Path("group/add")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_PLAIN})
    public Response addGroup(@FormParam("groupName") String groupName, @FormParam("userId") Long userId) {

        logger.info("Adding group with name = '{}' and of userId = '{}'", groupName, userId);

        try {
            User user = userService.findOne(userId);
            List<Group> groupList = user.getGroups();
            Group group = new Group();
            group.setGroupName(groupName);
            groupList.add(group);

            user.setGroups(groupList);
            userService.update(user);

            logger.info("Creating group completed for groupName : '{}'", groupName);
            return Response.ok("User group created sucessfully!!").build();

        } catch (Exception ex) {
            logger.error(" Exception while creating group with name '{}' : '{}'", groupName, ex);
            return Response.status(500).build();
        }


    }

    @Path("group/{userId}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserGroup(@PathParam("userId") Long userId) {

        try {
            User user = userService.findOne(userId);
            List<Group> groupList = user.getGroups();

            logger.info("Group listed for user with id : '{}'", userId);
            return Response.ok(groupList).build();

        } catch (Exception ex) {
            logger.error(" Exception while fetching groups with userId '{}' : '{}'", userId, ex);
            return Response.status(500).build();
        }
    }

}
