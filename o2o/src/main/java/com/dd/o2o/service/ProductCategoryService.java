package com.dd.o2o.service;

import java.util.List;

import com.dd.o2o.dto.ProductCategoryExecution;
import com.dd.o2o.entity.ProductCategory;
import com.dd.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	// 查询指定店铺下的所有商品类别信息
	List<ProductCategory> getProductCategoryList(long shopId);

	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;

	// 将此类别下的商品里的类别id置为空，再删除掉该商品的类别
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
