package com.storeapi.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebhookRequest {
  private Map<String, String> headers;
  private String payload;
}
