package com.lkathary.kafka.controllesrs;

import com.lkathary.kafka.model.dto.CarDTO;
import com.lkathary.kafka.model.entity.Car;
import com.lkathary.kafka.repositories.CarRepository;
import com.lkathary.kafka.servises.KafkaProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Tag(name = "Car controller")
@RestController
@RequiredArgsConstructor
public class Controller {

    private final CarRepository carRepository;
    private final KafkaProducer kafkaProducer;

    @Operation(
            summary = "Add new car to DB",
            description = "Based on the CarDTO, builds a Car and places it in the DB"
    )
    @PostMapping("/add-car")
    public void addCar(@RequestBody CarDTO carDTO) {
        log.info("New record: " + carRepository.save(
                Car.builder()
                        .name(carDTO.getName())
                        .age(carDTO.getAge())
                        .price(carDTO.getPrice())
                        .build())
        );
    }

    @Operation(
            summary = "Get all the records of the Cars",
            description = "List of cars sorted by name"
    )
    @GetMapping("/all")
    public List<Car> getAll() {
        return carRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Car::getName))
                .toList();
    }

    @Operation(
            summary = "Get particular the record of the Car"
    )
    @GetMapping("/get-car")
    public Car getCar(@RequestParam Long id) {
        return carRepository.findById(id).orElseThrow();
    }

    @Operation(
            summary = "Delete particular the record of the Car"
    )
    @DeleteMapping("/del-car")
    public void delCar(@RequestParam Long id) {
        carRepository.deleteById(id);
    }

    @Operation(
            summary = "Update particular the record of the Car"
    )
    @PutMapping("/update-car/{id}")
    public void updateCar(@PathVariable Long id, @RequestBody CarDTO carDTO) {
        if (carRepository.existsById(id)) {
            Car updatedCar = Car.builder()
                    .name(carDTO.getName())
                    .age(carDTO.getAge())
                    .price(carDTO.getPrice())
                    .build();
            updatedCar.setId(id);
            log.info("Update record: " + carRepository.save(updatedCar));
        } else {
            log.info("No such record");
        }
    }

    @Operation(
            summary = "Dispatching the Car to Kafka"
    )
    @PostMapping("/kafka-send")
    public String kafkaSend(@RequestParam Long id) {
        Optional<Car> car= carRepository.findById(id);
        if(car.isPresent()) {
            kafkaProducer.kafkaSender(car.get().toString());
            return "Success: Car sent";
        }
        return "Fail: Car is absent";
    }
}
