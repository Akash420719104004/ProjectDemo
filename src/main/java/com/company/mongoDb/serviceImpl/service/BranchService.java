package com.company.mongoDb.serviceImpl.service;

import com.company.mongoDb.dto.BranchDto;
import com.company.mongoDb.exception.CustomValidationException;

import java.util.List;

public interface BranchService {
    BranchDto createBranch(BranchDto branchDto) throws CustomValidationException;
    BranchDto getBranchById(String id) throws CustomValidationException;
    List<BranchDto> getAllBranches()  throws CustomValidationException;
    BranchDto updateBranch(String id, BranchDto branchDto)  throws CustomValidationException;
    void deleteBranch(String id)  throws CustomValidationException;
}
