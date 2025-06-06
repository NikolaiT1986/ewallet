package org.nikolait.assigment.ewallet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.nikolait.assigment.ewallet.dto.WalletResponse;
import org.nikolait.assigment.ewallet.entity.Wallet;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletMapper {

    WalletResponse toResponse(Wallet wallet);

}
