package com.service;


import com.model.spw.ShopInfo;

import java.util.List;
import java.util.Map;

/**
 * 订单服务
 * </br>
 * Created by zhangnan on 2017/11/20.
 */
public interface OrderService {

  /**
   * 导入账单信息
   */
  public void importSalesNumForCategory(long groupID, long shopID, long reportDate, int count) throws Exception;
  public int importSalesNumForFood(long groupID, long shopID, long reportDate)throws Exception;
  public String getToken();


  //查询菜品信息
  public Map<String,Object> queryFoood(long groupID, long shopID, long reportDate);
 //查询收入收款信息
  public Map<String,Object> queryPayInfo(long groupID, long shopID, long reportDate);
  public void importPayInfo(long groupID, long shopID, long reportDate) throws Exception;

  public Map<String,Object> queryYuanGongCanInfo(long groupID, long shopID, long reportDate);

  public void importYuanGongCanInfo(long groupID, long shopID, long reportDate) throws Exception;


    public List<ShopInfo> queryShopIds(long groupID);
}
