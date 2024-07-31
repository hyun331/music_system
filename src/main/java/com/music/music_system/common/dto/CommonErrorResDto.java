package com.music.music_system.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class CommonErrorResDto {
    private int status_code;
    private String error_message;

    public CommonErrorResDto(HttpStatus httpStatus, String message){
        this.status_code = httpStatus.value();
        this.error_message = message;
    }

}
