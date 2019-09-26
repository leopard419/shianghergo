package com.shianghergo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.shianghergo.exception.ProductNotFoundException;
import com.shianghergo.model.GroupsBean;
import com.shianghergo.model.GroupsCartBean;
import com.shianghergo.model.GroupsOrderBean;
import com.shianghergo.model.GroupsOrderDetailBean;
import com.shianghergo.model.ItemBean;
import com.shianghergo.model.MemberBean;
import com.shianghergo.model.OrderBean;
import com.shianghergo.model.OrderDetailBean;
import com.shianghergo.model.StoreBean;
import com.shianghergo.service.MemberService;
import com.shianghergo.service.ProductService;

@Transactional
@SessionAttributes("loginOK")
@Controller
public class ProductController {
	@Autowired
	ProductService service;
	
	@Autowired
	MemberService memberService;

	@Autowired
	ServletContext context;
	
	// -------------------以下聖捷--------------------
	
	@RequestMapping(value = "/Member002")
	public String getMyOrderList(@ModelAttribute("loginOK")MemberBean mb, Model model,StoreBean sb) {
		System.out.println("MemberBean mb++"+mb);
		List<OrderBean> list = memberService.getMemberOrders(mb.getId());
		String[] list2 = new String[list.size()];
		for(int i=0 ; i<list.size();i++) {
			StoreBean store = service.getStoreById(list.get(i).getStore_id());
			list2[i] = store.getName();
		}
		model.addAttribute("MyOrderList",list);//service.getorderById(ob.getId())
		model.addAttribute("it",list2);
		System.out.println("getMyOrderList裡的list:"+list);
		return "Member002";
	}
	@RequestMapping(value = "/Member003")
	public String getMyGroupsList(@ModelAttribute("loginOK")MemberBean mb, Model model) {
		List<GroupsBean> list = memberService.getMemberGroups(mb.getId());
		model.addAttribute("MyGroupsList",list);
		return "Member003";
	}
	@RequestMapping("/getMyOrderListD")
	public String getMyOrderListD(@RequestParam("order_id")Integer order_id, Model model) {
		List<OrderDetailBean> list = memberService.getMemberOrdersD(order_id);
		System.out.println("order_id::"+order_id);
		System.out.println("getMyOrderListD()裡的="+list);
		model.addAttribute("MyOrderListD",list);
		return "Member002_1";
	}
	
	@RequestMapping("/getMyGroupsListD")
	public String getMyGroupsListD(@RequestParam("groups_id")Integer groups_id, Model model) {
		List<GroupsCartBean> list = memberService.getGroupsOrdersD(groups_id);
		System.out.println("getMyGroupsListD()裡的="+list);
		model.addAttribute("MyGroupsListD",list);
		return "Member003_1";
	}
	
	@RequestMapping("/getMyGOrderListD")
	public String getMyGOrderListD(@RequestParam("groups_order_info_id")Integer groups_order_info_id, Model model) {
		List<GroupsOrderDetailBean> list = memberService.getGOrderD(groups_order_info_id);
		System.out.println("getMyGOrderListD()裡的="+list);
		model.addAttribute("MyGroupsListD",list);
		return "Member004_1";
	}
	
	@RequestMapping(value = "/Member004")
	public String getMyGroupsOrderList(@ModelAttribute("loginOK")MemberBean mb, Model model) {
		List<GroupsOrderBean> list = memberService.getMemberGroupsOrders(mb.getId());
		model.addAttribute("MyGroupsOrderList",list);
		System.out.println("參與的"+list);
		return "Member004";
	}
	
	
	//-------------------------以下浩瑜----------------------
	
	@RequestMapping("/hao/products")
	public String list(Model model) {
		List<ItemBean> list = service.getAllProducts();
		model.addAttribute("products", list);
		return "hao/products";
	}

	@RequestMapping("/hao/product")
	public String getProductsById(@RequestParam("id") Integer id, Model model) {
		model.addAttribute("product", service.getProductById(id));
		return "hao/product";
	}
	
	@RequestMapping(value = "/hao/product/Update", method = RequestMethod.GET)
	public String getUpdateProductForm(@RequestParam("id") Integer id, Model model) {
		model.addAttribute("product", service.getProductById(id));
		ItemBean bb = new ItemBean();
		model.addAttribute("itemBean", bb);
		return "hao/productUpdate";
	}
	
