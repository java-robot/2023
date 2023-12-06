package org.nju.demo.service;

import org.nju.demo.entity.ACode;

import java.util.List;

public interface CodeService {
    List<ACode> getAllCodes();

    int addCode(ACode aCode);

    List<ACode> getCodesByCodeName(String codeName);
}
