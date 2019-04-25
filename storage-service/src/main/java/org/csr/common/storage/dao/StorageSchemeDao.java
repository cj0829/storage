package org.csr.common.storage.dao;

import java.util.List;

import org.csr.common.storage.domain.StorageScheme;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.BaseDao;

/**
 * ClassName:StorageScheme.java <br/>
 * Date: Fri Jul 28 16:29:51 CST 2017 <br/>
 * 
 * @author XTM-01606101026 <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 资源路径2 dao接口
 */
public interface StorageSchemeDao extends BaseDao<StorageScheme,Long>{

	public PagedInfo<StorageScheme> findDropDownList(Page page,List<Long> selecteds);
}
