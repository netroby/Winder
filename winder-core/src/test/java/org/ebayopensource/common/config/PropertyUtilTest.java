/**
 * Copyright (c) 2016 eBay Software Foundation. All rights reserved.
 *
 * Licensed under the MIT license.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ebayopensource.common.config;


import org.ebayopensource.common.util.Parameters;
import org.ebayopensource.common.util.ParametersMap;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by xshao on 10/1/16.
 */
public class PropertyUtilTest {

    @Test
    public void convert() throws Exception {
        new PropertyUtil();

        PropertyUtil.PropertyInjector propertyInjector = new PropertyUtil.PropertyInjector();

        propertyInjector.setName("platform.${platform.primary}.trace.uri");

        Parameters<String> parameters = new ParametersMap<>();
        parameters.put("platform.primary", "raptor");
        parameters.put("platform.raptor.trace.uri", "raptor");
        parameters.put("platform.smoke.trace.uri", "smoke");

        assertEquals("platform.raptor.trace.uri", propertyInjector.replaceName(parameters));

        parameters.put("platform.primary", "smoke");
        assertEquals("platform.smoke.trace.uri", propertyInjector.replaceName(parameters));


        propertyInjector.setName("${platform.primary}");
        parameters.put("smoke", "Smoking");
        parameters.put("raptor", "Flying");
        assertEquals("smoke", propertyInjector.replaceName(parameters));

    }

    @Test
    public void inject() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("int1", 123);
        map.put("int2", new Object());

        map.put("long1", 123);
        map.put("long2", "");

        map.put("boolean1", true);
        map.put("boolean2", "");

        map.put("str1", "abc");
        map.put("str2", new Object());

        map.put("sample_enum", SampleEnum.THREE);
        map.put("sample_service", new SampleImpl());

        SimpleBean bean = new SimpleBean();
        PropertyUtil.inject(bean, map);

        assertEquals(bean.int1, 123);
        assertEquals(bean.int2, 2);
        assertEquals(bean.long1, 123);
        assertEquals(bean.long2, 2);

        assertEquals(bean.boolean1, true);
        assertEquals(bean.boolean2, true);

        assertEquals(bean.str1, "abc");
        assertEquals(bean.str2, "str2");
        assertEquals(bean.sampleEnum, SampleEnum.THREE);
        assertNotNull(bean.sampleService);

        PropertyUtil.inject(new NoInjection(), map);
        PropertyUtil.inject(new IllegalAccess(), map);

        try {
            PropertyUtil.inject(new UnsupportedType(), map);
            fail("Unsupported");
        }
        catch(Exception ex) {

        }

    }

    public static class UnsupportedType {

        @InjectProperty(name="test")
        Object obj;

    }

    public static class IllegalAccess {
        @InjectProperty(name="int1")
        int int1;
    }

    public static class NoInjection {
        private int int1;
        private int int2;
        private long long1;
        private long long2;
        private boolean boolean1;
        private boolean boolean2;
        private String str1;
        private String str2;
    }

    public static class SimpleBean {

        @InjectProperty(name="int1")
        private int int1;
        @InjectProperty(name="int2")
        private int int2 = 2;
        @InjectProperty(name="long1")
        private long long1;
        @InjectProperty(name="long2")
        private long long2 = 2;
        @InjectProperty(name="boolean1")
        private boolean boolean1;
        @InjectProperty(name="boolean2")
        private boolean boolean2 = true;
        @InjectProperty(name="str1")
        private String str1;
        @InjectProperty(name="str2")
        private String str2 = "str2";


        @InjectProperty(name="sample_enum")
        private SampleEnum sampleEnum;


        @InjectProperty(name="sample_service")
        private SampleService sampleService;
    }


    public interface SampleService {

        void sayHello();
    }

    public class SampleImpl implements SampleService {

        @Override
        public void sayHello() {
            System.out.println("Hello world!");
        }
    }


    public enum SampleEnum {
        ONE, TWO, THREE;
    }

}
