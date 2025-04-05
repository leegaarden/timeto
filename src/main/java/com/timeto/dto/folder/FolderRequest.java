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

    @Schema(title = "FOLDER_REQ_02 : 폴더 이름 변경 요청")
    public record EditFolderNameReq (
            @Schema(description = "폴더 아이디", example = "1")
            Long folderId,

            @Schema(description = "변경하려는 폴더 이름", example = "브랜드별 소셜 마케팅 전략 조사")
            String folderName
    ) {}
}
