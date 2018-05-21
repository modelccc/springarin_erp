/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.springrain.erp.modules.psi.web.lc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springrain.erp.common.persistence.Page;
import com.springrain.erp.common.web.BaseController;
import com.springrain.erp.modules.psi.entity.lc.LcPsiPartsInventoryLog;
import com.springrain.erp.modules.psi.entity.parts.PsiParts;
import com.springrain.erp.modules.psi.service.lc.LcPsiPartsInventoryLogService;
import com.springrain.erp.modules.psi.service.lc.LcPsiPartsInventoryService;
import com.springrain.erp.modules.psi.service.parts.PsiPartsService;

/**
 * 配件库存管理Controller
 * @author Michael
 * @version 2015-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/psi/lcPsiPartsInventoryLog")
public class LcPsiPartsInventoryLogController extends BaseController {
	@Autowired
	private LcPsiPartsInventoryLogService psiPartsInventoryLogService;
	@Autowired
	private LcPsiPartsInventoryService    psiPartsInventoryService;
	@Autowired
	private PsiPartsService               psiPartsService;
	
	@RequestMapping(value = {"list", ""})
	public String list(LcPsiPartsInventoryLog psiPartsInventoryLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<LcPsiPartsInventoryLog> page = psiPartsInventoryLogService.find(new Page<LcPsiPartsInventoryLog>(request, response), psiPartsInventoryLog); 
        List<PsiParts> partsList = this.psiPartsService.findAllParts(); 
        if(psiPartsInventoryLog!=null&&psiPartsInventoryLog.getPartsId()!=null){
        	 model.addAttribute("partsInventory",psiPartsInventoryService.getPsiPartsInventorys(psiPartsInventoryLog.getPartsId()));
        }
        model.addAttribute("partsList", partsList);
        model.addAttribute("psiPartsInventoryLog", psiPartsInventoryLog);
        model.addAttribute("page", page);
		return "modules/psi/lc/parts/lcPsiPartsInventoryLogList";
	}  
  
}
