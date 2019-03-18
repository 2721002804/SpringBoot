package com.dd.o2o.service;

import com.dd.o2o.dto.ImageHolder;
import com.dd.o2o.dto.ShopExecution;
import com.dd.o2o.entity.Shop;
import com.dd.o2o.exceptions.ShopOperationException;

public interface ShopService {
	// 分页查询
	ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

	// 通过id查询商铺信息
	Shop getByShopId(long shopId);

	// 更新商铺信息
	ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

	// 添加商铺
	ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;
}
