/*
 * Copyright 2022-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.code13.libs.alibaba.druid.ast;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateTableStatement;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * LineageTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/21 23:55
 */
public class LineageTest {

  static final String SQL = """
create table tmp.tmp_a_supp_achievement_an_mom_001 as
select a1.dim_day_txdate
       ,a.a_pin
      ,sum(coalesce(b.amount,0)) as total_amount
      ,sum(coalesce(c.refund_amt,0)) as refund_amt
      ,sum(os_prcp_amt)os_prcp_amt
from
   (select dim_day_txdate
    from dmv.dim_day
    where dim_day_txdate>=concat(cast(year('2018-05-15')-1 as string),'-',substring('2018-05-15',6,2),'-01') and dim_day_txdate<='2018-05-15'
    )a1
join
   (select distinct a_pin
          ,product_type
    from dwd.dwd_as_qy_cust_account_s_d
    where dt ='2018-05-15' and product_type='20288'
    )a
left outer join
   (select substring(tx_time,1,10) as time\s
          ,sum(order_amt) as amount\s
          ,a_pin
    from DWD.dwd_actv_as_qy_iou_receipt_s_d-------
    where a_order_type='20096'
      and a_pin not in ('vep_test','VOPVSP测试','VOPVSP测试_1','测试号','2016联通测试号','pxpx01','pxpx02',
                                  'i000','i001','测试','测试aa01','测试aa02','px01','px02',
                                  'test','test01','px031901','px031902','多级审核测试admin','邮政测试2015','中石油积分兑换-测试','买卖宝测试王','mengmengda111','ZHAOGANGWANG1809','ZHAOGANGWANGC1000508',
                                  '差旅测试01','差旅测试03','差旅测试04','差旅测试02','差旅测试06','差旅测试05','jc_test1','大连航天测试','大客户金采测试','移动测试账号1','中国联通测试','云积分商城测试'
                                  ,'多级审核测试采购08','多级审核测试采购05','国电物流有限公司测试')
      and dt='2018-05-15'
    group by substring(tx_time,1,10),a_pin
    )b on cast(a.a_pin as string)=cast(b.a_pin as string) and a1.dim_day_txdate=b.time
left outer join
   (select substring(refund_time,1,10) as refund_time
          ,a_pin
          ,sum(refund_amt)as refund_amt
    from DWD.dwd_as_qy_iou_refund_s_d
    where refund_status='20090'
      and dt='2018-05-15'
      and a_order_no <> '12467657248'\s
      and a_refund_no <> '1610230919767139947' \s
    group by substring(refund_time,1,10),a_pin
    )c on cast(a.a_pin as string)=cast(c.a_pin as string) and a1.dim_day_txdate=c.refund_time
left outer join
(select dt,a_pin,sum(os_prcp_amt) as os_prcp_amt  from dwd.dwd_as_qy_cycle_detail_s_d where dt>=concat(substr('2018-05-15',1,7),'-01') and dt<='2018-05-15' group by dt,a_pin)e on cast(a.jd_pin as string)=cast(e.a_pin as string) and a1.dim_day_txdate=e.dt
group by a1.dim_day_txdate,a.a_pin
;
""";

  @Test
  @DisplayName("testLineage")
  void testLineage() {
    SQLStatement sqlStatements = SQLUtils.parseSingleStatement(SQL, DbType.hive);
    System.out.println(sqlStatements);

    HiveCreateTableStatement statement = Utils.cast(sqlStatements, HiveCreateTableStatement.class);

    SQLExprTableSource tableSource = statement.getTableSource();
//    System.out.println(tableSource);

    SQLSelect select = statement.getSelect();
//    System.out.println(select);

    SQLSelectQuery query = select.getQuery();
//    System.out.println(query);
    SQLSelectQueryBlock queryBlock = Utils.cast(query, SQLSelectQueryBlock.class);

    SQLTableSource from = queryBlock.getFrom();

    System.out.println(from);
  }

}
