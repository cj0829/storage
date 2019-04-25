package org.csr.common.storage.service.impl;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.csr.common.storage.dao.StorageSchemeDao;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.entity.StorageSchemeBean;
import org.csr.common.storage.service.StorageSchemeService;
import org.csr.common.storage.supper.FileSystemContext;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.service.SetBean;
import org.csr.core.persistence.service.SimpleBasisService;
import org.csr.core.persistence.util.PersistableUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.PropertiesUtil;
import org.springframework.stereotype.Service;

/**
 * ClassName:StorageScheme.java <br/>
 * Date:     Fri Jul 28 16:29:51 CST 2017
 * @author   XTM-01606101026 <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 * 资源路径2 service实现
 */
@Service("storageSchemeService")
public class StorageSchemeServiceImpl extends SimpleBasisService<StorageScheme, Long> implements StorageSchemeService {
	private final static Long schemeDefault=ObjUtil.toLong(PropertiesUtil.getConfigureValue("storage.scheme.default"));

	public final static Long DEFAULT_FILE_LOCATION = schemeDefault == null ?1l:schemeDefault;
	
    @Resource
    private StorageSchemeDao storageSchemeDao;
    
    @Override
	public BaseDao<StorageScheme, Long> getDao() {
		return storageSchemeDao;
	}
    
	@PostConstruct
	protected void registerStorageScheme(){
		List<StorageScheme> allList = storageSchemeDao.findAll();
		List<StorageSchemeBean> listBeans = PersistableUtil.toListBeans(allList, new SetBean<StorageScheme>() {
			@Override
			public StorageSchemeBean setValue(StorageScheme domain) {
				return StorageSchemeBean.wrapBean(domain);
			}
		});
		FileSystemContext.registerStorageScheme(PersistableUtil.toMap(listBeans));
	}
	
    
    @Override
	public StorageScheme getDefaultStorageScheme() {
		return storageSchemeDao.findById(DEFAULT_FILE_LOCATION);
	}
	
	@Override
	public Long getDefaultFileLocationId() {
		return DEFAULT_FILE_LOCATION;
	}
}
