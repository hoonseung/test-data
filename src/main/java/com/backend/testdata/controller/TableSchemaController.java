package com.backend.testdata.controller;

import com.backend.testdata.dto.request.TableSchemaRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/table-schema")
@Controller
public class TableSchemaController {


    @GetMapping
    public String tableSchema(@ModelAttribute TableSchemaRequest tableSchemaRequest) {
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
    public ResponseEntity<String> exportTableSchema(@ModelAttribute TableSchemaRequest tableSchemaRequest) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt")
                .body("download complete!");
    }
}
