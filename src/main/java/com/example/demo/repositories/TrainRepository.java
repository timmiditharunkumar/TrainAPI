package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Trains;

public interface TrainRepository extends JpaRepository<Trains, String> {
}
