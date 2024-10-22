package com.reboot_course.firstcome_system.order.dto.response.read;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderMainResponse {
    private List<OrderMainItem> orders;
    private long totalCount;
}