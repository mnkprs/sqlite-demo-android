package com.example.manos.project21324;

public class Coordinates {
	
	//private variables
	int _id;
	String _userid;
	String _latitude;
	String _longitude;
	String _dt;

	// Empty constructor
    public Coordinates() {

    }
	// constructor
    public Coordinates(int _id) {
        this._id = _id;
    }
    public Coordinates(String _userid, String _latitude, String _longitude, String _dt) {
        this._userid = _userid;
        this._latitude = _latitude;
        this._longitude = _longitude;
        this._dt = _dt;
    }
    public Coordinates(String _userid,String _dt) {
        this._userid = _userid;
        this._dt = _dt;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_latitude() {
        return _latitude;
    }

    public void set_latitude(String _latitude) {
        this._latitude = _latitude;
    }

    public String get_longitude() {
        return _longitude;
    }

    public void set_longitude(String _longitude) {
        this._longitude = _longitude;
    }

    public String get_userid() {
        return _userid;
    }

    public void set_userid(String _userid) {
        this._userid = _userid;
    }

    public String get_dt() {
        return _dt;
    }

    public void set_dt(String _dt) {
        this._dt = _dt;
    }
}
