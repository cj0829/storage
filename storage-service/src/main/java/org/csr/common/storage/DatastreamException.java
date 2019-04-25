/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage;

import org.csr.core.exception.ServiceException;


/**
 * Defiend class file the
 * com.elearning.storage.datastream.DatastreamException.java
 * 
 * 
 * @author Rock.Lee
 * @version elearning 1.0
 * @since JDK-1.6.0
 * @date 2013-3-16下午7:55:40
 */
public class DatastreamException extends ServiceException {

	public DatastreamException(String newErrorNo, String errorMsg) {
		super(newErrorNo, errorMsg);
	}
	public DatastreamException(int errorCode,String newErrorNo, String errorMsg) {
		super(errorCode,newErrorNo, errorMsg);
	}

	public DatastreamException(String newErrorNo, String errorMsg,Object returnValue) {
		super(newErrorNo, errorMsg,returnValue);
	}
}
