/**
 * Project Name:storage-facade
 * File Name:DatastreamFacade.java
 * Package Name:org.csr.common.storage.facade
 * Date:2016-12-2上午9:13:59
 * Copyright (c) 2016, csr版权所有 ,All rights reserved 
*/

package org.csr.common.storage.facade;

import org.csr.common.storage.entity.DatastreamBean;
import org.csr.core.persistence.service.BasisFacade;

/**
 * ClassName: DatastreamFacade.java <br/>
 * System Name：    文件系统 <br/>
 * Date:     2016-12-2上午9:13:59 <br/>
 * @author   admin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public interface DatastreamFacade extends BasisFacade<DatastreamBean, Long>{
	
	int cpoydb(String downLoadurl, String filePath);

	int cpoydbs(String downLoadurl, String filePath);

}

