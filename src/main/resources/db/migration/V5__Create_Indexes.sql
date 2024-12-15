CREATE index index_userid
ON product (userid);

CREATE index index_title
on product (lower(title));

CREATE index index_subjects
on product using GIN(subjects);