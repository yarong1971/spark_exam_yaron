package com.spark_exam.services;

import com.spark_exam.models.CsvUser;
import com.spark_exam.repositories.CsvUserRepository;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.*;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class CsvUserService implements Serializable {
    @Autowired
    private CsvUserRepository csvUserRepository;
    @Autowired
    private SparkSession sparkSession;

    //public void displayUsers(){
        //csvUserRepository.getUsers();
    //}

    public Dataset<CsvUser> getUsersList(){

        return csvUserRepository.getCsvUsers();
        //Dataset usersDS = sparkSession.createDataset(usersList,Encoder<CsvUser>);
        //return usersDS;
    }

}
