package com.contemporaryparking.config;

import com.contemporaryparking.data.PricingPolicy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Class representing Toll Parking configuration")
public class TollParkingConfig {

  @ApiModelProperty(
      name = "numberOfStandardParkingSlot",
      required = true,
      value = "100",
      notes = "number of standard parking slot")
  private int numberOfStandardParkingSlot;

  @ApiModelProperty(
      name = "numberOfElectricCar20KWParkingSlot",
      required = true,
      value = "100",
      notes = "number of electric car parking slot with 20kW supply")
  private int numberOfElectricCar20KWParkingSlot;

  @ApiModelProperty(
      name = "numberOfElectricCar50KWParkingSlot",
      required = true,
      value = "100",
      notes = "number of electric car parking slot with 50kW supply")
  private int numberOfElectricCar50KWParkingSlot;

  @ApiModelProperty(name = "pricingPolicy", required = true, notes = "toll parking pricing policy")
  private PricingPolicy pricingPolicy;

  public TollParkingConfig(
      int numberOfStandardParkingSlot,
      int numberOfElectricCar20KWParkingSlot,
      int numberOfElectricCar50KWParkingSlot,
      PricingPolicy pricingPolicy) {
    this.numberOfStandardParkingSlot = numberOfStandardParkingSlot;
    this.numberOfElectricCar20KWParkingSlot = numberOfElectricCar20KWParkingSlot;
    this.numberOfElectricCar50KWParkingSlot = numberOfElectricCar50KWParkingSlot;
    this.pricingPolicy = pricingPolicy;
  }

  public int getNumberOfStandardParkingSlot() {
    return numberOfStandardParkingSlot;
  }

  public int getNumberOfElectricCar20KWParkingSlot() {
    return numberOfElectricCar20KWParkingSlot;
  }

  public int getNumberOfElectricCar50KWParkingSlot() {
    return numberOfElectricCar50KWParkingSlot;
  }

  public PricingPolicy getPricingPolicy() {
    return pricingPolicy;
  }
}
