/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.supper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.entity.DownloadFileBean;
import org.csr.common.storage.entity.StorageSchemeBean;
import org.csr.core.Constants;
import org.csr.core.exception.Exceptions;
import org.csr.core.persistence.business.ParameterUtil;
import org.csr.core.util.ObjUtil;

/**
 * Defiend class file the com.elearning.storage.FileSystemContext.java
 * 
 * 
 * @author Rock.Lee
 * @version elearning 1.0
 * @since JDK-1.6.0
 * @date 2013-3-23下午9:11:22
 */
public abstract class FileSystemContext {

	private final static String ROOT_NAME = "repository";
	private final static String ROOT_PATH = "/" + ROOT_NAME;
	private final static String PATH_STREAM_FEATURE = "/feature"; // PATH_STREAM +
	private final static String PATH_STREAM_IMAGE = "/image"; // PATH_STREAM +
	private final static String PATH_STREAM_PHOTO = "/photo"; // PATH_STREAM +
	private final static String PATH_STREAM_FLASH = "/flash"; // PATH_STREAM +
	private final static String PATH_STREAM_MEDIA = "/media"; // PATH_STREAM +
	
	private final static String PATH_CAMERA_MEDIA = "/camera"; // PATH_STREAM +
	private final static String PATH_IDCARD_MEDIA = "/idcard"; // PATH_STREAM +
	
	private final static String PATH_STREAM_FILE = "/file"; // PATH_STREAM +
	private final static String PATH_STREAM_PDF = "/pdf"; // PATH_STREAM +
	private final static String PATH_STREAM_OFFICE = "/office"; // PATH_STREAM +
	private final static String PATH_APP_APK = "/apk"; // PATH_STREAM +

	private final static String STREAM_URL = "/file/ajax/download.action?id=";
	private final static String INLINE_URL = "/file/ajax/inline.action?id=";
	public final static String VIEW_URL = "/file/view.action?id=";

	public final static String TYPE_FEATURE = "feature";
	public final static String TYPE_IMAGE = "image";
	public final static String TYPE_PHOTO = "photo";
	public final static String TYPE_CAMERA = "camera";
	public final static String TYPE_ID_CARD = "idcard";
	public final static String TYPE_FLASH = "flash";
	public final static String TYPE_MEDIA = "media";
	public final static String TYPE_FILE = "file";
	public final static String TYPE_APK = "apk";
	public final static String TYPE_PDF = "pdf";
	public final static String TYPE_OFFICE = "office";// word,excel

	/**
	 * 储存策略
	 */
	private static Map<Long, StorageSchemeBean> storageMap = new HashMap<Long, StorageSchemeBean>();
	
	static DateFormat df = new SimpleDateFormat("/yyyy/MM/dd/HH/");

	public static final int BUFFER_SIZE = 4096;
	// 定义上传最大文件大小
	private static long maxSize = 102400 * 50000;
	// 定义允许上传的文件扩展名
	private static Map<String, String> contentTypMap = new HashMap<String, String>(7);
	static {
		contentTypMap.put("image", "gif,jpg,jpeg,png,bmp");
		contentTypMap.put("flash", "flv");
		contentTypMap.put("media","swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4");
		contentTypMap.put("pdf", "pdf");
		contentTypMap.put("txt", "txt");
		contentTypMap.put("word", "doc,docx");
		contentTypMap.put("photo", "gif,jpg,jpeg,png,bmp");
		contentTypMap.put("file","gif,jpg,jpeg,png,bmp,doc,docx,xml,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,pdf,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4");
	}

	public static void registerStorageScheme(Map<Long, StorageSchemeBean> storageMap){
		if(ObjUtil.isNotEmpty(storageMap)){
			FileSystemContext.storageMap.putAll(storageMap);
		}
	}
	

	private static String suffix(boolean suf) {
		return (suf ? "/" : "");
	}

	/**
	 * obtain relative path of resouce root from contextpath /. for example:
	 * '/repository'
	 * 
	 * @param suf
	 *            . if ture, add "/" on the end path.
	 */
	public static String getStorageRootPath(boolean suf) {
		return ROOT_PATH + suffix(suf);
	}

	public static String getStorageStreamPathByType(String type, boolean suf) {
		if (TYPE_IMAGE.equals(type)) {
			return getStreamImageStoragePath(suf);
		} else if (TYPE_FLASH.equals(type)) {
			return getStreamFlashStoragePath(suf);
		} else if (TYPE_PDF.equals(type)) {
			return getStreamPdfStoragePath(suf);
		} else if (TYPE_OFFICE.equals(type)) {
			return getStreamOfficeStoragePath(suf);
		} else if (TYPE_MEDIA.equals(type)) {
			return getStreamMediaStoragePath(suf);
		} else if (TYPE_PHOTO.equals(type)) {
			return getPhotoStoragePath(suf);
		}else if (TYPE_CAMERA.equals(type)) {
			return getCameraStoragePath(suf);
		}else if (TYPE_ID_CARD.equals(type)) {
			return getIdcardStoragePath(suf);
		}else if (TYPE_APK.equals(type)) {
			return getAppStoragePath(suf);
		}else if (TYPE_FEATURE.equals(type)) {
			return getStreamFeatureStoragePath(suf);
		}else {
			return getStreamFileStoragePath(suf);
		}
	}

