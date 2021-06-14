package com.spark_exam.services;

import com.spark_exam.models.User;
import com.spark_exam.repositories.UserRepository;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private SparkSession sparkSession;
    @Autowired
    private UserRepository userRepository;

    public List<User> getListUsers() {
        return userRepository.findAll();
    }

    public Dataset<User> getDatasetUsers() {
        Encoder<User> userEncoder = Encoders.bean(User.class);
        return sparkSession.createDataset(userRepository.findAll(),userEncoder);

    }
}
