package com.antoine.springJwt.mapper;

import com.antoine.springJwt.dto.DepartmentDto;
import com.antoine.springJwt.model.Department;

public class DepartmentMapper {
        public static DepartmentDto toDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setUserId(department.getUser().getId());
        return dto;
    }
}
