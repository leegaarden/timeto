package com.timeto.dto.folder;

import com.timeto.config.exception.custom.annotation.ValidTextInput;
import io.swagger.v3.oas.annotations.media.Schema;

public class FolderRequest {

    @Schema(title = "FOLDER_REQ_01 : 폴더 생성 요청")
    public record CreateFolderReq (

            @Schema(description = "목표 아이디", example = "1")
            Long goalId,

            @ValidTextInput
            @Schema(description = "폴더 이름", example = "자료 조사")
            String folderName
    ) {}
}
