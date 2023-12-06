package com.srishti.billing.mapper;

import com.srishti.billing.dto.request.BillingAccountRequest;
import com.srishti.billing.dto.response.BillingAccountResponse;
import com.srishti.billing.entity.BillingAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingAccountMapper extends
        EntityMapper<BillingAccount, BillingAccountRequest, BillingAccountResponse>{

}
