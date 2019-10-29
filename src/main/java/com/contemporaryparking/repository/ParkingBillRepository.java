package com.contemporaryparking.repository;

import com.contemporaryparking.data.ParkingBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingBillRepository extends JpaRepository<ParkingBill, Long> {

}
