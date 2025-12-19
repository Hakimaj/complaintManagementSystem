package com.example.demo.service.compliant;

import com.example.demo.dto.compliant.CreateRequest;
import com.example.demo.dto.compliant.UpdateRequest;
import com.example.demo.model.Compliant;

import java.util.List;


public interface ICompliantService {
   public Compliant createCompliant(CreateRequest createRequest);
   public List<Compliant> getAllComplaints();
   public Compliant getCompliantById(Long id);
   public Compliant updateCompliant(Long id, UpdateRequest updateRequest);
   public Compliant updateStatus(Long id, com.example.demo.enums.Status status);
   public void deleteCompliant(Long id);
   public Compliant getCompliantByReference(String referenceNumber);
}
