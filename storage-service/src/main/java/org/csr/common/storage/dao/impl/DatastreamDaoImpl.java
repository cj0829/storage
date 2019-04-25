/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.csr.common.storage.constant.DatastreamConstant;
import org.csr.common.storage.dao.DatastreamContentDao;
import org.csr.common.storage.dao.DatastreamDao;
import org.csr.common.storage.domain.Datastream;
import org.csr.core.persistence.query.jpa.JpaDao;
import org.springframework.stereotype.Repository;

/**
 * Defiend class file the
 * com.elearning.core.datastream.dao.DatastreamDaoImpl.java
 * 
 * 
 * @author Rock.Lee
 * @version elearning 1.0
 * @since JDK-1.6.0
 * @date 2013-3-23下午10:25:56
 */
@Repository
public class DatastreamDaoImpl extends JpaDao<Datastream, Long> implements
		DatastreamDao {

	@Resource
	DatastreamContentDao datastreamContentDao;

	@Override
	public Class<Datastream> entityClass() {
		return Datastream.class;
	}

	public Datastream createDatastream(String content) {
		Datastream ds = new Datastream();
		ds.setLastModified(System.currentTimeMillis());
		ds.setName(ds.getLastModified() + ".htm");
		ds.setContentType(DatastreamConstant.STREAMTYPE);
		save(ds);
		datastreamContentDao.createDatastreamContent(ds, content);
		return ds;
	}

	
	
	public void updateDatastream(Datastream ds) {
		if (ds != null) {
			this.update(ds);
		}
	}

	public Datastream createFileDatastream(String name, String contentType,String filePath, long fileSize, Long storageId) {
		return save(new Datastream(name, contentType, filePath, fileSize,storageId));
	}

	public String findDatastreamContentByStream(Long ds) {
		if (null == ds) {
			return null;
		}
		return datastreamContentDao.findDatastreamContentById(ds);
	}

	public Datastream cloneDatastream(Datastream ds)
			throws CloneNotSupportedException {
		return (Datastream) clone(ds, new HashMap<Datastream, Datastream>(),
				new HashMap<String, String>());
	}

	protected Object clone(Datastream ds,
			Map<Datastream, Datastream> diLinkMap,
			Map<String, String> keyLinkMap) throws CloneNotSupportedException {
		Datastream clone = null;
		Datastream ditemImpl = (Datastream) ds;
		if (diLinkMap.containsKey(ditemImpl)) {
			clone = (Datastream) diLinkMap.get(ditemImpl);
		}

		if (clone == null) {
			clone = new Datastream(ditemImpl.getName(),
					ditemImpl.getContentType());
			clone.setFileSize(ditemImpl.getFileSize());
			clone.setFilePath(ditemImpl.getFilePath());
			clone.setStorageId(ditemImpl.getStorageId());
			save(clone);
			diLinkMap.put(ditemImpl, clone);
//			keyLinkMap.put(ditemImpl.getKey(), clone.getKey());
		}

		// references
//		Iterator<?> iter = ditemImpl.getReferences().iterator();
//		while (iter.hasNext()) {
//			Datastream ref = (Datastream) iter.next();
//			if (ref == null) {
//				continue;
//			}
//			Datastream refClone = (Datastream) clone(ref, diLinkMap, keyLinkMap);
//			clone.createReference(refClone);
//		}
//
//		replaceReference(clone, ditemImpl, keyLinkMap);

		return clone;
	}

}
