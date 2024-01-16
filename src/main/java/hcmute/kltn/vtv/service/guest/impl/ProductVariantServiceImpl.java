package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.repository.vtv.ProductVariantRepository;
import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.service.vendor.IAttributeShopService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements IProductVariantService {

    @Autowired
    private IAttributeShopService attributeService;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    ModelMapper modelMapper;

}
