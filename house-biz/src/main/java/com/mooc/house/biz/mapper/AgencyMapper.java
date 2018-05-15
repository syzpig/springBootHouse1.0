package com.mooc.house.biz.mapper;

import com.mooc.house.common.model.Agency;
import com.mooc.house.common.model.User;

import com.mooc.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgencyMapper {
  
    List<Agency> select(Agency agency);

    int insert(Agency agency);

    List<User>	selectAgent(@Param("user") User user, @Param("pageParams") PageParams pageParams);

	Long selectAgentCount(@Param("user") User user);

    /**
     *批量插入
     */
    int insertBatch(Agency agency);
}