	/**
	 * obtain relative path of resouce root from contextpath /. for example:
	 * '/repository/resource'
	 * 
	 * @param suf. if ture, add "/" on the end path.
	 */
	public static String getStreamFeatureStoragePath(boolean suf) {
		return PATH_STREAM_FEATURE + suffix(suf);
	}
	public static String getStreamPdfStoragePath(boolean suf) {
		return PATH_STREAM_PDF + suffix(suf);
	}
	
	public static String getStreamOfficeStoragePath(boolean suf) {
		return PATH_STREAM_OFFICE + suffix(suf);
	}

	public static String getPhotoStoragePath(boolean suf) {
		return PATH_STREAM_PHOTO + suffix(suf);
	}

	public static String getStreamImageStoragePath(boolean suf) {
		return PATH_STREAM_IMAGE + suffix(suf);
	}

	public static String getStreamFlashStoragePath(boolean suf) {
		return PATH_STREAM_FLASH + suffix(suf);
	}

	public static String getStreamMediaStoragePath(boolean suf) {
		return PATH_STREAM_MEDIA + suffix(suf);
	}

	public static String getStreamFileStoragePath(boolean suf) {
		return PATH_STREAM_FILE + suffix(suf);
	}

	public static String getCameraStoragePath(boolean suf) {
		return PATH_CAMERA_MEDIA + suffix(suf);
	}

	public static String getIdcardStoragePath(boolean suf) {
		return PATH_IDCARD_MEDIA + suffix(suf);
	}
	public static String getAppStoragePath(boolean suf) {
		return PATH_APP_APK + suffix(suf);
	}
	
	
	public static String generatorFilePath(String prefixPath, String extName) {
		String filePath = generatorFilePath(extName);
		if (StringUtils.isNotBlank(prefixPath)) {
			return prefixPath + filePath;
		}
		return filePath;
	}

	public static String generatorFilePath(String extName) {
		StringBuilder subPath = new StringBuilder(df.format(new Date()));
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		if (extName.startsWith(".")) {
			return subPath + uuid + extName;
		}
		return subPath + uuid + "." + extName;
	}

	/**
	 * 
	 * getFileStreamUrl: 描述方法的作用 <br/>
	 * @author caijin
	 * @param streamId
	 * @return
	 * @since JDK 1.7
	 */
	public static String getFileStreamAllUrl(StDatastream stream) {
		if(ObjUtil.isNotEmpty(stream)){
			StorageSchemeBean storageSchemeBean = storageMap.get(stream.getStorageId());
			if(ObjUtil.isNotEmpty(storageSchemeBean)){
				if(ObjUtil.isNotEmpty(stream.getRemoteFile()) && stream.getRemoteFile()){
					return storageSchemeBean.getRemotedownurl() + STREAM_URL + ObjUtil.toString(stream.getRemoteFileId());
				}else{
					return storageSchemeBean.getAddress() + STREAM_URL + ObjUtil.toString(stream.getId());
				}
			}else{
				return Constants.CXT + STREAM_URL + ObjUtil.toString(stream.getId());
			}
			
		}else{
			return "";
		}
	}
	
	/**
	 * getFileStreamUrl: 描述方法的作用 <br/>
	 * @author caijin
	 * @param streamId
	 * @return
	 * @since JDK 1.7
	 */
	public static String getFileInlineAllUrl(StDatastream stream) {
		if(ObjUtil.isNotEmpty(stream)){
			StorageSchemeBean storageSchemeBean = storageMap.get(stream.getStorageId());
			if(ObjUtil.isNotEmpty(storageSchemeBean)){
				if(ObjUtil.isNotEmpty(stream.getRemoteFile()) && stream.getRemoteFile()){
					return storageSchemeBean.getRemotedownurl() + INLINE_URL + ObjUtil.toString(stream.getRemoteFileId());
				}else{
					return storageSchemeBean.getAddress() + INLINE_URL + ObjUtil.toString(stream.getId());
				}
			}else{
				return Constants.CXT + INLINE_URL + ObjUtil.toString(stream.getId());
			}
			
		}else{
			return "";
		}
	}

