package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        try{
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            User user = userRepository3.findById(userId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            boolean spotBooked=false;
            for(Spot spot:spotList){
                if(!spot.isOccupied()){
                    spotBooked=true;
                    break;
                }
            }

            if(!spotBooked){
                throw new Exception("Cannot make reservation");
            }
            SpotType spotType;
            if(numberOfWheels>4){
                spotType=SpotType.OTHERS;
            }
            else if (numberOfWheels>2) {
                spotType=SpotType.FOUR_WHEELER;
            }
            else {
                spotType=SpotType.TWO_WHEELER;
            }

            int minimumPrice=Integer.MAX_VALUE;
            spotBooked=false;
            Spot requiredSpot = new Spot();

            for(Spot spot:spotList){
                if(spotType.equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS)){
                    if(spot.getPricePerHour()*timeInHours < minimumPrice && !spot.isOccupied()){
                        minimumPrice=spot.getPricePerHour()*timeInHours;
                        spotBooked=true;
                        requiredSpot=spot;
                    }
                }
                else if (spotType.equals(SpotType.FOUR_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS)||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER) ) {
                    if(spot.getPricePerHour()*timeInHours<minimumPrice && !spot.isOccupied()){
                        minimumPrice=spot.getPricePerHour()*timeInHours;
                        spotBooked=true;
                        requiredSpot=spot;
                    }
                } else if (spotType.equals(SpotType.TWO_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.OTHERS)) {
                    if(spot.getPricePerHour()*timeInHours<minimumPrice && !spot.isOccupied()){
                        minimumPrice=spot.getPricePerHour()*timeInHours;
                        spotBooked=true;
                        requiredSpot=spot;
                    }
                }
            }
            if(!spotBooked){
                throw new Exception("Cannot make reservation");
            }
            assert requiredSpot != null;
            requiredSpot.setOccupied(true);

            Reservation reservation=new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(requiredSpot);
            reservation.setUser(user);

            requiredSpot.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(requiredSpot);

            return reservation;
        }
        catch (Exception e){
            return null;
        }
    }
}
