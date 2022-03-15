package com.talent.market.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.talent.market.live.common.cart.AddProductCartCommon;
import com.talent.market.live.common.cart.DeleteProductCartCommon;
import com.talent.market.live.common.cart.SelectCartProductCommon;
import com.talent.market.live.common.cart.UpdateProductCartCommon;
import com.talent.market.live.dao.CartMapper;
import com.talent.market.live.dao.ProductMapper;
import com.talent.market.live.model.Cart;
import com.talent.market.live.model.Product;
import com.talent.market.live.model.User;
import com.talent.market.live.service.CartService;
import com.talent.market.live.util.ServerResponse;
import com.talent.market.live.vo.FindCartProductSumVo;
import com.talent.market.live.vo.FindCartProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Value(value = "${ftp.server.http.prefix:http://image.talent.com/}")
    private String imageHost;

    @Override
    public ServerResponse addProductCart(AddProductCartCommon cartCommon, User user) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("product_id",cartCommon.getProductId()).eq("user_id",user.getId());
        Cart findCart = cartMapper.selectOne(cartQueryWrapper);
        //获取商品的库存
        Product product = productMapper.selectById(cartCommon.getProductId());
        Integer stock = product.getStock();
        if(findCart!=null){
            Integer count=cartCommon.getCount()+findCart.getQuantity();
            if(count>stock){
                return ServerResponse.createByErrorMessage("商品库存不足");
            }
            UpdateWrapper<Cart> cartUpdateWrapper = new UpdateWrapper<>();
            cartUpdateWrapper.set("quantity",count).set("update_by",user.getUsername()).set("create_by",user.getUsername()).set("update_time   ",new Date()).eq("id",findCart.getId());
            int update = cartMapper.update(null, cartUpdateWrapper);
            if(update==0){
                ServerResponse.createByErrorMessage("更新失败");
            }
        }else {
            //购物车新增
            if (cartCommon.getCount() > stock) {
                return ServerResponse.createByErrorMessage("商品库存不足");
            }

            String id = UUID.randomUUID().toString().replace("-", "");
            Cart cart = new Cart();
            cart.setId(id);
            cart.setChecked(1);
            cart.setProductId(cartCommon.getProductId());
            cart.setQuantity(cartCommon.getCount());
            cart.setUserId(user.getId());
            cart.setCreateTime(new Date());
            cart.setUpdateTime(new Date());
            cart.setUpdateBy(user.getUsername());

            int insert = cartMapper.insert(cart);
            if (insert == 0) {
                return ServerResponse.createByErrorMessage("新增失败");
            }
        }

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse shopCartList(String userId) {
        //购物车查询
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",userId).eq("is_deleted",0);
        List<Cart> carts = cartMapper.selectList(cartQueryWrapper);

        FindCartProductSumVo productSumVo = new FindCartProductSumVo();
        if(carts.size()>0) {
            //去除购物车中所有的商品id
            List<String> collect = carts.stream().map(Cart::getProductId).collect(Collectors.toList());
            QueryWrapper<Product> wrapper = new QueryWrapper<>();
            wrapper.in("id",collect);
            List<Product> products = productMapper.selectList(wrapper);

            List<FindCartProductVo> listCartVoList=new ArrayList<>();
            carts.forEach(c->{
                FindCartProductVo findCartProductVo = new FindCartProductVo();
                products.forEach(p->{
                    //判断商品id是否相等
                    if(c.getProductId().equals(p.getId())){
                        findCartProductVo.setId(c.getId());
                        if(c.getQuantity()>p.getStock()){
                            findCartProductVo.setLimitQuantity("LIMIT_NUM_FAIL");
                        }
                        findCartProductVo.setLimitQuantity("LIMIT_NUM_SUCCESS");
                        findCartProductVo.setProductChecked(c.getChecked());
                        findCartProductVo.setProductId(p.getId());
                        findCartProductVo.setProductMainImage(p.getSubImages().split(",")[0]);
                        findCartProductVo.setProductName(p.getName());
                        findCartProductVo.setProductPrice(p.getPrice());
                        findCartProductVo.setProductStatus(p.getStatus());
                        findCartProductVo.setProductStock(p.getStock());
                        findCartProductVo.setProductSubtitle(p.getSubtitle());

                        //计算购物车中每一项的总价
                        findCartProductVo.setProductTotalPrice(p.getPrice().multiply(new BigDecimal(c.getQuantity())));
                        findCartProductVo.setQuantity(c.getQuantity());
                        findCartProductVo.setUserId(userId);
                        //将每一对应的对象放入集合中
                        listCartVoList.add(findCartProductVo);
                    }
                });
            });

            List<Cart> collect1 = carts.stream().filter(c -> c.getChecked().equals(0)).collect(Collectors.toList());
            if(collect1!=null && collect1.size()>0){
                productSumVo.setAllChecked(false);
            }else{
                productSumVo.setAllChecked(true);
            }
            //将组装好的购物车集合放入对应的对象中
            productSumVo.setListCartVoList(listCartVoList);

            BigDecimal cartTotalPrice=listCartVoList.stream().filter(l->l.getProductChecked()==1).map(FindCartProductVo::getProductTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
            productSumVo.setCartTotalPrice(cartTotalPrice);

        }else {
            productSumVo.setAllChecked(true);
            productSumVo.setCartTotalPrice(new BigDecimal(0));
            List<FindCartProductVo> findCartProductVos = new ArrayList<>();
            productSumVo.setListCartVoList(findCartProductVos);
            productSumVo.setImageHost(imageHost);
            return ServerResponse.createBySuccess(productSumVo);
        }
        productSumVo.setImageHost(imageHost);
        return ServerResponse.createBySuccess(productSumVo);
    }

    @Override
    public ServerResponse updateProductCart(UpdateProductCartCommon common,String userId) {
        UpdateWrapper<Cart> wrapper = new UpdateWrapper<>();
        Product product = productMapper.selectById(common.getProductId());

        if(common.getCount()>product.getStock()){
            return ServerResponse.createByErrorMessage("库存不足");
        }

        wrapper.set("quantity",common.getCount()).eq("user_id",userId).eq("product_id",common.getProductId());
        int update = cartMapper.update(null, wrapper);
        if(update==0){
            return ServerResponse.createByError();
        }

        ServerResponse serverResponse = shopCartList(userId);
        return serverResponse;
    }

    @Override
    public ServerResponse deleteProductCart(DeleteProductCartCommon common, String userId) {
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id",userId).in("product_id",common.getProductIds());
        int delete = cartMapper.delete(updateWrapper);
        if(delete==0){
            ServerResponse serverResponse = shopCartList(userId);
            return serverResponse;
        }
        ServerResponse serverResponse = shopCartList(userId);
        return serverResponse;
    }

    @Override
    public ServerResponse selectCartProduct(SelectCartProductCommon common,String userId) {
        UpdateWrapper<Cart> cartUpdateWrapper= new UpdateWrapper<>();
        cartUpdateWrapper.set("checked",1).eq("product_id",common.getProductId()).eq("user_id",userId);
        int update = cartMapper.update(null, cartUpdateWrapper);

        if(update==0){
            return ServerResponse.createByErrorMessage("更新失败");
        }

        //查询当前购物车信息
        ServerResponse serverResponse = shopCartList(userId);

        return serverResponse;
    }

    @Override
    public ServerResponse unSelectCartProduct(SelectCartProductCommon common, String userId) {
        UpdateWrapper<Cart> cartUpdateWrapper= new UpdateWrapper<>();
        cartUpdateWrapper.set("checked",0).eq("product_id",common.getProductId()).eq("user_id",userId);
        int update = cartMapper.update(null, cartUpdateWrapper);

        if(update==0){
            return ServerResponse.createByErrorMessage("更新失败");
        }
        //查询当前购物车信息
        ServerResponse serverResponse = shopCartList(userId);

        return serverResponse;
    }

    @Override
    public ServerResponse getCartProductCount(String userId) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",userId).eq("is_deleted", 0);
        Integer count = cartMapper.selectCount(cartQueryWrapper);
        return ServerResponse.createBySuccess(count);
    }

    @Override
    public ServerResponse selectAllCartProduct(String userId) {
        UpdateWrapper<Cart> cartUpdateWrapper= new UpdateWrapper<>();
        cartUpdateWrapper.set("checked",1).eq("user_id",userId);
        int update = cartMapper.update(null, cartUpdateWrapper);

        if(update==0){
            return ServerResponse.createByErrorMessage("更新失败");
        }
        //查询当前购物车信息
        ServerResponse serverResponse = shopCartList(userId);

        return serverResponse;
    }

    @Override
    public ServerResponse unSelectAllCartProduct(String userId) {
        UpdateWrapper<Cart> cartUpdateWrapper= new UpdateWrapper<>();
        cartUpdateWrapper.set("checked",0).eq("user_id",userId);
        int update = cartMapper.update(null, cartUpdateWrapper);

        if(update==0){
            return ServerResponse.createByErrorMessage("更新失败");
        }
        //查询当前购物车信息
        ServerResponse serverResponse = shopCartList(userId);

        return serverResponse;
    }


}
