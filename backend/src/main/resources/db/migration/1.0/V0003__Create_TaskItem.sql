CREATE TABLE TASK_ITEM (
  ID                      BIGINT NOT NULL,
  VERSION                 INT NOT NULL,
  TITLE                   VARCHAR(64) NOT NULL,
  COMPLETED               BOOLEAN,
  STARRED                 BOOLEAN,
  DEADLINE                TIMESTAMP,
  LIST_ID                 BIGINT,
  CONSTRAINT PK_TASK_ITEM PRIMARY KEY (ID),
  CONSTRAINT FK_ITEM_LIST FOREIGN KEY (LIST_ID) REFERENCES TASK_LIST(ID)
)