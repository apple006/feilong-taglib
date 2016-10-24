/*
 * Copyright (C) 2008 feilong
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
package com.feilong.taglib.display.pager;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static com.feilong.core.date.DateExtensionUtil.getIntervalTime;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.9.2
 */
public class PerformanceTestUtil{

    public static Map<Integer, Long> run(Runnable runnable,Integer...times){
        Map<Integer, Long> map = new TreeMap<>();
        for (Integer count : times){
            Date beginDate = new Date();
            for (int i = 0; i < count; ++i){
                runnable.run();
            }
            map.put(count, getIntervalTime(beginDate, new Date()));
        }
        return map;
    }
}
