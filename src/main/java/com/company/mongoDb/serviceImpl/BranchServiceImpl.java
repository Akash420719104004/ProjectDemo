package com.company.mongoDb.serviceImpl;

import com.company.mongoDb.dto.BranchDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.model.Branch;
import com.company.mongoDb.model.Product;
import com.company.mongoDb.repository.BranchRepository;
import com.company.mongoDb.repository.ProductRepository;
import com.company.mongoDb.serviceImpl.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public BranchDto createBranch(BranchDto branchDto)  throws CustomValidationException {
        Branch branch = new Branch();
        branch.setName(branchDto.getName());
        branch.setAddress(branchDto.getAddress());
        if (branchDto.getProductId() != null && !branchDto.getProductId().isEmpty()) {
            List<Product> products = productRepository.findAllById(branchDto.getProductId());
            branch.setProductId(products);
        }
        branchRepository.save(branch);
        branchDto.setId(branch.getId());
        return branchDto;
    }

    @Override
    public BranchDto getBranchById(String id)  throws CustomValidationException{
        Optional<Branch> branchOpt = branchRepository.findById(id);
        if (branchOpt.isEmpty()) {
            return null;
        }

        Branch branch = branchOpt.get();
        BranchDto dto = new BranchDto();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setAddress(branch.getAddress());
        if (branch.getProductId() != null) {
            dto.setProductId(
                    branch.getProductId().stream()
                            .map(Product::getId)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    @Override
    public List<BranchDto> getAllBranches()  throws CustomValidationException{
        return branchRepository.findAll()
                .stream()
                .map(branch -> {
                    BranchDto dto = new BranchDto();
                    dto.setId(branch.getId());
                    dto.setName(branch.getName());
                    dto.setAddress(branch.getAddress());
                    if (branch.getProductId() != null) {
                        dto.setProductId(
                                branch.getProductId().stream()
                                        .map(Product::getId)
                                        .collect(Collectors.toList())
                        );
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    public BranchDto updateBranch(String id, BranchDto branchDto)  throws CustomValidationException{
        Optional<Branch> branchOpt = branchRepository.findById(id);
        if (branchOpt.isEmpty()) {
            return null;
        }
        Branch branch = branchOpt.get();
        branch.setName(branchDto.getName());
        branch.setAddress(branchDto.getAddress());
        if (branchDto.getProductId() != null && !branchDto.getProductId().isEmpty()) {
            List<Product> products = productRepository.findAllById(branchDto.getProductId());
            branch.setProductId(products);
        }
        branchRepository.save(branch);
        branchDto.setId(branch.getId());
        return branchDto;
    }
    @Override
    public void deleteBranch(String id)  throws CustomValidationException {
        if (!branchRepository.existsById(id)) {
            throw new RuntimeException("Branch not found with ID: " + id);
        }
        branchRepository.deleteById(id);
    }
}
