package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/6 13:53
 */
public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByRegTime(List<String> months);
}
