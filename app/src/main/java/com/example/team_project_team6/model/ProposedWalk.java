package com.example.team_project_team6.model;

public class ProposedWalk{
    private String pDayMonthYearDate;
    private String pHourSecondTime;
    private Route pRoute;
    private String proposer;
    private boolean scheduled;

    public ProposedWalk() {
        this.proposer = "";
        scheduled = false;
    }

    public ProposedWalk(Route route, String dayMonthYearDate, String hourSecondTime) {
        this.pRoute = route;
        this.pDayMonthYearDate = dayMonthYearDate;
        this.pHourSecondTime = hourSecondTime;
        proposer = "";
        scheduled = false;
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

    public String getProposer() {
        return proposer;
    }

    public void setpDayMonthYearDate(String pDayMonthYearDate) {
        this.pDayMonthYearDate = pDayMonthYearDate;
    }

    public void setpHourSecondTime(String pHourSecondTime) {
        this.pHourSecondTime = pHourSecondTime;
    }

    public void setpRoute(Route pRoute) {
        this.pRoute = pRoute;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public Boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }
}
