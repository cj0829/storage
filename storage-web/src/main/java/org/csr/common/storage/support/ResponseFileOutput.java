package org.csr.common.storage.support;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csr.common.storage.constant.DatastreamConstant;
import org.csr.common.storage.entity.DownloadFileBean;
import org.csr.core.exception.Exceptions;
import org.csr.core.util.FileUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.StrUtil;
import org.csr.core.util.io.IOUtil;
import org.csr.core.util.mm.MimeUtil;

/**
 * 文件
 * @author caijin
 */
public class ResponseFileOutput {
	private static final int BUFFER_SIZE = 4096;
	private static Log log = LogFactory.getLog(ResponseFileOutput.class);

	public ResponseFileOutput() {
	}

	/**
	 * Serve the specified file, optionally including the data content.
	 * 
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * 
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet-specified error occurs
	 * @return a string handle result.
	 */
	public void handleFile(HttpServletRequest request, HttpServletResponse response, DownloadFileBean downloadFile,boolean isInline)
			throws IOException, ServletException {

		String contentType = downloadFile.getContentType();
		if (ObjUtil.isNotBlank(contentType)) {
			if (contentType.indexOf("/vnd.rn-") > 0) {
				contentType = "audio/x-pn-realaudio";
			}
			String encoding = null;
			if (contentType.equals(DatastreamConstant.STREAMTYPE)) {
				contentType = "text/html";
				encoding = "UTF-8";
			} else if (MimeUtil.isTextFile(contentType)) {
				try {
					InputStream in = new BufferedInputStream(downloadFile.getFile(), BUFFER_SIZE);
//					encoding = new CharsetUtil().rightCharSet(in, contentType);
					in.close();
				} catch (Exception ioe) {
				}
			}
			if (ObjUtil.isNotBlank(encoding)) {
				response.setContentType(contentType + "; charset=" + encoding);
			} else {
				response.setContentType(contentType);
			}
		}else{
			response.setContentType("application/octet-stream");
		}
		response.setHeader("Content-Length", String.valueOf(downloadFile.getFileSize()));
		response.setHeader("Content-Disposition", isInline?"inline":"attachment"+";filename=" + StrUtil.toUtf8String(downloadFile.getName()));

		ResourceInfo resourceInfo = new ResourceInfo(downloadFile.getFile(),downloadFile.getName(),downloadFile.getFileSize());
		Vector<Range> ranges = parseRange(request, response, resourceInfo);

		if ((ranges == null) || (ranges.isEmpty())) {
			if (resourceInfo.getLength() < Integer.MAX_VALUE) {
				response.setContentLength((int) resourceInfo.getLength());
			}
			InputStream is = resourceInfo.getStream();
			if (is == null) {
				Exceptions.service("", "文件不存在");
			}
			// response.reset();
			ServletOutputStream os = response.getOutputStream();
			try {
				IOUtil.pipe(is, os);
			} catch (IOException ioe) {
				// It is a org.apache.catalina.connector.ClientAbortException
				// An IOException on a write is almost always due to
				// the remote client aborting the request.
				if (log.isWarnEnabled()) {
					log.warn("doGet failure: The client aborting the request.");
				}
				ioe.printStackTrace();
			} catch (Exception e) {
				log.error("doGet failure: ", e);
				log.error("doGet failure with - " + e.getMessage());
				e.printStackTrace();
				Exceptions.service("", e.getMessage());
			} finally {
				if (is != null) {
					is.close();
					is = null;
				}
				if (os != null) {
					os.close();
					os = null;
				}
			}
		} else {
			response.setHeader("ETag", resourceInfo.getETag());
			response.setHeader("Last-Modified", resourceInfo.getHttpDate());
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			if (ranges.size() == 1) {
				Range range = (Range) ranges.elementAt(0);
				response.addHeader("Content-Range", "bytes " + range.start + "-" + range.end + "/" + range.length);
				response.setContentLength((int) (range.end - range.start + 1));
				try {
					response.setBufferSize(BUFFER_SIZE);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				ServletOutputStream ostream = null;
				PrintWriter writer = null;
				try {
					ostream = response.getOutputStream();
				} catch (IllegalStateException e) {
					if ((contentType == null) || (contentType.startsWith("text"))) {
						writer = response.getWriter();
					} else {
						e.printStackTrace();
						throw e;
					}
				}
				if (ostream != null) {
					copy(resourceInfo, ostream, range);
				} else {
					copy(resourceInfo, writer, range);
				}
			}
		}
		resourceInfo.getStream().close();
	}

	/**
	 * Serve the specified file, optionally including the data content.
	 * 
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * 
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet-specified error occurs
	 * @return a string handle result.
	 */
	public void handleFile(HttpServletRequest request, HttpServletResponse response, byte[] file,boolean isInline)
			throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Length", String.valueOf(file.length));
		Random rand = new Random();
		response.setHeader("Content-Disposition", isInline?"inline":"attachment"+";filename="+FileUtil.rename(ObjUtil.toString(rand.nextLong()),new Date()));
		// response.reset();
		ServletOutputStream os = response.getOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(file);
		try {
			IOUtil.pipe(is, os);
		} catch (IOException ioe) {
			// It is a org.apache.catalina.connector.ClientAbortException
			// An IOException on a write is almost always due to
			// the remote client aborting the request.
			if (log.isWarnEnabled()) {
				log.warn("doGet failure: The client aborting the request.");
			}
			ioe.printStackTrace();
		} catch (Exception e) {
			log.error("doGet failure: ", e);
			log.error("doGet failure with - " + e.getMessage());
			e.printStackTrace();
			Exceptions.service("", e.getMessage());
		} finally {
			if (is != null) {
				is.close();
				is = null;
			}
			if (os != null) {
				os.close();
				os = null;
			}
		}
	}

