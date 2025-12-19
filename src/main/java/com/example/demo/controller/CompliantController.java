package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.compliant.CompliantResponse;
import com.example.demo.dto.compliant.CreateRequest;
import com.example.demo.dto.compliant.UpdateRequest;
import com.example.demo.dto.compliant.UpdateStatusRequest;
import com.example.demo.mapper.CompliantMapper;
import com.example.demo.model.Compliant;
import com.example.demo.service.compliant.CompliantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compliant")
public class CompliantController {

    @Autowired
    private CompliantService compliantService;

    @Autowired
    private CompliantMapper compliantMapper;

    @PostMapping("/")
    public ApiResponse<CompliantResponse> createCompliant(@ModelAttribute CreateRequest createRequest){
         Compliant createdCompliant = compliantService.createCompliant(createRequest);
         CompliantResponse response = compliantMapper.mapCompliantToCompliantResponse(createdCompliant);
         return new ApiResponse<>(true, "Complaint created successfully", response);
    }

    @GetMapping("/")
    public ApiResponse<List<CompliantResponse>> getAllComplaints(){
        List<Compliant> complaints = compliantService.getAllComplaints();
        List<CompliantResponse> responses = compliantMapper.mapCompliantsToResponses(complaints);
        return new ApiResponse<>(true, "Complaints retrieved successfully", responses);
    }

    @GetMapping("/{id}")
    public ApiResponse<CompliantResponse> getCompliantById(@PathVariable Long id){
        Compliant compliant = compliantService.getCompliantById(id);
        CompliantResponse response = compliantMapper.mapCompliantToCompliantResponse(compliant);
        return new ApiResponse<>(true, "Complaint retrieved successfully", response);
    }

    @GetMapping("/reference/{referenceNumber}")
    public ApiResponse<CompliantResponse> getCompliantByReference(@PathVariable String referenceNumber){
        Compliant compliant = compliantService.getCompliantByReference(referenceNumber);
        CompliantResponse response = compliantMapper.mapCompliantToCompliantResponse(compliant);
        return new ApiResponse<>(true, "Complaint retrieved successfully", response);
    }

    @PutMapping("/{id}")
    public ApiResponse<CompliantResponse> updateCompliant(@PathVariable Long id, @ModelAttribute UpdateRequest updateRequest){
        Compliant updatedCompliant = compliantService.updateCompliant(id, updateRequest);
        CompliantResponse response = compliantMapper.mapCompliantToCompliantResponse(updatedCompliant);
        return new ApiResponse<>(true, "Complaint updated successfully", response);
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<CompliantResponse> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request){
        Compliant updated = compliantService.updateStatus(id, request.getStatus());
        CompliantResponse response = compliantMapper.mapCompliantToCompliantResponse(updated);
        return new ApiResponse<>(true, "Complaint status updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCompliant(@PathVariable Long id){
        compliantService.deleteCompliant(id);
        return new ApiResponse<>(true, "Complaint deleted successfully", null);
    }

}
