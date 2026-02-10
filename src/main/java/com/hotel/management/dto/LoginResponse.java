package com.hotel.management.dto;

import com.hotel.management.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserDTO user;
    private Integer expiresIn;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private String userId;
        private String username;
        private String role;
        private String fullName;
        private String email;
        
        public static UserDTO fromUser(User user) {
            return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getRole().name(),
                user.getFullName(),
                user.getEmail()
            );
        }
    }
}