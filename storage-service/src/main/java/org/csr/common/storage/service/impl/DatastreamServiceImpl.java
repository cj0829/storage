/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.csr.common.storage.constant.DatastreamConstant;
import org.csr.common.storage.dao.DatastreamContentDao;
import org.csr.common.storage.dao.DatastreamDao;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.entity.DatastreamBean;
import org.csr.common.storage.entity.DownloadFileBean;
import org.csr.common.storage.service.DatastreamService;
import org.csr.common.storage.service.StorageSchemeService;
import org.csr.common.storage.supper.FileSystemContext;
import org.csr.core.exception.Exceptions;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.service.SetBean;
import org.csr.core.persistence.service.SimpleBasisService;
import org.csr.core.persistence.util.PersistableUtil;
import org.csr.core.util.ObjUtil;
import org.springframework.stereotype.Service;

/**
 * @author caijin
 */
@Service("datastreamService")
public class DatastreamServiceImpl extends SimpleBasisService<Datastream, Long>
		implements DatastreamService {

	@Resource
	DatastreamDao datastreamDao;
	@Resource
	DatastreamContentDao datastreamContentDao;
	@Resource
	StorageSchemeService storageSchemeService;

	@Override
	public BaseDao<Datastream, Long> getDao() {
		return datastreamDao;
	}

	public DatastreamBean createDatastream(String content) {
		Datastream ds = datastreamDao.createDatastream(content);
		datastreamContentDao.updateDatastreamContent(ds, content);
		DatastreamBean bean = new DatastreamBean(ds.getId(),ds.getContentType(), ds.getName());
		bean.setStorageId(ds.getStorageId());
		bean.setFileSize(ds.getFileSize());
		bean.setFilePath(ds.getFilePath());
		bean.setLastModified(ds.getLastModified());
		return bean;
	}

	/**
	 * 根据DatastreamBean对象，创建一 个文件dataStream.
	 * 文件dataStream是没有stream字段信息，只有文件的一些基本信息，但必须要有storageKey。
	 * 
	 * @param bean
	 * @return
	 */
	@Override
	public Datastream createFileDatastream(Datastream domain) {
		if (domain == null) {
			return null;
		}
		this.checkParameter(domain.getName(), "文件名称不能为空");
		this.checkParameter(domain.getContentType(), "文件类型不存在");
		// 暂时可以不用放开。但是以后必须使用。
		// this.checkParameter(domain.getStorageId(), "storageId");
		this.checkParameter(Long.toString(domain.getFileSize()), "fileSize");
		this.checkParameter(domain.getFilePath(), "没有文件");
		return datastreamDao.save(domain);
	}

	@Override
	public Datastream createOrUpdateDatastream(DatastreamBean source,
			Datastream update) {
		if (null == source) {
			return null;
		}

		Datastream ds = null;
		String contentType = source.getContentType();
		try {
			if (!DatastreamConstant.STREAMTYPE.equals(contentType)) {
				// 处理 file ds
				if (update == null) {
					// 创建一个新的 file stream
					ds = datastreamDao.createFileDatastream(source.getName(),contentType, source.getFilePath(),source.getFileSize(), null);
				} else {
					ds = update;
					if (DatastreamConstant.STREAMTYPE.equals(update.getContentType())) {
						datastreamContentDao.deleteById(update.getId());
					}
					update.setName(source.getName());
					update.setContentType(contentType);
					update.setFilePath(source.getFilePath());
					update.setFileSize(source.getFileSize());
					update.setStorageId(null);
				}
			} else if (source.getContent() != null) {
				// 处理 data stream
				String newValue = source.getContent();

				if (update == null) {
					// 创建一个null内容的 data stream
					ds = datastreamDao.createDatastream(null);
				} else {
					// 修改data stream
					ds = update;
					update.setStorageId(null);
				}
				// 以后处理
				// // 处理content中引用的DI
				// CWTagAmender ctm = new CWTagAmender(newValue);
				// Set<String> newRefsKey = ctm.getNewRefs();
				// adjustRefDi(ds, newRefsKey);
				//
				// List<DatastreamBean> references = bean.getReferences();
				// if (references != null && references.size() > 0) {
				// // 处理blob DI中引用的DI
				// HashMap<String, String> mp = new HashMap<String, String>();
				// for (DatastreamBean reference : references) {
				// Datastream di1 = null;
				// if
				// (!DatastreamConstant.STREAMTYPE.equals(reference.getContentType()))
				// {
				// di1 = dim.createFileDatastream(reference.getName(),
				// reference.getContentType(),
				// reference.getFilePath(), reference.getFileSize(),
				// reference.getStorageKey());
				// } else {
				// di1 = dim.createBlobDatastream(reference.getContent());
				// }
				// dim.createReference(ds, di1);
				// mp.put(Integer.toString(reference.getOrder()),
				// CWTagAmender.PARAM_RETRIEVE_URL + di1.getKey());
				// }
				// newValue = ctm.replaceReferences(mp);
				// }
				datastreamContentDao.updateDatastreamContent(ds, newValue);
			} else {
				throw new IllegalArgumentException("The DatastreamBean's inputStream and content can't both null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (ds != null) {
			source.setId(ds.getId());
		}
		return ds;
	}

	@Override
	public String findDatastreamContentByStream(Long datastreamId) {
		if (ObjUtil.isEmpty(datastreamId)) {
			return "";
		}
		return datastreamContentDao.findDatastreamContentById(datastreamId);
	}
	
	@Override
	public String findDatastreamContentByStream(Datastream datastream) {
		if (ObjUtil.isEmpty(datastream)) {
			return "";
		}
		return datastreamContentDao.findDatastreamContentById(datastream.getId());
	}

	@Override
	public DatastreamBean findDatastreamBeanByStream(Datastream datastream) {
		if (ObjUtil.isNotEmpty(datastream)) {
			DatastreamBean bean = DatastreamBean.toBean(datastream);
			bean.setContent(findDatastreamContentByStream(datastream));
			return bean;
		}
		return null;
	}

	@Override
	public DownloadFileBean downloadDatastream(Datastream stream) throws IOException {
		if (ObjUtil.isEmpty(stream)) {
			return null;
		}
		StorageScheme storageScheme = storageSchemeService.findById(storageSchemeService.getDefaultFileLocationId());
		
		if(ObjUtil.isNotEmpty(stream.getRemoteFile()) && stream.getRemoteFile()){
			DownloadFileBean downloadFileRemoteFile = downloadFileRemoteFile(stream);
			if(ObjUtil.isEmpty(downloadFileRemoteFile)){
				Exceptions.service("", "远端文件无法找到，已丢失");
			}
			return downloadFileRemoteFile;
		}else{
			File file = new File(storageScheme.getResourcePath() + stream.getFilePath());
			DownloadFileBean downloadFileBean = new DownloadFileBean(new FileInputStream(file));
			downloadFileBean.setId(stream.getId());
			downloadFileBean.setContentType(stream.getContentType());
			downloadFileBean.setExtName(stream.getExtName());
			downloadFileBean.setFileSize(stream.getFileSize());
			downloadFileBean.setName(stream.getName());
			downloadFileBean.setLastModified(stream.getLastModified());
			return downloadFileBean;
		}
		
	}

	private DownloadFileBean downloadFileRemoteFile(Datastream stream) {
		CloseableHttpClient client = HttpClients.createDefault();
		// 目标文件url
		HttpGet httpGet = new HttpGet(FileSystemContext.getFileInlineAllUrl(stream));
		httpGet.setConfig(RequestConfig.custom().build());
		try {
			HttpResponse respone = client.execute(httpGet);
			if (respone.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = respone.getEntity();
			if (entity != null) {
				DownloadFileBean downloadFileBean = new DownloadFileBean(entity.getContent());
				downloadFileBean.setId(stream.getId());
				downloadFileBean.setContentType(entity.getContentType().getName());
				downloadFileBean.setExtName(stream.getExtName());
				downloadFileBean.setFileSize(entity.getContentLength());
				downloadFileBean.setName(stream.getName());
				downloadFileBean.setLastModified(stream.getLastModified());
				return downloadFileBean;
			}
		} catch (Exception e) {

		} 
		return null;
	}
	@Override
	public DownloadFileBean downloadDatastream(Long streamId) throws IOException {
		Datastream datastream = datastreamDao.findById(streamId);
		return downloadDatastream(datastream);
	}

	
	@Override
	public List<DownloadFileBean> downloadDatastreams(List<Long> streamIds){
		List<Datastream>  datastreams = datastreamDao.findByIds(streamIds);
		if(ObjUtil.isEmpty(datastreams)){
			return new ArrayList<DownloadFileBean>(0);
		}
		return PersistableUtil.toListBeans(datastreams, new SetBean<Datastream>() {
			@Override
			public DownloadFileBean setValue(Datastream doMain) {
				try {
					return downloadDatastream(doMain);
				} catch (IOException e) {}
				return null;
			}
		});
		 
	}

	
	@Override
	public void deleteDatastream(Long streamId) {
		datastreamDao.deleteById(streamId);
	}

}
