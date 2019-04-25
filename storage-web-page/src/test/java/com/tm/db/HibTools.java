package com.tm.db;

import org.csr.core.persistence.tool.jpa.hbm5ddl.NewMetadataSources;
import org.csr.core.persistence.tool.jpa.hbm5ddl.NewSchemaExport;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;

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
public class HibTools{
	public static void main(String[] args){
		try{
//			HashTable hstable=new HashTable();
			NewMetadataSources metadata=new NewMetadataSources();
			metadata.scanPackage("core","org.csr.core.persistence.business.domain");
			metadata.scanPackage("storage","org.csr.common.storage.domain");
			
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
			//builder.applySetting("hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
			builder.applySetting("hibernate.dialect","org.hibernate.dialect.MySQL5InnoDBDialect");
			StandardServiceRegistry serviceRegistry = builder.build();
			
			MetadataBuilder metBuilder = metadata.getMetadataBuilder(serviceRegistry);
			
			/**
			MetadataImplementor mt=(MetadataImplementor) metBuilder.build();
			mt.getMetadataBuildingOptions().getReflectionManager();*/
			 
			NewSchemaExport export = new NewSchemaExport((MetadataImplementor) metBuilder.build());
			export.setOutputFile("doc/ddl-mysql.sql");
			export.setFormat(true);
			export.setDelimiter(" ;");
			export.create(false, false);
			System.out.println("完成");
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
