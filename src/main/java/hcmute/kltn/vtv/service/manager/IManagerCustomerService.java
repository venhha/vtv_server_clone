package hcmute.kltn.vtv.service.manager;

import hcmute.kltn.vtv.model.data.manager.response.PageCustomerResponse;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerCustomerService {
    PageCustomerResponse getPageCustomerByStatus(int size, int page, Status status);

    PageCustomerResponse getListCustomerByStatusSort(int size, int page, Status status, String sort);

    PageCustomerResponse searchCustomerByStatus(int size, int page, Status status, String search);

    ProfileCustomerResponse getCustomerDetailByCustomerId(Long customerId);



    @Transactional
    void updateRoleWithCustomer(Long customerId, Role role);
}
