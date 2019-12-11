package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import org.springframework.stereotype.Repository;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/5 15:10
 */
@Repository
public interface MemberDao {

    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByRegTime(String sDate);

    Integer findTodayNewMember(String reportDate);

    Integer findTotalMember();

    Integer findThisNewMember(String thisWeekMonday);
}
