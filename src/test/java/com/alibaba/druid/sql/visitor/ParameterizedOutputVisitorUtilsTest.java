package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.DbType;
import junit.framework.TestCase;

/**
 * @author haibinz
 * @create 2022/9/12
 */
public class ParameterizedOutputVisitorUtilsTest extends TestCase {

    public void test_parameter() {
//        String sql = "select appid, stream_name, sid,cid,uid, bid, mic_no,audio_video_group, json, metadata,stream_group,mix,type, ver,gear, mix_group_no,line_infos,line_stage,line_update_time  from stream_infos where type in(1,2) and mix=3 and sid in (100,200,300,400,500);";
        String sql = "select appid, stream_name, sid,cid,uid, bid, mic_no,audio_video_group, json, metadata,stream_group,mix,type, ver,gear, mix_group_no,line_infos,line_stage,line_update_time  from stream_infos where type in(1,2) and mix=3 and sid in (100,200,300";
        System.out.println(ParameterizedOutputVisitorUtils.parameterize(sql, DbType.mysql));
    }
}