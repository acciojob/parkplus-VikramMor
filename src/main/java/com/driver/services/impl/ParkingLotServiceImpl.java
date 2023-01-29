package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot(name,address);
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        SpotType spotType;
        if(numberOfWheels>4){
            spotType = SpotType.OTHERS;
        }
        else if (numberOfWheels>2) {
            spotType = SpotType.FOUR_WHEELER;
        }
        else {
            spotType = SpotType.TWO_WHEELER;
        }
        Spot spot = new Spot();

        spot.setParkingLot(parkingLot);
        spot.setSpotType(spotType);
        spot.setOccupied(false);
        spot.setPricePerHour(pricePerHour);
        parkingLot.getSpotList().add(spot);
        parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

        Spot newSpot = new Spot();
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList=parkingLot.getSpotList();
        for(Spot spot:spotList){
            if(spot.getId()==spotId){
                newSpot=spot;
            }
        }
        newSpot.setParkingLot(parkingLot);
        newSpot.setPricePerHour(pricePerHour);

        spotRepository1.save(newSpot);
        return newSpot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
