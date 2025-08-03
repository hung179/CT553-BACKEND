package com.ecommerce.studentmarket.order.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderStateResponseDto {

    private Long maTTDH;

    private LocalDateTime choDuyetTTDH;

    private LocalDateTime xacNhanTTDH;

    private LocalDateTime dangGiaoTTDH;

    private LocalDateTime daGiaoTTDH;

    private LocalDateTime daNhanTTDH;

    private LocalDateTime daHuyTTDH;

    private LocalDateTime daHoanTienTTDH;
}
