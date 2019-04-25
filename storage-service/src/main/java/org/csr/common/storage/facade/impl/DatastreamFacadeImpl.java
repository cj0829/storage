package org.csr.common.storage.facade.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.csr.common.storage.dao.DatastreamDao;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.entity.DatastreamBean;
import org.csr.common.storage.facade.DatastreamFacade;
import org.csr.common.storage.service.StorageSchemeService;
import org.csr.core.page.PagedInfoImpl;
import org.csr.core.persistence.BaseDao;
import org.csr.core.persistence.PageRequest;
import org.csr.core.persistence.param.AndGEParam;
import org.csr.core.persistence.service.SimpleBasisFacade;
import org.csr.core.util.FileUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.http.DownLoadHttp;
import org.csr.core.util.http.HttpService;
import org.csr.core.util.io.IOUtil;
import org.springframework.stereotype.Service;

@Service("datastreamFacade")
public class DatastreamFacadeImpl extends SimpleBasisFacade<DatastreamBean, Datastream, Long> implements DatastreamFacade{

	@Resource
	private StorageSchemeService storageSchemeService;
	@Resource
	private DatastreamDao datastreamDao;
	@Override
	public BaseDao<Datastream, Long> getDao() {
		return datastreamDao;
	}

	@Override
	public DatastreamBean wrapBean(Datastream doMain) {
		return DatastreamBean.toBean(doMain);
	}
	
	int max = 100;
	@Override
	public int cpoydb(String downLoadurl,String filePath) {
		StorageScheme storageScheme = storageSchemeService.getDefaultStorageScheme();
		
		List<Datastream> list = datastreamDao.findByParam(new AndGEParam("id",23000l));
		System.out.println(list);
		ExecutorService executor = Executors.newFixedThreadPool(5);
		//122822
		int total = list.size();
		int size = total/max;
	
		HttpService http = new HttpService();
    	int i=0;
    	if(ObjUtil.isNotEmpty(list)){
			for (final Datastream datastream : list) {
				System.out.println(downLoadurl+"?id="+datastream.getId());
				File file = new File(filePath+datastream.getFilePath());
				try {
				FileUtil.createFile(file.getParentFile());
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				http.downLoadOutputStream(downLoadurl,fileOutputStream, new DownLoadHttp<HttpRequestBase>() {
					public List<NameValuePair> getParam(){
		    			ArrayList param = new ArrayList<NameValuePair>();
		    			param.add(new BasicNameValuePair("id", ObjUtil.toString(datastream.getId())));
		    			return param;
		    		}
					@Override
					public HttpGet getHttp() {
						return new HttpGet();
					}
				});
					fileOutputStream.close();
					i++;
				}  catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        return 0;
	}

	@Override
	public int cpoydbs(String downLoadurl,String filePath) {
		StorageScheme storageScheme = storageSchemeService.getDefaultStorageScheme();
		
		List<Datastream> list = datastreamDao.findByParam(new AndGEParam("id",17000l));
		
		System.out.println(list.size());
		ExecutorService executor = Executors.newFixedThreadPool(5);
		//122822
		int total = list.size();
		int size = total/max;
	
		List<DownLoadFile> downLoadFiles = new ArrayList<>();
		for(int i=1;i<=max;i++){
			PageRequest pageRequest = new PageRequest(i,size);
			PagedInfoImpl<Object> pagedInfoImpl = new PagedInfoImpl<>(null, pageRequest, total);
			List<Datastream> newList = null;
			int end = pageRequest.getPageSize()*pageRequest.getPageNumber();
			System.out.println(pageRequest.getOffset()+"=="+end);
			if(!pagedInfoImpl.hasNextPage()){
				end = total;
			}
			newList = list.subList(pageRequest.getOffset(),end);
			downLoadFiles.add(new DownLoadFile(newList, downLoadurl, filePath));
		}
        try {
			List<Future<Integer>> results = executor.invokeAll(downLoadFiles);
			executor.shutdown();
	        
	        for (Future<Integer> result : results) {
	            try {
					System.out.println(result.get());
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
	        }               
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
		return 0;
	}

	
	 
    static class DownLoadFile implements Callable<Integer> {
        private final List<Datastream> datastreamList;
        private final  String downLoadurl;
        private final String filePath;
        DownLoadFile(List<Datastream> datastreamList,String downLoadurl,String filePath) {
            this.datastreamList = datastreamList;
            this.downLoadurl = downLoadurl;
            this.filePath = filePath;
        }
        
        @Override
        public Integer call() {
        	HttpService http = new HttpService();
        	int i=0;
        	if(ObjUtil.isNotEmpty(datastreamList)){
    			for (final Datastream datastream : datastreamList) {
    				byte[] downLoadFile = http.downLoadFile(downLoadurl, new DownLoadHttp<HttpRequestBase>() {
    					public List<NameValuePair> getParam(){
    		    			ArrayList param = new ArrayList<NameValuePair>();
    		    			param.add(new BasicNameValuePair("id", ObjUtil.toString(datastream.getId())));
    		    			return param;
    		    		}
    					@Override
    					public HttpGet getHttp() {
    						return new HttpGet();
    					}
    				});
    				
    				try {
    					System.out.println(downLoadurl+"?id="+datastream.getId());
    					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(downLoadFile);
    					File file = new File(filePath+datastream.getFilePath());
    					FileUtil.createFile(file.getParentFile());
    					FileOutputStream fileOutputStream = new FileOutputStream(file);
    					IOUtil.pipe(byteArrayInputStream,fileOutputStream);
    					byteArrayInputStream.close();
    					fileOutputStream.close();
    					i++;
    				}  catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		}
            return i;
        }                
    }
    
    public static void main(String[] args) throws FileNotFoundException {
		FileOutputStream fileOutputStream = new FileOutputStream("D:\\test.jpg");
    	new HttpService().downLoadOutputStream("http://219.153.7.14:58080/checkin/file/ajax/inline.action",fileOutputStream, new DownLoadHttp<HttpGet>() {
    		public List<NameValuePair> getParam(){
    			ArrayList param = new ArrayList<NameValuePair>();
    			param.add(new BasicNameValuePair("id", "3526"));
    			return param;
    		}
			@Override
			public HttpGet getHttp() {
				return new HttpGet();
			}
		});
	}
}
