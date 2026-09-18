package com.datajpa.demo.wallet;

import com.datajpa.demo.transaction.Transaction;
import com.datajpa.demo.transaction.TransactionRepository;
import com.datajpa.demo.transaction.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
       private final TransactionRepository  transactionRepository;

    @Autowired
    public WalletServiceImpl(TransactionRepository transactionRepository,WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet registerNewWalletUser(Wallet newWallet) {
        //check if email already exist
        if(this.walletRepository.findByEmail(newWallet.getEmail()).isPresent()){
           throw new WalletException("Email id already exist");
        };
        newWallet.setCreatedAt(LocalDateTime.now());
        return this.walletRepository.save(newWallet);
    }

    @Override
    public Wallet getUserWalletById(Integer walletID) {
        Optional<Wallet> foundWallet = this.walletRepository.findById(walletID);
        if (foundWallet.isPresent()) {
            return foundWallet.get();
        }
        return null;
    }

    @Override
    public Wallet updateUserWallet(Wallet updateWallet) {
        return null;
    }

    @Override
    public Double addFundsToWalletByID(Integer ID, Double newBalance) {
        Wallet foundWallet = this.walletRepository.findById(ID).orElseThrow(()
                -> new WalletException("wallet id does not exist"));

        Double oldBalance = foundWallet.getBalance();
        foundWallet.setBalance(oldBalance+newBalance);
        this.walletRepository.save(foundWallet);  // Option 1 to modify DB
        return foundWallet.getBalance();
    }

    @Override
    @Transactional  //option 2 for performing DB operation
    public Double withdrawFundsToWalletByID(Integer ID, Double amount) throws WalletException {
    Wallet foundWallet = this.walletRepository.findById(ID)
            .orElseThrow(()->new WalletException("Wallet not found"));
           if(foundWallet.getBalance()<amount){
               throw  new WalletException("insufficient balance, Available balance :"+foundWallet.getBalance());
           }
    Double currentBalance = foundWallet.getBalance();
    foundWallet.setBalance(currentBalance-amount);
        return foundWallet.getBalance();
    }

    @Override
    public Boolean fundTransfer(Integer fromID, Integer toID, Double amount) {
        Wallet fromWallet = this.walletRepository.findById(fromID).orElseThrow(()-> new WalletException("from account not having balance"));
        Wallet toWallet = this.walletRepository.findById(fromID).orElseThrow(()-> new WalletException("To account not having balance"));
        if(fromWallet.getBalance()<amount) throw new WalletException("insufficent balance in ur account");
        Double fromBalance = fromWallet.getBalance();
        fromWallet.setBalance(amount);
        Double toBalance = fromWallet.getBalance();
        fromWallet.setBalance(amount);
// Using Setter
        Transaction debitTransaction = new Transaction(); //Transient
        debitTransaction.setDate(LocalDate.now());
        debitTransaction.setAmount(amount);
        debitTransaction.setType(TransactionType.DEBIT);
        debitTransaction = this.transactionRepository.save(debitTransaction);
        fromWallet.getTransactionList().add(debitTransaction);

         // Builder Pattern
        Transaction creditTransaction = Transaction.builder().date(LocalDate.now())
                .amount(amount).type(TransactionType.CREDIT)
                .build();
        creditTransaction = this.transactionRepository.save(creditTransaction);
        toWallet.getTransactionList().add(creditTransaction);


        return true;
    }

    @Override
    public Boolean deactivateWalletByID(Integer ID) {
        return null;
    }

    @Override
    public Boolean activateWalletByID(Integer ID) {
        return null;
    }
}
