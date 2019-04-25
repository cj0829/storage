package org.csr.common.storage.dao.impl;

import java.util.List;

import org.csr.common.storage.dao.StorageSchemeDao;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.Finder;
import org.csr.core.persistence.query.FinderImpl;
import org.csr.core.persistence.query.jpa.JpaDao;
import org.csr.core.util.ObjUtil;
import org.springframework.stereotype.Repository;


/**
 * ClassName:StorageScheme.java <br/>
 * Date: Fri Jul 28 16:29:51 CST 2017 <br/>
 * 
 * @author XTM-01606101026 <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 资源路径2 dao实现
 */
@Repository("storageSchemeDao")
public class StorageSchemeDaoImpl extends JpaDao<StorageScheme,Long> implements StorageSchemeDao{

	@Override
	public Class<StorageScheme> entityClass(){
		return StorageScheme.class;
	}
	
	public PagedInfo<StorageScheme> findDropDownList(Page page,List<Long> selecteds){
		Finder finder = FinderImpl.create("select t from StorageScheme t where 1=1");
		if(ObjUtil.isNotEmpty(selecteds)){
			finder.append(" or (t.id in (:selecteds))", "selecteds", selecteds);
		}
		return findPage(page,finder);
	}
}
