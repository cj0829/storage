/**
 * Project Name:storage-facade
 * File Name:LocalDatastreamMsg.java
 * Package Name:org.csr.common.storage.message
 * Date:2017年7月14日下午12:12:28
 * Copyright (c) 2017, 博海云领版权所有 ,All rights reserved 
*/

package org.csr.common.storage.message;

import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.core.queue.Message;

/**
 * ClassName: LocalDatastreamMsg.java <br/>
 * System Name：    文件系统 <br/>
 * Date:     2017年7月14日下午12:12:28 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class LocalDatastreamMsg implements Message<StorageScheme>{
	
	final StorageScheme storageScheme;
	final Datastream stream;

	public LocalDatastreamMsg(StorageScheme storageScheme,Datastream stream){
		this.storageScheme=storageScheme;
		this.stream=stream;
	}
	
	public Datastream getStream() {
		return stream;
	}

	@Override
	public StorageScheme body() {
		return storageScheme;
	}
}

