package com.example.project2021;

public class CovidEntry {
    private String prov;
    private String date;
    private String cases;
    private long dbID;

    public CovidEntry(String prov, String date, String cases) {
        this.prov = prov;
        this.date = date;
        this.cases = cases;
    }

    public void setDbID(long entryID) {
        this.dbID = entryID;
    }

    public String getProv() { return this.prov; }
    public String getDate() { return this.date; }
    public String getCases() { return this.cases; }
    public long getDbID() { return this.dbID; }
}

