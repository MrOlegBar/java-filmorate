SELECT f.user_id AS friend_id,
		f.login AS friend_login,
        f.name AS friend_name,
        f.email AS friend_email,
        f.birthday AS friend_birthday,
        fs.status AS friendship_status
FROM users AS u
LEFT JOIN user_friends AS uf ON u.user_id=uf.user_id
LEFT JOIN users AS f ON uf.friend_id=f.user_id
LEFT JOIN friendship_status AS fs ON uf.friendship_status_id=fs.friendship_status_id
WHERE uf.user_id = 2
GROUP BY u.user_id, u.login, u.name, u.email, u.birthday, f.user_id, f.login, f.name, f.email, f.birthday, fs.status
ORDER BY fs.status DESC, f.user_id;