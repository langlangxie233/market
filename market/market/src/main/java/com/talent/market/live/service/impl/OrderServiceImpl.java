package com.talent.market.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.talent.market.live.common.order.CreateOrderCommon;
import com.talent.market.live.common.order.DetailClientOrderCommon;
import com.talent.market.live.common.order.OrderClientListCommon;
import com.talent.market.live.dao.*;
import com.talent.market.live.model.*;
import com.talent.market.live.service.CartService;
import com.talent.market.live.service.OrderService;
import com.talent.market.live.util.Const;
import com.talent.market.live.util.ServerResponse;
import com.talent.market.live.vo.*;
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
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartService cartService;

    @Value(value = "${ftp.server.http.prefix:http://image.talent.com/}")
    private String imageHost;

    @Override
    public ServerResponse createOrder(CreateOrderCommon common, String userId, String username) {

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("status",10).eq("user_id",userId);
        List<Order> orders = orderMapper.selectList(wrapper);
        if(orders.size()>0){
            return ServerResponse.createByErrorMessage("请先支付未付款订单");
        }

        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id",userId).eq("checked",Const.Cart.CHECKED);

        Shipping shipping = shippingMapper.selectById(common.getShippingId());

        List<Cart> carts = cartMapper.selectList(cartQueryWrapper);
       if(carts.size()>0) {
           List<String> productIds = carts.stream().map(Cart::getProductId).collect(Collectors.toList());
           QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
           productQueryWrapper.in("id",productIds);
           List<Product> products = productMapper.selectList(productQueryWrapper);

           Order order = new Order();
           order.setId(UUID.randomUUID().toString().replace("-",""));
           order.setOrderNo(UUID.randomUUID().toString().replace("-",""));
           order.setUserId(userId);
           order.setCreateBy(username);
           order.setUpdateBy(username);
           order.setCreateTime(new Date());
           order.setUpdateTime(new Date());
           ServerResponse serverResponse = cartService.shopCartList(userId);
           if(serverResponse.getStatus()==Const.ProductStatusEnum.ON_SALE.getCode()){
               return serverResponse;
           }
           FindCartProductSumVo productSumVo =(FindCartProductSumVo) serverResponse.getData();
            //获取订单金额
           order.setPayment(productSumVo.getCartTotalPrice());

           order.setShippingId(common.getShippingId());
           order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
           //创建订单
            orderMapper.insert(order);
           List<FindCartProductVo> listCartVoList = productSumVo.getListCartVoList();

           //订单项的集合
           List<OrderItemVo> orderItemVoList= new ArrayList<>();

           listCartVoList.stream().filter(c->c.getProductChecked()==Const.Cart.CHECKED).forEach(l->{
               OrderItemVo orderItemVo = new OrderItemVo();
               OrderItem orderItem = new OrderItem();
               orderItem.setCurrentUnitPrice(l.getProductPrice());
               orderItem.setOrderNo(order.getOrderNo());
               orderItem.setProductId(l.getProductId());
               orderItem.setProductImage(l.getProductMainImage());
               orderItem.setProductName(l.getProductName());
               orderItem.setQuantity(l.getQuantity());
               orderItem.setTotalPrice(l.getProductTotalPrice());
               orderItem.setId(UUID.randomUUID().toString().replace("-",""));
               orderItem.setUserId(userId);
               orderItem.setCreateBy(username);
               orderItem.setUpdateBy(username);
               orderItem.setCreateTime(order.getCreateTime());
               orderItem.setUpdateTime(new Date());

               BeanUtils.copyProperties(orderItem,orderItemVo);
               orderItemVoList.add(orderItemVo);
                //创建订单项
                orderItemMapper.insert(orderItem);
           });

           UpdateWrapper<Cart> cartUpdateWrapper = new UpdateWrapper<>();
           List<String> collect = carts.stream().map(Cart::getId).collect(Collectors.toList());
           cartUpdateWrapper.set("is_deleted", 1).set("update_time", new Date()).set("update_by", username).in("id",collect);
           int delete = cartMapper.update(null, cartUpdateWrapper);
            if(delete==0){
                return ServerResponse.createByErrorMessage("购物车信息删除失败");
            }

           CreateOrderVo createOrderVo = new CreateOrderVo();
            BeanUtils.copyProperties(order,createOrderVo);
           createOrderVo.setOrderItemVoList(orderItemVoList);
           ShippingVo shippingVo = new ShippingVo();
            BeanUtils.copyProperties(shipping,shippingVo);
           createOrderVo.setShippingVo(shippingVo);
            return ServerResponse.createBySuccess(createOrderVo);
       }
        return ServerResponse.createByErrorMessage("数据异常");
    }

    @Override
    public ServerResponse getOrderCartProduct(String userId) {

        GetOrderCartProductVo getOrderCartProductVo = new GetOrderCartProductVo();

        //查询购物车列表
        ServerResponse serverResponse = cartService.shopCartList(userId);
        FindCartProductSumVo productSumVo=(FindCartProductSumVo)serverResponse.getData();

        //封装返回值
        getOrderCartProductVo.setImageHost(productSumVo.getImageHost());
        getOrderCartProductVo.setProductTotalPrice(productSumVo.getCartTotalPrice());

        //获取购物车中各项信息
        List<FindCartProductVo> listCartVoList = productSumVo.getListCartVoList();

        List<OrderItemVo> itemVoList = new ArrayList<>();

        //将购物车中各项信息传到itemVoList
        listCartVoList.forEach(l->{
            if(l.getProductChecked()== Const.ProductStatusEnum.PRODUCT_CHECKED.getCode()) {
                OrderItemVo orderItemVo = new OrderItemVo();
                orderItemVo.setTotalPrice(l.getProductTotalPrice());
                orderItemVo.setQuantity(l.getQuantity());
                orderItemVo.setProductName(l.getProductName());
                orderItemVo.setProductImage(l.getProductMainImage());
                orderItemVo.setProductId(l.getProductId());
                orderItemVo.setCurrentUnitPrice(l.getProductPrice());
                itemVoList.add(orderItemVo);
            }
        });

        getOrderCartProductVo.setOrderItemVoList(itemVoList);


        return ServerResponse.createBySuccess(getOrderCartProductVo);
    }

    @Override
    public ServerResponse orderClientList(OrderClientListCommon common, String userId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        List<Order> orders=new ArrayList<>();
        if(userId!=null){
            orders = orderMapper.selectList(null);
        }
        else {
            orders = orderMapper.selectList(orderQueryWrapper);
        }
        if(common.getFlag()!=null){
            orderQueryWrapper.eq("status",common.getFlag());
        }
        PageHelper.startPage(common.getPageNum(),common.getPageSize());


        if(orders.size()==0){
            return ServerResponse.createBySuccess();
        }

        List<String> orderNos = orders.stream().map(Order::getOrderNo).collect(Collectors.toList());

        //根据不同的订单号查询不同的订单
        QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
        orderItemQueryWrapper.in("order_no",orderNos);
        List<OrderItem> orderItems = orderItemMapper.selectList(orderItemQueryWrapper);


        List<OrderClientListVo> listVoList= new ArrayList<>();
        orders.forEach(o->{
            OrderClientListVo orderClientListVo = new OrderClientListVo();
            List<OrderItemVo> itemVoList= new ArrayList<>();

            //对订单的处理
            BeanUtils.copyProperties(o,orderClientListVo);
            if(o.getPaymentType()!=null && 1==o.getPaymentType()){
                orderClientListVo.setPaymentTypeDesc("在线支付");
            }

            if(o.getStatus()==Const.OrderStatusEnum.PAID.getCode() || o.getStatus()==Const.OrderStatusEnum.SHIPPED.getCode() || o.getStatus()==Const.OrderStatusEnum.ORDER_SUCCESS.getCode()){
                orderClientListVo.setStatusDesc("已支付");
            }else{
                orderClientListVo.setStatusDesc("未支付");
            }

            //对订单项处理
            orderItems.forEach(oi->{
                if(o.getOrderNo().equals(oi.getOrderNo())){
                    OrderItemVo orderItemVo = new OrderItemVo();
                    BeanUtils.copyProperties(oi,orderItemVo);
                    //订单项
                    itemVoList.add(orderItemVo);
                }
            });
            //订单中放入订单项集合
            orderClientListVo.setOrderItemVoList(itemVoList);
            orderClientListVo.setImageHost(imageHost);
            Shipping shipping = shippingMapper.selectById(orderClientListVo.getShippingId());
            orderClientListVo.setReceiverName(shipping.getReceiverName());
            ShippingVo shippingVo = new ShippingVo();
            BeanUtils.copyProperties(shipping,shippingVo);
            orderClientListVo.setShippingVo(shippingVo);
            //订单
            listVoList.add(orderClientListVo);
        });

        PageInfo<OrderClientListVo> pageInfo = new PageInfo<>(listVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse detailClientOrder(DetailClientOrderCommon common) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no",common.getOrderNo());
        Order order = orderMapper.selectOne(orderQueryWrapper);
        if(order==null){
            return ServerResponse.createByErrorMessage("没有找到订单");
        }

        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",order.getOrderNo());
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
        OrderClientListVo orderClientListVo = new OrderClientListVo();

        //组装订单内容
        BeanUtils.copyProperties(order,orderClientListVo);
        if(order.getPaymentType()!=null && order.getPaymentType()==1){
            orderClientListVo.setPaymentTypeDesc("在线支付");
        }else{
            orderClientListVo.setPaymentTypeDesc("未支付");
        }

        if(order.getStatus()==Const.OrderStatusEnum.CANCELED.getCode()){
            orderClientListVo.setStatusDesc("已取消");
        }else if(order.getStatus()==Const.OrderStatusEnum.NO_PAY.getCode()){
            orderClientListVo.setStatusDesc("未付款");
        }else if(order.getStatus()==Const.OrderStatusEnum.PAID.getCode()){
            orderClientListVo.setStatusDesc("已付款");
        }else if(order.getStatus()==Const.OrderStatusEnum.SHIPPED.getCode()){
            orderClientListVo.setStatusDesc("已发货");
        }else if(order.getStatus()==Const.OrderStatusEnum.ORDER_SUCCESS.getCode()){
            orderClientListVo.setStatusDesc("交易成功");
        }else if(order.getStatus()==Const.OrderStatusEnum.ORDER_CLOSE.getCode()){
            orderClientListVo.setStatusDesc("交易关闭");
        }
        orderClientListVo.setImageHost(imageHost);
        Shipping shipping = shippingMapper.selectById(orderClientListVo.getShippingId());
        orderClientListVo.setReceiverName(shipping.getReceiverName());
        ShippingVo shippingVo = new ShippingVo();
        BeanUtils.copyProperties(shipping,shippingVo);
        orderClientListVo.setShippingId(shipping.getId());
        orderClientListVo.setShippingVo(shippingVo);

        //组装订单项内容
        List<OrderItemVo> orderItemVoList=new ArrayList<>();
        orderItems.forEach(o->{
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(o,orderItemVo);
            orderItemVoList.add(orderItemVo);
        });
        orderClientListVo.setOrderItemVoList(orderItemVoList);

        return ServerResponse.createBySuccess(orderClientListVo);
    }

    @Override
    public ServerResponse cancelOrder(DetailClientOrderCommon common, String userId, String username) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",common.getOrderNo());
        Order order = orderMapper.selectOne(wrapper);
        if(order==null){
            return ServerResponse.createByErrorMessage("无订单信息");
        }

        if(Const.OrderStatusEnum.PAID.getCode()==order.getStatus()){
            return ServerResponse.createByErrorMessage("该订单已付款");
        }

        //取消订单
        UpdateWrapper<Order> orderUpdateWrapper = new UpdateWrapper<>();
        orderUpdateWrapper.eq("order_no",common.getOrderNo()).set("status",Const.OrderStatusEnum.CANCELED.getCode()).set("update_by",username).set("update_time",new Date());
        int update = orderMapper.update(null, orderUpdateWrapper);
        if(update>0){
            return ServerResponse.createBySuccessMessage("订单取消成功");
        }

        return ServerResponse.createBySuccessMessage("订单取消失败");
    }

    @Override
    public ServerResponse sendGoods(DetailClientOrderCommon common) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",common.getOrderNo());
        Order order = orderMapper.selectOne(wrapper);
        if(order!=null && order.getStatus()==Const.OrderStatusEnum.PAID.getCode()){
            UpdateWrapper<Order> orderUpdateWrapper = new UpdateWrapper<>();
            orderUpdateWrapper.set("status",Const.OrderStatusEnum.SHIPPED.getCode()).set("send_time",new Date()).set("update_time",new Date()).eq("order_no",common.getOrderNo());
            int update = orderMapper.update(null, orderUpdateWrapper);
            if(update>0){
                return ServerResponse.createBySuccessMessage("发货成功");
            }
        }
        return ServerResponse.createByErrorMessage("发货失败");
    }

    @Override
    public ServerResponse manageOrderSearchById(DetailClientOrderCommon common) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",common.getOrderNo());
        Order order = orderMapper.selectOne(wrapper);
        if(order!=null){
            List<OrderListVo> orderListVo=new ArrayList<>();
            orderListVo.add(getOrderListVo(order));
            PageInfo<OrderListVo> pageInfo = new PageInfo<>(orderListVo);
            return ServerResponse.createBySuccess("查询成功", pageInfo);
        }
        return ServerResponse.createByErrorMessage("查询失败");
    }

    public List<OrderItemVo> getOrderItemVos(Order order){
        List<OrderItemVo> orderItemVos=new ArrayList<>();
        QueryWrapper<OrderItem> wrapper=new QueryWrapper<>();
        wrapper.eq("order_no",order.getOrderNo());
        List<OrderItem> orderItems=orderItemMapper.selectList(wrapper);
        orderItems.forEach(orderItem->{
            OrderItemVo orderItemVo=new OrderItemVo();
            BeanUtils.copyProperties(orderItem,orderItemVo);
            orderItemVos.add(orderItemVo);
        });
        return orderItemVos;
    }

    public OrderVo getOrderVo(Order order){
        OrderVo orderVo=new OrderVo();
        List<OrderItemVo> orderItemVos=getOrderItemVos(order);
        BeanUtils.copyProperties(order,orderVo);
        orderVo.setOrderItemVoList(orderItemVos);
        return orderVo;
    }

    public OrderListVo getOrderListVo(Order order){
        OrderListVo orderListVo=new OrderListVo();
        OrderVo orderVo=getOrderVo(order);
        Shipping shipping=shippingMapper.selectById(order.getShippingId());
        BeanUtils.copyProperties(orderVo,orderListVo);

        orderListVo.setImageHost(imageHost);
        orderListVo.setReceiverName(shipping.getReceiverName());
        if (orderListVo.getStatus()==10) {
            orderListVo.setStatusDesc("未付款");
        }
        else if (orderListVo.getStatus()==0) {
            orderListVo.setStatusDesc("已取消");
        }
        else if (orderListVo.getStatus()==40) {
            orderListVo.setStatusDesc("已发货");
        }
        else{
            orderListVo.setStatusDesc("未发货");
        }
        orderListVo.setPaymentTypeDesc("在线付款");
        return orderListVo;
    }
}
