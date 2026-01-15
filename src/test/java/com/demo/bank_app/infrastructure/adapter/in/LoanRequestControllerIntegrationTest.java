package com.demo.bank_app.infrastructure.adapter.in;

import com.demo.bank_app.model.LoanRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Value("classpath:json/create-loan-request.json")
    private Resource createLoanRequestJson;

    @Value("classpath:json/create-loan-response.json")
    private Resource createLoanResponseJson;

    @Value("classpath:json/paged-loans-response.json")
    private Resource pagedLoansResponseJson;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void createLoanRequest_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        String requestJson = createLoanRequestJson.getContentAsString(StandardCharsets.UTF_8);
        String expectedResponseJson = createLoanResponseJson.getContentAsString(StandardCharsets.UTF_8);

        // Add applicationDate to request
        LoanRequestDto requestDto = objectMapper.readValue(requestJson, LoanRequestDto.class);
        requestDto.setApplicationDate(OffsetDateTime.now(ZoneOffset.UTC));
        String requestWithDate = objectMapper.writeValueAsString(requestDto);

        // Act
        MvcResult result = mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestWithDate))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
        String actualResponse = result.getResponse().getContentAsString();
        assertThatJson(actualResponse)
                .whenIgnoringPaths("id", "applicationDate")
                .isEqualTo(expectedResponseJson);
    }

    @Test
    public void getAllLoanRequests_WithPagination_ShouldReturnPagedResults() throws Exception {
        // Arrange
        int totalElements = 15;
        String size = "5";
        setupListOfLoanRequests(totalElements);
        String expectedResponseJson = pagedLoansResponseJson.getContentAsString(StandardCharsets.UTF_8);

        // Act
        MvcResult result = mockMvc.perform(get("/loans")
                        .param("page", "0")
                        .param("size", size)
                        .param("sort", "amount")
                        .param("direction", "DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(Integer.parseInt(size))))
                .andReturn();

        // Assert
        String actualResponse = result.getResponse().getContentAsString();
        assertThatJson(actualResponse)
                .whenIgnoringPaths("content")
                .isEqualTo(expectedResponseJson);
    }

    private void setupListOfLoanRequests(int totalElements) throws Exception {
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
