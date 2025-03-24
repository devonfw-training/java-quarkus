package org.example.app.task.common;

import org.example.app.general.common.search.SearchCriteria;
import org.example.app.general.common.search.StringSearchOptions;

/**
 * {@link SearchCriteria} for {@link TaskItem}.
 */
public class TaskItemSearchCriteria extends SearchCriteria {

    private String title;

    private StringSearchOptions titleOptions;

    private Boolean completed;

    private Boolean starred;

    /**
     * @return the {@link TaskItem#getTitle() title}.
     */
    public String getTitle() {

        return this.title;
    }

    /**
     * @param title new value of {@link #getTitle()}.
     */
    public void setTitle(String title) {

        this.title = title;
    }

    /**
     * @return the {@link StringSearchOptions} for {@link #getTitle() title}.
     */
    public StringSearchOptions getTitleOptions() {

        return this.titleOptions;
    }

    /**
     * @param titleOptions new value of {@link #getTitleOptions()}.
     */
    public void setTitleOptions(StringSearchOptions titleOptions) {

        this.titleOptions = titleOptions;
    }

    /**
     * @return the {@link TaskItem#isCompleted() completed flag} or {@code null} if unspecified.
     */
    public Boolean getCompleted() {

        return this.completed;
    }

    /**
     * @param completed new value of {@link #getCompleted()}.
     */
    public void setCompleted(Boolean completed) {

        this.completed = completed;
    }

    /**
     * @return the {@link TaskItem#isStarred() starred flag} or {@code null} if unspecified.
     */
    public Boolean getStarred() {

        return this.starred;
    }

    /**
     * @param starred new value of {@link #getStarred()}.
     */
    public void setStarred(Boolean starred) {

        this.starred = starred;
    }

}