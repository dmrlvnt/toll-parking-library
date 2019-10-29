package com.contemporaryparking.controller;

import com.contemporaryparking.business.ITollParkingManager;
import com.contemporaryparking.config.TollParkingConfig;
import com.contemporaryparking.data.ParkingBill;
import com.contemporaryparking.data.ParkingSlot;
import com.contemporaryparking.data.PricingPolicy;
import com.contemporaryparking.exception.AlreadyInitialized;
import com.contemporaryparking.exception.InvalidPricingPolicy;
import com.contemporaryparking.exception.NoAvailableParkingSlot;
import com.contemporaryparking.exception.NoParkingBill;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(
    value = "TollParkingController",
    tags = {"TollParking Controller"})
@SwaggerDefinition(
    tags = {
        @Tag(name = "TollParking Controller", description = "The controller class for Toll Parking")
    })
@RestController("/api/v1")
public class TollParkingController {

  private ITollParkingManager tollParkingManager;

  @Autowired
  public TollParkingController(ITollParkingManager tollParkingManager) {
    this.tollParkingManager = tollParkingManager;
  }

  @ApiOperation(value = "Initialize toll parking library", response = TollParkingConfig.class)
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "OK: Successfully initialized"),
          @ApiResponse(code = 403, message = "Forbidden: Already initialized")
      })
  @PostMapping("/initialize")
  public ResponseEntity<TollParkingConfig> initializeTollParking(
      @RequestBody TollParkingConfig tollParkingConfig) throws AlreadyInitialized {
    TollParkingConfig tpc =
        Optional.ofNullable(tollParkingManager.initialize(tollParkingConfig))
            .orElseThrow(
                () -> new AlreadyInitialized("Toll parking library has been already initialized"));
    return ResponseEntity.ok(tpc);
  }

  @ApiOperation(
      value = "Change pricing policy for toll parking library",
      response = PricingPolicy.class)
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "OK: Pricing policy changed"),
          @ApiResponse(code = 403, message = "Forbidden: Invalid pricing policy")
      })
  @PutMapping("/pricingpolicy")
  public ResponseEntity<PricingPolicy> updatePricingPolicy(@RequestBody PricingPolicy pricingPolicy)
      throws InvalidPricingPolicy {
    if (tollParkingManager.updatePricingPolicy(pricingPolicy)) {
      return ResponseEntity.ok(pricingPolicy);
    } else {
      throw new InvalidPricingPolicy("Invalid pricing policy rejected");
    }
  }

  @ApiOperation(value = "Get first available parking slot", response = ParkingSlot.class)
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "OK: Parking slot retrieved"),
          @ApiResponse(code = 404, message = "Not found: No available parking slot")
      })
  @GetMapping("/enterparking/{plateNumber}")
  public ResponseEntity<ParkingSlot> enterParking(@PathVariable("plateNumber") String plateNumber)
      throws NoAvailableParkingSlot {
    ParkingSlot parkingSlot =
        tollParkingManager
            .getParkingSlot(plateNumber)
            .orElseThrow(
                () ->
                    new NoAvailableParkingSlot(
                        "No available parking slot has been found for this car: " + plateNumber));

    return ResponseEntity.ok(parkingSlot);
  }

  @ApiOperation(value = "Leave parking slot, and return the bill", response = ParkingBill.class)
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "OK: Parking bill retrieved"),
          @ApiResponse(code = 404, message = "Not found: Parking bill not found")
      })
  @GetMapping("/leaveparking/{plateNumber}")
  public ResponseEntity<ParkingBill> leaveParking(@PathVariable("plateNumber") String plateNumber)
      throws NoParkingBill {
    ParkingBill parkingBill =
        tollParkingManager
            .leaveParking(plateNumber)
            .orElseThrow(
                () ->
                    new NoParkingBill(
                        "No parking bill has been found for this car: " + plateNumber));

    return ResponseEntity.ok(parkingBill);
  }
}
