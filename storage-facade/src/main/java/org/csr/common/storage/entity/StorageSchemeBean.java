package org.csr.common.storage.entity;

import org.csr.common.storage.domain.StorageScheme;
import org.csr.core.web.bean.VOBase;

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
public class StorageSchemeBean extends VOBase<Long> {

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

	public StorageSchemeBean() {
		super();
	}

	public StorageSchemeBean(String name, boolean defaultConfig) {
		this.name = name;
		this.defaultConfig = defaultConfig;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDefaultConfig() {
		return defaultConfig;
	}

	public void setDefaultConfig(Boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	public Boolean getLocalServer() {
		return localServer;
	}

	public void setLocalServer(Boolean localServer) {
		this.localServer = localServer;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRemotedownurl() {
		return remotedownurl;
	}

	public void setRemotedownurl(String remotedownurl) {
		this.remotedownurl = remotedownurl;
	}

	public String getVirPath() {
		return virPath;
	}

	public void setVirPath(String virPath) {
		this.virPath = virPath;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemoterulepath() {
		return remoterulepath;
	}

	public void setRemoterulepath(String remoterulepath) {
		this.remoterulepath = remoterulepath;
	}

	public String getRemoteruleext() {
		return remoteruleext;
	}

	public void setRemoteruleext(String remoteruleext) {
		this.remoteruleext = remoteruleext;
	}

	public static StorageSchemeBean wrapBean(StorageScheme doMain) {
		if (null == doMain) {
			return null;
		}
		StorageSchemeBean bean = new StorageSchemeBean();
		bean.setId(doMain.getId());
		bean.setName(doMain.getName());
		bean.setAddress(doMain.getAddress());
		bean.setResourcePath(doMain.getResourcePath());
		bean.setDescription(doMain.getDescription());
		bean.setDefaultConfig(doMain.getDefaultConfig());
		bean.setLocalServer(doMain.getLocalServer());
		bean.setProtocol(doMain.getProtocol());
		bean.setRemotedownurl(doMain.getRemotedownurl());
		bean.setVirPath(doMain.getVirPath());
		bean.setAccount(doMain.getAccount());
		bean.setPassword(doMain.getPassword());
		bean.setRemoterulepath(doMain.getRemoterulepath());
		bean.setRemoteruleext(doMain.getRemoteruleext());
		return bean;
	}
}
