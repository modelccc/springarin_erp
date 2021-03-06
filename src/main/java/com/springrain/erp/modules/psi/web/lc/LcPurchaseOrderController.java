/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.springrain.erp.modules.psi.web.lc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.springrain.erp.common.config.Global;
import com.springrain.erp.common.email.MailInfo;
import com.springrain.erp.common.email.MailManager;
import com.springrain.erp.common.persistence.Page;
import com.springrain.erp.common.service.BaseService;
import com.springrain.erp.common.utils.Collections3;
import com.springrain.erp.common.utils.DateUtils;
import com.springrain.erp.common.utils.FileUtils;
import com.springrain.erp.common.utils.StringUtils;
import com.springrain.erp.common.utils.pdf.PdfUtil;
import com.springrain.erp.common.web.BaseController;
import com.springrain.erp.modules.amazoninfo.service.AmazonProduct2Service;
import com.springrain.erp.modules.custom.entity.SendEmail;
import com.springrain.erp.modules.custom.service.SendEmailService;
import com.springrain.erp.modules.psi.entity.ProductSupplier;
import com.springrain.erp.modules.psi.entity.PsiProduct;
import com.springrain.erp.modules.psi.entity.PsiProductAttribute;
import com.springrain.erp.modules.psi.entity.PsiProductTieredPriceDto;
import com.springrain.erp.modules.psi.entity.PsiSupplier;
import com.springrain.erp.modules.psi.entity.PurchaseForecastDto;
import com.springrain.erp.modules.psi.entity.lc.LcHisPurchaseOrder;
import com.springrain.erp.modules.psi.entity.lc.LcHisPurchaseOrderItem;
import com.springrain.erp.modules.psi.entity.lc.LcPsiLadingBill;
import com.springrain.erp.modules.psi.entity.lc.LcPurchaseFinancialDto;
import com.springrain.erp.modules.psi.entity.lc.LcPurchaseOrder;
import com.springrain.erp.modules.psi.entity.lc.LcPurchaseOrderDeliveryDate;
import com.springrain.erp.modules.psi.entity.lc.LcPurchaseOrderInvoice;
import com.springrain.erp.modules.psi.entity.lc.LcPurchaseOrderInvoiceItem;
import com.springrain.erp.modules.psi.entity.lc.LcPurchaseOrderItem;
import com.springrain.erp.modules.psi.entity.lc.LcPurchasePayment;
import com.springrain.erp.modules.psi.entity.lc.LcPurchasePaymentItem;
import com.springrain.erp.modules.psi.entity.parts.PsiPartsInventoryOut;
import com.springrain.erp.modules.psi.scheduler.PoEmailManager;
import com.springrain.erp.modules.psi.service.PsiProductAttributeService;
import com.springrain.erp.modules.psi.service.PsiProductEliminateService;
import com.springrain.erp.modules.psi.service.PsiProductGroupUserService;
import com.springrain.erp.modules.psi.service.PsiProductMoldFeeService;
import com.springrain.erp.modules.psi.service.PsiProductPartsService;
import com.springrain.erp.modules.psi.service.PsiProductService;
import com.springrain.erp.modules.psi.service.PsiProductTieredPriceService;
import com.springrain.erp.modules.psi.service.PsiProductTypeGroupDictService;
import com.springrain.erp.modules.psi.service.PsiSupplierService;
import com.springrain.erp.modules.psi.service.PurchaseForecastService;
import com.springrain.erp.modules.psi.service.lc.LcHisPurchaseOrderService;
import com.springrain.erp.modules.psi.service.lc.LcPsiLadingBillService;
import com.springrain.erp.modules.psi.service.lc.LcPurchaseFinancialService;
import com.springrain.erp.modules.psi.service.lc.LcPurchaseOrderService;
import com.springrain.erp.modules.psi.service.parts.PsiPartsInventoryOutService;
import com.springrain.erp.modules.sys.entity.Role;
import com.springrain.erp.modules.sys.entity.User;
import com.springrain.erp.modules.sys.service.SystemService;
import com.springrain.erp.modules.sys.utils.DictUtils;
import com.springrain.erp.modules.sys.utils.UserUtils;

/**
 * 采购订单Controller
 * @author Michael
 * @version 2014-10-29
 */
@Controller
@RequestMapping(value = "${adminPath}/psi/lcPurchaseOrder")
public class LcPurchaseOrderController extends BaseController {
	
	@Autowired
	private LcPurchaseOrderService	 	purchaseOrderService;
	@Autowired
	private LcHisPurchaseOrderService  	hisPurchaseOrderService;
	@Autowired
	private SendEmailService 		 	sendEmailService;
	@Autowired
	private PsiSupplierService 		 	psiSupplierService;
	@Autowired
	private SystemService 			 	systemService;
	@Autowired
	private LcPsiLadingBillService     	billService;
	@Autowired
	private PsiProductService        	productService;
	@Autowired
	private PoEmailManager           	poMaillManager;
	@Autowired
	private MailManager              	mailManager;
	@Autowired
	private PsiPartsInventoryOutService partsOutService;
	@Autowired
	private PsiProductPartsService       productPartsService;
	@Autowired
	private LcPurchaseFinancialService	 purchaseFinancialService;
	@Autowired
	private PurchaseForecastService	 	 purchaseForecastService;
	@Autowired
	private PsiProductTieredPriceService productTieredPriceService;
	@Autowired
	private PsiProductEliminateService 	 productEliminateService;
	@Autowired
	private PsiProductAttributeService   productAttrService;
	@Autowired
	private PsiProductTypeGroupDictService  typeLineService;
	@Autowired
	private PsiProductGroupUserService psiProductGroupUserService;
	
	@Autowired
	private PsiProductMoldFeeService psiProductMoldFeeService;
	private static String filePath; 
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = {"list", ""})
	public String list(LcPurchaseOrder purchaseOrder,String isCheck,String productIdColor,String productName, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		String countryCode=purchaseOrder.getVersionNo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today =sdf.parse(sdf.format(new Date()));
		if (purchaseOrder.getCreateDate() == null) {
			purchaseOrder.setCreateDate(DateUtils.addMonths(today, -3));
		}
		//查询当前登陆人的角色，如果是跟单员，默认为只看自己的
		if(StringUtils.isEmpty(isCheck)){
			isCheck="0";
			Set<Role> roles = UserUtils.getUser().getRoleList();
			for (Role role : roles) {
				if(role.getName().contains("跟单员")){
					isCheck="1";
					break;
				}
			}
		}
		
		List<PsiSupplier> suppliers=this.psiSupplierService.findAll();
		Map<Integer,String> supplierAlertMap = Maps.newHashMap();
		for(PsiSupplier supplier:suppliers){
			supplierAlertMap.put(supplier.getId(), supplier.getPayRemark());
		}
		Page<LcPurchaseOrder> page =new Page<LcPurchaseOrder>(request, response);
        purchaseOrderService.findByProduct(page, purchaseOrder,isCheck,productIdColor); 
        
    	for(LcPurchaseOrder order:page.getList()){
        	for (Iterator<LcPurchaseOrderItem> iterator = order.getItems().iterator(); iterator.hasNext();) {
        		LcPurchaseOrderItem item = (LcPurchaseOrderItem) iterator.next();
        		Date  preDate = item.getDeliveryDate();
        		if(item.getActualDeliveryDate()!=null){
        			preDate=item.getActualDeliveryDate();
        		}
        		if((StringUtils.isNotEmpty(productName)&&!item.getProductNameColor().equals(productName))||(StringUtils.isNotEmpty(countryCode)&&!item.getCountryCode().equals(countryCode))||(purchaseOrder.getPurchaseDate()!=null&&preDate.after(purchaseOrder.getPurchaseDate())||purchaseOrder.getCreateDate().after(preDate))){
        			iterator.remove();
        		}
			}
	     }
        
        Map<String,String> proColorMap =this.purchaseOrderService.getAllProductColors(); 
        Map<String,String> fnskuMap=this.purchaseOrderService.getSkuAndFnsku();
        
        model.addAttribute("supplierAlertMap", supplierAlertMap);
        model.addAttribute("proColorMap", proColorMap);
        model.addAttribute("productName", productName);
    	model.addAttribute("suppliers", suppliers);
        model.addAttribute("isCheck", isCheck);
        model.addAttribute("fnskuMap", fnskuMap);
        model.addAttribute("page", page);
		return "modules/psi/lc/lcPurchaseOrderProductList";
	}
	

	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = {"singleProduct"})
	public String singleProduct(String productIdColor,String countryCode,String productName,String snNo, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        String[]  arr=productIdColor.split(",");
        Integer productId = Integer.parseInt(arr[0]);
        String colorCode ="";
        if(arr.length>1){
        	colorCode = arr[1];
        }
        String orderNo =new LcPurchaseOrder().getOrderNoBySnCode(snNo);
        Page<LcPurchaseOrder> page = purchaseOrderService.findByProduct(new Page<LcPurchaseOrder>(request, response), productId,colorCode,countryCode,orderNo); 
        
        if(StringUtils.isNotEmpty(countryCode)){
        	for(LcPurchaseOrder order:page.getList()){
	        	for (Iterator<LcPurchaseOrderItem> iterator = order.getItems().iterator(); iterator.hasNext();) {
	        		LcPurchaseOrderItem item = (LcPurchaseOrderItem) iterator.next();
	        		if(!item.getProductNameColor().equals(productName)||!item.getCountryCode().equals(countryCode)){
	        			iterator.remove();
	        		}
				}
		     }
		}else{
			 for(LcPurchaseOrder order:page.getList()){
	        	for (Iterator<LcPurchaseOrderItem> iterator = order.getItems().iterator(); iterator.hasNext();) {
	        		LcPurchaseOrderItem item = (LcPurchaseOrderItem) iterator.next();
	        		if(!item.getProductNameColor().equals(productName)){
	        			iterator.remove();
	        		}
				}
		     }
		}
       
        
        Map<String,String> proColorMap =this.purchaseOrderService.getAllProductColors(); 
        Map<String,String> fnskuMap=this.purchaseOrderService.getSkuAndFnsku();
        model.addAttribute("proColorMap", proColorMap);
        model.addAttribute("productName", productName);
        model.addAttribute("countryCode", countryCode);
        model.addAttribute("fnskuMap", fnskuMap);
        model.addAttribute("page", page);
		return "modules/psi/lc/lcPurchaseOrderSingleProductList";
	}
	
	   
	/**
	 *欠货明细 
	 * 
	 */
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value ="lessCargoList")
	public String lessCargoList(LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<PsiSupplier> suppliers=this.psiSupplierService.findAll();
        Page<LcPurchaseOrder> page = purchaseOrderService.findLessCargoList(new Page<LcPurchaseOrder>(request, response), purchaseOrder); 
        Map<String,LcPurchaseOrder> productMap = Maps.newLinkedHashMap();
        if(page.getList().size()>0){
        	 List<LcPurchaseOrder>  orders = page.getList();
        	 for(LcPurchaseOrder order:orders){
        		 for(LcPurchaseOrderItem item:order.getItems()){
        			 LcPurchaseOrder temp= new LcPurchaseOrder();
        			 if(!item.getQuantityOrdered().equals(item.getQuantityReceived())){
	        			 if(productMap.get(item.getProductName())==null){
	    					 temp.setTempLessCargoQuantity(item.getQuantityOrdered()-item.getQuantityReceived());
	    					 temp.setTempNoSureCargoQuantity(item.getQuantityPreReceived());
	    					 productMap.put(item.getProductName(),temp);
	        			 }else{
	        				 temp=productMap.get(item.getProductName());
	        				 temp.setTempLessCargoQuantity(temp.getTempLessCargoQuantity()+item.getQuantityOrdered()-item.getQuantityReceived());
	        				 temp.setTempNoSureCargoQuantity(temp.getTempNoSureCargoQuantity()+item.getQuantityPreReceived());
	        			 }
	        			 temp.getItems().add(item);
        			 }
        		 }
        	 }
             
        }
        
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplierId",purchaseOrder.getSupplier().getId());
        model.addAttribute("productMap", productMap);
        //model.addAttribute("page", page);
		return "modules/psi/lc/lcPurchaseLessCargoList";
	}
	
	
	/**
	 *资金视图
	 * 
	 */
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = {"moneyView"})
	public String moneyView(LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Date today = new Date();
		today.setHours(0);
		today.setSeconds(0);
		today.setMinutes(0);
		if (purchaseOrder.getCreateDate() == null) {
			purchaseOrder.setCreateDate(DateUtils.addMonths(today, -1));
			purchaseOrder.setPurchaseDate(today);
		}
		
		List<PsiSupplier> suppliers=this.psiSupplierService.findAll();
		model.addAttribute("suppliers", suppliers);
        Page<LcPurchaseOrder> page = purchaseOrderService.find(new Page<LcPurchaseOrder>(request, response), purchaseOrder); 
       
        model.addAttribute("page", page);
		return "modules/psi/lc/lcPurchaseOrderMoneyList";
	}
	
	
	@RequestMapping(value = {"preReceived"})
	public String preReceived(String firstOnce,String moreOnce,Integer week, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = sdf.parse(sdf.format(new Date()));
		Set<Integer> newProductIds = Sets.newHashSet();
		if(StringUtils.isEmpty(firstOnce)){
			firstOnce="0";
		}
		if(StringUtils.isEmpty(moreOnce)){
			moreOnce="0";
		}
		if("1".equals(firstOnce)||"1".equals(moreOnce)){
			newProductIds=this.productService.findNewMap().keySet();
		}
		List<Object[]> obs= this.purchaseOrderService.getDeliveryProducts(today, week, newProductIds,firstOnce,moreOnce);
		List<PsiProductAttribute> attrs=productAttrService.findAll();
		Map<String, Integer> transportTypeMap = productEliminateService.findProductTransportType();
		Map<String, String> positionMap = productEliminateService.findProductPositionByCountry(null);
		Map<String,String> attrMap = Maps.newHashMap();
		for(PsiProductAttribute attr:attrs){
			String value=DictUtils.getDictLabel(positionMap.get(attr.getColorName()), "product_position", "");
			if(transportTypeMap.get(attr.getColorName())!=null){
				if (transportTypeMap.get(attr.getColorName()).equals(3)) {
					value+="&海运/空运";
				} else if (transportTypeMap.get(attr.getColorName()).equals(1)) {
					value+="&海运";
				} else {
					value+="&空运";
				}
			}
			attrMap.put(attr.getColorName(), value);
		}
		
		model.addAttribute("attrMap", attrMap); 
        model.addAttribute("obs", obs);  
        model.addAttribute("week", week); 
        model.addAttribute("firstOnce", firstOnce); 
        model.addAttribute("moreOnce", moreOnce); 
		return "modules/psi/lc/lcPsiDeliveryProducts";
	}
	
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "add")
	public String add(LcPurchaseOrder purchaseOrder, Model model) {
		Date today = new Date();
		today.setHours(0);
		today.setSeconds(0);
		today.setMinutes(0);
		purchaseOrder.setPurchaseDate(today);
		//Map<Integer,Float> productPrices=this.productService.getAllPrice();
		List<PsiSupplier> suppliers=this.psiSupplierService.findAll();
		List<PsiProduct>  products = new ArrayList<PsiProduct>();
		String currencyType ="";
		model.addAttribute("suppliers", suppliers);
		List<User> users =Lists.newArrayList();
		for(User user:systemService.findAllUsers()){
			if("0".equals(user.getDelFlag())){
				users.add(user);
			}
		}
		Map<Integer, String> onSaleColorPlatformMap = productEliminateService.getOnSaleColorPlatform();
		Map<Integer, String> onSaleColor = productEliminateService.getOnSaleColor();
		Map<Integer, String> onSalePlatform = productEliminateService.getOnSalePlatform();
		//如果是选择了供应商,查出产品list
		if(purchaseOrder.getSupplier()==null){
			if(suppliers!=null&&suppliers.size()>0){
				purchaseOrder.setDeposit(suppliers.get(0).getDeposit());
				PsiSupplier supplier = suppliers.get(0);
				for(ProductSupplier proSup:supplier.getProducts()){
					PsiProduct product = proSup.getProduct();
					if(!"4".equals(product.getIsSale()) && "1".equals(product.getReviewSta())&&!"1".equals(product.getComponents())){
						PsiProduct productNew=new PsiProduct();
						productNew.setColorPlatform(onSaleColorPlatformMap.get(product.getId()));
						productNew.setColor(onSaleColor.get(product.getId()));
						productNew.setPlatform(onSalePlatform.get(product.getId()));
						productNew.setId(product.getId());
						productNew.setModel(product.getModel());
						productNew.setBrand(product.getBrand());
						productNew.setPackQuantity(product.getPackQuantity());
						products.add(productNew);
					}
					
				}
				
				currencyType=supplier.getCurrencyType();
			}
		}else{
			PsiSupplier  supplier = this.psiSupplierService.get(purchaseOrder.getSupplier().getId());
				purchaseOrder.setDeposit(supplier.getDeposit());
				for(ProductSupplier proSup:supplier.getProducts()){
					PsiProduct product = proSup.getProduct();
					if(!"4".equals(product.getIsSale()) && "1".equals(product.getReviewSta())&&!"1".equals(product.getComponents())){
						PsiProduct productNew=new PsiProduct();
						productNew.setColorPlatform(onSaleColorPlatformMap.get(product.getId()));
						productNew.setColor(onSaleColor.get(product.getId()));
						productNew.setPlatform(onSalePlatform.get(product.getId()));
						productNew.setId(product.getId());
						productNew.setModel(product.getModel());
						productNew.setBrand(product.getBrand());
						productNew.setPackQuantity(product.getPackQuantity());
						products.add(productNew);
					}
					
				}
			currencyType=supplier.getCurrencyType();			
		}
		purchaseOrder.setCurrencyType(currencyType);
		
		Map<Integer,String> receivedMap = this.productService.getAllReceivedDate(new Date());
		model.addAttribute("receivedMap", JSON.toJSON(receivedMap));
		model.addAttribute("purchaseOrder", purchaseOrder);
		model.addAttribute("products",products);
		model.addAttribute("users",users);
		//model.addAttribute("productPrices", JSON.toJSON(productPrices));
		return "modules/psi/lc/lcPurchaseOrderAdd";
	}
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "addSave")
	public String addSave(LcPurchaseOrder purchaseOrder, Model model,RedirectAttributes redirectAttributes) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer  sb = new StringBuffer("");
		List<LcPurchaseOrderItem> itemList = new ArrayList<LcPurchaseOrderItem>();
		Set<String>  productNames = Sets.newHashSet();
		Set<Integer>  productIds = Sets.newHashSet();
		String shortName  = "";
		BigDecimal  totalAmount =new BigDecimal(0);
		//根据供应商id查出供应商简称，生成订单号
		PsiSupplier  supplier = this.psiSupplierService.get(purchaseOrder.getSupplier().getId());
		if(supplier!=null){
			shortName =supplier.getNikename();
		}
		String orderNo = this.purchaseOrderService.createSequenceNumber(shortName+"_LC");
		purchaseOrder.setOrderNo(orderNo);
		
		//产品名称-产品线ID
		Map<String,String> nameAndLineIdMap=typeLineService.getLineByName();
		//产品线-国家-人id+","+name
		Map<String,Map<String,String>> saleUserMap=psiProductGroupUserService.getSingleGroupUser();
		
		//组成产品名+颜色   总数量，线下数量 Map
