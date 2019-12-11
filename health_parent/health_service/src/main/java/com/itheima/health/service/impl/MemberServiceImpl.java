package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/6 13:54
 */
@Transactional
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        if (member.getPassword() != null) {
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByRegTime(List<String> months) {
        // 组织数据
        List<Integer> list = new ArrayList<>();
        // 遍历months
        if (months != null && months.size() > 0) {
            // 格式：[2019-01, 2019-02, 2019-03, 2019-04, 2019-05, 2019-06, 2019-07, 2019-08, 2019-09, 2019-10, 2019-11, 2019-12]
            for (String month : months) {
                String sDate = month+"-31"; // 使用当前月，计算当前月的最后1天
                Integer count = memberDao.findMemberCountByRegTime(sDate);
                list.add(count);
            }
        }
         return list;
    }
}
