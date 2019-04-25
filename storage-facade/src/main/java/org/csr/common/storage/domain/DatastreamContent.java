package org.csr.common.storage.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.io.IOUtils;
import org.csr.core.Comment;
import org.csr.core.Persistable;
import org.csr.core.util.ObjUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * ClassName:DatastreamContent.java <br/>
 * System Name： 文件系统 <br/>
 * Date: 2016年11月3日下午3:52:36 <br/>
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
@Table(name = "sg_DatastreamContent")
@Comment(ch = "数据", en = "sg_DatastreamContent")
public class DatastreamContent implements Persistable<Long> {

	private static final long serialVersionUID = 6067260673103436166L;
	private Long id;
	private Datastream datastream;
	private byte[] content = null;

	DatastreamContent() {
	}

	public DatastreamContent(Long id) {
		this.id = id;
	}

	DatastreamContent(Long id, byte[] content) {
		this.id = id;
		this.content = content;
	}

	public DatastreamContent(Long id, String content) {
		this.id = id;
		setBlobContent(content);
	}

	@Id
	@Column(name = "id")
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
	public Datastream getDatastream() {
		return datastream;
	}

	public void setDatastream(Datastream datastream) {
		this.datastream = datastream;
	}
	
	@Column(name = "content",length=100000)
	@Comment(ch = "内容", en = "content",len=100000)
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Transient
	public String getBlobContent() {
		String str = null;
		if (this.content != null) {
			try {
				str = IOUtils.toString(new ByteArrayInputStream(content));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	@Transient
	public void setBlobContent(String content) {
		if (ObjUtil.isNotBlank(content)) {
			try {
				// Constant.UTF8_ENCODING
				byte[] contentBytes = content.getBytes();
				byte[] blobBytes = IOUtils.toByteArray(new ByteArrayInputStream(contentBytes));
				setContent(blobBytes);
				datastream.setFileSize(new Long(blobBytes.length));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
