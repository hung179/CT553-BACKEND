package com.ecommerce.studentmarket.order.dtos.request;

import com.ecommerce.studentmarket.order.domains.SubOrderDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderStateRequestDto {

    private Date choDuyetTTDH;

    private Date xacNhanTTDH;

    private Date dangGiaoTTDH;

    private Date daGiaoTTDH;

    private Date daHuyTTDH;

    private Date daNhanTTDH;

    private Date daHoanTienTTDH;
}
