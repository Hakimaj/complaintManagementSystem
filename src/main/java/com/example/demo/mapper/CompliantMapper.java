package com.example.demo.mapper;

import com.example.demo.dto.compliant.CompliantResponse;
import com.example.demo.dto.compliant.CreateRequest;
import com.example.demo.model.Compliant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompliantMapper {
    @Mapping(target = "files", ignore = true)
    Compliant mapCompliantRequestToCompliant(CreateRequest createRequest);

    @Mapping(target = "organizationalUnitId", source = "organizationalUnit.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "userId", source = "user.id")
    CompliantResponse mapCompliantToCompliantResponse(Compliant compliant);

    List<CompliantResponse> mapCompliantsToResponses(List<Compliant> compliants);
}
