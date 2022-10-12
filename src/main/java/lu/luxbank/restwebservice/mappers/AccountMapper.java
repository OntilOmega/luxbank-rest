package lu.luxbank.restwebservice.mappers;

import lu.luxbank.restwebservice.AccountDto;
import lu.luxbank.restwebservice.model.jpa.entities.Account;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AccountMapper {
    Account accountDtoToAccount(AccountDto accountDto);

    AccountDto accountToAccountDto(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account updateAccountFromAccountDto(AccountDto accountDto, @MappingTarget Account account);
}
