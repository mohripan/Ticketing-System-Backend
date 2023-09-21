package com.example.TicketingSystemBackend.config;

import com.example.TicketingSystemBackend.controller.GlobalExceptionHandler;
import com.example.TicketingSystemBackend.dto.UserDTO;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.UserRepository;
import com.example.TicketingSystemBackend.service.CustomUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(GlobalExceptionHandler.class)
public class AppConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getDepartment().getDepartmentID(), UserDTO::setDepartmentID);
                    mapper.map(src -> src.getRole().getRoleId(), UserDTO::setRoleID);
                });

        return modelMapper;
    }
}
