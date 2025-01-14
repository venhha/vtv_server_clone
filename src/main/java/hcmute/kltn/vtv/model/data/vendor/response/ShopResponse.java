package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vendor.ShopDTO;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse extends ResponseAbstract {

    private ShopDTO shopDTO;


    public static ShopResponse shopResponse(Shop shop, String message, String status) {
        ShopResponse shopResponse = new ShopResponse();
        shopResponse.setShopDTO(ShopDTO.convertEntityToDTO(shop));
        shopResponse.setMessage(message);
        shopResponse.setStatus(status);
        shopResponse.setCode(200);

        return shopResponse;
    }
}
