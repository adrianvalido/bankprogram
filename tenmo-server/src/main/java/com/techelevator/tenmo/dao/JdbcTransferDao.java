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
    public List<Transfer> getAllTransfers(Principal principal) throws AccountNotFoundException {
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao();
        long currentId = jdbcAccountDao.getBalanceByUserId(jdbcAccountDao.getCurrentUserId(principal)).getAccountId();
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer t" +
                "WHERE account_from = ? or account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, currentId, currentId);
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
    public boolean sendBucks(Transfer transfer, Principal principal) throws AccountNotFoundException {
//    public Transfer sendBucks(Principal principal, BigDecimal amountSent, long accountTo) throws AccountNotFoundException {
//        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao();
//        long currentUserId = jdbcAccountDao.getCurrentUserId(principal);

        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao();
        BigDecimal currentBalance = jdbcAccountDao.getBalanceByUserId(jdbcAccountDao.getCurrentUserId(principal)).getBalance();
        if (currentBalance.doubleValue() < transfer.getAmount() && transfer.getAmount() > 0) {
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
            int depositResults = jdbcTemplate.update(depositSql, transfer.getAmount(), transfer.getAccountTo());
            if (depositResults != 1) {
                transfer.setTransferStatusId(3);
                return false;
            }
            transfer.setTransferStatusId(2);
            return true;
        }
        return false;
    }

    @Override
    public Transfer createTransfer(Principal principal, BigDecimal amount, long accountTo) throws AccountNotFoundException {
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao();
        long currentAccountId = jdbcAccountDao.getAccountIdByUserId(jdbcAccountDao.getCurrentUserId(principal));
        if (accountTo != currentAccountId) {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                    "account_from, account_to, amount) VALUES (2, 2, ?, ?, ?) returning transfer_id;";
            long tranId = jdbcTemplate.queryForObject(sql, Long.class, currentAccountId, accountTo, amount);

            String sqlMap = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                    "account_from, account_to, a.user_id as user_id_from, tu.user_id as user_id_to, amount " +
                    "FROM transfer t " +
                    "JOIN account a ON t.account_from = a.account_id " +
                    "JOIN tenmo_user tu using(user_id) " +
                    "WHERE a.user_id = (SELECT user_id from account where account_id = ?) " +
                    "AND tu.user_id= (SELECT user_id from account where account_id = ?) " +
                    "AND transfer_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sqlMap, currentAccountId, accountTo, tranId);
            if (results.next()) {
                return mapRowToCreateTransfer(results);
            }

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

    private Transfer mapRowToCreateTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setUserIdFrom(results.getLong("user_id_from"));
        transfer.setUserIdTo(results.getLong("user_id_to"));
        transfer.setAmount(results.getDouble("amount"));
        return transfer;
    }
}
