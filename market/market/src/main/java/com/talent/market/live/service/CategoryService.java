package com.talent.market.live.service;

import com.talent.market.live.common.category.AddCategoryCommon;
import com.talent.market.live.common.category.GetCategoryCommon;
import com.talent.market.live.common.category.SetCategoryNameCommon;
import com.talent.market.live.model.User;
import com.talent.market.live.util.ServerResponse;


public interface CategoryService {
    ServerResponse addCategory(AddCategoryCommon common, String username);

    ServerResponse getCategory(GetCategoryCommon categoryCommon);

    ServerResponse setCategoryName(SetCategoryNameCommon common, String username);
}
