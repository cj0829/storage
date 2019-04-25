package org.csr.common.storage.facade;

import java.util.List;

import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.entity.StorageSchemeBean;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.service.BasisFacade;


/**
 * @(#)Sss.java 
 *       
 * ClassName:StorageScheme.java <br/>
 * Date: Fri Jul 28 16:29:51 CST 2017 <br/>
 * @author XTM-01606101026 <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 资源路径2 Facade接口
 */
public interface StorageSchemeFacade extends BasisFacade<StorageSchemeBean, Long>{

	/**
	 * save: 保存方法<br/>
	 * @author XTM-01606101026
	 * @param storageScheme 
	 */
	public StorageSchemeBean save(StorageScheme storageScheme);

	/**
	 * update: 编辑方法<br/>
	 * @author XTM-01606101026
	 * @param storageScheme 
	 */
	public StorageSchemeBean update(StorageScheme storageScheme);


	/**
	 * findDropDownList: 查询下拉的数据。如果当前有选择的，把选择的数据也查询到里面<br/>
	 * @author XTM-01606101026
	 * @param page
	 * @param selecteds
	 */
	public PagedInfo<StorageSchemeBean> findDropDownList(Page page,List<Long> selecteds);


	public boolean checkNameIsExist(Long id, String name);
}
