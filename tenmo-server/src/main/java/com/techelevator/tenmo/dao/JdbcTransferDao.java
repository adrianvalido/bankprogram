package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import com.techelevator.tenmo.dao.JdbcAccountDao;
@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }


    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersByAccountFrom(long accountFrom) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersByAccountTo(long accountTo) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountTo);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByAmount(BigDecimal amount) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE amount = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, amount);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(long transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE amount = ?;";
        SqlRowSet results;
        results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }

    @Override
    public Transfer sendBucks(Principal principal, BigDecimal amountSent, long userTo) throws AccountNotFoundException {
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao();
        long currentUserId = jdbcAccountDao.getCurrentUserId(principal);
        String withdrawSql = "Update account set balance = balance - ? " +
                "WHERE user_id = ?;";
        String depositSql = "Update account set balance = balance + ? " +
                "WHERE user_id = ?;";
        SqlRowSet withdrawResults = jdbcTemplate.update(withdrawSql, amountSent ,currentUserId);
        SqlRowSet depositResults = jdbcTemplate.update(depositSql, amountSent ,userTo);

        BigDecimal newPrincipalBalance = jdbcAccountDao.getBalanceByUserId(currentUserId).getBalance().subtract(amountSent);
        BigDecimal newUserToBalance = jdbcAccountDao.getBalanceByUserId(userTo).getBalance().add(amountSent);

    }
    //TODO consider creating two methods for sending and receiving ^

    @Override
    public List<Transfer> getAllTransfersByType(long transferTypeId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE transfer_type_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferTypeId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersByStatus(long transferStatusId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE transfer_status_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferStatusId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getDouble("amount"));
        return transfer;
    }
}
