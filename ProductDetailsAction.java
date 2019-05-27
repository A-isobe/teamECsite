package com.internousdev.orion.action;

import java.util.ArrayList;
import java.util.List;

import com.internousdev.orion.dao.ProductInfoDAO;
import com.internousdev.orion.dto.ProductInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class ProductDetailsAction extends ActionSupport {

	private int id;
	private int productId;
	private String productName;
	private String productNameKana;
	private String productDescription;
	private int categoryId;
	private int price;
	private String imageFilePath;
	private String imageFileName;
	private List<Integer> productCountList;
	private String releaseDate;
	private String releaseCompany;
	private List<ProductInfoDTO> relatedProductList;
	private ProductInfoDTO productInfoDTO = new ProductInfoDTO();

	public String execute() {

		// 商品情報取得(一覧画面から渡されるproductIdを引数にして)

		ProductInfoDAO productInfoDAO = new ProductInfoDAO();
		productInfoDTO = productInfoDAO.getProductInfoByProductId(productId);

		// もしDTOに値がなければjspにnullを返す

		if(productInfoDTO.getProductId() == 0) {
			productInfoDTO = null;
		} else {
		// 購入個数のリストを作成
			productCountList = new ArrayList<Integer>();
			for (int i=1; i<=5; i++) {
				productCountList.add(i);
			}

		// 関連商品を探す
		relatedProductList = productInfoDAO.getRelatedProductList(productInfoDTO.getCategoryId(), productInfoDTO.getProductId(), 0, 3);

		/*
		 * List<ProductInfoDTO>型の変数relatedProductListに、ProductInfoDAOのgetRelatedProductListメソッドの実行結果を代入しています。
		 * （ここでProductInfoDTOをList型にするのは複数の商品を持ってくるため。＝複数のdtoを持ってくるためListに入れる）
		 * ProductInfoDAOのgetRelatedProductListメソッドの内容は、
		 * 例えばカテゴリーIDが2なら、2(本)のなかのその商品(現在詳細情報を表示している)以外の商品情報をproduct_infoテーブルの0行目から3つ取得してきます
		 */
		}
		return SUCCESS;
		}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNameKana() {
		return productNameKana;
	}

	public void setProductNameKana(String productNameKana) {
		this.productNameKana = productNameKana;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public List<Integer> getProductCountList() {
		return productCountList;
	}

	public void setProductCountList(List<Integer> productCountList) {
		this.productCountList = productCountList;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getReleaseCompany() {
		return releaseCompany;
	}

	public void setReleaseCompany(String releaseCompany) {
		this.releaseCompany = releaseCompany;
	}

	public List<ProductInfoDTO> getRelatedProductList() {
		return relatedProductList;
	}

	public void setRelatedProductList(List<ProductInfoDTO> relatedProductList) {
		this.relatedProductList = relatedProductList;
	}

	public ProductInfoDTO getProductInfoDTO() {
		return productInfoDTO;
	}

	public void setProductInfoDTO(ProductInfoDTO productInfoDTO) {
		this.productInfoDTO = productInfoDTO;
	}
}
