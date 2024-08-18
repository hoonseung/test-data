package com.backend.testdata.controller;

import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.request.TableSchemaExportRequest;
import com.backend.testdata.dto.request.TableSchemaRequest;
import com.backend.testdata.dto.response.SchemaFieldResponse;
import com.backend.testdata.dto.response.TableSchemaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/table-schema")
@Controller
public class TableSchemaController {

  private final ObjectMapper mapper;


  @GetMapping
  public String tableSchema(@ModelAttribute TableSchemaRequest tableSchemaRequest, Model model) {
    var tableSchema = defaultTableSchema();
    model.addAttribute("tableSchema", tableSchema);
    model.addAttribute("mockDataTypes", MockDataType.toObjects());
    model.addAttribute("fileTypes", ExportFileType.toObjects());

    return "table-schema";
  }


  @PostMapping
  public String createOrUpdateTableSchema(
      @ModelAttribute TableSchemaRequest tableSchemaRequest,
      RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("tableSchemaRequest", tableSchemaRequest);

    return "redirect:/table-schema";
  }


  @GetMapping("/my-schemas")
  public String mySchemas() {

    return "my-schemas";
  }


  @PostMapping("/my-schemas/{schemaName}")
  public String deleteMySchema(
      @PathVariable(name = "schemaName") String schemaName,
      RedirectAttributes redirectAttributes) {

    return "redirect:/table-schema/my-schemas";
  }


  @GetMapping("/export")
  public ResponseEntity<String> exportTableSchema(
      @ModelAttribute TableSchemaExportRequest tableSchemaExportRequest) {

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt")
        .body(json(tableSchemaExportRequest));
  }


  private TableSchemaResponse defaultTableSchema() {
    return new TableSchemaResponse(
        "schema_name",
        "shlee",
        List.of(
            new SchemaFieldResponse("fieldName1", MockDataType.STRING, 1, 0, null, null),
            new SchemaFieldResponse("fieldName2", MockDataType.NUMBER, 2, 0, null, null),
            new SchemaFieldResponse("fieldName3", MockDataType.NAME, 3, 0, null, null))
    );
  }

  private String json(Object obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe);
    }
  }
}
