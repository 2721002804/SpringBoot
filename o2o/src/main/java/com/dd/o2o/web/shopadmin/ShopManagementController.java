package com.dd.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.dd.o2o.dto.ImageHolder;
import com.dd.o2o.dto.ShopExecution;
import com.dd.o2o.entity.Area;
import com.dd.o2o.entity.PersonInfo;
import com.dd.o2o.entity.Shop;
import com.dd.o2o.entity.ShopCategory;
import com.dd.o2o.enums.ShopStateEnum;
import com.dd.o2o.service.AreaService;
import com.dd.o2o.service.ShopCategoryService;
import com.dd.o2o.service.ShopService;
import com.dd.o2o.util.CodeUtil;
import com.dd.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;

	@RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			} else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		} else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误！");
			return modelMap;
		}
		// 1.接受并转换相应参数
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		// System.out.println(shopStr);
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "1" + e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		// 2.修改店铺信息
		if (shop != null && shop.getShopId() != null) {
			ShopExecution se;
			try {
				if (shopImg == null) {
					se = shopService.modifyShop(shop, null);
				} else {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageHolder);
				}
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());

				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "2" + e.getMessage());
			}

			return modelMap;

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺Id");
			return modelMap;
		}
	}

	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request) {
		System.out.print("111");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				// TODO: handle exception
				modelMap.put("success", false);
				modelMap.put("errMsg:", e.getMessage());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> modeMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modeMap.put("shopCategoryList", shopCategoryList);
			modeMap.put("areaList", areaList);
			modeMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			modeMap.put("success", false);
			modeMap.put("errMsg", e.getMessage());
		}
		return modeMap;
	}

	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modeMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modeMap.put("success", false);
			modeMap.put("errMsg", "验证码错误！");
			return modeMap;
		}
		// 1.接受并转换相应参数
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		// System.out.println(shopStr);
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modeMap.put("success", false);
			modeMap.put("errMsg", "1" + e.getMessage());
			return modeMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modeMap.put("success", false);
			modeMap.put("errMsg", "上传图片不能为空");
			return modeMap;
		}
		// 2.注册店铺
		if (shop != null && shopImg != null) {
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				se = shopService.addShop(shop, imageHolder);
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modeMap.put("success", true);
					// 该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if (shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				} else {
					modeMap.put("success", false);
					modeMap.put("errMsg", se.getStateInfo());

				}
			} catch (IOException e) {
				modeMap.put("success", false);
				modeMap.put("errMsg", "2" + e.getMessage());
			}

			return modeMap;

		} else {
			modeMap.put("success", false);
			modeMap.put("errMsg", "请输入店铺信息");
			return modeMap;
		}
	}

	// private static void inputStreamToFile(InputStream ins, File file) {
	// FileOutputStream os = null;
	// try {
	// os = new FileOutputStream(file);
	// int bytesRead = 0;
	// byte[] buffer = new byte[1024];
	// while ((bytesRead = ins.read(buffer)) != -1) {
	// os.write(buffer, 0, bytesRead);
	// }
	// } catch (Exception e) {
	// throw new RuntimeException("调用inputStreamToFile产生异常：" + e.getMessage());
	// } finally {
	// try {
	// if (os != null) {
	// os.close();
	// }
	// if (ins != null) {
	// ins.close();
	// }
	// } catch (Exception e) {
	// throw new RuntimeException("inputStreamToFile关闭io产生异常：" +
	// e.getMessage());
	// }
	// }
	// }

}
