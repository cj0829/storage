package org.csr.common.storage.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.csr.common.storage.domain.Datastream;
import org.csr.common.storage.service.FileSystemStorageService;
import org.csr.common.storage.supper.FileSystemContext;
import org.csr.common.storage.ueditor.ActionEnter;
import org.csr.common.storage.ueditor.define.AppInfo;
import org.csr.common.storage.ueditor.define.BaseState;
import org.csr.core.Constants;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.crop.CropImg;
import org.csr.core.util.crop.ImageCropUtil;
import org.csr.core.util.mm.MimeTypeTool;
import org.csr.core.web.controller.BasisAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("prototype")
@RequestMapping(value = "/ueditor")
public class UeditorAction extends BasisAction {
	static Logger log = LoggerFactory.getLogger(FileUploadAction.class);
	@Resource
	FileSystemStorageService fileSystemStorageService;

	/**
	 * Kindeditor 文件上传采用Struts的文件上传组件结合json处理
	 * 
	 * @return
	 */
	@RequestMapping(value = "ajax/config")
	public ModelAndView config(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setHeader("Content-Type", "text/html");

		String rootPath = request.getServletContext().getRealPath("/");
		System.out.println(rootPath);
		String xmsl = new ActionEnter(request, rootPath).exec();
		System.out.println(xmsl);
		return resultString(xmsl);
	}

	/**
	 * Kindeditor 文件上传采用Struts的文件上传组件结合json处理
	 * 
	 * @return
	 */
	@RequestMapping(value = "ajax/upload")
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		// 根据从编辑器插入的文件会创建不同的目录，比如：如果是图片就会创建image，如果是动画就会创建flash等，
		// 如果是插入多个文件的，这个值为空，就会直接上传到根目录下，可以根据应用需要设置一个值
		String contentType = request.getParameter("dir");
		if (contentType == null) {
			contentType = FileSystemContext.TYPE_FILE;
		}
		if (!FileSystemContext.checkFileContentType(contentType)) {
			return resultString(new BaseState(false, "目录名不正确").toJSONString());
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
						if (!FileSystemContext.checkFileExtName(contentType, fileExt)) {
							return resultString(new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE).toJSONString());
						}
						long fileSize = dFile.getSize();
						long maxSize = FileSystemContext.getUploadFileMaxSize();
						if (fileSize > maxSize) {
							return resultString(new BaseState(false, "[ " + dFile.getName() + " ]超过单个文件大小限制，文件大小[ "
									+ dFile.getSize() + " ]，限制为[ " + maxSize + " ] ").toJSONString());
						}
						Datastream stream = null;
						// 压缩图片
						if (FileSystemContext.TYPE_IMAGE.equals(contentType)
								|| FileSystemContext.TYPE_PHOTO.equals(contentType)) {
							CropImg bigImg = ImageCropUtil.imageCrop(new ByteArrayInputStream(dFile.get()),
									FileSystemContext.getBigPicture().getWidth(),
									FileSystemContext.getBigPicture().getHeight(), true);
							stream = fileSystemStorageService.uploadFile(bigImg, dFile.getName(), contentType);
						} else {
							stream = fileSystemStorageService.uploadFile(dFile, dFile.getName(), contentType);
						}

						log.debug("上传文件:[" + stream.getName() + "]" + ">>>[" + stream.getName() + "]成功");
						BaseState baseState = new BaseState(true);
						if (FileSystemContext.TYPE_IMAGE.equals(contentType) || FileSystemContext.TYPE_PHOTO.equals(contentType)) {
							baseState.putInfo("url",FileSystemContext.getFileInlineAllUrl(stream) + "&type=" + contentType);
						} else {
							baseState.putInfo("url",FileSystemContext.getFileStreamAllUrl(stream) + "&type=" + contentType);
						}
						baseState.putInfo("title", stream.getName());
						baseState.putInfo("original", stream.getName());
						baseState.putInfo("streamId", stream.getId());
						return resultString(baseState.toJSONString());
					}
				}
			}
		} catch (Exception e) {
			return resultString(new BaseState(false, e.getMessage()).toJSONString());
		}
		return resultString(new BaseState(false, "文件上传失败").toJSONString());
	}

	/**
	 * 上传文件到指定目录，
	 * 
	 * @return
	 */
	@RequestMapping(value = "ajax/uploadFile")
	public ModelAndView uploadFile(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		// 根据从编辑器插入的文件会创建不同的目录，比如：如果是图片就会创建image，如果是动画就会创建flash等，
		// 如果是插入多个文件的，这个值为空，就会直接上传到根目录下，可以根据应用需要设置一个值
		String contentType = request.getParameter("dir");
		if (contentType == null) {
			contentType = FileSystemContext.TYPE_FILE;
		}
		if (!FileSystemContext.checkFileContentType(contentType)) {
			return resultString(new BaseState(false, "目录名不正确").toJSONString());
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
						if (!FileSystemContext.checkFileExtName(contentType, fileExt)) {
							return resultString(new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE).toJSONString());
						}
						long fileSize = dFile.getSize();
						long maxSize = FileSystemContext.getUploadFileMaxSize();
						if (fileSize > maxSize) {
							return resultString(new BaseState(false, "[ " + dFile.getName() + " ]超过单个文件大小限制，文件大小[ "
									+ dFile.getSize() + " ]，限制为[ " + maxSize + " ] ").toJSONString());
						}
						Datastream stream = fileSystemStorageService.uploadFile(dFile, dFile.getName(), contentType);
						log.debug("上传文件:[" + stream.getName() + "]" + ">>>[" + stream.getName() + "]成功");

						BaseState baseState = new BaseState(true);
						String url = Constants.CXT + FileSystemContext.VIEW_URL + ObjUtil.toString(stream.getId());
						baseState.putInfo("url", url);
						baseState.putInfo("title", stream.getName());
						baseState.putInfo("original", stream.getName());
						return resultString(baseState.toJSONString());
					}
				}
			}
		} catch (Exception e) {
			return resultString(new BaseState(false, e.getMessage()).toJSONString());
		}
		return resultString(new BaseState(false, "文件上传失败").toJSONString());
	}
}
