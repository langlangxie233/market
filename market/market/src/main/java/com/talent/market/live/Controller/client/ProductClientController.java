package com.talent.market.live.Controller.client;

import com.talent.market.live.common.product.ProductDetailCommon;
import com.talent.market.live.common.product.ProductListCommon;
import com.talent.market.live.service.ProductService;
import com.talent.market.live.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author xiexianlang
 * @desc
 */
@RestController
@RequestMapping("/product/")
public class ProductClientController {
    @Autowired
    private ProductService productService;

    @PostMapping("list.do")
    public ServerResponse productClientList(@RequestBody ProductListCommon common){

        return productService.productClientList(common);
    }

    @PostMapping("detail.do")
    public ServerResponse productClientDetail(@RequestBody ProductDetailCommon common){

        return productService.productClientDetail(common);
    }
}
