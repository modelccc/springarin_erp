package com.springrain.erp.modules.amazoninfo.entity;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.springrain.erp.common.utils.StringUtils;
import com.springrain.erp.modules.sys.entity.User;

/**
 * 自发货库存管理Entity
 * @author Tim
 * @version 2015-07-09
 */
@Entity
@Table(name = "amazoninfo_mfn_inventory_feed")
public class MfnInventoryFeed implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id; // 编号

	private String requestId;

	private Date requestDate;

	private List<MfnItem> items = Lists.newArrayList();

	private String country;

	private User createBy;
	
	private String state = "1";
	
	private String result;
	
	private String resultFile;
	
	private Date endDate;
	
	private String accountName;
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Transient
	public Date getEndDate() {
		return endDate;
	}
	
	@Transient
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	
	
	@OneToMany(mappedBy = "mfnInventory", fetch=FetchType.LAZY)
	@OrderBy(value="id")
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.NONE)
	@Cascade(CascadeType.SAVE_UPDATE)
	public List<MfnItem> getItems() {
		return items;
	}

	public void setItems(List<MfnItem> items) {
		this.items = items;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	
	public String getResultFile() {
		return resultFile;
	}

	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}
	
	@Transient
	public String getStateStr() {
		if("1".equals(state)){
			return "发送到服务器...";
		}else if("2".equals(state)){
			return "等待反馈结果...";
		}else if("3".equals(state)){
			return "已完成";
		}else if("4".equals(state)){
			if(StringUtils.isEmpty(requestId)){
				return "上传失败,可能亚马逊服务不可用或网络原因,请稍后重试！";
			}else{
				return "累计10次获取结果失败,请自行观察上传结果;可能亚马逊服务不稳定或网络原因！";
			}
		}
		return state;
	}

	public File genXmlFile(String path) {
		Document doc = DocumentHelper.createDocument();
		Element rootElmt = doc.addElement("AmazonEnvelope");
		Element headerEl = rootElmt.addElement("Header");
		headerEl.addElement("DocumentVersion").addText("1.01");
		headerEl.addElement("MerchantIdentifier").addText(
				"ListingsContentHandler");
		rootElmt.addElement("MessageType").addText("Inventory");
		int i = 0;
		for (MfnItem item : items) {
			Element msg = rootElmt.addElement("Message");
			msg.addElement("MessageID").addText(++i + "");
			msg.addElement("OperationType").addText("Update");
			Element itemEl = msg.addElement("Inventory");
			itemEl.addElement("SKU").addText(item.getSku());
			itemEl.addElement("Quantity").addText(item.getQuantity()+"");
		}
		File rs = new File(path + "/data.xml");
		try {
			// 定义输出流的目的地
			FileWriter fw = new FileWriter(rs);
			// 定义输出格式和字符集
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			// 定义用于输出xml文件的XMLWriter对象
			XMLWriter xmlWriter = new XMLWriter(fw, format);
			xmlWriter.write(doc);
			xmlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs;
	}
}


