package com.contemporaryparking.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@ApiModel(description = "Class representing Parking bill")
@Entity
@Table(name = "ParkingBills")
public class ParkingBill {

  @ApiModelProperty(
      name = "id",
      required = true,
      value = "123",
      notes = "The database generated parking bill ID")
  private long id;

  @ApiModelProperty(
      name = "plateNumber",
      required = true,
      value = "AB 123 CD",
      notes = "customer vehicle plate number")
  private String plateNumber;

  @ApiModelProperty(name = "parkingSlot", required = true, value = "123", notes = "parking slot")
  private ParkingSlot parkingSlot;

  @ApiModelProperty(
      name = "hourPrice",
      required = true,
      value = "7.5",
      notes = "parking hour price")
  private double price;

  @ApiModelProperty(
      name = "start",
      required = true,
      value = "2018-04-10T03:34:18.115",
      notes = "parking start time")
  private LocalDateTime start;

  @ApiModelProperty(
      name = "end",
      required = false,
      value = "2018-04-10T03:34:18.115",
      notes = "parking end time")
  private LocalDateTime end;

  public ParkingBill(String plateNumber, ParkingSlot parkingSlot, LocalDateTime start) {
    this.plateNumber = plateNumber;
    this.parkingSlot = parkingSlot;
    this.start = start;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ManyToOne
  @JoinColumn(name = "PARKINGSLOTS_ID")
  public ParkingSlot getParkingSlot() {
    return parkingSlot;
  }

  public void setParkingSlot(ParkingSlot parkingSlot) {
    this.parkingSlot = parkingSlot;
  }

  @Column(name = "plate_number", nullable = false)
  public String getPlateNumber() {
    return plateNumber;
  }

  public void setPlateNumber(String plateNumber) {
    this.plateNumber = plateNumber;
  }

  @Column(name = "price", nullable = false)
  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Column(name = "start_time", nullable = false)
  public LocalDateTime getStart() {
    return start;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  @Column(name = "end_time", nullable = false)
  public LocalDateTime getEnd() {
    return end;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }
}
