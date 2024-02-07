package org.example.app.boredapi.common;

public class ActivityTo {
    private String activity;
    private String type;
    private Integer participants;
    private Float price;
    private String link;
    private Long key;
    private Integer accessibility;

    public ActivityTo() {
        // Nothing to do here
    }

    public ActivityTo(String activity) {
        this.activity = activity;
    }

    public String getActivity() {
        return activity;
    }

    public String getType() {
        return type;
    }

    public int getParticipants() {
        return participants;
    }

    public float getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    public long getKey() {
        return key;
    }

    public int getAccessibility() {
        return accessibility;
    }
}
