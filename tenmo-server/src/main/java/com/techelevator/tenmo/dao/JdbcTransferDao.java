package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
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
    public boolean sendBucks(Transfer transfer) throws AccountNotFoundException {
//    public Transfer sendBucks(Principal principal, BigDecimal amountSent, long accountTo) throws AccountNotFoundException {
//        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao();
//        long currentUserId = jdbcAccountDao.getCurrentUserId(principal);
        String withdrawSql = "UPDATE account SET balance = balance - ? " +
                "WHERE account_id = ?;";
        int withdrawResults = jdbcTemplate.update(withdrawSql, transfer.getAmount(), transfer.getAccountFrom());


        if (withdrawResults != 1) {
            transfer.setTransferStatusId(3);
            return false;
        }
//        BigDecimal newPrincipalBalance = jdbcAccountDao.getBalanceByUserId(currentUserId).getBalance().subtract(amountSent);
//        BigDecimal newUserToBalance = jdbcAccountDao.getBalanceByUserId(userTo).getBalance().add(amountSent);
        String depositSql = "UPDATE account SET balance = balance + ? " +
                "WHERE account_id = ?;";
        int depositResults = jdbcTemplate.update(depositSql,transfer.getAmount(), transfer.getAccountTo());
        if (depositResults != 1) {
            transfer.setTransferStatusId(3);
            return false;
        }
        transfer.setTransferStatusId(2);
        return true;
    }

    @Override
    public Transfer createTransfer(Principal principal, BigDecimal amount, long accountTo) throws AccountNotFoundException {
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao();
        long currentAccountId = jdbcAccountDao.getAccountIdByUserId(jdbcAccountDao.getCurrentUserId(principal));
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) VALUES (2, 1, ?, ?, ?) returning transfer_id;";
        long tranId = jdbcTemplate.queryForObject(sql, Long.class, currentAccountId, accountTo, amount);

        String sqlMap = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlMap, tranId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }

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
