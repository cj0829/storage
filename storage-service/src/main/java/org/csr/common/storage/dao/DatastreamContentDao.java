/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.dao;

import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.domain.DatastreamContent;
import org.csr.core.persistence.BaseDao;

/**
 * Defiend class file the com.elearning.core.datastream.dao.DatastreamDao.java
 * 
 * 
 * @author Rock.Lee
 * @version elearning 1.0
 * @since JDK-1.6.0
 * @date 2013-3-23下午10:42:13
 */
public interface DatastreamContentDao extends BaseDao<DatastreamContent, Long> {

	void createDatastreamContent(Datastream ds, String content);

	boolean updateDatastreamContent(Datastream ds, String content);

	String findDatastreamContentById(Long id);
}