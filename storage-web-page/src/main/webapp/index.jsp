<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<body>
	<h2>Hello World!</h2>


	<form action="${cxt}/file/ajax/upload.action" enctype="multipart/form-data" method="post">
		用户<input name="userId" value="1007"> 上传图片位置<input name="index" value="1"> 
		人脸<input name="phont" type="file">
		<button type="submit">上传图片</button>
	</form>
	
	
	<form action="${cxt}/file/ajax/uploadFile.action" enctype="multipart/form-data" method="post">
		用户<input name="userId" value="1007"> 上传图片位置<input name="index" value="1"> 
		人脸<input name="phont" type="file">
		<button type="submit">上传图片</button>
	</form>
</body>
</html>
