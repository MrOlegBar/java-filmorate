MERGE INTO USERS_FRIENDS
KEY (USER_ID, FRIEND_ID)
VALUES (?, ?, false);

DELETE
FROM USERS_FRIENDS
WHERE USER_ID = ? AND FRIEND_ID = ? AND FRIENDSHIP_STATUS = false;