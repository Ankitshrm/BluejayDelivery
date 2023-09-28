package com.internship.bluejaydelivery.repositories;

import com.internship.bluejaydelivery.models.TimeCard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TimeCardRepo extends JpaRepository<TimeCard,Integer> {

}
