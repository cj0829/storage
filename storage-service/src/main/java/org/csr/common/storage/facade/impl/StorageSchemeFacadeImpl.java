package  org.csr.common.storage.facade.impl;

import java.util.List;

import javax.annotation.Resource;

import org.csr.common.storage.dao.StorageSchemeDao;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.entity.StorageSchemeBean;
import org.csr.common.storage.facade.StorageSchemeFacade;
import org.csr.core.page.Page;
import org.csr.core.page.PagedInfo;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.param.AndEqParam;
import org.csr.core.persistence.service.SetBean;
import org.csr.core.persistence.service.SimpleBasisFacade;
import org.csr.core.persistence.util.PersistableUtil;
import org.csr.core.util.ObjUtil;
import org.springframework.stereotype.Service;

/**
 * ClassName:StorageScheme.java <br/>
 * Date:     Fri Jul 28 16:29:51 CST 2017
 * @author   XTM-01606101026 <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 * 资源路径2 Facade实现
 */
 
@Service("storageSchemeFacade")
public class StorageSchemeFacadeImpl extends SimpleBasisFacade<StorageSchemeBean,StorageScheme, Long> implements StorageSchemeFacade {
	@Resource
	private StorageSchemeDao storageSchemeDao;

	@Override
	public BaseDao<StorageScheme, Long> getDao() {
		return storageSchemeDao;
	}
	
	@Override
	public StorageSchemeBean wrapBean(StorageScheme doMain) {
		return StorageSchemeBean.wrapBean(doMain);
	}
	
	@Override
	public StorageSchemeBean save(StorageScheme storageScheme){
		//如果需要判断这里更改
		storageSchemeDao.save(storageScheme);
		return wrapBean(storageScheme);
	}

	@Override
	public StorageSchemeBean update(StorageScheme storageScheme){
	
		//如果需要判断这里更改
		StorageScheme oldstorageScheme = storageSchemeDao.findById(storageScheme.getId());
		
		//大家需要检查一下，有些在domain对象中没有Commnt标签，是否需要在这里存在！
		oldstorageScheme.setName(storageScheme.getName());
		oldstorageScheme.setAddress(storageScheme.getAddress());
		oldstorageScheme.setResourcePath(storageScheme.getResourcePath());
		oldstorageScheme.setDescription(storageScheme.getDescription());
		oldstorageScheme.setDefaultConfig(storageScheme.getDefaultConfig());
		oldstorageScheme.setLocalServer(storageScheme.getLocalServer());
		oldstorageScheme.setProtocol(storageScheme.getProtocol());
		oldstorageScheme.setRemotedownurl(storageScheme.getRemotedownurl());
		oldstorageScheme.setVirPath(storageScheme.getVirPath());
		oldstorageScheme.setAccount(storageScheme.getAccount());
		oldstorageScheme.setPassword(storageScheme.getPassword());
		oldstorageScheme.setRemoterulepath(storageScheme.getRemoterulepath());
		oldstorageScheme.setRemoteruleext(storageScheme.getRemoteruleext());

		storageSchemeDao.update(oldstorageScheme);
		return wrapBean(oldstorageScheme);
	}
	

	public PagedInfo<StorageSchemeBean> findDropDownList(Page page,List<Long> selecteds){
		PagedInfo<StorageScheme> pages = storageSchemeDao.findDropDownList(page,selecteds);
		return PersistableUtil.toPagedInfoBeans(pages, new SetBean<StorageScheme>() {
			@Override
			public StorageSchemeBean setValue(StorageScheme doMain) {
				return wrapBean(doMain);
			}
		});
	}
	
	
	@Override
	public boolean checkNameIsExist(Long id, String name) {
		StorageScheme storageScheme = storageSchemeDao.existParam(new AndEqParam("name",name));
		if (ObjUtil.isNotEmpty(id)) {
		    if (ObjUtil.isEmpty(storageScheme) || storageScheme.getId().equals(id)) {
			    return false;
			}
		}
		if (ObjUtil.isEmpty(storageScheme)) {
		    return false;
		}
		return true;
	}

}
