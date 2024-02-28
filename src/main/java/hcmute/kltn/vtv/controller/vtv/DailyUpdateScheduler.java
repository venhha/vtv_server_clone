package hcmute.kltn.vtv.controller.vtv;

import hcmute.kltn.vtv.service.vtv.IVoucherSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DailyUpdateScheduler {

    @Autowired
    private final IVoucherSchedulerService voucherSchedulerService;


    @PostConstruct
    public void onServerStart() {
        System.out.println("Server started at " + LocalDate.now() + " - Performing initial update...");
       voucherSchedulerService.checkExpirationVoucher();
    }

    // This method will be executed every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void performDailyUpdate() {
        System.out.println("Daily update at " + LocalDate.now() + " - Performing daily update...");
        voucherSchedulerService.checkExpirationVoucher();
    }
}
