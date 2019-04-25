/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.entity;

import org.csr.common.storage.constant.DatastreamConstant;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.supper.FileSystemContext;
import org.csr.common.storage.supper.StDatastream;
import org.csr.core.util.ObjUtil;
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
public class DatastreamBean extends VOBase<Long> implements StDatastream{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String contentType;
	private String content;
	private long lastModified;
	private String url;
	private String message;
	private long fileSize;
	private String filePath;
	private Long storageId;
	private String extName;
	private Boolean remoteFile;
	private Long remoteFileId;
	
	public DatastreamBean() {
		super();
	}
	/**
	 * 
	 * @param key
	 * @param contentType
	 */
	public DatastreamBean(Long id, String contentType) {
		setId(id);
		this.contentType = contentType;
	}

	/**
	 * Constructors one displayItemBean object with key, contentType, title
	 * 
	 * @param key
	 * @param contentType
	 * @param name
	 */
	public DatastreamBean(Long id, String contentType, String name) {
		this(id, name);
		this.contentType = contentType;
	}
	/**
	 * Constructors one DisplayItem with string value content
	 * contentType='text/html;stream=1' title='' key=null
	 * 
	 * @param content
	 *            String displayItem's contnet
	 */
	public DatastreamBean(String content) {
		// this(null, "");
		this.contentType = DatastreamConstant.STREAMTYPE;
		this.content = content;
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
	/**
	 * @return the extName
	 */
	public String getExtName() {
		return extName;
	}

	/**
	 * @param extName
	 *            the extName to set
	 */
	public void setExtName(String extName) {
		this.extName = extName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getStorageId() {
		return storageId;
	}

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}

	public void setContent(String content) {
		this.content = content;
		;
	}

	public String getContent() {
		return this.content;
	}

	/**
	 * get dispalyItem object's contentType
	 * 
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * set dispalyItem's contentType
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + (int) (fileSize ^ (fileSize >>> 32));
		result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((storageId == null) ? 0 : storageId.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatastreamBean other = (DatastreamBean) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (fileSize != other.fileSize)
			return false;
		if (lastModified != other.lastModified)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (storageId == null) {
			if (other.storageId != null)
				return false;
		} else if (!storageId.equals(other.storageId))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return FileSystemContext.getFileInlineAllUrl(this);
	}

	public static DatastreamBean toBean(Datastream doMain) {
		if(ObjUtil.isEmpty(doMain)){
			return null;
		}
		DatastreamBean bean = new DatastreamBean();
		bean.setId(doMain.getId());
		bean.setName(doMain.getName());
		bean.setExtName(doMain.getExtName());
		bean.setContentType(doMain.getContentType());
		bean.setFilePath(doMain.getFilePath());
		bean.setFileSize(doMain.getFileSize());
		bean.setContentType(doMain.getContentType());
		bean.setLastModified(doMain.getLastModified());
		bean.setStorageId(doMain.getStorageId());
		bean.setUrl(FileSystemContext.getFileInlineAllUrl(doMain));
		bean.remoteFile=doMain.getRemoteFile();
		bean.remoteFileId=doMain.getRemoteFileId();
		return bean;
	}
	@Override
	public Boolean getRemoteFile() {
		return remoteFile;
	}
	@Override
	public Long getRemoteFileId() {
		
		return remoteFileId;
	}

}
