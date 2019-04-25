/*
 * Copyright (c) 2013, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.support;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.csr.core.util.FileUtil;
import org.csr.core.util.io.IOUtil;

//import com.enterprisedt.net.ftp.FTPClient;

/**
 * Defined class file the FileUploadSupport.java
 *
 *
 * @author Rock.Lee
 * @version elearning 1.0
 * @since JDK-1.6.0
 * @date 2013-6-29下午6:09:25
 */
public class FileUploadSupport {
	/**
	 * 将给定的Datastream文件写入到一个流对象中
	 * @param defaultStreamRealPath 默认的stream系统路径
	 * @param filePath 文件存放路径
	 * @param in 目标流对象
	 * @return 返回这个文件流对象大小
	 * @throws IOException
	 */
	public static long writeStream(String defaultStreamRealPath, String filePath, InputStream in) throws IOException {
		long length = 0;
		FileOutputStream out = null;
		try {
			StringBuffer streamFileRealPath = new StringBuffer();
			streamFileRealPath.append(defaultStreamRealPath);
			streamFileRealPath.append(filePath);
			// 文件名
			File file = new File(streamFileRealPath.toString());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			out = new FileOutputStream(file);
			length = IOUtil.pipe(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return length;
	}

	/**
	 * 
	 * @param srcFile
	 * @param defaultStreamRealPath
	 * @param filePath
	 * @throws IOException
	 */
	public static void copyFile(InputStream srcFile, String defaultStreamRealPath, String filePath) throws IOException {
		StringBuffer streamFileRealPath = new StringBuffer();
		streamFileRealPath.append(defaultStreamRealPath);
		streamFileRealPath.append(filePath);
		File distFile = new File(streamFileRealPath.toString());
		if (!distFile.getParentFile().exists()) {
			distFile.getParentFile().mkdirs();
		}
		FileUtil.copy(srcFile, new FileOutputStream(distFile));
	}

		
	//
	//	public static long ftpUploadFile(String localFile, String defaultStreamRealPath, String filePath, StorageServiceBean service) {
	//		return ftpUploadFile(localFile, defaultStreamRealPath + filePath, service);
	//	}
	//
	//	public static long ftpUploadFile(File srcFile, String defaultStreamRealPath, String filePath, StorageServiceBean service) {
	//		String localFile = srcFile.getAbsolutePath();
	//		return ftpUploadFile(localFile, defaultStreamRealPath + filePath, service);
	//	}
	//
	//	private static void checkUploadDirOrMakeDir(FileTransferClient client, String filePath) {
	//		String dirPath = StringUtils.substringBeforeLast(filePath, "/");
	//		String[] dirArray = dirPath.split("/");
	//		for (String dir : dirArray) {
	//			if (StringUtils.isBlank(dir)) {
	//				continue;
	//			}
	//			try {
	//				client.createDirectory(dir);
	//			} catch (IOException e1) {
	//				e1.printStackTrace();
	//			} catch (FTPException e1) {
	//				e1.printStackTrace();
	//			}
	//		}
	//	}

//	public static long ftpUploadFileInputStream(InputStream in, String filePath, StorageServiceBean service) {
//		String server_filename = filePath;
//		if (StringUtils.isNotBlank(service.getVirPath())) {
//			server_filename = service.getVirPath() + filePath;
//		}
//		EDTFTPClient ftpConnect = new EDTFTPClient(service.getAddress(), service.getAccount(), service.getPassword(), service.getPort());
//		long length = 0l;
//		try {
//			ftpConnect.login();
//			FTPClient client = ftpConnect.getFtp();
//			checkDirAndMakeDir(client, server_filename.toString());
//			client.chdir("/");
//			ftpConnect.uploadFileStreamFromLogonFTPServer(in, server_filename.toString());
//			length = client.size(server_filename.toString());
//
//			ftpConnect.logout();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (FTPException e) {
//			e.printStackTrace();
//		}
//
//		return length;
//	}

//	public static long ftpUploadFileInputStream(InputStream in, String defaultStreamRealPath, String filePath, StorageServiceBean service) {
//		return ftpUploadFileInputStream(in, defaultStreamRealPath + filePath, service);
//	}
//
//	public static long ftpUploadFile(InputStream srcFile, String defaultStreamRealPath, String filePath, StorageServiceBean service) {
//		InputStream input = null;
//		try {
//			input = srcFile;
//			return ftpUploadFileInputStream(input, defaultStreamRealPath, filePath, service);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return 0l;
//	}
//
//	private static void checkDirAndMakeDir(FTPClient client, String filePath) {
//		String dirPath = StringUtils.substringBeforeLast(filePath, "/");
//		String[] dirArray = dirPath.split("/");
//		for (String dir : dirArray) {
//			if (StringUtils.isBlank(dir)) {
//				continue;
//			}
//			try {
//				client.chdir(dir);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (FTPException e) {
//				try {
//					client.mkdir(dir);
//					client.chdir(dir);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				} catch (FTPException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}
//	}

	public static void uploadFile(DiskFileItem srcFile, String destFilePath) {
		File destFile = new File(destFilePath);
		File parentFile = destFile.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		InputStream stream = null;
		OutputStream bos = null;
		try {
			if (srcFile.getSize() != 0) {
				stream = srcFile.getInputStream();
				bos = new BufferedOutputStream(new FileOutputStream(destFile));
				IOUtil.pipe(stream, bos);
			} else {
				// Create one empty file when the upload file is empty
				destFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void uploadFile(File srcFile, String destFilePath) {
		File destFile = new File(destFilePath);
		File parentFile = destFile.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		InputStream stream = null;
		OutputStream bos = null;
		try {
			if (srcFile.length() != 0) {
				stream = new FileInputStream(srcFile);
				bos = new BufferedOutputStream(new FileOutputStream(destFile));
				IOUtil.pipe(stream, bos);
			} else {
				// Create one empty file when the upload file is empty
				destFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	public static void uploadFile(File file, String fileFullPath, String fileName) {
		StringBuffer uploadPathName = new StringBuffer();
		uploadPathName.append(fileFullPath);
		if (!(fileFullPath.endsWith("/") || fileFullPath.endsWith("\\"))) {
			uploadPathName.append(File.separator);
		}
		fileName = fileName.toLowerCase();
		uploadPathName.append(fileName);
		uploadFile(file, uploadPathName.toString());
	}

	/**
	 * 将一个文件复制到另一个文件中，追加中到另一个文件的后面
	 * 
	 * @param srcFile 要拷贝的源文件
	 * @param destFile 拷贝到的目标文件
	 * @throws IOException 文件如果复制失败将抛出该异常
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		if (srcFile.exists()) {
			FileInputStream srcOutput = null;
			FileOutputStream destOutput = null;
			try {
				srcOutput = new FileInputStream(srcFile);
				destOutput = new FileOutputStream(destFile);
				int read = 0;
				while ((read = srcOutput.read()) != -1) {
					destOutput.write(read);
					destOutput.flush();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (srcOutput != null) {
					srcOutput.close();
					srcOutput = null;
				}
				if (destOutput != null) {
					destOutput.close();
					destOutput = null;
				}
			}
		}
	}
}
