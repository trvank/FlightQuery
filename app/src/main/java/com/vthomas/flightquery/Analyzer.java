package com.vthomas.flightquery;

import android.app.DownloadManager;
import android.util.Log;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Thomas on 11/4/2016.
 */
public class Analyzer {



    static QueryResult airline(Map<String, JsonElement> map, String action){
        QueryResult q = new QueryResult();
        //all the flights
//        ArrayList<Flight> flights = create_list();
        Flight[] flights = create_array();
        boolean[] flight_check = {true, true, true, true, true, true, true, true, true, true};
        ArrayList<String> airline_list = new ArrayList<>();
        ArrayList<String> depart_time_list = new ArrayList<>();
        ArrayList<String> arrive_time_list = new ArrayList<>();
        ArrayList<String> depart_city_list = new ArrayList<>();
        ArrayList<String> arrive_city_list = new ArrayList<>();

        String airline_string = "";
        String num_string = "";
        String depart_city_string = "";
        String arrive_city_string = "";
        String depart_time_string = "";
        String arrive_time_string = "";
        String status_string = "";
        String intro_string = "";
        String flight_string = "";

        Log.d("curious", String.valueOf("02:55:00".compareTo("02:54:00")));
        Log.d("curious", String.valueOf("02:55:00".compareTo("02:55:00")));
        Log.d("curious", String.valueOf("02:55:00".compareTo("02:56:00")));
        Log.d("checking...", map.toString());
        Log.d("shouldn't have", String.valueOf(map.containsKey("airline")));
        //go through flights and remove ones that don't match parameters
        for(int i = 0; i < 10; i++){
            if(map.containsKey("airline")){
                airline_string = " with airline " + map.get("airline");
                Log.d("checking...", "checking airline for" + i + " val for airline = " + map.get("airline") + " and we have = " + flights[i].get_airline());
                if(!(flights[i].get_airline().equals(map.get("airline").toString().replace("\"", "")))){
                    flight_check[i] = false;
                }
            }

            if(map.containsKey("flight_num")){
                num_string = " flight " + map.get("flight_num");
                if(!(Integer.toString(flights[i].get_flight()).equals(map.get("flight_num").toString().replace("\"", "")))){
                    flight_check[i] = false;
                }
            }

            if(map.containsKey("depart_city")){
                depart_city_string = " flying from " + map.get("depart_city");
                if(!(flights[i].get_depart_city().equals(map.get("depart_city").toString().replace("\"", "")))){
                    flight_check[i] = false;
                }
            }

            if(map.containsKey("depart_time")){
                int check = flights[i].get_depart_time().compareTo(map.get("depart_time").toString().replace("\"", ""));
                if(map.containsKey("time_frame")){
                    if(map.get("time_frame").toString().replace("\"", "").equals("at")){
                        depart_time_string = " departing at " + map.get("depart_time");
                        if(check != 0){
                            flight_check[i] = false;
                        }
                    }
                    else if(map.get("time_frame").toString().replace("\"", "").equals("after")){
                        depart_time_string = " departing after " + map.get("depart_time");
                        if(!(check < 0)){
                            flight_check[i] = false;
                        }
                    }
                    else if(map.get("time_frame").toString().replace("\"", "").equals("before")){
                        depart_time_string = " departing before " + map.get("depart_time");
                        if(!(check > 0)){
                            flight_check[i] = false;
                        }
                    }
                    //TODO DO WE NEED TO SAY IF NO TIME FRAME -> NOT ENOUGH INFO????
                }
            }

            if(map.containsKey("arrive_city")){
                Log.d("checking...", "checking arrival for" + i);
                Log.d("arrival", "arrival = " + map.get("arrive_city") + " array has = " + flights[i].get_arrive_city());
                arrive_city_string = " flying to " + map.get("arrive_city");
                if(!(flights[i].get_arrive_city().equals(map.get("arrive_city").toString().replace("\"", "")))){
                    Log.d("updating", "update " + i + " to false");
                    flight_check[i] = false;
                }
            }

            if(map.containsKey("arrive_time")){
                int check = flights[i].get_arrive_time().compareTo(map.get("arrive_time").toString().replace("\"", ""));
                if(map.containsKey("time_frame")){
                    if(map.get("time_frame").toString().replace("\"", "").equals("at")){
                        arrive_time_string = " arriving at " + map.get("arrive_time");
                        if(check != 0){
                            flight_check[i] = false;
                        }
                    }
                    else if(map.get("time_frame").toString().replace("\"", "").equals("after")){
                        arrive_time_string = " arriving after " + map.get("arrive_time");
                        if(!(check < 0)){
                            flight_check[i] = false;
                        }
                    }
                    else if(map.get("time_frame").toString().replace("\"", "").equals("before")){
                        arrive_time_string = " arriving before " + map.get("arrive_time");
                        if(!(check > 0)){
                            flight_check[i] = false;
                        }
                    }
                    //TODO DO WE NEED TO SAY IF NO TIME FRAME -> NOT ENOUGH INFO????
                }
            }

            if(map.containsKey("status")){
                status_string = " with status of " + map.get("status");
                if(!(flights[i].get_status().equals(map.get("status").toString().replace("\"", "")))){
                    flight_check[i] = false;
                }
            }

        }

        //fill data lists so speech know what to say
        //fill text string (to show full info of flights matching query)
        for(int i = 0; i < 10; i++){
            if(flight_check[i]){
 //               Log.d("string", i + " = " + flight_check[i] + " need " + flights[i].to_String());
                if(!(airline_list.contains(flights[i].get_airline()))) {
                    airline_list.add(flights[i].get_airline());
                }
                if(!(depart_time_list.contains(flights[i].get_delay_time()))) {
                    depart_time_list.add(flights[i].get_depart_time());
                }
                if(!(arrive_time_list.contains(flights[i].get_arrive_time()))) {
                    arrive_time_list.add(flights[i].get_arrive_time());
                }
                if(!(depart_city_list.contains(flights[i].get_depart_city()))){
                    depart_city_list.add(flights[i].get_depart_city());
                }
                if(!(arrive_city_list.contains(flights[i].get_arrive_city()))){
                    arrive_city_list.add(flights[i].get_arrive_city());
                }
                flight_string = flight_string.concat("\n" + flights[i].to_string());
                Log.d("string", "string is " + flight_string);
            }
        }




        //set the speech string to say only info asked for
        if(action.equals("flight")){
            intro_string = "The flights " +  airline_string + num_string + depart_city_string +
                    arrive_city_string + depart_time_string + arrive_time_string + status_string + " are: ";
            for(int i = 0; i < 10; i++){
                if(flight_check[i]){
                    intro_string = intro_string.concat("\n" + flights[i].get_airline() + " flight " + flights[i].get_flight());
                }
            }
        }
        else if(action.equals("airline")){
            intro_string = "The airlines " +  airline_string + num_string + depart_city_string +
                    arrive_city_string + depart_time_string + arrive_time_string + status_string + " are: ";
            for(String airline: airline_list){
                intro_string = intro_string.concat("\n" + airline);
            }

        }

        //TODO: city in ai.api and here

        else if(action.equals("city")){
            intro_string = "The cities " +  airline_string + num_string + depart_city_string +
                    arrive_city_string + depart_time_string + arrive_time_string + status_string + " are: ";
        }

        else if(action.equals("time")){
            intro_string = "The times for that are: ";

        }

        //set the strings to be passed back to mainActivity
        q.set_text_string(flight_string);
        q.set_speech_string(intro_string);
        return q;
    }

