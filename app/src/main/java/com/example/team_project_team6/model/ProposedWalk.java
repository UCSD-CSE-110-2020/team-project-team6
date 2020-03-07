package com.example.team_project_team6.model;

public class ProposedWalk{
    private String pDayMonthYearDate;
    private String pHourSecondTime;
    private Route pRoute;

    public ProposedWalk(Route route, String dayMonthYearDate, String hourSecondTime) {
        this.pRoute = route;
        this.pDayMonthYearDate = dayMonthYearDate;
        this.pHourSecondTime = hourSecondTime;
    }

    public String getpDayMonthYearDate() {
        return pDayMonthYearDate;
    }

    public String getpHourSecondTime() {
        return pHourSecondTime;
    }

    public Route getpRoute() {
        return pRoute;
    }
}
