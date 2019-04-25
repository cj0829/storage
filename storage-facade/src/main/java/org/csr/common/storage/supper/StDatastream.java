/**
 * Project Name:storage-facade
 * File Name:StDatastream.java
 * Package Name:org.csr.common.storage.supper
 * Date:2017年7月27日下午10:21:54
 * Copyright (c) 2017, 博海云领版权所有 ,All rights reserved 
*/

package org.csr.common.storage.supper;

import org.csr.core.Persistable;

/**
 * ClassName: StDatastream.java <br/>
 * System Name：    文件系统 <br/>
 * Date:     2017年7月27日下午10:21:54 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public interface StDatastream extends Persistable<Long>{
	/**
	 * 获取存储策略
	 * getStorageId: 描述方法的作用 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public Long getStorageId();
	
	
	/**
	 * 获取文件是否为远程文件
	 * getRemoteFile: 描述方法的作用 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public Boolean getRemoteFile();
	
	

	/**
	 * 获取文件是否为远程文件Id
	 * getRemoteFile: 描述方法的作用 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public Long getRemoteFileId();
}

