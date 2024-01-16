package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.ChangePasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ForgotPasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ProfileCustomerRequest;
import hcmute.kltn.vtv.model.data.user.response.ForgotPasswordResponse;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.entity.user.Customer;

public interface ICustomerService {
    Customer getCustomerByUsername(String username);

    ProfileCustomerResponse getProfileCustomer(String token);

    ProfileCustomerResponse updateProfileCustomer(ProfileCustomerRequest profileCustomerRequest);

    ProfileCustomerResponse changePassword(ChangePasswordRequest request);

    ForgotPasswordResponse resetPassword(ForgotPasswordRequest request);
}
