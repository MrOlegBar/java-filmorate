SELECT u.user_id,
		u.login,
        u.name,
        u.email,
        u.birthday,
        array_agg(DISTINCT uf.friend_id) AS friends,
        fs.status
FROM users AS u
LEFT JOIN user_friends AS uf ON u.user_id=uf.user_id
LEFT JOIN users AS f ON uf.friend_id=f.user_id
LEFT JOIN friendship_status AS fs ON uf.friendship_status_id=fs.friendship_status_id
WHERE u.user_id = 2
GROUP BY u.user_id, u.login, u.name, u.email, u.birthday, fs.status
ORDER BY u.user_id, fs.status DESC;