/*
 * Copyright (C) 2008 feilong (venusdrogon@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.taglib.display.breadcrumb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.util.Validator;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.taglib.base.AbstractWriteContentTag;
import com.feilong.tools.velocity.VelocityUtil;

/**
 * 飞龙面包屑标签.
 * 
 * @author 金鑫 2010-6-8 上午05:50:38
 */
public class BreadCrumbTag extends AbstractWriteContentTag{

    /** The Constant serialVersionUID. */
    private static final long              serialVersionUID = -8596553099620845748L;

    /** The Constant LOGGER. */
    private static final Logger            LOGGER           = LoggerFactory.getLogger(BreadCrumbTag.class);

    /** 连接符,默认>. */
    private String                         connector        = ">";

    /** siteMapEntityList,用户所有可以访问的菜单url List. */
    private List<BreadCrumbEntity<Object>> siteMapEntityList;

    /**
     * 实现自定义站点地图数据提供程序的途径.
     * 
     * @return the object
     */
    @Override
    protected Object writeContent(){
        if (Validator.isNotNullOrEmpty(siteMapEntityList)){
            // ConfigurationManager configurationManager = new ConfigurationManager(this.pageContext.getServletContext());
            // String xmlString = configurationManager.getSiteMapXmlPath(siteMapProvider);
            // SiteMapConfigure siteMapConfigure = new SiteMapConfigure(xmlString);
            // return siteMapConfigure.getSiteMapContent("index.jsp");
            HttpServletRequest request = getHttpServletRequest();
            LOGGER.info("request.getRequestURI():{}", request.getRequestURI());
            if (LOGGER.isDebugEnabled()){
                // UrlPathHelperUtil.showProperties(request);
            }
            // if (Validator.isNull(currentPath)){
            String currentPath = RequestUtil.getOriginatingServletPath(request);
            // }
            LOGGER.info("urlPathHelper.getLookupPathForRequest(request):{}", currentPath);

            Object allParentSiteMapEntityList = getAllParentSiteMapEntityList(currentPath, siteMapEntityList);

            Map<String, Object> contextKeyValues = new HashMap<String, Object>();
            contextKeyValues.put("siteMapEntityList", allParentSiteMapEntityList);
            contextKeyValues.put("connector", connector);
            contextKeyValues.put("request", request);
            String siteMapString = new VelocityUtil().parseTemplateWithClasspathResourceLoader("velocity/sitemap.vm", contextKeyValues);

            LOGGER.debug("siteMapString is:{}", siteMapString);
            if (Validator.isNullOrEmpty(siteMapString)){
                return "";
            }
            return siteMapString;
        }
        LOGGER.warn("siteMapEntityList is null");
        return "";
    }

    /**
     * 按照父子关系排序好的 list.
     *
     * @param <T>
     *            the generic type
     * @param currentPath
     *            the current path
     * @param siteMapEntities
     *            the site map entities
     * @return the all parent site map entity list
     */
    public <T> List<BreadCrumbEntity<T>> getAllParentSiteMapEntityList(String currentPath,List<BreadCrumbEntity<T>> siteMapEntities){
        LOGGER.info("currentPath:{}", currentPath);
        BreadCrumbEntity<T> siteMapEntity_in = getSiteMapEntityByPath(currentPath, siteMapEntities);
        return getAllParentSiteMapEntityList(siteMapEntity_in, siteMapEntities);
    }

