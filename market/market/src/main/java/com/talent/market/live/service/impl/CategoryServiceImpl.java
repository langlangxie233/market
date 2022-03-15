package com.talent.market.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.talent.market.live.common.category.AddCategoryCommon;
import com.talent.market.live.common.category.GetCategoryCommon;
import com.talent.market.live.common.category.SetCategoryNameCommon;
import com.talent.market.live.dao.CategoryMapper;
import com.talent.market.live.model.Category;
import com.talent.market.live.service.CategoryService;
import com.talent.market.live.util.Const;
import com.talent.market.live.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xiexianlang
 * @desc
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(AddCategoryCommon common, String username) {
        Category category = new Category();
        String id = UUID.randomUUID().toString().replace("-", "");
        category.setId(id);
        category.setCreateBy(username);
        category.setUpdateBy(username);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        category.setName(common.getCategoryName());
        category.setParentId(common.getParentId());

        int insert = categoryMapper.insert(category);
        if(insert>0){
            return ServerResponse.createBySuccessMessage("新增成功");
        }

        return ServerResponse.createByErrorMessage("新增失败");
    }

    @Override
    public ServerResponse getCategory(GetCategoryCommon categoryCommon) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",categoryCommon.getCategoryId()).eq("status", Const.ProductStatusEnum.ON_SALE.getCode());
        List<Category> categories = categoryMapper.selectList(wrapper);
        return ServerResponse.createBySuccess("查询成功",categories);
    }

    @Override
    public ServerResponse setCategoryName(SetCategoryNameCommon common, String username) {
        UpdateWrapper<Category> wrapper = new UpdateWrapper<>();
        wrapper.set("name",common.getCategoryName()).eq("id",common.getCategoryId());
        wrapper.set("update_by",username).eq("id",common.getCategoryId());
        wrapper.set("update_time",new Date()).eq("id",common.getCategoryId());
        int update = categoryMapper.update(null, wrapper);
        if(update>0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
