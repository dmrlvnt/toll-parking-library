package com.contemporaryparking.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Class representing Pricing policy")
public class PricingPolicy {

  @ApiModelProperty(
      name = "fixedAmount",
      required = true,
      value = "5.00",
      notes = "parking fixed amount")
  private double fixedAmount;

  @ApiModelProperty(
      name = "hourPrice",
      required = true,
      value = "1.5",
      notes = "parking hour price")
  private double hourPrice;

  public PricingPolicy(double fixedAmount, double hourPrice) {
    this.fixedAmount = fixedAmount;
    this.hourPrice = hourPrice;
  }

  public double getFixedAmount() {
    return fixedAmount;
  }

  public double getHourPrice() {
    return hourPrice;
  }
}
