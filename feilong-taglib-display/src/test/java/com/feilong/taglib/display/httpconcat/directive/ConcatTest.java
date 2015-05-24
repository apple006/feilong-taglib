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
package com.feilong.taglib.display.httpconcat.directive;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.tools.velocity.VelocityUtil;

/**
 * The Class ConcatTest.
 * 
 * @author <a href="mailto:venusdrogon@163.com">feilong</a>
 * @version 1.0.7 2014年5月15日 上午10:27:18
 * @since 1.0.7
 */
public class ConcatTest{

    /** The Constant log. */
    private static final Logger log = LoggerFactory.getLogger(ConcatTest.class);

    /**
     * Parses the vm template with classpath resource loader.
     */
    @Test
    public void parseVMTemplateWithClasspathResourceLoader(){
        String templateInClassPath = "velocity/concat.vm";
        String parseVMTemplate = VelocityUtil.parseTemplateWithClasspathResourceLoader(templateInClassPath, null);
        log.info(parseVMTemplate);
    }
}
