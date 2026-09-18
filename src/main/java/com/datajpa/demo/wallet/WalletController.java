package com.datajpa.demo.wallet;

import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wallets")
//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin()
public class WalletController {
    @Autowired
    private WalletService walletService;

//    @GetMapping
//    public String hello() {
//        return "hello";
//    }

    //Register New Wallet User
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    Wallet registerNewWallet(@Valid @RequestBody Wallet newWallet) {
        return this.walletService.registerNewWalletUser(newWallet);

    }

    @GetMapping()
    Wallet getWalletByID(@RequestParam Integer walletID) {

        return this.walletService.getUserWalletById(walletID);
    }
    // Put// patch //POST

    @PatchMapping
    public Double addFundsToWalletById(@RequestBody WalletDto walletDto) {
        try {
            return this.walletService.addFundsToWalletByID(walletDto.getFromId(), walletDto.getAmount());
        } catch (WalletException e) {
            throw e;
        }
    }


    @PatchMapping("/withdraw")
    public Double withdrawFundsToWalletByID(@RequestBody WalletDto walletDto) {
        return this.walletService.withdrawFundsToWalletByID(walletDto.getFromId(), walletDto.getAmount());
    }

    @PatchMapping("/transfer")
    public Boolean transferFunds(@RequestBody WalletDto walletDto) {
        return this.walletService.fundTransfer(walletDto.getFromId(), walletDto.getToId(), walletDto.getAmount());
    }

}

