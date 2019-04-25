package org.csr.common.storage.service;

import java.io.IOException;
import java.util.List;

import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.entity.DatastreamBean;
import org.csr.common.storage.entity.DownloadFileBean;
import org.csr.core.persistence.service.BasisService;



/**
 * ClassName:DatastreamService.java <br/>
 * System Name：    在线学习系统 <br/>
 * Date:     2016年6月27日下午9:27:44 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface DatastreamService extends BasisService<Datastream, Long> {

	/**
	 * 如果保存对象为null ，则创建一个新的Datastream数据对象。如果存在，修改。
	 * @param source 数据来源
	 * @param update 保存对象
	 * @return
	 */
	Datastream createOrUpdateDatastream(DatastreamBean source, Datastream update);
	
	DatastreamBean createDatastream(String content);
	
	Datastream createFileDatastream(Datastream stream);

	/**
	 * 根据数据对象id 查询出对应的数据内容。
	 * @param datastreamId
	 * @return
	 */
	String findDatastreamContentByStream(Long datastreamId);
	
	/**
	 * 根据数据对象，查询对应的数据内容
	 * @param datastream
	 * @return
	 */
	String findDatastreamContentByStream(Datastream datastream);
	/**
	 * 将content设置进bean中
	 * @param datastream
	 * @return
	 */
	DatastreamBean findDatastreamBeanByStream(Datastream datastream);

	/**
	 * 获取Datastream的对象文件流
	 * @param stream
	 * @return
	 * @throws IOException 
	 */
	DownloadFileBean downloadDatastream(Datastream stream) throws IOException;
	
	/**
	 * 获取Datastream的对象文件流
	 * @param streamId
	 * @return
	 * @throws IOException 
	 */
	DownloadFileBean downloadDatastream(Long streamId) throws IOException;

	/**
	 * 删除记录,并删除对应的文件
	 * @param streamId
	 */
	void deleteDatastream(Long streamId);

	/**
	 * 下载多个文件
	 * @param streamIds
	 * @return
	 */
	List<DownloadFileBean> downloadDatastreams(List<Long> streamIds);

}
