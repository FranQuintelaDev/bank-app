package com.demo.bank_app.infrastructure.adapter.in;

import com.demo.bank_app.model.LoanRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createLoanRequest_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        OffsetDateTime dateInUtc = OffsetDateTime.now(ZoneOffset.UTC);
        LoanRequestDto loanRequestDto = new LoanRequestDto(
                "Demo User",
                100.0f,
                "EUR",
                dateInUtc,
                "123456789R");

        // Act & Assert
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.applicantName").value("Demo User"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.identificationNumber").value("123456789R"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void getAllLoanRequests_WithPagination_ShouldReturnPagedResults() throws Exception {
       //Arrange
        var totalElements = 15;
        var size = "5";
        var totalPages = totalElements / Integer.parseInt(size);
        SetupListOfLoanRequests(totalElements);
        // Act & Assert
        mockMvc.perform(get("/loans")
                        .param("page", "0")
                        .param("size", size)
                        .param("sort", "amount")
                        .param("direction", "DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(Integer.parseInt(size))))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(Integer.parseInt(size)))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false));

    }

    private void SetupListOfLoanRequests(int totalElements) throws Exception {
        for (int i = 0; i < totalElements; i++) {
            LoanRequestDto dto = new LoanRequestDto(
                    "User " + i,
                    (i + 1) * 100.0f,
                    "EUR",
                    OffsetDateTime.now(ZoneOffset.UTC),
                    "ID" + i);

            mockMvc.perform(post("/loans")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());
        }
    }

}
