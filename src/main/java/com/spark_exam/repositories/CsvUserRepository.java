package com.spark_exam.repositories;

import com.spark_exam.models.Country;
import com.spark_exam.models.CsvUser;
import com.spark_exam.models.Event;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CsvUserRepository {

    @Autowired
    private SparkSession sparkSession;

    public void displayCsvUsers(){
        Encoder<CsvUser> userEncoder = Encoders.bean(CsvUser.class);
        String path = "data/users.json";
        // read CSV file to Dataset
        Dataset<CsvUser> ds = sparkSession.read().option("multiline", true).json(path).as(userEncoder);
        ds.show();
    }

    public Dataset<CsvUser> getCsvUsers(){
        Encoder<CsvUser> userEncoder = Encoders.bean(CsvUser.class);
        String path = "data/users.json";
        // read CSV file to Dataset
        return sparkSession.read().option("multiline", true).json(path).as(userEncoder);
        //return sparkSession.read().option("multiline", true).json(path);
    }


    public List<CsvUser> getCsvUsersList(){

        List<CsvUser> users = new ArrayList<CsvUser>();

        /*users.add(new CsvUser(1,"Alex", "Popov", "USA", "email@mail999.com" ));
        users.add(new CsvUser(2,"Igor", "Pov", "PL", "email@mail998.com" ));
        users.add(new CsvUser(3,"Mat", "Damon", "USA", "email@mail997.com" ));
        users.add(new CsvUser(4,"Steave", "Jobs", "DE", "email@mail996.com" ));
        users.add(new CsvUser(5,"Garry", "Gogol", "USA", "email@mail995.com" ));
        users.add(new CsvUser(6,"Rally", "Ral", "USA", "email@mail994.com" ));
        users.add(new CsvUser(7,"Sov", "Stasii", "PL", "email@mail99.com" ));
        users.add(new CsvUser(8,"Danny", "Bod", "USA", "email@mail949.com" ));
        users.add(new CsvUser(9,"Maik", "Hov", "PL", "email@mai9955.com" ));
        users.add(new CsvUser(10,"Hobc", "Entiny", "DE", "email@mail49.com" ));
        users.add(new CsvUser(11,"Stan", "Jeramy", "USA", "email@mail299.com" ));
        users.add(new CsvUser(12,"Gogolen", "Hgfg", "DE", "email@mail939.com" ));
        users.add(new CsvUser(13,"Wiktorya", "Alo", "PL", "email@mail923329.com" ));
        users.add(new CsvUser(14,"Mis", "Jafo", "USA", "email@mail992349.com" ));
        users.add(new CsvUser(15,"Ritta", "Skitter", "PL", "email@mail.com" ));
        users.add(new CsvUser(16,"Janny", "Hobbu", "USA", "email@madsfil999.com" ));
        users.add(new CsvUser(17,"Ron", "Wisley", "DE", "email@maisdfl999.com" ));
        users.add(new CsvUser(18,"Harry", "Potter", "USA", "email@mail999.com" ));
        users.add(new CsvUser(19,"Germiona", "Greindger", "USA", "email@mafffil999.com" ));
        users.add(new CsvUser(20,"Hagrid", "Bro", "PL", "email@mal999.com" ));*/

        users.add(new CsvUser(1,"Alex", "Popov", Country.USA, "email@mail999.com" ));
        users.add(new CsvUser(2,"Igor", "Pov", Country.PL, "email@mail998.com" ));
        users.add(new CsvUser(3,"Mat", "Damon", Country.USA, "email@mail997.com" ));
        users.add(new CsvUser(4,"Steave", "Jobs", Country.DE, "email@mail996.com" ));
        users.add(new CsvUser(5,"Garry", "Gogol", Country.USA, "email@mail995.com" ));
        users.add(new CsvUser(6,"Rally", "Ral", Country.USA, "email@mail994.com" ));
        users.add(new CsvUser(7,"Sov", "Stasii", Country.PL, "email@mail99.com" ));
        users.add(new CsvUser(8,"Danny", "Bod", Country.USA, "email@mail949.com" ));
        users.add(new CsvUser(9,"Maik", "Hov", Country.PL, "email@mai9955.com" ));
        users.add(new CsvUser(10,"Hobc", "Entiny", Country.DE, "email@mail49.com" ));
        users.add(new CsvUser(11,"Stan", "Jeramy", Country.USA, "email@mail299.com" ));
        users.add(new CsvUser(12,"Gogolen", "Hgfg", Country.DE, "email@mail939.com" ));
        users.add(new CsvUser(13,"Wiktorya", "Alo", Country.PL, "email@mail923329.com" ));
        users.add(new CsvUser(14,"Mis", "Jafo", Country.USA, "email@mail992349.com" ));
        users.add(new CsvUser(15,"Ritta", "Skitter", Country.PL, "email@mail.com" ));
        users.add(new CsvUser(16,"Janny", "Hobbu", Country.USA, "email@madsfil999.com" ));
        users.add(new CsvUser(17,"Ron", "Wisley", Country.DE, "email@maisdfl999.com" ));
        users.add(new CsvUser(18,"Harry", "Potter", Country.USA, "email@mail999.com" ));
        users.add(new CsvUser(19,"Germiona", "Greindger", Country.USA, "email@mafffil999.com" ));
        users.add(new CsvUser(20,"Hagrid", "Bro", Country.PL, "email@mal999.com" ));

        return users;
    }
}
