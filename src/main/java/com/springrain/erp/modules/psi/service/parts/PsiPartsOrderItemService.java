/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.springrain.erp.modules.psi.service.parts;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.springrain.erp.common.service.BaseService;
import com.springrain.erp.modules.psi.dao.parts.PsiPartsOrderItemDao;
import com.springrain.erp.modules.psi.entity.parts.PsiPartsOrderItem;

/**
 * 产品配件Service
 * @author Michael
 * @version 2015-06-02
 */
@Component
@Transactional(readOnly = true)
public class PsiPartsOrderItemService extends BaseService {

	@Autowired
	private PsiPartsOrderItemDao psiPartsOrderItemDao;
	   
	public List<PsiPartsOrderItem> getOrderItems(String items) {
		DetachedCriteria dc = psiPartsOrderItemDao.createDetachedCriteria();
		List<Integer> ids = Lists.newArrayList();
		for(String id:items.split(",")){
			ids.add(Integer.parseInt(id));
		}
		dc.add(Restrictions.in("id",ids));
		return psiPartsOrderItemDao.find(dc);
	}
	  
	
	
}
