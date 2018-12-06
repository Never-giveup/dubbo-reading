/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.common.extension.factory;

import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionFactory;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AdaptiveExtensionFactory
 *  ExtensionFactory拓展实现类
 */
@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory {

    private final List<ExtensionFactory> factories;

    public AdaptiveExtensionFactory() {
        // 获得ExtensionFactory的拓展对象类加载器
        ExtensionLoader<ExtensionFactory> loader = ExtensionLoader.getExtensionLoader(ExtensionFactory.class);
        List<ExtensionFactory> list = new ArrayList<ExtensionFactory>();
        // 返回@SPI拓展实现类,非 @Adaptive注解和Wrappper拓展实现类所对应的name
        // 即 META-INF/dubbo/internal/com.alibaba.dubbo.common.extension.ExtensionFactory 文件内相关拓展实现类
        // AdaptiveExtensionFactory 带有@Adaptive注解，所以返回的是
        // spi=com.alibaba.dubbo.common.extension.factory.SpiExtensionFactory 相关拓展实现类内容
        // 即返回的是spi
        for (String name : loader.getSupportedExtensions()) {
            // 根据name从cachedInstances缓存中获取对应的拓展实现类对象
            list.add(loader.getExtension(name));
        }
        // 返回不可修改集合，拓展实现类
        factories = Collections.unmodifiableList(list);
    }

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        // 遍历 工厂集合中的拓展实现类
        for (ExtensionFactory factory : factories) {
            T extension = factory.getExtension(type, name);
            if (extension != null) {
                return extension;
            }
        }
        return null;
    }

}
