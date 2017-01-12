package com.project.gtps.dao;

import com.project.gtps.domain.ResetCode;

/**
 * Created by suresh on 1/9/17.
 */
public interface ResetCodeDao extends GenericDao<ResetCode> {


    public Integer validateResetCode(String resetCode, String email);
}
