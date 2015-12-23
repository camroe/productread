package com.gophergroceries.productread.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gophergroceries.productread.entities.CategoryEntity;
import com.gophergroceries.productread.entities.ProductEntity;
import com.gophergroceries.productread.entities.SubCategoryEntity;
import com.gophergroceries.productread.repositories.CategoryRepository;
import com.gophergroceries.productread.repositories.ProductsRepository;

@Component
public class ProductReader {
	private static final Logger logger = LoggerFactory.getLogger(ProductReader.class);

	@Value("${com.grophergroceries.products.filename:data/products.xlsx}")
	private String FILE_PATH;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * Constructor
	 */
	public ProductReader() {
		logger.trace("ProductReader Constructed");
	}

	public void readProducts() {
		logger.debug("Products Repository is {}", productsRepository == null ? " NULL" : " NOT NULL");
		logger.debug("Category Repository is {}", categoryRepository == null ? " NULL" : " NOT NULL");
		logger.debug("File Name : {}", FILE_PATH);
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(FILE_PATH).getFile());
		logger.trace("File found " + file.getAbsolutePath());
		FileInputStream fis = null;
		Workbook workbook = null;
		try {
			fis = new FileInputStream(file);
			// InputStream is = getClass().getResourceAsStream(FILE_PATH);
			workbook = new XSSFWorkbook(fis);
			int numberOfSheets = workbook.getNumberOfSheets();
			logger.trace(numberOfSheets + " worksheets in workbook. ");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		if (rowIterator.hasNext()) {
			// get first row as header
			rowIterator.next();
		}
		ProductLine pl = new ProductLine();
		int count = 0;
		while (rowIterator.hasNext()) {
			// Process the rows.
			Row row = rowIterator.next();

			// Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
			// Category sub category product price store
			pl.setCategory(readString(row.getCell(0, Row.RETURN_BLANK_AS_NULL)));
			pl.setSubCategory(readString(row.getCell(1, Row.RETURN_BLANK_AS_NULL)));
			pl.setProductDescription(readString(row.getCell(2, Row.RETURN_BLANK_AS_NULL)));
			String price = readString(row.getCell(3, Row.RETURN_BLANK_AS_NULL));
			if (price.equals(""))
				price = "0";
			pl.setPrice(new BigDecimal(price));
			pl.setStore(readString(row.getCell(4, Row.RETURN_BLANK_AS_NULL)));

			updateProductTable(pl);
			count++;
		}
		logger.info(count + " rows processed from excel file.");
	}

	private void updateProductTable(ProductLine productLine) {
		CategoryEntity cat = new CategoryEntity();
		Set<SubCategoryEntity> subCats = new HashSet<SubCategoryEntity>();
		if (!categoryExists(productLine)) {
			cat.setName(productLine.getCategory());
			cat.setUrladdress("#");
			cat.setSubCats(subCats);
			cat = categoryRepository.saveAndFlush(cat);
			logger.debug("Created Category " + cat.getName() + " with ID: " + cat.getId());
		}
		else {
			cat = categoryRepository.findOneByName(productLine.getCategory());
			logger.debug("Found Category {} with ID: {}", cat.getName(), cat.getId());
		}
		SubCategoryEntity sce = new SubCategoryEntity();

		if (!subCategoryExists(productLine, cat.getSubCats())) {
			sce = new SubCategoryEntity();
			sce.setCat(cat);
			sce.setDisplayname(productLine.getSubCategory());
			sce.setUrladdress(buildUrlName(productLine));
			subCats.add(sce);
			cat.setSubCats(subCats);
			cat = categoryRepository.saveAndFlush(cat);
			logger.debug("Created SubCategory [{}] with urladdress: {}", sce.getDisplayname(), sce.getUrladdress());
		}
		else {
			// Do Nothing. At this point the category and subcategory exist and are in
			// the database.
			sce = findSubCategory(cat, productLine);
			if (null == sce)
				logger.error("SubCategoryEntity Not Found after we confirmed it was there. WTF?");
			else
				logger.debug("Found SubCategory [{}] with ID: {}", productLine.getSubCategory(), sce.getId());
		}
		ProductEntity pe = new ProductEntity();
		// TODO: Need to add store to Product Entity
		pe.setCategory(sce.getUrladdress());
		pe.setDescription(filterCharacters(productLine.getProductDescription()));
		pe.setImagefile("resources/images/photonotavailable.jpg");
		pe.setInventory(new Integer(0));
		pe.setName(filterCharacters(productLine.getProductDescription()));
		pe.setPrice(productLine.getPrice());
		pe.setSku("");
		pe.setStore(productLine.getStore());
		logger.trace(productLine.toString());
		pe = productsRepository.save(pe);
		logger.debug("Product Added to table. Id = {}. Name = {}", pe.getId(), pe.getName());
	}

	/**
	 * Find the SubCategoryEntity in the CategoryEntity that matches the
	 * subcategory in the productLine.
	 * 
	 * @param cat
	 * @param productLine
	 * @return null if the SubCategoryEntity can not be found.
	 */
	private SubCategoryEntity findSubCategory(CategoryEntity cat, ProductLine productLine) {
		Set<SubCategoryEntity> sceSet = cat.getSubCats();
		Iterator<SubCategoryEntity> sceIt = sceSet.iterator();
		SubCategoryEntity returnSce = null;
		while (sceIt.hasNext()) {
			SubCategoryEntity sce = sceIt.next();
			if (sce.getDisplayname().equals(productLine.getSubCategory())) {
				returnSce = sce;
			}
		}
		return returnSce;
	}

	private String buildUrlName(ProductLine productLine) {
		String urlName = productLine.getSubCategory();
		urlName = urlName.replaceAll("\\s+", "");
		urlName = urlName.replaceAll(",", "");
		urlName = urlName.replaceAll("&", "");
		urlName = urlName.replaceAll("'", "");
		urlName = urlName.replaceAll("\\.", "");
		urlName = urlName.replaceAll(" ", "");
		urlName = urlName.replaceAll("/", "-");
		urlName = urlName.toLowerCase();
		return urlName;
	}

	private boolean subCategoryExists(ProductLine productLine, Set<SubCategoryEntity> subCats) {
		boolean found = false;
		if (null != subCats) {
			// Need to iterate over list
			Iterator<SubCategoryEntity> it = subCats.iterator();
			while (it.hasNext()) {
				if (it.next().getDisplayname().equals(productLine.getSubCategory())) {
					found = true;
					break;
				}
			}
		}
		return found;
	}

	private boolean categoryExists(ProductLine productLine) {
		java.util.List<CategoryEntity> catList = categoryRepository.findAll();
		boolean found = false;
		if (null != catList) {
			// Need to iterate over list
			Iterator<CategoryEntity> it = catList.iterator();
			while (it.hasNext()) {
				if (it.next().getName().equals(productLine.getCategory())) {
					found = true;
					break;
				}
			}
		}
		return found;
	}

	private String readString(Cell cell) {
		String result = "";
		if (null == cell) {
			return result;
		}
		else {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					if (cell.getBooleanCellValue()) {
						result = "true";
					}
					else {
						result = "false";
					}
					break;
				case Cell.CELL_TYPE_NUMERIC:
					result = Double.toString(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_STRING:
					result = cell.getStringCellValue();
					break;
			}
		}
		return result;
	}

	private String filterCharacters(String filterString) {
		filterString.replace("'", "");
		filterString.replace("`", "");
		return filterString;
	}

}
