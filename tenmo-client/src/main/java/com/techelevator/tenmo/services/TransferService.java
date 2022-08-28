package com.techelevator.tenmo.services;

import com.techelevator.tenmo.controller.TransferController;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser currentUser;


    public TransferService(String BASE_URL, AuthenticatedUser currentUser) {
        this.BASE_URL = BASE_URL;
        this.currentUser = currentUser;
    }

    public List<Transfer> getAllTransfers() throws AccountNotFoundException {
        List<Transfer> transferHistory = new ArrayList<>();
        for (int i = 0; i < transferHistory.size(); i++) {

        }
        return null;
    }

    public Transfer sendBucks(Transfer transfer ) throws AccountNotFoundException{
        return null;
    }

    public Transfer createTransfer( BigDecimal amount, long accountTo)throws AccountNotFoundException{
        return null;
    }

    public Transfer getTransferById(long transferId){
        return null;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }
}
