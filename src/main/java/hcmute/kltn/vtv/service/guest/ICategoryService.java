package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.CategoriesResponse;
import hcmute.kltn.vtv.model.data.guest.CategoryResponse;
import hcmute.kltn.vtv.model.entity.vtv.Category;

public interface ICategoryService {
    CategoriesResponse getAllCategoryParent();

    CategoriesResponse getAllCategoryByParentId(Long categoryId);

    CategoriesResponse getAllCategory();

    CategoryResponse getCategoryByCategoryId(Long categoryId);

    void checkExistCategoryHasChild(Long categoryId);

    Category getCategoryById(Long categoryId);

    void checkExistCategoryById(Long categoryId);
}