    static private Flight[] create_array(){
        Flight[] table = {
                new Flight("AjaxAir", 113, "Portland", "08:03:00", "Atlanta", "12:51:00", "Landed", "99:99:99"),
                new Flight("AjaxAir", 114, "Atlanta", "14:05:00", "Portland", "16:44:00", "Boarding", "99:99:99"),
                new Flight("BakerAir", 121, "Atlanta", "17:14:00", "New York", "19:20:00", "Departed", "99:99:99"),
                new Flight("BakerAir", 122, "New York", "21:00:00", "Portland", "00:13:00", "Scheduled", "99:99:99"),
                new Flight("BakerAir", 124, "Portland", "09:03:00", "Atlanta", "12:52:00", "Delayed", "09:55:00"),
                new Flight("CarsonAir", 522, "Portland", "14:15:00", "New York", "16:58:00", "Scheduled", "99:99:99"),
                new Flight("CarsonAir", 679, "New York", "09:30:00", "Atlanta", "11:30:00", "Departed", "99:99:99"),
                new Flight("CarsonAir", 670, "New York", "09:30:00", "Portland", "12:05:00", "Departed", "99:99:99"),
                new Flight("CarsonAir", 671, "Atlanta", "13:20:00", "New York", "14:55:00", "Scheduled", "99:99:99"),
                new Flight("CarsonAir", 672, "Portland", "13:25:00", "New York", "20:36:00", "Scheduled", "99:99:99")
        };
        return table;
    }

    static private ArrayList<Flight> create_list(){
        ArrayList<Flight> table = new ArrayList<Flight>();
        table.add(new Flight("AjaxAir", 113, "Portland", "08:03:00", "Atlanta", "12:51:00", "Landed", "99:99:99"));
        table.add(new Flight("AjaxAir", 114, "Atlanta", "14:05:00", "Portland", "16:44:00", "Boarding", "99:99:99"));
        table.add(new Flight("BakerAir", 121, "Atlanta", "17:14:00", "New York", "19:20:00", "Departed", "99:99:99"));
        table.add(new Flight("BakerAir", 122, "New York", "21:00:00", "Portland", "00:13:00", "Scheduled", "99:99:99"));
        table.add(new Flight("BakerAir", 124, "Portland", "09:03:00", "Atlanta", "12:52:00", "Delayed", "09:55:00"));
        table.add(new Flight("CarsonAir", 522, "Portland", "14:15:00", "New York", "16:58:00", "Scheduled", "99:99:99"));
        table.add(new Flight("CarsonAir", 679, "New York", "09:30:00", "Atlanta", "11:30:00", "Departed", "99:99:99"));
        table.add(new Flight("CarsonAir", 670, "New York", "09:30:00", "Portland", "12:05:00", "Departed", "99:99:99"));
        table.add(new Flight("CarsonAir", 671, "Atlanta", "13:20:00", "New York", "14:55:00", "Scheduled", "99:99:99"));
        table.add(new Flight("CarsonAir", 672, "Portland", "13:25:00", "New York", "20:36:00", "Scheduled", "99:99:99"));

        return table;
    }


}
