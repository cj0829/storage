package org.csr.common.storage.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.csr.common.storage.dao.DatastreamDao;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.entity.DatastreamBean;
import org.csr.common.storage.message.LocalDatastreamMsg;
import org.csr.common.storage.service.StorageSchemeService;
import org.csr.core.constant.YesorNo;
import org.csr.core.exception.Exceptions;
import org.csr.core.queue.Message;
import org.csr.core.queue.MessageService;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.http.BaseContentParam;
import org.csr.core.util.http.ContentParam;
import org.csr.core.util.http.FileHttp;
import org.csr.core.util.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;


@Service("localDatastreamService")
public class LocalDatastreamService implements MessageService<StorageScheme> {

	static Logger log = LoggerFactory.getLogger(LocalDatastreamService.class);
	@Resource
	DatastreamDao datastreamDao;
	@Resource
	StorageSchemeService storageSchemeService;
	
	@Override
	public void processMessages(Message<StorageScheme> messages) {
		if(messages instanceof LocalDatastreamMsg){
			LocalDatastreamMsg msg = (LocalDatastreamMsg) messages;
			Datastream stream = msg.getStream();
			String url = messages.body().getVirPath();
			final File filePath = new File(msg.body().getResourcePath()+msg.getStream().getFilePath());
			log.info("上传地址：="+url);
			if(filePath.exists()){
				String json = new HttpService().httpPostFileCallBack(url,new FileHttp<HttpPost>() {
					@Override
					public List<NameValuePair> getParam() {
						return null;
					}
					@Override
					public void addHeader(HttpPost httppost) {}
					
					@Override
					public List<ContentParam> getContentParam() {
						List<ContentParam> bodys = new ArrayList<ContentParam>();
						bodys.add(new BaseContentParam("photo", new FileBody(filePath)));
						return bodys;
					}
				});
				log.debug("json：="+json);
				if(ObjUtil.isBlank(json)){
					Exceptions.service("", "文件上传失败");
				}
				JSONObject ojson = JSONObject.parseObject(json);
				if (YesorNo.YES.equals(ojson.getByte("status"))) {
					
					DatastreamBean datastreamBean = ojson.getObject("data", DatastreamBean.class);
					if(ObjUtil.isNotEmpty(datastreamBean) && ObjUtil.isNotEmpty(datastreamBean.getId())){
						stream.setRemoteFile(true);
						stream.setRemoteFileId(datastreamBean.getId());
						datastreamDao.update(stream);
						filePath.delete();
						log.debug("开始上传远程文件 完毕，已经删除本地文件");
					}else{
						log.debug("开始上传远程文件 完毕，但是无法获取正常文件id");
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String xiaoliang ="C:\\Users\\caijin\\Desktop\\Camera\\人脸特征数据.";
//		System.out.println(System.currentTimeMillis()/1000);
//		System.out.println(DateUtil.convert2Long("2017-10-14 00:00", DateUtil.DTAE_TIME_FORMAT_24));
//		String url = "http://127.0.0.1:8088/storage/file/ajax/uploadNoFileExt.action";
		String url = "http://111.198.71.172:22222/storage/file/ajax/uploadNoFileExt.action";
		final File file = new File(xiaoliang);
		String json = new HttpService().httpPostFileCallBack(url,new FileHttp<HttpPost>() {
			@Override
			public List<NameValuePair> getParam() {
				List<NameValuePair> param = new ArrayList<>();
				param.add(new BasicNameValuePair("notVerificationFileExt", "no"));
				return param;
			}

			@Override
			public void addHeader(HttpPost httppost) {}
			
			@Override
			public List<ContentParam> getContentParam() {
				List<ContentParam> bodys = new ArrayList<ContentParam>();
				bodys.add(new BaseContentParam("photo", new FileBody(file)));
				return bodys;
			}
		});
		System.out.println(json);
		if(ObjUtil.isBlank(json)){
			Exceptions.service("", "文件上传失败");
		}
		
		JSONObject ojson = JSONObject.parseObject(json);
		if (YesorNo.YES.equals(ojson.getInteger("status"))) {
			System.out.println("开始上传远程文件 完毕，并且删除本地文件");
		}
	}
	
	
	
}
