/**
 * Project Name:storage-facade
 * File Name:CustomerId.java
 * Package Name:org.csr.common.storage.domain
 * Date:2016年11月9日下午3:41:53
 * Copyright (c) 2016, csr版权所有 ,All rights reserved 
 */

package org.csr.common.storage.domain;

import java.io.Serializable;

/**
 * ClassName: CustomerId.java <br/>
 * System Name： 文件系统 <br/>
 * Date: 2016年11月9日下午3:41:53 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class CustomerId implements Serializable {

	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 6019678834072204572L;
	private Datastream id;

	public CustomerId() {}

	public CustomerId(Datastream id) {
		this.id = id;
	}

	public Datastream getId() {
		return id;
	}

	public void setId(Datastream id) {
		this.id = id;
	}

}
