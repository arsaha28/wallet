package com.wallet.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustodianWallet {

    private String publicKey;
    private String privateKey;
    private String mnemonic;
    private String password;
}
