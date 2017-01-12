package com.project.gtps.service.impl;

import com.project.gtps.dao.ResetCodeDao;
import com.project.gtps.dao.impl.ResetCodeDaoImpl;
import com.project.gtps.domain.ResetCode;
import com.project.gtps.service.ResetCodeService;

/**
 * Created by suresh on 1/9/17.
 */
public class ResetCodeServiceImpl implements ResetCodeService {

    ResetCodeDao resetCodeDao = new ResetCodeDaoImpl();

    @Override
    public void saveResetCode(ResetCode resetCode) {
        resetCodeDao.save(resetCode);
    }

    @Override
    public Integer validateResetCode(String resetCode, String email) {
        return resetCodeDao.validateResetCode(resetCode, email);
    }
}
