SELECT *
FROM USERS
WHERE USER_ID IN (SELECT FRIEND_ID
                  FROM (SELECT USER_ID, FRIEND_ID
                        FROM USERS_FRIENDS
                        WHERE USER_ID = ?)
                  WHERE FRIEND_ID IN (SELECT FRIEND_ID
                                      FROM USERS_FRIENDS
                                      WHERE USER_ID = ?))