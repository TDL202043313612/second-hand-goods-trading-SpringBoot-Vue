package com.second.hand.trading.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.second.hand.trading.server.enums.ErrorMsg;
import com.second.hand.trading.server.model.IdleItemModel;
import com.second.hand.trading.server.service.IdleItemService;
import com.second.hand.trading.server.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("idle")
public class IdleItemController {

    @Autowired
    private IdleItemService idleItemService;

    @PostMapping("add")
    public ResultVo addIdleItem(@CookieValue("shUserId")
                                    @NotNull(message = "登录异常 请重新登录")
                                    @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                @RequestBody String idleItemModel) throws JsonProcessingException {
//        idleItemModel.setUserId(Long.valueOf(shUserId));
//        idleItemModel.setIdleStatus((byte) 1);
//        idleItemModel.setReleaseTime(new Date());
//        System.out.println("idleItemModel: "+idleItemModel);
//        System.out.println("idleItemModel.getIdleName"+idleItemModel.getIdleName());
        System.out.println("idleItemModel_String: "+idleItemModel);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(idleItemModel);
        IdleItemModel idleItemModel_ = new IdleItemModel();
        idleItemModel_.setUserId(Long.valueOf(shUserId));
        idleItemModel_.setIdleStatus((byte) 1);
        idleItemModel_.setReleaseTime(new Date());

        idleItemModel_.setIdleName(jsonNode.get("idleName").asText());
        idleItemModel_.setIdleDetails(jsonNode.get("idleDetails").asText());
        idleItemModel_.setPictureList(jsonNode.get("pictureList").asText());
        idleItemModel_.setIdlePrice(jsonNode.get("idlePrice").decimalValue());
        idleItemModel_.setIdlePlace(jsonNode.get("idlePlace").asText());
        idleItemModel_.setIdleLabel(jsonNode.get("idleLabel").asInt());
        System.out.println("idleItemModel_: "+idleItemModel_);


        if(idleItemService.addIdleItem(idleItemModel_)){
            return ResultVo.success(idleItemModel_);
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("info")
    public ResultVo getIdleItem(@RequestParam Long id){
        return ResultVo.success(idleItemService.getIdleItem(id));
    }

    @GetMapping("all")
    public ResultVo getAllIdleItem(@CookieValue("shUserId")
                                       @NotNull(message = "登录异常 请重新登录")
                                       @NotEmpty(message = "登录异常 请重新登录") String shUserId){
        return ResultVo.success(idleItemService.getAllIdelItem(Long.valueOf(shUserId)));
    }

    @GetMapping("find")
    public ResultVo findIdleItem(@RequestParam(value = "findValue",required = false) String findValue,
                                 @RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "nums",required = false) Integer nums,
                                 @RequestParam(value = "randomPick", required = false) Boolean randomPick){
        if(null==findValue){
            findValue="";
        }

        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(idleItemService.findIdleItem(findValue,p,n,randomPick));
    }

    @GetMapping("lable")
    public ResultVo findIdleItemByLable(@RequestParam(value = "idleLabel",required = true) Integer idleLabel,
                                 @RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "nums",required = false) Integer nums){
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(idleItemService.findIdleItemByLable(idleLabel,p,n));
    }

    @PostMapping("update")
    public ResultVo updateIdleItem(@CookieValue("shUserId")
                                       @NotNull(message = "登录异常 请重新登录")
                                       @NotEmpty(message = "登录异常 请重新登录") String shUserId,
                                   @RequestBody IdleItemModel idleItemModel){
        idleItemModel.setUserId(Long.valueOf(shUserId));
        if(idleItemService.updateIdleItem(idleItemModel)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("/banner")
    public ResultVo getBanner() {
        // 1. 获取随机图片 URL 列表
        List<IdleItemModel> randomImages = idleItemService.getRandomBannerImages();

        // 2. 返回结果
        return ResultVo.success(randomImages);
    }
}
