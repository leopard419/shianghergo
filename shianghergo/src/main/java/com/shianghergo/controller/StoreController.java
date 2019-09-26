package com.shianghergo.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shianghergo.model.StoreBean;
import com.shianghergo.service.StoreService;


@Controller
public class StoreController {
	@Autowired
	StoreService service;
	
	@Autowired
	ServletContext context;
	
	@RequestMapping(value = "/hao/buildStore", method = RequestMethod.GET)
	public String getAddNewProductForm(Model model) {
		StoreBean sb = new StoreBean();
		model.addAttribute("storeBean", sb);
		return "hao/buildStore";
	}

	@RequestMapping(value = "/hao/buildStore", method = RequestMethod.POST)
	public String getAddNewProductForm(@ModelAttribute("storeBean") StoreBean sb, Model model,
			RedirectAttributes redirectAttributes, BindingResult result, HttpServletRequest request,
			HttpSession session) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("嘗試傳入不允許的欄位:" + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		if(sb.getMember_id()!=null&&sb.getName()!=null&&sb.getDetail()!=null) {
			redirectAttributes.addFlashAttribute("name", sb.getName());
			redirectAttributes.addFlashAttribute("success", "註冊成功");
			sb.setStatus(1);
			service.buildStore(sb);
			return "redirect:/";
		}else {
			redirectAttributes.addFlashAttribute("error", "註冊失敗");
			return "redirect:/hao/buildStore";
		}
	}
	
	@RequestMapping(value = "/hao/store/Update", method = RequestMethod.GET)
	public String getUpdateStoreForm(@RequestParam("id") Integer id, Model model) {
		model.addAttribute("store", service.getStoreById(id));
		StoreBean bb = new StoreBean();
		model.addAttribute("storeBean", bb);
		return "hao/storeUpdate";
	}
	
	@RequestMapping(value = "/hao/store/Update", method = RequestMethod.POST)
	public String getUpdateStoreForm(@RequestParam(value = "id",required = false)Integer id, @ModelAttribute("storeBean") StoreBean bb, BindingResult result, HttpServletRequest request) {
//		String[] suppressedFields = result.getSuppressedFields();
//		if (suppressedFields.length > 0) {
//			throw new RuntimeException("嘗試傳入不允許的欄位:" + StringUtils.arrayToCommaDelimitedString(suppressedFields));
//		}
		
		bb.setId(id);;
		service.updateStore(bb);
		
		return "redirect:/hao/stores";
	}
	
	@RequestMapping("/hao/stores")
	public String list(Model model) {
		List<StoreBean> list = service.getAllStores();
		model.addAttribute("stores", list);
		return "hao/stores";
	}
	
	@RequestMapping("/hao/store")
	public String getProductsById(@RequestParam("id") Integer id, Model model) {
		model.addAttribute("store", service.getStoreById(id));
		return "hao/store";
	}
}