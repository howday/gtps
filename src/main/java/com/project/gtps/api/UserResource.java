package com.project.gtps.api;

import com.project.gtps.domain.*;
import com.project.gtps.service.TransactionService;
import com.project.gtps.service.UserService;
import com.project.gtps.service.impl.TransactionServiceImpl;
import com.project.gtps.service.impl.UserServiceImpl;
import com.project.gtps.util.HibernateUtil;
import com.project.gtps.util.Notification;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by suresh on 12/24/16.
 */
@Path("/user")
@Transactional
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private UserService userService = new UserServiceImpl();
    private TransactionService transactionService = new TransactionServiceImpl();


    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON})
    public Response userLogin(@FormParam("userName") String userName, @FormParam("password") String password, @FormParam("deviceId") String deviceId) {

        logger.info("logging in user with username = '{}' and deviceId = '{}", userName, deviceId);

        try {
            logger.info("Trying to validate user  = '{}'", userName);
            Boolean isValid = userService.validateUser(userName, password);
            if (isValid) {
                logger.info("Successful validation of User '{}' ", userName);
                User validUser = userService.findByUsername(userName);
                /**
                 * Update the current device of user for notifications
                 */
                logger.info("Setting the current device of user = '{}' with deviceId = '{}'", userName, deviceId);
                validUser.getDevices().get(0).setDeviceId(deviceId);

                userService.update(validUser);
                logger.info("Setting current device for user '{}' is COMPLETED");
                /**
                 *  get the pending transactions of user
                 */
                logger.info("Retrieving current pending transaction of user = '{}'", userName);
                List<Transaction> transactionList = transactionService.findTxOfUser(userName, "PENDING");
                validUser.setTransactionList(transactionList);
                logger.info("Pending transaction set for the user = '{}'", userName);

                return Response.status(200).type(MediaType.APPLICATION_JSON).entity(validUser).build();
            } else {
                logger.info("Login of User '{}' is failed", userName);
                return Response.status(404).type(MediaType.TEXT_PLAIN).entity("User not found").build();
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

            User u = userService.findByUsername(username);
            if (u != null) {
                logger.info("Username = '{}' is already in use.", username);
                return Response.status(405).type(MediaType.TEXT_PLAIN).entity("Username already in use.").build();
            }
            u = userService.findByEmail(email);
            if (u != null) {
                logger.info("Email = '{}' is already in use.", email);
                return Response.status(405).type(MediaType.TEXT_PLAIN).entity("Email already in use.").build();
            }


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
            logger.info("Searching completed for name : '{}'", username);
            return Response.ok(userList).build();

        } catch (Exception ex) {
            logger.error(" Exception while searching user with name '{}' : '{}'", username, ex);
            return Response.status(500).build();
        }


    }

    @GET
    @Path("push")
    @Produces({MediaType.APPLICATION_JSON})
    public Response sendNotification() {

        String message = "This is message from server";
        String deviceId = "exv0abrfcb0:APA91bHpJsx5gk3G7cS-P664UxYFuGmglawsQZyoPdZwCHDlc1vqafslYzGBLh2Mx-H0AEd7eG-fY65P6LMwH4dz-rLSdJmxEEmh78qOA2UKq6Itg490NpzRk0cJ3nWQgG-gnpzBEqVL";

        System.out.println("Sending notification");
        logger.info("Sending notification");
        try {
            System.out.println("Creating sender");
            System.out.println("sender created and opeing connection");
            System.out.println("connection created");
            Notification.send(message, deviceId);
            System.out.println("sent....");
            return Response.ok("Push notification sent !!").build();

        } catch (Exception ex) {

            logger.error(ex.getMessage());
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return Response.status(500).build();
        }


    }

    @Path("group/add")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_PLAIN})
    public Response addGroup(@FormParam("groupName") String groupName, @FormParam("userName") String userName) {

        logger.info("Adding group with name = '{}' and of userId = '{}'", groupName, userName);

        try {
            User user = userService.findByUsername(userName);
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

    @Path("group/{userName}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserGroup(@PathParam("userName") String userName) {

        logger.info("Fetching all users in all groups of user = '{}'", userName);

        try {
            User user = userService.findByUsername(userName);
            List<Group> groupList = new ArrayList<>();

            try {
                for (Group g : user.getGroups()) {
                    String sql = "select username from user where id in (select user_id from user_group where group_id=:gid)";
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Query query = session.createSQLQuery(sql);
                    query.setParameter("gid", g.getId());
                    List users = query.list();
                    g.setUserList(users);
                    session.close();
                    groupList.add(g);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.info("Group listed for user with userName : '{}'", userName);
            return Response.ok(groupList).build();

        } catch (Exception ex) {
            logger.error(" Exception while fetching groups with userName '{}' : '{}'", userName, ex);
            return Response.status(500).build();
        }
    }


    @Path("addUserGroup")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_PLAIN})
    public Response addUserToGroup(@FormParam("groupId") Long groupId, @FormParam("users") String userListString) {


        System.out.println("User to be added: " + userListString + " in group with Id = " + groupId);
        logger.info("User to be added : '{}' in group with Id = '{}'", userListString, groupId);

        Group group = userService.findGroupById(groupId);

        for (String singleUser : userListString.split(",")) {

            User user = userService.findByUsername(singleUser);
            List<Group> userGroupList = user.getGroups();
            userGroupList.add(group);
            userService.update(user);
            logger.info("User '{}' is added to group '{}'", singleUser, group);

        }

        return Response.ok("User added to group").build();
    }


    @Path("requestMoney")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.TEXT_PLAIN})
    public Response requestMoney(@FormParam("requestFrom") String requestFrom, @FormParam("requestTo") String requestTo, @FormParam("requestSum") Double amount, @FormParam("description") String description) {

        logger.info("Cash - '{}' requested to '{}' by user '{}' for '{}'", amount, requestTo, requestFrom, description);
        try {


            String message = requestFrom + " has requested payment of " + amount;
            User requester = userService.findByUsername(requestFrom);

            if (description != null && !description.isEmpty()) {
                message += " for " + description;
            }
            List<String> receiverList = Arrays.asList(requestTo.split(","));
            List<String> receivers = receiverList.stream().filter(a -> !a.equalsIgnoreCase(requestFrom)).collect(Collectors.toList());

            for (String singleUser : receivers) {

                logger.info("Sending notification to '{}' for money request", singleUser);
                User u = userService.findByUsername(singleUser);
                Device device = u.getDevices().get(0);//retrieve first device

                logger.info("Device Id of '{}' is '{}'", singleUser, device.getDeviceId());

                Notification.send(message, device.getDeviceId());

                logger.info("Notification delivered to user - '{}'", singleUser);

                Transaction t = new Transaction();
                t.setAmount(amount);
                t.setDescription(description);
                t.setStatus("PENDING");
                t.setRequestToId(u.getUserName());
                t.setRequestFromId(requester.getUserName());
                t.setDate(new Date());

                transactionService.save(t);

            }
            logger.info("Transaction has been added to user = '{}'", requestFrom);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Response.ok("Request sent to members!!").build();
    }


    @Path("dashboard/{userName}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDashboardData(@PathParam("userName") String userName) {

        Dashboard dashboard = new Dashboard();
        logger.info("Fetching dah board data for user - '{}'", userName);
        try {

            User user = userService.findByUsername(userName);
            Session session = HibernateUtil.getSessionFactory().openSession();

            Integer totalTransaction = session.createQuery("from Transaction u where u.requestFromId = :user and u.status != 'PENDING'").
                    setParameter("user", userName).
                    list().
                    size();
            logger.info("Total Transaction =  " + totalTransaction);

            Integer totalPendingTx = session.createQuery("from Transaction u where u.requestToId = :user and u.status = 'PENDING'").
                    setParameter("user", userName).
                    list().
                    size();
            logger.info("Total Pending =  " + totalPendingTx);

            Integer totalGroups = user.getGroups().size();
            logger.info("Total Groups =  " + totalGroups);

            Integer totalConnections = session.createSQLQuery("select distinct user_id from user_group where group_id in (select distinct group_id from user_group where user_id=:userId)").
                    setParameter("userId", user.getId()).
                    list().
                    size();
            logger.info("Total of connections: " + totalConnections);
            session.close();

            dashboard.setTotalTransactions(totalTransaction);
            dashboard.setTotalPendingRequest(totalPendingTx);
            dashboard.setTotalGroups(totalGroups);
            dashboard.setTotalConnections(totalConnections);

            logger.info("Dashboard data fetched for user : '{}'", userName);

            return Response.ok(dashboard).build();

        } catch (Exception ex) {
            logger.error(" Exception while fetching dashboard data with userName '{}' : '{}'", userName, ex);
            return Response.status(500).build();
        }
    }


}
