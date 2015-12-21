package com.gophergroceries.productread.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "category")
public class CategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String urladdress;

	@OneToMany(mappedBy = "cat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OrderBy("displayname")
	@JsonManagedReference
	private Set<SubCategoryEntity> subCats;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrladdress() {
		return urladdress;
	}

	public void setUrladdress(String uurladdress) {
		this.urladdress = uurladdress;
	}

	public Set<SubCategoryEntity> getSubCats() {
		return subCats;
	}

	public void setSubCats(Set<SubCategoryEntity> subCats) {
		this.subCats = subCats;
	}

	public Integer getId() {
		return id;
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
