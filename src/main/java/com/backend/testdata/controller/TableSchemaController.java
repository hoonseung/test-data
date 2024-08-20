package com.backend.testdata.controller;

import com.backend.testdata.domain.constants.ExportFileType;
import com.backend.testdata.domain.constants.MockDataType;
import com.backend.testdata.dto.request.TableSchemaExportRequest;
import com.backend.testdata.dto.request.TableSchemaRequest;
import com.backend.testdata.dto.response.SchemaFieldResponse;
import com.backend.testdata.dto.response.SimpleTableSchemaResponse;
import com.backend.testdata.dto.response.TableSchemaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/table-schema")
@Controller
public class TableSchemaController {

  private final ObjectMapper mapper;


  @GetMapping
  public String tableSchema(@RequestParam(name = "schemaName", required = false) String schemaName,
      @ModelAttribute TableSchemaRequest tableSchemaRequest, Model model) {
    var tableSchema = defaultTableSchema(schemaName);
    model.addAttribute("tableSchema", tableSchema);
    model.addAttribute("mockDataTypes", MockDataType.toObjects());
    model.addAttribute("fileTypes", ExportFileType.toObjects());

    return "table-schema";
  }


  @PostMapping
  public String createOrUpdateTableSchema(@ModelAttribute TableSchemaRequest tableSchemaRequest,
      RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("tableSchemaRequest", tableSchemaRequest);

    return "redirect:/table-schema";
  }


  @GetMapping("/my-schemas")
  public String mySchemas(Model model) {
    var tableSchemas = mySampleSchemas();

    model.addAttribute("tableSchemas", tableSchemas);

    return "my-schemas";
  }


  @PostMapping("/my-schemas/{schemaName}")
  public String deleteMySchema(@PathVariable(name = "schemaName") String schemaName,
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

  private List<SimpleTableSchemaResponse> mySampleSchemas() {
    return List.of(new SimpleTableSchemaResponse("schema_name1", "hoonseung1",
            LocalDate.of(2024, 1, 1).atStartOfDay()),
        new SimpleTableSchemaResponse("schema_name1", "hoonseung2",
            LocalDate.of(2024, 2, 2).atStartOfDay()),
        new SimpleTableSchemaResponse("schema_name1", "hoonseung3",
            LocalDate.of(2024, 3, 3).atStartOfDay()));
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
