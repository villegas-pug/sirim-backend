package com.microservicios.operativo.services;

import com.commons.utils.services.CommonServiceImpl;
import com.microservicios.operativo.models.entities.OperativoJZ;
import com.microservicios.operativo.models.repository.OperativoJZRepository;

import org.springframework.stereotype.Service;

@Service
public class OperativoJZServiceImpl extends CommonServiceImpl<OperativoJZ, OperativoJZRepository> implements OperativoJZService {
   
}