//		Map<String,String> orderProColorMap = Maps.newHashMap();
//		Map<Integer,Integer> productQuantityMap=Maps.newHashMap();
		//解析前台产品详细， 国家+颜色+产品id  key
		Map<String,LcPurchaseOrderItem> itemsMap = new HashMap<String,LcPurchaseOrderItem>();
		Map<Integer,BigDecimal> prductPriceMap = Maps.newHashMap();
		if(purchaseOrder.getItems()!=null){
			for(LcPurchaseOrderItem item :purchaseOrder.getItems()){
				String colorStr         = item.getColorCode();
				String countryStr       = item.getCountryCode();
				BigDecimal itemPrice         = item.getItemPrice();
				Integer quantityOrdered = item.getQuantityOrdered();
				Integer quantityOffOrdered = item.getQuantityOffOrdered();
				productNames.add(item.getProductName().substring(item.getProductName().indexOf(" ")));
				Integer productId  = item.getProduct().getId();
				productIds.add(productId);
				String []  colorArr = colorStr.split(",");
				String []  countryArr = countryStr.split(",");
				for(int i =0;i<colorStr.split(",").length;i++){
					for(int j= 0;j<countryStr.split(",").length;j++){
						String key = productId+"," +colorArr[i]+","+countryArr[j];
						if(itemPrice!=null&&prductPriceMap.get(productId)==null){
							prductPriceMap.put(productId, itemPrice);
						}
						if(itemsMap.containsKey(key)){
							//如果key存在，就把原来的数量加起来
							Integer curQuantity = itemsMap.get(key).getQuantityOrdered()+quantityOrdered;
							Integer curOffQuantity = itemsMap.get(key).getQuantityOffOrdered()+quantityOffOrdered;
							item.setQuantityOrdered(curQuantity);
							item.setQuantityOffOrdered(curOffQuantity);
							itemsMap.put(key, item);
						}else{
							itemsMap.put(key, item);
						}
					}
				}
			}
			
				purchaseOrder.setIsOverInventory("0");
			//添加价格结束
			
			LcPurchaseOrderItem  orderItem = null;
			String [] keyArr=null;
			//生成新品下单通知数据
			Set<String> unOrderProducts=this.productService.getNewProductAndNoOrder();
			
			for(Map.Entry<String, LcPurchaseOrderItem> entry :itemsMap.entrySet()){
				String key = entry.getKey();
				LcPurchaseOrderItem temp = entry.getValue();
				orderItem = new LcPurchaseOrderItem();
				keyArr=key.split(",");
				orderItem.setProduct(temp.getProduct());
				orderItem.setColorCode(keyArr[1]);
				orderItem.setCountryCode(keyArr[2]);
				orderItem.setQuantityOrdered(temp.getQuantityOrdered());
				orderItem.setQuantityPayment(0);       //已付款数量为0
				orderItem.setPaymentAmount(BigDecimal.ZERO);        //已支付金额    0
				orderItem.setQuantityPreReceived(0);   //预收货数量为0
				orderItem.setQuantityReceived(0);      //已收货数量为0
				//线下数量
				orderItem.setQuantityOffPreReceived(0);
				orderItem.setQuantityOffReceived(0);
				orderItem.setQuantityOffOrdered(temp.getQuantityOffOrdered());
				
				orderItem.setRemark(temp.getRemark());
				orderItem.setProductName(temp.getProductName());
				orderItem.setDeliveryDate(temp.getDeliveryDate());
				orderItem.setActualDeliveryDate(temp.getActualDeliveryDate());
				orderItem.setPurchaseOrder(purchaseOrder);  //
				orderItem.setItemPrice(prductPriceMap.get(orderItem.getProduct().getId()));
				if(unOrderProducts.contains(orderItem.getProductNameColorCountry())){
					if(StringUtils.isEmpty(sb)){
						sb.append("Hi,All<br/>&nbsp;&nbsp;&nbsp;&nbsp;新品首单产品明细如下：<br/><table width='90%' style='border-right:1px solid;border-bottom:1px solid;color:#666;' cellpadding='0' cellspacing='0' >");
						sb.append("<tr style='background-repeat:repeat-x;height:30px; background-color:#f2f4f6;color:#666;'>");
						sb.append("<th style='border-left:1px solid;border-top:1px solid;color:#666;'>产品名称</th>");
						sb.append("<th style='border-left:1px solid;border-top:1px solid;color:#666;'>国家</th>");
						sb.append("<th style='border-left:1px solid;border-top:1px solid;color:#666;'>数量</th>");
						sb.append("<th style='border-left:1px solid;border-top:1px solid;color:#666;'>预计交期</th>");
						sb.append("</tr>");
					}
					sb.append("<tr style='background-repeat:repeat-x;text-align:center;height:30px; border-left:1px solid;border-top:1px solid;color:#666; '>");
					String productUrl="<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/psiInventory/productInfoDetail?productName="+orderItem.getProductNameColor()+"'>"+orderItem.getProductNameColor()+"</a>";
					sb.append("<td style='border-left:1px solid;border-top:1px solid;color:#666;'>"+productUrl+"</td>");
					sb.append("<td style='border-left:1px solid;border-top:1px solid;color:#666;'>"+("com".equals(orderItem.getCountryCode())?"us":orderItem.getCountryCode())+"</td>");
					sb.append("<td style='border-left:1px solid;border-top:1px solid;color:#666;'>"+orderItem.getQuantityOrdered()+"</td>");
					sb.append("<td style='border-left:1px solid;border-top:1px solid;color:#666;'>"+(orderItem.getDeliveryDate()!=null?sdf.format(orderItem.getDeliveryDate()):"")+"</td>");
					sb.append("</tr>");
				}
				if(orderItem.getItemPrice()!=null){
					totalAmount=totalAmount.add(new BigDecimal(orderItem.getItemPrice()+"").multiply(new BigDecimal(orderItem.getQuantityOrdered())));
				}
				
				String name="";
				if(StringUtils.isNotEmpty(orderItem.getColorCode())){
					name=orderItem.getProductName()+"_"+orderItem.getColorCode();
				}else{
					name=orderItem.getProductName();
				}
				String lineId=nameAndLineIdMap.get(name);
				if(StringUtils.isNotBlank(lineId)&&saleUserMap!=null&&saleUserMap.get(lineId)!=null&&saleUserMap.get(lineId).get(orderItem.getCountryCode())!=null){
					orderItem.setSalesUser(saleUserMap.get(lineId).get(orderItem.getCountryCode()).split(",")[1]);
				}
				itemList.add(orderItem);
			}
			
			
			purchaseOrder.setCreateDate(new Date());
			purchaseOrder.setCreateUser(UserUtils.getUser());
			purchaseOrder.setUpdateDate(new Date());
			purchaseOrder.setUpdateUser(UserUtils.getUser());
			purchaseOrder.setTotalAmount(totalAmount);    //订单总金额
			purchaseOrder.setDepositAmount(BigDecimal.ZERO);           //已支付定金金额 0f
			purchaseOrder.setDepositPreAmount(BigDecimal.ZERO);        //已申请定金金额0f
			purchaseOrder.setOrderSta("0");  		      //草稿状态
			purchaseOrder.setDelFlag("0");                //删除状态
			purchaseOrder.setPaySta("0");                 //是否付款
			purchaseOrder.setPaymentAmount(BigDecimal.ZERO);           //支付尾款金额0f
			purchaseOrder.setItems(itemList);
		}
		
		this.purchaseOrderService.save(purchaseOrder);
		
		if("1".equals(purchaseOrder.getToReview())){
//			if("1".equals(purchaseOrder.getIsOverInventory())){
//				//查询角色为：采购超标审核员
//				String emailAddress="";  
//				for(Role role:UserUtils.getRoleList()){
//					if("采购审核员(超标)".equals(role.getName())){
//						for(User user:role.getUserList()){
//							emailAddress+=user.getEmail()+",";
//						}
//						break;    
//					}
//				}
//				
//				if(emailAddress.length()>0){
//					emailAddress=emailAddress.substring(0, emailAddress.length()-1);
//					String content = "(超标)采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/review?id="+purchaseOrder.getId()+"'>"+orderNo+"</a>已创建，请尽快登陆erp系统审批";
//					if(StringUtils.isNotBlank(content)){
//						Date date = new Date();
//						final MailInfo mailInfo = new MailInfo(emailAddress,"(超标)采购订单已创建待审批"+DateUtils.getDate("-yyyy/M/dd"),date);
//						mailInfo.setContent(content+",原因:"+purchaseOrder.getOverRemark());
//						//发送成功不成功都能保存
//						new Thread(){
//							@Override
//							public void run(){
//								poMaillManager.send(mailInfo);
//							}
//						}.start();
//					}
//				}
//			}else{
				//查询角色为：采购审核员
//				StringBuilder emailAddress=new StringBuilder();  
//				for(Role role:UserUtils.getRoleList()){
//					if("采购审核员".equals(role.getName())){
//						for(User user:role.getUserList()){
//							emailAddress.append(user.getEmail()).append(",");
//						}
//						break;    
//					}
//				}
				StringBuilder emailAddress= new StringBuilder();
				List<User> users=systemService.findUserByPermission("psi:order:review");
				for(User user:users){
					emailAddress.append(user.getEmail()).append(",");
				}
				if(emailAddress.length()>0){
					emailAddress=new StringBuilder(emailAddress.substring(0, emailAddress.length()-1));
				}
				//String toAddress="bella@inateck.com";
				String content = "采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/review?id="+purchaseOrder.getId()+"'>"+orderNo+"</a>已创建，请尽快登陆erp系统审批";
				if(StringUtils.isNotBlank(content)){
					Date date = new Date();
					final MailInfo mailInfo = new MailInfo(emailAddress.toString(),"采购订单已创建待审批"+DateUtils.getDate("-yyyy/M/dd"),date);
					mailInfo.setContent(content);
					//发送成功不成功都能保存
					new Thread(){
						@Override
						public void run(){
							poMaillManager.send(mailInfo);
						}
					}.start();
				}
//			}
			
		}
		
		//给产品经理发信
		String content = UserUtils.getUser().getName()+"新建订单：<b>"+purchaseOrder.getOrderNo()+"</b><br/>包含产品：<b>";
		StringBuilder productStr= new StringBuilder();
		for(String productName:productNames){
			productStr.append(productName).append(",");
		}
		String tempStr=productStr.substring(0,productStr.length()-1);
		content=content+tempStr+"</b><br/>请留意！";
		
		Set<String> mangerEmails=this.productService.findMangerByProductIds(productIds);
		Set<String> purchaseEmails =this.productService.findPurchaseByProductIds(productIds);
		
		Set<String> tempEmails = Sets.newHashSet();
		tempEmails.add("shawn@inateck.com");
		StringBuilder sendEmail =new StringBuilder();
		if(mangerEmails!=null&&mangerEmails.size()>0){
			tempEmails.addAll(mangerEmails);
		}
		
		if(purchaseEmails!=null&&purchaseEmails.size()>0){
			tempEmails.addAll(purchaseEmails);
		}
		
		for(String email:tempEmails){
			sendEmail.append(email).append(",");
		}
		
		sendEmail=new StringBuilder(sendEmail.substring(0,sendEmail.length()-1));
		
		Date date = new Date();
		final MailInfo mailInfo1 = new MailInfo(sendEmail.toString(),"新建采购订单：["+(tempStr.length()>100?tempStr.substring(0,100)+"...":tempStr)+"]"+DateUtils.getDate("-yyyy/M/dd"),date);
		mailInfo1.setContent(content);
		//发送成功不成功都能保存
		new Thread(){
			@Override
			public void run(){
				poMaillManager.send(mailInfo1);
			}
		}.start();
		
		//新品首单发信
		if(StringUtils.isNotEmpty(sb)){
			//销售、产品经理、市场推广、图片设计
			String email="maik@inateck.com,amazon-sales@inateck.com,pmg@inateck.com,marketing_dept@inateck.com,design@inateck.com";
			this.purchaseOrderService.sendNoticeEmail(email, sb.toString(), "新品首单提醒", "", "");
		}
		//发送延迟收货信件
