/*
 * Copyright (c) 2013, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.common.storage.supper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.csr.common.storage.entity.DatastreamBean;
import org.csr.core.util.ObjUtil;

/**
 * Defined class file the KindeditorContentResolve.java
 *
 *
 * @author Rock.Lee
 * @version elearning 1.0
 * @since JDK-1.6.0
 * @date 2013-6-21下午4:46:00
 */
public abstract class DatastreamResolver {
	private static final String DUMMY_REQUESTURI = "http://www.pointdew.org";

	/**
	 * 
	 * 为保存编辑器中的内容，需要对kindeitor编辑器中的内容进行正解析和分析
	 * 包括对一些编辑器中的保存的路径和字符进行过滤处理
	 * 
	 * @param request 客户端请求上下文对象
	 * @param content 编辑器中的内容
	 * @return 返回一个DatastreamBean对象
	 */
	public static DatastreamBean solveStreamContent(HttpServletRequest request, String content) {
		DatastreamBean bean = new DatastreamBean(content);
		StringBuffer requestURL = request.getRequestURL();
		String contextPath = request.getContextPath();
		int startPos = requestURL.indexOf(contextPath);
		String requestHost = requestURL.substring(0, startPos);
		requestHost = requestHost + contextPath;
//		content = content.replaceAll(requestHost, DUMMY_REQUESTURI);
		bean.setContent(content);
		return bean;
	}

	/**
	 * 为确保查看或展示的内容是正确的
	 * 需要对编辑器中的内容进行反解析和分析处理 
	 * 
	 * @param request 客户端请求上下文对象
	 * @param content 编辑器中的内容
	 * @return 返回一个分析处理过的编辑器内容字符串对象
	 */
	public static String resolveContent(HttpServletRequest request, String content) {
		StringBuffer requestURL = request.getRequestURL();
		String contextPath = request.getContextPath();
		int startPos = requestURL.indexOf(contextPath);
		String requestHost = requestURL.substring(0, startPos);
		requestHost = requestHost + contextPath;
//		content = content.replaceAll(DUMMY_REQUESTURI, requestHost);
		return content;

	}

	/**
	 * 为确保查看或展示的内容是正确的
	 * 需要对编辑器中的内容进行反解析和分析处理 
	 * 
	 * @param request 客户端请求上下文对象
	 * @param content 编辑器中的内容
	 * @return 返回一个DatastreamBean对象
	 */
	public static DatastreamBean resolveStreamContent(HttpServletRequest request, String content) {
		DatastreamBean bean = new DatastreamBean(content);
		StringBuffer requestURL = request.getRequestURL();
		String contextPath = request.getContextPath();
		int startPos = requestURL.indexOf(contextPath);
		String requestHost = requestURL.substring(0, startPos);
		requestHost = requestHost + contextPath;
		content = content.replaceAll(DUMMY_REQUESTURI, requestHost);
		bean.setContent(content);
		return bean;

	}
	
	/**
	 * 为确保查看或展示的内容是正确的
	 * 需要对编辑器中的内容进行反解析和分析处理 
	 * 
	 * @param request 客户端请求上下文对象
	 * @param content 编辑器中的内容
	 * @return 返回一个DatastreamBean对象
	 */
	public static List<Long> resolveStreamContent(String content) {
		if(ObjUtil.isBlank(content)){
			return new ArrayList<Long>(0);
		}
		List<Long> streamIds=new ArrayList<Long>(0);
//		<img src="[^>]+/file/ajax/inline.action?id=29804&type=image" 
		Matcher contextMatcher=Pattern.compile("<img src=[^>]+/file/ajax/inline.action?[^>]+&type=image").matcher(content);
		while (contextMatcher.find()) {
			String inpStr=contextMatcher.group();
			Matcher matcher=Pattern.compile("id=([^d]+)&type=image").matcher(inpStr);
			Long value;
			while (matcher.find()) {
				System.out.println(matcher.group(1));
				value=ObjUtil.toLong(matcher.group(1));
				if(ObjUtil.isNotEmpty(value)){
					streamIds.add(value);
				}
				break;
			}
		}
		
		return streamIds;
	}
	
	public static void main(String[] args) {
		
	}
}
