package com.backend.testdata.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.backend.testdata.configuration.SecurityConfiguration;
import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.request.TableSchemaExportRequest;
import com.backend.testdata.util.FormDataEncoder;
import com.backend.testdata.util.SchemaFieldRequestWithMock;
import com.backend.testdata.util.TableSchemaRequestWithMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[Controller] 테이블 스키마")
@Import({SecurityConfiguration.class, FormDataEncoder.class})
@AutoConfigureMockMvc
@WebMvcTest
record TableSchemaControllerTest(
    @Autowired MockMvc mvc,
    @Autowired FormDataEncoder encoder,
    @Autowired ObjectMapper mapper
) {

  @DisplayName("[GET] 테이블 스키마 페이지를 요청하면 테이블 스키마 뷰 (정상) 를 반환한다.")
  @Test
  void whenEnteredTableSchemaPage_ThenShowTableSchemaView() throws Exception {
    //given

    //when & then
    mvc.perform(get("/table-schema"))
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("tableSchema"))
        .andExpect(model().attributeExists("mockDataTypes"))
        .andExpect(model().attributeExists("fileTypes"))
        .andExpect(view().name("table-schema"));
  }


  @DisplayName("[POST] 테이블 스키마 생성, 변경을 요청하면 정상적으로 수행 후 테이블 스키마 페이지로 리다이렉션한다.")
  @Test
  void whenCreatedOrUpdatedTableSchema_ThenRedirectionToTableSchemaPage() throws Exception {
    //given
    var request = TableSchemaRequestWithMock.create("test_schema", "홍길동",
        List.of(
            SchemaFieldRequestWithMock.create("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
            SchemaFieldRequestWithMock.create("name", MockDataType.NAME, 2, 0, null, null),
            SchemaFieldRequestWithMock.create("age", MockDataType.NUMBER, 3, 0, null, null)
        ));

    //when & then
    mvc.perform(post("/table-schema")
            .content(encoder.encode(request))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attributeExists("tableSchemaRequest"))
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
        .andExpect(view().name("my-schemas"));
  }


  @DisplayName("[POST] 회원 스키마 삭제 (정상)")
  @Test
  void whenAuthUserDeleted_ThenRedirectionToUserTableSchemaView() throws Exception {
    //given
    String schemaName = "schemaName";
    //when & then
    mvc.perform(post("/table-schema/my-schemas/{schemaName}", schemaName)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/table-schema/my-schemas"));
  }


  @DisplayName("[GET] 테이블 스키마 파일 다운로드 (정상) 후 파일을 반환한다.")
  @Test
  void whenTableSchemaDownLoading_ThenReturnResultFile() throws Exception {
    //given
    var request = TableSchemaExportRequest.of("test", 60, ExportFileType.JSON,
        List.of(
            SchemaFieldRequestWithMock.create("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
            SchemaFieldRequestWithMock.create("name", MockDataType.NAME, 2, 0, null, null),
            SchemaFieldRequestWithMock.create("age", MockDataType.NUMBER, 3, 0, null, null)
        ));
    var queryParam = encoder.encode(request, false);

    //when & then
    mvc.perform(get("/table-schema/export?" + queryParam))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=table-schema.txt"))
        .andExpect(content().json(mapper.writeValueAsString(request))); // TODO 수정 필요
  }
}
