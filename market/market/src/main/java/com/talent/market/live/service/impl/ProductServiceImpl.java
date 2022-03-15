package com.talent.market.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;
import com.talent.market.live.common.product.*;
import com.talent.market.live.dao.CategoryMapper;
import com.talent.market.live.dao.ProductMapper;
import com.talent.market.live.model.Category;
import com.talent.market.live.model.Product;
import com.talent.market.live.model.User;
import com.talent.market.live.service.ProductService;
import com.talent.market.live.util.Const;
import com.talent.market.live.util.ServerResponse;
import com.talent.market.live.vo.ProductDetailVo;
import com.talent.market.live.vo.ProductListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
/**
 * @author xiexianlang
 * @desc
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Value(value = "${ftp.server.http.prefix:http://image.talent.com/}")
    private String imageHost;

    @Override
    public ServerResponse productManageList(ProductManageListCommon common) {
        PageHelper.startPage(common.getPageNum(),common.getPageSize());
        List<Product> products = productMapper.selectList(null);
        PageInfo<Product> productPageInfo = new PageInfo<>(products);

        return ServerResponse.createBySuccess("成功",productPageInfo);
    }

    @Override
    public ServerResponse ProductSearchByCondition(ProductSearchByConditionCommon common) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if(common.getProductId()!=null && common.getProductId()!=""){
            wrapper.eq("id",common.getProductId().trim());
        }else if(common.getProductName()!=null && common.getProductName()!=""){
            wrapper.like("name",common.getProductName().trim());
        }
        PageHelper.startPage(common.getPageNum(),common.getPageSize());
        List<Product> products = productMapper.selectList(wrapper);
        PageInfo<Product> productPageInfo = new PageInfo<Product>(products);

        return ServerResponse.createBySuccess(productPageInfo);
    }

    @Override
    public ServerResponse productDetail(ProductDetailCommon common) {
        Product product = productMapper.selectById(common.getProductId());
        ProductDetailVo productDetailVo = new ProductDetailVo();
        if(product!=null){
            Category category = categoryMapper.selectById(product.getCategoryId());
            BeanUtils.copyProperties(product,productDetailVo);
            productDetailVo.setImageHost("http://image.talent.com/");
            productDetailVo.setParentCategoryId(category.getParentId());
            return ServerResponse.createBySuccess(productDetailVo);
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse setSaleStatus(SetSaleStatusCommon common, User user) {
        UpdateWrapper<Product> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status",common.getStatus()).eq("id",common.getProductId());
        updateWrapper.set("update_by",user.getUsername()).eq("id",common.getProductId());
        updateWrapper.set("update_time",new Date()).eq("id",common.getProductId());
        int update = productMapper.update(null, updateWrapper);
        if(update>0){
           return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse saveOrUpdateProduct(SaveOrUpdateProductCommon common, User user) {
        if(common.getId()==null){
            Product product = new Product();
            BeanUtils.copyProperties(common,product);
            product.setId(UUID.randomUUID().toString().replace("-",""));
            product.setCreateBy(user.getUsername());
            product.setUpdateBy(user.getUsername());
            product.setCreateTime(new Date());
            product.setUpdateTime(new Date());
            int insert = productMapper.insert(product);
            if(insert>0){
                return ServerResponse.createBySuccessMessage("新增成功");
            }else{
                return ServerResponse.createByErrorMessage("新增失败");
            }
        }

        common.setUpdateBy(user.getUsername());
        common.setUpdateTime(new Date());
        Integer integer = productMapper.updateByPrimaryKeySelective(common);
        if(integer>0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }else {
            return ServerResponse.createByErrorMessage("更新失败");
        }
    }

    @Override
    public ServerResponse productList(ProductListCommon common) {
        //商品的条件
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();

        if(common.getKeyword()!=null){
            //产品的条件
            QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.eq("name",common.getKeyword()).eq("status", Const.ProductStatusEnum.ON_SALE.getCode());
            Category category = categoryMapper.selectOne(categoryQueryWrapper);
            if(category!=null){
                common.setCategoryId(category.getId());
            }else{
                return ServerResponse.createBySuccessMessage("无数据");
            }

            /*Product product = productMapper.searchByKeyword(common);
            if(product!=null){
                common.setCategoryId(product.getId());
            }else{
                return ServerResponse.createBySuccessMessage("无数据");
            }*/
        }

        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("parent_id",common.getCategoryId());
        List<Category> categories = categoryMapper.selectList(categoryQueryWrapper);
        if(categories.size()>0) {
            List<String> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
            productQueryWrapper.in("category_id", categoryIds);
        }else{
            productQueryWrapper.eq("category_id", common.getCategoryId());
        }

        //根据指定分类id查询商品列表
