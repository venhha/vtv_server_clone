package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.ShopDetailResponse;
import hcmute.kltn.vtv.model.entity.vendor.Shop;

public interface IShopGuestService {

    Shop getShopById(Long shopId);

    void checkExistsById(Long shopId);

    ShopDetailResponse getShopDetailByShopId(Long shopId);
}
