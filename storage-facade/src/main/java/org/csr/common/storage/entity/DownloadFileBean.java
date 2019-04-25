/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.entity;

import java.io.InputStream;

import org.csr.core.web.bean.VOBase;

/**
 * ClassName:DatastreamBean.java <br/>
 * System Name： 文件系统 <br/>
 * Date: 2016年11月4日上午10:28:49 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class DownloadFileBean extends VOBase<Long> {
	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 5854361949874390432L;
	private Long id;
	private String name;
	private String contentType;
	private String entryFile;
	private long lastModified;
	private long fileSize;
	private final InputStream file;
	private String extName;
	private String filePath;

	public DownloadFileBean(InputStream file) {
		this.file = file;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getEntryFile() {
		return entryFile;
	}

	public void setEntryFile(String entryFile) {
		this.entryFile = entryFile;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public InputStream getFile() {
		return file;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
