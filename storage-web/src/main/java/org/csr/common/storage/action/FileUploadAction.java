package org.csr.common.storage.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.entity.DatastreamBean;
import org.csr.common.storage.entity.DownloadFileBean;
import org.csr.common.storage.service.DatastreamService;
import org.csr.common.storage.service.FileSystemStorageService;
import org.csr.common.storage.service.StorageSchemeService;
import org.csr.common.storage.supper.FileSystemContext;
import org.csr.common.storage.support.FileUploadSupport;
import org.csr.common.storage.support.PageControlSupport;
import org.csr.common.storage.support.ResponseFileOutput;
import org.csr.core.util.FileUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.PropertiesUtil;
import org.csr.core.util.StrUtil;
import org.csr.core.util.VerifyCodeUtils;
import org.csr.core.util.crop.CropImg;
import org.csr.core.util.crop.ImageCropUtil;
import org.csr.core.util.mm.MimeTypeTool;
import org.csr.core.web.controller.BasisAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("prototype")
@RequestMapping(value = "/file")
public class FileUploadAction extends BasisAction {
	private final String fileView = "/common/fileView";
	
	@Resource
	DatastreamService datastreamService;
	@Resource
	FileSystemStorageService fileSystemStorageService;
	@Resource
	StorageSchemeService storageSchemeService;
	
