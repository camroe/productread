package com.gophergroceries.productread.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "subcategory")
public class SubCategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/*
	 * The @ManyToOne side is always the owner of the relationship. There is no
	 * way to use the mapped by attribute inside the @ManyToOne annotation
	 */
	@ManyToOne
	@JoinColumn(name = "category_FK")
	@JsonBackReference
	private CategoryEntity cat;

	private String displayname;

	private String urladdress;

	public CategoryEntity getCat() {
		return cat;
	}

	public void setCat(CategoryEntity cat) {
		this.cat = cat;
	}

	

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getUrladdress() {
		return urladdress;
	}

	public void setUrladdress(String urladdress) {
		this.urladdress = urladdress;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
}
