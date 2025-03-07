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
package com.alibaba.druid.bvt.sql.oracle.block;

import com.alibaba.druid.sql.OracleTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Assert;

import java.util.List;

public class OracleBlockTest15 extends OracleTest {
    public void test_0() throws Exception {
        String sql = "DECLARE\n" +
                "   sales  NUMBER(8,2) := 10100;\n" +
                "   quota  NUMBER(8,2) := 10000;\n" +
                "   bonus  NUMBER(6,2);\n" +
                "   emp_id NUMBER(6) := 120;\n" +
                " BEGIN\n" +
                "   IF sales > (quota + 200) THEN\n" +
                "      bonus := (sales - quota)/4;\n" +
                " \n" +
                "      UPDATE employees SET salary =\n" +
                "        salary + bonus\n" +
                "          WHERE employee_id = emp_id;\n" +
                "   END IF;\n" +
                " END;"; //

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.ORACLE);
        assertEquals(1, stmtList.size());

        String result = SQLUtils.toOracleString(stmtList.get(0));
        System.out.println(result);
        assertEquals("DECLARE\n" +
                "\tsales NUMBER(8, 2) := 10100;\n" +
                "\tquota NUMBER(8, 2) := 10000;\n" +
                "\tbonus NUMBER(6, 2);\n" +
                "\temp_id NUMBER(6) := 120;\n" +
                "BEGIN\n" +
                "\tIF sales > quota + 200 THEN\n" +
                "\t\tbonus := (sales - quota) / 4;\n" +
                "\t\tUPDATE employees\n" +
                "\t\tSET salary = salary + bonus\n" +
                "\t\tWHERE employee_id = emp_id;\n" +
                "\tEND IF;\n" +
                "END;", result);

        Assert.assertEquals(1, stmtList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        for (SQLStatement statement : stmtList) {
            statement.accept(visitor);
        }

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));

        Assert.assertEquals(2, visitor.getColumns().size());
        Assert.assertEquals(1, visitor.getConditions().size());
        Assert.assertEquals(0, visitor.getRelationships().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
    }
}
