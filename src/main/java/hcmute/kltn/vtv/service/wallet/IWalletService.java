package hcmute.kltn.vtv.service.wallet;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface IWalletService {
    @Async
    @Transactional
    void addNewWalletAfterRegister(String username);
}