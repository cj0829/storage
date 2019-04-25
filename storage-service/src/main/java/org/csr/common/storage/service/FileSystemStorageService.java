package org.csr.common.storage.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.csr.common.storage.domain.Datastream;
import org.csr.core.util.crop.CropImg;

/**
 *  * 文件系统处理，主要处理一下几种类型的文件处理： 1、处理编辑器里面插入的文件 2、处理其他需要存入到Datastream对象中的附件文件上传
 * 3、处理资源上传文件
 * @author caijin
 *
 */
public interface FileSystemStorageService {

	
	/**
	 * 上传文件，根据存储方案配置将文件上传存储到文件系统，同时创建一个DatastreamBean对象存储相关上传的文件信息
	 * 该接口主要用于编辑器中需要上传文件的地方调用，页面中需要上传文件的地方建议调用上面的接口
	 * 
	 * @param srcFile 需要上传的源文件
	 * @param fileName 文件名
	 * @param dirName 目录名，编辑器需要根据该上传目录对上传的文件进行分类存储，在编辑器中的文件空间中使用的到()
	 * @return 返回一个DatastreamBean对象
	 * @throws IOException 上传失败抛出该异常
	 */
	public Datastream uploadFile(CropImg bigImg, String fileName, String dirName)throws IOException;
	
	/**
	 * 上传文件，根据存储方案配置将文件上传存储到文件系统，同时创建一个DatastreamBean对象存储相关上传的文件信息
	 * 该接口主要用于编辑器中需要上传文件的地方调用，页面中需要上传文件的地方建议调用上面的接口
	 * 
	 * @param srcFile 需要上传的源文件
	 * @param fileName 文件名
	 * @param dirName 目录名，编辑器需要根据该上传目录对上传的文件进行分类存储，在编辑器中的文件空间中使用的到()
	 * @return 返回一个DatastreamBean对象
	 * @throws IOException 上传失败抛出该异常
	 */
	public Datastream uploadFile(DiskFileItem srcFile, String fileName, String dirName)throws IOException;
	
	public Datastream uploadFile(InputStream srcFile, String fileName, long fileSize, String dirName) throws IOException;
	
	public Datastream uploadFile(InputStream srcFile, String fileName, long fileSize,String dirName, boolean localServer) throws IOException;

	Datastream uploadFile(InputStream srcFile, String fileName, long fileSize,int width, int height, String dirName) throws IOException;
	
}
