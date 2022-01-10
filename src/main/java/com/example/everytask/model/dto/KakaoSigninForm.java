package com.example.everytask.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoSigninForm {
    @ApiModelProperty(example = "1")
    private int kakaoId;
}
