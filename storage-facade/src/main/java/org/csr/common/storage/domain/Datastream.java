package org.csr.common.storage.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.io.FilenameUtils;
import org.csr.common.storage.supper.StDatastream;
import org.csr.core.Comment;
import org.csr.core.util.ObjUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

/**
 * ClassName:Datastream.java <br/>
 * System Name： 文件系统 <br/>
 * Date: 2016年11月3日下午3:53:20 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
@Entity
@DynamicInsert(true)
@DynamicUpdate(true)
@Table(name = "sg_Datastream")
@Comment(ch = "数据", en = "sg_Datastream")
public class Datastream implements StDatastream {

	private static final long serialVersionUID = 50003496262365603L;
	private Long id;
	private long lastModified;
	private String filePath;
	private Long storageId;
	private String extName;
	private Long fileSize;

	private String name;
	private String contentType;
	private Boolean remoteFile=false;
	private Long remoteFileId;

	private Integer width;
	private Integer height;
	
	public Datastream() {
	}

	public Datastream(Long id) {
		this.id = id;
	}

	public Datastream(String name, String contentType) {
		this.contentType = contentType;
		this.name = name;
		if (ObjUtil.isNotBlank(name)) {
			this.extName = FilenameUtils.getExtension(name);
		}
	}

	public Datastream(String name, String contentType, String filePath,
			long fileSize, Long storageId) {
		this(name, contentType);
		this.storageId = storageId;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.lastModified = System.currentTimeMillis();
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "globalGenerator")
	@GenericGenerator(name = "globalGenerator", strategy = "org.csr.core.persistence.generator5.GlobalGenerator")
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the lastModified time of this Datastream
	 */

	@Column(name = "lastModified")
	@Comment(ch = "最后修改时间", en = "lastModified")
	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the name for this blob
	 */
	@Column(name = "name", length = 63)
	@Length(min = 1, max = 63)
	@Comment(ch = "标题", en = "name", len = 63)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if (ObjUtil.isNotBlank(name)) {
			this.extName = FilenameUtils.getExtension(name);
		}
	}


	@Column(name = "contentType", length = 31)
	@Length(min = 1, max = 31)
	@Comment(ch = "内容类型", en = "contentType", len = 31)
	public String getContentType() {
		return contentType;
	}

	/**
	 * Where this di content is saved in file
	 */
	@Column(name = "filePath", length = 127)
	@Length(min = 1, max = 127)
	@Comment(ch = "文件相对路径", en = "filePath", len = 127)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Sets the type.
	 * @param type The type to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Column(name = "extName", length = 16)
	@Length(min = 0, max = 16)
	@Comment(ch = "扩展名", en = "extName", len = 16)
	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	@Column(name = "storageId")
	@Comment(ch = "存储方案", en = "storageId")
	public Long getStorageId() {
		return storageId;
	}

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}

	@Column(name = "fileSize")
	@Comment(ch = "附件大小", en = "fileSize")
	public Long getFileSize() {
		if (fileSize != null) {
			return fileSize;
		}
		return 0L;
	}

	
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "remoteFile")
	@Comment(ch = "是否为远程图片", en = "remoteFile")
	public Boolean getRemoteFile() {
		return remoteFile;
	}

	public void setRemoteFile(Boolean remoteFile) {
		this.remoteFile = remoteFile;
	}


	@Column(name = "remoteFileId")
	@Comment(ch = "远程文件id", en = "remoteFileId")
	public Long getRemoteFileId() {
		return remoteFileId;
	}

	public void setRemoteFileId(Long remoteFileId) {
		this.remoteFileId = remoteFileId;
	}

	@Column(name = "width")
	@Comment(ch = "图片宽度", en = "width")
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	@Column(name = "height")
	@Comment(ch = "图片高度", en = "height")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result
				+ ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result
				+ ((fileSize == null) ? 0 : fileSize.hashCode());
		result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((storageId == null) ? 0 : storageId.hashCode());
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
		Datastream other = (Datastream) obj;
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
		if (fileSize == null) {
			if (other.fileSize != null)
				return false;
		} else if (!fileSize.equals(other.fileSize))
			return false;
		if (lastModified != other.lastModified)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (storageId == null) {
			if (other.storageId != null)
				return false;
		} else if (!storageId.equals(other.storageId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Datastream [lastModified=" + lastModified + ", filePath="
				+ filePath + ", storageKey=" + storageId + ", extName="
				+ extName + ", fileSize=" + fileSize + ", name=" + name
				+ ", contentType=" + contentType
				+ "]";
	}

}
