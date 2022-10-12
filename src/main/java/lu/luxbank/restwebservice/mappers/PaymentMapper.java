package lu.luxbank.restwebservice.mappers;

import lu.luxbank.restwebservice.model.dtos.PaymentDto;
import lu.luxbank.restwebservice.model.jpa.entities.Payment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PaymentMapper {
    Payment paymentDtoToPayment(PaymentDto paymentDto);

    PaymentDto paymentToPaymentDto(Payment payment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Payment updatePaymentFromPaymentDto(PaymentDto paymentDto, @MappingTarget Payment payment);
}
