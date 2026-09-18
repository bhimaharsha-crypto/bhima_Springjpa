package com.datajpa.demo.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    //Simple JPARepository
    //Custom JPA Query by method name
    //Select wallet FROM Wallet wallet where wallet.email=?1
    Optional<Wallet> findByEmail(String email);

    //or Custom query
    @Query("SELECT wallet from Wallet wallet where wallet.email=?1")
    Wallet searchForWalletByEmail(String email);

    @Query(value="SELECT * from Wallet wallet where wallet.email=?1",nativeQuery = true)
    Wallet searchForWalletByEmailNative(String email);

    //List<Wallet> getALLByWallet();

}
