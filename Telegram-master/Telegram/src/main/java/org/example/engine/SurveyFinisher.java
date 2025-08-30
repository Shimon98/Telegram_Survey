package org.example.engine;


import org.example.community.CommunityBroadcaster;
import org.example.gui.components.ChartImageBuilder;

public class SurveyFinisher {
    private static final String MSG_HEADER_ALL = "Survey ended: everyone answered.";
    private static final String MSG_HEADER_TIMEOUT = "Survey ended: time expired.";
    private String lastHeader;
    private String lastSummary;
    private String lastChartPath;
    private SurveyCloser surveyCloser;
    private SurveyState surveyState;
    private SurveyResult surveyResult;
    private SurveyResultFormatter formatter;
    private CommunityBroadcaster broadcaster;

    public SurveyFinisher(SurveyCloser surveyCloser, SurveyState surveyState, SurveyResult surveyResult,
                          SurveyResultFormatter formatter, CommunityBroadcaster broadcaster) {
        this.surveyCloser = surveyCloser;
        this.surveyState = surveyState;
        this.surveyResult = surveyResult;
        this.formatter = formatter;
        this.broadcaster = broadcaster;
    }

    public void finishAllAnswered() {
        finishNow(MSG_HEADER_ALL);
    }

    public void finishTimeout() {
        finishNow(MSG_HEADER_TIMEOUT);
    }

    
    private void finishNow(String header) {
        this.surveyCloser.closeNow();
        String summary = this.formatter.buildSummary(this.surveyState, this.surveyResult);
        this.lastHeader = header;
        this.lastSummary = summary;
        this.lastChartPath = ChartImageBuilder.buildCombinedImage(this.surveyState, this.surveyResult);
        this.broadcaster.broadcast(header);
        System.out.println(header);
    }


    
    public String getLastHeader() { return this.lastHeader; }
    public String getLastSummary() { return this.lastSummary; }
    public String getLastChartPath() { return this.lastChartPath; }
}
