-- First insert users since they are referenced by products
INSERT INTO users (id, name, age, bio, city, profile_image) VALUES
('user1', 'Sarah Johnson', 28, 'Book lover | Fantasy & Sci-Fi enthusiast | Coffee addict', 'New York', 'https://picsum.photos/200'),
('user2', 'Michael Chen', 32, 'Literature professor | Poetry lover | Always reading', 'San Francisco', 'https://picsum.photos/201'),
('user3', 'Emma Wilson', 25, 'YA fiction blogger | Bookstagram lover | Tea drinker', 'London', 'https://picsum.photos/202'),
('user4', 'James Miller', 30, 'Mystery & Thriller fan | Book collector | Writer', 'Chicago', 'https://picsum.photos/203'),
('user5', 'Olivia Martinez', 27, 'Historical fiction lover | Art history buff | Museum guide', 'Boston', 'https://picsum.photos/204');

-- Insert products (book posts)
INSERT INTO product (
    userid, 
    username, 
    title, 
    authors, 
    cover_images, 
    description, 
    subjects, 
    score,
    caption,
    category,
    post_image,
    likes,
    comments,
    created_at
) VALUES
(
    'user1',
    'Sarah Johnson',
    'The Name of the Wind',
    ARRAY['Patrick Rothfuss'],
    ARRAY['https://covers.openlibrary.org/b/id/8259823-L.jpg'],
    'The riveting first-person narrative of a young man who grows to become a notorious wizard.',
    ARRAY['Fantasy', 'Magic', 'Adventure'],
    95,
    'I''m finally reading this masterpiece! The prose is absolutely beautiful 📚✨',
    'currentlyReading',
    'https://picsum.photos/300',
    ARRAY['user2', 'user3'],
    '[{"id": "comment1", "userId": "user2", "text": "One of my favorites!", "timestamp": "2024-03-15T10:30:00", "likeCount": 2}]'::jsonb,
    '2024-03-15 10:00:00'
),
(
    'user2',
    'Michael Chen',
    'Dune',
    ARRAY['Frank Herbert'],
    ARRAY['https://covers.openlibrary.org/b/id/8788435-L.jpg'],
    'A stunning blend of adventure and mysticism, environmentalism and politics.',
    ARRAY['Science Fiction', 'Space Opera'],
    90,
    'Looking to trade this classic! Perfect condition 📚',
    'barter',
    'https://picsum.photos/301',
    ARRAY['user1', 'user4'],
    '[{"id": "comment2", "userId": "user4", "text": "Would love to trade! DMing you.", "timestamp": "2024-03-16T14:20:00", "likeCount": 1}]'::jsonb,
    '2024-03-16 14:00:00'
),
(
    'user3',
    'Emma Wilson',
    'Six of Crows',
    ARRAY['Leigh Bardugo'],
    ARRAY['https://covers.openlibrary.org/b/id/8314275-L.jpg'],
    'A gripping fantasy heist story set in the dangerous criminal underworld.',
    ARRAY['Fantasy', 'Young Adult', 'Heist'],
    88,
    'This book has the best character development! Who''s your favorite? 🦅',
    'favourite',
    'https://picsum.photos/302',
    ARRAY['user1', 'user5'],
    '[{"id": "comment3", "userId": "user5", "text": "Kaz is absolutely brilliant!", "timestamp": "2024-03-17T09:15:00", "likeCount": 3}]'::jsonb,
    '2024-03-17 09:00:00'
),
(
    'user4',
    'James Miller',
    'The Silent Patient',
    ARRAY['Alex Michaelides'],
    ARRAY['https://covers.openlibrary.org/b/id/8915057-L.jpg'],
    'A psychological thriller about a woman''s act of violence against her husband.',
    ARRAY['Thriller', 'Mystery', 'Psychological'],
    92,
    'Just finished this in one sitting! My mind is blown 🤯',
    'favourite',
    'https://picsum.photos/303',
    ARRAY['user2', 'user5'],
    '[{"id": "comment4", "userId": "user2", "text": "That''s ending though!", "timestamp": "2024-03-18T20:45:00", "likeCount": 4}]'::jsonb,
    '2024-03-18 20:30:00'
),
(
    'user5',
    'Olivia Martinez',
    'The Seven Husbands of Evelyn Hugo',
    ARRAY['Taylor Jenkins Reid'],
    ARRAY['https://covers.openlibrary.org/b/id/8859124-L.jpg'],
    'A mesmerizing novel about Old Hollywood''s glamour and scandal.',
    ARRAY['Historical Fiction', 'Romance', 'LGBTQ+'],
    94,
    'Looking to swap this gem! Any historical fiction lovers? 📚💫',
    'barter',
    'https://picsum.photos/304',
    ARRAY['user1', 'user3'],
    '[{"id": "comment5", "userId": "user3", "text": "This is my favorite book of the year!", "timestamp": "2024-03-19T15:10:00", "likeCount": 5}]'::jsonb,
    '2024-03-19 15:00:00'
);

-- Update user arrays with related data
UPDATE users 
SET products = ARRAY[(SELECT id FROM product WHERE userid = 'user1')]::uuid[]
WHERE id = 'user1';

UPDATE users 
SET products = ARRAY[(SELECT id FROM product WHERE userid = 'user2')]::uuid[]
WHERE id = 'user2';

UPDATE users 
SET products = ARRAY[(SELECT id FROM product WHERE userid = 'user3')]::uuid[]
WHERE id = 'user3';

UPDATE users 
SET products = ARRAY[(SELECT id FROM product WHERE userid = 'user4')]::uuid[]
WHERE id = 'user4';

UPDATE users 
SET products = ARRAY[(SELECT id FROM product WHERE userid = 'user5')]::uuid[]
WHERE id = 'user5';

-- Set up some book buddies and connections
UPDATE users
SET 
    book_buddies = '[{"userId": "user2", "timestamp": "2024-03-15T10:00:00", "commonSubjectCount": 3}]'::jsonb,
    connections = ARRAY['user2', 'user3']
WHERE id = 'user1';

UPDATE users
SET 
    book_buddies = '[{"userId": "user1", "timestamp": "2024-03-15T10:00:00", "commonSubjectCount": 3}]'::jsonb,
    connections = ARRAY['user1', 'user4']
WHERE id = 'user2';

UPDATE users
SET 
    book_buddies = '[{"userId": "user1", "timestamp": "2024-03-19T15:00:00", "commonSubjectCount": 2}]'::jsonb,
    connections = ARRAY['user1', 'user3']
WHERE id = 'user5'; 