//        if(common.getCategoryId()!=null) {
//            productQueryWrapper.eq("category_id", common.getCategoryId());
//        }
        PageHelper.startPage(common.getPageNum(),common.getPageSize());
        List<Product> products = productMapper.selectList(productQueryWrapper);

        //返回参数的集合
        List<ProductListVo> listVos=new ArrayList<>();
        products.forEach(p->{
            ProductListVo productListVo = new ProductListVo();
            BeanUtils.copyProperties(p,productListVo);
            String[] images = p.getMainImage().split(",");
            productListVo.setMainImage(images[0]);
            productListVo.setImageHost("http://image.talent.com/");
            listVos.add(productListVo);
        });

        PageInfo<ProductListVo> productPageInfo = new PageInfo<>(listVos);

        return ServerResponse.createBySuccess("查询成功",productPageInfo);
    }

    @Override
    public ServerResponse productClientDetail(ProductDetailCommon common) {
        Product product = productMapper.selectById(common.getProductId());
        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product,productDetailVo);
        productDetailVo.setImageHost("http://image.talent.com/");
        productDetailVo.setMainImage(product.getSubImages().split(",")[0]);

        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse productClientList(ProductListCommon common) {
        QueryWrapper<Product> currentProductWrapper = new QueryWrapper<>();
        QueryWrapper<Product> deepProductWrapper = new QueryWrapper<>();
        if(common.getCategoryId()!=null){
            QueryWrapper<Product> wrapper = new QueryWrapper<>();
            wrapper.eq("category_id",common.getCategoryId());
            return search(wrapper,common);
        }
        if(common.getKeyword().equals("ALL")){
            QueryWrapper<Product> wrapper = new QueryWrapper<>();
            return search(wrapper,common);
        }
        if(common.getKeyword().trim()!=null){
            //查询所有含keyword的品类
            QueryWrapper<Category> CurrentCategoryWrapper = new QueryWrapper<>();
            CurrentCategoryWrapper.like("name",common.getKeyword()).eq("status",Const.ProductStatusEnum.ON_SALE.getCode());
            List<Category> categories = categoryMapper.selectList(CurrentCategoryWrapper);
            //查询这些品类的子类
            if (categories.size()>0){
                QueryWrapper<Category> categoryWrapper=new QueryWrapper<>();
                List<Category> deepCategoryList=new ArrayList<>();
                categories.forEach(c-> {
                    categoryWrapper.eq("id",c.getId()).or().eq("parent_id",c.getId());
                    deepCategoryList.addAll(categoryMapper.selectList(categoryWrapper));
                    categoryWrapper.clear();
                });
                //查询品类下的产品
                if(deepCategoryList.size()>0){
                    deepCategoryList.forEach(c->{
                        currentProductWrapper.eq("category_id",c.getId());
                        List<String> categoryIds = deepCategoryList.stream().map(Category::getId).collect(Collectors.toList());
                        deepProductWrapper.in("category_id", categoryIds);
                        currentProductWrapper.clear();
                    });
                    deepProductWrapper.eq("status",Const.ProductStatusEnum.ON_SALE.getCode());
                    return search(deepProductWrapper,common);}
            }else{
                //没有找到品类，去产品查询
                PageHelper.startPage(common.getPageNum(),common.getPageSize());
                QueryWrapper<Product> productWrapper = new QueryWrapper<>();
                productWrapper.like("name",common.getKeyword()).eq("status",Const.ProductStatusEnum.ON_SALE.getCode());
                return  search(productWrapper,common);
            }
        }
        return ServerResponse.createByError();
    }

    private ServerResponse search(QueryWrapper wrapper,ProductListCommon common) {
        PageHelper.startPage(common.getPageNum(),common.getPageSize());
        wrapper.eq("is_deleted",0);
        List<Product> products = productMapper.selectList(wrapper);
        //返回参数的集合
        List<ProductListVo> listVos=new ArrayList<>();
        products.forEach(p->{
            ProductListVo productListVo = new ProductListVo();
            BeanUtils.copyProperties(p,productListVo);
            String[] images = p.getSubImages().split(",");
            productListVo.setMainImage(images[0]);
            productListVo.setImageHost(imageHost);
            listVos.add(productListVo);
        });
        PageInfo<ProductListVo> productPageInfo = new PageInfo<>(listVos);
        return ServerResponse.createBySuccess("查询成功",productPageInfo);
    }


}
