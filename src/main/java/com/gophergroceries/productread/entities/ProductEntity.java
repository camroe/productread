package com.gophergroceries.productread.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	// @JsonBackReference
	// @JsonIgnore
	private Integer id;

	// @ManyToOne
	// @JoinColumn(name="ole", referencedColumnName="product")
	// @JsonBackReference
	// private OrderLinesEntity ole;

	@Column(name = "sku")
	private String sku;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "inventory")
	private Integer inventory;

	@Column(name = "popular")
	private String popular;

	@Column(name = "imagefile")
	private String imagefile;

	@Column(name = "category")
	private String category;

	@Column(name = "store")
	private String store;

	// ********** GETTERS AND SETTERS ************
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	// public OrderLinesEntity getOle() {
	// return ole;
	// }
	//
	// public void setOle(OrderLinesEntity ole) {
	// this.ole = ole;
	// }

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public String getPopular() {
		return popular;
	}

	public void setPopular(String popular) {
		this.popular = popular;
	}

	public String getImagefile() {
		return imagefile;
	}

	public void setImagefile(String imagefile) {
		this.imagefile = imagefile;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}"; // empty JSON object.
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 19)
				.append(id.intValue())
				.append(sku)
				.append(imagefile)
				.append(category)
				.append(name)
				.append(description)
				.append(store)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ProductEntity))
			return false;
		if (obj == this) {
			return true;
		}
		// Right-Hand-Side (rhs)
		ProductEntity rhs = (ProductEntity) obj;
		return new EqualsBuilder()
				.append(id.intValue(), rhs.getId().intValue())
				.append(sku, rhs.getSku())
				.append(imagefile, rhs.getImagefile())
				.append(category, rhs.getCategory())
				.append(name, rhs.getName())
				.append(description, rhs.getDescription())
				.append(store, rhs.getStore())
				.isEquals();
	}

}
