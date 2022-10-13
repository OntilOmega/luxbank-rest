package lu.luxbank.restwebservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.exeption.ExceptionInfo;
import lu.luxbank.restwebservice.model.PaymentRequest;
import lu.luxbank.restwebservice.model.ValidatedPayment;
import lu.luxbank.restwebservice.model.dtos.PaymentDto;
import lu.luxbank.restwebservice.model.jpa.entities.Account;
import lu.luxbank.restwebservice.services.AntiFraudService;
import lu.luxbank.restwebservice.services.PaymentService;
import lu.luxbank.restwebservice.services.ValidatePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
@RequestMapping(value = "/api/v1/payments",
        produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@Validated
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "401",
                description = "Invalid jwt token",
                content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(
                responseCode = "500",
                description = "Unknown internal error",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(
                                example = """
                                        {"errorMsg": "Unknown internal error"}
                                        """,
                                implementation = ExceptionInfo.class)))
})
public class PaymentsController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ValidatePaymentService validatePaymentService;

    @Autowired
    private AntiFraudService antiFraudService;


    @Operation(summary = "Create single payment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Payment created successfully",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            Trying to pay to forbidden account | 
                            Giver account is blocked | 
                            Giver account does not belong to the user | 
                            Beneficiary iban is not valid | 
                            Payments to the same account is not valid | 
                            Payment, giver and beneficiary currencies should be the same | 
                            User exceeded fraud payments limit | 
                            Insufficient funds
                            """,
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Giver account not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createPayment(
            @Valid @RequestBody PaymentRequest paymentRequest,
            Principal user) {
        antiFraudService.performAntiFraudTasks(paymentRequest);

        ValidatedPayment validPayment = validatePaymentService
                .validate(paymentRequest, user.getName());

        paymentService.createPayment(validPayment);
    }

    @Operation(summary = "List all current user payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payments list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Payments not found",
                    content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<List<PaymentDto>> listAllPayments(
            Principal user,
            @Parameter(example = """
                    {
                      "page": 0,
                      "size": 25
                    }
                    """)
            Pageable pageable) {
        return paymentService.listAllPayments(user.getName(), pageable);
    }

    @Operation(summary = "List all current user payments by beneficiary " +
            "account number and period")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payments list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Payments not found",
                    content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping(value = "/beneficiary/{beneficiaryAccountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Page<List<PaymentDto>> listAllPayments(
            Principal user,

            @Parameter(description = "Beneficiary account number",
                    example = "US64SVBKUS6S3300958879")
            @NotBlank
            @Pattern(regexp = "^[A-Z0-9]*$")
            @PathVariable String beneficiaryAccountNumber,

            @Parameter(description = "Search start date",
                    example = "2022-09-01")
            @PastOrPresent
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,

            @Parameter(description = "Search end date",
                    example = "2022-12-31")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
            @Parameter(example = """
                    {
                      "page": 0,
                      "size": 25
                    }
                    """)
            Pageable pageable) {
        return paymentService.listAllPayments(
                user.getName(),
                beneficiaryAccountNumber,
                dateFrom, dateTo, pageable);
    }

    @Operation(summary = "Delete payment by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Payment was deleted",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400",
                    description = "Payment doesn't belong to the user",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404",
                    description = "Payment not found",
                    content = @Content(schema = @Schema(hidden = true)))})
    @DeleteMapping(value = "/{paymentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayment(
            @Parameter(description = "Payment id",
                    example = "b2a98b25-e5c5-4867-b66b-36d4be790253")
            @PathVariable UUID paymentId, Principal user) {
        paymentService.deletePayment(paymentId, user.getName());
    }

}
