package atlantafx.sampler.services;

import atlantafx.sampler.entities.Wallet;

import java.util.List;

public interface WalletService {

    Wallet createWallet(Wallet wallet);

    Wallet getWalletById(int walletId);

    List<Wallet> getAllWallets();

    Wallet updateWallet(Wallet wallet);

    boolean deleteWallet(int walletId);
}