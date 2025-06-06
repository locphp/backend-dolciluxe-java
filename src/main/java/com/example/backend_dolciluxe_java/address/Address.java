package com.example.backend_dolciluxe_java.address;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Document(collection = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    private String id;

    private String user;
    private String fullName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String detail;
    private Boolean isDefault;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

}