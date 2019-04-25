/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.csr.common.storage.constant.DatastreamConstant;
import org.csr.common.storage.dao.DatastreamContentDao;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.domain.DatastreamContent;
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
public class DatastreamContentDaoImpl extends JpaDao<DatastreamContent, Long>
		implements DatastreamContentDao {

	@Override
	public Class<DatastreamContent> entityClass() {
		return DatastreamContent.class;
	}

	public void createDatastreamContent(Datastream ds, String content) {
		if (StringUtils.isNotBlank(content)) {
			DatastreamContent diContent = new DatastreamContent(ds.getId(), content);
			save(diContent);
		}
	}

	public boolean updateDatastreamContent(Datastream ds, String content) {
		DatastreamContent diContent = findById(ds.getId());
		if (StringUtils.isBlank(content)) { // delete content
			if (diContent != null) {
				this.delete(diContent);
			}
		} else {
			if (diContent == null) {
				diContent = new DatastreamContent(ds.getId());
				diContent.setDatastream(ds);
				diContent.setBlobContent(content);
				save(diContent);
			} else {
				diContent.setBlobContent(content);
			}

			ds.setLastModified(System.currentTimeMillis());
			ds.setName(ds.getLastModified() + ".htm");
			ds.setContentType(DatastreamConstant.STREAMTYPE);
			ds.setFilePath(null);
		}

		return true;
	}

	@Override
	public String findDatastreamContentById(Long id) {
		DatastreamContent content = findById(id);
		if (content != null) {
			return content.getBlobContent();
		}
		return null;
	}

}
