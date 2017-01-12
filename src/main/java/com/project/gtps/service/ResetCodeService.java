package com.project.gtps.service;

import com.project.gtps.domain.ResetCode;

/**
 * Created by suresh on 1/9/17.
 */
public interface ResetCodeService {

    public void saveResetCode(ResetCode resetCode);

    public Integer validateResetCode(String resetCode, String email);
}
