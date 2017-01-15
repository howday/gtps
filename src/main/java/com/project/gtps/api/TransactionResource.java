package com.project.gtps.api;

import com.project.gtps.domain.Transaction;
import com.project.gtps.domain.UserLog;
import com.project.gtps.service.TransactionService;
import com.project.gtps.service.UserLogService;
import com.project.gtps.service.impl.TransactionServiceImpl;
import com.project.gtps.service.impl.UserLogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by suresh on 1/6/17.
 */
@Path("/transaction")
public class TransactionResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private TransactionService transactionService = new TransactionServiceImpl();
    private UserLogService userLogService = new UserLogServiceImpl();

    @GET
    @Path("{state}/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response display(@PathParam("state") String state, @PathParam("username") String username) {

        logger.info("Searching transaction of usernames matching with '{}'", username);
        try {
            List<Transaction> transactionList = transactionService.findTxOfUser(username, state);
            logger.info("{} transaction searching completed for user : '{}'", state, username);

            return Response.ok(transactionList).build();

        } catch (Exception ex) {
            logger.error("Exception while searching {} user with name '{}' : '{}'", state, username, ex);
            return Response.status(500).build();
        }


    }

    @GET
    @Path("{username}/{transactionId}/{action}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateTransaction(@PathParam("username") String username, @PathParam("transactionId") Long transactionId, @PathParam("action") String action) {

        logger.info("Updating transaction with id '{}' of username matching with '{}'", transactionId, username);
        try {

            transactionService.processAcceptRejectTx(transactionId, action);
            List<Transaction> transactionList = transactionService.findTxOfUser(username, "PENDING");
            logger.info("Updating transaction with id '{}' of username matching with '{}' is COMPLETED.", transactionId, username);
            return Response.ok(transactionList).build();


        } catch (Exception ex) {
            logger.error(" Exception while Updating transaction with id '{}' of usernames matching with '{}' - {}", transactionId, username, ex);
            return Response.status(500).build();
        }


    }


    @GET
    @Path("userlog/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserLog(@PathParam("username") String username) {
        logger.info("Fetching user log of user with username '{}' ", username);
        try {


            List<UserLog> userLogList = userLogService.getAllUserLog(username);
            logger.info("User logs returned successfully for user '{}'", username);
            return Response.ok(userLogList).build();


        } catch (Exception ex) {
            logger.error("Exception while fetching user log of user with username '{}' ", username);
            return Response.status(500).build();
        }


    }


}
