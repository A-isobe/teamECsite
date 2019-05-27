<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="./css/orion.css">
	<link rel="stylesheet" href="./css/productDetails.css">
	<link rel="stylesheet" href="./css/error.css">
	<link rel="stylesheet" type="text/css" href="./css/header.css">

	<title>商品詳細</title>

</head>
<body>
<jsp:include page="header.jsp" />
	<div class="main-container clearfix" id="contents">
	<div class="page-title">
	 	<h1>商品詳細</h1>
	</div>

	<!-- ProductDetailsAction内でDTOにproductIdがあることがわかればtrueとなりs:if内を表示します -->

	<s:if test="productInfoDTO != null">

	<s:form action="AddCartAction">
	<div class="box">
	<div class="2column-container">
	<div class="right">
		<img src='<s:property value="productInfoDTO.imageFilePath"/>/<s:property value="productInfoDTO.imageFileName"/>' class="item-image-box-320"/><br>
	</div>
	<div class="left">
	<table class="vertical-list-table-mini">
		<tr>
		<th scope="row"><s:label value="商品名"/></th>
		<td><s:property value="productInfoDTO.productName"/></td>
		</tr>
		<tr>
		<th scope="row"><s:label value="商品名ふりがな"/></th>
		<td><s:property value="productInfoDTO.productNameKana"/></td>
		</tr>
		<tr>
		<th scope="row"><s:label value="値段"/></th>
		<td><s:property value="productInfoDTO.price"/>円</td>
		</tr>

<!-- ProductDetailsAction内のfor文でproductCountListに5までの値を入れ、selectタグでプルダウンにし表示 -->
		<tr>
		<th scope="row"><s:label value="購入個数"/></th>
		<td><s:select name="productCount" list="%{productCountList}"/>個</td>
		</tr>
		<tr>
		<th scope="row"><s:label value="発売会社名"/></th>
		<td><s:property value="productInfoDTO.releaseCompany"/></td>
		</tr>
		<tr>
		<th scope="row"><s:label value="発売年月日"/></th>
		<td><s:property value="productInfoDTO.releaseDate"/></td>
		</tr>
		<tr>
		<th scope="row"><s:label value="商品詳細情報"/></th>
		<td><s:property value="productInfoDTO.productDescription"/></td>
		</tr>
		</table>
	</div>
	</div>

<!-- ユーザーに見えない方法でAddCartActionに値を送信しています。
	 %{ }はognl式というjsp記述方法で、valueの値を取得しname(productId)へ入れています -->

	<s:hidden name="productId" value="%{productInfoDTO.productId}"/>

	</div>
	<div class="submit_btn_box">
	<s:submit value="カートに追加" class="submit_btn btn-field" />
	</div>
	</s:form>

	<s:if test="relatedProductList!=null && relatedProductList.size()>0">
		<div class="box">
			<div class="product-details-recomｍend-box">
			<h2>【関連商品】</h2>
			<s:iterator value="relatedProductList">
					<div class="recommend-box">
						<a href='<s:url action="ProductDetailsAction">
						<s:param name="productId" value="%{productId}"/>
						</s:url>'>
						<img src='<s:property value="imageFilePath"/>/<s:property value="imageFileName"/>' class="item-image-box-100"/>
						<s:property value="productName"/><br>
						</a>
					</div>
			</s:iterator>
			</div>
		</div>
	</s:if>
</s:if>
<s:else>
	<div class="error">
		商品の詳細情報がありません。
	</div>
</s:else>
</div>
</body>
</html>