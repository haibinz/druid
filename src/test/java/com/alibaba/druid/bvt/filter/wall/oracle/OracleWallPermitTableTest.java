/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.bvt.filter.wall.oracle;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.druid.wall.WallUtils;

public class OracleWallPermitTableTest extends TestCase {
    public void test_permitTable() throws Exception {
        Assert.assertFalse(WallUtils.isValidateOracle("SELECT * FROM T UNION select * from TAB"));
        Assert.assertFalse(WallUtils.isValidateOracle("SELECT * FROM T UNION select * from tab"));
        Assert.assertFalse(WallUtils.isValidateOracle("SELECT * FROM T UNION select * from SYS.TAB"));
        Assert.assertFalse(WallUtils.isValidateOracle("SELECT * FROM T UNION select * from SYS.\"TAB\""));

        Assert.assertFalse(WallUtils.isValidateOracle("SELECT * FROM T UNION select * from all_users"));
    }

    public void test_permitTable_subquery() throws Exception {
        Assert.assertTrue(WallUtils.isValidateOracle("select * from(select * from TAB) a"));
        Assert.assertTrue(WallUtils.isValidateOracle("select * from(select * from tab) a"));
        Assert.assertTrue(WallUtils.isValidateOracle("select * from(select * from SYS.TAB) a"));
        Assert.assertTrue(WallUtils.isValidateOracle("select * from(select * from SYS.\"TAB\") a"));
    }

    public void test_permitTable_join() throws Exception {
        Assert.assertTrue(WallUtils.isValidateOracle("select * from t1, TAB"));
        Assert.assertTrue(WallUtils.isValidateOracle("select * from t1, tab"));
        Assert.assertTrue(WallUtils.isValidateOracle("select * from t1, SYS.TAB"));
        Assert.assertTrue(WallUtils.isValidateOracle("select * from t1, SYS.\"TAB\""));
    }
}
