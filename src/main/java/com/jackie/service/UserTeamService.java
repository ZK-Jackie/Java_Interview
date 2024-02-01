package com.jackie.service;

import java.util.List;

public interface UserTeamService {

     List<Integer> getTeamMembers(Integer teamId);

     Boolean addTeamMember(Integer userId, Integer teamId);
}
