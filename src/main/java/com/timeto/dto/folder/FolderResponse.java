package com.timeto.dto.folder;

import io.swagger.v3.oas.annotations.media.Schema;

public class FolderResponse {

    @Schema(title = "FOLDER_RES_01 : 폴더 생성 응답")
    public record CreateFolderRes (
            @Schema(description = "폴더 아이디", example = "1")
            Long folderId,

            @Schema(description = "폴더 이름", example = "광고학 강의 레포트")
            String folderName,

            @Schema(description = "목표 아이디", example = "2")
            Long goalId
    ) {}
}
