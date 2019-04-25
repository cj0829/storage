package org.csr.common.storage.service;


import org.csr.common.storage.domain.StorageScheme;
import org.csr.core.persistence.service.BasisService;

/**
 * @(#)Sss.java 
 *       
 * ClassName:StorageScheme.java <br/>
 * Date: Fri Jul 28 16:29:51 CST 2017 <br/>
 * @author XTM-01606101026 <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 资源路径2 service接口
 */
public interface StorageSchemeService extends BasisService<StorageScheme, Long> {

	  public StorageScheme getDefaultStorageScheme();
		

		public Long getDefaultFileLocationId();

}
