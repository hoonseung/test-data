package com.backend.testdata.controller;

import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.request.TableSchemaExportRequest;
import com.backend.testdata.dto.request.TableSchemaRequest;
import com.backend.testdata.dto.response.SchemaFieldResponse;
import com.backend.testdata.dto.response.SimpleTableSchemaResponse;
import com.backend.testdata.dto.response.TableSchemaResponse;
import com.backend.testdata.dto.security.GithubUser;
import com.backend.testdata.service.SchemaExportService;
import com.backend.testdata.service.TableSchemaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequiredArgsConstructor
@RequestMapping("/table-schema")
@Controller
public class TableSchemaController {

    private final TableSchemaService tableSchemaService;
    private final ObjectMapper mapper;
    private final SchemaExportService schemaExportService;


    @GetMapping
    public String tableSchema(
        @RequestParam(name = "schemaName", required = false) String schemaName,
        @AuthenticationPrincipal GithubUser githubUser, Model model) {

        TableSchemaResponse tableSchema =
            (Objects.nonNull(githubUser) && StringUtils.hasText(githubUser.id())
                && StringUtils.hasText(schemaName)) ? TableSchemaResponse.toResponse(
                tableSchemaService.loadMyTableSchema(githubUser.id(), schemaName))
                : defaultTableSchema(schemaName);

        model.addAttribute("tableSchema", tableSchema);
        model.addAttribute("mockDataTypes", MockDataType.toObjects());
        model.addAttribute("fileTypes", ExportFileType.toObjects());

        return "table-schema";
    }


    @PostMapping
    public String createOrUpdateTableSchema(@AuthenticationPrincipal GithubUser githubUser,
        @ModelAttribute TableSchemaRequest tableSchemaRequest,
        RedirectAttributes redirectAttributes) {
        tableSchemaService.upsertMySchema(tableSchemaRequest.toDto(githubUser.id()));

        redirectAttributes.addAttribute("schemaName", tableSchemaRequest.getSchemaName());

        return "redirect:/table-schema";
    }


    @GetMapping("/my-schemas")
    public String mySchemas(@AuthenticationPrincipal GithubUser githubUser, Model model) {
        List<SimpleTableSchemaResponse> tableSchemas = tableSchemaService.loadTableSchemas(
                githubUser.id())
            .stream().map(SimpleTableSchemaResponse::toSimpleResponse).toList();
        model.addAttribute("tableSchemas", tableSchemas);

        return "my-schemas";
    }


    @PostMapping("/my-schemas/{schemaName}")
    public String deleteMySchema(@PathVariable(name = "schemaName") String schemaName,
        @AuthenticationPrincipal GithubUser githubUser) {
        tableSchemaService.deleteMySchema(githubUser.id(), schemaName);

        return "redirect:/table-schema/my-schemas";
    }


    @GetMapping("/export")
    public ResponseEntity<String> exportTableSchema(
        @AuthenticationPrincipal GithubUser githubUser,
        @ModelAttribute TableSchemaExportRequest tableSchemaExportRequest)
    {
        String fileName = getFilename(tableSchemaExportRequest.getSchemaName(),
            tableSchemaExportRequest.getFileType());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
            .body(schemaExportService.export(tableSchemaExportRequest.getFileType(),
                tableSchemaExportRequest.toDto(
                    Objects.nonNull(githubUser) ? githubUser.id() : null
                ),
                tableSchemaExportRequest.getRowCount()));
    }


    private String getFilename(String schemaName, ExportFileType fileType) {
        return schemaName + "."
            + fileType.name().toLowerCase();
    }


    private TableSchemaResponse defaultTableSchema(String schemaName) {
        return new TableSchemaResponse(schemaName != null ? schemaName : "schema_name", "shlee",
            List.of(new SchemaFieldResponse("id", MockDataType.STRING, 1, 0, null, null),
                new SchemaFieldResponse("order", MockDataType.NUMBER, 2, 0, null, null),
                new SchemaFieldResponse("name", MockDataType.NAME, 3, 0, null, null)));
    }

    private String json(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }
    }
}
