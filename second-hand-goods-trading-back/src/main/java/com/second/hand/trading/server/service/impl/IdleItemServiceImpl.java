package com.second.hand.trading.server.service.impl;

import com.second.hand.trading.server.dao.IdleItemDao;
import com.second.hand.trading.server.dao.UserDao;
import com.second.hand.trading.server.model.IdleItemModel;
import com.second.hand.trading.server.model.UserModel;
import com.second.hand.trading.server.service.IdleItemService;
import com.second.hand.trading.server.vo.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class IdleItemServiceImpl implements IdleItemService {

    @Resource
    private IdleItemDao idleItemDao;

    @Resource
    private UserDao userDao;

    /**
     * 发布闲置
     * @param idleItemModel
     * @return
     */
    public boolean addIdleItem(IdleItemModel idleItemModel) {
        return idleItemDao.insert(idleItemModel) == 1;
    }

    /**
     * 查询闲置信息，同时查出发布者的信息
     * @param id
     * @return
     */
    public IdleItemModel getIdleItem(Long id) {
        IdleItemModel idleItemModel=idleItemDao.selectByPrimaryKey(id);
        if(idleItemModel!=null){
            idleItemModel.setUser(userDao.selectByPrimaryKey(idleItemModel.getUserId()));
        }
        return idleItemModel;
    }

    /**
     * 查询用户发布的所有闲置
     * user_id建索引
     * @param userId
     * @return
     */
    public List<IdleItemModel> getAllIdelItem(Long userId) {
        return idleItemDao.getAllIdleItem(userId);
    }

    /**
     * 搜索，分页
     * 同时查出闲置发布者的信息
     * @param findValue
     * @param page
     * @param nums
     * @return
     */
    public PageVo<IdleItemModel> findIdleItem(String findValue, int page, int nums,Boolean randomPick) {
        List<IdleItemModel> list = null;
        int count=0;
        if (randomPick==null || randomPick == false){
            list=idleItemDao.findIdleItem(findValue, (page - 1) * nums, nums,randomPick);
            count=idleItemDao.countIdleItem(findValue);
        }else {
            list=idleItemDao.findIdleItem(findValue, (page - 1) * nums, 40,randomPick);
            Collections.shuffle(list); // 打乱顺序
            // 将 list 的个数减少到 4 个
            if (list != null && list.size() > nums) {
                list = list.subList(0, nums); // 截取前 5 个商品
                count = nums;
            }
        }

        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItemModel i:list){
                idList.add(i.getUserId());
            }
            /*获取商品对应的卖家信息*/
            List<UserModel> userList=userDao.findUserByList(idList);
            Map<Long,UserModel> map=new HashMap<>();
            for(UserModel user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItemModel i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }

        return new PageVo<>(list,count);
    }

    /**
     * 分类查询，分页
     * 同时查出闲置发布者的信息，代码结构与上面的类似，可封装优化，或改为join查询
     * @param idleLabel
     * @param page
     * @param nums
     * @return
     */
    public PageVo<IdleItemModel> findIdleItemByLable(int idleLabel, int page, int nums) {
        List<IdleItemModel> list=idleItemDao.findIdleItemByLable(idleLabel, (page - 1) * nums, nums);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItemModel i:list){
                idList.add(i.getUserId());
            }
            List<UserModel> userList=userDao.findUserByList(idList);
            Map<Long,UserModel> map=new HashMap<>();
            for(UserModel user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItemModel i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }
        int count=idleItemDao.countIdleItemByLable(idleLabel);
        return new PageVo<>(list,count);
    }

    /**
     * 更新闲置信息
     * @param idleItemModel
     * @return
     */
    public boolean updateIdleItem(IdleItemModel idleItemModel){
        int n = idleItemDao.updateByPrimaryKeySelective(idleItemModel);
        System.out.println("idleItemDao.updateByPrimaryKeySelective(idleItemModel): "+n);
        return n==1;
    }

    public PageVo<IdleItemModel> adminGetIdleList(int status, int page, int nums) {
        List<IdleItemModel> list=idleItemDao.getIdleItemByStatus(status, (page - 1) * nums, nums);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItemModel i:list){
                idList.add(i.getUserId());
            }
            List<UserModel> userList=userDao.findUserByList(idList);
            Map<Long,UserModel> map=new HashMap<>();
            for(UserModel user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItemModel i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }
        int count=idleItemDao.countIdleItemByStatus(status);
        return new PageVo<>(list,count);
    }


    public List<IdleItemModel> getRandomBannerImages() {
        // 1. 查询所有商品
        List<IdleItemModel> allProducts = idleItemDao.findBanner();
        // 2. 随机挑选 5 个商品
        List<IdleItemModel> randomImages = new ArrayList<>();
        if (!allProducts.isEmpty()) {
            Collections.shuffle(allProducts); // 打乱顺序
            for (int i = 0; i < Math.min(5, allProducts.size()); i++) {
                IdleItemModel idleItemModel = new IdleItemModel();
                idleItemModel.setPictureList(allProducts.get(i).getPictureList());
                idleItemModel.setId(allProducts.get(i).getId());
                randomImages.add(idleItemModel);
            }
        }
        return randomImages;
    }
}
