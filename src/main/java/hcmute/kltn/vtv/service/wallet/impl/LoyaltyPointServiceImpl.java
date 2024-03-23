package hcmute.kltn.vtv.service.wallet.impl;

import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointResponse;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.wallet.LoyaltyPointRepository;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoyaltyPointServiceImpl implements ILoyaltyPointService {

    private final LoyaltyPointRepository loyaltyPointRepository;
    private final ILoyaltyPointHistoryService loyaltyPointHistoryService;
    private final ICustomerService customerService;


    @Async
    @Override
    @Transactional
    public void addNewLoyaltyPointAfterRegister(String username) {
        if(!loyaltyPointRepository.existsByUsername(username)){
            try {
                loyaltyPointRepository.save(createLoyaltyPointByUserame(username));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Async
    @Override
    @Transactional
    public void plusLoyaltyPointByUsername(String username, Long point, String type) {
        LoyaltyPoint loyaltyPoint = getLoyaltyPointByUsername(username);
        loyaltyPoint.setTotalPoint(loyaltyPoint.getTotalPoint() + point);
        loyaltyPoint.setUpdateAt(LocalDateTime.now());
        try {

            loyaltyPointRepository.save(loyaltyPoint);
            loyaltyPointHistoryService.addNewLoyaltyPointHistoryByLoyaltyPointId(loyaltyPoint, point, type, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public LoyaltyPointResponse getLoyaltyPointResponseByUsername(String username) {
        LoyaltyPoint loyaltyPoint = getLoyaltyPointByUsername(username);
        return LoyaltyPointResponse.loyaltyPointResponse(loyaltyPoint, "Lấy thông tin điểm tích lũy thành công", "OK");
    }



    @Override
    public LoyaltyPoint getLoyaltyPointByUsername(String username) {
        return loyaltyPointRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin điểm tích lũy của tài khoản " + username));
    }


    @Override
    public void checkExistLoyaltyPointByIdAndUsername(Long loyaltyPointId, String username) {
        if(!loyaltyPointRepository.existsByLoyaltyPointIdAndUsername(loyaltyPointId, username)){
            throw new BadRequestException("Mã điểm tích lũy không tồn tại hoặc không thuộc tài khoản " + username);
        }
    }





    private LoyaltyPoint createLoyaltyPointByUserame(String username) {
        LoyaltyPoint loyaltyPoint = new LoyaltyPoint();
        loyaltyPoint.setUsername(username);
        loyaltyPoint.setTotalPoint(0L);
        loyaltyPoint.setStatus(Status.ACTIVE);
        loyaltyPoint.setCreateAt(LocalDateTime.now());
        loyaltyPoint.setUpdateAt(LocalDateTime.now());
        loyaltyPoint.setLoyaltyPointHistories(null);

        return loyaltyPoint;
    }



}
