package org.csr.common.storage.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import org.csr.core.exception.Exceptions;
import org.csr.core.page.PagedInfo;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.PropertiesUtil;
import org.csr.core.web.controller.BasisAction;
import org.csr.common.storage.domain.StorageScheme;
import org.csr.common.storage.entity.StorageSchemeBean;
import org.csr.common.storage.facade.StorageSchemeFacade;


/**
 * ClassName:StorageScheme.java <br/>
 * Date:     Fri Jul 28 16:29:51 CST 2017
 * @author   XTM-01606101026 <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 * 资源路径2 action
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/StorageScheme/storageScheme")
public class StorageSchemeAction extends BasisAction{

	final String preList="/storage/storageScheme/storageScheme/storageSchemeList";
	final String preInfo="/StorageSchemestorage/storageScheme/storageSchemeInfo";
	final String preAdd="/storage/storageScheme/storageScheme/storageSchemeAdd";
	final String preUpdate="/storage/storageScheme/storageScheme/storageSchemeUpdate";
	@Resource
	private StorageSchemeFacade storageSchemeFacade;

	/**
	 * 进入资源路径2 列表页面
	 * @author  XTM-01606101026
	 * @param:
	 * @return: String
	 */
	@RequestMapping(value="preList",method=RequestMethod.GET)
	public String preList(){
		return preList;
	}

	/**
	 * 查询资源路径2 列表数据
	 * @author  XTM-01606101026
	 * @param:
	 * @return: String
	 */
	@RequestMapping(value="ajax/list",method=RequestMethod.POST)
	public ModelAndView list(){
		PagedInfo<StorageSchemeBean> result=storageSchemeFacade.findAllPage(page);
		return resultExcludeJson(result);
	}

	/**
	 *  进入资源路径2 添加页面，
	 * @author  XTM-01606101026
	 * @param:
	 * @return: String
	 */
	@RequestMapping(value="preAdd",method=RequestMethod.GET)
	public String preAdd(){
		return preAdd;
	}
	
	/**
	 * 保存资源路径2  数据
	 * @author  XTM-01606101026
	 * @param:
	 * @return: String
	 */
	@RequestMapping(value="ajax/add",method=RequestMethod.POST)
	public ModelAndView add(StorageScheme storageScheme){
		storageSchemeFacade.save(storageScheme);
		return successMsgJson("");
	}

	/**
	 * 进入资源路径2 修改页面，
	 * @author  XTM-01606101026
	 * @param:
	 * @return: String
	 */
	@RequestMapping(value="preUpdate",method=RequestMethod.GET)
	public String preUpdate(Long id){
		setRequest("storageScheme",storageSchemeFacade.findById(id));
		return preUpdate;
	}

	/**
	 * 修改资源路径2 数据，
	 * @author  XTM-01606101026
	 * @param:
	 * @return: String
	 */
	@RequestMapping(value="ajax/update",method=RequestMethod.POST)
	public ModelAndView update(StorageScheme storageScheme){
		storageSchemeFacade.update(storageScheme);
		return successMsgJson("");
	}
	
	/**
	 * 删除 资源路径2数据，
	 * @author  XTM-01606101026
	 * @param:
	 * @return: String
	 */
	@RequestMapping(value="ajax/delete",method=RequestMethod.POST)
	public ModelAndView delete(Long[] ids){
		storageSchemeFacade.deleteSimple(ids);
		return successMsgJson("");
	}
	
    
    /**
	 * 查询下拉选择列表
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "ajax/findDropDownList", method = RequestMethod.POST)
	public ModelAndView findDropDownList(@RequestParam(value="selecteds",required=false)String selecteds) {
		// 此处
		List<Long> seleids =new ArrayList<Long>();
		if (ObjUtil.isNotBlank(selecteds)) {
			String[] ids = selecteds.split(",");
			if (ObjUtil.isNotEmpty(ids)) {
				for (String idstr : ids) {
					seleids.add(ObjUtil.toLong(idstr));
				}
			}

		}
		PagedInfo<StorageSchemeBean> result = storageSchemeFacade.findDropDownList(page,seleids);// 此处
		return resultExcludeJson(result);
	}
	
	  /**
	 * 验证数据，可以定义别的验证
	 * @author  XTM-01606101026
	 * @param:
	 * @return: String
	 */
	@RequestMapping(value = "ajax/findName", method = RequestMethod.POST)
    public ModelAndView findName(Long id,String name) {
		if (ObjUtil.isEmpty(name)) {
		    Exceptions.service("1000109", "未正确接收到您所输入的名称,请联系管理员");
		}
		if (storageSchemeFacade.checkNameIsExist(id,name)) {
			return errorMsgJson(PropertiesUtil.getExceptionMsg("NameIsExist"));
		}
		return successMsgJson("");
    }
}