    /**
     * 按照父子关系排序好的 list.
     *
     * @param <T>
     *            the generic type
     * @param siteMapEntity_in
     *            the site map entity_in
     * @param siteMapEntities
     *            the site map entities
     * @return the all parent site map entity list
     */
    public <T> List<BreadCrumbEntity<T>> getAllParentSiteMapEntityList(
                    BreadCrumbEntity<T> siteMapEntity_in,
                    List<BreadCrumbEntity<T>> siteMapEntities){
        if (null == siteMapEntity_in){
            return null;
        }
        // 每次成一个新的
        List<BreadCrumbEntity<T>> allParentSiteMapEntityList = new ArrayList<BreadCrumbEntity<T>>();
        constructParentSiteMapEntityList(siteMapEntity_in, siteMapEntities, allParentSiteMapEntityList);
        LOGGER.info("before Collections.reverse,allParentSiteMapEntityList size:{}", allParentSiteMapEntityList.size());
        // for (SiteMapEntity sme : allParentSiteMapEntityList){
        // LOGGER.info(sme.getName());
        // }
        // 反转
        Collections.reverse(allParentSiteMapEntityList);
        // LOGGER.info("after Collections.reverse");
        // for (SiteMapEntity sme : allParentSiteMapEntityList){
        // LOGGER.info(sme.getName());
        // }
        return allParentSiteMapEntityList;
    }

    /**
     * 通过当前的SiteMapEntity,查找到所有的父节点<br>
     * 递归生成.
     *
     * @param <T>
     *            the generic type
     * @param siteMapEntity_in
     *            the site map entity_in
     * @param siteMapEntities
     *            the site map entities
     * @param allParentSiteMapEntityList
     *            the all parent site map entity list
     */
    private <T> void constructParentSiteMapEntityList(
                    BreadCrumbEntity<T> siteMapEntity_in,
                    List<BreadCrumbEntity<T>> siteMapEntities,
                    List<BreadCrumbEntity<T>> allParentSiteMapEntityList){
        // 加入到链式表
        allParentSiteMapEntityList.add(siteMapEntity_in);
        Object parentId = siteMapEntity_in.getParentId();

        // 标识 是否找到parent node
        BreadCrumbEntity<T> siteMapEntity_parent = null;
        // ****************************************************
        for (BreadCrumbEntity<T> loopSiteMapEntity : siteMapEntities){
            // 当前的id和传入的siteMapEntity equals
            if (loopSiteMapEntity.getId().equals(parentId)){
                LOGGER.info("loopSiteMapEntity.getId():{},siteMapEntity_in.getParentId():{}", loopSiteMapEntity.getId(), parentId);
                siteMapEntity_parent = loopSiteMapEntity;
                break;
            }
        }
        if (null != siteMapEntity_parent){
            // 递归
            constructParentSiteMapEntityList(siteMapEntity_parent, siteMapEntities, allParentSiteMapEntityList);
        }
    }

    /**
     * 匹配路径.
     *
     * @param <T>
     *            the generic type
     * @param currentPath
     *            the current path
     * @param siteMapEntities
     *            the site map entities
     * @return the site map entity by path
     */
    public <T> BreadCrumbEntity<T> getSiteMapEntityByPath(String currentPath,List<BreadCrumbEntity<T>> siteMapEntities){
        boolean flag = false;
        BreadCrumbEntity<T> siteMapEntity_return = null;
        for (BreadCrumbEntity<T> siteMapEntity : siteMapEntities){
            if (siteMapEntity.getRequestMapping().equals(currentPath)){
                flag = true;
                siteMapEntity_return = siteMapEntity;
                break;
            }
        }
        if (!flag){
            LOGGER.warn("currentPath is :{},can't find match BreadCrumbEntity", currentPath);
        }
        return siteMapEntity_return;
    }

    /**
     * Sets the 连接符.
     * 
     * @param connector
     *            the new 连接符
     */
    public void setConnector(String connector){
        this.connector = connector;
    }

    /**
     * Sets the siteMapEntityList,用户所有可以访问的菜单url List.
     * 
     * @param siteMapEntityList
     *            the new siteMapEntityList,用户所有可以访问的菜单url List
     */
    public void setSiteMapEntityList(List<BreadCrumbEntity<Object>> siteMapEntityList){
        this.siteMapEntityList = siteMapEntityList;
    }

}
