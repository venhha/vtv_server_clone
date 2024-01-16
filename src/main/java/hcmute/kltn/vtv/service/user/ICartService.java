package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.CartRequest;
import hcmute.kltn.vtv.model.data.user.response.CartResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCartResponse;
import hcmute.kltn.vtv.model.entity.user.Cart;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICartService {
    @Transactional
    CartResponse addNewCart(CartRequest request);

    @Transactional
    CartResponse updateCart(CartRequest request);

    @Transactional
    CartResponse deleteCart(Long cartId, String username);

    ListCartResponse getListCartByUsername(String username);

    ListCartResponse getListCartByUsernameAndListCartId(String username, List<Long> cartIds);

    List<Cart> getListCartByUsernameAndIds(String username, List<Long> cartIds);

    Cart getCartByUserNameAndId(String username, Long cartId);

    @Transactional
    ListCartResponse deleteCartByShopId(Long shopId, String username);

    boolean checkCartsSameShop(String username, List<Long> cartIds);
}
