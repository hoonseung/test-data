package com.backend.testdata.controller;

import com.backend.testdata.configuration.SecurityConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("해당 테스트는 #24 이슈에서 진행되었음, 구현은 진행하지않고 테스트로 기초 스펙만 정의함")
@DisplayName("[Controller] 테이블 스키마")
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
@WebMvcTest
record TableSchemaControllerTest(
        @Autowired MockMvc mvc
) {

    @DisplayName("[GET] 테이블 스키마 페이지를 요청하면 테이블 스키마 뷰 (정상) 를 반환한다.")
    @Test
    void whenEnteredTableSchemaPage_ThenShowTableSchemaView() throws Exception {
        //given

        //when & then
        mvc.perform(get("/table-schema"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("table-schema"));
    }


    @DisplayName("[POST] 테이블 스키마 생성, 변경을 요청하면 정상적으로 수행 후 테이블 스키마 페이지로 리다이렉션한다.")
    @Test
    void whenCreatedOrUpdatedTableSchema_ThenRedirectionToTableSchemaPage() throws Exception {
        //given

        //when & then
        mvc.perform(post("/table-schema")
                        .queryParam("data", "sample") //TODO 수정 필요
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/table-schema"));
    }


    @DisplayName("[GET] 회원의 테이블 스키마 목록 페이지를 요청하면 회원의 테이블 스키마 목록 뷰 (정상) 를 반환한다.")
    @Test
    void whenAuthUserEnteredAllTableSchemaPage_ThenShowAllTableSchemaView() throws Exception {
        //given

        //when & then
        mvc.perform(get("/table-schema/my-schemas"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("my-schema"));
    }


    @DisplayName("[POST] 회원 스키마 삭제 (정상)")
    @Test
    void whenAuthUserDeleted_ThenRedirectionToUserTableSchemaView() throws Exception {
        //given
        String schemaName = "schemaName";
        //when & then
        mvc.perform(get("/table-schema/my-schemas/{schemaName}", schemaName))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/table-schema/my-schemas"));
    }


    @DisplayName("[GET] 테이블 스키마 파일 다운로드 (정상) 후 파일을 반환한다.")
    @Test
    void whenTableSchemaDownLoading_ThenReturnResultFile() throws Exception {
        //given

        //when & then
        mvc.perform(get("/table-schema/export"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt"))
                .andExpect(content().string("download complete!")); // TODO 수정 필요
    }
}
