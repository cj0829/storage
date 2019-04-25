package org.csr.common.storage.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.csr.core.Comment;
import org.csr.core.Persistable;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

/**
 * ClassName:StorageScheme.java <br/>
 * System Name： 文件系统 <br/>
 * Date: 2016年11月20日下午1:26:44 <br/>
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
@Table(name = "sg_StorageScheme")
@Comment(ch = "数据", en = "sg_StorageScheme")
public class StorageScheme implements Persistable<Long> {

	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = -7247791267733995881L;
	private Long id;
	private String name;
	private String address;
	private String resourcePath;
	private String description;
	private Boolean defaultConfig = false;
	private Boolean localServer = false;

	private String protocol;
	private String virPath;
	private String account;
	private String password;
	private String remotedownurl;
	private String remoterulepath;
	private String remoteruleext;

	public StorageScheme() {
		super();
	}

	public StorageScheme(String name, boolean defaultConfig) {
		this.name = name;
		this.defaultConfig = defaultConfig;
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

	@Column(name = "name", length = 127)
	@Length(min = 1, max = 127)
	@Comment(ch = "策略名称", en = "name", len = 127)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "address", length = 127)
	@Length(min = 1, max = 127)
	@Comment(ch = "地址", en = "address", len = 127)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "resourcePath", length = 64)
	@Length(min = 1, max = 64)
	@Comment(ch = "文件存储地址", en = "resourcePath", len = 64)
	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	@Column(name = "description", length = 127)
	@Length(min = 0, max = 127)
	@Comment(ch = "描述", en = "description", len = 127)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "defaultConfig")
	@Comment(ch = "是否是默认配置", en = "defaultConfig")
	public Boolean getDefaultConfig() {
		return defaultConfig;
	}

	public void setDefaultConfig(Boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	@Column(name = "localServer")
	@Comment(ch = "是否是本地服务", en = "localServer")
	public Boolean getLocalServer() {
		return localServer;
	}

	public void setLocalServer(Boolean localServer) {
		this.localServer = localServer;
	}

	@Column(name = "protocol", length = 127)
	@Length(min = 1, max = 127)
	@Comment(ch = "远程访问方法", en = "protocol", len = 127)
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Column(name = "remotedownurl", length = 127)
	@Length(min = 0, max = 127)
	@Comment(ch = "远端下载地址", en = "remotedownurl", len = 127)
	public String getRemotedownurl() {
		return remotedownurl;
	}

	public void setRemotedownurl(String remotedownurl) {
		this.remotedownurl = remotedownurl;
	}

	@Column(name = "virPath", length = 127)
	@Length(min = 0, max = 64)
	@Comment(ch = "远程路径", en = "virPath", len = 127)
	public String getVirPath() {
		return virPath;
	}

	public void setVirPath(String virPath) {
		this.virPath = virPath;
	}

	@Column(name = "account", length = 32)
	@Length(min = 0, max = 32)
	@Comment(ch = "访问远程的用户", en = "account", len = 32)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "password", length = 32)
	@Length(min = 0, max = 32)
	@Comment(ch = "访问远程的密码", en = "password", len = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "remoterulepath", length = 127)
	@Length(min = 0, max = 127)
	@Comment(ch = "上传的文件路径", en = "remoterulepath", len = 127)
	public String getRemoterulepath() {
		return remoterulepath;
	}

	public void setRemoterulepath(String remoterulepath) {
		this.remoterulepath = remoterulepath;
	}

	@Column(name = "remoteruleext", length = 127)
	@Length(min = 0, max = 127)
	@Comment(ch = "上传的文件后缀", en = "remoteruleext", len = 127)
	public String getRemoteruleext() {
		return remoteruleext;
	}

	public void setRemoteruleext(String remoteruleext) {
		this.remoteruleext = remoteruleext;
	}

}
