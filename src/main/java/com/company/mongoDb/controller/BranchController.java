package com.company.mongoDb.controller;

import com.company.mongoDb.dto.BranchDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.serviceImpl.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping("/create-branch")
    public BranchDto createBranch(@RequestBody BranchDto branchDto) throws CustomValidationException {
        return branchService.createBranch(branchDto);
    }

    @GetMapping("/branches")
    public List<BranchDto> getAllBranches() throws CustomValidationException {
        return branchService.getAllBranches();
    }

    @GetMapping("/get-branch-id")
    public BranchDto getBranchById(@RequestParam String id) throws CustomValidationException {
        return branchService.getBranchById(id);
    }

    @PutMapping("/update-branch")
    public BranchDto updateBranch(@RequestParam String id, @RequestBody BranchDto branchDto) throws CustomValidationException {
        return branchService.updateBranch(id, branchDto);
    }

    @DeleteMapping("/delete-branch")
    public String deleteBranch(@RequestParam String id) throws CustomValidationException {
        branchService.deleteBranch(id);
        return "Branch deleted successfully";
    }

}
