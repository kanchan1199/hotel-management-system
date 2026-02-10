package com.hotel.management.dto;

import com.hotel.management.model.Guest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {
    @NotBlank(message = "Guest name is required")
    private String name;
    
    @NotBlank(message = "Contact number is required")
    private String contactNumber;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotNull(message = "ID proof type is required")
    private Guest.IdProofType idProofType;
    
    @NotBlank(message = "ID proof number is required")
    private String idProofNumber;
    
    private String address;
}