	static Logger log = LoggerFactory.getLogger(FileUploadAction.class);
	/**
	 * Kindeditor 文件上传采用Struts的文件上传组件结合json处理
	 * @return
	 */
	@RequestMapping(value = "ajax/upload")
	public ModelAndView upload(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		// 根据从编辑器插入的文件会创建不同的目录，比如：如果是图片就会创建image，如果是动画就会创建flash等，
		// 如果是插入多个文件的，这个值为空，就会直接上传到根目录下，可以根据应用需要设置一个值
		String contentType = request.getParameter("dir");
		String verification = request.getParameter("notVerificationFileExt");
		if (contentType == null) {
			contentType = FileSystemContext.TYPE_FILE;
		}
		if (!FileSystemContext.checkFileContentType(contentType)) {
			return errorMsgJson("目录名不正确");
		}
		try {
			DefaultMultipartHttpServletRequest multipart = (DefaultMultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipart.getFileMap();
			for (MultipartFile multipartFile : fileMap.values()) {
				if (multipartFile instanceof CommonsMultipartFile) {
					FileItem fileItem = ((CommonsMultipartFile) multipartFile).getFileItem();
					if (fileItem instanceof DiskFileItem) {
						DiskFileItem dFile = (DiskFileItem) fileItem;

						String fileExt = MimeTypeTool.getExtension(dFile.getName());
						
						if (!"yes".equals(verification) && !FileSystemContext.checkFileExtName(contentType, fileExt)) {
							return errorMsgJson("上传文件扩展名[" + fileExt + "]是不允许的扩展名。");
						}
						
						long fileSize = dFile.getSize();
						long maxSize = FileSystemContext.getUploadFileMaxSize();
						if (fileSize > maxSize) {
							return errorMsgJson("[ " + dFile.getName() + " ]超过单个文件大小限制，文件大小[ " + dFile.getSize() + " ]，限制为[ " + maxSize + " ] ");
						}
						
						Datastream datastream = null;
						// 压缩图片
						if(FileSystemContext.TYPE_IMAGE.equals(contentType) || FileSystemContext.TYPE_PHOTO.equals(contentType)){
							CropImg bigImg = ImageCropUtil.imageCrop(new ByteArrayInputStream(dFile.get()),FileSystemContext.getBigPicture().getWidth(), FileSystemContext.getBigPicture().getHeight(), true);
							datastream = fileSystemStorageService.uploadFile(bigImg, dFile.getName(), contentType);
						}else{
							datastream = fileSystemStorageService.uploadFile(dFile,dFile.getName(), contentType);
						}
						DatastreamBean streamBean = DatastreamBean.toBean(datastream);
						String url = FileSystemContext.getFileStreamAllUrl(datastream);
						streamBean.setUrl(url);
						log.debug("上传文件:[" + streamBean.getName() + "]" + ">>>[" + streamBean.getName() + "]成功");
						return successMsgJson("上传文件:[" + streamBean.getName() + "]" + ">>>[" + streamBean.getName() + "]成功", streamBean);
					}
				}
			}
		} catch (Exception e) {
			log.error("文件上传失败:" + e);
		}
		return errorMsgJson("上传失败");
	}

	/**
	 * Kindeditor 文件上传采用Struts的文件上传组件结合json处理
	 * @return
	 */
	@RequestMapping(value = "ajax/uploadNoFileExt")
	public ModelAndView uploadNoFileExt(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		// 根据从编辑器插入的文件会创建不同的目录，比如：如果是图片就会创建image，如果是动画就会创建flash等，
		// 如果是插入多个文件的，这个值为空，就会直接上传到根目录下，可以根据应用需要设置一个值
		String contentType = request.getParameter("dir");
		if (contentType == null) {
			contentType = FileSystemContext.TYPE_FILE;
		}
		if (!FileSystemContext.checkFileContentType(contentType)) {
			return errorMsgJson("目录名不正确");
		}
		try {
			DefaultMultipartHttpServletRequest multipart = (DefaultMultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipart.getFileMap();
			for (MultipartFile multipartFile : fileMap.values()) {
				if (multipartFile instanceof CommonsMultipartFile) {
					FileItem fileItem = ((CommonsMultipartFile) multipartFile).getFileItem();
					if (fileItem instanceof DiskFileItem) {
						DiskFileItem dFile = (DiskFileItem) fileItem;
						
						long fileSize = dFile.getSize();
						long maxSize = FileSystemContext.getUploadFileMaxSize();
						if (fileSize > maxSize) {
							return errorMsgJson("[ " + dFile.getName() + " ]超过单个文件大小限制，文件大小[ " + dFile.getSize() + " ]，限制为[ " + maxSize + " ] ");
						}
						
						Datastream datastream = null;
						// 压缩图片
						if(FileSystemContext.TYPE_IMAGE.equals(contentType) || FileSystemContext.TYPE_PHOTO.equals(contentType)){
							CropImg bigImg = ImageCropUtil.imageCrop(new ByteArrayInputStream(dFile.get()),FileSystemContext.getBigPicture().getWidth(), FileSystemContext.getBigPicture().getHeight(), true);
							datastream = fileSystemStorageService.uploadFile(bigImg, dFile.getName(), contentType);
						}else{
							datastream = fileSystemStorageService.uploadFile(dFile,dFile.getName(), contentType);
						}
						DatastreamBean streamBean = DatastreamBean.toBean(datastream);
						String url = FileSystemContext.getFileStreamAllUrl(datastream);
						streamBean.setUrl(url);
						log.debug("上传文件:[" + streamBean.getName() + "]" + ">>>[" + streamBean.getName() + "]成功");
						return successMsgJson("上传文件:[" + streamBean.getName() + "]" + ">>>[" + streamBean.getName() + "]成功", streamBean);
					}
				}
			}
		} catch (Exception e) {
			log.error("文件上传失败:" + e);
		}
		return errorMsgJson("上传失败");
	}

	/**
	 * 下载图片
	 * @return
	 */
	@RequestMapping(value = "ajax/download")
	public ModelAndView download(HttpServletRequest request,HttpServletResponse response,Long id) {
		response.setCharacterEncoding("utf-8");
        try {
        	DownloadFileBean downloadFile = datastreamService.downloadDatastream(id);
        	if(ObjUtil.isEmpty(downloadFile)){
        		return errorMsgJson("");
        	}
        	new ResponseFileOutput().handleFile(request, response, downloadFile, false);
		}catch (Exception e) {
			e.printStackTrace();
		}
        return successMsgJson("");
	}

	/**
	 * 删除附件
	 * @return
	 */
	@RequestMapping(value = "ajax/delete")
	public ModelAndView delete(Long streamId) {
        try {
        	datastreamService.deleteDatastream(streamId);
		}catch (Exception e) {
			e.printStackTrace();
		}
        return successMsgJson("");
	}
	
	/**
	 * 在线打开
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "ajax/inline")
	public ModelAndView inline(HttpServletRequest request,HttpServletResponse response, Long id) throws IOException {
		response.setCharacterEncoding("utf-8");
		PageControlSupport.setInstantExpire(response, request);

		try {
			DownloadFileBean downloadFile = datastreamService.downloadDatastream(id);
        	if(ObjUtil.isEmpty(downloadFile)){
        		return errorMsgJson("");
        	}
        	new ResponseFileOutput().handleFile(request, response, downloadFile, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return successMsgJson("");
	}
	
	/**
	 * downloadFile: 文件下载<br/>
	 * @author caijin
	 * @param request
	 * @param response
	 * @param uuid
	 * @param fileType
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/flv/{id}/video")
	public ModelAndView flv(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		try {
			DownloadFileBean downloadFile = datastreamService.downloadDatastream(id);
        	if(ObjUtil.isEmpty(downloadFile)){
        		return errorMsgJson("");
        	}
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Length", String.valueOf(downloadFile.getFileSize()));
			response.setHeader("Content-Disposition", "inline;fileName=" + StrUtil.toUtf8String(downloadFile.getName()));
			FileUtil.copy(downloadFile.getFile(), response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="view")
	public String view(Long id) {
		setRequest("id", id);
		Datastream datastreamBean=datastreamService.findById(id);
		if(FileSystemContext.checkFileExtName("image", datastreamBean.getExtName())){
			setRequest("contentType", "image");
		}else if(FileSystemContext.checkFileExtName("flash", datastreamBean.getExtName())){
			setRequest("contentType", "flash");
			try {
				String url = getRequest().getServletContext().getContextPath()+ FileSystemContext.getFlvInlineUrl(datastreamBean);
				//jwplayer 传输格式。flashplayer=[转码内容]&amp;playlist=[转码内容]&amp;=base=[转码内容]
				
//				flashplayer="/cms/js/jwplayer/player.swf"
//				playlist="[[JSON]][{\"sources\":[{\"file\":\""+url+"\",\"default\":false}],\"tracks\":[]}]"
				//一定要记住上面的格式。 /flv/{id}/video.flv 需要增加对flv的处理。
				String flashplayer="flashplayer="+URLEncoder.encode(getRequest().getServletContext().getContextPath()+"/js/jwplayer/player.swf","UTF-8")+"&amp;";
				String playlist="playlist="+URLEncoder.encode("[[JSON]][{\"sources\":[{\"file\":\""+url+"\",\"default\":false}],\"tracks\":[]}]","UTF-8");
				setRequest("flvurl",flashplayer+playlist);
			} catch (Exception e) {}
		}else if(FileSystemContext.checkFileExtName("media", datastreamBean.getExtName())){
			setRequest("contentType", "media");
		}else if(FileSystemContext.checkFileExtName("pdf", datastreamBean.getExtName())){
			setRequest("contentType", "pdf");
		}else if(FileSystemContext.checkFileExtName("txt", datastreamBean.getExtName())){
			setRequest("contentType", "txt");
			 StorageScheme storageScheme = storageSchemeService.findById(storageSchemeService.getDefaultFileLocationId());
		        try {
		        	File file = new File(storageScheme.getResourcePath()+datastreamBean.getFilePath());
		        	ByteArrayOutputStream byteArray  = new ByteArrayOutputStream();
		        	FileUtil.copy(new FileInputStream(file), byteArray);
		        	System.out.println(new String(byteArray.toByteArray()));
					setRequest("textContent", new String(byteArray.toByteArray()));
		        }catch(Exception e){}
		}else if(FileSystemContext.checkFileExtName("file", datastreamBean.getExtName())){
			setRequest("contentType", "file");
		}else{
			setRequest("contentType", "none");
		}
		setRequest("title", datastreamBean.getName());
		return fileView;
	}
	
	/**
	 * 上传文件到指定文件夹中。文件不入库。 storage.address：为配置文件里的路径
	 * @return
	 */
	@RequestMapping(value = "ajax/uploadFile")
	public ModelAndView uploadFile(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		// 根据从编辑器插入的文件会创建不同的目录，比如：如果是图片就会创建image，如果是动画就会创建flash等，
		// 如果是插入多个文件的，这个值为空，就会直接上传到根目录下，可以根据应用需要设置一个值
		String contentType = request.getParameter("dir");
		String verification = request.getParameter("notVerificationFileExt");
		if (contentType == null) {
			contentType = FileSystemContext.TYPE_FILE;
		}
		if (!FileSystemContext.checkFileContentType(contentType)) {
			return errorMsgJson("目录名不正确");
		}
		try {
			DefaultMultipartHttpServletRequest multipart = (DefaultMultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipart.getFileMap();
			for (MultipartFile multipartFile : fileMap.values()) {
				if (multipartFile instanceof CommonsMultipartFile) {
					FileItem fileItem = ((CommonsMultipartFile) multipartFile).getFileItem();
					if (fileItem instanceof DiskFileItem) {
						DiskFileItem dFile = (DiskFileItem) fileItem;

						String fileExt = MimeTypeTool.getExtension(dFile.getName());
						
						if (!"yes".equals(verification) && !FileSystemContext.checkFileExtName(contentType, fileExt)) {
							return errorMsgJson("上传文件扩展名[" + fileExt + "]是不允许的扩展名。");
						}
						
						long fileSize = dFile.getSize();
						long maxSize = FileSystemContext.getUploadFileMaxSize();
						if (fileSize > maxSize) {
							return errorMsgJson("[ " + dFile.getName() + " ]超过单个文件大小限制，文件大小[ " + dFile.getSize() + " ]，限制为[ " + maxSize + " ] ");
						}
						String storageAddress =PropertiesUtil.getConfigureValue("storage.address");
						if(ObjUtil.isBlank(storageAddress)){
							storageAddress=FileUtil.getClassRootPath();
						}
						FileUploadSupport.copyFile(dFile.getInputStream(),storageAddress, dFile.getName());
						return successMsgJson("上传文件");
					}
				}
			}
		} catch (Exception e) {
			log.error("文件上传失败:" + e);
		}
		return errorMsgJson("上传失败");
	}

	
	
	@RequestMapping(value="verificationCode")
	public void verificationCode(HttpServletResponse response) {
		
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		setSession("validateCode", verifyCode);
		 int w = 100, h = 40;
		try {
			VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
}
