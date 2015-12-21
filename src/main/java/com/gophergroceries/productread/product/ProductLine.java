package com.gophergroceries.productread.product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ProductLine {

	private String category;
	private String subCategory;
	private String productDescription;
	private BigDecimal price;
	private String store;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append("Category:")
				.append("[")
				.append(category)
				.append("]  ")
				.append("SubCategory:")
				.append("[")
				.append(subCategory)
				.append("]  ")
				.append("Description:")
				.append("[")
				.append(productDescription)
				.append("]  ")
				.append("Price:")
				.append("[")
				.append(price)
				.append("]  ")
				.append("Store:")
				.append("[")
				.append(store)
				.append("]  ");
		return sb.toString();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		if (!category.equals(""))
			this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		if (!subCategory.equals(""))
			this.subCategory = subCategory;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		if (!productDescription.equals(""))
			this.productDescription = productDescription;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		BigDecimal b = price.round(new MathContext(5, RoundingMode.HALF_UP));
		this.price = b;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		if (!store.equals(""))
			this.store = store;
	}

}
