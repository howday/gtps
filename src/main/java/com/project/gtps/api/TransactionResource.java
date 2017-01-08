package com.project.gtps.api;

import com.project.gtps.domain.Transaction;
import com.project.gtps.service.TransactionService;
import com.project.gtps.service.impl.TransactionServiceImpl;
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

    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response display(@PathParam("username") String username) {

        logger.info("Searching transaction of usernames matching with '{}'", username);
        try {
            List<Transaction> transactionList = transactionService.findTxOfUser(username, "COMPLETED");
            logger.info("Completed transaction searching completed for user : '{}'", username);

            return Response.ok(transactionList).build();

        } catch (Exception ex) {
            logger.error(" Exception while searching user with name '{}' : '{}'", username, ex);
            return Response.status(500).build();
        }


    }

    @GET
    @Path("{username}/{transactionId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateTransaction(@PathParam("username") String username, @PathParam("transactionId") Long transactionId) {

        logger.info("Updating transaction with id '{}' of username matching with '{}'", transactionId, username);
        try {

            transactionService.processAcceptRejectTx(transactionId);
            List<Transaction> transactionList = transactionService.findTxOfUser(username, "PENDING");
            logger.info("Updating transaction with id '{}' of username matching with '{}' is COMPLETED.", transactionId, username);
            return Response.ok(transactionList).build();


        } catch (Exception ex) {
            logger.error(" Exception while Updating transaction with id '{}' of usernames matching with '{}' - {}", transactionId, username, ex);
            return Response.status(500).build();
        }


    }


}
