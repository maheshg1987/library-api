package com.libraryapi.commands;

import an.awesome.pipelinr.Command;
import com.libraryapi.dtos.BorrowerResponseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class BorrowerCommand implements Command<BorrowerResponseDTO> {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
