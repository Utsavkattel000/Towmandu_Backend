package com.tow.mandu.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilePojo implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MultipartFile file;
}
