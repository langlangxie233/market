package com.talent.market.live.Controller.manage;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Maps;
import com.talent.market.live.common.product.*;
import com.talent.market.live.model.User;
import com.talent.market.live.service.FileService;
import com.talent.market.live.service.ProductService;
import com.talent.market.live.util.PropertiesUtil;
import com.talent.market.live.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author huangzhengwei
 * @desc
 */
@RequestMapping("/manage/product/")
@RestController
public class ProductManageController {
    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @PostMapping("list.do")
    public ServerResponse productManageList(@RequestBody ProductManageListCommon common, HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }
        return productService.productManageList(common);
    }

    @PostMapping("search.do")
    public ServerResponse ProductSearchByCondition(@RequestBody ProductSearchByConditionCommon common, HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }

        return productService.ProductSearchByCondition(common);

    }


    /**
     * 图片上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @PostMapping("upload.do")
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     * 富文本上传图片
     * @param session
     * @param file
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/richtext_img_upload.do")
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file,path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }

    /**
     * 产品详情
     * @return
     */
    @PostMapping("detail.do")
    public ServerResponse productDetail(@RequestBody ProductDetailCommon common, HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }

        return productService.productDetail(common);
    }


    @PostMapping("set_sale_status.do")
    public ServerResponse setSaleStatus(@RequestBody SetSaleStatusCommon common, HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }
        User user=(User) session.getAttribute("manage_user");
        return productService.setSaleStatus(common, user);
    }

    @PostMapping("save.do")
    private ServerResponse saveOrUpdateProduct(@RequestBody SaveOrUpdateProductCommon common, HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }
        User user=(User) session.getAttribute("manage_user");
        return productService.saveOrUpdateProduct(common, user);
    }

    public ServerResponse validUser(HttpSession session){
        User user=(User) session.getAttribute("manage_user");
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(10,"请登录");
        }
        if(user.getRole().equals(0)){
            return ServerResponse.createByErrorMessage("权限不足");
        }
        return ServerResponse.createBySuccess();
    }

}
