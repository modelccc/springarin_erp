package com.springrain.erp.modules.amazoninfo.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.springrain.erp.modules.sys.entity.User;

/**
 * 销量预测Entity
 * @author Tim
 * @version 2015-03-03
 */
@Entity
@Table(name = "amazoninfo_sales_forecast_month")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SalesForecastByMonth implements Serializable,Comparable<SalesForecastByMonth> {
	
	private static final long serialVersionUID = 1L;
	
	private static DateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
	
	private Integer id; 		// 编号
	private String country; 	// 国家
	private User createBy;
	private Date dataDate;//那一月的最后一天
	private Date lastUpdateDate;
	private User lastUpdateBy;
	
	private String productName;
	private Integer productId;
	
	private String type;
	
	private Integer quantityForecast;
	
	private Integer quantityAuthentication;
	
	public SalesForecastByMonth() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(User lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantityForecast() {
		return quantityForecast;
	}

	public void setQuantityForecast(Integer quantityForecast) {
		this.quantityForecast = quantityForecast;
	}

	public Integer getQuantityAuthentication() {
		return quantityAuthentication;
	}

	public void setQuantityAuthentication(Integer quantityAuthentication) {
		this.quantityAuthentication = quantityAuthentication;
	}

	@Transient
	public String getMonth() {
		return monthFormat.format(dataDate);
	}

	public final static Map<String, Integer> countryCode = Maps.newHashMap();

	static{
		countryCode.put("de", 9);
		countryCode.put("fr", 8);
		countryCode.put("it", 7);
		countryCode.put("es", 6);
		countryCode.put("ebay", 5);
		countryCode.put("uk", 4);
		countryCode.put("com", 3);
		countryCode.put("jp", 2);
		countryCode.put("ca", 1);
		countryCode.put("mx", 0);
	}
	
	@Override
	public int compareTo(SalesForecastByMonth o) {
		return countryCode.get(o.getCountry())-countryCode.get(country);
	}
}


