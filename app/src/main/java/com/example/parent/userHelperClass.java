package com.example.parent;

public class userHelperClass {

    String daysString, mobile, fatherName, motherName, studentName, schoolName, standard,Coordinates,add,local,stage,landmark,timeStart, timeEnd;


    public userHelperClass(){

        this.mobile = "";
        this.fatherName="";
        this.motherName="";
        this.studentName="";
        this.schoolName="";
        this.standard="";
        this.timeStart="";
        this.timeEnd="";
        this.add="";
        this.local="";
        this.stage="";
        this.landmark="";
        this.Coordinates="";
    }


    public userHelperClass(String days){
        this.daysString=days;
    }


    public userHelperClass(String fatherName, String motherName, String studentName, String schoolName,
                           String standard, String timeStart, String timeEnd, String Coordinates,String add,
                           String local,String stage,String landmark, String mobile) {

        this.mobile = mobile;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.studentName = studentName;
        this.schoolName = schoolName;
        this.standard = standard;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.add=add;
        this.local=local;
        this.stage=stage;
        this.landmark=landmark;
        this.Coordinates= Coordinates;

    }
    public String getCoordinates() {
        return Coordinates;
    }
    public String getMobile(){ return mobile; }
    public void setCoordinates(String Coordinates) {
        this.Coordinates = Coordinates;
    }
    public String getadd() {
        return add;
    }
    public void setadd(String add) {
        this.add = add;
    }
    public String getlocal() {
        return local;
    }
    public void setlocal(String local) {
        this.local = local;
    }
    public String getstage() {
        return stage;
    }
    public void setstage(String stage) {
        this.stage = stage;
    }
    public String getlandmark() {
        return landmark;
    }
    public void setlandmark(String landmark) {
        this.landmark = landmark;
    }
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }



    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