	public static String getFlvInlineUrl(StDatastream stream) {
		if(ObjUtil.isNotEmpty(stream)){
			StorageSchemeBean storageSchemeBean = storageMap.get(stream.getStorageId());
			if(ObjUtil.isNotEmpty(storageSchemeBean)){
				if(ObjUtil.isNotEmpty(stream.getRemoteFile()) && stream.getRemoteFile()){
					return storageSchemeBean.getRemotedownurl()  + "/file/flv/" + ObjUtil.toString(stream.getRemoteFileId()) + "/video.flv";
				}else{
					return storageSchemeBean.getAddress()  + "/file/flv/" + ObjUtil.toString(stream.getId()) + "/video.flv";
				}
			}else{
				return Constants.CXT + INLINE_URL + ObjUtil.toString(stream.getId());
			}
			
		}else{
			return "";
		}
	}

	/**
	 * 用户头像默认的大图
	 */
	public static String getDefaultUserHead(StDatastream stream){
		String fileInlineUrl = FileSystemContext.getFileInlineAllUrl(stream);
		if (ObjUtil.isNotBlank(fileInlineUrl)) {
			return fileInlineUrl;
		} else {
			return Constants.CXT + "/css/img/user_img_120.jpg";
		}
	}
	/**
	 * 用户头像默认的中图
	 */
	public static String getDefaultUserMiddleHead(StDatastream stream){
		String fileInlineUrl = FileSystemContext.getFileInlineAllUrl(stream);
		if (ObjUtil.isNotBlank(fileInlineUrl)) {
			return fileInlineUrl;
		} else {
			return Constants.CXT + "/css/img/user_img_80.jpg";
		}
	}
	/**
	 * 用户头像默认的小图
	 */
	public static String getDefaultUserAvatar(StDatastream stream){
		String fileInlineUrl = FileSystemContext.getFileInlineAllUrl(stream);
		if (ObjUtil.isNotBlank(fileInlineUrl)) {
			return fileInlineUrl;
		} else {
			return Constants.CXT + "/css/img/user_img_50.jpg";
		}
	}
	
	public static boolean checkFileExtName(String contentType, String extName) {
		String extValue = contentTypMap.get(contentType);
		if (extValue == null) {
			extValue = contentTypMap.get("file");
		}
		String[] extNames = extValue.split(",");
		List<String> fileExtnames = Arrays.asList(extNames);
		return fileExtnames.contains(extName);
	}

	public static boolean checkFileContentType(String contentType) {
		Set<String> keSet = contentTypMap.keySet();
		return keSet.contains(contentType);
	}

	public static long getUploadFileMaxSize() {
		return maxSize;
	}

	public static FileSize getBigPicture() {
		FileSize rangeFile = getRangeFile(ParameterUtil.getParamValue(null,"big_picture"));
		if(ObjUtil.isEmpty(rangeFile)){
			Exceptions.service("", "系统参数没有设置大图");
		}
		return rangeFile;
	}

	public static FileSize getMiddlePicture() {
		FileSize rangeFile = getRangeFile(ParameterUtil.getParamValue(null,"middle_picture"));
		if(ObjUtil.isEmpty(rangeFile)){
			Exceptions.service("", "系统参数没有设置中图");
		}
		return rangeFile;
	}

	public static FileSize getThumbnail() {
		FileSize rangeFile = getRangeFile(ParameterUtil.getParamValue(null,"thumbnail"));
		if(ObjUtil.isEmpty(rangeFile)){
			Exceptions.service("", "系统参数没有设置小图");
		}
		return rangeFile;
	}

	private static FileSize getRangeFile(String range) {
		if (ObjUtil.isNotBlank(range)) {
			String[] split = range.split("\\*");
			if (ObjUtil.isNotEmpty(split) && split.length >= 2) {
				return new FileSize(ObjUtil.toInt(split[0]),ObjUtil.toInt(split[1]));
			}
		}
		return null;
	}
	

	public static DownloadFileBean downloadDatastream(Datastream stream) throws IOException {
		if (ObjUtil.isEmpty(stream)) {
			return null;
		}
		StorageSchemeBean storageSchemeBean = storageMap.get(stream.getStorageId());
		File file = new File(storageSchemeBean.getResourcePath() + stream.getFilePath());
		DownloadFileBean downloadFileBean = new DownloadFileBean(new FileInputStream(file));
		downloadFileBean.setFilePath(file.getPath());
		downloadFileBean.setId(stream.getId());
		downloadFileBean.setContentType(stream.getContentType());
		downloadFileBean.setExtName(stream.getExtName());
		downloadFileBean.setFileSize(stream.getFileSize());
		downloadFileBean.setName(stream.getName());
		downloadFileBean.setLastModified(stream.getLastModified());
		return downloadFileBean;
	}
	
	public static class FileSize {

		final int width, height;

		public FileSize(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

	}

	public static void main(String[] args) {
		System.out.println(generatorFilePath(".doc"));
	}

}
