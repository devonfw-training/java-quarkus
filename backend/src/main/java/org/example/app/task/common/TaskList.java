package org.example.app.task.common;

import org.example.app.general.common.ApplicationEntity;

public interface TaskList extends ApplicationEntity {

    /**
     * @return the title of this {@link TaskList}. Gives a brief summary to describe this list of tasks (e.g. "Shopping
     *         list", "Packing list" or "Things to buy at construction market").
     */
    String getTitle();

    /**
     * @param title new value of {@link #getTitle()}.
     */
    void setTitle(String title);
}