//		this.purchaseOrderService.sendEmailDeliveryReceived(purchaseOrder,null);
		addMessage(redirectAttributes, "保存采购订单'" + purchaseOrder.getOrderNo() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
		
	}
	
	@RequiresPermissions("psi:order:edit")
	@RequestMapping(value = "edit")
	public String edit(LcPurchaseOrder purchaseOrder, Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<Integer,String> inMap= Maps.newHashMap(); 
		List<PsiProduct>  products=Lists.newArrayList();
		Map<Integer,BigDecimal> poPrice=Maps.newHashMap();
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		purchaseOrder.setOldDeposit(purchaseOrder.getDeposit());
		StringBuilder sb = new StringBuilder("");
		Map<Integer,Integer> productQuantityMap = Maps.newHashMap();
		for(LcPurchaseOrderItem item :purchaseOrder.getItems()){
			Integer quantity = item.getQuantityOrdered();
			if(productQuantityMap.get(item.getProduct().getId())!=null){
				quantity+=productQuantityMap.get(item.getProduct().getId());
			};
			productQuantityMap.put(item.getProduct().getId(), quantity);
		}
		Map<Integer, String> onSaleColorPlatformMap = productEliminateService.getOnSaleColorPlatform();
		Map<Integer, String> onSaleColor = productEliminateService.getOnSaleColor();
		Map<Integer, String> onSalePlatform = productEliminateService.getOnSalePlatform();
		
		Integer tax=psiSupplierService.get(purchaseOrder.getSupplier().getId()).getTaxRate();
		Float  taxRate= (tax+100)/100f;
		//获得产品价格
		//获取产品价格 start
		 Map<String,PsiProductTieredPriceDto> dtoMap = productTieredPriceService.findPrices(productQuantityMap.keySet(), purchaseOrder.getSupplier().getId(), purchaseOrder.getCurrencyType());
		Map<String,Float> productPrices = Maps.newHashMap();
		for(Map.Entry<String, PsiProductTieredPriceDto> entry:dtoMap.entrySet()){
			String key = entry.getKey();
			String [] arr=key.split(",");
			String productIdStr=arr[0];
			String color =arr[1];
			PsiProductTieredPriceDto dto = entry.getValue();
			Integer orderQuantity = productQuantityMap.get(Integer.parseInt(productIdStr));
			Float price = null;
			String proColorKey = productIdStr+"_"+color;
			if(productPrices.get(proColorKey)==null){
				if("USD".equals(purchaseOrder.getCurrencyType())){
					if(orderQuantity<1000){ 		//小于1000用500的价
						price=dto.getLeval500usd(); 
					}else if(orderQuantity<2000){   //小于2000用1000的价
						price=dto.getLeval1000usd();
					}else if(orderQuantity<3000){   //小于3000用2000的价
						price=dto.getLeval2000usd();
					}else if(orderQuantity<5000){   //小于5000用3000的价
						price=dto.getLeval3000usd();
					}else if(orderQuantity<10000){  //小于10000用5000的价
						price=dto.getLeval5000usd();
					}else if(orderQuantity<15000){  //小于15000用10000的价
						price=dto.getLeval10000usd();
					}else{                          //大于15000用15000的价
						price=dto.getLeval15000usd();
					}
				}else{
					if(orderQuantity<1000){ 		//小于1000用500的价
						price=dto.getLeval500cny(); 
					}else if(orderQuantity<2000){   //小于2000用1000的价
						price=dto.getLeval1000cny();
					}else if(orderQuantity<3000){   //小于3000用2000的价
						price=dto.getLeval2000cny();
					}else if(orderQuantity<5000){   //小于5000用3000的价
						price=dto.getLeval3000cny();
					}else if(orderQuantity<10000){  //小于10000用5000的价
						price=dto.getLeval5000cny();
					}else if(orderQuantity<15000){  //小于15000用10000的价
						price=dto.getLeval10000cny();
					}else{                          //大于15000用15000的价
						price=dto.getLeval15000cny();
					}
				}
				if(price!=null){
					productPrices.put(proColorKey, new BigDecimal(price*taxRate+"").setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				}else{
					productPrices.put(proColorKey, price);
				}
				
			}
		}
		//获取该产品的颜色和国家  组成list，供页面选择
		for(LcPurchaseOrderItem item :purchaseOrder.getItems()){
			PsiProduct product = item.getProduct();
			if(inMap.get(product.getId())==null){
				inMap.put(product.getId(), sdf.format(item.getDeliveryDate()));
			}
			if(!purchaseOrder.getOrderSta().equals("0")&&!purchaseOrder.getOrderSta().equals("1")&&!"1".equals(product.getComponents())){
				
				if(poPrice.get(product.getId())==null){
					poPrice.put(product.getId(), item.getItemPrice());
					PsiProduct productNew=new PsiProduct();
					productNew.setColorPlatform(onSaleColorPlatformMap.get(product.getId()));
					productNew.setColor(onSaleColor.get(product.getId()));
					productNew.setPlatform(onSalePlatform.get(product.getId()));
					productNew.setId(product.getId());
					productNew.setModel(product.getModel());
					productNew.setBrand(product.getBrand());
					productNew.setPackQuantity(product.getPackQuantity());
					products.add(productNew);
				}
			}
			sb.append(item.getId()+",");
		}
		String itemIds="";
		if(StringUtils.isNotEmpty(sb.toString())){
			itemIds=sb.toString().substring(0,sb.toString().length()-1);
		}
		purchaseOrder.setOldItemIds(itemIds);
		List<User> users =Lists.newArrayList();
		for(User user:systemService.findAllUsers()){
			if("0".equals(user.getDelFlag())){
				users.add(user);
			}
		}
		model.addAttribute("users", users);
		model.addAttribute("purchaseOrder", purchaseOrder);
		//交期map
		Map<Integer,String> receivedMap = this.productService.getAllReceivedDate(new Date());
		
		for(Map.Entry<Integer,String> entry:receivedMap.entrySet()){
			Integer productId = entry.getKey();
			if(inMap.get(productId)!=null){
				receivedMap.put(productId, inMap.get(productId));
			}
		}
		
		model.addAttribute("receivedMap", JSON.toJSON(receivedMap));
		//查询产品价格
		if(purchaseOrder.getOrderSta().equals("0")||purchaseOrder.getOrderSta().equals("1")){
			PsiSupplier supplier = this.psiSupplierService.get(purchaseOrder.getSupplier().getId());
			for(ProductSupplier proSup:supplier.getProducts()){
				PsiProduct product = proSup.getProduct();
				if(!"4".equals(product.getIsSale())&&!"1".equals(product.getComponents())){
					PsiProduct productNew=new PsiProduct();
					productNew.setColorPlatform(onSaleColorPlatformMap.get(product.getId()));
					productNew.setColor(onSaleColor.get(product.getId()));
					productNew.setPlatform(onSalePlatform.get(product.getId()));
					productNew.setId(product.getId());
					productNew.setModel(product.getModel());
					productNew.setBrand(product.getBrand());
					productNew.setPackQuantity(product.getPackQuantity());
					products.add(productNew);
				}
			}
			model.addAttribute("products", products);
			model.addAttribute("productPrices", productPrices);
			List<Float> moldFeeList = psiProductMoldFeeService.findForReturn(purchaseOrder.getSupplier().getId());
			if (moldFeeList != null && moldFeeList.size() == 2 && moldFeeList.get(0) > 0) {	//有模具费记录
				//统计供应商已经返还的模具费(负数)
				Map<String, Float> moldFee = purchaseOrderService.getReturnedMoldFee(purchaseOrder.getSupplier().getId());
				//统计数据在页面上提示
				String tips = "总模具费用：" + moldFeeList.get(0);
				tips += ",已计入订单PDF总金额：" + moldFee.get("0");
				if ((moldFeeList.get(1) + moldFee.get("1"))>0) {
					tips += ",当前可返还金额：" + (moldFeeList.get(1) + moldFee.get("1"));
				} else {
					tips += ",当前可返还金额：0";
				}
				model.addAttribute("tips", tips);
			}
			return "modules/psi/lc/lcPurchaseOrderEdit";
		}else{
			model.addAttribute("poPrice", JSON.toJSON(poPrice));
			model.addAttribute("products", products);
			return "modules/psi/lc/lcPurchaseOrderEditOfficial";
		}
		
	}
	
	
	
	@RequiresPermissions("psi:order:edit")
	@RequestMapping(value = "editDeliveryDate")
	public String editDeliveryDate(LcPurchaseOrder purchaseOrder, Model model) {
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		model.addAttribute("purchaseOrder", purchaseOrder);
		return "modules/psi/lc/lcPurchaseOrderDeliveryDate";
	}
	   
	@RequiresPermissions("psi:order:edit")
	@RequestMapping(value = "fastCreateOrder")
	public String fastCreateOrder(LcPurchaseOrder purchaseOrder, Model model) {
		List<PsiProduct>  products = new ArrayList<PsiProduct>();
		Integer productId=0;
		Date today = new Date();
		today.setHours(0);
		today.setSeconds(0);
		today.setMinutes(0);
		purchaseOrder.setPurchaseDate(today);
		
		if(purchaseOrder.getItems()==null||purchaseOrder.getItems().size()==0){
			return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/add?repage";
		}
		for(LcPurchaseOrderItem item :purchaseOrder.getItems()){
			if(productId.equals(0)){
				productId=item.getProduct().getId();
				break;
			}
		}  
		
		PsiProduct product =this.productService.get(productId);
		
		Integer perido =product.getProducePeriod();
		for(LcPurchaseOrderItem item :purchaseOrder.getItems()){
			item.setProduct(product);
			item.setDeliveryDate(DateUtils.addDays(new Date(), perido));
			item.setActualDeliveryDate(item.getDeliveryDate());
		} 
		
		List<User> users =Lists.newArrayList();
		for(User user:systemService.findAllUsers()){
			if("0".equals(user.getDelFlag())){
				users.add(user);
			}
		}
		//交期map
		Map<Integer,String> receivedMap = this.productService.getAllReceivedDate(new Date());
		
		//多个供应商的话，允许切换
		List<ProductSupplier> proSupplier =product.getPsiSuppliers();
		List<PsiSupplier> suppliers=Lists.newArrayList();
		for(ProductSupplier pSupplier :proSupplier){
			suppliers.add(pSupplier.getSupplier());
		}
		
		Integer supplierId=0;
		if(purchaseOrder.getSupplier()!=null&&purchaseOrder.getSupplier().getId()!=null){
			supplierId=purchaseOrder.getSupplier().getId();
		}else{
			supplierId=suppliers.get(0).getId();
		}
		PsiSupplier  supplier = this.psiSupplierService.get(supplierId);
		purchaseOrder.setDeposit(supplier.getDeposit());
		purchaseOrder.setOldItemIds(productId+"");
		for(ProductSupplier proSup:supplier.getProducts()){
			if("1".equals(proSup.getProduct().getIsSale())){
				products.add(proSup.getProduct());
			}
		}
		
		purchaseOrder.setSupplier(supplier);
		purchaseOrder.setCurrencyType(supplier.getCurrencyType());			
		model.addAttribute("products", products);
		model.addAttribute("suppliers", suppliers);
		model.addAttribute("users", users);
		model.addAttribute("purchaseOrder", purchaseOrder);
		model.addAttribute("receivedMap", JSON.toJSON(receivedMap));
		
		return "modules/psi/lc/lcPurchaseOrderFast";    
	}
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "fastSave")
	public String fastSave(LcPurchaseOrder purchaseOrder, Model model,RedirectAttributes redirectAttributes) throws ParseException {
		String shortName  = "";
		BigDecimal  totalAmount =BigDecimal.ZERO;
		Set<String>  productNames = Sets.newHashSet();
		//根据供应商id查出供应商简称，生成订单号
		PsiSupplier  supplier = this.psiSupplierService.get(purchaseOrder.getSupplier().getId());
		if(supplier!=null){
			shortName =supplier.getNikename();
		}
		String orderNo = this.purchaseOrderService.createSequenceNumber(shortName+"_LC");
		purchaseOrder.setOrderNo(orderNo);
		
		//组成产品名+颜色   数量Map
//		Map<String,Integer> orderProColorMap = Maps.newHashMap();
		if(purchaseOrder.getItems()!=null){
			for(LcPurchaseOrderItem item :purchaseOrder.getItems()){
				item.setPurchaseOrder(purchaseOrder);
				item.setQuantityPreReceived(0);      //预接收数量0
				item.setQuantityReceived(0);         //已接收数量0
				//线下订单
				item.setQuantityOffPreReceived(0);   //线下预接收数量0
				item.setQuantityOffReceived(0);      //线下已接收数量0
				item.setQuantityPayment(0);          //已付款数量0
				item.setPaymentAmount(BigDecimal.ZERO);           //已支付金额
				item.setDelFlag("0");
				productNames.add(item.getProductName().substring(item.getProductName().indexOf(" ")));
				//star 20151214
//				String proColor = item.getProductName();
//				if(StringUtils.isNotEmpty(item.getColorCode())){
//					proColor=proColor+"_"+item.getColorCode();
//				}
//				
//				Integer orderQuantity = item.getQuantityOrdered();
//				if(orderProColorMap.get(proColor)!=null){
//					orderQuantity+=orderProColorMap.get(proColor);
//				}
//				orderProColorMap.put(proColor, orderQuantity);
				//end 20151214
			}
			purchaseOrder.setCreateDate(new Date());
			purchaseOrder.setCreateUser(UserUtils.getUser());
			purchaseOrder.setUpdateDate(new Date());
			purchaseOrder.setUpdateUser(UserUtils.getUser());
			purchaseOrder.setTotalAmount(totalAmount);    //订单总金额
			purchaseOrder.setDepositAmount(BigDecimal.ZERO);           //已支付定金金额 0f
			purchaseOrder.setDepositPreAmount(BigDecimal.ZERO);        //已申请定金金额0f
			purchaseOrder.setOrderSta("0");  		      //草稿状态
			purchaseOrder.setDelFlag("0");                //删除状态
			purchaseOrder.setPaySta("0");                 //是否付款
			purchaseOrder.setPaymentAmount(BigDecimal.ZERO);           //支付尾款金额0f
		}else{
			return null;
		}
		
		//star 20151214
//		String isOverFlag="0";
//		String remark="";
//		Map<String,Integer> maxMap = this.purchaseOrderService.getMaxInventory();
//		Map<String,Integer> canSaleMap = this.purchaseOrderService.getCanSaleMap();
//		for(String proColor:orderProColorMap.keySet()){
//			if(maxMap.get(proColor)!=null){
//				Integer canSaleQuantity=0;
//				if(canSaleMap.get(proColor)!=null){
//					canSaleQuantity=canSaleMap.get(proColor);
//				}
//				if(maxMap.get(proColor)-canSaleQuantity-orderProColorMap.get(proColor)<0){
//					isOverFlag="1";
//					remark+="产品:"+proColor+",最大允许库存数:"+maxMap.get(proColor)+",下单后库存数为:"+(canSaleQuantity+orderProColorMap.get(proColor)+";");
//				}
//			}
//		}
//		if("1".equals(isOverFlag)){
//			purchaseOrder.setIsOverInventory(isOverFlag);
//			purchaseOrder.setOverRemark(remark);
//		}else{
			purchaseOrder.setIsOverInventory("0");
//		}
		//end 20151214
		
		this.purchaseOrderService.save(purchaseOrder);
		if("1".equals(purchaseOrder.getToReview())){
//			if("1".equals(purchaseOrder.getIsOverInventory())){
//				//查询角色为：采购超标审核员
//				String emailAddress="";  
//				for(Role role:UserUtils.getRoleList()){
//					if("采购审核员(超标)".equals(role.getName())){
//						for(User user:role.getUserList()){
//							emailAddress+=user.getEmail()+",";
//						}
//						break;    
//					}
//				}
//				
//				if(emailAddress.length()>0){
//					emailAddress=emailAddress.substring(0, emailAddress.length()-1);
//					String content = "(超标)采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/review?id="+purchaseOrder.getId()+"'>"+orderNo+"</a>已创建，请尽快登陆erp系统审批";
//					if(StringUtils.isNotBlank(content)){
//						Date date = new Date();
//						final MailInfo mailInfo = new MailInfo(emailAddress,"(超标)采购订单已创建待审批"+DateUtils.getDate("-yyyy/M/dd"),date);
//						mailInfo.setContent(content+",原因:"+purchaseOrder.getOverRemark());
//						//发送成功不成功都能保存
//						new Thread(){
//							@Override
//							public void run(){
//								poMaillManager.send(mailInfo);
//							}
//						}.start();
//					}
//				}
//			}else{
				//查询角色为：采购审核员
//				for(Role role:UserUtils.getRoleList()){
//					if("采购审核员".equals(role.getName())){
//						for(User user:role.getUserList()){
//							emailAddress.append(user.getEmail()).append(",");
//						}   
//						break;
//					}
//				}
				StringBuilder emailAddress= new StringBuilder();
				List<User> users=systemService.findUserByPermission("psi:order:review");
				for(User user:users){
					emailAddress.append(user.getEmail()).append(",");
				}
				if(emailAddress.length()>0){
					emailAddress=new StringBuilder(emailAddress.substring(0, emailAddress.length()-1));
				}
				String content = "采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/review?id="+purchaseOrder.getId()+"'>"+orderNo+"</a>已创建，请尽快登陆erp系统审批";
				if(StringUtils.isNotBlank(content)){
					Date date = new Date();
					final MailInfo mailInfo = new MailInfo(emailAddress.toString(),"采购订单已创建待审批"+DateUtils.getDate("-yyyy/M/dd"),date);
					mailInfo.setContent(content);
					//发送成功不成功都能保存
					new Thread(){
						@Override
						public void run(){
							poMaillManager.send(mailInfo);
						}
					}.start();
				}
//			}
		}
		
		//给产品经理发信
		String content = UserUtils.getUser().getName()+"新建订单：<b>"+purchaseOrder.getOrderNo()+"</b><br/>包含产品：<b>";
		StringBuilder productStr=new StringBuilder();
		for(String productName:productNames){
			productStr.append(productName).append(",");
		}
		String tempStr=productStr.substring(0,productStr.length()-1);
		content=content+tempStr+"</b><br/>请留意！";
		String sendEmail ="shawn@inateck.com";
		Date date = new Date();
		final MailInfo mailInfo1 = new MailInfo(sendEmail,"新建采购订单：["+(tempStr.length()>100?tempStr.substring(0,100)+"...":tempStr)+"]"+DateUtils.getDate("-yyyy/M/dd"),date);
		mailInfo1.setContent(content);
		//发送成功不成功都能保存
		new Thread(){
			@Override
			public void run(){
				poMaillManager.send(mailInfo1);
			}
		}.start();
		//发送延迟收货信件
//		this.purchaseOrderService.sendEmailDeliveryReceived(purchaseOrder,null);
		addMessage(redirectAttributes, "保存采购订单'" + purchaseOrder.getOrderNo() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
		
	}
	

	@RequiresPermissions("psi:order:edit")
	@RequestMapping(value = "editSave")
	public String editSave(LcPurchaseOrder purchaseOrder, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if(purchaseOrder.getItems()==null||purchaseOrder.getItems().size()==0){
			throw new RuntimeException("提交失败，请不要用tab页提交！！！");
		}
		//产品名称-产品线ID
		Map<String,String> nameAndLineIdMap=typeLineService.getLineByName();
		//产品线-国家-人id+","+name
		Map<String,Map<String,String>> saleUserMap=psiProductGroupUserService.getSingleGroupUser();
		for(LcPurchaseOrderItem item:purchaseOrder.getItems()){
			String name=item.getProductNameColor();
			String lineId=nameAndLineIdMap.get(name);
			if(StringUtils.isNotBlank(lineId)&&saleUserMap!=null&&saleUserMap.get(lineId)!=null&&saleUserMap.get(lineId).get(item.getCountryCode())!=null){
				item.setSalesUser(saleUserMap.get(lineId).get(item.getCountryCode()).split(",")[1]);
			}
		}
		this.purchaseOrderService.editSave(purchaseOrder);
		addMessage(redirectAttributes, "编辑采购订单'" + purchaseOrder.getOrderNo() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	
	//拆单
	@RequestMapping(value = "splitEditSave")
	public String splitEditSave(LcPurchaseOrder purchaseOrder, RedirectAttributes redirectAttributes) throws Exception {
		String rs = purchaseOrderService.splitSaveData(purchaseOrder);
		if ("0".equals(rs)) {
			addMessage(redirectAttributes, "拆分采购订单'" + purchaseOrder.getOrderNo() + "'失败,拆单数为0");
		} else {
			addMessage(redirectAttributes, "拆分采购订单'" + purchaseOrder.getOrderNo() + "'成功,拆分的运单号为:"+rs);
		}
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	@RequiresPermissions("psi:order:edit")
	@RequestMapping(value = "deliveryDateSave")
	public String deliveryDateSave(LcPurchaseOrder purchaseOrder, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if(purchaseOrder.getItems()==null||purchaseOrder.getItems().size()==0){
			throw new RuntimeException("提交失败，请不要用tab页提交！！！");
		}
		this.purchaseOrderService.deliveryDateSave(purchaseOrder);
		addMessage(redirectAttributes, "编辑采购订单[" + purchaseOrder.getId() + "]预计交期成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
		
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value = {"updateDeliveryDate"})
	public String updateDeliveryDate(Integer purchaseOrderId,Integer itemId,Integer deliveryDateId,Integer quantity,Integer quantityReceived,Integer quantityOff,Integer quantityOffReceived,Date deliveryDate,String delIds,String remark) throws UnsupportedEncodingException, ParseException {
		this.purchaseOrderService.updateDeliveryDate(purchaseOrderId,itemId,deliveryDateId,quantity,quantityReceived,quantityOff,quantityOffReceived,deliveryDate,delIds,URLDecoder.decode(remark,"UTF-8"));
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = {"deleteDeliveryDate"})
	public String deleteDeliveryDate(String delIds){
		this.purchaseOrderService.deleteDeliveryDate(delIds);
		return "";
	}
	
	
	@RequiresPermissions("psi:order:edit")
	@RequestMapping(value = "appRevise")
	public String appRevise(LcPurchaseOrder purchaseOrder, Model model, RedirectAttributes redirectAttributes) throws Exception {
		purchaseOrder = this.purchaseOrderService.get(purchaseOrder.getId());
		if("0".equals(purchaseOrder.getToReview())){
			//查询角色为：采购审核员
			if("1".equals(purchaseOrder.getIsOverInventory())){
				//查询角色为：采购超标审核员
//				StringBuilder emailAddress=new StringBuilder();  
//				for(Role role:UserUtils.getRoleList()){
//					if("采购审核员(超标)".equals(role.getName())){
//						for(User user:role.getUserList()){
//							emailAddress.append(user.getEmail()).append(",");
//						}
//						break;    
//					}
//				}
				StringBuilder emailAddress= new StringBuilder();
				List<User> users=systemService.findUserByPermission("psi:order:overReview");
				for(User user:users){
					emailAddress.append(user.getEmail()).append(",");
				}
				if(emailAddress.length()>0){
					emailAddress=new StringBuilder(emailAddress.substring(0, emailAddress.length()-1));
					String content = "(超标)采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/review?id="+purchaseOrder.getId()+"'>"+purchaseOrder.getOrderNo()+"</a>已创建，请尽快登陆erp系统审批";
					if(StringUtils.isNotBlank(content)){
						Date date = new Date();
						final MailInfo mailInfo = new MailInfo(emailAddress.toString(),"(超标)采购订单已创建待审批"+DateUtils.getDate("-yyyy/M/dd"),date);
						mailInfo.setContent(content+",原因:"+purchaseOrder.getOverRemark());
						//发送成功不成功都能保存
						new Thread(){
							@Override
							public void run(){
								poMaillManager.send(mailInfo);
							}
						}.start();
					}
				}
			}else{
//				StringBuilder emailAddress= new StringBuilder();   
//				for(Role role:UserUtils.getRoleList()){
//					if("采购审核员".equals(role.getName())){
//						for(User user:role.getUserList()){
//							emailAddress.append(user.getEmail()).append(",");
//						}
//						break;
//					}
//				}
				StringBuilder emailAddress= new StringBuilder();
				List<User> users=systemService.findUserByPermission("psi:order:review");
				for(User user:users){
					emailAddress.append(user.getEmail()).append(",");
				}
				if(emailAddress.length()>0){
					emailAddress=new StringBuilder(emailAddress.substring(0, emailAddress.length()-1));
				}
				String content = "采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/review?id="+purchaseOrder.getId()+"'>"+purchaseOrder.getOrderNo()+"</a>已创建，请尽快登陆erp系统审批";
				if(StringUtils.isNotBlank(content)){
					Date date = new Date();
					final MailInfo mailInfo = new MailInfo(emailAddress.toString(),"采购订单已创建待审批"+DateUtils.getDate("-yyyy/M/dd"),date);
					mailInfo.setContent(content);
					//发送成功不成功都能保存
					new Thread(){
						@Override
						public void run(){
							poMaillManager.send(mailInfo);
						}
					}.start();
				}
			}
			purchaseOrder.setToReview("1");
			this.purchaseOrderService.save(purchaseOrder);
		}
		addMessage(redirectAttributes, "申请审核采购订单:'" + purchaseOrder.getOrderNo() + "'邮件已发送成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	
	@RequiresPermissions("psi:order:confirm")
	@RequestMapping(value = "sure")
	public String sure(LcPurchaseOrder purchaseOrder, Model model) {
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		Integer supplierId=purchaseOrder.getSupplier().getId();
		Map<Integer,Integer> productQuantityMap = Maps.newHashMap();//不分颜色，产品的总数量
		for(LcPurchaseOrderItem item:purchaseOrder.getItems()){
			Integer productId=item.getProduct().getId();
			Integer productQuantity = item.getQuantityOrdered();
			if(productQuantityMap.get(productId)!=null){
				productQuantity+=productQuantityMap.get(productId);
			}
			productQuantityMap.put(productId, productQuantity);
		}
		Map<String,PsiProductTieredPriceDto> dtoMap = productTieredPriceService.findPrices(productQuantityMap.keySet(), supplierId, purchaseOrder.getCurrencyType());
		Map<String,Float> productPrices=Maps.newHashMap();
		
		Integer tax=psiSupplierService.get(purchaseOrder.getSupplier().getId()).getTaxRate();
		Float  taxRate= (tax+100)/100f;
		
		for(Map.Entry<String,PsiProductTieredPriceDto>entry:dtoMap.entrySet()){
			String key = entry.getKey();
			String [] arr=key.split(",");
			String productIdStr=arr[0];
			String color =arr[1];
			PsiProductTieredPriceDto dto = entry.getValue();
			Integer orderQuantity = productQuantityMap.get(Integer.parseInt(productIdStr));
			Float price = null;
			String proColorKey = productIdStr+"_"+color;
			if(productPrices.get(proColorKey)==null){
				if("USD".equals(purchaseOrder.getCurrencyType())){
					if(orderQuantity<1000){ 		//小于1000用500的价
						price=dto.getLeval500usd(); 
					}else if(orderQuantity<2000){   //小于2000用1000的价
						price=dto.getLeval1000usd();
					}else if(orderQuantity<3000){   //小于3000用2000的价
						price=dto.getLeval2000usd();
					}else if(orderQuantity<5000){   //小于5000用3000的价
						price=dto.getLeval3000usd();
					}else if(orderQuantity<10000){  //小于10000用5000的价
						price=dto.getLeval5000usd();
					}else if(orderQuantity<15000){  //小于15000用10000的价
						price=dto.getLeval10000usd();
					}else{                          //大于15000用15000的价
						price=dto.getLeval15000usd();
					}
				}else{
					if(orderQuantity<1000){ 		//小于1000用500的价
						price=dto.getLeval500cny(); 
					}else if(orderQuantity<2000){   //小于2000用1000的价
						price=dto.getLeval1000cny();
					}else if(orderQuantity<3000){   //小于3000用2000的价
						price=dto.getLeval2000cny();
					}else if(orderQuantity<5000){   //小于5000用3000的价
						price=dto.getLeval3000cny();
					}else if(orderQuantity<10000){  //小于10000用5000的价
						price=dto.getLeval5000cny();
					}else if(orderQuantity<15000){  //小于15000用10000的价
						price=dto.getLeval10000cny();
					}else{                          //大于15000用15000的价
						price=dto.getLeval15000cny();
					}
				}
				if(price!=null){
					productPrices.put(proColorKey, price*taxRate);
				}
				
			}
		}
		//查询产品价格
		model.addAttribute("productPrices", productPrices);
		model.addAttribute("purchaseOrder", purchaseOrder);
		return "modules/psi/lc/lcPurchaseOrderSure";
	}
	
	
	@RequestMapping(value = "sureReview")
	public String sureReview(LcPurchaseOrder purchaseOrder, Model model) {
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		Set<Integer> productIds = purchaseOrder.getProductIds();
		Map<Integer,String> productPriceMap = Maps.newHashMap();
		for(Integer proId:productIds){
			productPriceMap.put(proId, this.purchaseOrderService.getLastTwicePrice(proId));
		}
		model.addAttribute("productPriceMap", productPriceMap);
		model.addAttribute("purchaseOrder", purchaseOrder);
		return "modules/psi/lc/lcPurchaseOrderSureReview";
	}
	
	@RequestMapping(value = "sureReviewSave")
	public String sureReviewSave(LcPurchaseOrder purchaseOrder,RedirectAttributes redirectAttributes) throws IOException {
		this.purchaseOrderService.sureReviewSave(purchaseOrder, redirectAttributes);
		addMessage(redirectAttributes, "确认订单'" + purchaseOrder.getOrderNo() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	@RequiresPermissions("psi:order:confirm")
	@RequestMapping(value = "sureSave")
	public String sureSave(MultipartFile piFile,LcPurchaseOrder purchaseOrder,RedirectAttributes redirectAttributes) throws IOException {
		this.purchaseOrderService.sureSave(piFile, purchaseOrder, filePath, redirectAttributes);
		String subject="采购订单已录入PI及价格，请及时核对价格，确认下单！";
		String content = "Hi,<br/>采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/sureReview?id="+purchaseOrder.getId()+"'>"+purchaseOrder.getOrderNo()+"</a>已录入PI及价格，请及时核对PI生成订单";
		List<User> userList = systemService.findUserByPermission("psi:order:financeReview");//财务审核
		List<User> replys = Lists.newArrayList();
		if(userList!=null){
			replys.addAll(userList);
		}
		replys.add(UserUtils.getUser());
		String email = Collections3.extractToString(replys,"email", ",")+","+UserUtils.getUser().getEmail();
		this.sendNoticeEmail(email, content, subject);   
		addMessage(redirectAttributes, "确认订单'" + purchaseOrder.getOrderNo() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	@RequiresPermissions(value={"psi:order:review","psi:order:overReview"},logical=Logical.OR)
	@RequestMapping(value = "review")
	public String review(LcPurchaseOrder purchaseOrder, Model model) {
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		model.addAttribute("purchaseOrder", purchaseOrder);
		if("0".equals(purchaseOrder.getOrderSta())){
			return "modules/psi/lc/lcPurchaseOrderReview";
		}else{
			return "modules/psi/lc/lcPurchaseOrderView";
		}
	}
	
	
	@RequestMapping(value = "overReview")
	public String overReview(LcPurchaseOrder purchaseOrder, Model model) {
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		model.addAttribute("purchaseOrder", purchaseOrder);
		if("0".equals(purchaseOrder.getOrderSta())){
			return "modules/psi/lc/lcPurchaseOrderOverReview";
		}else{
			return "modules/psi/lc/lcPurchaseOrderView";
		}
	}
	
	
	@RequiresPermissions("psi:order:review")
	@RequestMapping(value = "toDraft")
	public String toDraft(LcPurchaseOrder purchaseOrder,RedirectAttributes redirectAttributes) {
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		purchaseOrder.setOrderSta("0");
		this.purchaseOrderService.save(purchaseOrder);
		addMessage(redirectAttributes, "订单'" + purchaseOrder.getOrderNo() + "'变成草稿成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	
	
	@RequiresPermissions("psi:order:review")
	@RequestMapping(value = "reviewSave")
	public String reviewSave(LcPurchaseOrder purchaseOrder,RedirectAttributes redirectAttributes) throws Exception {
		purchaseOrder=this.purchaseOrderService.get(purchaseOrder.getId());
		if(!"0".equals(purchaseOrder.getOrderSta())){
			addMessage(redirectAttributes, "订单已经审核、不能重复审核！！");
			return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
		}
		purchaseOrderService.updateReviewStaEmail(purchaseOrder.getId(), "1", "1");
		//给供应商发邮件、
		this.sendEmailToSupplier(purchaseOrder, purchaseOrder.getMerchandiser());
		
		String subject="采购订单已审核，请及时PI确认";
		String content = "Hi,<br/>采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/sure?id="+purchaseOrder.getId()+"'>"+purchaseOrder.getOrderNo()+"</a>已审核，请及时录入价格核对PI";
		String email="sophie@inateck.com";//+","+UserUtils.getUser().getEmail();
		this.sendNoticeEmail(email, content, subject);   
		addMessage(redirectAttributes, "审核订单'" + purchaseOrder.getOrderNo() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	
	@RequestMapping(value = "reviewOverSave")
	public String reviewOverSave(LcPurchaseOrder purchaseOrder,RedirectAttributes redirectAttributes) throws Exception {
		String remark =purchaseOrder.getOverRemark();
		purchaseOrder=this.purchaseOrderService.get(purchaseOrder.getId());
		if(!"0".equals(purchaseOrder.getOrderSta())){
			addMessage(redirectAttributes, "订单已经审核、不能重复审核！！");
			return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
		}
		purchaseOrderService.updateReviewStaEmailRemark(purchaseOrder.getId(), "1", "1",remark);
		//给供应商发邮件、
		this.sendEmailToSupplier(purchaseOrder, purchaseOrder.getMerchandiser());
		String subject="采购订单已审核，请及时PI确认";
		String content = "Hi,<br/>采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/sure?id="+purchaseOrder.getId()+"'>"+purchaseOrder.getOrderNo()+"</a>已审核，请及时录入价格核对PI";
		String email="sophie@inateck.com,"+UserUtils.getUser().getEmail();
		this.sendNoticeEmail(email, content, subject);   
		
		addMessage(redirectAttributes, "审核订单'" + purchaseOrder.getOrderNo() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "view")
	public String view(LcPurchaseOrder purchaseOrder, Model model) {
		if(purchaseOrder.getId()!=null){
			purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		}else if(purchaseOrder.getOrderNo()!=null){
			purchaseOrder = purchaseOrderService.get(purchaseOrder.getOrderNo());
		}else{
			return null;
		}
		
		if("1".equals(purchaseOrder.getToPartsOrder())){
			//迭代item获得，产品颜色有无配件
			Set<String> proColors = Sets.newHashSet();
			for(LcPurchaseOrderItem item:purchaseOrder.getItems()){
				proColors.add(item.getProduct().getId()+","+item.getColorCode());
			}
			//存在配件
			Set<String> proColorNews= productPartsService.getPartsColors(proColors);
			Map<String,Integer> canLadingMap= partsOutService.getCanLadingQuantity(purchaseOrder.getId());
			List<PsiPartsInventoryOut> partsOutList=partsOutService.findByOrderId(purchaseOrder.getId());
			for(String key:proColorNews){
				if(canLadingMap.get(key)==null){
					//有配件没配送
					canLadingMap.put(key, 0);
				}
			}
			model.addAttribute("canLadingMap", canLadingMap);
			model.addAttribute("partsOutList", partsOutList);
		}
		//查询版本号
		List<Object[]> versions =hisPurchaseOrderService.getOrderVersion(purchaseOrder.getOrderNo());
		model.addAttribute("versions", versions);
		model.addAttribute("purchaseOrder", purchaseOrder);
		return "modules/psi/lc/lcPurchaseOrderView";
	}
	
	@ResponseBody
	@RequestMapping(value = {"updateSingelDeliveryDate"})
	public String updateSingelDeliveryDate(Integer orderItemId,String deliveryDate) {
		return this.purchaseOrderService.updateDeliveryDate(orderItemId, new Date(deliveryDate));
	}
	
	@RequiresPermissions("psi:all:view")
	@ResponseBody
	@RequestMapping(value = "printPdf")
	public String printPdf(LcPurchaseOrder purchaseOrder,HttpServletResponse response) throws Exception {
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		for (LcPurchaseOrderItem item : purchaseOrder.getItems()) {
			item.setProduct(productService.get(item.getProduct().getId()));
		}
		if (filePath == null) {
			filePath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")+ Global.getCkBaseDir() + "/psi/lcPurchaseOrders";
		}
		File file = new File(filePath, purchaseOrder.getOrderNo());
		if (!file.exists()) {
			file.mkdirs();
		}
		File pdfFile = new File(file, purchaseOrder.getOrderNo() + ".pdf");
		
		Map<String,String> versionMap = productService.getFnskuAndSkuVersionMap();
		PdfUtil.genPurchaseOrderPdf(pdfFile, purchaseOrder,versionMap);
		FileInputStream in = new FileInputStream(pdfFile);
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("application/pdf");// pdf文件
		response.addHeader("Content-Disposition", "filename="
					+ purchaseOrder.getOrderNo()+".pdf");
		byte data[] = new byte[1024];
		int len;
		while ((len = in.read(data)) != -1) {
			out.write(data, 0, len);
		}
		out.flush();
		in.close();
		out.close();
		return null;
	}
	
//	@RequiresPermissions("psi:all:view")
//	@ResponseBody
//	@RequestMapping(value = "printPi")
//	public String printPi(LcPurchaseOrder purchaseOrder,HttpServletResponse response) throws Exception {
//		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
//		File pdfFile = new File(ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")+purchaseOrder.getPiFilePath());
//		FileInputStream in = new FileInputStream(pdfFile);
//		ServletOutputStream out = response.getOutputStream();
//		response.setContentType("application/pdf");// pdf文件
//		byte data[] = new byte[1024];
//		int len;
//		while ((len = in.read(data)) != -1) {
//			out.write(data, 0, len);
//		}
//		out.flush();
//		in.close();
//		out.close();
//		return null;
//	}

	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "uploadPi")
	public String uploadPi(LcPurchaseOrder purchaseOrder, Model model) {
		purchaseOrder = purchaseOrderService.get(purchaseOrder.getId());
		model.addAttribute("purchaseOrder", purchaseOrder);
		return "modules/psi/lc/lcPurchaseOrderUploadPi";
	}
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "uploadPiSave")
	public String uploadPiSave(MultipartFile piFile,LcPurchaseOrder purchaseOrder,RedirectAttributes redirectAttributes) throws IOException {
		purchaseOrder=this.purchaseOrderService.get(purchaseOrder.getId());
		if(piFile!=null&&piFile.getSize()!=0){
			if (filePath == null) {
				filePath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")+ Global.getCkBaseDir() + "/psi/lcPurchaseOrders";
			}
			String uuid = UUID.randomUUID().toString();
			File file1 = new File(filePath, purchaseOrder.getOrderNo());
			if (!file1.exists()) {
				file1.mkdirs();
			}
			File piFilePdf = new File(file1, "pi_"+uuid+".pdf");
			FileUtils.copyInputStreamToFile(piFile.getInputStream(),piFilePdf);
			purchaseOrder.setPiFilePath(Global.getCkBaseDir() + "/psi/lcPurchaseOrders/"+purchaseOrder.getOrderNo()+"/pi_"+uuid+".pdf");
			this.purchaseOrderService.save(purchaseOrder);
			addMessage(redirectAttributes, "上传文件'" + piFile.getOriginalFilename() + "'成功");
		}
		
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}

	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "form")
	public String form(LcPurchaseOrder purchaseOrder, Model model) {
		model.addAttribute("purchaseOrder", purchaseOrder);
		return "modules/psi/lc/lcPurchaseOrderForm";
	}
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "cancel")
	public String cancle(Integer id, Model model,RedirectAttributes redirectAttributes) {
		LcPurchaseOrder purchaseOrder = purchaseOrderService.get(id);
		String orderSta=purchaseOrder.getOrderSta();
		purchaseOrder.setOrderSta("6");//已取消
		purchaseOrder.setCancelDate(new Date());
		purchaseOrder.setCancelUser(UserUtils.getUser());
		for(LcPurchaseOrderItem item:purchaseOrder.getItems()){
			if(item.getForecastItemId()!=null){
				this.purchaseOrderService.updateFroecastPurchaseQuantity(item.getForecastItemId(), 0);
			}
		}
		boolean flag=false;
		for(LcPurchaseOrderItem item: purchaseOrder.getItems()){
			if(item.getForecastItemId()!=null){
				flag=true;
				break;
			}
		}
		if(flag){
			this.sendEmail(purchaseOrder);
		}
		
		this.purchaseOrderService.save(purchaseOrder);
		//取消分批预计收货日期
		this.purchaseOrderService.deleteDeliveryDateByOrderId(id);
		//订单取消发信给物流部
		String content = "Hi,All<br/>&nbsp;&nbsp;&nbsp;&nbsp;深圳理诚采购订单取消!订单号："+purchaseOrder.getOrderNo()+",请知悉！<br/>以上、谢谢!";
		String toAddress="supply-chain@inateck.com";
		if("1".equals(orderSta)){//已审核状态加入供应商
			toAddress+=","+purchaseOrder.getSupplier().getMail();
		}
		this.purchaseOrderService.sendNoticeEmail(toAddress, content, "深圳理诚采购订单取消["+purchaseOrder.getOrderNo()+"]", "", "");
		
		addMessage(redirectAttributes, "取消订单'" + purchaseOrder.getOrderNo() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	}
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		purchaseOrderService.delete(id);
		addMessage(redirectAttributes, "删除采购订单成功");
		return "redirect:"+Global.getAdminPath()+"/modules/psi/lc/lcPurchaseOrder/?repage";
	}
	
	@RequiresPermissions("psi:all:view")
	@ResponseBody
	@RequestMapping(value = "orderInfo")
	public String getOrderUpdateTimes(String type,String order){
		StringBuilder rs =new StringBuilder();
		if(StringUtils.isNotEmpty(order)){
			if("0".equals(type)){
					rs.append("订单号[").append(order).append("]最后修改于:").append(purchaseOrderService.get(order).getUpdateDate().toLocaleString());
			}else{
				List<LcPurchaseOrder> orderIds = billService.getOrderInfo(order);
				if(orderIds!=null){
					for (LcPurchaseOrder orderIn : orderIds) {
						if(orderIn.getUpdateDate()!=null){
							rs.append("订单号[").append(orderIn.getOrderNo()).append("]最后修改于:").append(orderIn.getUpdateDate().toLocaleString()).append(",");
						}
					}
				}
			}
		}
		return rs.toString();
	}
	

	private void sendEmailToSupplier(LcPurchaseOrder purchaseOrder,User user) throws Exception{
		SendEmail  sendEmail = new SendEmail();
		//往采购供应商发信 获取供应商模板
		PsiSupplier supplier = psiSupplierService.get(purchaseOrder.getSupplier().getId());
		String orderNo=purchaseOrder.getOrderNo();
		if(supplier==null){
			throw new RuntimeException("供应商不存在！");
		}
		Map<String,Object> params = Maps.newHashMap();
		String template = "";
		try {
			params.put("supplier", supplier);
			params.put("cuser", user);
			template = PdfUtil.getPsiTemplate("orderEmail.ftl", params);
		} catch (Exception e) {}
		sendEmail.setSendContent(template);
		sendEmail.setSendEmail(supplier.getMail());    
		sendEmail.setSendSubject("新订单PN"+orderNo+"("+DateUtils.getDate()+")");
		sendEmail.setCreateBy(UserUtils.getUser());
		//向供应商发送邮件 加入附件和抄送人
		String address =  "frank@inateck.com,sophie@inateck.com,bella@inateck.com,emma.chao@inateck.com,"+user.getEmail();  
		sendEmail.setBccToEmail(address);
		//发送邮件
		final MailInfo mailInfo = sendEmail.getMailInfo();
		for (LcPurchaseOrderItem item : purchaseOrder.getItems()) {
			item.setProduct(productService.get(item.getProduct().getId()));
		}
		String baseDirStr = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/")+Global.getCkBaseDir()+"/customEmail";
		String uuid = UUID.randomUUID().toString();
		File baseDir = new File(baseDirStr+"/"+uuid); 
		if(!baseDir.isDirectory())
			baseDir.mkdirs();
		File pdfFile = new File(baseDir,orderNo + ".pdf");
		Map<String,String> versionMap = productService.getFnskuAndSkuVersionMap();
		PdfUtil.genPurchaseOrderPdf(pdfFile, purchaseOrder,versionMap);
		mailInfo.setFileName(orderNo + ".pdf");
		mailInfo.setFilePath(pdfFile.getAbsolutePath());
		sendEmail.setSendAttchmentPath(uuid+"/"+orderNo + ".pdf");
		new Thread(){
			@Override
			public void run() {
				poMaillManager.send(mailInfo);
			}
		}.start();
		
		sendEmail.setSentDate(new Date());
		sendEmail.setSendFlag("1");
		sendEmailService.save(sendEmail);
		
	}
	
	@RequestMapping(value =  "deliveryRate" )
	public String deliveryRate(LcPurchaseOrder purchaseOrder, Model model,RedirectAttributes redirectAttributes ){
		SimpleDateFormat   formatMonth = new SimpleDateFormat("yyyyMM");
		Date today = new Date();
		today.setHours(0);
		today.setSeconds(0);
		today.setMinutes(0);
		if (purchaseOrder.getCreateDate() == null) {
			purchaseOrder.setCreateDate(DateUtils.addMonths(today, -6));
		}
        if(purchaseOrder.getPurchaseDate()==null){
        	purchaseOrder.setPurchaseDate(today);
        }
        if(purchaseOrder.getSupplier()!=null&&purchaseOrder.getSupplier().getId()!=null){
        	purchaseOrder.setSupplier(psiSupplierService.get(purchaseOrder.getSupplier().getId()));
        }
        List<LcPurchaseOrderItem> list=purchaseOrderService.findRate2(purchaseOrder);
        Map<String,Map<String,Integer>> map=new HashMap<String,Map<String,Integer>>();
		Map<String,Map<String,Float>> mapRate=new HashMap<String,Map<String,Float>>();
		Map<String,Map<String,List<LcPurchaseOrderItem>>> itemMap=Maps.newHashMap();
		Date curDate=new Date();
		Set<Integer> idSets=Sets.newHashSet();
		for(LcPurchaseOrderItem item:list){
			String itemIdStr=item.getItemIdStr();
			if(itemIdStr.contains(",")){
				String[] itemId=itemIdStr.split(",");
				for(String tId:itemId){
					idSets.add(Integer.parseInt(tId));
				}
			}else{
				idSets.add(Integer.parseInt(itemIdStr));
			}
		}
		if(idSets.size()>0){
			Map<Integer,Date> firstDateMap =billService.getFirstReceiveDateById(idSets);
			Map<Integer,Date> finishDateMap =billService.getFinishedReceiveDateById(idSets);
			//Map<Integer,Integer>  receiverNumMap=billService.getReceiverNum(idSets);
			
			for(LcPurchaseOrderItem item:list){
	        	Date firstDate =null;
	        	Date finishDate =null;
	        	String itemIdStr=item.getItemIdStr();
	        	Integer receiverNum=0;
				if(itemIdStr.contains(",")){
					String[] itemId=itemIdStr.split(",");
					List<Date> firstDateList=Lists.newArrayList();
					List<Date> finishDateList=Lists.newArrayList();
					Set<Integer> tempIds=Sets.newHashSet();
					for(String tId:itemId){
						if(firstDateMap!=null&&firstDateMap.get(Integer.parseInt(tId))!=null){
							firstDateList.add(firstDateMap.get(Integer.parseInt(tId)));
			        	}
			        	if(finishDateMap!=null&&finishDateMap.get(Integer.parseInt(tId))!=null){
			        		finishDateList.add(finishDateMap.get(Integer.parseInt(tId)));
			        	}
			        	/*if(receiverNumMap!=null&&receiverNumMap.get(Integer.parseInt(tId))!=null){
			        		receiverNum+=receiverNumMap.get(Integer.parseInt(tId));
			        	}*/
			        	tempIds.add(Integer.parseInt(tId));
					}
					if(firstDateList!=null&&firstDateList.size()>0){
						firstDate=Collections.min(firstDateList);
					}
					if(finishDateList!=null&&finishDateList.size()>0){
						finishDate=Collections.max(finishDateList);
					}
					receiverNum=billService.getReceiverNum2(tempIds);
				}else{
		        	if(firstDateMap!=null&&firstDateMap.get(Integer.parseInt(itemIdStr))!=null){
		        		firstDate=firstDateMap.get(Integer.parseInt(itemIdStr));
		        	}
		        	if(finishDateMap!=null&&finishDateMap.get(Integer.parseInt(itemIdStr))!=null){
		        		finishDate=finishDateMap.get(Integer.parseInt(itemIdStr));
		        	}
		        	/*if(receiverNumMap!=null&&receiverNumMap.get(Integer.parseInt(itemIdStr))!=null){
		        		receiverNum=receiverNumMap.get(Integer.parseInt(itemIdStr));
		        	}*/
		        	receiverNum=billService.getReceiverNum2(Sets.newHashSet(Integer.parseInt(itemIdStr)));
				}
	        	
	        	Date deliveredDate=item.getDeliveryDate();
	        	String remark=item.getRemark();
	        	String deliveryStatus="";
	        	String productName_color=item.getProductName();
	        	String orderNo=item.getForecastRemark();
	        	//String orderState=item.getOrderSta();//4 5
	        	if(firstDate!=null){
	        		int nDay = (int) ((firstDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	        		if(nDay>3){
	    				if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
	    					deliveryStatus="0";//非供应商逾期
	    				}else{
	    					deliveryStatus="3";//供应商逾期
	    				}
	    			}else{
	    				if("已收货".equals(item.getDelFlag())){
	    					int fDay = (int) ((finishDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	    					if(fDay>3){
								if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
									deliveryStatus="0";//非供应商逾期
								}else{
									deliveryStatus="3";//供应商逾期
								}
							}else if(fDay<-3){
								deliveryStatus="1";//提前
	    					}else{
	    						deliveryStatus="2";//正常
	    					}
	            		}else{
	            			int fDay = (int) ((finishDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	    					if(fDay>3){
								if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
									deliveryStatus="0";//非供应商逾期
								}else{
									deliveryStatus="3";//供应商逾期
								}
							}else{
								    nDay = (int) ((curDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
					        		if(nDay>3){
					    				if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
					    					deliveryStatus="0";//非供应商逾期
					    				}else{
					    					deliveryStatus="3";//供应商逾期
					    				}
					    			}else{
					    				deliveryStatus="2";//正常
					    			}
	    						//deliveryStatus="2";//正常
	    					}
	            		}
	    			}
	        	}else{
	        		int nDay = (int) ((curDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	        		if(nDay>3){
	    				if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
	    					deliveryStatus="0";//非供应商逾期
	    				}else{
	    					deliveryStatus="3";//供应商逾期
	    				}
	    			}else{
	    				deliveryStatus="2";//正常
	    			}
	        	}
	        	String compareDate=formatMonth.format(deliveredDate);
				Map<String,Integer> data=map.get(deliveryStatus);
				if(data==null){
					data= Maps.newHashMap();
					map.put(deliveryStatus,data);
				}
				Integer amount=data.get(compareDate);
				if(amount==null){
					data.put(compareDate,1);
				}else{
				    data.put(compareDate,amount+1);
				}
				
				
				Integer totalAmount=data.get("total");
				if(totalAmount==null){
					data.put("total",1);
				}else{
				    data.put("total",totalAmount+1);
				}
				
				Map<String,List<LcPurchaseOrderItem>> temp=itemMap.get(deliveryStatus);
				if(temp==null){
					temp=Maps.newHashMap();
					itemMap.put(deliveryStatus, temp);
				}
				List<LcPurchaseOrderItem> tempList=temp.get(compareDate);
				if(tempList==null){
					tempList=Lists.newArrayList();
					temp.put(compareDate, tempList);
				}
				
				List<LcPurchaseOrderItem> totalTempList=temp.get("total");
				if(totalTempList==null){
					totalTempList=Lists.newArrayList();
					temp.put("total", totalTempList);
				}
				
				LcPurchaseOrderItem tempItem=new LcPurchaseOrderItem();
				tempItem.setRemark(remark);
				tempItem.setProductName(productName_color);
				tempItem.setDeliveryDate(deliveredDate);
				tempItem.setActualDeliveryDate(finishDate);
				tempItem.setUpdateDate(firstDate);
				tempItem.setForecastRemark(orderNo);
				tempItem.setColorCode(item.getColorCode());
				tempItem.setForecastItemId(item.getForecastItemId());
				tempItem.setDelFlag(item.getDelFlag());
				tempItem.setQuantityOrdered(receiverNum);
				tempList.add(tempItem);
				totalTempList.add(tempItem);
	        }
			
		}
		
        
      //构建x轴
      		List<String> xAxis  = Lists.newArrayList();
      		Date start = purchaseOrder.getCreateDate();
      		Date end = purchaseOrder.getPurchaseDate();

      		while(end.after(start)||end.equals(start)){
      			String key = formatMonth.format(start);
      			xAxis.add(key);
      			start = DateUtils.addMonths(start, 1);
      		}
      		xAxis.add("total");
      		model.addAttribute("xAxis", xAxis);
      		//类型/日期/数据
      		for (String date : xAxis) {
      			int total=0;
      			int afterDate=0;
      			int otherAfterDate=0;
      			int beforeDate=0;
      			int suitDate=0;
      			if(map.get("0")!=null&&map.get("0").get(date)!=null){
      				afterDate=map.get("0").get(date);
      				total+=afterDate;
      			}
      			if(map.get("1")!=null&&map.get("1").get(date)!=null){
      				beforeDate=map.get("1").get(date);
      				total+=beforeDate;
      			}
      			if(map.get("2")!=null&&map.get("2").get(date)!=null){
      				suitDate=map.get("2").get(date);
      				total+=suitDate;
      			}
      			if(map.get("3")!=null&&map.get("3").get(date)!=null){
      				otherAfterDate=map.get("3").get(date);
      				total+=otherAfterDate;
      			}
      			Map<String,Float> data=mapRate.get("0");
      			if(data==null){
      				data= Maps.newHashMap();
      				mapRate.put("0",data);
      			}
      			data.put(date,total==0?0:afterDate*100f/total);
      			
      			Map<String,Float> data1=mapRate.get("1");
      			if(data1==null){
      				data1= Maps.newHashMap();
      				mapRate.put("1",data1);
      			}

      			data1.put(date,total==0?0:beforeDate*100f/total);
      			
      			Map<String,Float> data2=mapRate.get("2");
      			if(data2==null){
      				data2= Maps.newHashMap();
      				mapRate.put("2",data2);
      			}
      			data2.put(date,total==0?0:suitDate*100f/total);
      			
      			Map<String,Float> data3=mapRate.get("3");
      			if(data3==null){
      				data3= Maps.newHashMap();
      				mapRate.put("3",data3);
      			}
      			data3.put(date,total==0?0:otherAfterDate*100f/total);
      		}
      	model.addAttribute("rate",mapRate);
    	model.addAttribute("mapNum",map);
    	model.addAttribute("itemMap",itemMap);
		model.addAttribute("suppliers", this.psiSupplierService.findAll());
        model.addAttribute("purchaseOrder",purchaseOrder);
		model.addAttribute("productList",this.productService. findProductNameAndColorList());
		return "modules/psi/lc/supplierDeliveryRate";
	}
	
	
	@RequestMapping(value =  "exportDeliveryRate" )
	public String exportDeliveryRate(LcPurchaseOrder purchaseOrder, Model model,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response ){
		SimpleDateFormat   formatMonth = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat   formatDay = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		today.setHours(0);
		today.setSeconds(0);
		today.setMinutes(0);
		if (purchaseOrder.getCreateDate() == null) {
			purchaseOrder.setCreateDate(DateUtils.addMonths(today, -6));
		}
        if(purchaseOrder.getPurchaseDate()==null){
        	purchaseOrder.setPurchaseDate(today);
        }
        if(purchaseOrder.getSupplier()!=null&&purchaseOrder.getSupplier().getId()!=null){
        	purchaseOrder.setSupplier(psiSupplierService.get(purchaseOrder.getSupplier().getId()));
        }
        List<LcPurchaseOrderItem> list=purchaseOrderService.findRate2(purchaseOrder);
        Map<String,Map<String,Integer>> map=new HashMap<String,Map<String,Integer>>();
		Map<String,Map<String,Float>> mapRate=new HashMap<String,Map<String,Float>>();
		Map<String,Map<String,List<LcPurchaseOrderItem>>> itemMap=Maps.newHashMap();
		Date curDate=new Date();
		Set<Integer> idSets=Sets.newHashSet();
		for(LcPurchaseOrderItem item:list){
			String itemIdStr=item.getItemIdStr();
			if(itemIdStr.contains(",")){
				String[] itemId=itemIdStr.split(",");
				for(String tId:itemId){
					idSets.add(Integer.parseInt(tId));
				}
			}else{
				idSets.add(Integer.parseInt(itemIdStr));
			}
		}
		if(idSets.size()>0){
			Map<Integer,Date> firstDateMap =billService.getFirstReceiveDateById(idSets);
			Map<Integer,Date> finishDateMap =billService.getFinishedReceiveDateById(idSets);
			//Map<Integer,Integer>  receiverNumMap=billService.getReceiverNum(idSets);
			
			for(LcPurchaseOrderItem item:list){
	        	Date firstDate =null;
	        	Date finishDate =null;
	        	String itemIdStr=item.getItemIdStr();
	        	Integer receiverNum=0;
				if(itemIdStr.contains(",")){
					String[] itemId=itemIdStr.split(",");
					List<Date> firstDateList=Lists.newArrayList();
					List<Date> finishDateList=Lists.newArrayList();
					
					Set<Integer> tempIds=Sets.newHashSet();
					for(String tId:itemId){
						if(firstDateMap!=null&&firstDateMap.get(Integer.parseInt(tId))!=null){
							firstDateList.add(firstDateMap.get(Integer.parseInt(tId)));
			        	}
			        	if(finishDateMap!=null&&finishDateMap.get(Integer.parseInt(tId))!=null){
			        		finishDateList.add(finishDateMap.get(Integer.parseInt(tId)));
			        	}
			        	/*if(receiverNumMap!=null&&receiverNumMap.get(Integer.parseInt(tId))!=null){
			        		receiverNum+=receiverNumMap.get(Integer.parseInt(tId));
			        	}*/
			        	tempIds.add(Integer.parseInt(tId));
					}
					
					if(firstDateList!=null&&firstDateList.size()>0){
						firstDate=Collections.min(firstDateList);
					}
					if(finishDateList!=null&&finishDateList.size()>0){
						finishDate=Collections.max(finishDateList);
					}
					receiverNum=billService.getReceiverNum2(tempIds);
				}else{
		        	if(firstDateMap!=null&&firstDateMap.get(Integer.parseInt(itemIdStr))!=null){
		        		firstDate=firstDateMap.get(Integer.parseInt(itemIdStr));
		        	}
		        	if(finishDateMap!=null&&finishDateMap.get(Integer.parseInt(itemIdStr))!=null){
		        		finishDate=finishDateMap.get(Integer.parseInt(itemIdStr));
		        	}
		        	/*if(receiverNumMap!=null&&receiverNumMap.get(Integer.parseInt(itemIdStr))!=null){
		        		receiverNum=receiverNumMap.get(Integer.parseInt(itemIdStr));
		        	}*/
		        	receiverNum=billService.getReceiverNum2(Sets.newHashSet(Integer.parseInt(itemIdStr)));
				}
	        	
	        	Date deliveredDate=item.getDeliveryDate();
	        	String remark=item.getRemark();
	        	String deliveryStatus="";
	        	String productName_color=item.getProductName();
	        	String orderNo=item.getForecastRemark();
	        	//String orderState=item.getOrderSta();//4 5
	        	if(firstDate!=null){
	        		int nDay = (int) ((firstDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	        		if(nDay>3){
	    				if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
	    					deliveryStatus="0";//非供应商逾期
	    				}else{
	    					deliveryStatus="3";//供应商逾期
	    				}
	    			}else{
	    				if("已收货".equals(item.getDelFlag())){
	    					int fDay = (int) ((finishDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	    					if(fDay>3){
								if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
									deliveryStatus="0";//非供应商逾期
								}else{
									deliveryStatus="3";//供应商逾期
								}
							}else if(fDay<-3){
								deliveryStatus="1";//提前
	    					}else{
	    						deliveryStatus="2";//正常
	    					}
	            		}else{
	            			int fDay = (int) ((finishDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	    					if(fDay>3){
								if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
									deliveryStatus="0";//非供应商逾期
								}else{
									deliveryStatus="3";//供应商逾期
								}
							}else{
	    						//deliveryStatus="2";//正常
								nDay = (int) ((curDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
								if(nDay>3){
				    				if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
				    					deliveryStatus="0";//非供应商逾期
				    				}else{
				    					deliveryStatus="3";//供应商逾期
				    				}
				    			}else{
				    				deliveryStatus="2";//正常
				    			}
	    					}
	            		}
	    			}
	        	}else{
	        		int nDay = (int) ((curDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	        		if(nDay>3){
	    				if(remark.contains("新品")||remark.contains("付款延后")||remark.contains("暂缓")||remark.contains("包材延误")||remark.contains("包材送晚")||remark.contains("配件交期延误")||remark.contains("法定")||remark.contains("等待运输")||remark.contains("暂停")||remark.contains("质检")){
	    					deliveryStatus="0";//非供应商逾期
	    				}else{
	    					deliveryStatus="3";//供应商逾期
	    				}
	    			}else{
	    				deliveryStatus="2";//正常
	    			}
	        	}
	        	String compareDate=formatMonth.format(deliveredDate);
				Map<String,Integer> data=map.get(deliveryStatus);
				if(data==null){
					data= Maps.newHashMap();
					map.put(deliveryStatus,data);
				}
				Integer amount=data.get(compareDate);
				if(amount==null){
					data.put(compareDate,1);
				}else{
				    data.put(compareDate,amount+1);
				}
				
				Map<String,List<LcPurchaseOrderItem>> temp=itemMap.get(deliveryStatus);
				if(temp==null){
					temp=Maps.newHashMap();
					itemMap.put(deliveryStatus, temp);
				}
				List<LcPurchaseOrderItem> tempList=temp.get(compareDate);
				if(tempList==null){
					tempList=Lists.newArrayList();
					temp.put(compareDate, tempList);
				}
				LcPurchaseOrderItem tempItem=new LcPurchaseOrderItem();
				tempItem.setRemark(remark);
				tempItem.setProductName(productName_color);
				tempItem.setDeliveryDate(deliveredDate);
				tempItem.setActualDeliveryDate(finishDate);
				tempItem.setUpdateDate(firstDate);
				tempItem.setForecastRemark(orderNo);
				tempItem.setColorCode(item.getColorCode());
				tempItem.setForecastItemId(item.getForecastItemId());
				tempItem.setDelFlag(item.getDelFlag());
				tempItem.setQuantityOrdered(receiverNum);
				tempList.add(tempItem);
	        }
			
		}
        
        
      //构建x轴
      		List<String> xAxis  = Lists.newArrayList();
      		Date start = purchaseOrder.getCreateDate();
      		Date end = purchaseOrder.getPurchaseDate();

      		while(end.after(start)||end.equals(start)){
      			String key = formatMonth.format(start);
      			xAxis.add(key);
      			start = DateUtils.addMonths(start, 1);
      		}
      		

      		XSSFWorkbook wb = new XSSFWorkbook();
    		XSSFSheet sheet = wb.createSheet();
    		XSSFRow row = sheet.createRow(0);
    		XSSFCellStyle style = wb.createCellStyle();
    		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    		style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
    		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
    		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
    		style.setBottomBorderColor(HSSFColor.BLACK.index);
    		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
    		style.setLeftBorderColor(HSSFColor.BLACK.index);
    		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
    		style.setRightBorderColor(HSSFColor.BLACK.index);
    		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
    		style.setTopBorderColor(HSSFColor.BLACK.index);
    		XSSFFont font = wb.createFont();
    		font.setFontHeightInPoints((short) 16); // 字体高度
    		font.setFontName(" 黑体 "); // 字体
    		font.setBoldweight((short) 16);
    		style.setFont(font);

    		CellStyle contentStyle = wb.createCellStyle();
    		contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    		contentStyle.setBottomBorderColor(HSSFColor.BLACK.index);
    		contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    		contentStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    		contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    		contentStyle.setRightBorderColor(HSSFColor.BLACK.index);
    		contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    		contentStyle.setTopBorderColor(HSSFColor.BLACK.index);
    		XSSFCell cell = null;	
    		List<String> title=Lists.newArrayList("月份","类型","订单号","供应商","产品名称","国家","状态","订单交期","第一次收货","最新一次收货","收货批次","备注");
      		//类型/日期/数据 
    		 for (int i = 0; i < title.size(); i++) {
 				cell = row.createCell(i);
 				cell.setCellValue(title.get(i));
 				cell.setCellStyle(style);
 				sheet.autoSizeColumn((short) i);
 			}
    		int rownum=1;
    		List<String> typeList=Lists.newArrayList("0","3","1","2");
      		for (String date : xAxis) {
      			
      			for(String type:typeList){
      				if(itemMap!=null&&itemMap.get(type)!=null&&itemMap.get(type).get(date)!=null&&itemMap.get(type).get(date).size()>0){
      					List<LcPurchaseOrderItem> items=itemMap.get(type).get(date);
      					for(LcPurchaseOrderItem item:items){
      						row=sheet.createRow(rownum++);
      						int j=0;
      		      			row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(date); 
      		      		    row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue("0".equals(type)?"非供应商原因逾期":("3".equals(type)?"供应商原因逾期":("1".equals(type)?"提前":"正常")));
      		      		    row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(item.getForecastRemark()); 
      		      	       // row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue("1".equals(item.getDelFlag())?"已审核":("2".equals(item.getDelFlag())?"生产":("3".equals(item.getDelFlag())?"部分收货":("4".equals(item.getDelFlag())?"已收货":"已完成")))); 
      		      		   
      		      		    row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(item.getColorCode()); 
      		      	        row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(item.getProductName()); 
      		      	        row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue("com".equals(item.getCountryCode())?"us":item.getCountryCode()); 
      		      	        row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(item.getDelFlag());
      		      	        row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(formatDay.format(item.getDeliveryDate()));
	      		      	    if(item.getUpdateDate()==null){
	  		      	            row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(""); 
	  		      	        }else{
	  		      	           row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(formatDay.format(item.getUpdateDate())); 
	  		      	        }
	      		      	    
      		      	        if(item.getActualDeliveryDate()==null){
      		      	            row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(""); 
      		      	        }else{
      		      	           row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(formatDay.format(item.getActualDeliveryDate())); 
      		      	        }
	      		      	   
      		      	        row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(item.getQuantityOrdered());
	      		      	    if(item.getRemark()==null){
	  		      	            row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(""); 
	  		      	        }else{
	  		      	           row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(item.getRemark()); 
	  		      	        }
      					}
      				}
      			}
      		}
      	
    	model.addAttribute("mapNum",map);
    	model.addAttribute("itemMap",itemMap);
    	try {
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
        	response.setContentType("application/x-download");
        	SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmm");
        	String fileName = "supplierDeliveryRate" + sdf.format(new Date()) + ".xlsx";
        	fileName = URLEncoder.encode(fileName, "UTF-8");
        	response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
    		OutputStream out = response.getOutputStream();
    		wb.write(out);
    		out.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
	}
	
	
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value =  "exp" )
	public String exp(LcPurchaseOrder purchaseOrder, HttpServletRequest request,String isCheck,String productIdColor,HttpServletResponse response, Model model) throws UnsupportedEncodingException, ParseException {

		SimpleDateFormat   sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		int excelNo =1;
		
		Map<String,Integer> packMap= this.productService.findPackQuantityMap();
		List<String> isNewList = productEliminateService.findIsNewProductName();
		Page<LcPurchaseOrder>	page= new Page<LcPurchaseOrder>(request, response);
		page.setPageSize(99999);
	    page=purchaseOrderService.findByProduct(page, purchaseOrder, isCheck, productIdColor);
	    List<LcPurchaseOrder> list=page.getList();
	    if(list.size()>65535){
	    	throw new RuntimeException("一次导出的最大数据不能超过65535条！请分两次导出！");
	    }
	    
	    HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		String[] title = {"   PN   ", " PO NO. ", "PO DATE",  " MPQ ","VENDOR","CURRENCY","PO QTY","Delivered","Balance","PO L/T","First L/T","Delivery Status","Actual Delivery Date",
				" DE "," UK "," FR "," IT "," ES "," JP "," US "," CA "," DE "," UK "," FR "," IT "," ES "," JP "," US "," CA "," DE "," UK "," FR "," IT "," ES "," JP "," US "," CA ","Is New"," Remark "};
		
		  style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
		  style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		  style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  //设置Excel中的边框(表头的边框)
//		  //设置字体
		  HSSFFont font = wb.createFont();
		  font.setFontHeightInPoints((short) 11); // 字体高度
		  style.setFont(font);
		  row.setHeight((short) 400);
		  HSSFCell cell = null;		
		  for (int i = 0; i < title.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(title[i]);
				cell.setCellStyle(style);
				sheet.autoSizeColumn((short) i);
			}
		  
		 for(LcPurchaseOrder purchaseOrderTrue: list){
		    	List<LcPurchaseOrder> tempOrders=purchaseOrderTrue.getTempOrders();
		    	for(int i =0;i<tempOrders.size();i++){
		    		String deliveryStatus="";
    				LcPurchaseOrder tempOrder = tempOrders.get(i);
    				List<LcPurchaseOrderItem> orderItems = tempOrder.getItems();
    				String productName = "";
    				Date   deliveredDate=null;
//    				Date   compareDate=sdf1.parse(sdf1.format(new Date()));
    				Date   acDeliveryDate=null;
    				Map<String,String> itemQuantityMap  = Maps.newHashMap();
    				Set<Integer> orderItemSet = Sets.newHashSet();
    				Map<String,String>  colorMap = Maps.newHashMap();
    				String remark ="";
                    for(int j =0;j<orderItems.size();j++){
                    	LcPurchaseOrderItem item = orderItems.get(j);
                    	if(j==0){
                    		productName=item.getProductName();
                    		deliveredDate=item.getDeliveryDate();//po交期
                    		acDeliveryDate = item.getActualDeliveryDate();
                    		//如果有收货完成时间，按收货完成时间
	           				 if(tempOrder.getOrderSta().equals("4")||tempOrder.getOrderSta().equals("5")){
//	           				 	compareDate=item.getActualDeliveryDate();
		           				 int nDay = (int) ((acDeliveryDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
		 						 if(nDay>3){
		 							deliveryStatus="逾期";
		 						 }else if(nDay<-3){
		 							deliveryStatus="提前";
		     					 }else{
		     						deliveryStatus="正常";
		     					 }
	           				 }else{
	           					 if(deliveredDate.before(new Date())){//如果收货未完成且今天大于PO
	           						 int nDay = (int) ((new Date().getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	    	 						 if(nDay>3){
	    	 							deliveryStatus="逾期";
	    	 						 }else if(nDay<-3){
	    	 							deliveryStatus="提前";
	    	     					 }else{
	    	     						deliveryStatus="正常";
	    	     					 }
	           					 }
	           				 }
                    	}
                    	String quantitys=item.getQuantityOrdered()+","+item.getQuantityReceived()+","+item.getQuantityUnReceived();
                    	itemQuantityMap.put(item.getCountryCode()+","+item.getColorCode(), quantitys);
                    	
                    	if(colorMap.get(item.getColorCode())!=null){
                    		String[] auqArr=colorMap.get(item.getColorCode()).split(",");
                    		quantitys=(item.getQuantityOrdered()+Integer.parseInt(auqArr[0]))+","+(item.getQuantityReceived()+Integer.parseInt(auqArr[1]))+","+(item.getQuantityUnReceived()+Integer.parseInt(auqArr[2]));
                    	}
                    	colorMap.put(item.getColorCode(), quantitys);
                    	if(StringUtils.isNotEmpty(item.getRemark())){
                    		remark= item.getRemark();
                    	}
                    	
                    	orderItemSet.add(item.getId());
                    }
                  //根据下面颜色迭代行
                    for (Map.Entry<String, String> entry : colorMap.entrySet()) {
                    	String color = entry.getKey();
                    	 row = sheet.createRow(excelNo++);  //生成行
                    	 String productName_color="";
        				 String key = productName;
        				 if(StringUtils.isNotEmpty(color)){
        					 productName_color=productName+"_"+color;
        					 key = productName + "_" + color;
        				 }else{
        					 productName_color=productName;
        				 }
        				 
        				 row.createCell(0,Cell.CELL_TYPE_STRING).setCellValue(productName_color);
        				 row.createCell(1,Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getOrderNo());
        				 row.createCell(2,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format((Date)tempOrder.getCreateDate()));  //创建日期
        				 
        			
                         row.createCell(3,Cell.CELL_TYPE_NUMERIC).setCellValue(packMap.get(productName));
                         if (tempOrder.getSupplier() != null) {
                        	 row.createCell(4,Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getSupplier().getNikename());
						 } else {
							 row.createCell(4,Cell.CELL_TYPE_STRING).setCellValue("");
						 }
                         row.createCell(5,Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getCurrencyType());
                         
                         
                         String[] arr=entry.getValue().split(",");
                         row.createCell(6,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(arr[0]));
                         row.createCell(7,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(arr[1]));
                         row.createCell(8,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(arr[2]));  //未收货
                         row.createCell(9,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format((Date)deliveredDate));

//                         Date finishedDate =billService.getFinishedReceiveDate(orderItemSet);//最近一次收货时间
                         
        				 Date firstDate =billService.getFirstReceiveDate(orderItemSet);
        				 if(firstDate!=null){
        					row.createCell(10,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format(firstDate));//第一次收货日期 
        				 }else{
        					 row.createCell(10,Cell.CELL_TYPE_STRING).setCellValue("");
        				 }
        				 
        				
 						
 						row.createCell(11,Cell.CELL_TYPE_STRING).setCellValue(deliveryStatus);
        				
//        				 if(tempOrder.getItemsQuantityUnReceived()==0&&finishedDate!=null){  //如果没有收货的      说明都收货完成
 						 if(acDeliveryDate!=null){ 
        					 row.createCell(12,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format(acDeliveryDate));
        				 }else{
        					 row.createCell(12,Cell.CELL_TYPE_STRING).setCellValue("");//完成日期
        				 }
        				 
        				 if(itemQuantityMap.get("de,"+color)!=null){
         					String values = itemQuantityMap.get("de,"+color);
         					row.createCell(13,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(21,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(29,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(13,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(21,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(29,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("uk,"+color)!=null){
         					String values = itemQuantityMap.get("uk,"+color);
         					row.createCell(14,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(22,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(30,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(14,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(22,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(30,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("fr,"+color)!=null){
         					String values = itemQuantityMap.get("fr,"+color);
         					row.createCell(15,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(23,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(31,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(15,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(23,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(31,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("it,"+color)!=null){
         					String values = itemQuantityMap.get("it,"+color);
         					row.createCell(16,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(24,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(32,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(16,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(24,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(32,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("es,"+color)!=null){
         					String values = itemQuantityMap.get("es,"+color);
         					row.createCell(17,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(25,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(33,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(17,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(25,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(33,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("jp,"+color)!=null){
         					String values = itemQuantityMap.get("jp,"+color);
         					row.createCell(18,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(26,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(34,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(18,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(26,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(34,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("com,"+color)!=null){
         					String values = itemQuantityMap.get("com,"+color);
         					row.createCell(19,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(27,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(35,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(19,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(27,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(35,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("ca,"+color)!=null){
         					String values = itemQuantityMap.get("ca,"+color);
         					row.createCell(20,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(28,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(36,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(20,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(28,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(36,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				row.createCell(37,Cell.CELL_TYPE_STRING).setCellValue(isNewList.contains(key)?"新品":"普通品");
         				row.createCell(38,Cell.CELL_TYPE_STRING).setCellValue(remark);
                    }
		    	}
		  }
		
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	response.setContentType("application/x-download");

	SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmm");

	String fileName = "PurchaseOrderData" + sdf.format(new Date()) + ".xls";
	fileName = URLEncoder.encode(fileName, "UTF-8");
	response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
	try {
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
		return null;
	}
	
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value =  "exp2" )
	public String exp2(LcPurchaseOrder purchaseOrder, HttpServletRequest request,String isCheck,String productIdColor,HttpServletResponse response, Model model) throws UnsupportedEncodingException, ParseException {

		SimpleDateFormat   sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		int excelNo =1;
		
		Map<String,Integer> packMap= this.productService.findPackQuantityMap();
		List<String> isNewList = productEliminateService.findIsNewProductName();
		Page<LcPurchaseOrder>	page= new Page<LcPurchaseOrder>(request, response);
		page.setPageSize(99999);
	    page=purchaseOrderService.findByProduct2(page, purchaseOrder, isCheck, productIdColor);
	    List<LcPurchaseOrder> list=page.getList();
	    if(list.size()>65535){
	    	throw new RuntimeException("一次导出的最大数据不能超过65535条！请分两次导出！");
	    }
	    
	    HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		String[] title = {"   PN   ", " PO NO. ", "PO DATE",  " MPQ ","VENDOR","CURRENCY","PO QTY","Delivered","Balance","PO L/T","First L/T","Delivery Status","Actual Delivery Date",
				" DE "," UK "," FR "," IT "," ES "," JP "," US "," CA "," DE "," UK "," FR "," IT "," ES "," JP "," US "," CA "," DE "," UK "," FR "," IT "," ES "," JP "," US "," CA ","Is New"," Remark "," Price "};
		
		  style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
		  style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		  style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  //设置Excel中的边框(表头的边框)
//		  //设置字体
		  HSSFFont font = wb.createFont();
		  font.setFontHeightInPoints((short) 11); // 字体高度
		  style.setFont(font);
		  row.setHeight((short) 400);
		  HSSFCell cell = null;		
		  for (int i = 0; i < title.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(title[i]);
				cell.setCellStyle(style);
				sheet.autoSizeColumn((short) i);
			}
		  
		 for(LcPurchaseOrder purchaseOrderTrue: list){
		    	List<LcPurchaseOrder> tempOrders=purchaseOrderTrue.getTempOrders();
		    	for(int i =0;i<tempOrders.size();i++){
		    		String deliveryStatus="";
    				LcPurchaseOrder tempOrder = tempOrders.get(i);
    				List<LcPurchaseOrderItem> orderItems = tempOrder.getItems();
    				String productName = "";
    				Date   deliveredDate=null;
//    				Date   compareDate=sdf1.parse(sdf1.format(new Date()));
    				Date   acDeliveryDate=null;
    				Map<String,String> itemQuantityMap  = Maps.newHashMap();
    				Set<Integer> orderItemSet = Sets.newHashSet();
    				Map<String,String>  colorMap = Maps.newHashMap();
    				String remark ="";
    				BigDecimal price=new BigDecimal(0);
                    for(int j =0;j<orderItems.size();j++){
                    	LcPurchaseOrderItem item = orderItems.get(j);
                    	if(j==0){
                    		productName=item.getProductName();
                    		deliveredDate=item.getDeliveryDate();//po交期
                    		acDeliveryDate = item.getActualDeliveryDate();
                    		//如果有收货完成时间，按收货完成时间
	           				 if(tempOrder.getOrderSta().equals("4")||tempOrder.getOrderSta().equals("5")){
//	           				 	compareDate=item.getActualDeliveryDate();
		           				 int nDay = (int) ((acDeliveryDate.getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
		 						 if(nDay>3){
		 							deliveryStatus="逾期";
		 						 }else if(nDay<-3){
		 							deliveryStatus="提前";
		     					 }else{
		     						deliveryStatus="正常";
		     					 }
	           				 }else{
	           					 if(deliveredDate.before(new Date())){//如果收货未完成且今天大于PO
	           						 int nDay = (int) ((new Date().getTime() - deliveredDate.getTime()) / (24 * 60 * 60 * 1000));
	    	 						 if(nDay>3){
	    	 							deliveryStatus="逾期";
	    	 						 }else if(nDay<-3){
	    	 							deliveryStatus="提前";
	    	     					 }else{
	    	     						deliveryStatus="正常";
	    	     					 }
	           					 }
	           				 }
                    	}
                    	String quantitys=item.getQuantityOrdered()+","+item.getQuantityReceived()+","+item.getQuantityUnReceived();
                    	itemQuantityMap.put(item.getCountryCode()+","+item.getColorCode(), quantitys);
                    	
                    	if(colorMap.get(item.getColorCode())!=null){
                    		String[] auqArr=colorMap.get(item.getColorCode()).split(",");
                    		quantitys=(item.getQuantityOrdered()+Integer.parseInt(auqArr[0]))+","+(item.getQuantityReceived()+Integer.parseInt(auqArr[1]))+","+(item.getQuantityUnReceived()+Integer.parseInt(auqArr[2]));
                    	}
                    	colorMap.put(item.getColorCode(), quantitys);
                    	if(StringUtils.isNotEmpty(item.getRemark())){
                    		remark= item.getRemark();
                    	}
                    	price=item.getItemPrice();
                    	orderItemSet.add(item.getId());
                    }
                  //根据下面颜色迭代行
                    for (Map.Entry<String, String> entry : colorMap.entrySet()) {
                    	String color = entry.getKey();
                    	 row = sheet.createRow(excelNo++);  //生成行
                    	 String productName_color="";
        				 String key = productName;
        				 if(StringUtils.isNotEmpty(color)){
        					 productName_color=productName+"_"+color;
        					 key = productName + "_" + color;
        				 }else{
        					 productName_color=productName;
        				 }
        				 
        				 row.createCell(0,Cell.CELL_TYPE_STRING).setCellValue(productName_color);
        				 row.createCell(1,Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getOrderNo());
        				 row.createCell(2,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format((Date)tempOrder.getCreateDate()));  //创建日期
        				 
        			
                         row.createCell(3,Cell.CELL_TYPE_NUMERIC).setCellValue(packMap.get(productName));
                         if (tempOrder.getSupplier() != null) {
                        	 row.createCell(4,Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getSupplier().getNikename());
						 } else {
							 row.createCell(4,Cell.CELL_TYPE_STRING).setCellValue("");
						 }
                         row.createCell(5,Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getCurrencyType());
                         
                         
                         String[] arr=entry.getValue().split(",");
                         row.createCell(6,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(arr[0]));
                         row.createCell(7,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(arr[1]));
                         row.createCell(8,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(arr[2]));  //未收货
                         row.createCell(9,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format((Date)deliveredDate));

//                         Date finishedDate =billService.getFinishedReceiveDate(orderItemSet);//最近一次收货时间
                         
        				 Date firstDate =billService.getFirstReceiveDate(orderItemSet);
        				 if(firstDate!=null){
        					row.createCell(10,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format(firstDate));//第一次收货日期 
        				 }else{
        					 row.createCell(10,Cell.CELL_TYPE_STRING).setCellValue("");
        				 }
        				 
        				
 						
 						row.createCell(11,Cell.CELL_TYPE_STRING).setCellValue(deliveryStatus);
        				
//        				 if(tempOrder.getItemsQuantityUnReceived()==0&&finishedDate!=null){  //如果没有收货的      说明都收货完成
 						 if(acDeliveryDate!=null){ 
        					 row.createCell(12,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format(acDeliveryDate));
        				 }else{
        					 row.createCell(12,Cell.CELL_TYPE_STRING).setCellValue("");//完成日期
        				 }
        				 
        				 if(itemQuantityMap.get("de,"+color)!=null){
         					String values = itemQuantityMap.get("de,"+color);
         					row.createCell(13,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(21,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(29,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(13,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(21,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(29,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("uk,"+color)!=null){
         					String values = itemQuantityMap.get("uk,"+color);
         					row.createCell(14,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(22,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(30,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(14,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(22,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(30,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("fr,"+color)!=null){
         					String values = itemQuantityMap.get("fr,"+color);
         					row.createCell(15,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(23,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(31,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(15,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(23,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(31,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("it,"+color)!=null){
         					String values = itemQuantityMap.get("it,"+color);
         					row.createCell(16,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(24,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(32,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(16,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(24,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(32,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("es,"+color)!=null){
         					String values = itemQuantityMap.get("es,"+color);
         					row.createCell(17,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(25,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(33,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(17,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(25,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(33,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("jp,"+color)!=null){
         					String values = itemQuantityMap.get("jp,"+color);
         					row.createCell(18,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(26,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(34,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(18,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(26,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(34,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("com,"+color)!=null){
         					String values = itemQuantityMap.get("com,"+color);
         					row.createCell(19,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(27,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(35,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(19,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(27,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(35,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				
         				if(itemQuantityMap.get("ca,"+color)!=null){
         					String values = itemQuantityMap.get("ca,"+color);
         					row.createCell(20,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[0]);
         					row.createCell(28,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[1].equals("0")?"":values.split(",")[1]);
         					row.createCell(36,Cell.CELL_TYPE_STRING).setCellValue(values.split(",")[2].equals("0")?"":values.split(",")[2]);
         				}else{
         					row.createCell(20,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(28,Cell.CELL_TYPE_STRING).setCellValue("");
         					row.createCell(36,Cell.CELL_TYPE_STRING).setCellValue("");
         				}
         				row.createCell(37,Cell.CELL_TYPE_STRING).setCellValue(isNewList.contains(key)?"新品":"普通品");
         				row.createCell(38,Cell.CELL_TYPE_STRING).setCellValue(remark);
         				if(price==null){
         					row.createCell(39,Cell.CELL_TYPE_STRING).setCellValue("");
         				}else{
         					row.createCell(39,Cell.CELL_TYPE_STRING).setCellValue(price.floatValue());
         				}
                    }
		    	}
		  }
		
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	response.setContentType("application/x-download");

	SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmm");

	String fileName = "PurchaseOrderPriceData" + sdf.format(new Date()) + ".xls";
	fileName = URLEncoder.encode(fileName, "UTF-8");
	response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
	try {
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
		return null;
	}
	
	
	
	//导出仓库数据报表
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value =  "expInventoryData" )
	public String expInventoryData(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException{
		//查询barcode表，获得所有产品及市场
		List<Object[]> list=this.purchaseOrderService.getProductInfos();
		//查询PO  Map
		Map<String,Integer> poMap=this.purchaseOrderService.getPoBalance();
		Map<String,Integer> cnHouseMap=this.purchaseOrderService.getLocalStock().get("21");
		Map<String,Integer> deHouseMap=this.purchaseOrderService.getLocalStock().get("19");
		Map<String,Integer> usaHouseMap=this.purchaseOrderService.getLocalStock().get("120");
		Map<String,Integer> fbaMap=this.purchaseOrderService.getFbaStock();
		Map<String,Integer> tranMap=this.purchaseOrderService.getTranQuantity();
		
        int excelNo=1;
        
	    if(list.size()>65535){
	    	throw new RuntimeException("一次导出的最大数据不能超过65535条！请分两次导出！");
	    }
	    
	    HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		String[] title = { " PN ", "  市场  ", " PO ", "中国仓","  在途    "," 德国仓  ", "  美国仓    ","    FBA     "};
		
		  style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
		  style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		  style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  //设置Excel中的边框(表头的边框)
		  style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		  
		  style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		  style.setBottomBorderColor(HSSFColor.BLACK.index);
		  
		  style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		  style.setLeftBorderColor(HSSFColor.BLACK.index);
		  
		  style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		  style.setRightBorderColor(HSSFColor.BLACK.index);
		  
		  style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		  style.setTopBorderColor(HSSFColor.BLACK.index);
		  //设置字体
		  HSSFFont font = wb.createFont();
		  font.setFontHeightInPoints((short) 16); // 字体高度
		  font.setFontName(" 黑体 "); // 字体
		  font.setBoldweight((short) 16);
		  style.setFont(font);
		  row.setHeight((short) 500);
		  HSSFCell cell = null;		
		  for (int i = 0; i < title.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(title[i]);
				cell.setCellStyle(style);
				sheet.autoSizeColumn((short) i);
			}
		  
		// 输出亚马逊excel订单
		 for(Object[] object: list){
			String key=object[0]+","+object[1]+","+object[2];
    		row = sheet.createRow(excelNo++);
    		String productNameColor="";
    		String arr[]=object[0].toString().split(" ");
    		if(StringUtils.isNotEmpty(object[2].toString())){
    			if(arr.length>2){
    				productNameColor=object[0].toString().substring(arr[0].length()+1)+"_"+object[2];
    			}else{
    				productNameColor=arr[1]+"_"+object[2];
    			}
    		}else{ 
    			if(arr.length>2){
    				productNameColor=object[0].toString().substring(arr[0].length()+1);
    			}else{
    				productNameColor=arr[1].toString();
    			}
    		}
			row.createCell(0,Cell.CELL_TYPE_STRING).setCellValue(productNameColor); 
			row.createCell(1,Cell.CELL_TYPE_STRING).setCellValue(object[1].toString().equals("com")?"us":object[1].toString());
			row.createCell(2,Cell.CELL_TYPE_STRING).setCellValue(poMap.get(key)==null?0:poMap.get(key));
			row.createCell(3,Cell.CELL_TYPE_STRING).setCellValue(cnHouseMap.get(key)==null?0:cnHouseMap.get(key));
			row.createCell(4,Cell.CELL_TYPE_STRING).setCellValue(tranMap.get(key)==null?0:tranMap.get(key));
			row.createCell(5,Cell.CELL_TYPE_STRING).setCellValue(deHouseMap.get(key)==null?0:deHouseMap.get(key));
			row.createCell(6,Cell.CELL_TYPE_STRING).setCellValue(usaHouseMap.get(key)==null?0:usaHouseMap.get(key));
			row.createCell(7,Cell.CELL_TYPE_STRING).setCellValue(fbaMap.get(key)==null?0:fbaMap.get(key));
		}
			    				 
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");

		SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmm");
		String fileName = "PurchasingForecast" + sdf.format(new Date()) + ".xls";
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
		try {
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value ="marketExport")
	public String marketExport(LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Date today = new Date();
		today.setHours(0);
		today.setSeconds(0);
		today.setMinutes(0);
		if (purchaseOrder.getCreateDate() == null) {
			purchaseOrder.setCreateDate(DateUtils.addMonths(today, -6));
		}
		List<Object[]> list = purchaseOrderService.getProductAmountByByCountry(purchaseOrder); 
        List<String> title=Lists.newArrayList("商品","DE","UK", "FR","IT","ES","US","JP","CA","下单日期","出货日期","跟进状态");
        HSSFWorkbook wb = new HSSFWorkbook();
      		HSSFSheet sheet = wb.createSheet();
      		HSSFRow row = sheet.createRow(0);
      		HSSFCellStyle style = wb.createCellStyle();
      		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      		style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
      		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
      		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
      		style.setBottomBorderColor(HSSFColor.BLACK.index);
      		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
      		style.setLeftBorderColor(HSSFColor.BLACK.index);
      		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
      		style.setRightBorderColor(HSSFColor.BLACK.index);
      		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
      		style.setTopBorderColor(HSSFColor.BLACK.index);
      		HSSFFont font = wb.createFont();
      		font.setFontHeightInPoints((short) 16); // 字体高度
      		font.setFontName(" 黑体 "); // 字体
      		font.setBoldweight((short) 16);
      		style.setFont(font);
      		
	      	  CellStyle contentStyle = wb.createCellStyle();
			  contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			  contentStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			  contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			  contentStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			  contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			  contentStyle.setRightBorderColor(HSSFColor.BLACK.index);
			  contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			  contentStyle.setTopBorderColor(HSSFColor.BLACK.index);
      		row.setHeight((short) 600);
      		HSSFCell cell = null;		
      		for (int i = 0; i < title.size(); i++) {
 				cell = row.createCell(i);
 				cell.setCellValue(title.get(i));
 				cell.setCellStyle(style);
 				sheet.autoSizeColumn((short) i);
 			}
      		if(list!=null){
      			for(int i=0;i<list.size();i++){
      				Object[] object=list.get(i);
      				row=sheet.createRow(i+1);
    				int j=0;
    				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(object[0].toString());
    				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(object[1].toString()));
    				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(object[2].toString()));
    				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(object[3].toString()));
    				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(object[4].toString()));
    				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(object[5].toString()));
    				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(object[6].toString()));
    				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(object[7].toString()));
    				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(object[8].toString()));
    				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format((Date)object[9]));
    				if(object[10]!=null){
    					row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format((Date)object[10]));
    				}else{
    					row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue("");
    				}
    				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(object[11].toString());
      			}
      			
	      			for (int i=0;i<list.size();i++) {
			        	 for (int j = 0; j < title.size(); j++) {
				        	  sheet.getRow(i+1).getCell(j).setCellStyle(contentStyle);
						 }
		            }
			
					for (int i = 0; i < title.size(); i++) {
				       sheet.autoSizeColumn((short)i);
							
					}
      		}
      		try {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/x-download");
		
				SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmm");
		
				String fileName = "byCountry" + sdf.format(new Date()) + ".xls";
				fileName = URLEncoder.encode(fileName, "UTF-8");
				response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
				OutputStream out = response.getOutputStream();
				wb.write(out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	
	/**
	 *付款明细导出
	 * 
	 */
	@RequestMapping(value ="payExport")
	public String payExport(LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Date today = new Date();
		today.setHours(0);
		today.setSeconds(0);
		today.setMinutes(0);
		if (purchaseOrder.getCreateDate() == null) {
			purchaseOrder.setCreateDate(DateUtils.addMonths(today, -6));
		}
		DecimalFormat df = new DecimalFormat("#.##");
		List<LcPurchaseOrder> list = purchaseOrderService.exp(purchaseOrder); 
        List<String> title=Lists.newArrayList("订单编号","金额","状态", "付款单号","付款类型","付款金额","入库状态","收到货税金","收到货总价","提单号","供应商","订单创建人");
        HSSFWorkbook wb = new HSSFWorkbook();
      		HSSFSheet sheet = wb.createSheet();
      		HSSFRow row = sheet.createRow(0);
      		HSSFCellStyle style = wb.createCellStyle();
      		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      		style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
      		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
      		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
      		style.setBottomBorderColor(HSSFColor.BLACK.index);
      		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
      		style.setLeftBorderColor(HSSFColor.BLACK.index);
      		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
      		style.setRightBorderColor(HSSFColor.BLACK.index);
      		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
      		style.setTopBorderColor(HSSFColor.BLACK.index);
      		HSSFFont font = wb.createFont();
      		font.setFontHeightInPoints((short) 16); // 字体高度
      		font.setFontName(" 黑体 "); // 字体
      		font.setBoldweight((short) 16);
      		style.setFont(font);
      		
      	    CellStyle contentStyle = wb.createCellStyle();
		    contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		    contentStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		    contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		    contentStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		    contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		    contentStyle.setRightBorderColor(HSSFColor.BLACK.index);
		    contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		    contentStyle.setTopBorderColor(HSSFColor.BLACK.index);
      		row.setHeight((short) 600);
      		HSSFCell cell = null;
      		int  rowIndex=1;
      		for (int i = 0; i < title.size(); i++) {
 				cell = row.createCell(i);
 				cell.setCellValue(title.get(i));
 				cell.setCellStyle(style);
 				sheet.autoSizeColumn((short) i);
 			}
      		if(list!=null){
      			for(int i=0;i<list.size();i++){
      				LcPurchaseOrder order=list.get(i);
      				//查找付款单
      				Map<String,LcPurchasePayment> payMap= order.getPaymentsMap();
  					String orderSta=order.getOrderSta();
  					String inInventorySta="未收货";
    				if("3".equals(orderSta)){
    					inInventorySta="部分收货";
    				}else if("4".equals(orderSta)||"5".equals(orderSta)){
    					inInventorySta="已收货";
    				}
    				StringBuilder  ladingNos=new StringBuilder();
    				Map<String,LcPsiLadingBill> ladingMap =order.getLadingsMap();
    			    for(String ladingNo : ladingMap.keySet()){
    			    	ladingNos.append(ladingNo).append(",");
    			    }
    			    if(ladingNos.length()>0){
    			    	ladingNos=new StringBuilder(ladingNos.substring(0, ladingNos.length()-1));
    			    }
    			    BigDecimal receivedQuantityAmount = order.getReceivedQuantityAmount().divide(new BigDecimal("1.17"),2, BigDecimal.ROUND_HALF_EVEN);
    			    
    			    String supplierName = "("+order.getSupplier().getNikename()+")"+order.getSupplier().getName();
    			    String createName=order.getCreateUser().getName();
      				if(payMap.size()>0){
      					Set<Integer> itemIds = Sets.newHashSet();
      					for(LcPurchaseOrderItem item:order.getItems()){
      						itemIds.add(item.getId());
      					}
      					
      					for(Map.Entry<String,LcPurchasePayment> entry:payMap.entrySet()){
      						String payNo = entry.getKey();
      						LcPurchasePayment payment = entry.getValue();
      						if(!"2".equals(payment.getPaymentSta())){
          						continue;
          					}
      						row=sheet.createRow(rowIndex++);
          					StringBuilder payType= new StringBuilder();
          					int j=0;
          					BigDecimal payTotal = new BigDecimal(0);
            			    for(LcPurchasePaymentItem payItem : payment.getItems()){
            			    	
            			    	if(payType.indexOf(payItem.getTypeName())<0){
            			    		payType.append(payItem.getTypeName()).append(",");
            			    	}
            			    	//算出本采购订单，付款的金额
            			    	if("定金".equals(payItem.getTypeName())&&payItem.getBillNo().equals(order.getOrderNo())){
            			    		payTotal=payTotal.add(payItem.getPaymentAmount());
            			    		//如果是尾款，必须这个提单item是在本订单item里面的，才算尾款
            			    	}else if("尾款".equals(payItem.getTypeName())&&itemIds.contains(payItem.getLadingBillItem().getPurchaseOrderItem().getId())){
            			    		payTotal=payTotal.add(payItem.getPaymentAmount());
            			    	}
            			    }
            			    
            			    if(payType.length()>0){
            			    	payType=new StringBuilder(payType.substring(0, payType.length()-1));
            			    }
            			    
          					//订单编号","金额","状态", "付款单号","入库状态","提单号");
            				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(order.getOrderNo());
            				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Float.parseFloat(df.format(order.getTotalAmount())));
            				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(order.getStatusName());
            				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(payNo);
            				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(payType.toString());
            				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Float.parseFloat(df.format(payTotal.floatValue())));
            			    row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(inInventorySta);
            			    row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Float.parseFloat(df.format(receivedQuantityAmount.multiply(new BigDecimal("0.17")))));
            			    row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Float.parseFloat(df.format(receivedQuantityAmount)));
            				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(ladingNos.toString());
            			    row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(supplierName);
            			    row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(createName);
              			}
      				}else{
      					row=sheet.createRow(rowIndex++);
      					int j=0;
      					row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(order.getOrderNo());
        				row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Float.parseFloat(df.format(order.getTotalAmount())));
        				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(order.getStatusName());
        				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue("");
        				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue("");
        				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue("");
        			    row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(inInventorySta);
        			    row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Float.parseFloat(df.format(receivedQuantityAmount.multiply(new BigDecimal("0.17")))));
        			    row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(Float.parseFloat(df.format(receivedQuantityAmount)));
        				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(ladingNos.toString());
        				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(supplierName);
        				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(createName);
      				}
      			}
      			
      		}
      		try {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/x-download");
		
				SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmm");
		
				String fileName = "purchaseOrderPayDetail" + sdf.format(new Date()) + ".xls";
				fileName = URLEncoder.encode(fileName, "UTF-8");
				response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
				OutputStream out = response.getOutputStream();
				wb.write(out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	
	/**
	 *对账明细
	 * @throws ParseException 
	 * 
	 */
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value ="reconciliation")
	public String reconciliation(LcPurchaseOrder purchaseOrder,HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		List<PsiSupplier> suppliers=this.psiSupplierService.findAll();
		model.addAttribute("suppliers", suppliers);
        Page<LcPurchaseOrder> page = new Page<LcPurchaseOrder>(request, response);
        page.setPageSize(10000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(purchaseOrder.getCreateDate() == null) {
			purchaseOrder.setCreateDate(sdf.parse("2015-01-01"));
		}
        purchaseOrderService.findReconciliation(page, purchaseOrder);
        model.addAttribute("purchaseOrder", purchaseOrder);
        model.addAttribute("page", page);
        String mobilCss="";
		if(this.isMobileDevice(request)){
			mobilCss="width: 700px; overflow-x: scroll";
		}
		model.addAttribute("mobilCss", mobilCss);
		return "modules/psi/lc/lcPurchaseReconciliation";
	}
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value ="accountBalance")
	public String accountBalance(LcPurchaseOrder purchaseOrder,HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		List<Object[]> accounts=purchaseOrderService.findAccountBalance();
		Map<String,Object[]> accountMap = Maps.newHashMap();
		for(Object[] obj:accounts){
			accountMap.put(obj[0].toString(), obj);
		}
		model.addAttribute("accountMap", accountMap);
		return "modules/psi/lc/accountBalance";
	}
	
	
	/**
	 *采购支付报表
	 * @throws ParseException 
	 * 
	 */
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value ="fReport")
	public String fReport(LcPurchaseOrder purchaseOrder,String startMonth,String endMonth, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		if(StringUtils.isEmpty(startMonth)){
			endMonth=sdf.format(new Date());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			startMonth=calendar.get(Calendar.YEAR)+"01";
		}
		List<PsiSupplier> suppliers=this.psiSupplierService.findAll();
		List<LcPurchaseFinancialDto> financialDtos=this.purchaseFinancialService.getFinancicalReport(purchaseOrder,"201501",endMonth);
		if(financialDtos!=null&&financialDtos.size()>1){
			String []  monthArr = purchaseOrder.getDelFlag().split(",");
			if(monthArr!=null&&monthArr.length>1){
				Map<String,LcPurchaseFinancialDto> map = Maps.newHashMap();
				for(LcPurchaseFinancialDto dto:financialDtos){
					map.put(dto.getMonth(), dto);
				}
				financialDtos=Lists.newArrayList();
				Float amountTemp =0f;
				List<String> months = DateUtils.getContineMonthList(monthArr[0], monthArr[1]);
				for(String month:months){
					LcPurchaseFinancialDto dto=new LcPurchaseFinancialDto();
					if(map.get(month)!=null){
						dto =map.get(month);
						if(month.equals(monthArr[0])){
							amountTemp=dto.getOrderAmount()-dto.getAllPayment();
						}else{
							amountTemp=amountTemp+(dto.getOrderAmount()-dto.getAllPayment());
						}
						dto.setUpPayAmount(amountTemp);
					}else{
						dto.setMonth(month);
						dto.setOrderAmount(0f);
						dto.setPayLadingAmount(0f);
						dto.setPayOrderAmount(0f);
						dto.setUpPayAmount(amountTemp);
					}
					if(Integer.parseInt(month)>=Integer.parseInt(startMonth)){
						financialDtos.add(dto);
					}
					
				}
			}
		}else if(financialDtos!=null&&financialDtos.size()==1){
			LcPurchaseFinancialDto dto=financialDtos.get(0);
			dto.setUpPayAmount(dto.getOrderAmount()-dto.getAllPayment());
		}
		
		Map<String,Set<Integer>> proSupplierMap =purchaseFinancialService.getProductSupplierMap();
		
		String supplierDisabled="0";
		String productDisabled="0";
		if(StringUtils.isNotEmpty(purchaseOrder.getOrderNo())){
			//如果产品名不为空
			supplierDisabled="1";
		}
		
		if(purchaseOrder.getSupplier()!=null&&purchaseOrder.getSupplier().getId()!=null){
			productDisabled="1";
		}
		
		//如果一个产品有多个供应商，供应商选择框不封闭
		if(StringUtils.isNotEmpty(purchaseOrder.getOrderNo())&&proSupplierMap.get(purchaseOrder.getOrderNo())!=null&&proSupplierMap.get(purchaseOrder.getOrderNo()).size()>1){
			supplierDisabled="0";
			List<PsiSupplier> supplierTemp =Lists.newArrayList();
			for(PsiSupplier supplier:suppliers){
				if(proSupplierMap.get(purchaseOrder.getOrderNo()).contains(supplier.getId())){
					supplierTemp.add(supplier);
				}
			}
			suppliers = supplierTemp;
		}
		
		String mobilCss="";
		if(this.isMobileDevice(request)){
			mobilCss="width: 700px; overflow-x: scroll";
		}
		model.addAttribute("mobilCss", mobilCss);
		
		model.addAttribute("supplierDisabled", supplierDisabled);
		model.addAttribute("productDisabled", productDisabled);
		model.addAttribute("proNameColors", proSupplierMap.keySet());
		model.addAttribute("financialDtos", financialDtos);
		model.addAttribute("suppliers", suppliers);
		model.addAttribute("startMonth", startMonth);
		model.addAttribute("endMonth", endMonth);
		return "modules/psi/lc/lcFinancialReport";
	}
	
	/**
	 * 未来12周资金预算(资金分配计划)
	 * @throws ParseException 
	 * 
	 */
	@RequestMapping(value ="capitalBudget")
	public String capitalBudget(Integer supplierId,LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		DateFormat formatWeek = new SimpleDateFormat("yyyy-ww");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		List<String> weeks = Lists.newArrayList();
		Map<String,String> weekStartMap = Maps.newHashMap();
		Map<Integer,PsiSupplier> supplierMap = Maps.newHashMap();
		Map<Integer,PsiSupplier> allSupplierMap=this.psiSupplierService.findAllMap();  
		//从提单,获取未确认收货，但是已经付款
	    Map<Integer,Float>  payNoReceivedMap= billService.getPayPreLadingItem(supplierId);
	    //从采购订单,未收货产品(尾款)金额
		Map<Integer,Map<String,Float>> orderMap= billService.getUnReceivedAmount(supplierId,allSupplierMap,payNoReceivedMap);
		//从采购订单,未收货产品(定金)金额
		Map<Integer,Float> depositMap=billService.getUnpayDepositAmount(supplierId);  
		//从提单,获取已确认收货未付款
		Map<Integer,Map<String,Float>>  ladingMap= billService.getUnPaymentLadingItem(supplierId,allSupplierMap,depositMap);
	
		//组装未来12周数据
		for(int i=0;i<12;i++){
			String weekStr=DateUtils.getWeekStr(DateUtils.addDays(today,i*7), formatWeek, 5, "-");
			weeks.add(weekStr);
			weekStartMap.put(weekStr, sdf.format(DateUtils.getFirstDayOfWeek(Integer.parseInt(weekStr.substring(0, 4)), Integer.parseInt(weekStr.substring(5))))+"("+Integer.parseInt(weekStr.substring(5))+"W)");
		}
		for(Integer suId:ladingMap.keySet()){
			supplierMap.put(suId, allSupplierMap.get(suId));
		}
		for(Integer suId:orderMap.keySet()){
			if(supplierMap.get(suId)==null){
				supplierMap.put(suId, allSupplierMap.get(suId));
			}
		}
		
		
		//如果是手机版屏幕缩小
		String mobilCss="";
		if(this.isMobileDevice(request)){
			mobilCss="width: 700px; overflow-x: scroll";
		}
		model.addAttribute("mobilCss", mobilCss);
		
		//普通跟单员只能看到自己负责的产品
		if(!UserUtils.hasPermission("psi:purchaseOrder:accounts")){
			List<Integer> supplierIds = this.productService.getSupplierIdByUser(UserUtils.getUser().getId());
			if(supplierIds.size()>0){
				Map<Integer,PsiSupplier> tempAllSupplierMap = Maps.newHashMap();
				for(Map.Entry<Integer,PsiSupplier> entry:allSupplierMap.entrySet()){
					if(supplierIds.contains(entry.getKey())){
						tempAllSupplierMap.put(entry.getKey(), entry.getValue());
					}
				}
				allSupplierMap = tempAllSupplierMap;
				
				Map<Integer,PsiSupplier> tempSupplierMap = Maps.newHashMap();   
				for(Map.Entry<Integer,PsiSupplier> entry:supplierMap.entrySet()){
					if(supplierIds.contains(entry.getKey())){
						tempSupplierMap.put(entry.getKey(), entry.getValue());
					}
				}
				supplierMap = tempSupplierMap;
			}
		}
		
		model.addAttribute("allSupplierMap", allSupplierMap);
		model.addAttribute("supplierMap", supplierMap);
		model.addAttribute("ladingMap", ladingMap);
		model.addAttribute("orderMap", orderMap);
		model.addAttribute("weeks", weeks);
		model.addAttribute("supplierId", supplierId);
		model.addAttribute("weekStartMap", weekStartMap);
		return "modules/psi/lc/lcCapitalBudget";
	}
	
	//逾期付款详细
	@RequestMapping(value ="overDetail")
	public String overDetail(Integer supplierId,LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		//往前推四个月，超出四个月的算到第四个月
		List<String> months = Lists.newArrayList();
		for(int i =0;i<4;i++){
			months.add(df.format(DateUtils.addMonths(new Date(), i-3)));
			if(i==0){
				model.addAttribute("firstMonth", df.format(DateUtils.addMonths(new Date(), i-3)));
			}
		}
		Map<Integer,PsiSupplier> allSupplierMap   = this.psiSupplierService.findAllMap();
		//定金未付(生产状态定金未付即算逾期)
		Map<Integer,Map<String,Float>> depositMap = billService.getUnpayDepositAmountOver();
		Map<Integer,Map<String,Float>>  overMap   = billService.getUnPaymentLadingItemOver(allSupplierMap,depositMap);
		String mobilCss="";
		if(this.isMobileDevice(request)){
			mobilCss="width: 700px; overflow-x: scroll";
		}
		
		if(!UserUtils.hasPermission("psi:purchaseOrder:accounts")){
			List<Integer> supplierIds = this.productService.getSupplierIdByUser(UserUtils.getUser().getId());
			if(supplierIds.size()>0){
				Map<Integer,PsiSupplier> tempAllSupplierMap = Maps.newHashMap();
				for(Map.Entry<Integer,PsiSupplier> entry:allSupplierMap.entrySet()){
					if(supplierIds.contains(entry.getKey())){
						tempAllSupplierMap.put(entry.getKey(), entry.getValue());
					}
				}
				allSupplierMap = tempAllSupplierMap;
				
				Map<Integer,Map<String,Float>> tempOverMap = Maps.newHashMap();
				for(Map.Entry<Integer,Map<String,Float>> entry:overMap.entrySet()){
					if(supplierIds.contains(entry.getKey())){
						tempOverMap.put(entry.getKey(), entry.getValue());
					}
				}
				overMap = tempOverMap;
			}
		}
		
		model.addAttribute("mobilCss", mobilCss);
		model.addAttribute("allSupplierMap", allSupplierMap);
		model.addAttribute("months", months);
		model.addAttribute("overMap", overMap);
		model.addAttribute("supplierId", supplierId);
		return "modules/psi/lc/lcCapitalBudgetOver";
	}
	
	//逾期付款提单详细
	@RequestMapping(value ="overLadDetail")
	public String overLadDetail(Integer supplierId, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		//从采购订单,未收货产品(定金)金额
		Map<Integer,Float> depositMap=billService.getUnpayDepositAmount(supplierId);
		Map<Integer,PsiSupplier> allSupplierMap = this.psiSupplierService.findAllMap();
		List<Object[]> overList = billService.findOverdue(allSupplierMap.get(supplierId));
		model.addAttribute("allSupplierMap", allSupplierMap);
		model.addAttribute("overList", overList);
		model.addAttribute("supplierId", supplierId);
		model.addAttribute("depositMap", depositMap);
		return "modules/psi/lc/lcLadOver";
	}
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value ="fReportExport")
	public String fReportExport(LcPurchaseOrder purchaseOrder,String startMonth,String endMonth, HttpServletRequest request, HttpServletResponse response,	Model model) throws UnsupportedEncodingException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		if(StringUtils.isEmpty(startMonth)){
			endMonth=sdf.format(new Date());
			startMonth=sdf.format(DateUtils.addMonths(new Date(), -12));
		}
		
		List<PsiSupplier> suppliers = this.psiSupplierService.findSupplierByType(new String[]{"0","1","2"});
		Map<Integer,String> suppMap = Maps.newHashMap();
		for(PsiSupplier supplier:suppliers){
			suppMap.put(supplier.getId(), supplier.getNikename());
		}
		
		
		StringBuilder titleStr=new StringBuilder("供应商");
		Map<Integer,Map<String,Float>> resMap=this.purchaseFinancialService.getFinancicalReportExport(purchaseOrder,startMonth,endMonth);
		if(resMap!=null&&resMap.size()>0){
				for(String month:DateUtils.getContineMonthList(startMonth,endMonth)){
					titleStr.append(",").append(month);
			}
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 16); // 字体高度
		font.setFontName(" 黑体 "); // 字体
		font.setBoldweight((short) 16);
		style.setFont(font);

		CellStyle contentStyle = wb.createCellStyle();
		contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		contentStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		contentStyle.setRightBorderColor(HSSFColor.BLACK.index);
		contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		contentStyle.setTopBorderColor(HSSFColor.BLACK.index);

		String[] title = titleStr.toString().split(",");

		row.setHeight((short) 600);
		HSSFCell cell = null;
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn((short) i);
		}
		
		int rowIndex=1;
		if(resMap!=null&&resMap.size()>0){
			for(Map.Entry<Integer,Map<String,Float>> entry :resMap.entrySet()){
				Integer supplierId = entry.getKey();
				Map<String,Float> monthMap = resMap.get(supplierId);
				int j=0;
				row=sheet.createRow(rowIndex++);
	    		row.createCell(j++, Cell.CELL_TYPE_STRING).setCellValue(suppMap.get(supplierId));
	    		for(int m=1;m<title.length;m++){
	    			Float f =0f;
	    			if(monthMap.get(title[m])!=null){
	    				f=monthMap.get(title[m]);
	    			};
	    			if(f.intValue()==0){
	    				j++;
	    			}else{
	    				 BigDecimal   b  =   new BigDecimal(f);  
	    				 float   f1   =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();  
	    				row.createCell(j++, Cell.CELL_TYPE_NUMERIC).setCellValue(f1);
	    			}
	    			
	    		}
			}
		}
		
		int rowTotal = 0;
		for (int i = 0; i < rowTotal; i++) {
			for (int j = 0; j < title.length; j++) {
				sheet.getRow(i + 1).getCell(j).setCellStyle(contentStyle);
			}
		}
		for (int i = 0; i < title.length; i++) {
			sheet.autoSizeColumn((short) i);
		}

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MMddhhmm");
		String fileName = "PurchaseOrderUnReceivedData" + sdf1.format(new Date()) + ".xls";
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		try {
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 *采购财务报表
	 * @throws ParseException 
	 * 
	 */
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value ="forecast")
	public String forecast(LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		List<PsiSupplier> suppliers=this.psiSupplierService.findAll();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(new Date());
		if(Integer.parseInt(dateStr.substring(6))>25){
			dateStr=sdf.format(DateUtils.addDays(new Date(), 10));
		}
		
		List<PurchaseForecastDto> forecastDtos=this.purchaseForecastService.getForecastReport(purchaseOrder,dateStr.substring(0, 6));
		Map<String,Set<Integer>> proSupplierMap =purchaseFinancialService.getProductSupplierMap();
		
		String supplierDisabled="0";
		String productDisabled="0";
		if(StringUtils.isNotEmpty(purchaseOrder.getOrderNo())){
			//如果产品名不为空
			supplierDisabled="1";
		}
		
		if(purchaseOrder.getSupplier()!=null&&purchaseOrder.getSupplier().getId()!=null){
			productDisabled="1";
		}
		
		//如果一个产品有多个供应商，供应商选择框不封闭
		if(StringUtils.isNotEmpty(purchaseOrder.getOrderNo())&&proSupplierMap.get(purchaseOrder.getOrderNo())!=null&&proSupplierMap.get(purchaseOrder.getOrderNo()).size()>1){
			supplierDisabled="0";
			List<PsiSupplier> supplierTemp =Lists.newArrayList();
			for(PsiSupplier supplier:suppliers){
				if(proSupplierMap.get(purchaseOrder.getOrderNo()).contains(supplier.getId())){
					supplierTemp.add(supplier);
				}
			}
			suppliers = supplierTemp;
		}
		
		model.addAttribute("supplierDisabled", supplierDisabled);
		model.addAttribute("productDisabled", productDisabled);
		model.addAttribute("proNameColors", proSupplierMap.keySet());
		model.addAttribute("forecastDtos", forecastDtos);
		model.addAttribute("suppliers", suppliers);
		return "modules/psi/lc/lcPurchaseForecastReport";
	}
	
	@RequestMapping(value ="reconciliation2")
	public String reconciliation2(LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Date today = new Date();
		today.setHours(0);
		today.setSeconds(0);
		today.setMinutes(0);
		if(purchaseOrder.getReceiveFinishedDate()==null){
			//purchaseOrder.setReceiveFinishedDate(DateUtils.getFirstDayOfMonth(today));
			purchaseOrder.setReceiveFinishedDate(null);
		}
		if (purchaseOrder.getPurchaseDate()== null) {
			purchaseOrder.setPurchaseDate(DateUtils.getLastDayOfMonth(today));
		}
		List<PsiSupplier> suppliers=this.psiSupplierService.findAll();
		model.addAttribute("suppliers", suppliers);
		List<PsiSupplier> page = psiSupplierService.findAll(purchaseOrder);
		List<PsiSupplier> fundPlanList=new ArrayList<PsiSupplier>();
		for (PsiSupplier psiSupplier : page) {
			if(purchaseOrderService.getUnReceiving(purchaseOrder).get(psiSupplier.getId())!=null||purchaseOrderService.getUnPayFinal(purchaseOrder).get(psiSupplier.getId())!=null||purchaseOrderService.getUnPayDeposit(purchaseOrder).get(psiSupplier.getId())!=null){
				fundPlanList.add(psiSupplier);
			}
		}
		model.addAttribute("unReceiving", purchaseOrderService.getUnReceiving(purchaseOrder));
		model.addAttribute("unPayFinal", purchaseOrderService.getUnPayFinal(purchaseOrder));
		model.addAttribute("unPayDeposit", purchaseOrderService.getUnPayDeposit(purchaseOrder));
		model.addAttribute("page", fundPlanList);
		return "modules/psi/lc/lcPurchaseReconciliationBytime";
	}
	
	@RequestMapping(value ="reconciliationExport")
	public String reconciliationExport(LcPurchaseOrder purchaseOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		//Page<Object[]> page  = purchaseOrderService.findReconciliation2(new Page<Object[]>(request, response,-1),purchaseOrder); 
		List<PsiSupplier> fundPlanList = psiSupplierService.findAll(purchaseOrder);
		List<PsiSupplier> list=new ArrayList<PsiSupplier>();
		for (PsiSupplier psiSupplier : fundPlanList) {
			if(purchaseOrderService.getUnReceiving(purchaseOrder).get(psiSupplier.getId())!=null||purchaseOrderService.getUnPayFinal(purchaseOrder).get(psiSupplier.getId())!=null||purchaseOrderService.getUnPayDeposit(purchaseOrder).get(psiSupplier.getId())!=null){
				list.add(psiSupplier);
			}
		}
        HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
		  style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		  style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		  style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		  style.setBottomBorderColor(HSSFColor.BLACK.index);
		  style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		  style.setLeftBorderColor(HSSFColor.BLACK.index);
		  style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		  style.setRightBorderColor(HSSFColor.BLACK.index);
		  style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		  style.setTopBorderColor(HSSFColor.BLACK.index);
		  HSSFFont font = wb.createFont();
		  font.setFontHeightInPoints((short) 16); // 字体高度
		  font.setFontName(" 黑体 "); // 字体
		  font.setBoldweight((short) 16);
		  style.setFont(font);
		  row.setHeight((short) 600);
		  
		  CellStyle contentStyle = wb.createCellStyle();
		  contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		  contentStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		  contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		  contentStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		  contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		  contentStyle.setRightBorderColor(HSSFColor.BLACK.index);
		  contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		  contentStyle.setTopBorderColor(HSSFColor.BLACK.index);
		  
		  CellStyle titleStyle = wb.createCellStyle();
		  titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		  HSSFFont font1 = wb.createFont();
		  font1.setFontHeightInPoints((short) 15); // 字体高度
		  font1.setFontName(" 黑体 "); // 字体
		  font1.setBoldweight((short) 15);
		  titleStyle.setFont(font1);

		  HSSFCell cell = null;		
		  List<String> title=Lists.newArrayList("序号","","供应商", "待付定金额",  "待付尾款金额（已收货）","待付尾款金额（未收货）");
		  for (int i = 0; i < title.size(); i++) {
				cell = row.createCell(i);
				cell.setCellValue(title.get(i));
				cell.setCellStyle(style);
				sheet.autoSizeColumn((short) i);
			}
			Map<Integer, Double> unReceiving =purchaseOrderService.getUnReceiving(purchaseOrder);
			Map<Integer, Double> unPayFinal=purchaseOrderService.getUnPayFinal(purchaseOrder);
			Map<Integer, Double> unPayDeposit=purchaseOrderService.getUnPayDeposit(purchaseOrder);
			
		 List<Integer> rowIndex=new ArrayList<Integer>();	
		 if(list!=null){
			int rowCount=1;
			for (int i=0;i<list.size();i++) {
				PsiSupplier object=list.get(i);
				row=sheet.createRow(rowCount++);
				rowIndex.add(rowCount-1);
				row.setHeight((short) 500);
				int j=0;
				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(i+1);
				row.getCell(j-1).setCellStyle(style);
				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue("");
				row.getCell(j-1).setCellStyle(style);
				row.createCell(j++,Cell.CELL_TYPE_STRING).setCellValue(object.getNikename());
				row.getCell(j-1).setCellStyle(style);
			    if(unPayDeposit.get(object.getId())!=null){
					row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(unPayDeposit.get(object.getId()));
				}else{
					row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue("");
				}
			    row.getCell(j-1).setCellStyle(style);
			    if(unPayFinal.get(object.getId())!=null){
					row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(unPayFinal.get(object.getId()));
				}else{
					row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue("");
				}
			    row.getCell(j-1).setCellStyle(style);
			    if(unReceiving.get(object.getId())!=null){
					row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue(unReceiving.get(object.getId()));
				}else{
					row.createCell(j++,Cell.CELL_TYPE_NUMERIC).setCellValue("");
				}
			    row.getCell(j-1).setCellStyle(style);
				LcPurchaseOrder queryOrder=new LcPurchaseOrder();
				PsiSupplier  supplier=new PsiSupplier();
				supplier.setId(object.getId());
				queryOrder.setSupplier(supplier);
				queryOrder.setPurchaseDate(purchaseOrder.getPurchaseDate());
				queryOrder.setReceiveFinishedDate(purchaseOrder.getReceiveFinishedDate());
				List<LcPurchaseOrder> orderList=purchaseOrderService.findPurchaseOrder(queryOrder);
				if(orderList!=null){
					for(int k=0;k<orderList.size();k++){
						if(k==0){
							int y=0;
							row=sheet.createRow(rowCount++);
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("采购单号");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("采购总金额");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("定金比例");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("已付定金");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("未付定金");
						}
						int m=0;
						LcPurchaseOrder order1=orderList.get(k);
						row=sheet.createRow(rowCount++);
						row.createCell(m++,Cell.CELL_TYPE_STRING).setCellValue(order1.getOrderNo());
						row.createCell(m++,Cell.CELL_TYPE_STRING).setCellValue("");
						BigDecimal totalAmount=order1.getTotalAmount();
						BigDecimal depositAmount=order1.getDepositAmount();
						if("CNY".equals(order1.getCurrencyType())){
							totalAmount=totalAmount.divide(new BigDecimal(AmazonProduct2Service.getRateConfig().get("USD/CNY")));
							depositAmount=depositAmount.divide(new BigDecimal(AmazonProduct2Service.getRateConfig().get("USD/CNY")));
						}
						row.createCell(m++,Cell.CELL_TYPE_NUMERIC).setCellValue(totalAmount.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
						row.createCell(m++,Cell.CELL_TYPE_NUMERIC).setCellValue(order1.getDeposit());
						row.createCell(m++,Cell.CELL_TYPE_NUMERIC).setCellValue(depositAmount.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
						//row.createCell(m++,Cell.CELL_TYPE_NUMERIC).setCellValue(new BigDecimal((totalAmount*order1.getDeposit()-depositAmount)/100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
						row.createCell(m++,Cell.CELL_TYPE_NUMERIC).setCellValue(totalAmount.multiply(new BigDecimal(order1.getDeposit())).subtract(depositAmount).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP).doubleValue());
					}
					
				}
				 List<LcPurchaseOrderItem> itemList=purchaseOrderService.findPurchaseOrderItem(queryOrder);
				 if(itemList!=null){
					    for(int k=0;k<itemList.size();k++){
							if(k==0){
								int y=0;
								row=sheet.createRow(rowCount++);
								row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("采购单号(交货日期)");
								row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("产品");
								row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("订单数量");
								row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("已接收数量");
								row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("单价");
								row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("未收货金额");
							}
							LcPurchaseOrderItem item=itemList.get(k);
							BigDecimal itemPrice=item.getItemPrice();
							if("CNY".equals(item.getPurchaseOrder().getCurrencyType())){
								itemPrice=itemPrice.divide(new BigDecimal(AmazonProduct2Service.getRateConfig().get("USD/CNY")));
							}
							int w=0;
							row=sheet.createRow(rowCount++);
							row.createCell(w++,Cell.CELL_TYPE_STRING).setCellValue(item.getPurchaseOrder().getOrderNo()+"("+new SimpleDateFormat("yyyy-MM-dd").format(item.getDeliveryDate())+")");
							row.createCell(w++,Cell.CELL_TYPE_STRING).setCellValue(item.getProductName());
							row.createCell(w++,Cell.CELL_TYPE_NUMERIC).setCellValue(item.getQuantityOrdered());
							row.createCell(w++,Cell.CELL_TYPE_NUMERIC).setCellValue(item.getQuantityReceived());
							row.createCell(w++,Cell.CELL_TYPE_NUMERIC).setCellValue(itemPrice.multiply(new BigDecimal((100-item.getPurchaseOrder().getDeposit())/100)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() );
							row.createCell(w++,Cell.CELL_TYPE_NUMERIC).setCellValue(new BigDecimal((item.getQuantityOrdered()-item.getQuantityReceived())).multiply(itemPrice).multiply(new BigDecimal((100-item.getPurchaseOrder().getDeposit())/100)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() );
						}
				 }
				
				LcPsiLadingBill ladingBill=new LcPsiLadingBill();
				ladingBill.setSupplier(supplier);
				ladingBill.setCreateDate(purchaseOrder.getReceiveFinishedDate());
				ladingBill.setSureDate(purchaseOrder.getPurchaseDate());
				List<LcPsiLadingBill> billList=billService.findLadingBill(ladingBill);
				if(billList!=null){
					for(int p=0;p<billList.size();p++){
						if(p==0){
							int y=0;
							row=sheet.createRow(rowCount++);
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("提单号");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("总金额");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("已付金额");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("未付金额");
							row.createCell(y++,Cell.CELL_TYPE_STRING).setCellValue("");
						}
						LcPsiLadingBill bill=billList.get(p);
						int n=0;
						row=sheet.createRow(rowCount++);
						row.createCell(n++,Cell.CELL_TYPE_STRING).setCellValue(bill.getBillNo());
						row.createCell(n++,Cell.CELL_TYPE_STRING).setCellValue("");
						BigDecimal totalPaymentAmount=bill.getTotalPaymentAmount();
						BigDecimal totalAmount=bill.getTotalAmount();
						if("CNY".equals(bill.getSupplier().getCurrencyType())){
							totalPaymentAmount=totalPaymentAmount.divide(new BigDecimal(AmazonProduct2Service.getRateConfig().get("USD/CNY")));
							totalAmount=totalAmount.divide(new BigDecimal(AmazonProduct2Service.getRateConfig().get("USD/CNY")));
						}
						row.createCell(n++,Cell.CELL_TYPE_NUMERIC).setCellValue(totalAmount.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
						row.createCell(n++,Cell.CELL_TYPE_NUMERIC).setCellValue(totalPaymentAmount.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
						row.createCell(n++,Cell.CELL_TYPE_NUMERIC).setCellValue(totalAmount.subtract(totalPaymentAmount).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
						row.createCell(n++,Cell.CELL_TYPE_NUMERIC).setCellValue("");
					}
				}
				
			}
			
			
		     for (int i=1;i<rowCount;i++) {
		    	 if(!rowIndex.contains(i)){
		    		 for (int j = 0; j < title.size(); j++) {
			        	  sheet.getRow(i).getCell(j).setCellStyle(contentStyle);
					 }
		    	 }
	         }
			
			for (int i = 0; i < title.size(); i++) {
		       sheet.autoSizeColumn((short)i);
					
			}
		}
		
			try {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/x-download");
		
				SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmm");
		
				String fileName = "compare" + sdf.format(new Date()) + ".xls";
				fileName = URLEncoder.encode(fileName, "UTF-8");
				response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
				OutputStream out = response.getOutputStream();
				wb.write(out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = {"ajaxPrice"})
	public String ajaxPrice(Integer supplierId,Integer productId,String color,Integer quantity,String currency) {
		if("No color".equals(color)){
			color="";
		}
		PsiProductTieredPriceDto dto = productTieredPriceService.findPrices(productId, supplierId, currency, color);
		Integer tax=psiSupplierService.get(supplierId).getTaxRate();
		Float price = 0f;
		Float  taxRate= (tax+100)/100f;
		if(dto!=null){
			if("USD".equals(currency)){
				if(quantity<1000){ 		//小于1000用500的价
					price=dto.getLeval500usd(); 
				}else if(quantity<2000){   //小于2000用1000的价
					price=dto.getLeval1000usd();
				}else if(quantity<3000){   //小于3000用2000的价
					price=dto.getLeval2000usd();
				}else if(quantity<5000){   //小于5000用3000的价
					price=dto.getLeval3000usd();
				}else if(quantity<10000){  //小于10000用5000的价
					price=dto.getLeval5000usd();
				}else if(quantity<15000){  //小于15000用10000的价
					price=dto.getLeval10000usd();
				}else{                          //大于15000用15000的价
					price=dto.getLeval15000usd();
				}
			}else{
				if(quantity<1000){ 		//小于1000用500的价
					price=dto.getLeval500cny(); 
				}else if(quantity<2000){   //小于2000用1000的价
					price=dto.getLeval1000cny();
				}else if(quantity<3000){   //小于3000用2000的价
					price=dto.getLeval2000cny();
				}else if(quantity<5000){   //小于5000用3000的价
					price=dto.getLeval3000cny();
				}else if(quantity<10000){  //小于10000用5000的价
					price=dto.getLeval5000cny();
				}else if(quantity<15000){  //小于15000用10000的价
					price=dto.getLeval10000cny();
				}else{                          //大于15000用15000的价
					price=dto.getLeval15000cny();
				}
			}
		}
		String rs="{\"price\":\""+(price==null?"":new BigDecimal(price*taxRate+"").setScale(2, BigDecimal.ROUND_HALF_UP))+"\"}";
		return rs;
	}
	

	@RequiresPermissions("psi:all:view")
	@RequestMapping(value =  "expPreReceived" )
	public String expPreReceived(String firstOnce,String moreOnce,Integer week, HttpServletRequest request,HttpServletResponse response, Model model) throws UnsupportedEncodingException, ParseException {
		    
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = sdf.parse(sdf.format(new Date()));
		Set<Integer> newProductIds = Sets.newHashSet();
		if(StringUtils.isEmpty(firstOnce)){
			firstOnce="0";
		}
		if(StringUtils.isEmpty(moreOnce)){
			moreOnce="0";
		}
		if("1".equals(firstOnce)||"1".equals(moreOnce)){
			newProductIds=this.productService.findNewMap().keySet();
		}
		List<Object[]> obs= this.purchaseOrderService.getDeliveryProducts(today, week, newProductIds,firstOnce,moreOnce);
		List<PsiProductAttribute> attrs=productAttrService.findAll();
		Map<String, Integer> transportTypeMap = productEliminateService.findProductTransportType();
		Map<String, String> positionMap = productEliminateService.findProductPositionByCountry(null);
		Map<String,String> attrMap = Maps.newHashMap();
		for(PsiProductAttribute attr:attrs){
			String value=DictUtils.getDictLabel(positionMap.get(attr.getColorName()), "product_position", "");
			if(transportTypeMap.get(attr.getColorName())!=null){
				if (transportTypeMap.get(attr.getColorName()).equals(3)) {
					value+="&海运/空运";
				} else if (transportTypeMap.get(attr.getColorName()).equals(1)) {
					value+="&海运";
				} else {
					value+="&空运";
				}
			}
			attrMap.put(attr.getColorName(), value);
		}
		
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		String[] title = {" 产品名 "," 市场 ", " 数量 ", " 收货日期  "," 收货还剩天数 ","产品属性"};
		style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 11); // 字体高度
		style.setFont(font);
		row.setHeight((short) 400);
		HSSFCell cell = null;		
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn((short) i);
		}
		
		int j =1;
		for(Object[] obj:obs){
			row = sheet.createRow(j++);
			int i=0;
			row.createCell(i++,Cell.CELL_TYPE_STRING).setCellValue(obj[0].toString()); 
			row.createCell(i++,Cell.CELL_TYPE_STRING).setCellValue(obj[1].toString());
			row.createCell(i++,Cell.CELL_TYPE_STRING).setCellValue(obj[2].toString());
			row.createCell(i++,Cell.CELL_TYPE_STRING).setCellValue(obj[3].toString());
			row.createCell(i++,Cell.CELL_TYPE_STRING).setCellValue(DateUtils.pastDaysByStr(obj[3].toString())+1);
			row.createCell(i++,Cell.CELL_TYPE_STRING).setCellValue(attrMap.get(obj[0].toString()));
		 }
				
			
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
	
		SimpleDateFormat sdf1 = new SimpleDateFormat("MMddhhmm");
	
		String fileName = "ReceivingInfos" + sdf1.format(new Date()) + ".xls";
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
		try {
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//发送邮件给销售
	public boolean sendEmail(LcPurchaseOrder purchaseOrder){
		StringBuffer contents= new StringBuffer("");
		contents.append("<table width='90%' style='border-right:1px solid;border-bottom:1px solid;color:#666;width:100%' cellpadding='0' cellspacing='0' >");
		contents.append("<tr style='background-repeat:repeat-x;height:30px; background-color:#B2B2B2;color:#666;'>");
		contents.append("<th style='border-left:1px solid;border-top:1px solid;color:#666;width:20%'>产品名</th>");
		contents.append("<th style='border-left:1px solid;border-top:1px solid;color:#666;width:5%'>国家</th>");
		contents.append("<th style='border-left:1px solid;border-top:1px solid;color:#666;width:15%'>数量</th>");
		contents.append("</tr>");
		for(LcPurchaseOrderItem item:purchaseOrder.getItems()){
			Integer quantity=item.getQuantityOrdered();
			contents.append("<tr style='background-repeat:repeat-x;text-align:center;height:30px; border-left:1px solid;border-top:1px solid;color:#666; '>");
			contents.append("<td style='border-left:1px solid;border-top:1px solid;color:#666;'>"+item.getProductNameColor()+"</td>");
			contents.append("<td style='border-left:1px solid;border-top:1px solid;color:#666;'>"+(("com".equals(item.getCountryCode())?"us":item.getCountryCode()).toUpperCase())+"</td>");
			contents.append("<td style='border-left:1px solid;border-top:1px solid;color:#666;'>"+quantity+"</td>");
		}
		contents.append("</table><br/>");
		String toAddress ="amazon-sales@inateck.com";
		Date date = new Date();   
		final MailInfo mailInfo = new MailInfo(toAddress,"采购订单取消[no:"+purchaseOrder.getOrderNo()+"]"+DateUtils.getDate("-yyyy/M/dd"),date);
		mailInfo.setContent(contents.toString());
		new Thread(){
			public void run(){
				 mailManager.send(mailInfo);
			}
		}.start();
		
		return true;
	}
	
	@RequiresPermissions("psi:all:view")
	@RequestMapping(value =  "expUnReceived" )
	public String expUnReceived(LcPurchaseOrder purchaseOrder,String isCheck,String productIdColor,HttpServletRequest request, HttpServletResponse response,Model model) throws UnsupportedEncodingException, ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		int excelNo = 1;
		Map<String,String> typeLineMap= typeLineService.getTypeLine(null);
		Map<String,String> typeMap=productService.findProductTypeMap();
		Page<LcPurchaseOrder> page = new Page<LcPurchaseOrder>(request, response);
		page.setPageSize(99999);
		page=purchaseOrderService.findByProduct(page, purchaseOrder, isCheck, productIdColor);
		List<LcPurchaseOrder> list=page.getList();
		List<LcHisPurchaseOrder> hisList=Lists.newArrayList();
		for(LcPurchaseOrder order:list){
			LcHisPurchaseOrder hisOrder= new LcHisPurchaseOrder(order);
			List<LcHisPurchaseOrderItem>  temItems = Lists.newArrayList();
        	for (Iterator<LcPurchaseOrderItem> iterator = order.getItems().iterator(); iterator.hasNext();) {
        		LcPurchaseOrderItem item = (LcPurchaseOrderItem) iterator.next();
        		//如果收货完成，移除掉
        		Integer unReceived =item.getQuantityUnReceived()-item.getQuantityPreReceived();
        		if(unReceived.intValue()==0){
        			continue;
        		}
        		Integer unPreReceived =item.getQuantityPreReceived();
        		//如果有分批收货，item里面添加
        		if(item.getDeliveryDateList()!=null&&item.getDeliveryDateList().size()>0){
        			for(LcPurchaseOrderDeliveryDate deliveryPo:item.getDeliveryDateList()){
        				Integer unReceivedItem = deliveryPo.getQuantity()-deliveryPo.getQuantityReceived();
        				if(unPreReceived>0){
        					unPreReceived=unPreReceived-unReceivedItem;
            				if(unReceivedItem>0&&unPreReceived<=0){
            					LcHisPurchaseOrderItem lcItem = new LcHisPurchaseOrderItem(deliveryPo.getProductName(), deliveryPo.getColorCode(),deliveryPo.getCountryCode(),"", deliveryPo.getQuantity(), unReceivedItem+unPreReceived,0, item.getDeliveryDate(), deliveryPo.getDeliveryDate());
            					temItems.add(lcItem);
            				}
        				}else{
        					LcHisPurchaseOrderItem lcItem = new LcHisPurchaseOrderItem(deliveryPo.getProductName(), deliveryPo.getColorCode(),deliveryPo.getCountryCode(),"", deliveryPo.getQuantity(), deliveryPo.getQuantityReceived(),0, item.getDeliveryDate(), deliveryPo.getDeliveryDate());
        					temItems.add(lcItem);
        				}
        			}
        		}else{
        			temItems.add(new LcHisPurchaseOrderItem(item,hisOrder));
        		}
			}
        	hisOrder.setItems(temItems);
        	hisList.add(hisOrder);
        	
	     }
		
		if (list.size() > 65535) {
			throw new RuntimeException("一次导出的最大数据不能超过65535条！请分两次导出！");
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 16); // 字体高度
		font.setFontName(" 黑体 "); // 字体
		font.setBoldweight((short) 16);
		style.setFont(font);

		CellStyle contentStyle = wb.createCellStyle();
		contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		contentStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		contentStyle.setRightBorderColor(HSSFColor.BLACK.index);
		contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		contentStyle.setTopBorderColor(HSSFColor.BLACK.index);

		String[] title = { "型号", "订单日期","订单号", "供应商", "总数", " DE ", " UK ", " FR ",
				" IT ", " ES ", " US ", " CA ", " JP ", " MX ", "订单收货日期", " 交期 "," Sku Version " ,"SN"," 产品线 ", " 备注 " };

		row.setHeight((short) 600);
		HSSFCell cell = null;
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn((short) i);
		}
		int rowTotal = 0;
		
		Map<String,String> skuMap =this.productService.getAllBandingSku();
		
		for (LcHisPurchaseOrder purchaseOrderTrue : hisList) {
			if(purchaseOrderTrue.getItems().size()==0){
				continue;
			}
			List<LcHisPurchaseOrder> tempOrders = purchaseOrderTrue.getTempOrdersByDeliveryDate();
			for (int i = 0; i < tempOrders.size(); i++) {
				LcHisPurchaseOrder tempOrder = tempOrders.get(i);
				List<LcHisPurchaseOrderItem> orderItems = tempOrder.getItems();
				if(orderItems.size()==0){
					continue;
				}
				String productName = "";
				Date deliveredDate = null;
				Date actualDeliveryDate = null;

				Map<String, String> itemQuantityMap = Maps.newHashMap();
				Map<String, String> colorMap = Maps.newHashMap();
				String productType="";
				for (int j = 0; j < orderItems.size(); j++) {
					LcHisPurchaseOrderItem item = orderItems.get(j);
					if (j == 0) {
						productName = item.getProductName();
						deliveredDate = item.getDeliveryDate();
					}
					if (actualDeliveryDate == null) {
						actualDeliveryDate = item.getActualDeliveryDate();
					} else if (item.getActualDeliveryDate() != null && item.getActualDeliveryDate().after(actualDeliveryDate)){
						actualDeliveryDate = item.getActualDeliveryDate();
					}
					
					String quantitys = item.getQuantityOrdered() + ","+ item.getQuantityReceived() + ","+ (item.getQuantityCanReceived());
					String tempQ=quantitys;
					String itemKey=item.getCountryCode() + "," + item.getColorCode();
					if(itemQuantityMap.get(itemKey)!=null){
						String[] auqArr = itemQuantityMap.get(itemKey).split(",");
						tempQ = (item.getQuantityOrdered() + Integer.parseInt(auqArr[0]))+ ","+ (item.getQuantityReceived() + Integer.parseInt(auqArr[1]))+ ","+ (item.getQuantityCanReceived() + Integer.parseInt(auqArr[2]));
					}
					itemQuantityMap.put(itemKey, tempQ);

					if (colorMap.get(item.getColorCode()) != null) {
						String[] auqArr = colorMap.get(item.getColorCode()).split(",");
						quantitys = (item.getQuantityOrdered() + Integer.parseInt(auqArr[0]))+ ","+ (item.getQuantityReceived() + Integer.parseInt(auqArr[1]))+ ","+ (item.getQuantityCanReceived() + Integer.parseInt(auqArr[2]));
					}
					colorMap.put(item.getColorCode(), quantitys);
				}
				// 根据下面颜色迭代行
				for (Map.Entry<String, String> entry : colorMap.entrySet()) {
					String color = entry.getKey();
					String total = entry.getValue();
					if (total.split(",")[2].equals("0")) {
						continue;
					}
					rowTotal++;
					int cellNum = 0;
					//查看德国sku绑定的条码
					String proKey=productName+",de,"+color;
					String version ="";
					if(skuMap.get(proKey)!=null){
						version=this.productService.getSkuVersion(skuMap.get(proKey));
					}
					row = sheet.createRow(excelNo++); // 生成行
					row.setHeight((short) 400);
					String productName_color = "";
					if (StringUtils.isNotEmpty(color)) {
						productType=typeMap.get(productName+"_"+color);
						productName_color = productName	+ "_" + color;
					} else {
						productName_color = productName;
						productType=typeMap.get(productName);
					}
					// 产品型号
					row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(productName_color);
					// 订单日期
					row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(sdf1.format(sdf2.parse(tempOrder.getOrderNo().substring(0, 8)))); 
					row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getOrderNo()); // 订单号
					// 供应商简称
					if (tempOrder.getSupplier() != null) {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getSupplier().getName() != null?tempOrder.getSupplier().getName(): "");
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}
					// 总数(所有导出数据为订单未收货数据)
					if(StringUtils.isNotEmpty(total.split(",")[2])&&!total.split(",")[2].equals("0")){
						row.createCell(cellNum++,Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(total.split(",")[2]));
					}else{
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}
                    
					// 各平台数量
					if (StringUtils.isNotEmpty(itemQuantityMap.get("de," + color))&&!itemQuantityMap.get("de," + color).equals("0") ) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(itemQuantityMap.get("de," + color).split(",")[2]));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}

					if (StringUtils.isNotEmpty(itemQuantityMap.get("uk," + color))&&!itemQuantityMap.get("uk," + color).equals("0") ) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(itemQuantityMap.get("uk," + color).split(",")[2]));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}

					if (StringUtils.isNotEmpty(itemQuantityMap.get("fr," + color))&&!itemQuantityMap.get("fr," + color).equals("0") ) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(itemQuantityMap.get("fr," + color).split(",")[2]));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}

					if (StringUtils.isNotEmpty(itemQuantityMap.get("it," + color))&&!itemQuantityMap.get("it," + color).equals("0") ) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(itemQuantityMap.get("it," + color).split(",")[2]));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}

					if (StringUtils.isNotEmpty(itemQuantityMap.get("es," + color))&&!itemQuantityMap.get("es," + color).equals("0") ) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(itemQuantityMap.get("es," + color).split(",")[2]));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}
					Integer usNum = 0;
					if (StringUtils.isNotEmpty(itemQuantityMap.get("com," + color))&&!itemQuantityMap.get("com," + color).equals("0") ) {
						usNum += Integer.parseInt(itemQuantityMap.get("com," + color).split(",")[2]);
					}
					if (StringUtils.isNotEmpty(itemQuantityMap.get("com2," + color))&&!itemQuantityMap.get("com2," + color).equals("0") ) {
						usNum += Integer.parseInt(itemQuantityMap.get("com2," + color).split(",")[2]);
					}
					if (StringUtils.isNotEmpty(itemQuantityMap.get("com3," + color))&&!itemQuantityMap.get("com3," + color).equals("0") ) {
						usNum += Integer.parseInt(itemQuantityMap.get("com3," + color).split(",")[2]);
					}

					if (usNum>0) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(usNum);
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}

					if (StringUtils.isNotEmpty(itemQuantityMap.get("ca," + color))&&!itemQuantityMap.get("ca," + color).equals("0") ) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(itemQuantityMap.get("ca," + color).split(",")[2]));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}

					if (StringUtils.isNotEmpty(itemQuantityMap.get("jp," + color))&&!itemQuantityMap.get("jp," + color).equals("0") ) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(itemQuantityMap.get("jp," + color).split(",")[2]));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}

					if (StringUtils.isNotEmpty(itemQuantityMap.get("mx," + color))&&!itemQuantityMap.get("mx," + color).equals("0") ) {
						row.createCell(cellNum++, Cell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(itemQuantityMap.get("mx," + color).split(",")[2]));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}
					// 收货日期
					if (actualDeliveryDate != null) {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(sdf1.format(actualDeliveryDate));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}
					//交期
					if (deliveredDate != null) {
						row.createCell(cellNum++,Cell.CELL_TYPE_STRING).setCellValue(sdf1.format(deliveredDate));
					} else {
						row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					}
					// 备注(导出后手动填写)
					//row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(isNewMap.get(productName));
					row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(StringUtils.isEmpty(version)?"没绑条码":version);
					row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(tempOrder.getSnCode());
					row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue(typeLineMap.get(productType.toLowerCase())!=null?typeLineMap.get(productType.toLowerCase()):"");
					row.createCell(cellNum++, Cell.CELL_TYPE_STRING).setCellValue("");
					
				}
			}
		}
		for (int i = 0; i < rowTotal; i++) {
			for (int j = 0; j < title.length; j++) {
				sheet.getRow(i + 1).getCell(j).setCellStyle(contentStyle);
			}
		}
		for (int i = 0; i < title.length; i++) {
			sheet.autoSizeColumn((short) i);
		}

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmm");
		String fileName = "PurchaseOrderUnReceivedData" + sdf.format(new Date()) + ".xls";
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		try {
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void sendNoticeEmail(String email,String content,String subject){
//		String content = "(超标)采购订单编号：<a href='"+BaseService.BASE_WEBPATH+Global.getAdminPath()+"/psi/lcPurchaseOrder/review?id="+purchaseOrder.getId()+"'>"+orderNo+"</a>已创建，请尽快登陆erp系统审批";
			if(StringUtils.isNotBlank(content)){
				Date date = new Date();
				final MailInfo mailInfo = new MailInfo(email,subject+DateUtils.getDate("-yyyy/M/dd"),date);
				mailInfo.setContent(content);
				//发送成功不成功都能保存
				new Thread(){
					@Override
					public void run(){
						poMaillManager.send(mailInfo);
					}
				}.start();
			}
	}

	
	
	//下面是判断请求是不是手机发出的
	
    /**Wap网关Via头信息中特有的描述信息*/
    private static String mobileGateWayHeaders[]=new String[]{
    "ZXWAP",//中兴提供的wap网关的via信息，例如：Via=ZXWAP GateWayZTE Technologies，
    "chinamobile.com",//中国移动的诺基亚wap网关，例如：Via=WTP/1.1 GDSZ-PB-GW003-WAP07.gd.chinamobile.com (Nokia WAP Gateway 4.1 CD1/ECD13_D/4.1.04)
    "monternet.com",//移动梦网的网关，例如：Via=WTP/1.1 BJBJ-PS-WAP1-GW08.bj1.monternet.com. (Nokia WAP Gateway 4.1 CD1/ECD13_E/4.1.05)
    "infoX",//华为提供的wap网关，例如：Via=HTTP/1.1 GDGZ-PS-GW011-WAP2 (infoX-WISG Huawei Technologies)，或Via=infoX WAP Gateway V300R001 Huawei Technologies
    "XMS 724Solutions HTG",//国外电信运营商的wap网关，不知道是哪一家
    "wap.lizongbo.com",//自己测试时模拟的头信息
    "Bytemobile",//貌似是一个给移动互联网提供解决方案提高网络运行效率的，例如：Via=1.1 Bytemobile OSN WebProxy/5.1
    };
    /**电脑上的IE或Firefox浏览器等的User-Agent关键词*/
    private static String[] pcHeaders=new String[]{
    "Windows 98",
    "Windows ME",
    "Windows 2000",
    "Windows XP",
    "Windows NT",
    "Ubuntu"
    };
    /**手机浏览器的User-Agent里的关键词*/
    private static String[] mobileUserAgents=new String[]{
    "Nokia",//诺基亚，有山寨机也写这个的，总还算是手机，Mozilla/5.0 (Nokia5800 XpressMusic)UC AppleWebkit(like Gecko) Safari/530
    "SAMSUNG",//三星手机 SAMSUNG-GT-B7722/1.0+SHP/VPP/R5+Dolfin/1.5+Nextreaming+SMM-MMS/1.2.0+profile/MIDP-2.1+configuration/CLDC-1.1
    "MIDP-2",//j2me2.0，Mozilla/5.0 (SymbianOS/9.3; U; Series60/3.2 NokiaE75-1 /110.48.125 Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML like Gecko) Safari/413
    "CLDC1.1",//M600/MIDP2.0/CLDC1.1/Screen-240X320
    "SymbianOS",//塞班系统的，
    "MAUI",//MTK山寨机默认ua
    "UNTRUSTED/1.0",//疑似山寨机的ua，基本可以确定还是手机
    "Windows CE",//Windows CE，Mozilla/4.0 (compatible; MSIE 6.0; Windows CE; IEMobile 7.11)
    "iPhone",//iPhone是否也转wap？不管它，先区分出来再说。Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_1 like Mac OS X; zh-cn) AppleWebKit/532.9 (KHTML like Gecko) Mobile/8B117
    "iPad",//iPad的ua，Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; zh-cn) AppleWebKit/531.21.10 (KHTML like Gecko) Version/4.0.4 Mobile/7B367 Safari/531.21.10
    "Android",//Android是否也转wap？Mozilla/5.0 (Linux; U; Android 2.1-update1; zh-cn; XT800 Build/TITA_M2_16.22.7) AppleWebKit/530.17 (KHTML like Gecko) Version/4.0 Mobile Safari/530.17
    "BlackBerry",//BlackBerry8310/2.7.0.106-4.5.0.182
    "UCWEB",//ucweb是否只给wap页面？ Nokia5800 XpressMusic/UCWEB7.5.0.66/50/999
    "ucweb",//小写的ucweb貌似是uc的代理服务器Mozilla/6.0 (compatible; MSIE 6.0;) Opera ucweb-squid
    "BREW",//很奇怪的ua，例如：REW-Applet/0x20068888 (BREW/3.1.5.20; DeviceId: 40105; Lang: zhcn) ucweb-squid
    "J2ME",//很奇怪的ua，只有J2ME四个字母
    "YULONG",//宇龙手机，YULONG-CoolpadN68/10.14 IPANEL/2.0 CTC/1.0
    "YuLong",//还是宇龙
    "COOLPAD",//宇龙酷派YL-COOLPADS100/08.10.S100 POLARIS/2.9 CTC/1.0
    "TIANYU",//天语手机TIANYU-KTOUCH/V209/MIDP2.0/CLDC1.1/Screen-240X320
    "TY-",//天语，TY-F6229/701116_6215_V0230 JUPITOR/2.2 CTC/1.0
    "K-Touch",//还是天语K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223 Release/30.07.2008 Browser/WAP2.0
    "Haier",//海尔手机，Haier-HG-M217_CMCC/3.0 Release/12.1.2007 Browser/WAP2.0
    "DOPOD",//多普达手机
    "Lenovo",// 联想手机，Lenovo-P650WG/S100 LMP/LML Release/2010.02.22 Profile/MIDP2.0 Configuration/CLDC1.1
    "LENOVO",// 联想手机，比如：LENOVO-P780/176A
    "HUAQIN",//华勤手机
    "AIGO-",//爱国者居然也出过手机，AIGO-800C/2.04 TMSS-BROWSER/1.0.0 CTC/1.0
    "CTC/1.0",//含义不明
    "CTC/2.0",//含义不明
    "CMCC",//移动定制手机，K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223 Release/30.07.2008 Browser/WAP2.0
    "DAXIAN",//大显手机DAXIAN X180 UP.Browser/6.2.3.2(GUI) MMP/2.0
    "MOT-",//摩托罗拉，MOT-MOTOROKRE6/1.0 LinuxOS/2.4.20 Release/8.4.2006 Browser/Opera8.00 Profile/MIDP2.0 Configuration/CLDC1.1 Software/R533_G_11.10.54R
    "SonyEricsson",// 索爱手机，SonyEricssonP990i/R100 Mozilla/4.0 (compatible; MSIE 6.0; Symbian OS; 405) Opera 8.65 [zh-CN]
    "GIONEE",//金立手机
    "HTC",//HTC手机
    "ZTE",//中兴手机，ZTE-A211/P109A2V1.0.0/WAP2.0 Profile
    "HUAWEI",//华为手机，
    "webOS",//palm手机，Mozilla/5.0 (webOS/1.4.5; U; zh-CN) AppleWebKit/532.2 (KHTML like Gecko) Version/1.0 Safari/532.2 Pre/1.0
    "GoBrowser",//3g GoBrowser.User-Agent=Nokia5230/GoBrowser/2.0.290 Safari
    "IEMobile",//Windows CE手机自带浏览器，
    "WAP2.0"//支持wap 2.0的
    };
	  /**
	    * 根据当前请求的特征，判断该请求是否来自手机终端，主要检测特殊的头信息，以及user-Agent这个header
	    * @param request http请求
	    * @return 如果命中手机特征规则，则返回对应的特征字符串
	    */
	    public  boolean isMobileDevice(HttpServletRequest request){
	        boolean b = false;
	        boolean pcFlag = false;
	        boolean mobileFlag = false;
	        String via = request.getHeader("Via");
	        String userAgent = request.getHeader("user-agent");
	        for (int i = 0; via!=null && !via.trim().equals("") && i < mobileGateWayHeaders.length; i++) {
	            if(via.contains(mobileGateWayHeaders[i])){
	                mobileFlag = true;
	                break;
	            }
	        }
	        for (int i = 0;!mobileFlag && userAgent!=null && !userAgent.trim().equals("") && i < mobileUserAgents.length; i++) {
	            if(userAgent.contains(mobileUserAgents[i])){
	                mobileFlag = true;
	                break;
	            }
	        }
	        for (int i = 0; userAgent!=null && !userAgent.trim().equals("") && i < pcHeaders.length; i++) {
	            if(userAgent.contains(pcHeaders[i])){
	                pcFlag = true;
	                break;
	            }
	        }
	        if(mobileFlag==true && pcFlag==false){
	            b=true;
	        }
	        return b;//false 电脑  true 手机
	    
	    }

	    
	    @RequestMapping(value = {"invoiceList"})
		public String invoiceList(Integer id, RedirectAttributes redirectAttributes, Model model) {
			model.addAttribute("invoiceList",purchaseOrderService.findPurchaseOrders(id));
			return "modules/psi/lc/lcPurchaseOrderInvoiceList";
		}
	    
	    @RequestMapping(value = {"genInvoice"})
	  	public String genInvoice(LcPurchaseOrder purchaseOrder, RedirectAttributes redirectAttributes, Model model) {
	    	    List<LcPurchaseOrderInvoice> invoiceList=purchaseOrderService.findPurchaseOrders(purchaseOrder.getId()); 
			    if(invoiceList==null||invoiceList.size()==0){
		  			purchaseOrderService.genInvoice(purchaseOrder);
			    }
	  			return "redirect:"+Global.getAdminPath()+"/psi/lcPurchaseOrder/?repage";
	  	}
	    
	    
	    @ResponseBody
		@RequestMapping(value = {"deleteItem"})
		public String deleteItem(Integer itemId) {
			return purchaseOrderService.updateInvoiceItem(itemId);
		}

	    
		@ResponseBody
		@RequestMapping(value = {"saveInvoice"})
		public Integer saveOrderItem(Integer inoviceId,String invoiceNo,Integer invoiceQuantity,Integer purchaseOrderId) {
			   
			    LcPurchaseOrderInvoiceItem item=new LcPurchaseOrderInvoiceItem();
			    item.setInvoice(new LcPurchaseOrderInvoice(inoviceId));
			    item.setInvoiceNo(invoiceNo);
			    item.setInvoiceQuantity(invoiceQuantity);
			    item.setDelFlag("0");
			    purchaseOrderService.saveInvoiceItem(item);
				return item.getId();
		}
		
}
