package com.tm.db;

import java.util.Date;

import org.csr.common.storage.facade.DatastreamFacade;
import org.csr.core.util.DateUtil;
import org.csr.core.util.SpringBeanFactoryUtil;

/**
 * ClassName: HibTools.java <br/>
 * System Name： 用户管理系统 <br/>
 * Date: 2014年3月27日下午3:13:50 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class TestDb{
	static String url = "http://219.153.7.14:58081/checkin/file/ajax/inline.action";
	
//	DatastreamFacade  datastreamFacade= null;
	
	public static void main(String[] args) {
		DatastreamFacade datastreamFacade = (DatastreamFacade) SpringBeanFactoryUtil.getBeansFactory(null).getBean("datastreamFacade");
		datastreamFacade.cpoydb(url,"d:\\file" );
		Date parseDateTimeToMin = DateUtil.parseDateTimeToMin("2017-11-28 00:00");
		System.out.println(parseDateTimeToMin.getTime());
	}
	
	
}
