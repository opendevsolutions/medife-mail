package ar.com.opendevsolutions.commons.mail;

import java.util.Date;

public class EmailStructure {

    private String from;
    private String to;
    private String subject;
    private String subjectFormat;
    private String text;
    private String textFormat;
    private Date sentDate;

    public void setFrom(String internetAddress) {
        this.from = internetAddress;
    }

    public void setSubject(String title, String format) {
        this.subject = title;
        this.subjectFormat = format;
    }

    public void setText(String body, String format) {
        this.text = body;
        this.textFormat = format;
    }

    public void setSentDate(Date date) {
        this.sentDate = date;
    }

    public void setRecipient(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getSubjectFormat() {
        return subjectFormat;
    }

    public String getText() {
        return text;
    }

    public String getTextFormat() {
        return textFormat;
    }

    public Date getSentDate() {
        return sentDate;
    }
}