	/**
	 * Copy the contents of the specified input stream to the specified output stream, and ensure that both streams are
	 * closed before returning (even in the face of an exception).
	 * 
	 * @param istream The input stream to read from
	 * @param ostream The output stream to write to
	 * @param start Start of the range which will be copied
	 * @param end End of the range which will be copied
	 * @return Exception which occurred during processing
	 */
	private IOException copyRange(InputStream istream, ServletOutputStream ostream, long start, long end) {
		try {
			istream.skip(start);
		} catch (IOException e) {
			return e;
		}
		IOException exception = null;
		long bytesToRead = end - start + 1;

		byte buffer[] = new byte[BUFFER_SIZE];
		int len = buffer.length;
		while ((bytesToRead > 0) && (len >= buffer.length)) {
			try {
				len = istream.read(buffer);
				if (bytesToRead >= len) {
					ostream.write(buffer, 0, len);
					bytesToRead -= len;
				} else {
					ostream.write(buffer, 0, (int) bytesToRead);
					bytesToRead = 0;
				}
			} catch (IOException e) {
				exception = e;
				len = -1;
			}
			if (len < buffer.length)
				break;
		}
		return exception;
	}

	/**
	 * Copy the contents of the specified input stream to the specified output stream, and ensure that both streams are
	 * closed before returning (even in the face of an exception).
	 * 
	 * @param reader The reader to read from
	 * @param writer The writer to write to
	 * @param start Start of the range which will be copied
	 * @param end End of the range which will be copied
	 * @return Exception which occurred during processing
	 */
	private IOException copyRange(Reader reader, PrintWriter writer, long start, long end) {
		try {
			reader.skip(start);
		} catch (IOException e) {
			return e;
		}
		IOException exception = null;
		long bytesToRead = end - start + 1;
		char buffer[] = new char[BUFFER_SIZE];
		int len = buffer.length;
		while ((bytesToRead > 0) && (len >= buffer.length)) {
			try {
				len = reader.read(buffer);
				if (bytesToRead >= len) {
					writer.write(buffer, 0, len);
					bytesToRead -= len;
				} else {
					writer.write(buffer, 0, (int) bytesToRead);
					bytesToRead = 0;
				}
			} catch (IOException e) {
				exception = e;
				len = -1;
			}
			if (len < buffer.length)
				break;
		}
		return exception;
	}

	/**
	 * Copy the contents of the specified input stream to the specified output stream, and ensure that both streams are
	 * closed before returning (even in the face of an exception).
	 * 
	 * @param resourceInfo The ResourceInfo object
	 * @param ostream The output stream to write to
	 * @param range Range the client wanted to retrieve
	 * @exception IOException if an input/output error occurs
	 */
	private void copy(ResourceInfo resourceInfo, ServletOutputStream ostream, Range range) throws IOException {
		IOException exception = null;
		InputStream resourceInputStream = resourceInfo.getStream();
		InputStream istream = new BufferedInputStream(resourceInputStream, BUFFER_SIZE);
		exception = copyRange(istream, ostream, range.start, range.end);
		// Clean up the input stream
		try {
			istream.close();
		} catch (Throwable t) {
			;
		}
		// Rethrow any exception that has occurred
		if (exception != null)
			throw exception;
	}

