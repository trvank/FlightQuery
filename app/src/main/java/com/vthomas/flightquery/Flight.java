package com.vthomas.flightquery;

/**
 * Created by Thomas on 11/4/2016.
 */
public class Flight {
    private String airline;
    private int num;
    private String depart_city;
    private int depart_hour;
    private int depart_min;
    private String depart_time;
    private String arrive_city;
    private int arrive_hour;
    private int arrive_min;
    private String arrive_time;
    private String status;
    private int delay_hour;
    private int delay_min;
    private String delay_time;

    public Flight(String airline, int num, String depart_city, String depart_time,
                  String arrive_city, String arrive_time, String status,
                  String delay_time){
        this.airline = airline;
        this.num = num;
        this.depart_city = depart_city;
        this.depart_time = depart_time;
        this.arrive_city = arrive_city;
        this.arrive_time = arrive_time;
        this.status = status;
        this.delay_time = delay_time;
    }

    public String get_airline(){
        return airline;
    }

    public int get_flight(){
        return num;
    }

    public String get_depart_city(){
        return depart_city;
    }

    public String get_depart_time(){
        return depart_time;
    }


    public String get_arrive_city(){
        return arrive_city;
    }

    public String get_arrive_time(){
        return arrive_time;
    }

    public String get_status(){
        return status;
    }

    public String get_delay_time(){
        return delay_time;
    }

    public String to_string(){
        return airline + " " + Integer.toString(num) + " " +
                depart_city + " " + depart_time + " " + arrive_city + " " + arrive_time + " " + status;
    }
}
