UPDATE USERS
SET LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ?
WHERE USER_ID = ?;

SELECT *
FROM USERS_FRIENDS
WHERE USER_ID = ? AND FRIENDSHIP_STATUS IN (false, true);

DELETE
FROM USERS_FRIENDS
WHERE USER_ID = ?;

MERGE INTO USERS_FRIENDS
KEY (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS)
VALUES (?, ?, ?);