	/**
	 * Copy the contents of the specified input stream to the specified output stream, and ensure that both streams are
	 * closed before returning (even in the face of an exception).
	 * 
	 * @param resourceInfo The ResourceInfo object
	 * @param writer The writer to write to
	 * @param range Range the client wanted to retrieve
	 * @exception IOException if an input/output error occurs
	 */
	private void copy(ResourceInfo resourceInfo, PrintWriter writer, Range range) throws IOException {
		IOException exception = null;
		InputStream resourceInputStream = resourceInfo.getStream();
		Reader reader = new InputStreamReader(resourceInputStream);
		exception = copyRange(reader, writer, range.start, range.end);
		// Clean up the input stream
		try {
			reader.close();
		} catch (Throwable t) {
			;
		}

		// Rethrow any exception that has occurred
		if (exception != null)
			throw exception;

	}

	/**
	 * Parse the range header.
	 * 
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @return Vector of ranges
	 */
	private Vector<Range> parseRange(HttpServletRequest request, HttpServletResponse response, ResourceInfo resourceInfo)
			throws IOException {

		// Retrieving the range header (if any is specified
		String rangeHeader = request.getHeader("Range");
		long fileLength = resourceInfo.getLength();

		if (rangeHeader == null)
			return null;

		// bytes is the only range unit supported (and I don't see the point of
		// adding new ones).
		if (!rangeHeader.startsWith("bytes")) {
			response.addHeader("Content-Range", "bytes */" + fileLength);
			response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
			return null;
		}

		rangeHeader = rangeHeader.substring(6);

		// Vector which will contain all the ranges which are successfully
		// parsed.
		Vector<Range> result = new Vector<Range>();
		StringTokenizer commaTokenizer = new StringTokenizer(rangeHeader, ",");

		// Parsing the range list
		while (commaTokenizer.hasMoreTokens()) {
			String rangeDefinition = commaTokenizer.nextToken();
			Range currentRange = new Range();
			currentRange.length = fileLength;

			int dashPos = rangeDefinition.indexOf('-');

			if (dashPos == -1) {
				response.addHeader("Content-Range", "bytes */" + fileLength);
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return null;
			}

			if (dashPos == 0) {
				try {
					long offset = Long.parseLong(rangeDefinition);
					currentRange.start = fileLength + offset;
					currentRange.end = fileLength - 1;
				} catch (NumberFormatException e) {
					response.addHeader("Content-Range", "bytes */" + fileLength);
					response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
					return null;
				}

			} else {

				try {
					currentRange.start = Long.parseLong(rangeDefinition.substring(0, dashPos));
					if (dashPos < rangeDefinition.length() - 1) {
						currentRange.end = Long.parseLong(rangeDefinition.substring(dashPos + 1, rangeDefinition.length()));
					} else {
						currentRange.end = fileLength - 1;
					}
				} catch (NumberFormatException e) {
					response.addHeader("Content-Range", "bytes */" + fileLength);
					response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
					return null;
				}

			}
			if (!currentRange.validate()) {
				response.addHeader("Content-Range", "bytes */" + fileLength);
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return null;
			}
			result.addElement(currentRange);
		}

		return result;
	}

	private class Range {

		public long start;
		public long end;
		public long length;

		public boolean validate() {
			if (end >= length)
				end = length - 1;
			return ((start >= 0) && (end >= 0) && (start <= end) && (length > 0));
		}

		public void recycle() {
			start = 0;
			end = 0;
			length = 0;
		}

	}

	private static class ResourceInfo {
		private boolean exists = false;
		private String eTag;
		private String httpDate;
		private InputStream is;
		private long length = -1L;

		public ResourceInfo(File file) {
			set(file);
		}

		public ResourceInfo(InputStream file,String name,long length) {
			recycle();
			try {
				this.eTag = name;
				this.httpDate = String.valueOf(System.currentTimeMillis());
				this.is = new BufferedInputStream(file, BUFFER_SIZE);
				this.length = length;
				this.exists = true;
			} catch (Exception e) {
				log.error("doGet failure: ", e);
				exists = false;
			}
		}

		public void recycle() {
			eTag = null;
			httpDate = null;
			is = null;
		}

		public void set(File file) {
			recycle();
			try {
				eTag = file.getName();
				httpDate = String.valueOf(System.currentTimeMillis());
				is = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
				length = file.length();
				exists = true;
			} catch (Exception e) {
				log.error("doGet failure: ", e);
				exists = false;
			}
		}

		/**
		 * Test if the associated resource exists.
		 */
		public boolean exists() {
			return exists;
		}

		/**
		 * Get IS from resource.
		 */
		public InputStream getStream() throws IOException {
			return is;
		}

		public long getLength() {
			return length;
		}

		public String getETag() {
			return eTag;
		}

		public String getHttpDate() {
			return httpDate;
		}
	}
}
