package com.internousdev.orion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.orion.dto.ProductInfoDTO;
import com.internousdev.orion.util.DBConnector;

	public class ProductInfoDAO {

		//商品一覧ページ
		public ArrayList<ProductInfoDTO> getSyouhinDTO(){
			DBConnector db=new DBConnector();
			Connection con=db.getConnection();
			ArrayList<ProductInfoDTO> productInfoDTO=new ArrayList<ProductInfoDTO>();
			String sql="SELECT * FROM product_info";

			try{
				PreparedStatement ps=con.prepareStatement(sql);
				ResultSet rs=ps.executeQuery();

				while(rs.next()){
					ProductInfoDTO dto=new ProductInfoDTO();
					dto.setProductId(rs.getInt("product_id"));
					dto.setProductName(rs.getString("product_name"));
					dto.setProductNameKana(rs.getString("product_name_kana"));
					dto.setPrice(rs.getInt("price"));
					dto.setImageFilePath(rs.getString("image_file_path"));
					dto.setImageFileName(rs.getString("image_file_name"));

					productInfoDTO.add(dto);
				}

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					con.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			return productInfoDTO;
		}

		//商品詳細ページ
		public ProductInfoDTO getProductInfoByProductId(int productId) {
			DBConnector dbConnector = new DBConnector();
			Connection connection = dbConnector.getConnection();
			ProductInfoDTO productInfoDTO = new ProductInfoDTO();
			String sql = "select * from product_info where product_id=?";
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, productId);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					productInfoDTO.setId(resultSet.getInt("id"));
					productInfoDTO.setProductId(resultSet.getInt("product_id"));
					productInfoDTO.setProductName(resultSet.getString("product_name"));
					productInfoDTO.setProductNameKana(resultSet.getString("product_name_kana"));
					productInfoDTO.setProductDescription(resultSet.getString("product_description"));
					productInfoDTO.setCategoryId(resultSet.getInt("category_id"));
					productInfoDTO.setPrice(resultSet.getInt("price"));
					productInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
					productInfoDTO.setImageFileName(resultSet.getString("image_file_name"));
					productInfoDTO.setReleaseCompany(resultSet.getString("release_company"));
					//release_companyは日付と時刻までの情報がはいってるため、Date型に変えて日付までの情報を取り出しString型に変える。
					productInfoDTO.setReleaseDate(resultSet.getDate("release_date"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return productInfoDTO;
		}

		//関連商品
		public List<ProductInfoDTO> getRelatedProductList(int categoryId, int productId, int limitOffset,int limitRowCount) {
			DBConnector dbConnector = new DBConnector();
			Connection connection = dbConnector.getConnection();
			List<ProductInfoDTO> productInfoDTOList = new ArrayList<ProductInfoDTO>();

		//例えばカテゴリーIDが2なら、2(本)のなかのその商品(現在詳細情報を表示している)以外の商品情報をproduct_infoテーブルの0行目から3つ取得してきます
		//補足:product_infoテーブルの0行目からの3つの商品はorder by rand()で順番が毎回変わっているので取り出される3つの商品は毎回変わる。
		//limit ?,?で0,3 (0行目から3つの商品を取り出す指示をしていてもテーブルに関連する商品が3つない場合はある分だけで取り出す。
			String sql = "select * from product_info where category_id=? and product_id not in(?) order by rand() limit ?,?";

			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, categoryId);
				preparedStatement.setInt(2, productId);
				preparedStatement.setInt(3, limitOffset);
				preparedStatement.setInt(4, limitRowCount);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ProductInfoDTO productInfoDTO=new ProductInfoDTO();
					productInfoDTO.setProductId(resultSet.getInt("product_id"));
					productInfoDTO.setProductName(resultSet.getString("product_name"));
					productInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
					productInfoDTO.setImageFileName(resultSet.getString("image_file_name"));
					productInfoDTOList.add(productInfoDTO);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return productInfoDTOList;
		}

		//検索機能
		//カテゴリーを選択しないで検索したときに使用されるメソッド
		public List<ProductInfoDTO> getProductInfoListByKeyword(String[] keywordsList) {
			DBConnector dbConnector = new DBConnector();
			Connection connection = dbConnector.getConnection();
			List<ProductInfoDTO> productInfoDTOList = new ArrayList<ProductInfoDTO>();
			String sql = "select * from product_info where";
			// 1回目の処理と2回目以降の処理を分岐するためのフラグ
			boolean initializeFlag = true;
			for (String keyword : keywordsList) {
				if (initializeFlag) {

//					*前提としてproduct_infoテーブルのproduct_nameに「河原田」、「河原」、「原田」
//					product_name_kanaに「かわはらだ」、「かわはら」、「はらだ」がはいっているものとする。
//					product_name like %+keyword+% はkeywordの値が「原」の場合、product_nemeの「原」が含まれる
//					全てのデータが取り出される。
//					product_name_kana like %+keyword+% はkeywordの値が「はら」の場合、product_name_kanaの「はら」が含まれる
//					全てのデータが取り出される。

					// キーワードがいくつ入力されているかわからないので、for文でsqlを作成
					sql += " (product_name like '%" + keyword + "%' or product_name_kana like '%" + keyword + "%')";
					initializeFlag = false;
				} else {
					sql += " or (product_name like '%" + keyword + "%' or product_name_kana like '%" + keyword + "%')";
				}
			}

			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ProductInfoDTO productInfoDTO = new ProductInfoDTO();
					productInfoDTO.setId(resultSet.getInt("id"));
					productInfoDTO.setProductId(resultSet.getInt("product_id"));
					productInfoDTO.setProductName(resultSet.getString("product_name"));
					productInfoDTO.setProductNameKana(resultSet.getString("product_name_kana"));
					productInfoDTO.setProductDescription(resultSet.getString("product_description"));
					productInfoDTO.setCategoryId(resultSet.getInt("category_id"));
					productInfoDTO.setPrice(resultSet.getInt("price"));
					productInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
					productInfoDTO.setImageFileName(resultSet.getString("image_file_name"));
					productInfoDTO.setReleaseCompany(resultSet.getString("release_company"));
					productInfoDTOList.add(productInfoDTO);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return productInfoDTOList;
		}

		//上のメソッドとは違いカテゴリーを選択した上で検索したときに使用されるメソッド
		public List<ProductInfoDTO> getProductInfoListByCategoryIdAndKeyword(String[] keywordsList, String categoryId) {
			DBConnector dbConnector = new DBConnector();
			Connection connection = dbConnector.getConnection();
			List<ProductInfoDTO> productInfoDTOList = new ArrayList<ProductInfoDTO>();
			String sql = "select * from product_info where";
			boolean initializeFlag = true;
			for (String keyword : keywordsList) {
				if (initializeFlag) {
					sql += " category_id=" + categoryId + " and ((product_name like '%" + keyword + "%' or product_name_kana like '%" + keyword + "%')";
					initializeFlag = false;
				} else {
					sql += " or (product_name like '%" + keyword + "%' or product_name_kana like '%" + keyword + "%')";
				}
			}
			sql += ")";

			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ProductInfoDTO productInfoDTO = new ProductInfoDTO();
					productInfoDTO.setId(resultSet.getInt("id"));
					productInfoDTO.setProductId(resultSet.getInt("product_id"));
					productInfoDTO.setProductName(resultSet.getString("product_name"));
					productInfoDTO.setProductNameKana(resultSet.getString("product_name_kana"));
					productInfoDTO.setProductDescription(resultSet.getString("product_description"));
					productInfoDTO.setCategoryId(resultSet.getInt("category_id"));
					productInfoDTO.setPrice(resultSet.getInt("price"));
					productInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
					productInfoDTO.setImageFileName(resultSet.getString("image_file_name"));
					productInfoDTO.setReleaseCompany(resultSet.getString("release_company"));
					productInfoDTOList.add(productInfoDTO);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return productInfoDTOList;
		}
}
