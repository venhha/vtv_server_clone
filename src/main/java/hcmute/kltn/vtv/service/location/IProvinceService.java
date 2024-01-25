package hcmute.kltn.vtv.service.location;

import hcmute.kltn.vtv.model.data.location.ListProvinceResponse;
import hcmute.kltn.vtv.model.entity.location.Province;

import java.util.List;

public interface IProvinceService {
    ListProvinceResponse getAllProvince();

    List<Province> getPronvincesByProvinceCode(List<String> provincesCode);
}