	@RequestMapping(value = "/hao/product/Update", method = RequestMethod.POST)
	public String getUpdateProductForm(@RequestParam(value = "id",required = false)Integer id, @ModelAttribute("itemBean") ItemBean bb, BindingResult result, HttpServletRequest request) {
//		String[] suppressedFields = result.getSuppressedFields();
//		if (suppressedFields.length > 0) {
//			throw new RuntimeException("嘗試傳入不允許的欄位:" + StringUtils.arrayToCommaDelimitedString(suppressedFields));
//		}
		MultipartFile productImage = bb.getProductImage();
		String originalFilename = productImage.getOriginalFilename();
		bb.setFileName(originalFilename);

		String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
		String rootDirectory = context.getRealPath("/");

		if (productImage != null && !productImage.isEmpty()) {
			byte[] b;
			try {
				b = productImage.getBytes();
				Blob blob = new SerialBlob(b);
				bb.setCoverImage(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常:" + e.getMessage());
			}
		}
		bb.setId(id);
		service.updateItem(bb);

		try {
			File imageFolder = new File(rootDirectory, "images");
			if (!imageFolder.exists())
				imageFolder.mkdirs();
			File file = new File(imageFolder, bb.getId() + ext);
			productImage.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳生異常:" + e.getMessage());
		}
		
		return "redirect:/hao/products";
	}
	
	@RequestMapping(value = "/hao/products/add", method = RequestMethod.GET)
	public String getAddNewProductForm(Model model) {
		ItemBean bb = new ItemBean();
		model.addAttribute("itemBean", bb);
		return "hao/addProduct";
	}

	@RequestMapping(value = "/hao/products/add", method = RequestMethod.POST)
	public String getAddNewProductForm(@ModelAttribute("itemBean") ItemBean bb, BindingResult result, HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("嘗試傳入不允許的欄位:" + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}

		MultipartFile productImage = bb.getProductImage();
		String originalFilename = productImage.getOriginalFilename();
		bb.setFileName(originalFilename);

		String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
		String rootDirectory = context.getRealPath("/");

		if (productImage != null && !productImage.isEmpty()) {
			byte[] b;
			try {
				b = productImage.getBytes();
				Blob blob = new SerialBlob(b);
				bb.setCoverImage(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常:" + e.getMessage());
			}
		}

		service.addProduct(bb);

		try {
			File imageFolder = new File(rootDirectory, "images");
			if (!imageFolder.exists())
				imageFolder.mkdirs();
			File file = new File(imageFolder, bb.getId() + ext);
			productImage.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳生異常:" + e.getMessage());
		}
		return "redirect:/hao/products";
	}

	@RequestMapping(value = "/hao/product/Update/Delete", method = RequestMethod.POST)
	public String ProductDelete(@RequestParam(value = "id",required = false)Integer id, 
			@ModelAttribute("itemBean") ItemBean bb, BindingResult result, HttpServletRequest request) {
		bb.setId(id);
		service.deleteProduct(id);
		
		return "redirect:/hao/products";
	}
	
	@ModelAttribute("categoryList")
	public List<String> getCategoryList() {
		return service.getAllCategories();
	}

	@InitBinder
	public void whiteListing(WebDataBinder binder) {
		binder.setAllowedFields("name", "category", "store_id", "reserve", "price", "detail", "productImage");
	}

	

	@RequestMapping(value = "/hao/getPicture/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getPicture(HttpServletRequest resp, @PathVariable Integer id) {
		String filePath = "/resources/images/NoImage.jpg";
		byte[] media = null;
		HttpHeaders headers = new HttpHeaders();
		String filename = "";
		int len = 0;
		ItemBean bean = service.getProductById(id);
		if (bean != null) {
			Blob blob = bean.getCoverImage();
			filename = bean.getFileName();
			if (blob != null) {
				try {
					len = (int) blob.length();
					media = blob.getBytes(1, len);
				} catch (SQLException e) {
					throw new RuntimeException("ProductController的getPicture()發生SQLException:" + e.getMessage());
				}
			} else {
				media = toByteArray(filePath);
				filename = filePath;
			}
		} else {
			media = toByteArray(filePath);
			filename = filePath;
		}
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		String mimeType = context.getMimeType(filename);
		MediaType mediaType = MediaType.valueOf(mimeType);
		System.out.println("mediaType = " + mediaType);
		headers.setContentType(mediaType);
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
		return responseEntity;
	}

	private byte[] toByteArray(String filePath) {
		byte[] b = null;
		String realPath = context.getRealPath(filePath);
		try {
			File file = new File(realPath);
			long size = file.length();
			b = new byte[(int) size];
			InputStream fis = context.getResourceAsStream(filePath);
			fis.read(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	@ExceptionHandler({ProductNotFoundException.class})
	public ModelAndView handleError(HttpServletRequest request,ProductNotFoundException exception) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("invalidItemId", exception.getId());
		mv.addObject("exception", exception);
		mv.addObject("url", request.getRequestURL() + "?" + request.getQueryString());
		mv.setViewName("productNotFound");
		return mv;
	}
}