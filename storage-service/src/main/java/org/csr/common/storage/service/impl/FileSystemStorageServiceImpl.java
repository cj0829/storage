/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.message.LocalDatastreamMsg;
import org.csr.common.storage.service.DatastreamService;
import org.csr.common.storage.service.FileSystemStorageService;
import org.csr.common.storage.service.StorageSchemeService;
import org.csr.common.storage.supper.FileSystemContext;
import org.csr.common.storage.support.FileUploadSupport;
import org.csr.core.exception.Exceptions;
import org.csr.core.queue.QueueService;
import org.csr.core.util.ClassBeanFactory;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.crop.CropImg;
import org.csr.core.util.mm.MimeTypeTool;
import org.csr.core.util.mm.MimeUtil;
import org.springframework.stereotype.Service;

/**
 * @author caijin
 */
@Service("fileSystemStorageService")
public class FileSystemStorageServiceImpl implements FileSystemStorageService {

	private static Log log = LogFactory.getLog(FileSystemStorageServiceImpl.class);
	
	@Resource
	DatastreamService datastreamService;
	@Resource
	StorageSchemeService storageSchemeService;
	@Override
	public Datastream uploadFile(CropImg img, String fileName, String dirName)throws IOException {
		return uploadFile(img.getFile(), fileName, img.getLength(),img.getWidth(), img.getHeight(),dirName);
	}
	@Override
	public Datastream uploadFile(DiskFileItem srcFile, String fileName,String dirName) throws IOException {
		return uploadFile(new ByteArrayInputStream(srcFile.get()), fileName, srcFile.getSize(), dirName);
	}

	@Override
	public Datastream uploadFile(InputStream srcFile, String fileName, long fileSize , String dirName) throws IOException {
		return uploadFile(srcFile, fileName, fileSize, dirName,true);
	}
	
	
	@Override
	public Datastream uploadFile(InputStream srcFile, String fileName, long fileSize , String dirName,boolean localServer) throws IOException {
		StorageScheme storageScheme = storageSchemeService.getDefaultStorageScheme();
		if(ObjUtil.isEmpty(storageScheme)){
			Exceptions.service("", "上传策略没有配置");
			log.error("上传策略没有配置");
		}
		String prefixPath = FileSystemContext.getStorageStreamPathByType(dirName, false);
		MimeUtil.getMimeTypes(fileName);
		
		String contentType = MimeTypeTool.getMimeType(fileName);
		String extName = MimeTypeTool.getExtension(fileName);
		String filePath = FileSystemContext.generatorFilePath(prefixPath,extName);
		Datastream stream = new Datastream(fileName, contentType,filePath,fileSize,storageScheme.getId());
		Datastream domain = datastreamService.createFileDatastream(stream);
		uploadStreamFile(srcFile, filePath, storageScheme,stream,localServer);
		log.info("上传文件:[" + fileName + "]" + ">>>[" + fileName + "]成功");
		return domain;
	}
	
	
	private void uploadStreamFile(InputStream srcFile, String filePath,StorageScheme storage,Datastream stream, boolean localServer) throws IOException {
		StringBuffer defaultStreamRealPath = new StringBuffer();
		if(ObjUtil.isEmpty(storage)){
			StorageScheme storageScheme = storageSchemeService.findById(storageSchemeService.getDefaultFileLocationId());
			FileUploadSupport.copyFile(srcFile,storageScheme.getResourcePath(), filePath);
		}else{
			defaultStreamRealPath.append(storage.getResourcePath());
			//先保存到本地。然后 // 本地服务上传文件
			FileUploadSupport.copyFile(srcFile,defaultStreamRealPath.toString(), filePath);
			//如果是远程保存需要 保存成功之后，
			if(localServer){
				if (!storage.getLocalServer()) {//
//					File file = new FileSystemStorageServiceImpl();
					QueueService queueService = ClassBeanFactory.getBean("queueService", org.csr.core.queue.QueueService.class);
					LocalDatastreamMsg LocalDatastreamMsg = new LocalDatastreamMsg(storage,stream);
					if(ObjUtil.isNotEmpty(queueService)){
						queueService.sendMessage(LocalDatastreamMsg);
					}
				}
			}
		}
	}
	@Override
	public Datastream uploadFile(InputStream srcFile, String fileName, long fileSize , int width, int height, String dirName) throws IOException {
		StorageScheme storageScheme = storageSchemeService.getDefaultStorageScheme();
		if(ObjUtil.isEmpty(storageScheme)){
			Exceptions.service("", "上传策略没有配置");
		}
		String prefixPath = FileSystemContext.getStorageStreamPathByType(dirName, false);
		MimeUtil.getMimeTypes(fileName);
		
		String contentType = MimeTypeTool.getMimeType(fileName);
		String extName = MimeTypeTool.getExtension(fileName);
		String filePath = FileSystemContext.generatorFilePath(prefixPath,extName);
		Datastream stream = new Datastream(fileName, contentType,filePath,fileSize,storageScheme.getId());
		stream.setWidth(width);
		stream.setHeight(height);
		Datastream domain = datastreamService.createFileDatastream(stream);
		uploadStreamFile(srcFile, filePath, storageScheme,stream,true);
		log.info("上传文件:[" + fileName + "]" + ">>>[" + fileName + "]成功");
		return domain;
	}
}
