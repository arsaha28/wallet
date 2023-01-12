package com.wallet.controller;

import com.wallet.config.WalletConfiguration;
import com.wallet.model.CustodianWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.*;

import java.io.File;
import java.io.IOException;

@RestController("/custodianwallet")
public class WalletController {

    @Autowired
    private WalletConfiguration configuration;

    @PostMapping
    @PreAuthorize("hasAuthority('APPROLE_Subscriber')")
    public ResponseEntity<CustodianWallet> createWallet(@RequestParam(required = true) String password,
                                                        @RequestParam(required = true) String userName) throws CipherException, IOException {

        File walletDirectory = new File(configuration.getNetworkPath());
        Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(password,walletDirectory);
        String walletLocation = walletDirectory + "/" + bip39Wallet;
        Credentials credentials = WalletUtils.loadBip39Credentials(password,bip39Wallet.getMnemonic());
        ECKeyPair ecKeyPair = credentials.getEcKeyPair();

        CustodianWallet custodianWallet = new CustodianWallet();
        custodianWallet.setMnemonic(bip39Wallet.getMnemonic());
        custodianWallet.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
        custodianWallet.setPublicKey(ecKeyPair.getPublicKey().toString(16));
        return  ResponseEntity.ok(custodianWallet);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('APPROLE_Subscriber')")
    public ResponseEntity<CustodianWallet> findWallet(@RequestParam(required = true) String userName,
                                                      @RequestParam(required = true) String password,
                                                      @RequestParam(required = true) String mnemonics){
        CustodianWallet custodianWallet = new CustodianWallet();
        Credentials credentials = WalletUtils.loadBip39Credentials(password,mnemonics);
        ECKeyPair ecKeyPair = credentials.getEcKeyPair();
        custodianWallet.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
        custodianWallet.setPublicKey(ecKeyPair.getPublicKey().toString(16));
        return  ResponseEntity.ok(custodianWallet);
    }
}
